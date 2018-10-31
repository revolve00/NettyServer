package com.aiparker.net.codec;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.constants.Device_Data_Constants;
import com.aiparker.net.domain.HexMessage;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.utils.QDESTools;
import com.aiparker.net.utils.QHex;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;

/**
 * 
 *
 * HexDecode
 * 
 * @author Hypnos
 * 
 *         2016年12月3日 上午10:38:07
 * 
 * @version 1.0.0
 *
 */
public class HexDecode extends ByteToMessageDecoder {

	private static final Logger logger = LoggerFactory.getLogger(HexDecode.class);

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		final int size = in.readableBytes();
		final ByteBuffer buf = in.nioBuffer();
		final byte[] read_byte = new byte[size];
		buf.get(read_byte);
		logger.debug("size" + size + ":" + QHex.bytesToHexString(read_byte));

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
		if (QHex.bytesToHexString(read_byte).toUpperCase().indexOf("55555555") >= 0) {
			final int index = in.bytesBefore((byte) 0x55);
			in.skipBytes(index);
		} else if (QHex.bytesToHexString(read_byte).toUpperCase().indexOf("7573722E636E230D0A") >= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("组包{}", QHex.bytesToHexString(read_byte));
			}
			final int ccid_index = in.bytesBefore((byte) 0x75);

			in.skipBytes(ccid_index);
			final int index_0D = in.bytesBefore((byte) 0x0D);
			byte[] header = new byte[index_0D + 2];
			in.readBytes(header);

			if (logger.isDebugEnabled()) {
				logger.debug("header{}", QHex.bytesToHexString(header));
			}

			final int index_0D_2 = in.bytesBefore((byte) 0x0D);
			byte[] body = new byte[index_0D_2 + 2];
			in.readBytes(body);

			if (logger.isDebugEnabled()) {
				logger.debug("body{}", QHex.bytesToHexString(body));
			}
			final Attribute<Object> attr_ccid = ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_CCID);

			final Attribute<Object> attr_lbs = ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_LBS);
			
			
			if(attr_ccid == null || Device_Data_Constants.CHANNEL_TABS_SENDOVER.equals(attr_ccid.get())){
				attr_ccid.set(new String(body));
				if (logger.isDebugEnabled()) {
					logger.debug("attr{}", attr_ccid);
				}
			}else if(attr_lbs == null || Device_Data_Constants.CHANNEL_TABS_SENDOVER.equals(attr_lbs.get().toString())){
				attr_lbs.set(new String(body));
				if (logger.isDebugEnabled()) {
					logger.debug("attr{}", attr_lbs);
				}
			}
			return;
		} else {
			logger.error("异常数据" + ctx.channel().toString() + ":"
					+ ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get() + ":" + in.readableBytes());
			bit_error.set("null".equals(bit_error.toString()) ? 0F : bit_error.get() + size);
			return;
		}
		/**
		 * 标记当前读节点
		 */
		in.markReaderIndex();

		/**
		 * 初始化包实例
		 */
		HexMessage message = new HexMessage();

		/**
		 * 读取sync
		 */
		in.readBytes(message.getByte_sync());
		/**
		 * 验证sync是否正确 不正确继续读
		 */
		if (!Arrays.equals(message.getByte_sync(), Device_Data_Constants.BYTE_SYNC_YES_ENRCY_AIPARKER)
				&& !Arrays.equals(message.getByte_sync(), Device_Data_Constants.BYTE_SYNC_NO_ENRCY_AIPARKER)) {
			logger.error("sync验证错误" + ctx.channel().toString() + ":"
					+ ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get() + ":" + in.readableBytes());
			message = null;
			
			bit_error.set("null".equals(bit_error.toString()) ? 0F : bit_error.get() + size);
			
			return;
		}

		/**
		 * 读取header
		 */
		in.readBytes(message.getByte_header());
		/**
		 * 读取length
		 */
		in.readBytes(message.getByte_length());
		/**
		 * 读取cmd
		 */
		in.readBytes(message.getByte_cmd());
		/**
		 * 数据长度转换 判断剩下的缓存区的内容是否足够当前数据包 不足reset 继续读下一包补全
		 */
		final int data_length = QHex.getInt(message.getByte_length());
		if (in.readableBytes() < data_length) {
			in.resetReaderIndex();
			logger.error("长度验证错误" + message.toString());
			message = null;
			return;
		}
		
		/**
		 * 统计上行流量
		 */
		bit_up.set("null".equals(bit_up.toString()) ? 0F : bit_up.get() + size);
		
		/**
		 * 初始化data
		 */
		final byte[] byte_data = new byte[data_length - 2];
		in.readBytes(byte_data);
		message.setByte_data(byte_data);
		/**
		 * 读取crc
		 */
		in.readBytes(message.getByte_crc());
		/**
		 * 读取etx
		 */
		in.readBytes(message.getByte_etx());
		/**
		 * 验证包数据
		 */
		if (!QHex.check_data_integrity(message)) {
			logger.error("数据包验证错误" + message.toString());
			return;
		}
		/**
		 * 解密数据
		 */
		if (Arrays.equals(message.getByte_sync(), Device_Data_Constants.BYTE_SYNC_YES_ENRCY_AIPARKER)) {
			message.setByte_data_device(
					QDESTools.decrypt(message.getByte_data(), Device_Data_Constants.BYTE_SYNC_YES_ENRCY_AIPARKER_KEY));
		} else {
			message.setByte_data_device(message.getByte_data());
		}

		/**
		 * 数据传输
		 */
		out.add(message);
	}

}
