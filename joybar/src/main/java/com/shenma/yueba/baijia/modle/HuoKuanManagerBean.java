package com.shenma.yueba.baijia.modle;

public class HuoKuanManagerBean {
	private double TotalAmount;//总货款
	private double PickedAmount;//已提现
	private double CanPickAmount;//可提现
	private double  FrozenAmount;//已冻结
	private double RmaAmount;//退款
	private String PickedPercent;//已经提现比例,
	private String CanPickPercent;//可提现比例
	private String FrozenPercent;//冻结的比例
	private String RmaPercent;//退货的比例
	private double Credit;//货款总额度
	private double UsedCredit;//已使用货款额度
	private String UsedCreditPercent;//已使用货款额度比例

	public double getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}

	public double getPickedAmount() {
		return PickedAmount;
	}

	public void setPickedAmount(double pickedAmount) {
		PickedAmount = pickedAmount;
	}

	public double getCanPickAmount() {
		return CanPickAmount;
	}

	public void setCanPickAmount(double canPickAmount) {
		CanPickAmount = canPickAmount;
	}

	public double getFrozenAmount() {
		return FrozenAmount;
	}

	public void setFrozenAmount(double frozenAmount) {
		FrozenAmount = frozenAmount;
	}

	public double getRmaAmount() {
		return RmaAmount;
	}

	public void setRmaAmount(double rmaAmount) {
		RmaAmount = rmaAmount;
	}

	public String getPickedPercent() {
		return PickedPercent;
	}
	public void setPickedPercent(String pickedPercent) {
		PickedPercent = pickedPercent;
	}
	public String getCanPickPercent() {
		return CanPickPercent;
	}
	public void setCanPickPercent(String canPickPercent) {
		CanPickPercent = canPickPercent;
	}
	public String getFrozenPercent() {
		return FrozenPercent;
	}
	public void setFrozenPercent(String frozenPercent) {
		FrozenPercent = frozenPercent;
	}
	public String getRmaPercent() {
		return RmaPercent;
	}
	public void setRmaPercent(String rmaPercent) {
		RmaPercent = rmaPercent;
	}

	public double getCredit() {
		return Credit;
	}

	public void setCredit(double credit) {
		Credit = credit;
	}

	public double getUsedCredit() {
		return UsedCredit;
	}

	public void setUsedCredit(double usedCredit) {
		UsedCredit = usedCredit;
	}

	public String getUsedCreditPercent() {
		return UsedCreditPercent;
	}
	public void setUsedCreditPercent(String usedCreditPercent) {
		UsedCreditPercent = usedCreditPercent;
	}
	
	
}
