package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BaseRequest;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Request_CityInfo extends BaseRequest {
    private CityListItembean data;

    public CityListItembean getData() {
        return data;
    }

    public void setData(CityListItembean data) {
        this.data = data;
    }
}