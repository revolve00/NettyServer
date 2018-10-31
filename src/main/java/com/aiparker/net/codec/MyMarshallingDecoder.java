/**
 * 系统项目名称
 * com.aiparker.net.codec
 * MarshallingDecoder.java
 * 
 * 2017年1月18日-下午1:53:00
 * 2017爱泊客公司-版权所有
 * @version 1.0.0
 */
package com.aiparker.net.codec;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 *
 * MarshallingDecoder
 * 
 * @author Hypnos
 * 
 * 2017年1月18日 下午1:53:00
 * 
 * @version 1.0.0
 *
 */
public class MyMarshallingDecoder extends MarshallingDecoder{
    
    final static UnmarshallerProvider provider;
    
    static{
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
    }
    public MyMarshallingDecoder(){
        super(provider, 1024);
    }
}
