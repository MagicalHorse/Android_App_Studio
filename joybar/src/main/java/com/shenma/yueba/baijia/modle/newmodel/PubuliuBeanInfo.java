package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by Administrator on 2015/11/11.
 *  用于瀑布流中 每个Item 对应的数据
 */
public class PubuliuBeanInfo {

    String id="";
    String name="";//商品名称
    String picurl="http://d.hiphotos.baidu.com/image/pic/item/0b46f21fbe096b63edff42510e338744eaf8acc8.jpg";//图片地址
    boolean iscollection=false;//是否收藏
    double price;//价格
    float ration=1;//图片的 宽高比
    int favoriteCount;

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
