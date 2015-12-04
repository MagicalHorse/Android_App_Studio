package com.shenma.yueba.yangjia.modle;

import java.util.List;

public class CircleDetailBean {

	private String GroupName;
	private String GroupPic;
	private String MemberCount;
	boolean IsOwer=false; // 是否为创建者
	boolean IsMember=false; // 是否已经加入圈子
	String GroupId;//圈子id
	private List<Users> Users;

	public String getGroupId() {
		return GroupId;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

	public boolean isIsOwer() {
		return IsOwer;
	}

	public void setIsOwer(boolean isOwer) {
		IsOwer = isOwer;
	}

	public boolean isIsMember() {
		return IsMember;
	}

	public void setIsMember(boolean isMember) {
		IsMember = isMember;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public String getGroupPic() {
		return GroupPic;
	}

	public void setGroupPic(String groupPic) {
		GroupPic = groupPic;
	}

	public String getMemberCount() {
		return MemberCount;
	}

	public void setMemberCount(String memberCount) {
		MemberCount = memberCount;
	}

	public List<Users> getUsers() {
		return Users;
	}

	public void setUsers(List<Users> users) {
		Users = users;
	}

}
