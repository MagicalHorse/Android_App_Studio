package im.form;

import java.io.Serializable;
/*****
 * 信息接收对象
 * ***/
public class RequestMessageBean extends MessageBean implements Serializable {

	public final static String type_img = "img";//图片
	public final static String type_produtc_img = "product_img";//商品图片
	public final static String notice = "notice";//notice
	public final static String type_empty = "";//空串的是普通消息。
	MessageUserInfoBean user = new MessageUserInfoBean();

	public MessageUserInfoBean getUser() {
		return user;
	}

	public void setUser(MessageUserInfoBean user) {
		this.user = user;
	}
}
