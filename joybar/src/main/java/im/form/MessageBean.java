package im.form;

import java.io.Serializable;

/*****
 * 发生消息信息对象
 * ****/
public class MessageBean implements Serializable{
	String _id="";//消息id
	int fromUserId;//发送方ID
	int toUserId;//接收方id
	String userName="";//发送方名称
	int productId;//商品id
	String fromUserType="";//用户类型

	String body="";//信息内容
	String logo="";//图片
	String sharelink="";
	String roomId="";
	int messageType=0;//【0、私聊  1 群聊 】所在的组（私聊 ，群聊）
	String type="";//信息类型
	String creationDate="";


	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getFromUserType() {
		return fromUserType;
	}

	public void setFromUserType(String fromUserType) {
		this.fromUserType = fromUserType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getSharelink() {
		return sharelink;
	}

	public void setSharelink(String sharelink) {
		this.sharelink = sharelink;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
