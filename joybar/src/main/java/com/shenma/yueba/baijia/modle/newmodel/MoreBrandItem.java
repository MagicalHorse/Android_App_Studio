package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.Product;

import java.util.List;

/**
 * Created by a on 2015/11/25.
 */
public class MoreBrandItem {


    private String BuyerLogo;//买手头像
    private String BuyerName;//买手昵称
    private String StoreName;//买手门店名称
    private String SectionLocal;//门店地址
    private String IsFollow;//当前用户是否关注过
    private List<Product> Products;//


    public String getBuyerLogo() {
        return BuyerLogo;
    }

    public void setBuyerLogo(String buyerLogo) {
        BuyerLogo = buyerLogo;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getSectionLocal() {
        return SectionLocal;
    }

    public void setSectionLocal(String sectionLocal) {
        SectionLocal = sectionLocal;
    }

    public String getIsFollow() {
        return IsFollow;
    }

    public void setIsFollow(String isFollow) {
        IsFollow = isFollow;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }
}
