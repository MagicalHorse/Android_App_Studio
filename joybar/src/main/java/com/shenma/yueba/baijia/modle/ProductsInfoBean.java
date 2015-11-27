package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-27 下午1:39:17  
 * 程序的简单说明  商品信息
 */

public class ProductsInfoBean implements Serializable{


	private int Buyerid;//买手id
	private String BuyerName="";// 买手名字
	private String CreateTime="";//创建时间
	private String BuyerAddress="";// 地址
	private String BuyerLogo="";// 买手头像
	private double Price;//价格
	private String ProductName="";// 产品名字
	private int ProductId; //产品编号
	private String ShareLink="";//分享链接
	private String ShareDesc="";
	private String Brandid;
	private String BrandId;
	private String  Brand;
	private String IsFavorite;// 当前用户是否关注
	private String Pic="";//图片"
	private String StoreItemNo;//商店编号",
	private String UnitPrice;//吊牌价
	private boolean IsFavite;//当前用户是否关注

	//活动信息
	private ProductsDetailsPromotion Promotion=new ProductsDetailsPromotion();
	//关注的人
	private LikeUsersInfoBean LikeUsers=new LikeUsersInfoBean();
	//买手推荐商品信息
	private ProductPicInfoBean ProductPic=new ProductPicInfoBean();

	public String getUnitPrice() {
		return UnitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		UnitPrice = unitPrice;
	}

	public boolean isFavite() {
		return IsFavite;
	}

	public void setIsFavite(boolean isFavite) {
		IsFavite = isFavite;
	}

	public int getBuyerid() {
		return Buyerid;
	}

	public void setBuyerid(int buyerid) {
		Buyerid = buyerid;
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

	public String getBuyerAddress() {
		return BuyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		BuyerAddress = buyerAddress;
	}

	public String getBuyerLogo() {
		return BuyerLogo;
	}

	public void setBuyerLogo(String buyerLogo) {
		BuyerLogo = buyerLogo;
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

	public String getShareLink() {
		return ShareLink;
	}

	public void setShareLink(String shareLink) {
		ShareLink = shareLink;
	}

	public String getShareDesc() {
		return ShareDesc;
	}

	public void setShareDesc(String shareDesc) {
		ShareDesc = shareDesc;
	}

	public String getBrandid() {
		return Brandid;
	}

	public void setBrandid(String brandid) {
		Brandid = brandid;
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getIsFavorite() {
		return IsFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		IsFavorite = isFavorite;
	}

	public String getPic() {
		return Pic;
	}

	public void setPic(String pic) {
		Pic = pic;
	}

	public String getStoreItemNo() {
		return StoreItemNo;
	}

	public void setStoreItemNo(String storeItemNo) {
		StoreItemNo = storeItemNo;
	}

	public ProductsDetailsPromotion getPromotion() {
		return Promotion;
	}

	public void setPromotion(ProductsDetailsPromotion promotion) {
		Promotion = promotion;
	}

	public LikeUsersInfoBean getLikeUsers() {
		return LikeUsers;
	}

	public void setLikeUsers(LikeUsersInfoBean likeUsers) {
		LikeUsers = likeUsers;
	}

	public ProductPicInfoBean getProductPic() {
		return ProductPic;
	}

	public void setProductPic(ProductPicInfoBean productPic) {
		ProductPic = productPic;
	}
}
