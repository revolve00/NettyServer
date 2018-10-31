package com.aiparker.net.srv;

import java.net.SocketAddress;

/**
 * 
 * @author Hypnos
 * 定义server 标准接口
 */
public interface SrvAcceptor {
	
	/**
	 * 
	 * @return
	 */
	SocketAddress localAddress();

	/**
	 * 启动
	 * @throws InterruptedException
	 */
	void start() throws InterruptedException;
	
	/**
	 * 关闭
	 */
	void shutdownGracefully();

	/**
	 * 
	 * @param sync
	 * @throws InterruptedException
	 */
	void start(boolean sync) throws InterruptedException;
}
