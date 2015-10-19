package com.shenma.yueba.baijia.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜买手商品详情数据
 */
public class CKProductDeatilsInfoBean extends ProductsDetailsInfoBean {
    double UnitPrice;//吊牌价
    boolean IsSupportDeposit;//是否支持定金  如果true  则只能定金支付   false 则只能全款支付
    String ReservedDay;//支付定金后商品预留时间
    String ProductType;//商品类型    2  普通商品    4通用商品
    String BrandName;//品牌名字
    String BuyerLeave;//买手级别   0 普通用户  4 商场导购 	8认证买手	16品牌买手
    String StoreService;//门店服务信息
    String SizeContrastPic;//尺码图
    List<CkProductInfoBean> InterestedProduct = new ArrayList<CkProductInfoBean>();//可能感兴趣的商品

    public List<CkProductInfoBean> getInterestedProduct() {
        return InterestedProduct;
    }

    public void setInterestedProduct(List<CkProductInfoBean> interestedProduct) {
        InterestedProduct = interestedProduct;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public boolean isSupportDeposit() {
        return IsSupportDeposit;
    }

    public void setIsSupportDeposit(boolean isSupportDeposit) {
        IsSupportDeposit = isSupportDeposit;
    }

    public String getReservedDay() {
        return ReservedDay;
    }

    public void setReservedDay(String reservedDay) {
        ReservedDay = reservedDay;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getBuyerLeave() {
        return BuyerLeave;
    }

    public void setBuyerLeave(String buyerLeave) {
        BuyerLeave = buyerLeave;
    }

    public String getStoreService() {
        return StoreService;
    }

    public void setStoreService(String storeService) {
        StoreService = storeService;
    }

    public String getSizeContrastPic() {
        return SizeContrastPic;
    }

    public void setSizeContrastPic(String sizeContrastPic) {
        SizeContrastPic = sizeContrastPic;
    }


}
