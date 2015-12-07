package com.shenma.yueba.util;

import com.shenma.yueba.inter.InterfaceOrderRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 * 订单观察者模式 当 有需要通知的数据 更新时  调用refreshList 方法 所有的观察者将 接收到通知
 *
 */
public class OrderChangeRefreshObserver {
    static OrderChangeRefreshObserver instance;
    private List<InterfaceOrderRefreshListener> mList = new ArrayList<InterfaceOrderRefreshListener>();
    private OrderChangeRefreshObserver()
    {

    }
    public static OrderChangeRefreshObserver getInstance()
    {
        if(instance==null)
        {
            instance=new OrderChangeRefreshObserver();
        }
        return  instance;
    }

    public void addObserver(InterfaceOrderRefreshListener listener)
    {
        if(!mList.contains(listener))
        {
            mList.add(listener);
        }
    }

    public void clearAllObserver()
    {
        mList.clear();
    }

    public void removeObserver(InterfaceOrderRefreshListener listener)
    {
        if(mList.contains(listener))
        {
            mList.remove(listener);
        }
    }

    public void refreshList(int index,Object bean) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).refreshOrderList(index,bean);
        }
    }

}
