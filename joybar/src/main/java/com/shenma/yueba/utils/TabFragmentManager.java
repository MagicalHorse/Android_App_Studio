package com.shenma.yueba.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.models.FragmentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class TabFragmentManager {
    List<FragmentBean> fragmentBeanList = new ArrayList<FragmentBean>();
    List<View> array_view = new ArrayList<View>();
    int currid=-1;
    enum Type {
        hidden,//隐藏
        show//显示
    }

    Type text_type = Type.show;
    Type image_type = Type.show;
    Activity activity;
    LinearLayout parentLinearlayout;
    TabFragmentOnClickListener listener;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    /*******************
     * 创建tab切换 fragment 视图的管理对象
     * @param  fragmentBeanList List<FragmentBean> //tab对象数组
     * @param  activity Activity
     * @param  frameLayout FrameLayout //内容视图
     * @param  parentLinearlayout Linearyaloyt //tab视图
     * ************************/
    public TabFragmentManager(List<FragmentBean> fragmentBeanList, Activity activity, FrameLayout frameLayout,LinearLayout parentLinearlayout,TabFragmentOnClickListener listener) {
        if (fragmentBeanList != null) {
            this.fragmentBeanList = fragmentBeanList;
        }

        if(frameLayout==null)
        {
            throw new NullPointerException("frameLayout not null");
        }
        this.frameLayout=frameLayout;

        if (activity == null) {
            throw new NullPointerException("activity not null");
        }
        this.activity = activity;

        if (parentLinearlayout == null) {
            throw new NullPointerException("parentLinearlayout not null");
        }
        this.parentLinearlayout = parentLinearlayout;
        this.listener=listener;
        fragmentManager=activity.getFragmentManager();

    }

    /*********
     * 设置 显示或隐藏 TextView
     *
     * @param status true 显示 false隐藏
     *********/
    public void setHiddenText(boolean status) {
        if (status) {
            text_type = Type.hidden;
        } else {
            text_type = Type.show;
        }
    }

    /*********
     * 设置 显示或隐藏 ImageView
     *
     * @param status true 显示 false隐藏
     *********/
    public void setHiddenImage(boolean status) {
        if (status) {
            image_type = Type.hidden;
        } else {
            image_type = Type.show;
        }
    }

    public void initView() {
        for (int i = 0; i < fragmentBeanList.size(); i++) {
            LinearLayout ll = getChildView(i);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1;
            parentLinearlayout.addView(ll, param);
            array_view.add(ll);
            ll.setTag(i);
            ll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    setCurrView(i);
                }
            });
        }


    }

    public synchronized void setCurrView(int i) {
        if(listener!=null)
        {
            listener.tabFragmentOnClick(i);
        }
        if (currid == -1 && i == 0) {
            if (!(((Fragment) fragmentBeanList.get(i).getFragment()).isAdded())) {
                 fragmentManager.beginTransaction().add(frameLayout.getId(), (Fragment) fragmentBeanList.get(i).getFragment()).commit();
            }
        } else if (currid == i) {
            return;
        } else {
            if (!(((Fragment) fragmentBeanList.get(i).getFragment()).isAdded())) {
                fragmentManager.beginTransaction().replace(frameLayout.getId(), (Fragment) fragmentBeanList.get(i).getFragment()).commit();
            }
        }
        currid = i;
        setTextColor_ImageView(i);
    }


    LinearLayout getChildView(int i) {
        LinearLayout ll = (LinearLayout) LinearLayout.inflate(activity, R.layout.tab_text_image_layout, null);
        ImageView imageview = (ImageView) ll.findViewById(R.id.imageview);
        switch (image_type) {
            case hidden:
                imageview.setVisibility(View.GONE);
                break;
            case show:
                imageview.setVisibility(View.VISIBLE);
                break;
        }

        imageview.setImageResource(fragmentBeanList.get(i).getIcon());
        TextView tv1 = (TextView) ll.findViewById(R.id.tv1);
        tv1.setText(ToolsUtil.nullToString(fragmentBeanList.get(i).getName()));
        switch (text_type) {
            case hidden:
                tv1.setVisibility(View.GONE);
                break;
            case show:
                tv1.setVisibility(View.VISIBLE);
                break;
        }
        return ll;
    }


    /********
     * 根据 选中的 item 切换文字和图片
     * ***********/
    void setTextColor_ImageView(int value) {
        for (int i = 0; i < array_view.size(); i++) {
            LinearLayout ll = (LinearLayout) array_view.get(i);
            ImageView iv = (ImageView) ll.findViewById(R.id.imageview);
            TextView tv = (TextView) ll.findViewById(R.id.tv1);
            if (i == value) {
                tv.setTextColor(activity.getResources().getColor(R.color.color_deeoyellow));
                iv.setSelected(true);

            } else {
                tv.setTextColor(activity.getResources().getColor(R.color.color_deeoyellow));
                iv.setSelected(false);
            }

        }
    }

    public interface TabFragmentOnClickListener
    {
        void tabFragmentOnClick(int i);
    }
}
