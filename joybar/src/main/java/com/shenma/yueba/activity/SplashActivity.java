package com.shenma.yueba.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.shenma.yueba.R;
import com.shenma.yueba.activity.baijia.MainActivityForBaiJia;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.config.PerferneceConfig;
import com.shenma.yueba.utils.AiManager;
import com.shenma.yueba.utils.PerferneceUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 程序刚启动的时候，加载页面
 *
 * @author zhou
 */
public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_layout);
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(true); // 统计在线时长
        MobclickAgent.onEvent(this, "SplashActivity"); // 打开客户端
        MobclickAgent.updateOnlineConfig(SplashActivity.this);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setDebugMode(true);

        /*************************
         * 判断 用户是否第一次运行 应用 如果运行 则显示引导页 否则 跳转到 程序主页面
         * ***********************/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PerferneceUtil.getBoolean(PerferneceConfig.USER_FIRST)) {
                    skip(GuideActivity.class, true);
                } else {
                    skip(MainActivityForBaiJia.class, true);
                }
            }
        }, 2500);
        AiManager.getKeyAndSignFromNetSetToLocal(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onBackPressed() {
    }
}
