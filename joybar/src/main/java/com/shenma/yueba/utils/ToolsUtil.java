package com.shenma.yueba.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.https.CommonHttpControl;
import com.shenma.yueba.interfaces.HttpCallBackInterface;
import com.shenma.yueba.models.Request_CheckAppVersionInfo;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ToolsUtil {


    public static String nullToString(String str) {
        if (str != null) {
            return str.trim();
        }
        return "";
    }


    /**********
     * 检查当前应用版本更新
     **********/
    public static void checkVersion(final Context context) {
        CommonHttpControl.checkAppVersion(new HttpCallBackInterface<Request_CheckAppVersionInfo>() {
            @Override
            public void http_Success(Request_CheckAppVersionInfo bean) {
                if (bean.getData() != null) {
                    String versionRemote = bean.getData().getVersion();
                    String localVersionStr = MyApplication.getInstance().getAppVersionNo();
                    if (!versionRemote.equals(localVersionStr)) {
                        UpdateManager manager = new UpdateManager(context, versionRemote + "", bean.getData().getUrl(), bean.getData().getTitle(), bean.getData().getDetails());
                        manager.startUpdate();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        });
    }

    /**
     * 获取当前页面的屏幕宽度
     *
     * @param cx
     * @return
     */
    public static int getDisplayWidth(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取当前页面的屏幕高度
     *
     * @param cx
     * @return
     */
    public static int getDisplayHeight(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }


}
