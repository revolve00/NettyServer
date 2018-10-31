package com.aiparker.net.event.listener;

import java.util.Map;

import com.aiparker.net.domain.NettyEvent;

import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

/**
 * 链路回调抽象接口
 *
 * ChannelEventListener
 * 
 * @author Hypnos
 * 
 * 2017年1月17日 下午2:36:46
 * 
 * @version 1.0.0
 *
 */
public interface ChannelEventListener {
	void onChannelConnect(final NettyEvent event);

	void onChannelClose(final NettyEvent event);

	void onChannelException(final NettyEvent event);

	void onChannelIdle(final NettyEvent event);

	void onChannelRead(final NettyEvent event);

	void onChannelWrite(final NettyEvent event);
	
	boolean toChannelWrite(final NettyEvent event,Object msg);
	
	ChannelGroup getChannelGroup();
	
	Map<String,ChannelId> getChannelID_map(); 
}
