package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/12/29.
 * 门店退货信息
 */
public class StoreSalesReturnInfo {
    String StoreMobile;//联系电话
    String StoreName;// 门店名
    String StoreZipCode;// 邮编
    String RmaAddress;//退货地址
    String RmaPerson;//退货联系人
    String[] RmaTips;//温馨提示  该字段是数据 里面可能有多条提示
    public String[] getRmaTips() {
        return RmaTips;
    }

    public void setRmaTips(String[] rmaTips) {
        RmaTips = rmaTips;
    }
    public String getStoreMobile() {
        return StoreMobile;
    }

    public void setStoreMobile(String storeMobile) {
        StoreMobile = storeMobile;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreZipCode() {
        return StoreZipCode;
    }

    public void setStoreZipCode(String storeZipCode) {
        StoreZipCode = storeZipCode;
    }

    public String getRmaAddress() {
        return RmaAddress;
    }

    public void setRmaAddress(String rmaAddress) {
        RmaAddress = rmaAddress;
    }

    public String getRmaPerson() {
        return RmaPerson;
    }

    public void setRmaPerson(String rmaPerson) {
        RmaPerson = rmaPerson;
    }
}
