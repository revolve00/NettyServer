package com.aiparker.net.event.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.domain.NettyEvent;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

/**
 * 定义回调默认实现
 *
 * AbstractChannelEventListener
 * 
 * @author Hypnos
 * 
 *         2017年1月17日 上午10:46:03
 * 
 * @version 1.0.0
 *
 */
public abstract class AbstractChannelEventListener implements ChannelEventListener {

	private static final Logger logger = LoggerFactory.getLogger(AbstractChannelEventListener.class);

	@Override
	public void onChannelConnect(NettyEvent event) {
		AbstractAttributeMap.allChannels.add(event.getChannel());
		AbstractAttributeMap.client_ip_channelID_map.put(event.getLongText(), event.getChannel().id());
		logger.debug("AbstractChannelEventListener :::: onChannelConnect: event" + event + "attr:"
				+ event.getChannel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY) + "allChannels:"
				+ AbstractAttributeMap.allChannels);
	}

	@Override
	public void onChannelClose(NettyEvent event) {
		AbstractAttributeMap.client_ip_channelID_map.remove(event.getLongText());
		logger.debug("AbstractChannelEventListener :::: onChannelClose: event" + event + "attr:"
				+ event.getChannel().attr(AbstractAttributeMap.NETTY_CHANNEL_KEY) + "allChannels:"
				+ AbstractAttributeMap.allChannels);
	}

	@Override
	public boolean toChannelWrite(NettyEvent event, Object msg) {
		Channel channel = event.getChannel();
		if (channel.isActive()) {
			channel.writeAndFlush(msg).addListener(WriteAndFlushListener.getInstance(this.getClass().getName()));
			return true;
		}
		return false;

	}

	@Override
	public ChannelGroup getChannelGroup() {
		return AbstractAttributeMap.allChannels;
	}

	@Override
	public Map<String, ChannelId> getChannelID_map() {
		return AbstractAttributeMap.client_ip_channelID_map;
	}
}
