package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-8 下午7:45:33  
 * 程序的简单说明  
 */

public class MyFavoriteProductListPic implements Serializable{

	String pic="";// 图片地址
    float Ratio;// 0.7500   图片高度/图片宽度的比例
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public float getRatio() {
		return Ratio;
	}
	public void setRatio(float ratio) {
		Ratio = ratio;
	}

}
