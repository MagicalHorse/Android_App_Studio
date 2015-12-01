package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/1.
 * 获取商品可用会员卡
 */
public class MemberCard_eticketsBean implements Serializable{
    String account;//优惠券账户编码
    String subaccount;//子账户编码
    String accounttype;//账户类型
    String eticketname;//优惠券账户名称
    float eticketmoney;//优惠券账户余额
    float eticketlimitmoney;//限收金额// -1标示不限制 否则只能此优惠券只能使用最多这个数值
    String paymentno;//支付类型
    String paymentname;//返利卡", //支付类型名称
    String validdate;// 有效期
    boolean iscasheticket;//是否是增值券本金

    public boolean iscasheticket() {
        return iscasheticket;
    }

    public void setIscasheticket(boolean iscasheticket) {
        this.iscasheticket = iscasheticket;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSubaccount() {
        return subaccount;
    }

    public void setSubaccount(String subaccount) {
        this.subaccount = subaccount;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getEticketname() {
        return eticketname;
    }

    public void setEticketname(String eticketname) {
        this.eticketname = eticketname;
    }

    public float getEticketmoney() {
        return eticketmoney;
    }

    public void setEticketmoney(float eticketmoney) {
        this.eticketmoney = eticketmoney;
    }

    public float getEticketlimitmoney() {
        return eticketlimitmoney;
    }

    public void setEticketlimitmoney(float eticketlimitmoney) {
        this.eticketlimitmoney = eticketlimitmoney;
    }

    public String getPaymentno() {
        return paymentno;
    }

    public void setPaymentno(String paymentno) {
        this.paymentno = paymentno;
    }

    public String getPaymentname() {
        return paymentname;
    }

    public void setPaymentname(String paymentname) {
        this.paymentname = paymentname;
    }

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }

}
