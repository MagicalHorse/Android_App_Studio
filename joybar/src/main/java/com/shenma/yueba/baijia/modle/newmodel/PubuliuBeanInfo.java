package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by Administrator on 2015/11/11.
 *  用于瀑布流中 每个Item 对应的数据
 */
public class PubuliuBeanInfo {

    String id="";
    String name="";//商品名称
    String picurl="";//图片地址
    boolean iscollection=false;//是否收藏
    double price;//价格
    float ration=1;//图片的 宽高比
    int favoriteCount;
    //该数据是否正在运行中 （true 表示 数据正在网络通信中）
    boolean isruning=false;
    public boolean isruning() {
        return isruning;
    }

    public void setIsruning(boolean isruning) {
        this.isruning = isruning;
    }



    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRation() {
        return ration;
    }

    public void setRation(float ration) {
        this.ration = ration;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public boolean iscollection() {
        return iscollection;
    }

    public void setIscollection(boolean iscollection) {
        this.iscollection = iscollection;
    }


}
