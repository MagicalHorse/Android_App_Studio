package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/12/2.
 * 创建订单 bean
 */
public class RequestCreateOrderInfo extends BaseRequest {
    public CreateOrderInfo getData() {
        return data;
    }

    public void setData(CreateOrderInfo data) {
        this.data = data;
    }

    CreateOrderInfo data=new CreateOrderInfo();
}
