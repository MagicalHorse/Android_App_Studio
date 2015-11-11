package com.shenma.yueba.utils;

/**
 * @author gyj
 * @version 创建时间：2015-5-11 下午2:37:48
 * 程序的简单说明  本类提供字符串 MD5加密
 */

import com.shenma.yueba.config.HttpPerderneceConfig;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Md5Utils {

    // 十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /** 对字符串进行MD5加密 */
    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param b byte[]字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /** 将一个字节转化成十六进制形式的字符串 */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    /****
     * MD5加密后的字符串
     * 根据 K-V根据K 进行排序 最终生成 MD5字符串
     * 根据Map的k,v 按照K的小写排序 最终生成类似于 a=1&b=2&c=3类型的字符串   如果json不为空 则加入字符串末端  之后再在 字符串前后加上钥匙
     * @param map_list Map<String, String>  K--传递的参数   v--对应的值
     * @param json String  JSON格式字符串(没有可设置NUll 主要用上传json类型数据)
     * ****/
    public static String md5ToString(Map<String, String> map_list, String json) {
        if (map_list == null) {
            return null;
        }
        //转化成小写后的map 即 K 转化成小写
        Map<String, String> newmap = new HashMap<String, String>();


        List<String> str_array = new ArrayList<String>();

        if (map_list != null) {
            Set<String> set = map_list.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String k = iterator.next();
                if (map_list.containsKey(k)) {
                    String v = map_list.get(k);
                    newmap.put(k.toLowerCase(), v);
                }
                str_array.add(k.toLowerCase());
            }
        }
        if (str_array != null) {
            lowerSort(str_array);
            StringBuffer sb = new StringBuffer();
            sb.append(HttpPerderneceConfig.PRIVATEKEY);
            for (int i = 0; i < str_array.size(); i++) {
                String str = str_array.get(i);
                if (newmap.containsKey(str)) {
                    sb.append(str);
                    sb.append("=");
                    sb.append(newmap.get(str));
                }
                if (i != str_array.size() - 1) {
                    sb.append("&");
                }
            }
            if (json != null) {
                sb.append(json);
            }
            sb.append(HttpPerderneceConfig.PRIVATEKEY);
            return encodeByMD5(sb.toString());
        }
        return null;
    }


    /******
     * 参数按照“参数名”进行字母排序
     * ***/
    public static void lowerSort(List<String> str_array) {
        if (str_array != null) {
            Collections.sort(str_array);
        }
    }
}
