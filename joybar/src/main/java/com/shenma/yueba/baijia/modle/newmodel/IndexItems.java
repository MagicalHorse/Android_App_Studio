package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.IndexProductInfo;
import com.shenma.yueba.baijia.modle.Product;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.util.TimerDownUtils;
import com.shenma.yueba.yangjia.modle.ProductItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by a on 2015/11/24.
 */
public class IndexItems implements Serializable{

private String Name;
    private String Id;
    private String Logo;
    private String CityId;
    private String CityName;
    private String Location;
    private String Description;
    private String StoreLeave;

    private List<BrandInfo> Brands;
    private List<IndexProductInfo> Products;
    boolean IsStart=false;
    private long BusinessTime;//营业时长（秒
    private long RemainTime;//剩余时长（秒） 如果IsStart=true  则是剩余结束时间，否则是剩余开始时间
    double Lon;//门店经度            0表示没有坐标
    double Lat ;//  门店所在纬度          0表示没有坐标

    public boolean isStart() {
        return IsStart;
    }

    public void setIsStart(boolean isStart) {
        IsStart = isStart;
    }

    public long getBusinessTime() {
        return BusinessTime;
    }

    public void setBusinessTime(long businessTime) {
        BusinessTime = businessTime;
    }

    public long getRemainTime() {
        return RemainTime;
    }

    public void setRemainTime(long remainTime) {
        RemainTime = remainTime;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }


    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

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


    public List<BrandInfo> getBrands() {
        return Brands;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
    public void setBrands(List<BrandInfo> brands) {
        Brands = brands;
    }

    public List<IndexProductInfo> getProducts() {
        return Products;
    }

    public void setProducts(List<IndexProductInfo> products) {
        Products = products;
    }

    HomeItemInfoListener listener;
    TimerDownUtils timerDownUtils;

    public String getShowstr() {
        return showstr;
    }

    public void setShowstr(String showstr) {
        this.showstr = showstr;
    }

    String showstr="";

    public void startTime()
    {
        if(timerDownUtils==null)
        {
            timerDownUtils=new TimerDownUtils();
            timerDownUtils.timerDown(null, IsStart, BusinessTime, RemainTime, new TimerDownUtils.TimerCallListener() {
                @Override
                public void currTime(String str) {
                    setShowstr(str);
                    if(listener!=null)
                    {
                        listener.callback();
                    }
                }
            });
        }

    }

    public void setTimerCallListener(HomeItemInfoListener listener)
    {
        this.listener=listener;
    }

    public interface HomeItemInfoListener
    {
        void callback();
    }
}
