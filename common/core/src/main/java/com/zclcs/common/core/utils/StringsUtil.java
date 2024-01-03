package com.zclcs.common.core.utils;

/**
 * @author zclcs
 */
public class StringsUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String trimString(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String chooseOneIsNotBlank(String... strs) {
        if (strs == null) {
            return null;
        }
        for (String str : strs) {
            if (isNotBlank(str)) {
                return str;
            }
        }
        return null;
    }

}
