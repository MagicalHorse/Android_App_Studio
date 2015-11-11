
package com.shenma.yueba.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;


import com.shenma.yueba.activity.LoginAndRegisterActivity;

import java.util.LinkedList;
import java.util.List;

/********
 * MyApplication 单例
 **********/
public class MyApplication extends Application {
    private static MyApplication instance;
    private static List<Activity> activityList = new LinkedList<Activity>();//activity对象缓存

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /********************************
     * 获取 MyApplication
     ***********************************/
    public static MyApplication getInstance() {
        return instance;
    }


    /**
     * 将acitivity加入到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityList != null && activity != null) {
            activityList.add(activity);
        }
    }

    /**
     * 移除某个activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activityList != null && activity != null
                && activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }


    /***
     * 跳转到登录页
     **/
    public void startLogin(final Context context, final String msg) {
        if (context != null && context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showMessage(context, msg);
                    Intent intent = new Intent(context, LoginAndRegisterActivity.class);
                    intent.putExtra("flag", "needLogin");
                    ((Activity) context).startActivity(intent);
                }
            });
        }

    }


    /******
     * 显示提示信息
     *
     ***/
    public void showMessage(final Context context, final String msg) {
        if (context != null && context instanceof Activity && msg != null) {
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    /************
     * 获取 当前app的版本号
     * *******/
    public  String getAppVersionNo()
    {
        String versionName = "0";
        // 获取版本号
        PackageInfo pi = null;
        try {
            pi = instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = pi.versionName;
        versionName = "2.3";
        return versionName;
    }
}

