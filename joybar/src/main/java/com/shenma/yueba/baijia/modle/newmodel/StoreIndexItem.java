package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by a on 2015/11/25.
 * 门店商品列表
 */
public class StoreIndexItem {


    String ProductId;//  商品id
    String ProductName;// 商品名称

    private String BrandName;//品牌名称
    private String Price; //商品价格
    private String UnitPrice; //吊牌价
    private String  FavoriteCount;//收藏数
    private boolean IsFavorite;//当前用户是否收藏
    float Ratio=1;// 图片高度/图片宽度的比例
    String Pic;// 商品图片

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
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

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getFavoriteCount() {
        return FavoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        FavoriteCount = favoriteCount;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        IsFavorite = isFavorite;
    }

    public float getRatio() {
        return Ratio;
    }

    public void setRatio(float ratio) {
        Ratio = ratio;
    }
}
