package com.jindidata.user.redpacket.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类（主要实现系统工具方法实现）
 */
public class Util {
    /**
     * 获取当前日期的 yyyy-MM-dd HH:mm:SS格式
     *
     * @return
     */
    public static String getSimilerDatePath() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 获取当前日期的yyyy/MM/dd格式
     *
     * @return
     */
    public static String getSimilerDate() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(new Date());
    }

    /**
     * 获取当前日期的yyyy年MM月dd日格式
     *
     * @return
     */
    public static String getDateString() {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(new Date());
    }

    /**
     * 获取月份
     * @return
     */
    public static int getMonths() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String getFormatDate(String format) throws Exception {
        if (Util.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        return dateformat.format(cal.getTime());
    }

    /**
     * 获取days
     *
     *
     * @return
     */
    public static int getDays() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static byte[] md5(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest tool = MessageDigest.getInstance("MD5");
        tool.update(src);
        byte[] byts = tool.digest();
        return byts;
    }

    /**
     * MD5 加密
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String md5(String src) throws Exception {
        return new String(md5(src.getBytes()));
    }

    /**
     * 创建32位seqid 采用uuid算法
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replaceAll("-", "");
    }

    public static Map<String, String> getRequestMap(Map map) {
        Map<String, String> rst = new HashMap<String, String>();
        Set keys = map.keySet();
        for (Object obj : keys) {
            String key = String.valueOf(obj);
            String[] value = (String[])map.get(key);
            if (value.length > 0) {
                rst.put(key, value[0]);
            }
        }
        return rst;
    }

    public static void responseAJAXData(HttpServletResponse response, String writeValue) throws IOException {
        response.setContentType("text/html");
        response.setContentType("utf-8");
        response.getWriter().write(writeValue);
    }

    /**
     * 数值判断
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^([1-9]{1})([0-9]*)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * String 空值判断(为空)
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * String 空值判断(不为空)
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        str = str.trim();
        if (null == str || "".equals(str)) {
            return false;
        }
        return true;
    }

    /**
     * Map 空值判断(为空)
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Map str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * Map 空值判断(不为空)
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Map str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 获取秒杀日期
     *
     * @param str
     * @return
     */
    public static Date getSpikeDate(String str) {
        Date convertDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            format.setLenient(false);
            convertDate = format.parse(str);
        } catch (Exception e) {
            //出现异常就获取当前时间
            convertDate = new Date();
        }
        return convertDate;
    }

    /**
     * 时间比较
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            return 0;
        }
    }

    /**
     * 时间比较
     *
     * @param DATE1
     * @return
     */
    public static int compare_date_2_sysdate(String DATE1) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = new Date();
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return -1;
            }
        } catch (Exception exception) {
            return -1;
        }
    }

}
