package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜买手 商品详情 推荐商品信息
 */
public class CkProductInfoBean implements Serializable{
    String ProductId;//商品id
    String Pic;//商品图片
    String ProductName;//商品名
    double Price;//价格

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }



}
