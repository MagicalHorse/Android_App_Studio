package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-4 下午4:20:20  
 * 程序的简单说明  
 */

public class ProductInFo implements Serializable{
	int ProductId;//商品编号",
    String ProductName="";//商品名称",
    String Pic="";//图片"
    String StoreItemNo;//商店编号",


    
	public String getStoreItemNo() {
		return StoreItemNo;
	}
	public void setStoreItemNo(String storeItemNo) {
		StoreItemNo = storeItemNo;
	}
	public int getProductId() {
		return ProductId;
	}
	public void setProductId(int productId) {
		ProductId = productId;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	
	public String getPic() {
		return Pic;
	}
	public void setPic(String pic) {
		Pic = pic;
	}

}
