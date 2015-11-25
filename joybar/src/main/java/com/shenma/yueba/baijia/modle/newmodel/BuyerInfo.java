package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.ProductItem;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;

import java.util.List;

/**
 * Created by a on 2015/11/25.
 */
public class BuyerInfo {
    private String Logo;//买手头像
    private String Nickname;//买手昵称
    private String BrandName;//品牌名字
    private String StoreName;//门店名字
    private String StoreLocal;//门店地址
    private boolean IsFllowed;//当前用户是否关注
    private String Lon;// 门店经度            0表示没有坐标
    private String  Lat;//门店所在纬度        0表示没有坐标
    private List<ProductsInfoBean> Products;//商品列表


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
}
