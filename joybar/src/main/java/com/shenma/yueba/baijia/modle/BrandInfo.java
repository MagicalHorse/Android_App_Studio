package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-4 下午4:13:17  
 * 程序的简单说明  
 */

public class BrandInfo implements Serializable{
	int BrandId;//品牌编号",
    String BrandName="";//品牌名称",
    String Pic="";//品牌Logo",
	public int getBrandId() {
		return BrandId;
	}
	public void setBrandId(int brandId) {
		BrandId = brandId;
	}
	public String getBrandName() {
		return BrandName;
	}
	public void setBrandName(String brandName) {
		BrandName = brandName;
	}
	public String getPic() {
		return Pic;
	}
	public void setPic(String pic) {
		Pic = pic;
	}
    
}
