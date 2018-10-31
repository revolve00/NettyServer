package com.aiparker.net.codec;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.constants.Device_Data_Constants;
import com.aiparker.net.domain.JsonMessage;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.utils.QHex;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;

public class JSONHexDecode extends ByteToMessageDecoder {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONHexDecode.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int size = in.readableBytes();
		ByteBuffer buf = in.nioBuffer();
		byte[] read_byte = new byte[size];
		buf.get(read_byte);
		logger.debug("[服务端解码:" + size + ":" + QHex.bytesToHexString(read_byte) + "]");
		/**
		 * 如果缓存区内容小于10字节 等待下一包数据读取
		 */
		if (in.readableBytes() < 10) {
			return;
		}
		
		final Attribute<Float> bit_error = ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_BIT_ERROR);
		final Attribute<Float> bit_up = ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_BIT_UP);
		
		/**
		 * 查找0x55 数据的位置 截取sync 头
		 */
		int index = in.bytesBefore((byte) 0x5c);
		
		if (index > 0) {
			in.skipBytes(index);
		}
		/**
		 * 标记当前读节点
		 */
		in.markReaderIndex();
		
		JsonMessage data = new JsonMessage();
		
		in.readBytes(data.getByte_header());
		
		if(!Arrays.equals(data.getByte_header(), Device_Data_Constants.BYTE_HEADER_JSON)){
			logger.error("包头验证错误" + ctx.channel().toString() + ":" + ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get() + ":" + QHex.bytesToHexString(read_byte));
			data = null;
			
			bit_error.set("null".equals(bit_error.toString()) ? 0F : bit_error.get() + size);
			return;
		}
		
		in.readBytes(data.getByte_length());
		
		/**
		 * 数据长度转换 判断剩下的缓存区的内容是否足够当前数据包 不足reset 继续读下一包补全
		 */
		final int data_length = QHex.bytesToInt(data.getByte_length(),0);
		
		if (in.readableBytes() < data_length) {
			in.resetReaderIndex();
			logger.error("长度验证错误" + data.toString());
			data = null;
			return;
		}
		
		
		/**
		 * 统计上行流量
		 */
		bit_up.set("null".equals(bit_up.toString()) ? 0F : bit_up.get() + size);
		
		/**
		 * 初始化data
		 */
		final byte[] byte_data = new byte[data_length];
		in.readBytes(byte_data);
		
		data.setMessage(new String(byte_data,"utf-8"));
		/**
		 * 读取etx
		 */
		in.readBytes(data.getByte_end());
		
		/**
		 * 数据传输
		 */
		out.add(data);
	}

}
