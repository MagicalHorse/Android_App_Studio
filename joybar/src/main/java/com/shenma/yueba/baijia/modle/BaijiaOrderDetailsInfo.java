package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/30.
 * 订单详情
 */
public class BaijiaOrderDetailsInfo implements Serializable {
    int ProductId;//产品id
    String ProductName="";//产品名字
    String ProductPic="";//产品图片
    double Price;//价格
    int ProductCount;//产品数量
    String SizeName="";//规格
    String SizeValue="";//规格值
    public String getSizeValue() {
        return SizeValue;
    }

    public void setSizeValue(String sizeValue) {
        SizeValue = sizeValue;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPic() {
        return ProductPic;
    }

    public void setProductPic(String productPic) {
        ProductPic = productPic;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int productCount) {
        ProductCount = productCount;
    }

    public String getSizeName() {
        return SizeName;
    }

    public void setSizeName(String sizeName) {
        SizeName = sizeName;
    }
}
