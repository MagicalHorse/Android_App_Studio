package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

public class Income implements Serializable {
	private double today_income;// 今日收益
	private double total_income;// 总收益
	private String avail_amount;//可提現金額
	private String request_amount;//申请金额

	public double getToday_income() {
		return today_income;
	}

	public void setToday_income(double today_income) {
		this.today_income = today_income;
	}

	public double getTotal_income() {
		return total_income;
	}

	public void setTotal_income(double total_income) {
		this.total_income = total_income;
	}

	public String getAvail_amount() {
		return avail_amount;
	}

	public void setAvail_amount(String avail_amount) {
		this.avail_amount = avail_amount;
	}

	public String getRequest_amount() {
		return request_amount;
	}

	public void setRequest_amount(String request_amount) {
		this.request_amount = request_amount;
	}
}
