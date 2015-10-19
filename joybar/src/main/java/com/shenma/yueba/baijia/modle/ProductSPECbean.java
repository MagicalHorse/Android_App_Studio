package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/10/12.
 * 本类定义 商品规格 对象
 */
public class ProductSPECbean {
    boolean ischecked=false;//是否选择
    String SizeName;// 规格名称
    String SizeId;//规格id
    int Inventory;// int 类型   库存

    public int getInventory() {
        return Inventory;
    }

    public void setInventory(int inventory) {
        Inventory = inventory;
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
    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }


}
