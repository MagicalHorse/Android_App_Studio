package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.media.Image;

import com.shenma.yueba.util.ProductImagesBean;
import com.shenma.yueba.util.SizeBean;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-22 下午6:04:20  
 * 程序的简单说明  上传商品信息 返回的数据
 */

public class RequestUploadProductDataBean implements Serializable{
	private String Id;
	private String Price;//价格
	private String UnitPrice;//吊牌价
	private String Desc;
	private String Sku_Code;
	private List<SizeBean> Sizes;
	private List<ProductImagesBean> Images;
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public String getSku_Code() {
		return Sku_Code;
	}
	public void setSku_Code(String sku_Code) {
		Sku_Code = sku_Code;
	}
	public List<SizeBean> getSizes() {
		return Sizes;
	}
	public void setSizes(List<SizeBean> sizes) {
		Sizes = sizes;
	}
	public List<ProductImagesBean> getImages() {
		if(Images == null){
			Images = new ArrayList<ProductImagesBean>();
			Images.add(new ProductImagesBean());
			Images.add(new ProductImagesBean());
			Images.add(new ProductImagesBean());
		}
		return Images;
	}
	public void setImages(List<ProductImagesBean> images) {
		Images = images;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	
	

}
