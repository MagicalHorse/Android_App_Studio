package com.shenma.yueba.yangjia.modle;

import com.shenma.yueba.util.SizeBean;

import java.util.List;

public class ProductItemBean {

	private String Id;//订单号
	private String BrandName;//品牌名字
	private String BrandId;//品牌id
	private String Name;//商品名称
	private String Count;//商品个数
	private String Price;//商品价格
	private String StoreItemNo;//货号
	private String SizeName;//规格
	private String  ColorName;//颜色
	private String Picture;//商品图片
	private String UnitPrice;//吊牌价
	private String Sku_Code;//销售编码
	private String PublishStatus;//商品发布状态
;	private String Desc;//商品描述
	private List<SizeBean> Sizes;//规格库存
	private List<ImageItem> Images;//图片
	private String productImage;//商品图片
	private String productName;//商品名
	private String productPrice;//商品价格
	private String productIntroduct;//商品介绍
	private String productCount;//购买的该商品的数量
	private String productCode;//货号
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductIntroduct() {
		return productIntroduct;
	}
	public void setProductIntroduct(String productIntroduct) {
		this.productIntroduct = productIntroduct;
	}
	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCount() {
		return Count;
	}
	public void setCount(String count) {
		Count = count;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getStoreItemNo() {
		return StoreItemNo;
	}
	public void setStoreItemNo(String storeItemNo) {
		StoreItemNo = storeItemNo;
	}
	public String getSizeName() {
		return SizeName;
	}
	public void setSizeName(String sizeName) {
		SizeName = sizeName;
	}
	public String getColorName() {
		return ColorName;
	}
	public void setColorName(String colorName) {
		ColorName = colorName;
	}
	public String getPicture() {
		return Picture;
	}
	public void setPicture(String picture) {
		Picture = picture;
	}

	public String getUnitPrice() {
		return UnitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		UnitPrice = unitPrice;
	}

	public String getSku_Code() {
		return Sku_Code;
	}

	public void setSku_Code(String sku_Code) {
		Sku_Code = sku_Code;
	}

	public String getPublishStatus() {
		return PublishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		PublishStatus = publishStatus;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public List<SizeBean> getSizes() {
		return Sizes;
	}

	public void setSizes(List<SizeBean> sizes) {
		Sizes = sizes;
	}

	public List<ImageItem> getImages() {
		return Images;
	}

	public void setImages(List<ImageItem> images) {
		Images = images;
	}
}
