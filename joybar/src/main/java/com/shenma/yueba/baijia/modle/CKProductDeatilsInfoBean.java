package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜买手商品详情数据
 */
public class CKProductDeatilsInfoBean implements Serializable{
    String BuyerId;//买手id
    String BuyerName;//买手名字
    String BuyerLogo;//买手头像
    String BuyerMobile;//买手手机
    String TurnCount;//商品成交量
    String PickAddress;//自提地址
    String ProductId;//商品id
    String ProductName;//商品名
    double Price;//价格
    double UnitPrice;//吊牌价
    String ProductType;//商品类型    2  普通商品    4通用商品
    String BrandName;//品牌名字
    String UserLevel;//
    String StoreService;//门店服务信息
    String SizeContrastPic;//尺码图
    String CityId;// 商品所在城市
    String CityName;//商品所在城市

    List<CkProductInfoBean> InterestedProduct = new ArrayList<CkProductInfoBean>();//可能感兴趣的商品
    LikeUsersInfoBean LikeUsers=new LikeUsersInfoBean();//关注的人
    boolean IsFavorite=false;// 当前用户是否喜欢该商品
    String StoreName;// 门店名称/商场名称
    String StoreId;//商场编号
    boolean IsStart=false;//是否开始
    String BusinessTime;//营业时长（秒） 即非打样购时间
    String RemainTime;// 剩余时长（秒） 如果IsStart=true  则是剩余结束时间，否则是剩余开始时间
    //活动信息
    ProductsDetailsPromotion Promotion=new ProductsDetailsPromotion();
    String ShareLink;//分享链接地址
    String ShareDesc;//分享内容
    List<ProductsDetailsTagInfo>  ProductPic=new ArrayList<ProductsDetailsTagInfo>();
    boolean  IsJoinDeiscount=false;//是否参加Vip折扣  eg.False
    float VipDiscount;//Vip折扣率  eg.0

    public float getVipDiscount() {
        return VipDiscount;
    }

    public void setVipDiscount(float vipDiscount) {
        VipDiscount = vipDiscount;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public boolean isJoinDeiscount() {
        return IsJoinDeiscount;
    }

    public void setIsJoinDeiscount(boolean isJoinDeiscount) {
        IsJoinDeiscount = isJoinDeiscount;
    }

    public List<ProductsDetailsTagInfo> getProductPic() {
        return ProductPic;
    }

    public void setProductPic(List<ProductsDetailsTagInfo> productPic) {
        ProductPic = productPic;
    }
    public String getShareDesc() {
        return ShareDesc;
    }

    
    public void setShareDesc(String shareDesc) {
        ShareDesc = shareDesc;
    }

    
    public String getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(String buyerId) {
        BuyerId = buyerId;
    }

    
    public String getBuyerName() {
        return BuyerName;
    }

    
    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    
    public String getBuyerLogo() {
        return BuyerLogo;
    }

    
    public void setBuyerLogo(String buyerLogo) {
        BuyerLogo = buyerLogo;
    }

    
    public String getBuyerMobile() {
        return BuyerMobile;
    }

    
    public void setBuyerMobile(String buyerMobile) {
        BuyerMobile = buyerMobile;
    }


    public String getTurnCount() {
        return TurnCount;
    }

    public void setTurnCount(String turnCount) {
        TurnCount = turnCount;
    }

    
    public String getPickAddress() {
        return PickAddress;
    }

    
    public void setPickAddress(String pickAddress) {
        PickAddress = pickAddress;
    }

    
    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    
    public String getProductName() {
        return ProductName;
    }

    
    public void setProductName(String productName) {
        ProductName = productName;
    }

    
    public double getPrice() {
        return Price;
    }

    
    public void setPrice(double price) {
        Price = price;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
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

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public List<CkProductInfoBean> getInterestedProduct() {
        return InterestedProduct;
    }

    public void setInterestedProduct(List<CkProductInfoBean> interestedProduct) {
        InterestedProduct = interestedProduct;
    }

    
    public LikeUsersInfoBean getLikeUsers() {
        return LikeUsers;
    }

    
    public void setLikeUsers(LikeUsersInfoBean likeUsers) {
        LikeUsers = likeUsers;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    
    public void setIsFavorite(boolean isFavorite) {
        IsFavorite = isFavorite;
    }

    
    public String getStoreName() {
        return StoreName;
    }

    
    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    
    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public boolean isStart() {
        return IsStart;
    }

    public void setIsStart(boolean isStart) {
        IsStart = isStart;
    }

    public String getBusinessTime() {
        return BusinessTime;
    }

    public void setBusinessTime(String businessTime) {
        BusinessTime = businessTime;
    }

    public String getRemainTime() {
        return RemainTime;
    }

    public void setRemainTime(String remainTime) {
        RemainTime = remainTime;
    }

    
    public ProductsDetailsPromotion getPromotion() {
        return Promotion;
    }

    
    public void setPromotion(ProductsDetailsPromotion promotion) {
        Promotion = promotion;
    }

    
    public String getShareLink() {
        return ShareLink;
    }

    
    public void setShareLink(String shareLink) {
        ShareLink = shareLink;
    }

}
