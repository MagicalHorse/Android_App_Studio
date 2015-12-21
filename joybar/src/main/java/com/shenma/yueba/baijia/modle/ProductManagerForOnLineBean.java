package com.shenma.yueba.baijia.modle;

public class ProductManagerForOnLineBean {
	
	private String Pic;//商品图片
	private String ProductName;//商品名称
	private String ProductId;//商品编号
	private String BrandName;//品牌名
	private String BrandId;//品牌ID
	private String ExpireTime;//下架时间
	private String StoreItemNo;//货号
	private double Price;//价格
	private String UpdateDate;
	private String ShareLink;
	private RequestUploadProductDataBean Detail;
	public String getPic() {
		return Pic;
	}
	public void setPic(String pic) {
		Pic = pic;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	public String getBrandId() {
		return BrandId;
	}
	public void setBrandId(String brandId) {
		BrandId = brandId;
	}
	public String getExpireTime() {
		return ExpireTime;
	}
	public void setExpireTime(String expireTime) {
		ExpireTime = expireTime;
	}
	public String getStoreItemNo() {
		return StoreItemNo;
	}
	public void setStoreItemNo(String storeItemNo) {
		StoreItemNo = storeItemNo;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public String getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(String updateDate) {
		UpdateDate = updateDate;
	}
	public String getShareLink() {
		return ShareLink;
	}
	public void setShareLink(String shareLink) {
		ShareLink = shareLink;
	}
	public RequestUploadProductDataBean getDetail() {
		return Detail;
	}
	public void setDetail(RequestUploadProductDataBean detail) {
		Detail = detail;
	}
	
	
	
}
