package com.shenma.yueba.models;

/**
 * Created by Administrator on 2015/11/3.
 * 启动 阿里云 数据对象
 */
public class Request_AliYunKeyBackInfo extends BaseRequest {

    private AliYunKeyBean data;

    public AliYunKeyBean getData() {
        return data;
    }

    public void setData(AliYunKeyBean data) {
        this.data = data;
    }
}
