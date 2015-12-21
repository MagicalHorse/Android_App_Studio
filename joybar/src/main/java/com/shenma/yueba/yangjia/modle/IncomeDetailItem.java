package com.shenma.yueba.yangjia.modle;

public class IncomeDetailItem {
	private String id;//编号
	private String order_type;//Giftcard/product （1/2）
	private String order_no;//订单编号
	private double income_amount;//提成金额
	private String status;//状态        0:不可用;1:冻结;2:失效;3:有效
	private double amount;//订单金额
	private String create_date;//日期,
	private String status_show;//文字状态：如“冻结”等
	private String orderstatus;	//订单状态
	private String orderstatus_s;// 订单状态文字描述
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public double getIncome_amount() {
		return income_amount;
	}

	public void setIncome_amount(double income_amount) {
		this.income_amount = income_amount;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getStatus_show() {
		return status_show;
	}
	public void setStatus_show(String status_show) {
		this.status_show = status_show;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getOrderstatus_s() {
		return orderstatus_s;
	}
	public void setOrderstatus_s(String orderstatus_s) {
		this.orderstatus_s = orderstatus_s;
	}
	
	
}
