package com.shenma.yueba.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.shenma.yueba.R;
import com.shenma.yueba.activity.baijia.MainActivityForBaiJia;
import com.shenma.yueba.adapter.GuideAdapter;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.config.PerferneceConfig;
import com.shenma.yueba.utils.PerferneceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 * 引导页
 */
public class GuideActivity extends Activity {
    private static final int TO_THE_END = 0;// 到达最后一张
    private static final int LEAVE_FROM_END = 1;// 离开最后一张
    private int[] ids = {R.mipmap.splash1, R.mipmap.splash2, R.mipmap.splash3, R.mipmap.splash4};
    private List<View> guides = new ArrayList<View>();
    private ViewPager pager;
    private ImageView open;
    private Context context = GuideActivity.this;
    private Button mRegister;
    private Button mLogin;
    private int position;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication.getInstance().addActivity(GuideActivity.this);
        guide();
    }

    protected void guide() {
        setContentView(R.layout.guide);
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ids.length - 1 == position) {
                        PerferneceUtil.setBoolean(PerferneceConfig.USER_FIRST, true);
                        Intent intent = new Intent(context, MainActivityForBaiJia.class);
                        startActivity(intent);
                        GuideActivity.this.finish();
                    }

                }
            });
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            guides.add(iv);
        }

        GuideAdapter adapter = new GuideAdapter(guides);
        pager = (ViewPager) findViewById(R.id.contentPager);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TO_THE_END)
                open.setVisibility(View.VISIBLE);
            else if (msg.what == LEAVE_FROM_END)
                open.setVisibility(View.GONE);
        }
    };

}