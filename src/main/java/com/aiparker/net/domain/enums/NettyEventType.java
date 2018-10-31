package com.aiparker.net.domain.enums;

/**
 * Netty产生的事件类型
 * @author Hypnos
 *
 */
public enum NettyEventType {
	CONNECT, 	// 连接
	CLOSE, 		// 关闭
	READ,		// 读取
	WRITE,		// 写入
	IDLE, 		// 空闲
	EXCEPTION 	// 异常
	
	
}
