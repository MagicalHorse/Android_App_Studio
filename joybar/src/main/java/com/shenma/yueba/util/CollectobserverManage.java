package com.shenma.yueba.util;

import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/21.
 * 该类 定义 观察者模式 用于监听 收藏商品与取消收藏商品数据变化是 通知改变
 */
public class CollectobserverManage {
   static CollectobserverManage collectobserverManage;
    List<ObserverListener> observer_array = new ArrayList<ObserverListener>();

    private CollectobserverManage() {

    }

    /********
     * 添加观察者
     *
     * @param observerListener ObserverListener
     ******/
    public void addObserver(ObserverListener observerListener) {
        if(observerListener!=null)
        {
            if (!observer_array.contains(observerListener)) {
                observer_array.add(observerListener);
            }
        }

    }


    /********
     * 移除观察者
     *
     * @param observerListener ObserverListener
     ******/
    public void removeObserver(ObserverListener observerListener) {
        if(observerListener!=null)
        {
            if (observer_array.contains(observerListener)) {
                observer_array.add(observerListener);
            }
        }

    }

    /*****
     * 通知数据更新
     * ***/
    public synchronized  void dataChangeNotication(PubuliuBeanInfo pubuliuBeanInfo)
    {
        if(pubuliuBeanInfo!=null)
        {
            for(int i=0;i<observer_array.size();i++)
            {
                observer_array.get(i).observerCallNotification(pubuliuBeanInfo);
            }
        }

    }

    /********
     * 移除所以观察者
     ******/
    public void clearObserver() {
        observer_array.clear();
    }

    public static CollectobserverManage getInstance() {
        if (collectobserverManage == null) {
            collectobserverManage = new CollectobserverManage();
        }
        return collectobserverManage;
    }

    public interface ObserverListener {
        void observerCallNotification(PubuliuBeanInfo pubuliuBeanInfo);
    }
}
