package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.ProductItem;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;

import java.util.List;

/**
 * Created by a on 2015/11/25.
 */
public class BuyerInfo {
    private String NickName;//买手昵称
    private String Logo;//买手头像
    private String Nickname;//买手昵称
    private String BrandName;//品牌名字
    private String StoreName;//门店名字
    private String StoreLocal;//门店地址
    private boolean IsFllowed;//当前用户是否关注
    private String Lon;// 门店经度            0表示没有坐标
    private String Lat;//门店所在纬度        0表示没有坐标
    private List<ProductsInfoBean> Products;//商品列表
    private String UserId;
    private String BuyerId;//买手Id
    private String UserLevel;//用户等级
    private String ProvinceName;//省名称
    private String CityName;//市名称
    private String DistrictName;//去名称
    private String Address;//地址
    private List<String> Brands;//品牌


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreLocal() {
        return StoreLocal;
    }

    public void setStoreLocal(String storeLocal) {
        StoreLocal = storeLocal;
    }

    public boolean isFllowed() {
        return IsFllowed;
    }

    public void setIsFllowed(boolean isFllowed) {
        IsFllowed = isFllowed;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public List<ProductsInfoBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsInfoBean> products) {
        Products = products;
    }

    public String getBuyerId() {
        return BuyerId;
    }

    public void setBuyerId(String buyerId) {
        BuyerId = buyerId;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<String> getBrands() {
        return Brands;
    }

    public void setBrands(List<String> brands) {
        Brands = brands;
    }
}
