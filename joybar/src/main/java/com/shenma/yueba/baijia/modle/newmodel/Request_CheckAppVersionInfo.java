package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BaseRequest;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Request_CheckAppVersionInfo  extends BaseRequest {
    private CheckAppVersionbean data;

    public CheckAppVersionbean getData() {
        return data;
    }

    public void setData(CheckAppVersionbean data) {
        this.data = data;
    }
}
