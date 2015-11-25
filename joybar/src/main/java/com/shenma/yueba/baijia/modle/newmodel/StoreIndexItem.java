package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by a on 2015/11/25.
 */
public class StoreIndexItem {


    private String Id;
    private String Name;
    private String BrandName;
    private String Price;
    private String UnitPrice;
    private String  FaviteCount;
    private boolean IsFavite;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getFaviteCount() {
        return FaviteCount;
    }

    public void setFaviteCount(String faviteCount) {
        FaviteCount = faviteCount;
    }

    public boolean isFavite() {
        return IsFavite;
    }

    public void setIsFavite(boolean isFavite) {
        IsFavite = isFavite;
    }
}
