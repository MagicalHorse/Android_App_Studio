package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-11 下午2:43:15  
 * 程序的简单说明   (败家订单详情)
 */

public class BaiJiaOrdeDetailsInfoBean implements Serializable{
	String OrderNo="";//订单编号
    double OrderAmount;//订单金额
	int BuyerId;// 买手编号
	String BuyerMobile="";//买手手机
	String BuyerName="";//买手名字
	String PickAddress="";// 提货地址
	int OrderStatus;//订单状态  int
	String OrderStatusName="";// 订单状态描述
	String CreateDate="";// 创建时间
	String BuyerLogo="";//买手头像
	boolean IsShareable;// 是否可以分享
    String ShareLink="";//分享连接
    List<OrderPromotions> Promotions=new ArrayList<OrderPromotions>();//活动信息
    double ActualAmount;//实际付款
    String ShareDesc="";//分享信息
	List<BaijiaOrderDetailsInfo> Product=new ArrayList<BaijiaOrderDetailsInfo>();
	int OrderProductType;//订单类型
	String UserLevel;//买手级别
	boolean CanInvoiced;// 是否可开发票
	boolean CanRma;// 是否可退货
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

	public String getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public double getOrderAmount() {
		return OrderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		OrderAmount = orderAmount;
	}

	public int getBuyerId() {
		return BuyerId;
	}

	public void setBuyerId(int buyerId) {
		BuyerId = buyerId;
	}

	public String getBuyerMobile() {
		return BuyerMobile;
	}

	public void setBuyerMobile(String buyerMobile) {
		BuyerMobile = buyerMobile;
	}

	public String getBuyerName() {
		return BuyerName;
	}

	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}

	public String getPickAddress() {
		return PickAddress;
	}

	public void setPickAddress(String pickAddress) {
		PickAddress = pickAddress;
	}

	public int getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getOrderStatusName() {
		return OrderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		OrderStatusName = orderStatusName;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getBuyerLogo() {
		return BuyerLogo;
	}

	public void setBuyerLogo(String buyerLogo) {
		BuyerLogo = buyerLogo;
	}

	public boolean isShareable() {
		return IsShareable;
	}

	public void setIsShareable(boolean isShareable) {
		IsShareable = isShareable;
	}

	public String getShareLink() {
		return ShareLink;
	}

	public void setShareLink(String shareLink) {
		ShareLink = shareLink;
	}

	public List<OrderPromotions> getPromotions() {
		return Promotions;
	}

	public void setPromotions(List<OrderPromotions> promotions) {
		Promotions = promotions;
	}

	public double getActualAmount() {
		return ActualAmount;
	}

	public void setActualAmount(double actualAmount) {
		ActualAmount = actualAmount;
	}

	public String getShareDesc() {
		return ShareDesc;
	}

	public void setShareDesc(String shareDesc) {
		ShareDesc = shareDesc;
	}

	public List<BaijiaOrderDetailsInfo> getProduct() {
		return Product;
	}

	public void setProduct(List<BaijiaOrderDetailsInfo> product) {
		Product = product;
	}

	public int getOrderProductType() {
		return OrderProductType;
	}

	public void setOrderProductType(int orderProductType) {
		OrderProductType = orderProductType;
	}
}
