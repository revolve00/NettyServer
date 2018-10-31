package com.aiparker.net.common;

/**
 * 
 *
 * NativeSupport
 * 
 * @author Hypnos
 * 
 *         2016年12月3日 上午10:38:27
 * 
 * @version 1.0.0
 *
 */
public final class NativeSupport {

    private static final boolean SUPPORT_NATIVE_ET;
    static {
        boolean epoll;
        try {
            Class.forName("io.netty.channel.epoll.Native");
            epoll = true;
        }
        catch (final Throwable e) {
            epoll = false;
        }
        SUPPORT_NATIVE_ET = epoll;
    }

    /**
     * The native socket transport for Linux using JNI.
     */
    public static boolean isSupportNativeET() {
        return SUPPORT_NATIVE_ET;
    }
}
