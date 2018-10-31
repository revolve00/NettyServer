package com.aiparker.net.srv;

import com.aiparker.net.domain.NettyEvent;
import com.aiparker.net.domain.enums.NettyEventType;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.srv.NettyEventExecuterAcceptor.NettyEventExecuter;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class NettyReadWriteHandler extends ChannelDuplexHandler {

    private final NettyEventExecuter nettyEventExecuter;

    public NettyReadWriteHandler(NettyEventExecuter nettyEventExecuter) {
        this.nettyEventExecuter = nettyEventExecuter;
    }

    /**
     * 
     * channelRead(读取数据) (这里描述这个方法适用条件 – 可选)
     * 
     * @param ctx
     * @param msg
     * @throws Exception
     *             void
     * @exception @since
     *                1.0.0
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        nettyEventExecuter.putNettyEvent(new NettyEvent(NettyEventType.READ, ctx.channel().remoteAddress().toString(), ctx.channel(), msg, AbstractAttributeMap.NETTY_CHANNEL_KEY));
        
    }

}
