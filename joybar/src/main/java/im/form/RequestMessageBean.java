package im.form;

import java.io.Serializable;
/*****
 * 信息接收对象
 * ***/
public class RequestMessageBean extends MessageInfoBean implements Serializable{
	
	public final static String type_img="img";//图片
	public final static String type_produtc_img="product_img";//商品图片
	public final static String notice="notice";//notice
	public final static String type_empty="";//空串的是普通消息。


	int productId;
	String sharelink="";

	public String getSharelink() {
		return sharelink;
	}

	public void setSharelink(String sharelink) {
		this.sharelink = sharelink;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
}
