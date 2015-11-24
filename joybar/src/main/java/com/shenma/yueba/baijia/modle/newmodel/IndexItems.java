package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.Product;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.yangjia.modle.ProductItemBean;

import java.util.List;

/**
 * Created by a on 2015/11/24.
 */
public class IndexItems {

private String Name;
    private String Id;
    private String Logo;
    private String logoCityId;
    private String idCityName;
    private String Location;
    private String Description;
    private String StoreLeave;
    private String StartTime;
    private String EndTime;
    private List<BrandInfo> Brands;
    private List<Product> Products;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getLogoCityId() {
        return logoCityId;
    }

    public void setLogoCityId(String logoCityId) {
        this.logoCityId = logoCityId;
    }

    public String getIdCityName() {
        return idCityName;
    }

    public void setIdCityName(String idCityName) {
        this.idCityName = idCityName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStoreLeave() {
        return StoreLeave;
    }

    public void setStoreLeave(String storeLeave) {
        StoreLeave = storeLeave;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public List<BrandInfo> getBrands() {
        return Brands;
    }

    public void setBrands(List<BrandInfo> brands) {
        Brands = brands;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }
}
