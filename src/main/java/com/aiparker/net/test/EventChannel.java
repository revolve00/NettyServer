package com.aiparker.net.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.domain.NettyEvent;
import com.aiparker.net.event.listener.AbstractChannelEventListener;


public class EventChannel extends AbstractChannelEventListener {

	private static final Logger logger = LoggerFactory.getLogger(EventChannel.class);

	@Override
	public void onChannelConnect(NettyEvent event) {
		logger.info("onChannel:{}", event);

	}

	@Override
	public void onChannelClose(NettyEvent event) {
		logger.info("onChannelClose:{}", event);

	}

	@Override
	public void onChannelException(NettyEvent event) {

		logger.info("onChannelException:{}", event);

	}

	@Override
	public void onChannelIdle(NettyEvent event) {

		logger.info("onChannelIdle:{}", event);

	}

	@Override
	public void onChannelRead(NettyEvent event) {

		logger.info("onChannelRead:{}", event);
	}

	@Override
	public void onChannelWrite(NettyEvent event) {

		logger.info("onChannelWrite:{}", event);

	}

}
