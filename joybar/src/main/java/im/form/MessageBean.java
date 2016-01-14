package im.form;

import java.io.Serializable;

/*****
 * 发生消息信息对象
 * ****/
public class MessageBean implements Serializable{

	String fromUserId="";//发送方ID
	String toUserId="";//接收方id
	String userName="";//发送方名称
	String productId="";//商品id
	String fromUserType="";//用户类型

	String body="";//信息内容
	String logo="";//图片
	String sharelink="";
	String roomId="";
	String messageType="";//【0、私聊  1 群聊 】所在的组（私聊 ，群聊）

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getSharelink() {
		return sharelink;
	}
	public void setSharelink(String sharelink) {
		this.sharelink = sharelink;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getFromUserType() {
		return fromUserType;
	}
	public void setFromUserType(String fromUserType) {
		this.fromUserType = fromUserType;
	}

}
