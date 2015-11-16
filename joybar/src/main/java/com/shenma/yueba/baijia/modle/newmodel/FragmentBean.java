package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by Administrator on 2015/11/3.
 * 用于 TAB 切换和 存储数据
 */
public class FragmentBean {
    String name = "";
    int icon;
    Object fragment = null;


    public FragmentBean(String name, int icon, Object fragment) {
        this.name = name;
        this.icon = icon;
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Object getFragment() {
        return fragment;
    }

    public void setFragment(Object fragment) {
        this.fragment = fragment;
    }

}
