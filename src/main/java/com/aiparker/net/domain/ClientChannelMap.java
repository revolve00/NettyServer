package com.aiparker.net.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelId;

public class ClientChannelMap {
	

	/*
	 * 连接与channelID的集合
	 */
	private static Map<String, ChannelId> client_ip_channelID_map;
	
	
	private ClientChannelMap(){
		
	}
	
	public synchronized static Map<String, ChannelId> instance(){
		if(client_ip_channelID_map == null){
			client_ip_channelID_map = new ConcurrentHashMap<String, ChannelId>();
		}
		return client_ip_channelID_map;
	}
}
