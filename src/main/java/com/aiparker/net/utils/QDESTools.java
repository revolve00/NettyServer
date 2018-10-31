package com.aiparker.net.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class QDESTools {
	/** 加密算法,可用 DES,DESede,Blowfish. */
	private final static String ALGORITHM = "DES";
	/** j加密码填充模式另种1，"DES/ECB/NOPADDING" 2."DES/CBC/PKCS5Padding" */
	private final static String ENCIPHER_MODEL = "DES/ECB/NOPADDING";

	/**
	 * 将8位密文解密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 * @author cdl
	 * @date Apr 1, 20144:40:11 PM
	 * 
	 */
	public static byte[] decrypt(byte[] message) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);
		byte[] key = { (byte) 0x41, (byte) 0x69, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x6B, (byte) 0x65,
				(byte) 0x72 };
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(message);

	}
	
	/**
	 * 将8位密文解密
	 * @param message
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] message,String keyStr) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);
		byte[] key = keyStr.getBytes();
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(message);

	}
	
	/**
	 * 将8位密文解密
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] message,byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(message);

	}

	/**
	 * 把8位明文加密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 * @author cdl
	 * @date Apr 1, 20144:27:33 PM
	 * 
	 */
	public static byte[] encrypt(byte[] message) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);// 不填充规定明文必须8位
		// byte[] key = "AiParker".getBytes();//
		byte[] key = { (byte) 0x41, (byte) 0x69, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x6B, (byte) 0x65,
				(byte) 0x72 };
		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(message);
	}
	

	
	/**
	 * 把8位明文加密
	 * @param message
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] message, String keyStr) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);// 不填充规定明文必须8位
		byte[] key = keyStr.getBytes();
		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(message);
	}
	
	/**
	 * 把8位明文加密
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] message, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCIPHER_MODEL);// 不填充规定明文必须8位
		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(message);
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte数组转换成16进制字符串
	 * 
	 * @param src
	 * @return
	 */

	public static String byteToHexString(byte src) {
		StringBuilder stringBuilder = new StringBuilder();
		int v = src & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);
		return stringBuilder.toString();
	}
}
