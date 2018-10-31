package com.aiparker.net.codec;


import com.aiparker.net.event.listener.AbstractAttributeMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.Attribute;

/**
 * 
 *
 * HexEncoder
 * 
 * @author Hypnos
 * 
 *         2016年12月3日 上午10:38:17
 * 
 * @version 1.0.0
 *
 */
public class HexEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(final ChannelHandlerContext ctx, final byte[] msg, final ByteBuf out) throws Exception {
    	final Attribute<Float> bit_down = ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_BIT_DOWN);
    	bit_down.set("null".equals(bit_down.toString()) ? 0F :bit_down.get() + msg.length);
        out.writeBytes(msg);
    }

}
