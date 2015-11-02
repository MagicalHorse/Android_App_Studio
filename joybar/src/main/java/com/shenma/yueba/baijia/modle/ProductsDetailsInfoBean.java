package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-3 下午4:43:18  
 * 程序的简单说明  
 */

public class ProductsDetailsInfoBean implements Serializable{
	int BuyerId;//买手id
	String BuyerName="";// 买手名字
	String CreateTime="";//创建时间
	String BuyerLogo="";// 买手头像
	int TurnCount;//交易量
	String PickAddress="";//提货地址
	double Price;//价格
	String ProductName="";// 产品名字
	int ProductId; //产品编号
	String BuyerMobile="";//手机号
	boolean IsFavorite=false;//是否收藏
	String StoreName="";//门店名称/商场名称
	int StoreId  ;//店铺id
	
	//活动信息
    ProductsDetailsPromotion Promotion=new ProductsDetailsPromotion();
	String ShareLink="";//分享链接
	String ShareDesc="";//分享描述
	public String getShareDesc() {
		return ShareDesc;
	}

	public void setShareDesc(String shareDesc) {
		ShareDesc = shareDesc;
	}

	public String getShareLink() {
		return ShareLink;
	}

	public void setShareLink(String shareLink) {
		ShareLink = shareLink;
	}


	
	public ProductsDetailsPromotion getPromotion() {
		return Promotion;
	}
	public void setPromotion(ProductsDetailsPromotion promotion) {
		Promotion = promotion;
	}
	public String getStoreName() {
		return StoreName;
	}
	public void setStoreName(String storeName) {
		StoreName = storeName;
	}
	public int getStoreId() {
		return StoreId;
	}
	public void setStoreId(int storeId) {
		StoreId = storeId;
	}
	List<PrioductSizesInfoBean> Sizes=new ArrayList<PrioductSizesInfoBean>();
	
	public boolean isIsFavorite() {
		return IsFavorite;
	}
	public void setIsFavorite(boolean isFavorite) {
		IsFavorite = isFavorite;
	}
	public List<PrioductSizesInfoBean> getSizes() {
		return Sizes;
	}
	public void setSizes(List<PrioductSizesInfoBean> sizes) {
		Sizes = sizes;
	}
	public String getBuyerMobile() {
		return BuyerMobile;
	}
	public void setBuyerMobile(String buyerMobile) {
		BuyerMobile = buyerMobile;
	}
	//关注的人
	LikeUsersInfoBean LikeUsers=new LikeUsersInfoBean();
	//买手推荐商品信息
	List<ProductsDetailsTagInfo> ProductPic=new ArrayList<ProductsDetailsTagInfo>();
	public List<ProductsDetailsTagInfo> getProductPic() {
		return ProductPic;
	}
	public void setProductPic(List<ProductsDetailsTagInfo> productPic) {
		ProductPic = productPic;
	}
	public int getBuyerId() {
		return BuyerId;
	}
	public void setBuyerId(int buyerId) {
		BuyerId = buyerId;
	}
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	
	public String getBuyerLogo() {
		return BuyerLogo;
	}
	public void setBuyerLogo(String buyerLogo) {
		BuyerLogo = buyerLogo;
	}
	public int getTurnCount() {
		return TurnCount;
	}
	public void setTurnCount(int turnCount) {
		TurnCount = turnCount;
	}
	public String getPickAddress() {
		return PickAddress;
	}
	public void setPickAddress(String pickAddress) {
		PickAddress = pickAddress;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public int getProductId() {
		return ProductId;
	}
	public void setProductId(int productId) {
		ProductId = productId;
	}
	public LikeUsersInfoBean getLikeUsers() {
		return LikeUsers;
	}
	public void setLikeUsers(LikeUsersInfoBean likeUsers) {
		LikeUsers = likeUsers;
	}
	
}
