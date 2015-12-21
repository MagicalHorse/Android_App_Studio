package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.IndexProductInfo;
import com.shenma.yueba.baijia.modle.Product;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.util.TimerDownUtils;
import com.shenma.yueba.yangjia.modle.ProductItemBean;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a on 2015/11/24.
 */
public class IndexItems implements Serializable{

   String StoreName ;//门店名称
    String StoreId  ;//门店编号

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


    public String getShowstr() {
        return showstr;
    }

    public void setShowstr(String showstr) {
        this.showstr = showstr;
    }

    String showstr = "";//显示倒计时时间

    public void setTimerLinstener(TimerLinstener timerLinstener) {
        this.timerLinstener = timerLinstener;
    }


    long tem_BusinessTime;
    long tem_RemainTime;
    long DYGTime;
    long tmpBusinessTime;

    public boolean isDayangGou() {
        return isDayangGou;
    }

    boolean isDayangGou=false;
    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    boolean isRunning = false;

    public TimerLinstener getTimerLinstener() {
        return timerLinstener;
    }

    TimerLinstener timerLinstener;//设置时间回调

    public void startTime() {
        if (isRunning) {
            return;
        }
        if(BusinessTime<0)
        {
            return;
        }
        tem_BusinessTime = BusinessTime;
        tem_RemainTime = RemainTime;
        DYGTime = 24 * 3600 - BusinessTime;
        tmpBusinessTime = BusinessTime;
        isDayangGou=IsStart;

        isRunning = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                jisuan();
            }
        }, 0, 1000);
    }


    void jisuan() {
        //如果已经开始
        if (IsStart) {
            if (tem_RemainTime > 0) {
                isDayangGou=true;
                tem_RemainTime--;
                showstr = TimerDownUtils.millSecendToStr(tem_RemainTime);
                if (timerLinstener != null) {
                    timerLinstener.timerCallBack();
                }
            } else {
                if (tmpBusinessTime > 0) {
                    isDayangGou=false;
                    tmpBusinessTime--;
                    showstr = TimerDownUtils.millSecendToStr(tmpBusinessTime);
                    if (timerLinstener != null) {
                        timerLinstener.timerCallBack();
                    }
                } else {
                    if (DYGTime > 0) {
                        isDayangGou=true;
                        DYGTime--;
                        showstr = TimerDownUtils.millSecendToStr(DYGTime);
                        if (timerLinstener != null) {
                            timerLinstener.timerCallBack();
                        }
                    }else{
                        isDayangGou=false;
                        tmpBusinessTime=BusinessTime;
                        DYGTime=24 * 3600 - BusinessTime;
                    }
                }
            }


        } else {

            if (tem_RemainTime > 0) {
                isDayangGou=false;
                tem_RemainTime--;
                showstr = TimerDownUtils.millSecendToStr(tem_RemainTime);
                if (timerLinstener != null) {
                    timerLinstener.timerCallBack();
                }
            }else
            {
                if (DYGTime > 0) {
                    isDayangGou=true;
                    DYGTime--;
                    showstr = TimerDownUtils.millSecendToStr(DYGTime);
                    if (timerLinstener != null) {
                        timerLinstener.timerCallBack();
                    }
                }else
                {
                    if(tmpBusinessTime>0)
                    {
                        isDayangGou=false;
                        tmpBusinessTime--;
                        showstr = TimerDownUtils.millSecendToStr(tmpBusinessTime);
                        if (timerLinstener != null) {
                            timerLinstener.timerCallBack();
                        }
                    }else
                    {
                        isDayangGou=true;
                        DYGTime=24 * 3600 - BusinessTime;
                        tmpBusinessTime=BusinessTime;
                    }
                }
            }
        }
    }

    public interface TimerLinstener {
        void timerCallBack();
    }

}
