package com.aiparker.net.event.listener;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.Executors;

import com.aiparker.net.domain.ClientChannelMap;

import io.netty.channel.ChannelId;
import io.netty.util.concurrent.GlobalEventExecutor;

public abstract class AbstractAttributeMap {
    /*
     * channel 里的通道明细对象
     */
	public static final AttributeKey<Object> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");
	
	/*
	 * channel 里的ccid对象
	 */
	public static final AttributeKey<Object> NETTY_CHANNEL_CCID = AttributeKey.valueOf("netty.channel.ccid");
	

	/*
	 * channel 里的lbs对象
	 */
	public static final AttributeKey<Object> NETTY_CHANNEL_LBS = AttributeKey.valueOf("netty.channel.lbs");
	
	/*
	 * channel 里的异常数据总数
	 */
	public static final AttributeKey<Float> NETTY_CHANNEL_BIT_ERROR = AttributeKey.valueOf("netty.channel.bit.error");
	
	/*
	 * channel 里的上行数据总数
	 */
	public static final AttributeKey<Float> NETTY_CHANNEL_BIT_UP = AttributeKey.valueOf("netty.channel.bit.up");
	
	/*
	 * channel 里的下行数据总数
	 */
	public static final AttributeKey<Float> NETTY_CHANNEL_BIT_DOWN = AttributeKey.valueOf("netty.channel.bit.down");
	
	/*
	 * 所有连接集合
	 */
	public static final ChannelGroup allChannels = new DefaultChannelGroup("channelGroup", GlobalEventExecutor.INSTANCE);
	
	/*
	 * 连接与channelID的集合
	 */
	public static final Map<String, ChannelId> client_ip_channelID_map = ClientChannelMap.instance();
	
	/*
	 * 总体流量监控
	 */
	public static final GlobalTrafficShapingHandler trafficHandler = new GlobalTrafficShapingHandler(Executors.newScheduledThreadPool(1),1000);
	
	
	/*
	 * 通道流量监控
	 */
	public static final ChannelTrafficShapingHandler channelTrafficHandler = new ChannelTrafficShapingHandler(1000);
	

}
