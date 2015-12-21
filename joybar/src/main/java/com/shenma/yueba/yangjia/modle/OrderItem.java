package com.shenma.yueba.yangjia.modle;

import java.util.List;

import com.shenma.yueba.baijia.modle.Product;

public class OrderItem {
	private String OrderNo;
	private String Status;
	private String StatusName;
	private double InCome;
	private String Amount;
	private String CreateTime;
	private String GoodsAmount;//货款金额
	private boolean IsGoodsPick;//是否已经提款
	private boolean IsNeedRma;//是否显示退款相关
	private String RmaNo="";//充值并退款号
	public String getRmaNo() {
		return RmaNo;
	}
	public void setRmaNo(String rmaNo) {
		RmaNo = rmaNo;
	}
	private List<ProductItemBean> Products;
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getStatusName() {
		return StatusName;
	}
	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public List<ProductItemBean> getProducts() {
		return Products;
	}
	public void setProducts(List<ProductItemBean> products) {
		Products = products;
	}
	public String getGoodsAmount() {
		return GoodsAmount;
	}
	public void setGoodsAmount(String goodsAmount) {
		GoodsAmount = goodsAmount;
	}
	public boolean isIsGoodsPick() {
		return IsGoodsPick;
	}
	public void setIsGoodsPick(boolean isGoodsPick) {
		IsGoodsPick = isGoodsPick;
	}
	public boolean isIsNeedRma() {
		return IsNeedRma;
	}
	public void setIsNeedRma(boolean isNeedRma) {
		IsNeedRma = isNeedRma;
	}

	public double getInCome() {
		return InCome;
	}

	public void setInCome(double inCome) {
		InCome = inCome;
	}
}
