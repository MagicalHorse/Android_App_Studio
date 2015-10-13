package com.shenma.yueba.baijia.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 * 该类 用于 实现 Linearlayout+ViewPager 实现TAB 效果
 */
public class TabViewpagerManager {
    List<FragmentBean> bean = new ArrayList<FragmentBean>();
    List<View> footer_list = new ArrayList<View>();
    Activity activity;
    int currid = -1;//当前选中的tab
    LinearLayout parentView;
    ViewPager viewpager;

    public TabViewpagerManager(Activity activity, List<FragmentBean> bean, LinearLayout parentView, ViewPager viewpager) {
        this.activity = activity;
        this.bean = bean;
        this.parentView = parentView;
        this.viewpager = viewpager;
        iniView();
    }

    /***********
     * 根据现在的列表个数 初始化 TAB视图
     ***********/
    void iniView() {
        for (int i = 0; i < bean.size(); i++) {
            RelativeLayout rl = (RelativeLayout) RelativeLayout.inflate(activity, R.layout.tab_line_layout, null);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            FontManager.changeFonts(activity, tv);
            rl.setTag(i);
            rl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    setCurrView(i);
                }
            });
            tv.setGravity(Gravity.CENTER);
            tv.setText(bean.get(i).getName());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1;
            //param.gravity = Gravity.CENTER;
            parentView.addView(rl, param);
            footer_list.add(rl);
        }
    }


    /********
     * 初始化viewpager (viewpager 加载的信息为 view)
     ********/
    public void initViewPager() {
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return bean.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = ((BaseView) bean.get(position).getFragment()).getView();
                container.addView(v, 0);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View v = ((BaseView) bean.get(position).getFragment()).getView();
                container.removeView(v);
            }
        });
    }

    /*****
     * 初始化 ViewPager 的 操作 (viewpager 加载的信息为 fragment)
     ***/
    public void initFragmentViewPager(final FragmentManager fragmentManager, final Bundle bundle) {
        viewpager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                FragmentBean itembean = bean.get(position);
                Fragment fragment = (Fragment) itembean.getFragment();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return bean.size();
            }
        });

    }


    /***
     * 设置当前需要显示的 item
     ***/
    public void setCurrView(int i) {
        if (currid == i) {
            return;
        }
        currid = i;
        setTextColor(i);
        viewpager.setCurrentItem(i);
    }


    /*****
     * 设置字体颜色及选中后显示的图片
     ***/
    void setTextColor(int value) {
        for (int i = 0; i < footer_list.size(); i++) {
            RelativeLayout rl = (RelativeLayout) footer_list.get(i);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            View v = (View) rl.findViewById(R.id.tab_line_view);
            if (i == value) {
                tv.setTextColor(activity.getResources().getColor(
                        R.color.color_deeoyellow));
                tv.setTextSize(Constants.title_text_selected_size);
                v.setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(activity.getResources().getColor(
                        R.color.text_gray_color));
                tv.setTextSize(Constants.title_text_normal_size);
                v.setVisibility(View.INVISIBLE);
            }

        }
    }
}
