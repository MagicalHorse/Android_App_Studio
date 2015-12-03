package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/2.
 * 打样够 信息
 */
public class DaYangGouDisInfoBean implements Serializable {
    float discount;//打烊购折扣率 eg.0.4000,
    double maxamount;//打样购最大折扣金额  eg.50.0

    public double getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(double maxamount) {
        this.maxamount = maxamount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
