package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-2 下午6:10:39  
 * 程序的简单说明   商品详情
 */

public class ProductInfoBean implements Serializable{
	int StoreId;// 商店id
    int ProductId;//  商品id
    String Name;// 商品名称
    String Productdesc="";//颜色:绿,尺码:W",    商品描述
    double Price;// 价格
    int ProductCount;//个数
    String Image="";// 图片地址
	String UserLevel;
	boolean CanRma=false; //是否可退货
	boolean CanInvoiced=false;//是否可开发票
	public boolean isCanInvoiced() {
		return CanInvoiced;
	}

	public void setCanInvoiced(boolean canInvoiced) {
		CanInvoiced = canInvoiced;
	}

	public boolean isCanRma() {
		return CanRma;
	}

	public void setCanRma(boolean canRma) {
		CanRma = canRma;
	}

	public String getUserLevel() {
		return UserLevel;
	}

	public void setUserLevel(String userLevel) {
		UserLevel = userLevel;
	}

	public int getStoreId() {
		return StoreId;
	}
	public void setStoreId(int storeId) {
		StoreId = storeId;
	}
	public int getProductId() {
		return ProductId;
	}
	public void setProductId(int productId) {
		ProductId = productId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getProductdesc() {
		return Productdesc;
	}
	public void setProductdesc(String productdesc) {
		Productdesc = productdesc;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getProductCount() {
		return ProductCount;
	}
	public void setProductCount(int productCount) {
		ProductCount = productCount;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
}
