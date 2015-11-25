package com.shenma.yueba.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.ApplyAuthBuyerBean;
import com.shenma.yueba.baijia.modle.BaiJiaCheckBuyerStatusBeanRequest;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.BrandDetailInfoBean;
import com.shenma.yueba.baijia.modle.BuyerIndexInfoBean;
import com.shenma.yueba.baijia.modle.CheckVersionBackBean;
import com.shenma.yueba.baijia.modle.CityInfoBackBean;
import com.shenma.yueba.baijia.modle.CityListBackBean;
import com.shenma.yueba.baijia.modle.CityListRequestBean;
import com.shenma.yueba.baijia.modle.GetUserFlowStatusBackBean;
import com.shenma.yueba.baijia.modle.HuoKuanManagerBackBean;
import com.shenma.yueba.baijia.modle.ModifyLogoBackBean;
import com.shenma.yueba.baijia.modle.MyRequestProductListInfoBean;
import com.shenma.yueba.baijia.modle.ProvinceCityListBeanRequest;
import com.shenma.yueba.baijia.modle.RequestBaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.baijia.modle.RequestBaiJiaOrderListInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandCityWideInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandInfoInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandSearchInfoBean;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.baijia.modle.RequestComputeAmountInfoBean;
import com.shenma.yueba.baijia.modle.RequestCreatOrderInfoBean;
import com.shenma.yueba.baijia.modle.RequestImMessageInfoBean;
import com.shenma.yueba.baijia.modle.RequestMsgListInfoBean;
import com.shenma.yueba.baijia.modle.RequestMyCircleInfoBean;
import com.shenma.yueba.baijia.modle.RequestMyFavoriteProductListInfoBean;
import com.shenma.yueba.baijia.modle.RequestMyInfoBean;
import com.shenma.yueba.baijia.modle.RequestProductDetailsInfoBean;
import com.shenma.yueba.baijia.modle.RequestProductListInfoBean;
import com.shenma.yueba.baijia.modle.RequestRoomInfoBean;
import com.shenma.yueba.baijia.modle.RequestTuiJianCircleInfoBean;
import com.shenma.yueba.baijia.modle.RequestUploadChatImageInfoBean;
import com.shenma.yueba.baijia.modle.RequestUploadProductDataBean;
import com.shenma.yueba.baijia.modle.RequestUserDynamicInfoBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.baijia.modle.StoreListBackBean;
import com.shenma.yueba.baijia.modle.TagListBackBean;
import com.shenma.yueba.baijia.modle.UserInfo;
import com.shenma.yueba.baijia.modle.UserRequestBean;
import com.shenma.yueba.baijia.modle.newmodel.BinnerBackBean;
import com.shenma.yueba.baijia.modle.newmodel.IndexBackBean;
import com.shenma.yueba.baijia.modle.newmodel.MoreBrandBackBean;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexBackBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.constants.HttpConstants;
import com.shenma.yueba.yangjia.modle.AliYunKeyBackBean;
import com.shenma.yueba.yangjia.modle.AttationAndFansListBackBean;
import com.shenma.yueba.yangjia.modle.BuyerProductManagerListBack;
import com.shenma.yueba.yangjia.modle.CircleDetailBackBean;
import com.shenma.yueba.yangjia.modle.CircleListBackBean;
import com.shenma.yueba.yangjia.modle.ContactsAddressRequestBean;
import com.shenma.yueba.yangjia.modle.ContactsAddressRequestListBean;
import com.shenma.yueba.yangjia.modle.ContactsAddressResponseBean;
import com.shenma.yueba.yangjia.modle.FansBackListForInviteCirlce;
import com.shenma.yueba.yangjia.modle.HuoKuanListBackBean;
import com.shenma.yueba.yangjia.modle.IncomeDetailBackBean;
import com.shenma.yueba.yangjia.modle.IncomeHistoryBackBean;
import com.shenma.yueba.yangjia.modle.KaiXiaoPiaoBackBean;
import com.shenma.yueba.yangjia.modle.OrderDetailBackBean;
import com.shenma.yueba.yangjia.modle.OrderListBackBean;
import com.shenma.yueba.yangjia.modle.RewardDetailBackBean;
import com.shenma.yueba.yangjia.modle.TastRewardListBackBean;

/**
 * @author gyj
 * @version 创建时间：2015-5-10 下午4:29:03 程序的简单说明:本类定义 Http控制类 用于各种Http访问的的方法处理
 *          {@link HttpCallBackInterface}
 * @see #getCityList(HttpCallBackInterface, Context)
 * @see #getAllCityList(HttpCallBackInterface, Context)
 * @see #setLoginInfo(Context, UserRequestBean)
 * @see #createContactAddress(HttpCallBackInterface, Context,
 *      ContactsAddressResponseBean)
 * @see #registerUserInfo(String, String, String, int, HttpCallBackInterface,
 *      Context)
 * @see #validVerifyCode(String, String, HttpCallBackInterface, Context)
 * @see #updateLoginPwd(String, String, String, HttpCallBackInterface, Context)
 * @see #getContactAddressDetails(HttpCallBackInterface, Context, int)
 * @see #getDefaultContactAddress(HttpCallBackInterface, Context, int)
 * @see #getDeleteContactAddress(HttpCallBackInterface, Context, int)
 * @see #getMyContactAddressList(HttpCallBackInterface, Context, int)
 * @see #getUpdateContactAddress(HttpCallBackInterface, Context,
 *      ContactsAddressResponseBean)
 * @see #setDefaultContactAddress(HttpCallBackInterface, Context, int)
 * 
 */
public class HttpControl {
	private HttpUtils httpUtils;
	private HttpControl httpControl;
	private final String setContentType = "application/json;charset=UTF-8";

	/****
	 * 创建对象
	 * **/
	/*
	 * public static HttpControl the() { if (httpControl == null) { httpControl
	 * = new HttpControl(); } httpControl = new HttpControl(); return
	 * httpControl; }
	 */

	public HttpControl() {
		httpUtils = new HttpUtils(30000);
		httpUtils.configSoTimeout(30000);
		httpUtils.configTimeout(30000);
	}


