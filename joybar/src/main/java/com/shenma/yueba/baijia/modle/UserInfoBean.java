package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-17 下午2:47:22  
 * 程序的简单说明  败家 获取用户信息
 */

public class UserInfoBean implements Serializable{

	boolean IsBuyer=false;//是否为买手
	boolean IsFollowing=false;//是否已关注,
	String Address="";//地址 （格式："门店"-"专柜"）,
	int NewProductCount;//上新数 
	String UserName="";//萝卜天使",    用户昵称
	String Description="";//好东西顶顶顶",     买手描述
	int FollowingCount;// 关注人数
	int FollowerCount;// 粉丝人数
	int GoodCount;// 赞过的数量
	int FavoriteCount;//收藏数
	int CommunityCount;// 圈子个数
	String Logo="";//头像
	int ProductCount;//商品总数
	boolean IsBindMobile=false;//是否绑定过手机号
	String StoreName="";//门店名称

	public String getSectionName() {
		return SectionName;
	}

	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	String SectionName="";//专柜名称


	public boolean isBindMobile() {
		return IsBindMobile;
	}

	public void setIsBindMobile(boolean isBindMobile) {
		IsBindMobile = isBindMobile;
	}
	public int getProductCount() {
		return ProductCount;
	}
	public void setProductCount(int productCount) {
		ProductCount = productCount;
	}
	public String getLogo() {
		return Logo;
	}
	public void setLogo(String logo) {
		Logo = logo;
	}
	public boolean isIsBuyer() {
		return IsBuyer;
	}
	public void setIsBuyer(boolean isBuyer) {
		IsBuyer = isBuyer;
	}
	public boolean isIsFollowing() {
		return IsFollowing;
	}
	public void setIsFollowing(boolean isFollowing) {
		IsFollowing = isFollowing;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getNewProductCount() {
		return NewProductCount;
	}
	public void setNewProductCount(int newProductCount) {
		NewProductCount = newProductCount;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getFollowingCount() {
		return FollowingCount;
	}
	public void setFollowingCount(int followingCount) {
		FollowingCount = followingCount;
	}
	public int getFollowerCount() {
		return FollowerCount;
	}
	public void setFollowerCount(int followerCount) {
		FollowerCount = followerCount;
	}
	public int getGoodCount() {
		return GoodCount;
	}
	public void setGoodCount(int goodCount) {
		GoodCount = goodCount;
	}
	public int getFavoriteCount() {
		return FavoriteCount;
	}
	public void setFavoriteCount(int favoriteCount) {
		FavoriteCount = favoriteCount;
	}
	public int getCommunityCount() {
		return CommunityCount;
	}
	public void setCommunityCount(int communityCount) {
		CommunityCount = communityCount;
	}

}
