package com.shenma.yueba.utils;

import android.content.Context;

import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.config.PerferneceConfig;

/**
 * Created by gyj on 2015/11/2.
 * 属性 存储信息 基础类
 * 在属性中  设置 或 获取 存储的 属性信息 包含返回各种类型的方法
 */
public class PerferneceUtil {

    private final static Context mcontext=MyApplication.getInstance().getApplicationContext();
    /********
     * 存储阿里云key
     * ********/
    public static void setAliYunKey(String userPassword) {
        if (userPassword != null) {
            BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).setStringPerfernece(PerferneceConfig.KEY,userPassword);
        }
    }


    /********
     * 存储阿里云sign
     * ********/
    public static void setAliYunSign(String sign) {
        if (sign != null) {
            BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).setStringPerfernece(PerferneceConfig.SING, sign);
        }
    }


    /***************
     * 获取指定key 存储的 boolean值 默认 false
     ****************/
    public static boolean getBoolean(String key) {
        return BasePerferneceUtils.getInstance(mcontext).getBooleanPerfernece(key, false);
    }

    /***************
     * 获取指定key 存储的 String 默认 null
     ****************/
    public static String getString(String key) {
        return BasePerferneceUtils.getInstance(mcontext).getStringPerfernece(key, null);
    }

    /***************
     * 获取指定key 存储的 int 默认 -1
     ****************/
    public static int getInt(String key) {
        return BasePerferneceUtils.getInstance(mcontext).getIntPerfernece(key, -1);
    }

    /***************
     * 获取指定key 存储的 long 默认 -1
     ****************/
    public static long getLong(String key) {
        return BasePerferneceUtils.getInstance(mcontext).getLongPerfernece(key, -1);
    }

    public static void setString(String key,String value)
    {
        BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).setStringPerfernece(key,value);
    }

    public static void setInt(String key,int value)
    {
        BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).setIntPerfernece(key, value);
    }

    public static void setLong(String key,long value)
    {
        BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).setLongdefaultvalue(key, value);
    }

    public static void setBoolean(String key,boolean value)
    {
        BasePerferneceUtils.getInstance(MyApplication.getInstance().getApplicationContext()).settBooleanPerfernece(key, value);
    }


}
