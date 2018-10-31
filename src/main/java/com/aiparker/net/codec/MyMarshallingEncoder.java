/**
 * 系统项目名称
 * com.aiparker.net.codec
 * MyMarshallingEncoder.java
 * 
 * 2017年1月18日-下午1:58:33
 * 2017爱泊客公司-版权所有
 * @version 1.0.0
 */
package com.aiparker.net.codec;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 *
 * MyMarshallingEncoder
 * 
 * @author Hypnos
 * 
 * 2017年1月18日 下午1:58:33
 * 
 * @version 1.0.0
 *
 */
public class MyMarshallingEncoder extends MarshallingEncoder{
    
    final static MarshallerProvider provider;
    static{
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
    }
    
    public MyMarshallingEncoder(){
        super(provider);
    }

}
