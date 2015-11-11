package com.shenma.yueba.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by gyj on 2015/11/2.
 * 属性 存储信息 基础类
 * 在属性中  设置 或 获取 存储的 属性信息 包含返回各种类型的方法
 *
 */
public class BasePerferneceUtils {
    private static BasePerferneceUtils perferneceUtil;
    Context mcontext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private BasePerferneceUtils(Context mcontext)
    {
        this.mcontext=mcontext;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        editor=sharedPreferences.edit();
    }

    public  static BasePerferneceUtils getInstance(Context mcontext)
    {
        if(perferneceUtil==null)
        {
            perferneceUtil=new BasePerferneceUtils(mcontext);
        }
        return perferneceUtil;
    }

    /***************************************
     * 获取属性中指定字段 存储的 布尔形数据
     * **********************************/
    public boolean getBooleanPerfernece(String perfernecestr,boolean defaultstatus)
    {
       return sharedPreferences.getBoolean(perfernecestr,defaultstatus);
    }

    /***************************************
     * 设置属性中指定字段 存储的 布尔形数据
     * **********************************/
    public void settBooleanPerfernece(String perfernecestr,boolean status)
    {
        editor.putBoolean(perfernecestr, status).commit();
    }


    /***************************************
     * 获取属性中指定字段 存储的 字符串数据
     * **********************************/
    public String getStringPerfernece(String str,String defaultvalue)
    {
        return sharedPreferences.getString(str,defaultvalue);
    }

    /***************************************
     * 设置属性中指定字段 存储的 字符串数据
     * **********************************/
    public void setStringPerfernece(String perfernecestr,String value)
    {
        editor.putString(perfernecestr,value).commit();
    }


    /***************************************
     * 获取属性中指定字段 存储的 整形数据
     * **********************************/
    public int getIntPerfernece(String str,int defaultvalue)
    {
        return sharedPreferences.getInt(str,defaultvalue);
    }


    /***************************************
     * 设置属性中指定字段 存储的 整形数据
     * **********************************/
    public void setIntPerfernece(String perfernecestr,int value)
    {
        editor.putInt(perfernecestr, value).commit();
    }


    /***************************************
     * 获取属性中指定字段 存储的 浮点形数据
     * **********************************/
    public  float getFloatPerfernece(String str,float defaultvalue)
    {
        return sharedPreferences.getFloat(str,defaultvalue);
    }


    /***************************************
     * 设置属性中指定字段 存储的 浮点形数据
     * **********************************/
    public void setFloatPerfernece(String perfernecestr,float value)
    {
        editor.putFloat(perfernecestr,value).commit();
    }


    /***************************************
     * 获取属性中指定字段 存储的 长整形数据
     * **********************************/
    public long getLongPerfernece(String perfernecestr,long defaultvalue)
    {
        return sharedPreferences.getLong(perfernecestr,defaultvalue);
    }


    /***************************************
     * 设置属性中指定字段 存储的 长整形数据
     * **********************************/
    public void setLongdefaultvalue(String perfernecestr,long defaultvalue)
    {
        editor.putLong(perfernecestr, defaultvalue).commit();
    }

    /***************************************
     * 清空 属性资源存储的信息
     * **********************************/
    public void cleargPerfernece()
    {
        editor.clear().commit();
    }

}
