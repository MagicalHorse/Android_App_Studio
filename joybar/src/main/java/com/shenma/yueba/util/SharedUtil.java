package com.shenma.yueba.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 公共数据存放类
 * 
 * @author zhou
 * 
 */
public class SharedUtil {
	private static final String updateUrl = "updateUrl";//版本更新地址
	public static final String install_flag = "install_flag";
	public static final String user_id = "user_id";//登录用户id
	public static final String user_level = "level";//用户级别
	public static final String store_id = "store_id";
	public static final String user_name = "user_name";//登录用户名称
	public static final String user_names = "user_names";//登录用户昵称
	public static final String user_mobile = "user_mobile";//登录用户手机号
	public static final String user_logo = "user_logo";//登录用户头像（缩略图）
	public static final String user_logo_full = "user_logo_full";///登录用户头像（大图）
	public static final String user_logobg_s = "user_logobg_s";///登录用户背景
	public static final String user_logobg = "user_logobg";///登录用户背景
	public static final String user_token = "user_token";///登录用户令牌
	public static final String user_loginstatus = "user_loginstatus";//用户登录状态


	public static final String search_history ="search_history";//商品搜索历史记录
	/**
	 * -2表示还没有申请过认证买手  ，-1表示身亲被拒绝  ，0表示正在申请中，1表示申请通过
	 */
	public static final String user_AuditStatus = "AuditStatus";//用户审核状态
	
	public static final String user_first = "firstFlag";//是否是第一次打开软件
	
	public static final String user_IsBindMobile = "IsBindMobile";//是否绑定手机号
	
	public static final String user_IsBindWeiXin = "IsBindWeiXin";//是否绑定微信号
	public static final String user_canPush = "IsOpenPush";//是否绑定微信号
	public static final String user_Description = "Description";//店铺说明
	
	
	
	private static final String user_password = "user_password";
	private static final String weibo_sina = "weibo_sina";
	private static final String weibo_qq = "weibo_qq";
	private static final String social_msg = "social_msg";
	private static final String system_msg = "system_msg";
	private static final String friend_msg = "friend_msg";
	private static final String user_type = "user_type";
	private static final String friend_circle = "friend_circle";
	private static final String weixin_friends = "weixin_friends";
	private static final String headImage = "headImage";// 用户头像
	private static final String session_id = "session_id";
	public static final String key = "key";// 阿里云需要的key
	public static final String sign = "sign";//阿里云需要的sign

	private static final String current_city_id = "current_city_id";//当前城市ID
	public static String getStringPerfernece(Context context,String str) {
		return getSharedPreferences(context).getString(str, "");
	}

	public static void setStringPerfernece(Context context, String str1,String value) {
		if (str1 != null) {
			getSharedPreferences(context).edit().putString(str1, value).commit();
		}
	}
	
	public static boolean getBooleanPerfernece(Context context,String str) {
		return getSharedPreferences(context).getBoolean(str, false);
	}

	public static void setBooleanPerfernece(Context context, String str1,boolean value) {
		if (str1 != null) {
			getSharedPreferences(context).edit().putBoolean(str1, value).commit();
		}
	}
	

	public static boolean getInstallFlag(Context context) {
		return getSharedPreferences(context).getBoolean(install_flag, false);
	}

	public static void setInstallFlag(Context context) {
		getSharedPreferences(context).edit().putBoolean(install_flag, true)
				.commit();
	}

	public static String getStoreId(Context context) {
		return getSharedPreferences(context).getString(store_id, null);
	}

	public static void setStoreId(Context context, String storeId) {
		if (storeId != null) {
			getSharedPreferences(context).edit().putString(store_id, storeId)
					.commit();
		}
	}

	public static String getUserId(Context context) {
		return getSharedPreferences(context).getString(user_id, null);
	}

	public static void setUserId(Context context, String userId) {
		if (userId != null) {
			getSharedPreferences(context).edit().putString(user_id, userId)
					.commit();
		}
	}

	public static String getHeadImage(Context context) {
		return getSharedPreferences(context).getString(user_logo, null);
	}

	public static void setHeadImage(Context context, String img) {
		if (img != null) {
			getSharedPreferences(context).edit().putString(user_logo, img)
					.commit();
		}
	}

	public static String getUserName(Context context) {
		return getSharedPreferences(context).getString(user_name, "");
	}

	public static void setUserName(Context context, String userName) {
		if (userName != null) {
			getSharedPreferences(context).edit().putString(user_name, userName)
					.commit();
		}
	}
	public static String getUserNames(Context context) {
		return getSharedPreferences(context).getString(user_names, "");
	}

	public static void setUserNames(Context context, String userName) {
		if (userName != null) {
			if(TextUtils.isEmpty(getUserNames(context))){
				getSharedPreferences(context).edit().putString(user_names, userName+"-")
				.commit();
			}else{
				String usernames = getUserNames(context);
				getSharedPreferences(context).edit().putString(user_names, usernames+userName+"-")
				.commit();
			}
			
		}
	}

	public static String getUserPassword(Context context) {
		return getSharedPreferences(context).getString(user_password, null);
	}

