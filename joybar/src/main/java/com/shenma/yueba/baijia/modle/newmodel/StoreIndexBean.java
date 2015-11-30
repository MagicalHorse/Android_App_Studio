package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BrandInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2015/11/25.
 * 门店商品列表
 */
public class StoreIndexBean {

    private List<StoreIndexItem> items;
    private int pageindex;
    private int pagesize;
    private int totalcount;
    private int totalpaged;

    String StoreId;//门店编号
    String StoreName;//门店名称
    String Logo;//门店logo
    String StoreLocal;//门店地址
    String CityId;// 城市id
    String CityName;//城市名字
    String StoreLeave;// 门店级别    认证买手8  专柜买手4  品牌买手16（暂时不用）
    String Description;//描述
    List<BrandInfo> Brands=new ArrayList<BrandInfo>();//品牌信息

    public List<BrandInfo> getBrands() {
        return Brands;
    }

    public void setBrands(List<BrandInfo> brands) {
        Brands = brands;
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

    public String getStoreId() {
        return StoreId;
    }

    public void setStoreId(String storeId) {
        StoreId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getStoreLocal() {
        return StoreLocal;
    }

    public void setStoreLocal(String storeLocal) {
        StoreLocal = storeLocal;
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

    public List<StoreIndexItem> getItems() {
        return items;
    }

    public void setItems(List<StoreIndexItem> items) {
        this.items = items;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getTotalpaged() {
        return totalpaged;
    }

    public void setTotalpaged(int totalpaged) {
        this.totalpaged = totalpaged;
    }
}
