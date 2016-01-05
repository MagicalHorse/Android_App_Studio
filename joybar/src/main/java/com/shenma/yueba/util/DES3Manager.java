package com.shenma.yueba.util;

/**
 * Created by Administrator on 2016/1/5.
 */
public class DES3Manager {
    final static String Key="SHENMAVmZ2hpamtsbW5vcHFy";
    final static String iv="01234567";

    /*******
     * 加密
     * ****/
    public static String encryptByDes3(String str)
    {
        if(str!=null && !str.equals(""))
        {
            try {
                return DES3.encode(Key,iv,str);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /*******
     * 解密
     * ****/
    public static String decodeByDes3(String str)
    {
        if(str!=null && !str.equals(""))
        {
            try {
                return DES3.decode(Key,iv,str);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
