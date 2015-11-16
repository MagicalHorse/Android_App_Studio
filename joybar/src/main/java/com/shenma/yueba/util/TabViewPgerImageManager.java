package com.shenma.yueba.util;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.ScrollViewPagerAdapter;
import com.shenma.yueba.view.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/4.
 * ViewPager+Linearlayout 实现循环 图片滚动
 */
public class TabViewPgerImageManager {
    LayoutInflater layoutInflater;
    View tabview;
    Activity activity;
    ViewPager appprovebuyer_viewpager;
    LinearLayout appprovebuyer_viewpager_footer_linerlayout;
    List<String> array_str = new ArrayList<String>();
    ScrollViewPagerAdapter customPagerAdapter;
    List<View> viewlist = new ArrayList<View>();
    Timer timer;
    int currid = -1;

    public TabViewPgerImageManager(Activity activity, List<String> array_str) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        if (array_str != null) {
            this.array_str = array_str;
        }
        init();
    }

    /********
     * 初始化
     *******/
    void init() {
        tabview = layoutInflater.inflate(R.layout.tab_viewpaer_linearlayout_layout, null);
        RelativeLayout appprovebuyer_viewpager_relativelayout = (RelativeLayout) tabview.findViewById(R.id.appprovebuyer_viewpager_relativelayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)appprovebuyer_viewpager_relativelayout.getLayoutParams();
        int width = ToolsUtil.getDisplayWidth(activity);
        int height = width / 2;
        params.width = width;
        params.height = height;
        appprovebuyer_viewpager = (ViewPager) tabview.findViewById(R.id.appprovebuyer_viewpager);

        appprovebuyer_viewpager_footer_linerlayout = (LinearLayout) tabview.findViewById(R.id.appprovebuyer_viewpager_footer_linerlayout);
        appprovebuyer_viewpager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopTimerToViewPager();
                        break;
                    case MotionEvent.ACTION_UP:
                        startTimeToViewPager();
                        break;
                }
                return false;
            }
        });
        appprovebuyer_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currid = arg0;
                setcurrItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        customPagerAdapter = new ScrollViewPagerAdapter(activity, viewlist);
        appprovebuyer_viewpager.setAdapter(customPagerAdapter);
    }


    /*********
     * 获取 视图
     *********/
    public View getTabView() {
        notification();
        return tabview;
    }

    void setvalue() {
        viewlist.clear();
        for (int i = 0; i < array_str.size(); i++) {
            RelativeLayout rl = new RelativeLayout(activity);
            ImageView iv = new ImageView(activity);
            iv.setBackgroundColor(activity.getResources().getColor(R.color.color_lightgrey));
            iv.setImageResource(R.drawable.default_pic);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            rl.addView(iv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            viewlist.add(rl);
            MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(array_str.get(i)),iv,MyApplication.getInstance().getDisplayImageOptions());
        }
    }

    /**********
     * 刷新
     ************/
    public void notification() {
        if (customPagerAdapter != null) {
            setvalue();
            customPagerAdapter.notifyDataSetChanged();
            startTimeToViewPager();
        }
    }

    /*****
     * 启动自动滚动
     **/
    void startTimeToViewPager() {

        stopTimerToViewPager();
        if (viewlist == null || viewlist.size() <= 2) {
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                currid++;
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setViewPagerDuration(1000);
                        setcurrItem(currid);
                    }
                });
            }
        }, 2000, 3000);
    }

    /*****
     * 停止自动滚动
     **/
    void stopTimerToViewPager() {
        setViewPagerDuration(500);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // 设置滑动速度
    void setViewPagerDuration(int value) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(
                    appprovebuyer_viewpager.getContext(),
                    new AccelerateInterpolator());
            field.set(appprovebuyer_viewpager, scroller);
            scroller.setmDuration(value);
        } catch (Exception e) {
        }
    }


    /****
     * 设置当前显示的 item
     **/
    void setcurrItem(int i) {
        appprovebuyer_viewpager.setCurrentItem(i);
        addTabImageView(viewlist.size(), i);
    }


    /***
     * 添加原点
     *
     * @param size  int 原点的个数
     * @param value int 当前选中的tab
     **/
    void addTabImageView(int size, int value) {
        appprovebuyer_viewpager_footer_linerlayout.removeAllViews();
        ((RelativeLayout.LayoutParams) appprovebuyer_viewpager_footer_linerlayout.getLayoutParams()).bottomMargin = 25;
        if (size <= 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            View v = new View(activity);
            v.setBackgroundResource(R.drawable.tab_round_background);
            int width = (int) activity.getResources().getDimension(R.dimen.round_width);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.leftMargin = (int) activity.getResources().getDimension(R.dimen.round_margin);
            appprovebuyer_viewpager_footer_linerlayout.addView(v, params);
            if (i == value % size) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

}
