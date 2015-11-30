package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-27 下午1:41:50  
 * 程序的简单说明  关注的人信息
 */

public class LikeUsersInfoBean implements Serializable{

	int Count;
	//用户信息
	List<UsersInfoBean> Users=new ArrayList<UsersInfoBean>();
	public List<UsersInfoBean> getUsers() {
		return Users;
	}

	public void setUsers(List<UsersInfoBean> users) {
		Users = users;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

}
