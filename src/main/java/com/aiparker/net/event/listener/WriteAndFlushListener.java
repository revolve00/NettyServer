/**
 * 系统项目名称
 * com.localpark.local.net.listener
 * WriteAndFlushListener.java
 * 
 * 2016年12月8日-下午5:12:23
 * 2016爱泊客公司-版权所有
 *
 */
package com.aiparker.net.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 *
 * WriteAndFlushListener
 * 
 * @author Hypnos
 * 
 *         2016年12月8日 下午5:12:23
 * 
 * @version 1.0.0
 *
 */
public class WriteAndFlushListener implements ChannelFutureListener {

	private static final Logger logger = LoggerFactory.getLogger(WriteAndFlushListener.class);

	private static WriteAndFlushListener instance = null;
	
	private String class_name = null;

	private WriteAndFlushListener() {

	}

	private String getClass_name() {
		return class_name;
	}

	private void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public static WriteAndFlushListener getInstance(String class_name) {
		if (instance == null) {
			synchronized (logger) {
				instance = new WriteAndFlushListener();
			}
		}
		instance.setClass_name(class_name);
		return instance;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		logger.debug("listener:{},{}",instance.getClass_name(),future);
		if (!future.isSuccess()) {
			logger.error("send fail,reason is { " + future.cause().getMessage() + "}");
		}

	}

}
