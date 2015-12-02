package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CreateOrderInfo implements Serializable {
    String OrderNo;//订单号     如果是全款支付，则给微信传这个单号   回调地址是原来的地址
    double TotalAmount;// 订单总金额
    double DisCountAmount;// 神马优惠金额（打样购）
    double VipDisCountAmount;//会员优惠金额
    double ActualAmount;//实际需要支付的总金额

    public double getActualAmount() {
        return ActualAmount;
    }

    public void setActualAmount(double actualAmount) {
        ActualAmount = actualAmount;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getDisCountAmount() {
        return DisCountAmount;
    }

    public void setDisCountAmount(double disCountAmount) {
        DisCountAmount = disCountAmount;
    }

    public double getVipDisCountAmount() {
        return VipDisCountAmount;
    }

    public void setVipDisCountAmount(double vipDisCountAmount) {
        VipDisCountAmount = vipDisCountAmount;
    }

}