	public static void setUserPassword(Context context, String userPassword) {
		if (userPassword != null) {
			getSharedPreferences(context).edit()
					.putString(user_password, userPassword).commit();
		}
	}
	
	
	
	

	public static boolean getWeiboSina(Context context) {
		return getSharedPreferences(context).getBoolean(weibo_sina, true);
	}

	public static void setWeiboSina(Context context, boolean sina) {
		getSharedPreferences(context).edit().putBoolean(weibo_sina, sina)
				.commit();
	}

	public static boolean getWeiboQQ(Context context) {
		return getSharedPreferences(context).getBoolean(weibo_qq, true);
	}

	public static void setWeiboQQ(Context context, boolean qq) {
		getSharedPreferences(context).edit().putBoolean(weibo_qq, qq).commit();
	}

	public static boolean getSocialMsgState(Context context) {
		return getSharedPreferences(context).getBoolean(social_msg, true);
	}

	public static void setSocialMsgState(Context context, boolean socialMsgState) {
		getSharedPreferences(context).edit()
				.putBoolean(social_msg, socialMsgState).commit();
	}

	public static boolean getWeixinFriends(Context context) {
		return getSharedPreferences(context).getBoolean(weixin_friends, true);
	}

	public static void setWeixinFriends(Context context, boolean weixin) {
		getSharedPreferences(context).edit().putBoolean(weixin_friends, weixin)
				.commit();
	}

	public static boolean getFriendCircle(Context context) {
		return getSharedPreferences(context).getBoolean(friend_circle, true);
	}

	public static void setFriendCircle(Context context, boolean weixin) {
		getSharedPreferences(context).edit().putBoolean(friend_circle, weixin)
				.commit();
	}

	public static boolean getSystemMsgState(Context context) {
		return getSharedPreferences(context).getBoolean(system_msg, true);
	}

	public static void setSystemMsgState(Context context, boolean systemMsgState) {
		getSharedPreferences(context).edit()
				.putBoolean(system_msg, systemMsgState).commit();
	}

	public static boolean getFriendMsgState(Context context) {
		return getSharedPreferences(context).getBoolean(friend_msg, true);
	}

	public static void setFriendMsgState(Context context, boolean friendMsgState) {
		getSharedPreferences(context).edit()
				.putBoolean(friend_msg, friendMsgState).commit();
	}

	public static String getUpdateUrl(Context context) {
		return getSharedPreferences(context).getString(updateUrl, "");
	}

	public static void setUpdateUrl(Context context, String update) {
		if (update != null) {
			getSharedPreferences(context).edit().putString(updateUrl, update)
					.commit();
		}
	}
	
	
	public static String getUserType(Context context) {
		return getSharedPreferences(context).getString(user_type, "");
	}

	public static void setUserType(Context context, String type) {
		if (type != null) {
			getSharedPreferences(context).edit().putString(user_type, type)
					.commit();
		}
	}

	public static String getSessionId(Context context) {
		return getSharedPreferences(context).getString(session_id, "");
	}

	public static void setSessionId(Context context, String sessionId) {
		if (sessionId != null) {
			getSharedPreferences(context).edit()
					.putString(session_id, sessionId).commit();
		}
	}

	public static void clearSP(Context context) {
		String username = getUserName(context);
		String usernames = getUserNames(context);
		getSharedPreferences(context).edit().clear().commit();
		setUserName(context, username);
		setUserNames(context, usernames);
	}

	public static String getStoreName(Context context) {
		return getSharedPreferences(context).getString("store_name", "");
	}

	public static void setStoreName(Context context, String sessionId) {
		if (sessionId != null) {
			getSharedPreferences(context).edit()
					.putString("store_name", sessionId).commit();
		}
	}

	
	public static String getAliYunKey(Context context) {
		return getSharedPreferences(context).getString(key, null);
	}

	public static void setAliYunKey(Context context, String userPassword) {
		if (userPassword != null) {
			getSharedPreferences(context).edit()
					.putString(key, userPassword).commit();
		}
	}
	
	public static String getAliYunSign(Context context) {
		return getSharedPreferences(context).getString(sign, null);
	}
	
	public static void setAliYunSign(Context context, String userPassword) {
		if (userPassword != null) {
			getSharedPreferences(context).edit()
			.putString(sign, userPassword).commit();
		}
	}
	
	public static String getAuditStatus(Context context) {
		return getSharedPreferences(context).getString(user_AuditStatus, "");
	}
	
	public static void setAuditStatus(Context context, String suditStatus) {
		if (suditStatus != null) {
			getSharedPreferences(context).edit()
			.putString(user_AuditStatus, suditStatus).commit();
		}
	}
	
	// 保存用户信息
	public static void setUserInfo(Context mContext, UserBean user,
			String username, String password) {
			setUserId(mContext, user.getUserId());
	}

	/**
	 * Get SharedPreferences
	 */
	private static SharedPreferences getSharedPreferences(Context context) {
		return MyPreference.getPreference(context);
	}



	public  static void setCurrentCityId(Context context, String cityId){
		if (cityId != null) {
			getSharedPreferences(context).edit()
					.putString(current_city_id, cityId).commit();
		}
	}

	public static String getCurrentCityId(Context context){
		return getSharedPreferences(context).getString(current_city_id, "");

	}

}
