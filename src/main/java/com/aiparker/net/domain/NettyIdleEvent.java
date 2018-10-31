package com.aiparker.net.domain;

import com.aiparker.net.domain.enums.NettyEventType;
import com.aiparker.net.domain.enums.NettyIdleType;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class NettyIdleEvent extends NettyEvent {

    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     *
     * @since 1.0.0
     */

    private static final long serialVersionUID = -890132419665270007L;

    private final NettyIdleType idletype;

    public NettyIdleEvent(NettyEventType type, NettyIdleType idletype, String remoteAddr, Channel channel, AttributeKey<Object> netty_channel_key) {
        super(type, remoteAddr, channel, netty_channel_key);
        this.idletype = idletype;
    }

    public NettyIdleType getIdletype() {
        return idletype;
    }

    @Override
    public String toString() {
        return super.toString() + ",[idletype=" + idletype + "]";
    }

}
