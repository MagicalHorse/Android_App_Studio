package com.shenma.yueba.yangjia.modle;

public class Users {

	public enum User_Type
	{
		data,//普通数据
		jia,//添加
		jian//减
	}
	private String UserId;
	private String Nickname;
	private String Logo;
	private boolean showDelete;
	private User_Type user_Type=User_Type.data;
	
	
	public User_Type getUser_Type() {
		return user_Type;
	}
	public void setUser_Type(User_Type user_Type) {
		this.user_Type = user_Type;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getNickName() {
		return Nickname;
	}
	public void setNickName(String nickName) {
		Nickname = nickName;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public boolean isShowDelete() {
		return showDelete;
	}
	public void setShowDelete(boolean showDelete) {
		this.showDelete = showDelete;
	}
	
	
	
}
