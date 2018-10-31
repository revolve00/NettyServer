/**
 * 系统项目名称
 * com.localpark.local.net.codec
 * HexDecode.java
 * 
 * 2016年11月16日-上午10:58:23
 * 2016爱泊客公司-版权所有
 *
 */
package com.aiparker.net.codec;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * 
 *
 * MarshallingCodeCFactory
 * 
 * @author Hypnos
 * 
 *         2016年11月16日 上午11:02:59
 * 
 * @version 1.0.0
 *
 */
public final class MarshallingCodeCFactory {

    /**
     * 创建Jboss Marshalling解码器MarshallingDecoder
     * 
     * @return
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        final UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        final MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
        return decoder;
    }

    /**
     * 创建Jboss Marshalling编码器MarshallingEncoder
     * 
     * @return
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        final MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        final MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
}
