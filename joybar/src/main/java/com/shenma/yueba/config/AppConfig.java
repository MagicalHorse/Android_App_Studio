package com.shenma.yueba.config;

import android.os.Environment;

/**
 * Created by Administrator on 2015/11/4.
 */
public class AppConfig {

    // SD卡路径(有"/")
    public static String SDCARDPATH = Environment.getExternalStorageDirectory() + "/";
    // 存贮图片的app文件夹名
    public final static String APP_WENJIANJIA = "joybar/";
    public final static String SD = SDCARDPATH + APP_WENJIANJIA;
    // apk新版本要存的名字
    public final static String apkName = "joybar.apk";
}
