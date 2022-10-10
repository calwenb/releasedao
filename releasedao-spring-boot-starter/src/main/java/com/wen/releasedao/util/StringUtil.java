package com.wen.releasedao.util;

/**
 * 字符串工具类
 */
public class StringUtil {

    public static void append(StringBuilder sb, String format, Object... args) {
        int l = 0;
        int r;
        for (Object arg : args) {
            int i = format.indexOf("{}", l);
            sb.append(format, l, i);
            l = i;
            r = l + 2;
            sb.append(arg);
            l = r;
        }
        sb.append(format.substring(l));
    }

    /**
     * 底层通过StringBuffer实现<br>
     * String.format()形式调用保证代码可读性
     * @param sb StringBuffer
     * @param format 格式化字符串
     * @param args 值列表
     */
    public static void append(StringBuffer sb, String format, Object... args) {
        int l = 0;
        int r;
        for (Object arg : args) {
            int i = format.indexOf("{}", l);
            sb.append(format, l, i);
            l = i;
            r = l + 2;
            sb.append(arg);
            l = r;
        }
        sb.append(format.substring(l));
    }
}