	/**
	 * 获取binner数据
	 * @param httpCallBack
	 * @param context
	 */
	public void getBinner(final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_BannerSubject, httpCallBack,
				BinnerBackBean.class, false, false);
	}




	/**
	 * 获取首页数据
	 * @param CityId String 城市ID 不传则默认0  返回全国数据
	 * @param  Page int 当前页大小
	 * @param  PageSize int 每页显示的个数
	 * @param httpCallBack
	 * @param context
	 */
	public void getIndex(String CityId,int Page,int PageSize,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("CityId",CityId);
		map.put("Page",Integer.toString(Page));
		map.put("PageSize",Integer.toString(PageSize));
		BasehttpSend(map, context, HttpConstants.METHOD_index, httpCallBack, IndexBackBean.class, false, false);
	}

	/**
	 * 商场首页商品列表
	 * @param StoreId
	 * @param UserId
	 * @param Page
	 * @param PageSize
	 * @param SortType
	 * @param httpCallBack
	 * @param context
	 */
	public void getStoreIndex(String StoreId,String UserId,int Page,int PageSize,String SortType,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("StoreId",StoreId);
		map.put("UserId",UserId);
		map.put("SortType",SortType);
		map.put("Page",Integer.toString(Page));
		map.put("PageSize",Integer.toString(PageSize));
		BasehttpSend(map, context, HttpConstants.METHOD_storeindex, httpCallBack, StoreIndexBackBean.class, false, false);
	}


	/**
	 * 门店下的更多品牌
	 * @param StoreId
	 * @param Page
	 * @param PageSize
	 * @param httpCallBack
	 * @param context
	 */
	public void getMoreBrands(String StoreId,int Page,int PageSize,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("StoreId",StoreId);
		map.put("Page",Integer.toString(Page));
		map.put("PageSize",Integer.toString(PageSize));
		BasehttpSend(map, context, HttpConstants.METHOD_storebrand, httpCallBack, MoreBrandBackBean.class, false, false);
	}


	/**
	 * 搜商场
	 * @param Page
	 * @param PageSize
	 * @param httpCallBack
	 * @param context
	 *

	 */
	public void searchMarket(String key,String cityId,String longitude,String latitude,int Page,int PageSize,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key",key);
		map.put("cityId",cityId);
		map.put("longitude",longitude);
		map.put("latitude",latitude);
		map.put("Page",Integer.toString(Page));
		map.put("PageSize",Integer.toString(PageSize));
		BasehttpSend(map, context, HttpConstants.METHOD_storebrand, httpCallBack, MoreBrandBackBean.class, false, false);
	}


	/**
	 *  发送手机验证码
	 * @param str
	 * @param type 0表示注册，1表示找回密码，2表示绑定手机号
	 * @param httpCallBack
	 * @param context
	 */
	
	
	public void sendPhoeCode(String str,String type,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, str.trim());
		map.put(Constants.TYPE, type);
		BasehttpSend(map, context, HttpConstants.sendPhoneCode, httpCallBack,
				BaseRequest.class, true, false);
	}


	/**
	 * 修改昵称
	 * @param nickname
	 * @param showDialog
	 * @param isCancal
	 * @param httpCallBack
	 * @param context
	 */
	public void modifyNickName(String nickname,boolean showDialog,boolean isCancal,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.NICKNAME, nickname.trim());
		BasehttpSend(map, context, HttpConstants.changeNickName, httpCallBack,
				BaseRequest.class, true, false);
	}
	
	
	/**
	 * 修改用户头像
	 * @param httpCallBack
	 * @param context
	 *
	 */
	public void modifyUserLogo(String logo,boolean showDialog,boolean isCancal,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.LOGO, logo);
		BasehttpSend(map, context, HttpConstants.changeUserLogo, httpCallBack,
				ModifyLogoBackBean.class, false, false);
	}
	
	
	/**
	 * 校验手机验证码
	 * 
	 * @param phone
	 *            String 手机号码
	 * @param code
	 *            String验证码
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void validVerifyCode(String phone, String code,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, phone.trim());
		map.put(Constants.CODE, code.trim());
		BasehttpSend(map, context, HttpConstants.METHOD_VERIFYCODE,
				httpCallBack, BaseRequest.class, true, false);
	}

	/**
	 * 获取城市列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void getCityList(final HttpCallBackInterface httpCallBack,
			Context context) {
		BasehttpSend(null, context, HttpConstants.METHOD_GETCITYLIST,
				httpCallBack, CityListRequestBean.class, true, false);
	}

	/**
	 *  根据经纬度获取城市信息
	 */

	public void getCityInfoById(final HttpCallBackInterface httpCallBack,Context context,String longitude,String latitude){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.LONGITUDE,longitude);//经度
		map.put(Constants.LATITUDE,latitude);//维度
		BasehttpSend(map, context, HttpConstants.METHOD_GETCITYBYID,
				httpCallBack, CityInfoBackBean.class, false, false);
	}
	/**
	 *  获取开通SHOPPING的城市列表
	 */

	public void getShoppingCityList(final HttpCallBackInterface httpCallBack,Context context){
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.GETALLSHOPPINGCITY,
				httpCallBack, CityListBackBean.class, true, false);
	}

	/**
	 * 版本检查更新
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void checkVersion(final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TYPE,"0");//0表示android 1表示IOS
		BasehttpSend(map, context, HttpConstants.METHOD_VERSION_UPDATE,
				httpCallBack, CheckVersionBackBean.class, false, false);
	}

	
	
	
	
	/**
	 * 获取阿里云key
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void getALiYunKey(final HttpCallBackInterface httpCallBack,
			Context context) {
		BasehttpSend(null, context, HttpConstants.METHOD_GETALIYUNKEY,
				httpCallBack, AliYunKeyBackBean.class, false, false);
	}

	/**
	 * 获取省市地区所有数据
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void getAllCityList(final HttpCallBackInterface httpCallBack,
			Context context) {
		BasehttpSend(null, context, HttpConstants.METHOD_ALLGETCITYLIST,
				httpCallBack, ProvinceCityListBeanRequest.class, true, false);
	}

	/**
	 * 根据ID获取地区列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void getCityListById(String parentId,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PARENTID, parentId);
		BasehttpSend(map, context,
				HttpConstants.METHOD_COMMON_GETCITYLISYBYPARENTID,
				httpCallBack, CityListBackBean.class, true, false);
	}

	/**
	 * 注册用户信息
	 * 
	 * @param phone
	 *            String手机号码
	 * @param name
	 *            String用户名
	 * @param pwd
	 *            String密码
	 * @param cityId
	 *            String 城市ID
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void registerUserInfo(String phone, String name, String pwd,
			int cityId, final HttpCallBackInterface httpCallBack,
			final Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.NAME, name.trim());
		map.put(Constants.MOBILE, phone.trim());
		map.put(Constants._PASSWORD, pwd.trim());
		map.put(Constants.CITYID, Integer.toString(cityId));
		BasehttpSend(map, context, HttpConstants.METHOD_REGISTER, httpCallBack,
				UserRequestBean.class, true, false);
	}

	/**
	 * 登录用户
	 * 
	 * @param phone
	 *            String手机号码
	 * @param password
	 *            String密码
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void userLogin(String phone, String password,
			final HttpCallBackInterface httpCallBack, final Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, phone.trim());
		map.put(Constants._PASSWORD, password.trim());
		BasehttpSend(map, context, HttpConstants.METHOD_LOGIN, httpCallBack,
				UserRequestBean.class, true, false);
	}

	/**
	 * 修改登录密码
	 * 
	 * @param phone
	 *            String手机号码
	 * @param oldpassword
	 *            String旧密码
	 * @param password
	 *            String新密码
	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void updateLoginPwd(String phone, String password,
			String oldpassword, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, phone.trim());
		map.put(Constants._PASSWORD, password.trim());
		map.put(Constants._OLDPASSWORD, oldpassword.trim());
		BasehttpSend(map, context, HttpConstants.METHOD_UPDATEPWD,
				httpCallBack, BaseRequest.class, true, false);
	}

	/**
	 * 重置密码
	 * 
	 * @param phone
	 *            String手机号码
	 * @param password
	 *            String 密码
	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void resetPassword(String phone, String password,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, phone.trim());
		map.put(Constants._PASSWORD, password.trim());
		BasehttpSend(map, context, HttpConstants.METHOD_RESETPASSWORD,
				httpCallBack, BaseRequest.class, true, false);
	}

	/**
	 * 获取我关注的人和我的粉丝的列表
	 * @param CurrentUserId int 登录用户的id
	 * @param status int   0表示我关注的人   1表示我的粉丝
	 * @param UserID int 用户id
	 * @param UserLevel int (注： 负数 为查询全部)根据UserLevel对关注的人进行筛选，null表示全部，0表示默认登记，1表示普通用户，2表示大人，4表示导购，8表示认证买手
	 * @param httpCallBack  HttpCallBackInterface回调接口
	 * @param context  Context
	 * @return void
	 * **/
	public void getAttationOrFansList(int CurrentUserId,int UserID,int UserLevel,int status, int page,int pageSize,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.STATUS, Integer.toString(status));
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		map.put("CurrentUserId", Integer.toString(CurrentUserId));
		map.put("UserID", Integer.toString(UserID));
		if(UserLevel>-1)
		{
			map.put("UserLevel", Integer.toString(UserLevel));
		}
		BasehttpSend(map, context, HttpConstants.METHOD_GETUSERFAVOITE,
				httpCallBack, AttationAndFansListBackBean.class, showDialog,
				false);
	}

	/**
	 * 创建联系人地址
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param responsebrean
	 *            ContactsAddressResponseBean联系人地址信息
	 * @return void
	 * **/
	public void createContactAddress(final HttpCallBackInterface httpCallBack,
			Context context, ContactsAddressResponseBean responsebrean) {
		String responsejsonstr = BaseGsonUtils.getObjectToJson(responsebrean);
		if (responsejsonstr == null) {
			httpCallBack.http_Fails(500, "发送数据错误");
		}
		basehttpSendToJson(responsejsonstr, null, context,
				HttpConstants.METHOD_ADDRESSCREATE, httpCallBack,
				ContactsAddressRequestBean.class, true, false);
	}

	/**
	 * 获取联系人地址详细信息
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int 指定id
	 * @return void
	 * **/
	public void getContactAddressDetails(
			final HttpCallBackInterface httpCallBack, Context context, int Id) {
		String json = "{'Id':" + Id + "}";
		basehttpSendToJson(json, null, context,
				HttpConstants.METHOD_ADDRESSCREATE_DETAILS, httpCallBack,
				ContactsAddressRequestBean.class, true, false);
	}

	/**
	 * 获取联系人地址列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int
	 * @return void
	 * **/
	public void getMyContactAddressList(
			final HttpCallBackInterface httpCallBack, Context context, int Id) {
		String json = "{'Id':" + Id + "}";
		basehttpSendToJson(json, null, context,
				HttpConstants.METHOD_MYADDRESSCREATE_LIST, httpCallBack,
				ContactsAddressRequestListBean.class, true, false);
	}

	/**
	 * 删除联系人地址详细信息
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int 删除的ID
	 * @return void
	 * **/
	public void getDeleteContactAddress(
			final HttpCallBackInterface httpCallBack, Context context, int Id) {
		String json = "{'id':" + Id + "}";
		basehttpSendToJson(json, null, context,
				HttpConstants.METHOD_ADDRESSCREATE_DELETE, httpCallBack,
				BaseRequest.class, true, false);
	}

	/**
	 * 修改联系人地址详细信息
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param bean
	 *            ContactsAddressResponseBean 修改的数据
	 * @return void
	 * **/
	public void getUpdateContactAddress(
			final HttpCallBackInterface httpCallBack, Context context,
			ContactsAddressResponseBean bean) {
		String responsejsonstr = BaseGsonUtils.getObjectToJson(bean);
		if (responsejsonstr == null) {
			httpCallBack.http_Fails(500, "发送数据错误");
		}
		basehttpSendToJson(responsejsonstr, null, context,
				HttpConstants.METHOD_ADDRESSCREATE_UPDATE, httpCallBack,
				ContactsAddressRequestListBean.class, true, false);
	}

	/**
	 * 设置默认联系人地址详细信息
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int
	 * @return void
	 * **/
	public void setDefaultContactAddress(
			final HttpCallBackInterface httpCallBack, Context context, int Id) {
		String json = "{'id':" + Id + "}";
		basehttpSendToJson(json, null, context,
				HttpConstants.METHOD_ADDRESSCREATE_DEFAULT, httpCallBack,
				BaseRequest.class, true, false);
	}

	/**
	 * 获取默认联系人地址详细信息
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int
	 * @return void
	 * **/
	public void getDefaultContactAddress(
			final HttpCallBackInterface httpCallBack, Context context, int Id) {
		basehttpSendToJson(null, null, context,
				HttpConstants.METHOD_ADDRESSCREATE_GETDEFAULT, httpCallBack,
				ContactsAddressRequestBean.class, true, false);
	}

	/**
	 * 获取品牌信息详细
	 * @param httpCallBack
	 * @param context
	 * @param BrandId
	 */
	public void getBrandDetailInfo(final HttpCallBackInterface httpCallBack,
			Context context, int BrandId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.BRANDID, BrandId + "");
		BasehttpSend(map, context, HttpConstants.METHOD_BRANDMANAGEER_DETAIL,
				httpCallBack, BrandDetailInfoBean.class, true, false);
	}


	/**
	 * 根据关键字搜索商品
	 * 0：销量从小到大
	 1：销量从大到小
	 2：价格从低到高
	 3：价格从高到低
	 4：创建时间正序,先创建的在最后
	 5：创建时间倒序,先创建的在前面

	 */
	public void getProductListByKey(final HttpCallBackInterface httpCallBack,
									Context context, String key,String cityId,String page,String sort){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, page);
		map.put(Constants.PAGESIZE, Constants.PageSize);
		map.put(Constants.SORT,sort);
		map.put(Constants.CITYID,cityId);
		map.put(Constants.KEY,key);
		BasehttpSend(map, context, HttpConstants.METHOD_GET_PRODUCT_BY_KEY,
				httpCallBack, RequestProductListInfoBean.class, true, false);

	}


	/**
	 * 根据名称搜索标签
	 * @param name
	 * @param type
	 * @param httpCallBack
	 * @param context
	 * @param BrandId
	 */
	public void getProductTag(String name,String type,final HttpCallBackInterface httpCallBack,
			Context context, int BrandId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.NAME, name);
		map.put(Constants.TYPE, type);
		BasehttpSend(map, context, HttpConstants.METHOD_GETPRODUCTTAG,
				httpCallBack, TagListBackBean.class, false, false);
	}

	/**
	 * 获取品牌列表
	 * @param httpCallBack
	 * @param context
	 * @param type
	 * @param refreshts
	 */
	public void getBrandList(final HttpCallBackInterface httpCallBack,
			Context context, String type, String refreshts) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TYPE, type);
		map.put(Constants.REFRESHTS, refreshts);
		BasehttpSend(map, context, HttpConstants.METHOD_BRANDMANAGEER_ALL,
				httpCallBack, BrandDetailInfoBean.class, true, false);
	}

	/**
	 * 按照时间获取传入时间后的品牌列表
	 * @param httpCallBack
	 * @param context
	 * @param type
	 * @param refreshts
	 */
	public void getRecentBrandList(final HttpCallBackInterface httpCallBack,
			Context context, String type, String refreshts) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.REFRESHTS, refreshts);
		BasehttpSend(map, context, HttpConstants.METHOD_BRANDMANAGEER_REFRESH,
				httpCallBack, BrandDetailInfoBean.class, true, false);
	}

	/**
	 * 按照时间获取传入时间后的品牌列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param Id
	 *            int
	 * @return void
	 * **/
	// public void getRecentBrandList(final HttpCallBackInterface
	// httpCallBack,Context context,String type,String refreshts) {
	// Map<String, String> map=new HashMap<String, String>();
	// map.put(Constants.REFRESHTS, refreshts);
	// BasehttpSend(map, context, HttpConstants.METHOD_BRANDMANAGEER_GROUPALL,
	// httpCallBack, BrandDetailInfoBean.class, true, true);
	// }

	/**
	 * 买手首页统计信息
	 * @param httpCallBack
	 * @param context
	 * @param refresh
	 * @param canCancle
	 */
	public void getBuyerIndexInfo(final HttpCallBackInterface httpCallBack,
			Context context, boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_BUYER_INDEX,
				httpCallBack, BuyerIndexInfoBean.class, refresh, canCancle);
	}

	/**
	 * 获取门店列表
	 *
	 * @param httpCallBack
	 * @param context
	 */
	public void getStoreList(final HttpCallBackInterface httpCallBack,
			Context context, boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_BUYER_GET_STORE_LIST,
				httpCallBack, StoreListBackBean.class, refresh, canCancle);
	}

	/**
	 * 货款管理信息
	 * @param httpCallBack
	 * @param context
	 * @param refresh
	 * @param canCancle
	 */
	public void getHuoKuanManagerInfo(final HttpCallBackInterface httpCallBack,
			Context context, boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_BUYER_PAYMENTGOODS,
				httpCallBack, HuoKuanManagerBackBean.class, refresh, canCancle);
	}


	/**
	 * 货款管理列表
	 * @param page
	 * @param status
	 * @param httpCallBack
	 * @param context
	 * @param refresh
	 * @param canCancle
	 */
	public void getHuoKuanList(int page,String status,final HttpCallBackInterface httpCallBack,
			Context context, boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGESIZE, Constants.PageSize);
		map.put(Constants.PAGE, page+"");
		map.put(Constants.STATUS, status);
		BasehttpSend(map, context, HttpConstants.METHOD_BUYER_PAYMENTGOODLIST,
				httpCallBack, HuoKuanListBackBean.class, refresh, canCancle);
	}


	/**
	 * 申请认证买手的接口
	 * @param bean
	 * @param httpCallBack
	 * @param context
	 */
	public void getAppliyAuthBuyer(ApplyAuthBuyerBean bean,
			final HttpCallBackInterface httpCallBack, Context context) {
		String responsejsonstr = BaseGsonUtils.getObjectToJson(bean);
		if (responsejsonstr == null) {
			httpCallBack.http_Fails(500, "发送数据错误");
		}
		//basehttpSendToJson(responsejsonstr, null, context,HttpConstants.METHOD_BUYER_CREATE_AUTH_BUYER, httpCallBack,BaseRequest.class, true, false);
		basehttpSendToJson(responsejsonstr, null, context, HttpConstants.METHOD_BUYER_CREATE_AUTH_BUYER, httpCallBack, BaseRequest.class, true, false);
	}


	/**
	 * 获取买手在线商品列表（买手）
	 * @param page
	 * @param pageSize
	 * @param status
	 * @param httpCallBack
	 * @param context
	 * @param refresh
	 * @param canCancle
	 */
	public void getBuyerProductListForOnLine(String page, String pageSize,
			int status, final HttpCallBackInterface httpCallBack,
			Context context, boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, page);
		map.put(Constants.PAGESIZE, pageSize);
		map.put(Constants.STATUS, status + "");
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTLIST,
				httpCallBack, BuyerProductManagerListBack.class, refresh,
				canCancle);
	}

	/**
	 * 上线商品（买手）
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void setProductOnLineOrOffLine(String id, int status,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.ID, id);
		map.put(Constants.STATUS, status + "");
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_ONLINE,
				httpCallBack, BaseRequest.class, refresh, canCancle);
	}

	
	
	
	
	/**
	 * 设置店铺说明
	 * @param description  店铺说明
	 * @param httpCallBack
	 * @param context
	 * @param refresh
	 * @param canCancle
	 */
	public void setStoreIntroduce(String description,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.DESCRIPTION, description);
		BasehttpSend(map, context, HttpConstants.METHOD_BUYER_SETSTOREDESCRIPTION,
				httpCallBack, BaseRequest.class, refresh, canCancle);
	}
	
	
	
	/**
	 * 删除商品（买手）
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void deleteProduct(String id,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean refresh, boolean canCancle) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.ID, id);
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_DELETE,
				httpCallBack, BaseRequest.class, refresh, canCancle);
	}

	/************************** 商品信息 ***********************************/
	/**
	 * 设置收藏或取消收藏商品
	 * 
	 * @param httpCallBack   HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param productId int 商品编号
	 * @param Status int 商品编号 0表示取消收藏   1表示收藏
	 * @return void
	 * **/
	public void setFavor(long productId,int Status,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Id", Long.toString(productId));
		map.put("Status", Integer.toString(Status));
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_FAVOR,httpCallBack, BaseRequest.class, true, false);
	}


	/**
	 * 设置喜欢的商品
	 * 
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param productId  int 商品编号
	 * @param Status  int 0表示取消喜欢   1表示喜欢
	 * @return void
	 * **/
	public void setLike(int productId,int Status,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Id", Integer.toString(productId));
		map.put("Status", Integer.toString(Status));
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_LIKE,httpCallBack, BaseRequest.class, true, false);
	}

	/**
	 * 获取主页商品列表(首页买手街列表（败家)
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param page
	 *            int 请求页
	 * @param pagesize
	 *            int 每页显示的条数
	 * @param isshowDialog
	 *            boolean 是否显示对话框
	 * @param isEnableCancell
	 *            boolean 返回键是否可用
	 * @return void
	 * **/
	public void getProduceHomeListData(int page, int pagesize,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean isshowDialog, boolean isEnableCancell) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		String userid = SharedUtil.getStringPerfernece(context,
				SharedUtil.user_id);
		if (userid.equals("")) {
			map.put(Constants.PRODUCTI_USERID, "0");
		} else {
			map.put(Constants.PRODUCTI_USERID, userid);
		}

		BasehttpSend(map, context,
				HttpConstants.METHOD_PRODUCTMANAGER_HOMELIST, httpCallBack,
				RequestProductListInfoBean.class, isshowDialog, isEnableCancell);
	}

	/**
	 * 获取我的买手数据(我的买手（败家)
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param page
	 *            int 请求页
	 * @param pagesize
	 *            int 每页显示的条数
	 * @param isshowDialog
	 *            boolean 是否显示对话框
	 * @param isEnableCancell
	 *            boolean 返回键是否可用
	 * @return void
	 * **/
	public void getMyBuyerListData(int page, int pagesize,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean isshowDialog, boolean isEnableCancell) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		/*
		 * String userid=SharedUtil.getStringPerfernece(context,
		 * SharedUtil.user_id); if(userid.equals("")) {
		 * map.put(Constants.PRODUCTI_USERID,"0"); }else {
		 * map.put(Constants.PRODUCTI_USERID,userid); }
		 */
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_MYBUYER,
				httpCallBack, MyRequestProductListInfoBean.class, isshowDialog,
				isEnableCancell);
	}

	/**
	 * 获取商品被喜欢的用户列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param productId
	 *            int 商品编号
	 * @param page
	 *            int第几页
	 * @param pagesize
	 *            int每页显示的条数
	 * @return void
	 * **/
	public void setLikedUsers(int productId, int page, int pagesize,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PRODUCTID, Integer.toString(productId));
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		BasehttpSend(map, context,
				HttpConstants.METHOD_PRODUCTMANAGER_LIKEDUSERS, httpCallBack,
				BaseRequest.class, true, false);
	}

	/**
	 * 获取我收藏的买手的商品列表
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param page
	 *            int第几页
	 * @param pagesize
	 *            int每页显示的条数
	 * @return void
	 * **/
	public void getMyBuyerProductList(int page, int pagesize,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		BasehttpSend(map, context,
				HttpConstants.METHOD_PRODUCTMANAGER_MYBUYERPRODUCTLIST,
				httpCallBack, BaseRequest.class, true, false);
	}

	/**
	 * 获取商品信息详情(获取商品详情(败家))
	 * 
	 * @param httpCallBack
	 *            HttpCallBackInterface 回调接口
	 * @param context
	 *            Context
	 * @param ProductId
	 *            int商品编号
	 * @return void
	 * **/
	public void getMyBuyerProductDetails(int ProductId,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("productId", Integer.toString(ProductId));
		map.put("UserId", ToolsUtil.nullToString(SharedUtil
				.getStringPerfernece(context, SharedUtil.user_id)));
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_DETAIL,
				httpCallBack, RequestProductDetailsInfoBean.class, true, false);
	}


	/**
	 * 获取专柜商品信息详情(获取商品详情(败家))
	 *
	 * @param httpCallBack  HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param productId int商品编号
	 * @return void
	 * **/
	public void getCkrProductDetails(int productId, final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("productId", Integer.toString(productId));
		map.put("UserId", ToolsUtil.nullToString(SharedUtil.getStringPerfernece(context, SharedUtil.user_id)));
		BasehttpSend(map, context, HttpConstants.METHOD_CK_PRODUCTMANAGER_DETAIL,
				httpCallBack, RequestCKProductDeatilsInfo.class, true, false);
	}


	/**
	 * 获取专柜商品规格信息详情(获取商品详情(败家))
	 *
	 * @param httpCallBack  HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param productId int商品编号
	 * @param isshow boolean 是否显示等待对话框
	 * @param iscancel boolean 是否可以取消对话框
	 * @return void
	 * **/
	public void getCkrProductSPECDetails(int productId, final HttpCallBackInterface httpCallBack, Context context,final boolean isshow,final boolean iscancel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("productId", Integer.toString(productId));
		map.put("UserId", ToolsUtil.nullToString(SharedUtil.getStringPerfernece(context, SharedUtil.user_id)));
		BasehttpSend(map, context, HttpConstants.METHOD_CK_SPEC_PRODUCTMANAGER_DETAIL,
				httpCallBack, RequestCk_SPECDetails.class, isshow, iscancel);
	}

	

	/**
	 * 获取订单列表
	 * 
	 * @return void
	 * **/
	public void productCopy(String productId,
			final HttpCallBackInterface httpCallBack, Context context
			) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PRODUCTID, productId);
		BasehttpSend(map, context,
				HttpConstants.METHOD_PRODUCT_COPY, httpCallBack,
				BaseRequest.class, true, false);
	}
	
	

	/**
	 * 获取订单列表
	 * 
	 * @return void
	 * **/
	public void getOrderList(int Page, String Pagesize,
			String OrderProductType, String Status,String CustomerId,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(Page));
		map.put(Constants.PAGESIZE, Pagesize);
		map.put(Constants.CustomerId, CustomerId);
		map.put(Constants.OrderProductType, OrderProductType);
		map.put(Constants.STATUS, Status);
		BasehttpSend(map, context,
				HttpConstants.METHOD_ORDER_GETALLORDERFORBUYER, httpCallBack,
				OrderListBackBean.class, showDialog, false);
	}
	
	/**
	 * 确认退款
	 * @return void
	 * **/
	public void comformBack(String orderId,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.ORDER_NO, orderId);
		BasehttpSend(map, context,
				HttpConstants.METHOD_ORDER_RMAConfirm, httpCallBack,
				OrderListBackBean.class, showDialog, false);
	}
	
	/**
	 * 判断是否能开小票
	 * @return void
	 * **/
	public void checkCreateGeneralOrder(
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context,
				HttpConstants.METHOD_ORDER_CHECK, httpCallBack,
				BaseRequest.class, showDialog, false);
	}
	
	
	/**
	 * 开小票
	 * @return void
	 * **/
	public void createGeneralOrder(String price,String saleCode,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PRICE, price);
		map.put(Constants.SALECODE, saleCode);
		BasehttpSend(map, context,
				HttpConstants.METHOD_ORDER_CREATEGENERALORDER, httpCallBack,
				KaiXiaoPiaoBackBean.class, showDialog, false);
	}
	
	
	
	

	/**
	 * 获取收益明细
	 * 
	 * @return void
	 * **/
	public void getIncomeDetail(int page,String pageCount,String incomeStatus,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE,page+"");
		map.put(Constants.PAGESIZE,pageCount);
		map.put(Constants.INCOMESTATUS,incomeStatus);
		BasehttpSend(map, context, HttpConstants.METHOD_ASSISTANT_GETINCOMEINFO,
				httpCallBack, IncomeDetailBackBean.class, true, false);
	}
	
	/**
	 * 获取收益历史
	 * 
	 * @return void
	 * **/
	public void getIncomeHistory(int page,String pageCount,String IncomeTransferStatus,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE,page+"");
		map.put(Constants.PAGESIZE,pageCount);
		map.put(Constants.INCOMETRANSFERSTATUS,IncomeTransferStatus);
		BasehttpSend(map, context, HttpConstants.METHOD_ASSISTANT_GETINCOMEHISTORY,
				httpCallBack, IncomeHistoryBackBean.class, true, false);
	}
	
	/**
	 * 买手提现
	 * 
	 * @return void
	 * **/
	public void getIncomeRedPack(String amount,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.AMOUNT, amount);
		BasehttpSend(map, context, HttpConstants.METHOD_ASSISTANT_INCOMEREQUESTREDPACK,
				httpCallBack, IncomeHistoryBackBean.class, true, false);
	}
	
	/**
	 * 货款提现
	 * 
	 * @return void
	 * **/
	public void withdrawGoods(String orderNos,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.ORDERNOS, orderNos);
		BasehttpSend(map, context, HttpConstants.METHOD_ASSISTANT_WithdrawGoods,
				httpCallBack, IncomeHistoryBackBean.class, true, false);
	}
	
	
	

	/**
	 * 获取订单详情
	 * 
	 * @return void
	 * **/
	public void getOrderDetail(String orderNo,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.ORDER_NO, orderNo);
		BasehttpSend(map, context, HttpConstants.METHOD_ORDER_GETORDERDETAIL,
				httpCallBack, OrderDetailBackBean.class, true, false);
	}

	/**
	 * 获取圈子列表
	 * 
	 * @return void
	 * **/
	public void getCircleList(int page, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.MAX_VALUE+"");
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_GETBUYERGROUPS,
				httpCallBack, CircleListBackBean.class, showDialog, false);
	}

	/**
	 * 获取圈子详情
	 * 
	 * @return void
	 * **/
	public void getCircleDetail(String groupid, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		BasehttpSend(map, context,
				HttpConstants.METHOD_CIRCLE_GETBUYERGROUPDETAIL, httpCallBack,
				CircleDetailBackBean.class, showDialog, false);
	}



	/**
	 * 获取用户基础圈子详情
	 * @param  buyerId String
	 * @param  userId String
	 * @return void
	 * **/
	public void getBaseCircleDetailByUserID(String buyerId,String userId, boolean showDialog,
								final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("buyerId", buyerId);
		map.put("userId", userId);
		BasehttpSend(map, context,
				HttpConstants.METHOD_CIRCLE_GETBUYERGROUPDETAIL, httpCallBack,
				CircleDetailBackBean.class, showDialog, false);
	}


	/**
	 * 邀请粉丝加入圈子
	 * 
	 * @return void
	 * **/
	public void addFansToCircle(String groupid,String ids, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		map.put(Constants.USERIDSTR, ids);
		BasehttpSend(map, context,
				HttpConstants.METHOD_CIRCLE_ADDFANSTOGROUP, httpCallBack,
				BaseRequest.class, showDialog, false);
	}
	
	/**
	 * 修改圈子名称
	 * 
	 * @return void
	 * **/
	public void renameGroup(String groupid, String name,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		map.put(Constants.NAME, name);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_RENAMEGROUP,
				httpCallBack, BaseRequest.class, showDialog, false);
	}

	/**
	 * 获取粉丝列表（去除该圈子还没有添加的人）
	 * 
	 * @return void
	 * **/
	public void GetValidFansListToGroup(String status,String groupid,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		map.put(Constants.STATUS, status);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_GETVALIDFANSLISTTOGROUP,
				httpCallBack, FansBackListForInviteCirlce.class, showDialog, false);
	}
	
	
	/**
	 * 删除圈子成员
	 * 
	 * @return void
	 * **/
	public void removeCircleMember(String groupid, String userId,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		map.put(Constants.USERID, userId);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_REMOVEGROUPMEMBER,
				httpCallBack, BaseRequest.class, showDialog, false);
	}
	/**
	 * 新建圈子
	 * 
	 * @return void
	 * **/
	public void createCircle(String name, String logo, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.NAME, name);
		map.put(Constants.LOGO, logo);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_CREATEGROUP,
				httpCallBack, BaseRequest.class, showDialog, false);
	}

	/**
	 * 修改圈子头像
	 * 
	 * @return void
	 * **/ 
	public void changeCircleLogo(String circleId, String logo, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, circleId);
		map.put(Constants.LOGO, logo);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_CHANGEGROUPLOGO,
				httpCallBack, BaseRequest.class, showDialog, false);
	}
	
	
	/**
	 * 加入圈子
	 * @param groupid int 圈子id
	 * @return void
	 * **/ 
	public void addCircle(String groupid,boolean showDialog,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_ADDGROUP,httpCallBack, BaseRequest.class, showDialog, false);
	}
	
	
	/**
	 * 退出圈子
	 * @param groupid int 圈子id
	 * @return void
	 * **/ 
	public void exitCircle(String groupid,boolean showDialog,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, groupid);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_EXITGROUP,httpCallBack, BaseRequest.class, showDialog, false);
	}
	
	
	
	/**
	 * 上新商品
	 * @param requestUploadProductDataBean
	 * @param httpCallBack
	 * @param context
	 */
	public void createBuyerProductInfo(
			RequestUploadProductDataBean requestUploadProductDataBean,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("json",
				BaseGsonUtils.getObjectToJson(requestUploadProductDataBean));
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_CREATE,
				httpCallBack, BaseRequest.class, false, false);
	}

	
	
	
	/**
	 * 修改商品
	 * @param requestUploadProductDataBean
	 * @param httpCallBack
	 * @param context
	 */
	public void updateBuyerProductInfo(
			RequestUploadProductDataBean requestUploadProductDataBean,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("json",
				BaseGsonUtils.getObjectToJson(requestUploadProductDataBean));
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCTMANAGER_UPDATE,
				httpCallBack, BaseRequest.class, false, false);
	}

	
	/**
	 * 删除圈子
	 * 
	 * @return void
	 * **/
	public void deleteCircle(String circleId, String logo, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.GROUPID, circleId);
		BasehttpSend(map, context, HttpConstants.METHOD_CIRCLE_DELETEGROUP,
				httpCallBack, BaseRequest.class, showDialog, false);
	}
	
	
	/**
	 * 获取任务奖励列表
	 * 
	 * @return void
	 * **/ 
	public void getTaskRewardList( boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context,boolean isShowDialog) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_PROMOTION_LIST,
				httpCallBack, TastRewardListBackBean.class, isShowDialog, false);
	}
	
	
	/**
	 * 获取任务奖励详情
	 * 
	 * @return void
	 * **/ 
	public void getTaskRewardDetail(String promotionId, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PROMOTIONID, promotionId);
		BasehttpSend(map, context, HttpConstants.METHOD_PROMOTION_DETAIL,
				httpCallBack, RewardDetailBackBean.class, showDialog, false);
	}
	
	
	
	/**
	 * @param phone String 提货人手机号
	 * @param ProductId 商品id
	 * @param Count  int 购买数量
	 * @return void
	 * **/
	public void createProductOrder(String phone,int ProductId, int Count, long SizeId,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("ProductId", Integer.toString(ProductId));
		map.put("Count", Integer.toString(Count));
		map.put("SizeId", Long.toString(SizeId));
		BasehttpSend(map, context, HttpConstants.CREATEORDER, httpCallBack,RequestCreatOrderInfoBean.class, showDialog, false);
	}
	
	
	/**
	 * 获取败家获取用户信息（新）
	 * @param showDialog  boolean 当前页
	 * @return void
	 * **/
	public void getBaijiaUserInfo(int userid,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", Integer.toString(userid));
		BasehttpSend(map, context, HttpConstants.GETUSERINFO, httpCallBack,RequestUserInfoBean.class, showDialog, false);
	}
	
	
	
	/**
	 * 获取败家订单列表
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @param State
	 *            int 全部订单 0，待付款 1， 专柜自提 2， 售后 3
	 * @return void
	 * **/
	public void getBaijiaOrderList(int currPage, int pageSize, int State,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		map.put("State", Integer.toString(State));
		BasehttpSend(map, context, HttpConstants.GETORDERLIST, httpCallBack,RequestBaiJiaOrderListInfoBean.class, showDialog, true);
	}
	
	
	
	/**
	 * 获取败家订单详情
	 *
	 * @return void
	 * **/
	public void getBaijiaOrderDetails(String orderNo,boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo",orderNo);
		BasehttpSend(map, context, HttpConstants.GETORDERLISTDETAILS, httpCallBack,RequestBaiJiaOrdeDetailsInfoBean.class, showDialog, false);
	}
	
	
	
	/**
	 * 败家  获取用户是否可以养家等操作
	 *
	 * @return void
	 * **/
	public void getCheckBuyerStatus(boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHODCHECKBUYERSTATUS, httpCallBack,BaiJiaCheckBuyerStatusBeanRequest.class, showDialog, false);
	}
	
	
	/**
	 * 计算订单商品金额
	 * @param ProductId int 商品编号
	 * @param Quantity int 数量 
	 * @return void
	 * **/
	public void getBaijiaOrderPrice(int ProductId,int Quantity,boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ProductId",Integer.toString(ProductId));
		map.put("Quantity",Integer.toString(Quantity));
		BasehttpSend(map, context, HttpConstants.GETCOMPUTEAMOUNT, httpCallBack,RequestComputeAmountInfoBean.class, showDialog, false);
	}

	/**
	 * 获取败家我的圈子
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @return void
	 * **/
	public void getMyCircle(int currPage, int pageSize, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETMYCIRCLE, httpCallBack,
				RequestMyCircleInfoBean.class, showDialog, false);
	}

	
	/**
	 * 获取用户的圈子(如果是买手显示由买手创建的圈子，如果是普通用户则显示用户加入的圈子)
	 * 
	 * @param currPage  int 当前页
	 * @param pageSize int 条数
	 * @param UserId  int 用户ID
	 * @return void
	 * **/
	public void getUserGroups(int UserId,int currPage, int pageSize, boolean showDialog,
			final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("UserId", Integer.toString(UserId));
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETUSERGROUPS, httpCallBack,
				RequestMyCircleInfoBean.class, showDialog, false);
	}

	
	/**
	 * 获取败家推荐的圈子
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @return void
	 * **/
	public void getRecommendGroup(int currPage, int pageSize,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETRECOMMENDGROUP,
				httpCallBack, RequestTuiJianCircleInfoBean.class, showDialog, false);
	}

	/**
	 * 获取败家获取按品牌划分的商品列表（发现→品牌界面）(败家)
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @return void
	 * **/
	public void getBrandProductList(int currPage, int pageSize,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETBRANDPRODUCTLIST,
				httpCallBack, RequestBrandInfoBean.class, showDialog, true);
	}

	/**
	 * 获取同城用户的商品列表（发现→同城界面）(败家
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @param CityId
	 *            int 城市编号 int 条数
	 *  @param userid int 用户id
	 * @return void
	 * **/
	public void getBrandCity_Wide(int currPage, int pageSize, int CityId,int userid,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		map.put("userid", Integer.toString(userid));
		map.put(Constants.LONGITUDE, SharedUtil.getStringPerfernece(context,"longitude"));
		map.put(Constants.LATITUDE, SharedUtil.getStringPerfernece(context,"latitude"));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		map.put("CityId", Integer.toString(CityId));
		BasehttpSend(map, context, HttpConstants.GETCITYPRODUCTLIST,
				httpCallBack, RequestBrandCityWideInfoBean.class, showDialog,
				true);
	}
	
	
	/**
	 * 获取败家品牌信息详细
	 * @param currPage  int 当前页
	 * @param pageSize  int 条数
	 * @param BrandId int 品牌id   (如果是品牌  就传BrandId    如果是标签就传Name)
	 * @param Name 标签名字
	 * @return void
	 * **/
	public void getBaijiaBrandDetails(int currPage, int pageSize, int BrandId ,String Name,
			boolean showDialog, final HttpCallBackInterface httpCallBack,
			Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		if(BrandId>=0)
		{
			map.put("BrandId", Integer.toString(BrandId));
		}
		if(Name!=null)
		{
			map.put("Name", Name);
		}
		BasehttpSend(map, context, HttpConstants.METHOD_BRANDMANAGEER_DETAIL, httpCallBack,
				RequestBrandInfoInfoBean.class, showDialog, false);
	}
	
	
	/**
	 * 
	 * 败家取消订单
	 * @param OrderNo String 订单编号" 必填
     * @return void
	 * **/
	public void cancelOrder(String OrderNo, boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo",OrderNo);
		BasehttpSend(map, context, HttpConstants.METHOD_CANCELORDER,httpCallBack, BaseRequest.class, showDialog,false);
	}
	
	/**
	 * 
	 * 败家申请退款（认证买手商品退款）
	 * @param OrderNo String 订单编号" 必填
     * @param Count int 退货数量
     * @param Reason string 退货原因
     * @return void
	 * **/
	public void applyForRefund(String OrderNo, int Count,String Reason,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo",OrderNo);
		map.put("Reason",Reason);
		map.put("Count", Integer.toString(Count));
		BasehttpSend(map, context, HttpConstants.METHOD_APPLY_RMA,httpCallBack, BaseRequest.class, showDialog,false);
	}
	
	
	/**
	 * 败家  认证买手商品取消退款
	 * 
	 * @param OrderNo  String 订单编号" 必填
     * @return void
	 * **/
	public void cancelRma(String OrderNo,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo",OrderNo);
		BasehttpSend(map, context, HttpConstants.METHOD_CANCELRMA,httpCallBack, BaseRequest.class, showDialog,false);
	}
	
	/**
	 * 败家  确认提货
	 * 
	 * @param OrderNo  String 订单编号" 必填
     * @return void
	 * **/
	public void affirmPickToGood(String OrderNo,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo",OrderNo);
		BasehttpSend(map, context, HttpConstants.METHOD_CONFIRMGOODS,httpCallBack, BaseRequest.class, showDialog,false);
	}
	
	/**
	 * 获取用户信息
	 * 败家  客户获取自己的基本信息（新）
	 * @return void
	 * **/
	public void GetBaijiaMyInfo(boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		BasehttpSend(map, context, HttpConstants.METHOD_MYINFO,httpCallBack, RequestMyInfoBean.class, showDialog,false);
	}
	
	/**
	 * 获取买手的商品列表、上新商品列
	 * @param page int 当前页
	 * @param UserId int 当前用户id
     *  @param pagesize int 页大小
     * @param Filter  int 0:全部商品,1:上新商品
     * @return void
	 * **/
	public void GetBaijiaGetUserProductList(int UserId,int page,int pagesize,int Filter,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		map.put("Filter", Integer.toString(Filter));
		map.put("UserId", Integer.toString(UserId));
		BasehttpSend(map, context, HttpConstants.METHOD_GETUSERPRODUCTLIST,httpCallBack, RequestMyFavoriteProductListInfoBean.class, showDialog,false);
	}
	
	
	/**
	 * 获取消息列表
	 * @param page int 当前页
     *  @param pagesize int 页大小
     * @return void
	 * **/
	public void GetBaijiaMessageList(int page,int pagesize,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		BasehttpSend(map, context, HttpConstants.METHOD_GETMESSAGELIST,httpCallBack, RequestMsgListInfoBean.class, showDialog,false);
	}
	
	/**
	 *获取用户动态
	 * @param page int 当前页
     *  @param pagesize int 页大小
     * @return void
	 * **/
	public void GetBaijiaUserDynamic(int page,int pagesize,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pagesize));
		BasehttpSend(map, context, HttpConstants.METHOD_GETUSERDYNAMIC,httpCallBack, RequestUserDynamicInfoBean.class, showDialog,false);
	}
	
	
	/**
	 * 获取我收藏的商品列表-败家(新)
	 * 
	 * @param currPage
	 *            int 当前页
	 * @param pageSize
	 *            int 条数
	 * @return void
	 * **/
	public void getMyFavoriteProductList(int currPage, int pageSize,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETMYFAVORITEPRODUCTLIST,httpCallBack, RequestMyFavoriteProductListInfoBean.class, showDialog,false);
	}
	
	
	
	
	/**
	 * 获取用户收藏的商品列表
	 * @param userid int 用户id
	 * @param currPage int 当前页
	 * @param pageSize int 条数
	 * @return void
	 * **/
	public void getUserFavoriteProductList(int userid,int currPage, int pageSize,boolean showDialog, final HttpCallBackInterface httpCallBack,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", Integer.toString(userid));
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.GETUSERFAVORITELIST,httpCallBack, RequestMyFavoriteProductListInfoBean.class, showDialog,false);
	}
	
	/**
	 * 根据品牌名称检索品牌
	 * @param currPage int 当前页
	 * @param pageSize  int 条数
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param state int 0: 搜索品牌列表, 1:搜索买手列表
     * @param  key  string类型，关键字
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void searchbrandList(int currPage,int pageSize, int state,String key,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.PAGE, Integer.toString(currPage));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		map.put("state", Integer.toString(state));
		map.put("key", key);
		BasehttpSend(map, context, HttpConstants.METHOD_SEARCH,httpCallBack, RequestBrandSearchInfoBean.class, ishowStatus, false);
	}
	
	/**
	 * 获取房间号,如果没有则创建房间
	 * @param  groupId  int 圈子编号  如果是私聊 则传0，如果是群聊，则传圈子编号
	 * @param  fromUser int 用户编号     如果是私聊则传用户编号，如果是群聊则传0    fromuserId,toUserId不用区分顺序  
     * @param  toUser  int 用户编号       如果是私聊则传用户编号，如果是群聊则传0
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void getRoom_Id(int groupId,int fromUser,int toUser,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("groupId", Integer.toString(groupId));
		map.put("fromUser", Integer.toString(fromUser));
		map.put("toUser", Integer.toString(toUser));
		BasehttpSend(map, context, HttpConstants.METHOD_GETROOMID,httpCallBack, RequestRoomInfoBean.class, ishowStatus, false);
	}
	
	
	
	/**
	 *  分享商品
     * @param  productid int  商品编号
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void createProductShare(int productid,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("productid", Integer.toString(productid));
		map.put("parentshareid", "");
		BasehttpSend(map, context, HttpConstants.METHOD_PRODUCT_CREATESHARE,httpCallBack, BaseRequest.class, ishowStatus, false);
	}
	
	/**
	 *  现金分享 分享订单分享订单可获得红包
     * @param  OrderNo String  商品编号
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void createOrderShare(String OrderNo,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OrderNo", OrderNo);
		map.put("parentshareid", "");
		BasehttpSend(map, context, HttpConstants.METHOD_ORDER_CREATESHARE,httpCallBack, BaseRequest.class, ishowStatus, false);
	}
	
	
	
	
	
	/**
	 * 获取某个房间的历史聊天信息
	 * @param  roomId   int 圈子编号 
	 * @param  lastMessageId string  其实消息id   可不传(传递 -1 则后太不传递该字段)
     * @param  page  int 当前页
     * @param  pageSize  没有显示的个数
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void getRoomMessage(String roomId,int lastMessageId,int page,int pageSize,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("roomId", roomId);
		if(lastMessageId>0)
		{
			map.put("lastMessageId", Integer.toString(lastMessageId));
		}
		map.put(Constants.PAGE, Integer.toString(page));
		map.put(Constants.PAGESIZE, Integer.toString(pageSize));
		BasehttpSend(map, context, HttpConstants.METHOD_GETROOMMESSAE,httpCallBack, RequestImMessageInfoBean.class, ishowStatus, false);
	}
	
	/**
	 * 聊天发送图片
	 * @param  "imageurl":  图片网址
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
     * @param ishowStatus boolean 是否显示等待对话框 true 是  false否
	 * @return void
	 * **/
	public void getUploadChatImage(String imageurl,boolean ishowStatus,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("imageurl", imageurl);
		BasehttpSend(map, context, HttpConstants.METHOD_UPOADLCHATIMAGE,httpCallBack, RequestUploadChatImageInfoBean.class, ishowStatus, false);
	}
	
	
	/**
	 * 设置关注或取消关注
	 * 
	 * @param httpCallBack HttpCallBackInterface 回调接口
	 * @param context  Context
	 * @param Status   1表示关注   0表示取消关注
	 * @return void
	 * **/
	public void setFavoite(int favoriteId,int Status,final HttpCallBackInterface httpCallBack, Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FavoriteId", Integer.toString(favoriteId));
		map.put("Status", Integer.toString(Status));
		BasehttpSend(map, context, HttpConstants.SETFAVOITE,httpCallBack, BaseRequest.class, true, false);
	}
	
	
	

	/********
	 * 基础Http传递json数据通用类
	 *
	 * @param responsejsonstr
	 *            String JSON类型数据
	 * @param map
	 *            Map<String, String> map 传递的参数
	 * @param method
	 *            String 访问的方法
	 * @param httpCallBack
	 *            HttpCallBackInterface Ui回调
	 * @param classzz
	 *            Class<T> 泛型即相应成功后 返回的对象类型<T extends BaseRequest> 且
	 *            只能是BaseRequest 或 BaseRequest的 子类
	 * @param isshwoDialog
	 *            boolean 是否显示等待对话框 true 显示 false不显示 默认不显示
	 * @param isDialogCancell
	 *            boolean 是否可取消对话框 true 不可取消 false 可取消 默认 可取消
	 * @return void
	 * ****/
	<T extends BaseRequest> void basehttpSendToJson(String responsejsonstr,
			Map<String, String> map, final Context context, String method,
			final HttpCallBackInterface httpCallBack, final Class<T> classzz,
			boolean isshwoDialog, boolean isDialogCancell) {
		final CustomProgressDialog progressDialog = CustomProgressDialog
				.createDialog(context);
		if (isDialogCancell) {
			progressDialog.setCancelable(false);
		}
		if (isshwoDialog) {
			progressDialog.show();
		}
		httpSendToJson(responsejsonstr, map, context, method,
				new HttpRequestDataInterface() {

					@Override
					public void httpRequestDataInterface_Sucss(Object obj) {

						if (obj != null && obj instanceof String) {
							String str = (String) obj;
							BaseRequest bean = BaseGsonUtils.getJsonToObject(classzz, str);
							if (bean == null) {
								httpRequestDataInterface_Fails("数据不存在");
							}else if(bean.getStatusCode()==200)
							{
								httpCallBack.http_Success(bean);
							}else if(bean.getStatusCode()==401)//如果token 失效
							{
								setUnLoginInfo(MyApplication.getInstance().getApplicationContext());
								MyApplication.getInstance().startLogin(context,"登录已经失效,请重新登录");
							}
							else {
								httpRequestDataInterface_Fails(bean.getMessage());
							}
						} else {
							httpRequestDataInterface_Fails("数据不存在");
						}
						progressDialog.cancel();
					}

					@Override
					public void httpRequestDataInterface_Fails(String msg) {

						httpCallBack.http_Fails(400, msg);
						progressDialog.cancel();
					}
				});
	}

	/****
	 * 通用传输数据 用于上传JSON类型数据 返回url拼接后的字符串 如 a=1&b=2&sign=md5
	 * 
	 * @param map
	 *            Map<String, String> 传递的参数
	 * @param context
	 *            Context
	 * @param json
	 *            String传递的json数据 没有值传递NULL
	 * @return String
	 * **/
	String setBaseRequestJsonParams(Map<String, String> map, Context context,
			final String json) {
		StringBuilder stringBuilder = new StringBuilder();
		if (stringBuilder != null) {
			String versionName = "0";
			try {
				// 获取版本号
				PackageInfo pi = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				versionName = pi.versionName;
				versionName = "2.3";
			} catch (Exception e) {

			}
			if (map == null) {
				map = new HashMap<String, String>();
			}
			if (map != null) {
				map.put(Constants.HTTPCHANNEL, Constants.ANDROID);
				map.put(Constants.CLIENTVERSION, versionName);
				map.put(Constants.UUID, UUID.randomUUID().toString());
				map.put(Constants.TOKEN, SharedUtil.getStringPerfernece(context, SharedUtil.user_token));
				String md5str = Md5Utils.md5ToString(map, json);
				try {
					map.put(Constants.TOKEN, URLEncoder.encode(SharedUtil.getStringPerfernece(context, SharedUtil.user_token), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put(Constants.SIGN, md5str);
				Set<String> set = map.keySet();
				if (set != null && set.size() > 0) {
					Iterator<String> iterator = set.iterator();
					while (iterator.hasNext()) {
						String k = iterator.next();
						if (map.containsKey(k)) {
							String v = map.get(k);
							stringBuilder.append(k);
							stringBuilder.append("=");
							stringBuilder.append(v);
							if(iterator.hasNext())
							{
							  stringBuilder.append("&");
							}
						}

					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	

	/****
	 * 通用传输数据 生成http传输对象 RequestParams
	 * 
	 * @param map
	 *            Map<String, String> 需要传递的参数 K=v K-传递的参数 V-对应的值
	 * @param context
	 *            Context
	 * @return RequestParams
	 * **/
	public RequestParams setBaseRequestParams(Map<String, String> map, Context context) {
		RequestParams params = new RequestParams();
		if (params != null) {
			String versionName = "0";
			try {
				// 获取版本号
				PackageInfo pi = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				versionName = pi.versionName;
				versionName = "2.3";
			} catch (Exception e) {

			}
			if (map == null) {
				map = new HashMap<String, String>();
			}
			if (map != null) {
				map.put(Constants.HTTPCHANNEL, Constants.ANDROID);
				map.put(Constants.CLIENTVERSION, versionName);
				map.put(Constants.UUID, UUID.randomUUID().toString());
				map.put(Constants.TOKEN, SharedUtil.getStringPerfernece(
						context, SharedUtil.user_token));
				String md5str = Md5Utils.md5ToString(map, null);
				map.put(Constants.SIGN, md5str);
				Set<String> set = map.keySet();
				if (set != null && set.size() > 0) {
					Iterator<String> iterator = set.iterator();
					while (iterator.hasNext()) {
						String k = iterator.next();
						if (map.containsKey(k)) {
							String v = map.get(k);
							params.addBodyParameter(k, v);
						}

					}
				}
			}
		}
		return params;
	}

	/*****
	 * 访问网络基础Http 用于post上传K-V
	 * 
	 * @param map
	 *            Map<String, String> 上传的参数 k-V
	 * @param method
	 *            String 方法名

	 * @param classzz
	 *            Class<T> 泛型即相应成功后 返回的对象类型<T extends BaseRequest> 且
	 *            只能是BaseRequest 或 BaseRequest的 子类
	 * @param isshwoDialog
	 *            boolean 是否显示等待对话框 true 显示 false不显示 默认不显示
	 * @param isDialogCancell
	 *            boolean 是否可取消对话框 true 不可取消 false 可取消 默认 可取消
	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * ***/
	<T extends BaseRequest> void BasehttpSend(final Map<String, String> map,
			final Context context, final String method,
			final HttpCallBackInterface httpCallBack, final Class<T> classzz,
			boolean isshwoDialog, boolean isDialogCancell) {
		 final CustomProgressDialog progressDialog = CustomProgressDialog
		 .createDialog(context);
		 progressDialog.setCancelable(true);
		 progressDialog.setCanceledOnTouchOutside(false);
		 try {
			 if(isshwoDialog)
			 {
				 progressDialog.show();
			 }
		} catch (Exception e) {
		}
		httpUtils.send(HttpMethod.POST, method.trim(),
				setBaseRequestParams(map, context),
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						if (httpCallBack != null) {
							Log.i("result", responseInfo.result);
							BaseRequest bean = BaseGsonUtils.getJsonToObject(classzz, responseInfo.result);
							if (bean == null) {
								httpCallBack.http_Fails(0, "数据解析异常");

							}else if(bean.getStatusCode()==200)
							{
								httpCallBack.http_Success(bean);
							}else if(bean.getStatusCode()==401)//如果token 失效
							{
								MyApplication.getInstance().startLogin(context,"登录已经失效,请重新登录");
							}else {
								httpCallBack.http_Fails(bean.getStatusCode(),bean.getMessage());
							} 
						}
						try {
							if(progressDialog!=null){
								progressDialog.cancel();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}

					
					@Override
					public void onFailure(HttpException error, String msg) {
						if (httpCallBack != null) {
							if(error.getExceptionCode() == 0){
								httpCallBack.http_Fails(0, "网络异常，请检查网络");
							}else 
							{
								httpCallBack.http_Fails(0, msg);
							}
							
						}
						if(progressDialog!=null){
							progressDialog.cancel();
						}
					}
				});
	}

	/*<T extends BaseRequest> void BasehttpSend2(final Map<String, String> map,
			final Context context, final String method,
			final HttpCallBackInterface httpCallBack, final Class<T> classzz,
			boolean isshwoDialog, boolean isDialogCancell) {
		final CustomProgressDialog progressDialog = CustomProgressDialog
				.createDialog(context);
		progressDialog.setCancelable(isDialogCancell);
		progressDialog.show();
		httpUtils.send(HttpMethod.POST, method.trim(),
				setBaseRequestParams(map, context),
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						if (httpCallBack != null) {
							Log.i("result", responseInfo.result);
							BaseRequest bean = BaseGsonUtils.getJsonToObject(
									classzz, responseInfo.result);
							if (bean == null) {
								httpCallBack.http_Fails(0, "数据解析异常");

							} else if (bean.getStatusCode() != 200) {
								httpCallBack.http_Fails(bean.getStatusCode(),
										bean.getMessage());
							} else {
								httpCallBack.http_Success(bean);
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

						if (httpCallBack != null) {
							httpCallBack.http_Fails(0, msg);
						}
						progressDialog.cancel();
					}
				});
	}*/

	/*****
	 * 访问网络 上传JSON类型数据
	 * @param json
	 *            String json类型的字符串
	 * @param map
	 *            Map<String, String> 传递的参数
	 * @param context
	 *            Context
	 * @param method
	 *            RequestCallBack<T> 回调
	 * @param httpRequestDataInterface
	 *            HttpRequestDataInterface http接口
	 * @return void
	 * ***/
	<T> void httpSendToJson(final String json, final Map<String, String> map,
			final Context context, String method,
			final HttpRequestDataInterface httpRequestDataInterface) {
		
			method = method + "?" +setBaseRequestJsonParams(map, context, json);
		
		sendHttpJson(method, json, httpRequestDataInterface);
	}
		

	/*******
	 * 本类定义 异步 上传JSON 数据
	 * 
	 * @param url
	 *            String 访问的url
	 * @param json
	 *            String 需要传递的json 格式的字符串
	 * @param httpRequestDataInterface
	 *            HttpRequestDataInterface 回调接口
	 * @return void
	 * ***/
	void sendHttpJson(final String url, final String json,
			final HttpRequestDataInterface httpRequestDataInterface) {
		final CustonHandler handler = new CustonHandler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
				if (httpRequestDataInterface == null) {
					return;
				}
				switch (msg.what) {
				case 200:
					httpRequestDataInterface.httpRequestDataInterface_Sucss(msg.obj);
					break;
				case 400:
					httpRequestDataInterface.httpRequestDataInterface_Fails((String) msg.obj);
					break;
				}
			}
		};

		// 启动线程进行数据请求
		new Thread() {
			public void run() {
				try {
					URL htturl = new URL(url);
					HttpURLConnection http = (HttpURLConnection) htturl
							.openConnection();
					http.setDoInput(true);
					http.setDoOutput(true);
					http.setReadTimeout(10000);
					http.setConnectTimeout(6*5000);
					http.setRequestMethod("POST");
					http.setRequestProperty("Content-type",
							"application/json;charset=UTF-8");
					OutputStream out = http.getOutputStream();
					if (out != null) {
						out.write(json.getBytes());
					} else {
						handler.sendError("连接失败");
					}
					InputStream io = http.getInputStream();
					StringBuilder sb = new StringBuilder();
					if (io != null) {
						byte[] buffer = new byte[1024];
						int count = 0;
						while ((count = io.read(buffer, 0, 1024)) != -1) {
							sb.append(new String(buffer, 0, count, "UTF-8"));
						}
						// String returnValue=URLDecoder.decode(sb.toString(),
						// sb.toString().trim());
						handler.sendSucess(sb.toString());
					} else {
						handler.sendError("连接失败");
					}
				} catch (Exception e) {
					if(e!=null ){
						if (e instanceof SocketTimeoutException) {
							Log.i("error", "shuyu---"+e.getMessage()+"tostring---"+e.toString());
							handler.sendError("网络超时，请稍后重试");
						}else{
							Log.i("error", "bushuyu---"+e.getMessage()+"tostring---"+e.toString());
							e.printStackTrace();
							handler.sendError(e.getMessage());
						}
					}
				}
			};
		}.start();
	}

	/******
	 * Handler
	 * ****/
	class CustonHandler extends Handler {
		public void sendError(String errormsg) {
			Message msg = obtainMessage(400);
			msg.obj = errormsg;
			sendMessage(msg);
		}

		public void sendSucess(String sucessMsg) {
			Message msg = obtainMessage(200);
			msg.obj = sucessMsg;
			sendMessage(msg);
		}
	}

	/****
	 * 设置登录信息 存储到属性文件
	 * 
	 * @param bean
	 *            UserRequestBean 用户登录信息类
	 * @param context
	 *            Context
	 * @return void
	 * ***/
	public void setLoginInfo(Context context, UserRequestBean bean) {
		if (bean != null) {
			UserInfo userInfo = bean.getData();
			SharedUtil.setStringPerfernece(context, SharedUtil.user_id,
					Integer.toString(userInfo.getId()));
			SharedUtil.setStringPerfernece(context, SharedUtil.user_level,
					userInfo.getLevel());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_name,
					userInfo.getName());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_names,
					userInfo.getNickname());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_logo,
					userInfo.getLogo());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_logo_full,
					userInfo.getLogo_full());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_logobg,
					userInfo.getLogobg());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_logobg_s,
					userInfo.getLogobg_s());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_mobile,
					userInfo.getMobile());
			Log.i("token",userInfo.getToken());
			SharedUtil.setStringPerfernece(context, SharedUtil.user_token,
					userInfo.getToken());
			SharedUtil.setUpdateUrl(context, userInfo.getUpdateUrl());
			SharedUtil.setBooleanPerfernece(context,
					SharedUtil.user_loginstatus, true);
			SharedUtil.setStringPerfernece(context,
					SharedUtil.user_AuditStatus, userInfo.getAuditStatus());
			SharedUtil.setStringPerfernece(context,
					SharedUtil.user_Description, userInfo.getDescription());
			SharedUtil.setBooleanPerfernece(context,
					SharedUtil.user_IsBindMobile, userInfo.isIsBindMobile());
			SharedUtil.setBooleanPerfernece(context,
					SharedUtil.user_IsBindWeiXin, userInfo.isIsBindWeiXin());
			SharedUtil.setBooleanPerfernece(context,
					SharedUtil.user_canPush, userInfo.isIsOpenPush());
			JpushUtils jpushUtils = new JpushUtils(context);
			jpushUtils.setAlias(SharedUtil.getStringPerfernece(context, SharedUtil.user_id));//设置别名
		}
	}

	/****
	 * 设置登录信息 存储到属性文件
	 * 
	 * @param context
	 *            Context
	 * @return void
	 * ***/
	public void setUnLoginInfo(Context context) {
		SharedUtil.setStringPerfernece(context, SharedUtil.user_id, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_name, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_names, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_logo, null);
		SharedUtil
				.setStringPerfernece(context, SharedUtil.user_logo_full, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_logobg, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_logobg_s, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_mobile, null);
		SharedUtil.setStringPerfernece(context, SharedUtil.user_token, null);
		SharedUtil.setBooleanPerfernece(context, SharedUtil.user_loginstatus,
				false);
	}

	/****
	 * 定义Http接口回调类 用于接收返回的 成功与失败
	 * 
	 * @see #http_Success(Object)
	 * @see #http_Fails(int, String)
	 * ***/
	public interface HttpCallBackInterface {

		/**
		 * 返回成功
		 * 
		 * @param obj
		 *            Object
		 * @return void
		 */
		void http_Success(Object obj);

		/**
		 * 返回失败
		 * 
		 * @param error
		 *            int
		 * @param msg
		 *            String
		 * @return void
		 */
		void http_Fails(int error, String msg);

	};

	/****
	 * 定义Http接口回调类 用于接收返回的 成功与失败 用于 异步上传Json类型数据时 的回调接口
	 * 
	 * @see #httpRequestDataInterface_Sucss(Object)
	 * @see #httpRequestDataInterface_Fails(String)
	 * ***/
	private interface HttpRequestDataInterface {
		/**
		 * json上传成功
		 * 
		 * @param obj
		 *            Object
		 * @return void
		 * */
		void httpRequestDataInterface_Sucss(Object obj);

		/**
		 * json上传失败
		 * 
		 * @param msg
		 *            String
		 * @return void
		 * */
		void httpRequestDataInterface_Fails(String msg);
	}

	private OSSService ossService;
	private OSSBucket bucket;

	// 同步上传数据
	public void syncUpload(String imageLocalPath, SaveCallback callBack) {
		if(!TextUtils.isEmpty(imageLocalPath)){
			ossService = OSSServiceProvider.getService();
			bucket = ossService.getOssBucket("2".equals(Constants.PublishStatus)?Constants.aliyunDirForWork:Constants.aliyunDirForDev);//开发环境和生产环境的判断
			OSSFile bigfFile = ossService.getOssFile(bucket, imageLocalPath
					.substring(imageLocalPath.lastIndexOf("/") + 1,
							imageLocalPath.length()));
			try {
				bigfFile.setUploadFilePath(imageLocalPath, "image/*");
				bigfFile.ResumableUploadInBackground(callBack);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}

	
	
	
	
	

	/**
	 * 微信登录
	 *
	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void wxLongin(String jsonStr,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.JSON, jsonStr);
		map.put("appid",Constants.WX_APP_ID);
		BasehttpSend(map, context, HttpConstants.METHOD_wxLogin,
				httpCallBack, UserRequestBean.class, showDialog,
				false);
	}
	
	
	/**
	 * 绑定微信
	 * 

	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void bindWeChat(String jsonStr,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.JSON, jsonStr);
		map.put("appid",Constants.WX_APP_ID);
		BasehttpSend(map, context, HttpConstants.METHOD_BindWX,
				httpCallBack, UserRequestBean.class, showDialog,
				false);
	}
	
	
	/**
	 * 
	 * 

	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void getUserFlowStatus(
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		BasehttpSend(null, context, HttpConstants.METHOD_GETUSERFLOWSTATUS,
				httpCallBack, GetUserFlowStatusBackBean.class, showDialog,
				false);
	}
	
	
	

	/**
	 * 绑定手机号
	 * 

	 * @param httpCallBack
	 *            HttpCallBackInterface回调接口
	 * @param context
	 *            Context
	 * @return void
	 * **/
	public void bindMobile(String mobile,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.MOBILE, mobile);
		BasehttpSend(map, context, HttpConstants.METHOD_BINDMOBILE,
				httpCallBack, BaseRequest.class, showDialog,
				false);
	}
	
	/**
	 * 修改推送状态
	 * @param state
	 * @param httpCallBack
	 * @param context
	 * @param showDialog
	 */
	public void changePushState(String state,
			final HttpCallBackInterface httpCallBack, Context context,
			boolean showDialog) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.STATE, state);
		BasehttpSend(map, context, HttpConstants.changePushState,
				httpCallBack, BaseRequest.class, showDialog,
				false);
	}
	
	
	
}
