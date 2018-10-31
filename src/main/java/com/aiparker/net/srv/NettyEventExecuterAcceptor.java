package com.aiparker.net.srv;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiparker.net.common.ServiceThread;
import com.aiparker.net.domain.NettyEvent;
import com.aiparker.net.event.listener.AbstractAttributeMap;
import com.aiparker.net.event.listener.ChannelEventListener;

import io.netty.channel.ChannelId;

/**
 * 观察者模式  消息制造队列初始化
 *
 * NettyEventExecuterAcceptor
 * 
 * @author Hypnos
 * 
 * 2017年1月17日 上午10:36:55
 * 
 * @version 1.0.0
 *
 */
public abstract class NettyEventExecuterAcceptor extends AbstractAttributeMap {

	private static final Logger logger = LoggerFactory.getLogger(NettyEventExecuterAcceptor.class);
	
	/*
	 * 任务队列处理线程
	 */
	protected final NettyEventExecuter nettyEventExecuter = new NettyEventExecuter();

	class NettyEventExecuter extends ServiceThread {
	    /*
	     * 队列初始化
	     */
		private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
		/*
		 * 队列最大消息数
		 */
		private final int MaxSize = 50000;
		
		private final long index = new Random().nextLong();
		
		/*
		 * 队列消息添加
		 */
		public void putNettyEvent(final NettyEvent event) {
			if (this.eventQueue.size() <= MaxSize) {
				this.eventQueue.add(event);
			} else {
				logger.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(),
						event.toString());
			}
		}
		
		/*
		 * (non-Javadoc) 队列消息消费
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			logger.info(this.getServiceName() + " service started");
			/*
			 * 获得回调对象
			 */
			final ChannelEventListener listener = NettyEventExecuterAcceptor.this.getChannelEventListener();
			
			while (!this.isStoped()) {
				try {
				    //获得消息  （1000毫秒超时时间）
					NettyEvent event = this.eventQueue.poll(1000, TimeUnit.MILLISECONDS);
					if (event != null && listener != null) {
						switch (event.getType()) {
						case IDLE:
							listener.onChannelIdle(event);
							break;
						case READ:
							listener.onChannelRead(event);
							break;
						case WRITE:
							listener.onChannelWrite(event);
							break;
						case CLOSE:
							listener.onChannelClose(event);
							break;
						case CONNECT:
							listener.onChannelConnect(event);
							break;
						case EXCEPTION:
							listener.onChannelException(event);
							break;
						default:
						    logger.warn(this.getServiceName() + "no type of event" + event);
							break;
						}
					}
				} catch (Exception e) {
					logger.warn(this.getServiceName() + " service has exception. ", e);
				}
			}
			logger.info(this.getServiceName() + " service end");
		}

		@Override
		public String getServiceName() {
			return NettyEventExecuter.class.toString() + index;
		}

	}
	
	//实现获得通讯事件监听方法
	protected abstract ChannelEventListener getChannelEventListener();
	
	//获得channelid和ChannelId对象的对应关系
	protected abstract Map<String, ChannelId> getClient_ip_ChannelID_map();
}
