package com.aiparker.net.utils;


import com.aiparker.net.domain.HexMessage;

public class QHex {

    /**
     * 
     * check_data_integrity(crc包验证) (这里描述这个方法适用条件 – 可选)
     * 
     * @param data
     * @return boolean
     * @exception @since
     *                1.0.0
     */
    public static boolean check_data_integrity(HexMessage data) {
        byte[] crc_data = new byte[data.getByte_data().length + 3];
        System.arraycopy(data.getByte_length(), 0, crc_data, 0, 2);
        System.arraycopy(data.getByte_cmd(), 0, crc_data, 2, 1);
        System.arraycopy(data.getByte_data(), 0, crc_data, 3, data.getByte_data().length);
        return QHex.XOR(crc_data) == (data.getByte_crc()[0] & 0xff);
    }

    /**
     * byte转str
     * 
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 * 
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8);
		return value;
	}

    /**
     * byte数组转int xxH+xxH
     * 
     * @param buf
     * @return
     */
    public static int getInt(byte[] buf) {
        int sn = Integer.valueOf(bytesToHexString(buf), 16);
        return sn;
    }

    /**
     * 16进制转int 累加 xxH+xxH
     * 
     * @param str
     * @return
     */
    public static int getInt(String str) {
        int sn = 0;
        for (int i = 0; i < str.length(); i = i + 2) {
            sn += Integer.parseInt(str.substring(i, i + 2), 16);
        }
        return sn;
    }

    /**
     * XOR
     * 
     * @param str
     * @return
     */
    public static int XOR(byte[] str) {
        int CRC = 0;
        for (int i = 0; i < str.length; i++) {
            CRC = CRC ^ 0xFF & str[i];
        }
        return CRC;
    }

    /**
     * 获得HEX编码 不足按长度补0
     * 
     * @param str
     * @param length
     * @return
     */
    public static String getHexInt(int str, int length) {
        StringBuffer sn = new StringBuffer();
        String a = Integer.toHexString(str);
        if (a.length() < length) {
            for (int i = a.length(); i < length; i++) {
                a = "0" + a;
            }
            sn.append(a);
        } else {
            sn.append(a);
        }
        return sn.toString();
    }

    /**
     * 16进制字符串转化为byte数组
     * 
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
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
     * 将十六进制字符数组转换为字节数组
     * 
     * @param data
     *            十六进制char[]
     * @return byte[]
     * @throws RuntimeException
     *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     * 
     * @param ch
     *            十六进制char
     * @param index
     *            十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException
     *             当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * 获得HEX串 不足补0
     * 
     * @param str
     * @return
     */
    public static String getHexIntStr(String str) {
        StringBuffer sn = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            sn.append(getHexInt(Integer.parseInt(str.substring(i, i + 1), 16), 2));
        }
        return sn.toString();
    }

    /**
     * 16进制转字符串 0102030405060708 = 12345678
     * 
     * @param str
     * @return
     */
    public static String getString(String str) {
        StringBuffer sn = new StringBuffer();
        for (int i = 0; i < str.length(); i = i + 2) {
            sn.append(Integer.parseInt(str.substring(i, i + 2), 16));
        }
        return sn.toString();
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }

    public static void main(String[] args) {
        System.out.println(bytesToHexString(intToBytes2(255)));
    }
}
