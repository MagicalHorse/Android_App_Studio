package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.ProductInfoBean;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.RequestUploadProductDataBean;
import com.shenma.yueba.yangjia.modle.ProductItemBean;

public class ProductDetailBackBean extends BaseRequest{
	private RequestUploadProductDataBean data;


	public RequestUploadProductDataBean getData() {
		return data;
	}

	public void setData(RequestUploadProductDataBean data) {
		this.data = data;
	}
}
