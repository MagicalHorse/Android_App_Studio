package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/30.
 */
public class AffirmProductInfo implements Serializable {
    RequestCKProductDeatilsInfo data;
    String ColorName;// 颜色名称       如果不存在颜色信息  则为 ‘默认’
    String ColorId;//颜色id         如果不存在颜色信息  则为0
    String Pic;//颜色尺码图
    String SizeName;//规格名称
    String SizeId;//规格id
    int buycount;//int 购买数量
    public int getBuycount() {
        return buycount;
    }

    public void setBuycount(int buycount) {
        this.buycount = buycount;
    }

    public RequestCKProductDeatilsInfo getData() {
        return data;
    }

    public void setData(RequestCKProductDeatilsInfo data) {
        this.data = data;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public String getColorId() {
        return ColorId;
    }

    public void setColorId(String colorId) {
        ColorId = colorId;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getSizeName() {
        return SizeName;
    }

    public void setSizeName(String sizeName) {
        SizeName = sizeName;
    }

    public String getSizeId() {
        return SizeId;
    }

    public void setSizeId(String sizeId) {
        SizeId = sizeId;
    }
}
