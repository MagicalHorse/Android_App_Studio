package im.form;

import java.io.Serializable;

import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import android.content.Context;
import im.control.ChatBaseManager;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-3 下午3:56:30  
 * 程序的简单说明  基础信息类
 */

public abstract class  BaseChatBean implements Serializable{
	Context context;
	/*****
	 * @param chattype ChatType 发送消息的文本类型(根据此类型 设置 视图的样式)
	 * @param type  String （消息的类型  即 发送消息时 type 对应的信息）
	 * ***/
	public BaseChatBean(ChatType chattype,String type,Context context)
	{
		this.chattype=chattype;
		this.type=type;
		this.context=context;
	}
	
	/***
	 * 发送消息的文本类型
	 * **/
	public enum ChatType
	{
		link_type,//链接信息
		pic_type,//图片信息
		text_trype,//文本信息
		notice_type//推广信息
	}
	
	
	public enum SendStatus
	{
		send_unsend,//未发送
		send_sucess,//发送成功
		send_loading,//发送中
		send_fails//发送失败
	}
	
	String room_No="";//房间号
	int from_id;//发送者id
	int to_id;//接受者id
	Object content;//信息内容
	boolean isoneself=false;//是否是自己发送的信息
	ChatBaseManager baseManager;
	
	SendStatus sendStatus=SendStatus.send_unsend;
	ChatType chattype;//信息类型
	String userName="";
	String logo="";
	int productId;
	String type="";//信息的类型 默认为 文本    sss
	String creationDate="";
	String sharelink="";

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	int messageType;//【0、私聊  1 群聊 】



	public String getSharelink() {
		return sharelink;
	}
	public void setSharelink(String sharelink) {
		this.sharelink = sharelink;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ChatType getChattype() {
		return chattype;
	}
	public void setChattype(ChatType chattype) {
		this.chattype = chattype;
	}
	public SendStatus getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(SendStatus sendStatus) {
		this.sendStatus = sendStatus;
	}
	public ChatBaseManager getBaseManager() {
		return baseManager;
	}
	public void setBaseManager(ChatBaseManager baseManager) {
		this.baseManager = baseManager;
	}
	public String getRoom_No() {
		return room_No;
	}
	public void setRoom_No(String room_No) {
		this.room_No = room_No;
	}
	public int getFrom_id() {
		return from_id;
	}
	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}
	public int getTo_id() {
		return to_id;
	}
	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}
	
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public boolean isIsoneself() {
		return isoneself;
	}
	public void setIsoneself(boolean isoneself) {
		this.isoneself = isoneself;
	}
	
	/******
	 * 发送数据
	 * ***/
	public abstract void sendData();
	/***
	 * 赋值 讲 接收到的消息数据 进行赋值
	 * **/
	public abstract void setValue(Object bean);
	
	/*****
	 * 通用信息赋值
	 * ***/
	public void setParentView(Object obj)
	{
		if(obj!=null && obj instanceof RequestMessageBean)
		{
			RequestMessageBean bean=(RequestMessageBean)obj;
			from_id=bean.getFromUserId();
			to_id=bean.getToUserId();
			room_No=bean.getRoomId();
			type=Integer.toString(bean.getMessageType());
			MessageUserInfoBean userbean=bean.getUser();
			if(userbean!=null)
			{
				userName=userbean.getNickName();
				logo=userbean.getLogo();
			}
			productId=bean.getProductId();
			content=bean.getBody();
			creationDate=ToolsUtil.nullToString(bean.getCreationDate());
			String user_id=SharedUtil.getStringPerfernece(MyApplication.getInstance().getApplicationContext(), SharedUtil.user_id);
			if(user_id.equals(Integer.toString(from_id)))
			{
				isoneself=true;
			}else
			{
				isoneself=false;
			}
			sendStatus=SendStatus.send_sucess;
		}
	}
	
	/*****
	 * 获取 传递消息的对象
	 * ****/
	public MessageBean getMessageBean()
	{
		MessageBean bean=new MessageBean();
		bean.setRoomId(room_No);
		bean.setBody((String)content);
		bean.setFromUserId(Integer.toString(from_id));
		bean.setProductId(Integer.toString(productId));
		bean.setToUserId(Integer.toString(to_id));
		bean.setMessageType(type);
		bean.setUserName(userName);
		bean.setLogo(logo);
		bean.setSharelink((String)content);
		return bean;
	}
}
