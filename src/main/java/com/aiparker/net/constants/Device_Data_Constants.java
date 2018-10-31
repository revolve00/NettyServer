package com.aiparker.net.constants;

/**
 * 
 *
 * Device_Data_Constants
 * 
 * @author Hypnos
 * 
 *         2016年12月3日 上午10:38:48
 * 
 * @version 1.0.0
 *
 */
public class Device_Data_Constants {
	
	//JSON头
	public static final byte[] BYTE_HEADER_JSON = new byte[] { (byte)0x5C, (byte)0x5C };
    
    //加密同步码
    public static final byte[] BYTE_SYNC_YES_ENRCY_AIPARKER = new byte[] { (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0xA5 };
    
    //非加密同步码
    public static final byte[] BYTE_SYNC_NO_ENRCY_AIPARKER = new byte[] { (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0x55 };
    
    //加密解密key
    public static final byte[] BYTE_SYNC_YES_ENRCY_AIPARKER_KEY = new byte[] { (byte) 0x41, (byte) 0x69, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x6B, (byte) 0x65, (byte) 0x72 };

    //ccid lbs 已发送命令标志
    public static final String CHANNEL_TABS_SENDOVER = "SENDOVER";
}
