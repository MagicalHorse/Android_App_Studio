package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/11/29.
 */
public class MoreBrandBackBean extends BaseRequest {
    public MoreBrandBackInfo getData() {
        return data;
    }

    public void setData(MoreBrandBackInfo data) {
        this.data = data;
    }

    MoreBrandBackInfo data=new MoreBrandBackInfo();
}
