package com.aiparker.net.domain;

import com.aiparker.net.utils.QHex;

/**
 * ******************************************************************************************************************
 * 自定义数据包格式
 * 											HexMessage
 * ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐
 * 		5BYTE		|	1BYTE(Ox04)	|	2BYTE		|	1BYTE		|	NBYTE		|	1BYTE		|	1BYTE		
 * ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┤
 * 					|				|				|				|				|				|
 * |  SYNC(同步码)  		STX(包头)		LEN(长度)		CMD(命令字)		DATA(数据)		CHK(校验)		ETX(包尾)	│
 * 					|				|				|				|				|				|
 * └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
 * 
 * 消息定义
 * 	SYNC	旧版同步码(0x55,0x55,0x55,0x55,0x55)  加密同步码1[密钥aiperker](0x55,0x55,0x55,0x55,0xA5) 
 *  STX		包头(0x04)
 *  LEN		长度
 *  CMD		(0x01:[初始化]) 
 *  		(0x02:[初始化成功回执]) 
 *  		(0x03:[正常读卡上送]) 
 *  		(0x04:[心跳]) 
 *  		(0x06:[心跳同步成功回执][旧 010300020100 版本后不再支持])
 *  		(0x07:[实时读卡上送])	
 *  		(0x08:[POS消费记录上送])
 *  		(0x09:[后台服务主动推送])	
 *  		(0x0A:[在线升级])
 *  		(0x0B:[终端收费方式回执]) 
 *  
 *  DATA	数据域
 *  		([CMD:0x01]
 *  			  [上行]
 *  			  [6063:12BYTE:设备唯一物理编号]
 *  			  [7665:06BYTE:设备版本号(硬件版本03BYTE)+(软件版本03BYTE)]
 *  			  ---------------------------------------------------
 *  			  [下行]
 *  			  [7374:1BYTE:状态-[0x00:成功][0x01:失败]]
 *  			  [736E:6BYTE:设备唯一虚拟编号]
 *  			  [6164:NBYTE:服务器连接地址[无意义]]
 *  			  [6B65:8BYTE:设备License[无意义]]
 *  			  [656E:1BYTE:设备出入类型-[0x00:入口(按平台配置)][0x01:出口(按平台配置)][0x02:出入口(按平台配置)][0x03:出入口(按设备传输)]]
 *   			  [6474:7BYTE:系统时间[YYYYMMDDHHMISS]]
 *    			  [7266:1BYTE:重复触发时间(相同标示固定时间内不重复触发)]
 *     			  [7263:1BYTE:上行等待次数]
 *     			  [7074:1BYTE:上行等待时长]
 *     			  [6862:1BYTE:区域类型-[0x00:小区][0x01:商用]]
 *     			  [7264:1BYTE:心跳等待时长]
 *     			  [7663:2BYTE
 *     			  	读卡器类型「
 *     			  		[0x00:IC]
 *     					[0x01:蓝牙]
 *     					[0x02:车牌识别]
 *     					[0x05:POS]
 *     					[0x06:2.4G]
 *     					[0x07:5.8G(车牌)]
 *     					[0x08:900M]
 *     					[0x09:5.8G(卡号)]
 *     			  	」
 *     				接口类型「
 *     					[0x00:232]
 *     					[0x01:485]
 *     					[0x02:WG26]
 *     					[0x03:WG34]
 *     					车牌识别
 *     					[0x00:臻识]
 *     					[0x10:智芯源动]
 *     				」
 *     			  ]
 *     			  [6663:52BYTE:计费信息配置
 *     			  	┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *     					
 *     				└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *     			  ]
 *     		}
 *  			  
 *  CHK		校验位(CRC)
 *  EXT		包尾(0x06)
 * 
 * 
 * @author Hypnos
 * 
 *
 */
public class HexMessage {
	private byte[] byte_sync = new byte[5];
	private byte[] byte_header = new byte[1];
	private byte[] byte_length = new byte[2];
	private byte[] byte_cmd = new byte[1];
	private byte[] byte_data;
	private byte[] byte_data_device;
	private byte[] byte_crc = new byte[1];
	private byte[] byte_etx = new byte[1];

	public byte[] getByte_data_device() {
		return byte_data_device;
	}

	public void setByte_data_device(byte[] byte_data_device) {
		this.byte_data_device = byte_data_device;
	}

	public byte[] getByte_sync() {
		return byte_sync;
	}

	public void setByte_sync(byte[] byte_sync) {
		this.byte_sync = byte_sync;
	}

	public byte[] getByte_header() {
		return byte_header;
	}

	public void setByte_header(byte[] byte_header) {
		this.byte_header = byte_header;
	}

	public byte[] getByte_length() {
		return byte_length;
	}

	public void setByte_length(byte[] byte_length) {
		this.byte_length = byte_length;
	}

	public byte[] getByte_cmd() {
		return byte_cmd;
	}

	public void setByte_cmd(byte[] byte_cmd) {
		this.byte_cmd = byte_cmd;
	}

	public byte[] getByte_data() {
		return byte_data;
	}

	public void setByte_data(byte[] byte_data) {
		this.byte_data = byte_data;
	}

	public byte[] getByte_crc() {
		return byte_crc;
	}

	public void setByte_crc(byte[] byte_crc) {
		this.byte_crc = byte_crc;
	}

	public byte[] getByte_etx() {
		return byte_etx;
	}

	public void setByte_etx(byte[] byte_etx) {
		this.byte_etx = byte_etx;
	}

	@Override
	public String toString() {
		return "[byte_sync:" + QHex.bytesToHexString(byte_sync) 
		        + ",byte_header:" + QHex.bytesToHexString(byte_header)
				+ ",byte_length:" + QHex.bytesToHexString(byte_length) 
				+ ",byte_cmd:" + QHex.bytesToHexString(byte_cmd)
				+ ",byte_data:" + QHex.bytesToHexString(byte_data) 
				+ ",byte_data_device:" + QHex.bytesToHexString(byte_data_device)
				+ ",byte_crc:" + QHex.bytesToHexString(byte_crc)
				+ ",byte_etx:" + QHex.bytesToHexString(byte_etx);
	}
}
