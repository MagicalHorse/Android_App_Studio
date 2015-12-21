package com.shenma.yueba.yangjia.modle;

public class HuoKuanItem {

	private boolean isChecked;
	private String CreateDate;// 时间,
	private double Amount;// 金额,
	private String OrderNo;// 订单号
	private String StatusName;// 状态

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
