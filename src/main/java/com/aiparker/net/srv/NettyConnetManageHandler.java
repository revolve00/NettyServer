package com.aiparker.net.srv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.domain.NettyEvent;
import com.aiparker.net.domain.enums.NettyEventType;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.event.listener.WriteAndFlushListener;
import com.aiparker.net.srv.NettyEventExecuterAcceptor.NettyEventExecuter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * ChannelDuplexHandler (InAndOut) 所有事件
 *
 * NettyConnetManageHandler
 * 
 * @author Hypnos
 * 
 *         2016年12月14日 下午4:45:18
 * 
 * @version 1.0.0
 *
 */
@Sharable
public class NettyConnetManageHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyConnetManageHandler.class);

    private final NettyEventExecuter nettyEventExecuter;

    public NettyConnetManageHandler(NettyEventExecuter nettyEventExecuter) {
        this.nettyEventExecuter = nettyEventExecuter;
    }

    /**
     * 
     * channelActive(管道从不活跃状态 转到 活跃状态 触发) (这里描述这个方法适用条件 – 可选)
     * 
     * @param ctx
     *            void
     * @throws Exception
     * @exception @since
     *                1.0.0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("==============channelActive==============");
        nettyEventExecuter.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
    }

    /**
     * 
     * channelInactive(管道从活跃状态 转到 不活跃状态 并且生命周期结束) (这里描述这个方法适用条件 – 可选)
     * 
     * @param ctx
     *            void
     * @throws Exception
     * @exception @since
     *                1.0.0
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("==============channelInactive==============");
        nettyEventExecuter.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
    }

    /**
     * 
     * exceptionCaught(异常) (这里描述这个方法适用条件 – 可选)
     * 
     * @param ctx
     * @param cause
     * @throws Exception
     *             void
     * @exception @since
     *                1.0.0
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("==============exceptionCaught==============",cause);
        nettyEventExecuter.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, ctx.channel().remoteAddress().toString(), ctx.channel(), AbstractAttributeMap.NETTY_CHANNEL_KEY));
    }
}
