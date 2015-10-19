package com.shenma.yueba.baijia.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 * 本类定义商品颜色 类型 对象
 */
public class ProductColorTypeBean {
    boolean isChecked=false; //是否被选中
    String ColorName;//颜色名称       如果不存在颜色信息  则为 ‘默认’
    String ColorId;//颜色id      //   如果不存在颜色信息  则为0
    String Pic;//颜色尺码图
    List<ProductSPECbean> Size = new ArrayList<ProductSPECbean>();//
    public List<ProductSPECbean> getSize() {
        return Size;
    }

    public void setSize(List<ProductSPECbean> size) {
        Size = size;
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


    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


}
