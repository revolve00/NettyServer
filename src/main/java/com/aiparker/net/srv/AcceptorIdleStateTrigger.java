package com.aiparker.net.srv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.domain.NettyIdleEvent;
import com.aiparker.net.domain.enums.NettyEventType;
import com.aiparker.net.domain.enums.NettyIdleType;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.srv.NettyEventExecuterAcceptor.NettyEventExecuter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 链路空闲监测
 * 
 * @author Hypnos
 *
 */
@Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AcceptorIdleStateTrigger.class);

    private final NettyEventExecuter nettyEventExecuter;

    public AcceptorIdleStateTrigger(NettyEventExecuter nettyEventExecuter) {
        this.nettyEventExecuter = nettyEventExecuter;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if(ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY) == null){
            	ctx.close();
            	return;
            }
            switch (e.state()) {
            case READER_IDLE:
                //logger.info(ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get().toString() + ctx.channel().remoteAddress() +  "==============READER_IDLE==============");
                nettyEventExecuter.putNettyEvent(new NettyIdleEvent(NettyEventType.IDLE, NettyIdleType.READER_IDLE, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
                break;
            case WRITER_IDLE:
                //logger.info(ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get().toString() + ctx.channel().remoteAddress() +  "==============WRITER_IDLE==============");
                nettyEventExecuter.putNettyEvent(new NettyIdleEvent(NettyEventType.IDLE, NettyIdleType.WRITER_IDLE, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
                break;
            case ALL_IDLE:
                //logger.info(ctx.channel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY).get().toString() + ctx.channel().remoteAddress() + "==============ALL_IDLE==============");
                nettyEventExecuter.putNettyEvent(new NettyIdleEvent(NettyEventType.IDLE, NettyIdleType.ALL_IDLE, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
                ctx.close();
                break;
            }
        }
    }
}
