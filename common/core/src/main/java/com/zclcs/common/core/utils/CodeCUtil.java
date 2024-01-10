package com.zclcs.common.core.utils;

/**
 * @author zclcs
 */
public class CodeCUtil {

    /**
     * 8位
     */
    private static final int EIGHT = 8;

    /**
     * 包分隔符
     */
    public static final int PACKET_DELIMITER = 0x01;

    /**
     * 成功标志
     */
    public static final byte FLAG_SUCCESS = 0x00;

    /**
     * 失败标志
     */
    public static final byte FLAG_ERROR = 0x01;

    /**
     * 整形转换字节数组
     *
     * @param data 被转换得整形
     * @param size 字节数组大小
     * @return 字节数组
     */
    public static byte[] toBytes(int data, int size) {
        byte[] bytes = new byte[size];

        for (int i = 0; i < bytes.length; i++) {
            int position = i * EIGHT;
            bytes[i] = (byte) ((data & (0xff << position)) >> position);
        }
        return bytes;
    }

    /**
     * 字节数组转换整形
     *
     * @param bytes 字节数组
     * @return 整形
     */
    public static int toInt(byte[] bytes) {
        int num = 0;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * EIGHT;
            num = num | (0xff << position & (bytes[i] << position));
        }
        return num;
    }

    /**
     * 去除字符串末尾的空格
     *
     * @param str 字符串
     * @return 去除空格后的字符串
     */
    public static String trimString(String str) {
        int blankIndex = str.indexOf('\0');
        if (blankIndex != -1) {
            return str.substring(0, blankIndex).strip();
        }
        return str;
    }

    /**
     * 字节数组转换为16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // 将字节转换为16进制字符串
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                // 如果字节只有一位，则在结果字符串前补0
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转换为字节数组
     *
     * @param hexString 16进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }


}
