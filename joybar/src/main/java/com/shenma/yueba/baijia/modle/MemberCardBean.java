package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 * 会员卡 列表 数据
 */
public class MemberCardBean implements Serializable {
    String cardtypeimg;//卡图片
    String cardno;//卡号
    String cardtypename;//卡类型名称
    float discrate1;//一次折扣率
    float discrate2;//二次折扣率
    List<MemberCard_eticketsBean> etickets=new ArrayList<MemberCard_eticketsBean>();
    float vipdiscount;//Vip折扣
    float totalpaymentamount;//使用此卡优惠需付款金额

    public float getTotalpaymentamount() {
        return totalpaymentamount;
    }

    public void setTotalpaymentamount(float totalpaymentamount) {
        this.totalpaymentamount = totalpaymentamount;
    }

    public String getCardtypeimg() {
        return cardtypeimg;
    }

    public void setCardtypeimg(String cardtypeimg) {
        this.cardtypeimg = cardtypeimg;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCardtypename() {
        return cardtypename;
    }

    public void setCardtypename(String cardtypename) {
        this.cardtypename = cardtypename;
    }

    public float getDiscrate1() {
        return discrate1;
    }

    public void setDiscrate1(float discrate1) {
        this.discrate1 = discrate1;
    }

    public float getDiscrate2() {
        return discrate2;
    }

    public void setDiscrate2(float discrate2) {
        this.discrate2 = discrate2;
    }

    public List<MemberCard_eticketsBean> getEtickets() {
        return etickets;
    }

    public void setEtickets(List<MemberCard_eticketsBean> etickets) {
        this.etickets = etickets;
    }

    public float getVipdiscount() {
        return vipdiscount;
    }

    public void setVipdiscount(float vipdiscount) {
        this.vipdiscount = vipdiscount;
    }

}
