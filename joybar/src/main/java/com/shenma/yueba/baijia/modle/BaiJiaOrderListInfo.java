package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-2 下午5:59:50  
 * 程序的简单说明   订单详情
 */

public class BaiJiaOrderListInfo implements Serializable{
	String BuyerName="";//买手昵称"
	String OrderNo;//订单编号"
    int OrderProductType;//订单类型
    int OrderStatus;// 状态值
    String OrderStatusStr="";// 状态描述字符串
	int OrderProductCount;//商品数量
    double Amount;//收益
    String CreateDate="";// 创建时间
    String Address="";//地址
    ProductInfoBean Product=new ProductInfoBean();//商品详情
	
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getOrderStatusStr() {
		return OrderStatusStr;
	}
	public void setOrderStatusStr(String orderStatusStr) {
		OrderStatusStr = orderStatusStr;
	}
    
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public int getOrderProductType() {
		return OrderProductType;
	}
	public void setOrderProductType(int orderProductType) {
		OrderProductType = orderProductType;
	}
	public int getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		OrderStatus = orderStatus;
	}
	public int getOrderProductCount() {
		return OrderProductCount;
	}
	public void setOrderProductCount(int orderProductCount) {
		OrderProductCount = orderProductCount;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public ProductInfoBean getProduct() {
		return Product;
	}
	public void setProduct(ProductInfoBean product) {
		Product = product;
	}
}
