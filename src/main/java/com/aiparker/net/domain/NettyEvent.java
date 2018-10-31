package com.aiparker.net.domain;

import java.io.Serializable;
import java.util.Date;

import com.aiparker.net.domain.enums.NettyEventType;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class NettyEvent implements Serializable {

    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     *
     * @since 1.0.0
     */

    private static final long serialVersionUID = 1L;
    private final NettyEventType type;
    private final String remoteAddr;
    private final Channel channel;
    private final Long lastReportTime;
    private final AttributeKey<Object> netty_channel_key;
    private Object message;

    public NettyEvent(NettyEventType type, String remoteAddr, Channel channel, AttributeKey<Object> netty_channel_key) {
        this(type, remoteAddr, channel, null, netty_channel_key);
    }

    public NettyEvent(NettyEventType type, String remoteAddr, Channel channel, Object message, AttributeKey<Object> netty_channel_key) {
        this.type = type;
        this.remoteAddr = remoteAddr;
        this.channel = channel;
        this.lastReportTime = new Date().getTime();
        this.message = message;
        this.netty_channel_key = netty_channel_key;
    }
    
    public Attribute<Object> getAttribute(){
        return channel.attr(netty_channel_key);
    }

    public Long getLastReportTime() {
        return lastReportTime;
    }

    public NettyEventType getType() {
        return type;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public Channel getChannel() {
        return channel;
    }

    public Object getMessage() {
        return message;
    }

    public String getLongText() {
        return channel.id().asLongText();
    }
    
    @Override
    public String toString() {
        return "NettyEvent [type=" + type + "], [remoteAddr=" + remoteAddr + "], [channel=" + channel + "],[lastReportTime=" + lastReportTime + "],[message = " + message + "]";
    }
}
