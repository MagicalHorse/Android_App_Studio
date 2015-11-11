package com.shenma.yueba.utils;

import com.shenma.yueba.interfaces.CityChangeRefreshInter;
import com.shenma.yueba.models.CityListItembean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 * 城市列表 观察者模式 当 有需要通知的数据 更新时  调用refreshList 方法 所有的观察者将 接收到通知
 *
 */
public class CityChangeRefreshObserver {
    static CityChangeRefreshObserver instance;
    private List<CityChangeRefreshInter> mList = new ArrayList<CityChangeRefreshInter>();
    private CityChangeRefreshObserver()
    {

    }
    public static CityChangeRefreshObserver getInstance()
    {
        if(instance==null)
        {
            instance=new CityChangeRefreshObserver();
        }
        return  instance;
    }

    public void addObserver(CityChangeRefreshInter cityChangeRefreshInter)
    {
        if(!mList.contains(cityChangeRefreshInter))
        {
            mList.add(cityChangeRefreshInter);
        }
    }

    public void clearAllObserver()
    {
        mList.clear();
    }

    public void removeObserver(CityChangeRefreshInter cityChangeRefreshInter)
    {
        if(mList.contains(cityChangeRefreshInter))
        {
            mList.remove(cityChangeRefreshInter);
        }
    }

    public void refreshList(CityListItembean bean) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).refresh(bean);
        }
    }

}
