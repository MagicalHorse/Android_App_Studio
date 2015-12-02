package com.shenma.yueba.constants;

public class HttpConstants {
	
	private static String baseUrlForWriteForWork = getServerUrlForWrite();//正式生产环境的写操作接口（写）
	private static String baseUrlForReadForWork = getServerUrlForRead();//正式生产环境的读取操作（读）
	private static String USERURLFORWRITE = baseUrlForWriteForWork + "User/";// USER接口(写)
	private static String USERURLFORREAD = baseUrlForReadForWork + "User/";// USER接口(读)
	private static String COMMONURLFORWRITE = baseUrlForWriteForWork + "Common/";//通用接口（写）
	private static String COMMONURLFORREAD = baseUrlForReadForWork + "Common/";//通用接口（读）
	private static String ADDRESSFORWRITE = baseUrlForWriteForWork + "Address/";// 地址管理（写）
	private static String ADDRESSFORREAD = baseUrlForReadForWork + "Address/";// 地址管理（读）
	private static String BRANDFORWRITE = baseUrlForWriteForWork + "Brand/";// 品牌管理（写）
	private static String BRANDFORREAD  = baseUrlForReadForWork + "Brand/";// 品牌管理（读）
	private static String PRODUCTFORWRITE = baseUrlForWriteForWork + "Product/";// 商品管理（写）
	private static String PRODUCTFORREAD  = baseUrlForReadForWork + "Product/";// 商品管理（读）
	private static String ASSISTANTFORWRITE = baseUrlForWriteForWork + "Assistant/";// 买手相关的接口（写）
	private static String ASSISTANTFORREAD = baseUrlForReadForWork + "Assistant/";// 买手相关的接口（读）
	private static String BUYERFORWRITE = baseUrlForWriteForWork + "Buyer/";// 买手相关的接口（写）
	private static String BUYERFORREAD  = baseUrlForReadForWork + "Buyer/";// 买手相关的接口（读）
	private static String OrderFORWRITE = baseUrlForWriteForWork + "Order/";// 订单相关的接口（写）
	private static String OrderFORREAD = baseUrlForReadForWork + "Order/";// 订单相关的接口（读）
	private static String CircleFORWRITE = baseUrlForWriteForWork + "Community/";// 圈子相关的接口（写）
	private static String CircleFORREAD = baseUrlForReadForWork + "Community/";// 圈子相关的接口（读）
	private static String SEARCHFORWRITE = baseUrlForWriteForWork + "Search/";// 搜索（写）
	private static String SEARCHFORREAD = baseUrlForReadForWork + "Search/";// 搜索（读）
	private static String PromotionFORWRITE = baseUrlForWriteForWork + "Promotion/";//活动相关
	private static String PromotionFORREAD = baseUrlForReadForWork + "Promotion/";//活动相关
	private static String V3FORWRITE = baseUrlForWriteForWork + "v3/";//三期接口写
	private static String V3FORREAD = baseUrlForReadForWork + "v3/";//三期接口读
	private static String BuyerV3FORWRITE = baseUrlForWriteForWork + "BuyerV3/";//三期接口写
	private static String BuyerV3FORREAD = baseUrlForReadForWork + "BuyerV3/";//三期接口读

	private static String weixinBaseUrl = "https://api.mch.weixin.qq.com/";//微信接口
	private static String weixinPAYUrl = weixinBaseUrl+"pay/";//微信支付接口
	private static String weixinPAYCallBackUrl = baseUrlForWriteForWork+"Payment/";//微信支平台回调


	/**
	 * 创建订单
	 */
	public static String METHOD_CreateOrderV3 = V3FORWRITE + "CreateOrderV3";


	/**
	 * 首页获取banner
	 */
	public static String METHOD_BannerSubject = V3FORREAD + "BannerSubject";

	/**
	 * 首页除去banner外的其他数据
	 */
	public static String METHOD_index = V3FORREAD + "index";

	/**
	 * 商场首页商品列表
	 */
	public static String METHOD_storeindex = V3FORREAD + "storeindex";

	/**
	 * 门店下的更多品牌
	 *
	 */
	public static String METHOD_storebrand = V3FORREAD + "storebrand";

	/**
	 * 门店品牌商品列表
	 *
	 */
	public static String METHOD_brandproduct = V3FORREAD + "brandproduct";


	/**
	 * 搜商场
	 *
	 */
	public static String METHOD_searchbrand = V3FORREAD + "searchbrand";

	/**
	 * 搜买手
	 *
	 */
	public static String METHOD_searchbuyer = V3FORREAD + "searchbuyer";

	/**
	 * 搜品牌
	 *
	 */
	public static String METHOD_searchstore = V3FORREAD + "searchstore";

	/**
	 * 搜商品
	 *
	 */
	public static String METHOD_searchproduct = V3FORREAD + "searchproduct";



	/**
	 * 找导购--获取关注的买手
	 *
	 */
	public static String METHOD_FavBuyers = BuyerV3FORREAD + "FavBuyers";



	/**
	 *
	 *找导购--获取买手的产品数据
	 */
	public static String METHOD_BuyersProducts = BuyerV3FORREAD + "BuyersProducts";


	/**
	 *
	 * 找导购--同商场的其他买手
	 *
	 */
	public static String METHOD_OtherStoreBuyers = BuyerV3FORREAD + "OtherStoreBuyers";

	/**
	 *
	 * 找导购--推荐的买手/导购
	 *
	 */
	public static String METHOD_RecommondBuyerlist = BuyerV3FORREAD + "RecommondBuyerlist";


	/**
	 *
	 * 提醒上新/戳一下
	 *
	 */
	public static String METHOD_Touch = BuyerV3FORREAD + "Touch";

	/**
	 * 判断用户是否关注了公共号
	 */
	public static String METHOD_GETUSERFLOWSTATUS = USERURLFORREAD + "GetUserFlowStatus";
	/**
	 * 绑定微信
	 */
	public static String METHOD_BindWX = USERURLFORWRITE + "BindOutSideUser";
	
	/**
	 * 微信登录
	 */
	public static String METHOD_BINDMOBILE = USERURLFORWRITE + "BindMobile";
	/**
	 * 微信登录
	 */
	public static String METHOD_wxLogin = USERURLFORWRITE + "OutSiteLogin";
	
	/*****
	 * 微信创建统一订单
	 * ***/
	public static String METHOD_WXCREATEORDER = weixinPAYUrl + "unifiedorder";
	
	
	/**
	 * 修改昵称
	 */
	public static String changeNickName = USERURLFORWRITE + "ChangeNickname";

	/**
	 * 修改用户头像
	 */
	public static String changeUserLogo = USERURLFORWRITE + "ChangeUserLogo";
	
	/**
	 * 注册----获取手机短信验证码
	 */
	public static String sendPhoneCode = USERURLFORWRITE + "SendMobileCode";
	/**
	 * 校验手机验证
	 */
	public static String METHOD_VERIFYCODE = USERURLFORREAD + "VerifyCode";
	/**
	 * 版本更新接口
	 */
	public static String METHOD_VERSION_UPDATE = COMMONURLFORREAD + "CheckVersion";
	/**
	/**
	 * 获取城市列表
	 */
	public static String METHOD_GETCITYLIST = COMMONURLFORREAD + "GetCityList";

	/**
	 * 根据经纬度获取城市
	 */

	public static String METHOD_GETCITYBYID = COMMONURLFORREAD+"GetCityByCoord";


	/**
	 * 获取所有开通shopping的城市列表
	 */
	public static String GETALLSHOPPINGCITY = COMMONURLFORREAD+"GetAllShoopingCity";
	/**
	/**
	 * 获取阿里云key
	 */
	public static String METHOD_GETALIYUNKEY = COMMONURLFORREAD + "GetALiYunAccessKey";
	/**
	 * 所有城市列表包含省
	 */
	public static String METHOD_ALLGETCITYLIST = COMMONURLFORREAD + "GetAllCity";
	/**
	 * 不论省市 只需要传编号，就可以获取到下一级的数据。获取省列表parentId=0
	 */
	public static String METHOD_COMMON_GETCITYLISYBYPARENTID = COMMONURLFORREAD + "GetCityListByParentId";
	/**
	 * 注册用户信息
	 */
	public static String METHOD_REGISTER = USERURLFORWRITE + "Register";
	/**
	 * 登录
	 */
	public static String METHOD_LOGIN = USERURLFORREAD + "Login";
	/**
	 * 修改登录密码
	 */
	public static String METHOD_UPDATEPWD = USERURLFORWRITE + "ChangePassword";
	/**
	 * 重置手机密码
	 */
	public static String METHOD_RESETPASSWORD = USERURLFORWRITE + "ResetPassword";
	/**
	 * 我关注的人/我的粉丝
	 */
	public static String METHOD_GETUSERFAVOITE = USERURLFORREAD + "GetUserFavoite";
	
	/**
	 * 设置或取消我的关注
	 */
	public static String SETFAVOITE = USERURLFORWRITE + "Favoite";
	
	/**
	 * 创建联系人地址
	 */
	public static String METHOD_ADDRESSCREATE = ADDRESSFORWRITE+ "Create";
	/**
	 * 获取我的联系人地址列表
	 */
	public static String METHOD_MYADDRESSCREATE_LIST = ADDRESSFORREAD + "My";

	/**
	 * 获取联系人地址详细
	 */
	public static String METHOD_ADDRESSCREATE_DETAILS = ADDRESSFORREAD + "Details";

	/**
	 * 删除联系人地址
	 */
	public static String METHOD_ADDRESSCREATE_DELETE = ADDRESSFORWRITE + "Delete";

	/**
	 * 修改联系人地址信息
	 */
	public static String METHOD_ADDRESSCREATE_UPDATE = ADDRESSFORWRITE + "Edit";

	/**
	 * 设置默认联系人地址信息
	 */
	public static String METHOD_ADDRESSCREATE_DEFAULT = ADDRESSFORWRITE+ "SetDefault";

	/**
	 * 获取默认联系人地址
	 */
	public static String METHOD_ADDRESSCREATE_GETDEFAULT = ADDRESSFORREAD + "Default";

	/**
	 * ---------------------------------品牌管理接口----------------------------------
	 * ---------
	 */

	/**
	 * 获取品牌信息详细
	 */
	public static String METHOD_BRANDMANAGEER_DETAIL = PRODUCTFORREAD + "GetProductListByBrandId";

	/**
	 * 根据关键字获取商品
	 */
	public static String METHOD_GET_PRODUCT_BY_KEY = PRODUCTFORREAD + "Search";
	
	/**
	 * 复制商品
	 */
	public static String METHOD_PRODUCT_COPY = PRODUCTFORWRITE + "Copy";

	/**
	 * 根据名称搜索标签
	 */
	public static String METHOD_GETPRODUCTTAG = PRODUCTFORREAD + "GetProductTag";

	/**
	 * 获取品牌列表
	 */
	public static String METHOD_BRANDMANAGEER_ALL = BRANDFORREAD + "All";

	/**
	 * 按照时间获取传入时间后的品牌列表
	 */
	public static String METHOD_BRANDMANAGEER_REFRESH = BRANDFORREAD + "Refresh";

	/**
	 * 按照品牌首字母获取品牌列表
	 */
	public static String METHOD_BRANDMANAGEER_GROUPALL = BRANDFORREAD + "GroupAll";


	/**
	 * ---------------------------------商品相关接口----------------------------------
	 * ---------
	 */

	/**
	 * 收藏或取消商品
	 */
	public static String METHOD_PRODUCTMANAGER_FAVOR = PRODUCTFORWRITE + "Favorite";
	/**
	 * 喜欢或取消喜欢商品
	 */
	public static String METHOD_PRODUCTMANAGER_LIKE = PRODUCTFORWRITE + "Like";
	/**
	 * 获取主页商品列表(买手街)
	 */
	public static String METHOD_PRODUCTMANAGER_HOMELIST = PRODUCTFORREAD + "Index";
	
	/**
	 * 我的买手
	 */
	public static String METHOD_PRODUCTMANAGER_MYBUYER = PRODUCTFORREAD + "MyBuyer";
	
	/**
	 * 获取商品被喜欢的用户列表
	 */
	public static String METHOD_PRODUCTMANAGER_LIKEDUSERS = PRODUCTFORREAD
			+ "LikedUsers";
	/**
	 * 获取我收藏的买手的商品列表
	 */
	public static String METHOD_PRODUCTMANAGER_MYBUYERPRODUCTLIST = PRODUCTFORREAD
			+ "MyBuyerProductList";
	/**
	 * 获取商品详情(败家)
	 */
	public static String METHOD_PRODUCTMANAGER_DETAIL = PRODUCTFORREAD + "GetProductDetail";

	/**
	 * 获取商品详情(败家)
	 */
	public static String METHOD_CK_PRODUCTMANAGER_DETAIL = PRODUCTFORREAD + "GetProductDetailV3";

	/**
	 * 获取专柜商品规格详情(败家)
	 */
	public static String METHOD_CK_SPEC_PRODUCTMANAGER_DETAIL = PRODUCTFORREAD + "GetProductSku";


	/**
	 * 上传商品信息(买手)
	 */
	public static String METHOD_PRODUCTMANAGER_CREATE = PRODUCTFORWRITE + "Create";
	
	/**
	 * 修改商品信息(买手)
	 */
	public static String METHOD_PRODUCTMANAGER_UPDATE = PRODUCTFORWRITE + "Update";
	
	
	
	/**
	 * 商品管理列表
	 */
	public static String METHOD_PRODUCTLIST = PRODUCTFORREAD+"GetBuyerProductList";
	
	
	/**
	 * 商品上线、下线（买手）
	 */
	public static String METHOD_PRODUCTMANAGER_ONLINE = PRODUCTFORWRITE + "OnLine";
	

	/**
	 * 删除商品
	 */
	public static String METHOD_PRODUCTMANAGER_DELETE = PRODUCTFORWRITE + "Delete";
	
	
	/**
	 * ---------------------------------买手相关接口----------------------------------
	 * ---------
	 */
	
	
	
	/**
	 * 提现货款
	 */
	public static String METHOD_ASSISTANT_WithdrawGoods= BUYERFORWRITE + "WithdrawGoods";
	/**
	 * 获取提现历史
	 */
	public static String METHOD_ASSISTANT_INCOMEREQUESTREDPACK= ASSISTANTFORWRITE + "Income_Request_RedPack";
	/**
	 * 获取提现历史
	 */
	public static String METHOD_ASSISTANT_GETINCOMEHISTORY = ASSISTANTFORREAD + "GetIncomeHistory";
	/**
	 * 收益明细
	 */
	public static String METHOD_ASSISTANT_GETINCOMEINFO = ASSISTANTFORREAD + "GetIncomeInfo";

	
	
	/**
	 * 获取订单列表
	 */
	
	public static String METHOD_ORDER_GETALLORDERFORBUYER = OrderFORREAD
			+ "GetOrderList";
	
	/**
	 * 确认退款
	 */
	
	public static String METHOD_ORDER_RMAConfirm = OrderFORWRITE
			+ "RMAConfirm";
	
	
	/**
	 * 判断是否可以开小票
	 */
	public static String METHOD_ORDER_CHECK = OrderFORREAD+"CheckIsCanCreateGeneralOrder";
	/**
	 * 开小票
	 */
	public static String METHOD_ORDER_CREATEGENERALORDER = OrderFORWRITE
			+ "CreateGeneralOrder";
	  
	
	
	/**
	 * 获取订单详情
	 */
	
	public static String METHOD_ORDER_GETORDERDETAIL = OrderFORREAD
			+ "GetBuyerOrderDetail";
	/**
	 * 社交管理--获取圈子列表
	 */
	
	public static String METHOD_CIRCLE_GETBUYERGROUPS = CircleFORREAD
			+ "GetBuyerGroups";
	
	/**
	 * 社交管理--修改圈子名称
	 */
	public static String METHOD_CIRCLE_RENAMEGROUP = CircleFORWRITE
			+ "RenameGroup";
	
	
	/**
	 * 社交管理--加入粉丝到圈子时显示的粉丝列表
	 */
	public static String METHOD_CIRCLE_GETVALIDFANSLISTTOGROUP = CircleFORREAD
			+ "GetValidFansListToGroup";
	

	
	/**
	 * 社交管理--删除圈子成员
	 */
	public static String METHOD_CIRCLE_REMOVEGROUPMEMBER = CircleFORWRITE
			+ "RemoveGroupMember";
	/**
	 * 社交管理--获取圈子详情
	 */
	
	public static String METHOD_CIRCLE_GETBUYERGROUPDETAIL = CircleFORREAD
			+ "GetGroupDetail";
	/**
	 * 社交管理--邀请粉丝加入圈子
	 */
	
	public static String METHOD_CIRCLE_ADDFANSTOGROUP = CircleFORWRITE
			+ "AddFansToGroup";
	/**
	 * 社交管理--新建圈子
	 */
	
	public static String METHOD_CIRCLE_CREATEGROUP = CircleFORWRITE
			+ "CreateGroup";
	
	/**
	 * 社交管理--修改圈子头像
	 */
	
	public static String METHOD_CIRCLE_CHANGEGROUPLOGO = CircleFORWRITE
			+ "ChangeGroupLogo";
	 
	
	
	/**
	 * 社交管理--删除圈子
	 */
	
	public static String METHOD_CIRCLE_DELETEGROUP = CircleFORWRITE
			+ "DeleteGroup";
	 
	/**
	 * 败家--加入圈子
	 */
	
	public static String METHOD_CIRCLE_ADDGROUP = CircleFORWRITE+ "JoinGroup";
	
	/**
	 * 败家--退出圈子
	 */
	
	public static String METHOD_CIRCLE_EXITGROUP = CircleFORWRITE+ "ExitGroup";
	
	
	/**
	 *  任务奖励---获取当前用户的活动列表
	 */
	
	public static String METHOD_PROMOTION_LIST = PromotionFORREAD
			+ "List";
	
	/**
	 *  任务奖励---获取当前用户的活动列表
	 */
	
	public static String METHOD_PROMOTION_DETAIL = PromotionFORREAD
			+ "Detail";
	
	
	/**
	 * buyer相关的接口
	 */
	
	/**
	 * 货款管理的信息
	 */
	public static String METHOD_BUYER_PAYMENTGOODS = BUYERFORREAD
	+ "PaymentGoods";
	
	/**
	 * 货款管理列表
	 */
	public static String METHOD_BUYER_PAYMENTGOODLIST = BUYERFORREAD
	+ "PaymentGoodsList";
	
	
	
	/**
	 * 获取门店列表 （养家）
	 */
	public static String METHOD_BUYER_GET_STORE_LIST = BUYERFORREAD
	+ "GetStoreList";
	
	/**
	 * 申请认证买手
	 */
	public static String METHOD_BUYER_CREATE_AUTH_BUYER = BUYERFORWRITE
			+ "CreateAuthBuyer";
	
	
	/**
	 * 买手首页统计信息
	 */
	public static String METHOD_BUYER_INDEX = BUYERFORREAD
			+ "Index";
	
	/**
	 * 设置店铺说明
	 */
	public static String METHOD_BUYER_SETSTOREDESCRIPTION = BUYERFORWRITE
			+ "SetStoreDescription";
	
	/**
	 * 败家 -获取订单列表
	 */
	
	public static String GETORDERLIST = OrderFORREAD+ "GetOrderListByState";

	/**
	 * 败家 -获取用户信息（新）
	 */
	
	public static String GETUSERINFO = USERURLFORREAD+ "GetUserInfo";
	
	
	/**
	 * 败家 -用户获取自己的订单详情
	 */
	
	public static String GETORDERLISTDETAILS = OrderFORREAD+ "GetUserOrderDetail";
	
	/**
	 * 败家 -获取用户是否可以养家等操作
	 */
	
	public static String METHODCHECKBUYERSTATUS = USERURLFORREAD+ "CheckBuyerStatus";
	
	
	
	
	
	/**
	 * 败家 -计算订单商品金额
	 */
	
	public static String GETCOMPUTEAMOUNT = PRODUCTFORREAD+ "ComputeAmount";
	
	/**
	 * 败家 -创建订单（生成订单 （败家）-新）
	 */
	
	public static String CREATEORDER = OrderFORWRITE+ "CreateOrder";
	
	/**
	 * 败家 -我的圈子
	 */
	
	public static String GETMYCIRCLE = CircleFORREAD+ "GetMyGroup";
	
	/**
	 * 获取用户的圈子(如果是买手显示由买手创建的圈子，如果是普通用户则显示用户加入的圈子)
	 */
	
	public static String GETUSERGROUPS = CircleFORREAD+ "GetUserGroups";
	
	/**
	 * 修改推送状态
	 */
	
	public static String changePushState = USERURLFORWRITE+ "changePushState";
	
	
	/**
	 * 败家 -我的圈子
	 */
	
	public static String GETRECOMMENDGROUP = CircleFORREAD+ "GetRecommendGroup";
	
	/**
	 * 获取按品牌划分的商品列表（发现→品牌界面）(败家)
	 */
	
	public static String GETBRANDPRODUCTLIST = PRODUCTFORREAD+ "GetBrandProductList";
	
	/**
	 * 获取同城用户的商品列表（发现→同城界面）(败家)
	 */
	
	public static String GETCITYPRODUCTLIST = PRODUCTFORREAD+ "GetCityProductList";
	
	/**
	 * 获取我收藏的商品列表-败家(新)
	 */
	
	public static String GETMYFAVORITEPRODUCTLIST = PRODUCTFORREAD+ "GetMyFavoriteProductList";
	
	
	/**
	 * 败家 -获取用户头像
	 */
	
	public static String GETUSERICON = USERURLFORREAD+ "GetUserLogo";
	
	/**
	 *获取用户收藏的商品列表
	 */
	
	public static String GETUSERFAVORITELIST = PRODUCTFORREAD+"GetUserFavoriteList";
	
	/**
	 * 败家申请退款 （认证买手商品退款）
	 */
	
	public static String METHOD_APPLY_RMA = OrderFORWRITE+ "Apply_Rma";
	
	/**
	 * 败家取消订单
	 */
	
	public static String METHOD_CANCELORDER = OrderFORWRITE+ "Void";
	
	/**
	 * 败家 确认提货
	 */
	
	public static String METHOD_CONFIRMGOODS = OrderFORWRITE+ "ConfirmGoods";
	
	/**
	 * 败家 认证买手商品取消退款
	 */
	
	public static String METHOD_CANCELRMA = OrderFORWRITE+ "CancelRma";
	
	/*****
	 * 客户获取自己的基本信息（新）
	 * **/
	public static String METHOD_MYINFO = USERURLFORREAD+ "GetMyInfo";
	
	/*****
	 * 获取买手的商品列表、上新商品列表
	 * **/
	public static String METHOD_GETUSERPRODUCTLIST = PRODUCTFORREAD+ "GetUserProductList";
	
	/*****
	 * 获取败家消息列表
	 * **/
	public static String METHOD_GETMESSAGELIST = CircleFORREAD+ "GetMessagesList";
	
	/*****
	 * 获取用户动态
	 * **/
	public static String METHOD_GETUSERDYNAMIC = CircleFORREAD+ "UserDynamic";
	
	/*****
	 *根据品牌名称检索品牌
	 * **/
	public static String METHOD_SEARCH = SEARCHFORREAD+ "Search";
	
	/*****
	 *获取圈子房间号
	 * **/
	public static String METHOD_GETROOMID = CircleFORWRITE+ "GetRoom";
	
	/*****
	 *分享商品
	 * **/
	public static String METHOD_PRODUCT_CREATESHARE = PRODUCTFORWRITE+ "CreateShare";
	
	
	/*****
	 *现金分享  （分享订单分享订单可获得红包）
	 * **/
	public static String METHOD_ORDER_CREATESHARE = OrderFORWRITE+ "CreateShare";
	
	
	/*****
	 *获取房间消息
	 * **/
	public static String METHOD_GETROOMMESSAE = CircleFORREAD+ "GetMessages";
	
	/*****
	 *聊天发送图片
	 * **/
	public static String METHOD_UPOADLCHATIMAGE = CircleFORWRITE+ "UploadChatImage";
	
	
	/*****
	 *微信支付回调url
	 * **/
	public static String METHOD_WEIXINCALLURL = weixinPAYCallBackUrl+ "WeiXinPayResult";
	
	/*****
	 *充值并退款微信回调地址
	 * **/
	public static String METHOD_PayAndDoRmaResult = weixinPAYCallBackUrl+ "PayAndDoRmaResult";




	/*****
	 *获取买手的基本圈子
	 * **/
	public static String METHOD_GetBuyerBaseCircle = CircleFORREAD+ "GetBuyerBaseGroup";

   //----------------------------------以下是会员卡----------------------------------------------
	/**
	 * 获取商品可用会员卡
	 */
	public static String METHOD_GetVipCards = V3FORREAD + "GetVipCards";
	
	
	
	//----------------------------------以下是开发/测试环境----------------------------------------------
	
	
	
	
	
//    private static String baseUrl = "http://123.57.77.86:8080/app/";//测试地址
////    private static String baseUrl = "http://123.57.52.187:8080/app/";//开发地址
//	private static String USERURL = baseUrl + "User/";// USER接口
//	private static String COMMONURL = baseUrl + "Common/";
//	private static String ADDRESS = baseUrl + "Address/";// 地址管理
//	private static String BRAND = baseUrl + "Brand/";// 品牌管理
//	private static String PRODUCT = baseUrl + "Product/";// 商品管理
//	private static String ASSISTANT = baseUrl + "Assistant/";// 买手相关的接口
//	private static String BUYER = baseUrl + "Buyer/";// 买手相关的接口
//	private static String Order = baseUrl + "Order/";// 订单相关的接口
//	private static String Circle = baseUrl + "Community/";// 圈子相关的接口
//	private static String SEARCH = baseUrl + "Search/";// 搜索
//	private static String Promotion = baseUrl + "Promotion/";//活动相关
//	private static String weixinBaseUrl = "https://api.mch.weixin.qq.com/";//微信接口
//	private static String weixinPAYUrl = weixinBaseUrl+"pay/";//微信支付接口
//	private static String weixinPAYCallBackUrl = baseUrl+"Payment/";//微信支平台回调
//	
//	
//	
//	
//	
//	/**
//	 * 判断用户是否关注了公共号
//	 */
//	public static String METHOD_GETUSERFLOWSTATUS = USERURL + "GetUserFlowStatus";
//	/**
//	 * 绑定微信
//	 */
//	public static String METHOD_BindWX = USERURL + "BindOutSideUser";
//	
//	/**
//	 * 微信登录
//	 */
//	public static String METHOD_BINDMOBILE = USERURL + "BindMobile";
//	/**
//	 * 微信登录
//	 */
//	public static String METHOD_wxLogin = USERURL + "OutSiteLogin";
//	
//	/*****
//	 * 微信创建统一订单
//	 * ***/
//	public static String METHOD_WXCREATEORDER = weixinPAYUrl + "unifiedorder";
//	
//	
//	/**
//	 * 修改昵称
//	 */
//	public static String changeNickName = USERURL + "ChangeNickname";
//
//	/**
//	 * 修改用户头像
//	 */
//	public static String changeUserLogo = USERURL + "ChangeUserLogo";
//	
//	/**
//	 * 注册----获取手机短信验证码
//	 */
//	public static String sendPhoneCode = USERURL + "SendMobileCode";
//	// 注册
//	public static String RegisterCompleate = USERURL + "RegisterCompleate";
//	/**
//	 * 校验手机验证
//	 */
//	public static String METHOD_VERIFYCODE = USERURL + "VerifyCode";
//	/**
//	 * 版本更新接口
//	 */
//	public static String METHOD_VERSION_UPDATE = COMMONURL + "CheckVersion";
//	/**
//	/**
//	 * 获取城市列表
//	 */
//	public static String METHOD_GETCITYLIST = COMMONURL + "GetCityList";
//	/**
//	/**
//	 * 获取阿里云key
//	 */
//	public static String METHOD_GETALIYUNKEY = COMMONURL + "GetALiYunAccessKey";
//	/**
//	 * 所有城市列表包含省
//	 */
//	public static String METHOD_ALLGETCITYLIST = COMMONURL + "GetAllCity";
//	/**
//	 * 不论省市 只需要传编号，就可以获取到下一级的数据。获取省列表parentId=0
//	 */
//	public static String METHOD_COMMON_GETCITYLISYBYPARENTID = COMMONURL + "GetCityListByParentId";
//	/**
//	 * 注册用户信息
//	 */
//	public static String METHOD_REGISTER = USERURL + "Register";
//	/**
//	 * 登录
//	 */
//	public static String METHOD_LOGIN = USERURL + "Login";
//	/**
//	 * 修改登录密码
//	 */
//	public static String METHOD_UPDATEPWD = USERURL + "ChangePassword";
//	/**
//	 * 重置手机密码
//	 */
//	public static String METHOD_RESETPASSWORD = USERURL + "ResetPassword";
//	/**
//	 * 我关注的人/我的粉丝
//	 */
//	public static String METHOD_GETUSERFAVOITE = USERURL + "GetUserFavoite";
//	
//	/**
//	 * 设置或取消我的关注
//	 */
//	public static String SETFAVOITE = USERURL + "Favoite";
//	
//	/**
//	 * 常见联系人地址
//	 */
//	public static String METHOD_ADDRESSCREATE = ADDRESS + "Create";
//	/**
//	 * 获取我的联系人地址列表
//	 */
//	public static String METHOD_MYADDRESSCREATE_LIST = ADDRESS + "My";
//
//	/**
//	 * 获取联系人地址详细
//	 */
//	public static String METHOD_ADDRESSCREATE_DETAILS = ADDRESS + "Details";
//
//	/**
//	 * 删除联系人地址
//	 */
//	public static String METHOD_ADDRESSCREATE_DELETE = ADDRESS + "Delete";
//
//	/**
//	 * 修改联系人地址信息
//	 */
//	public static String METHOD_ADDRESSCREATE_UPDATE = ADDRESS + "Edit";
//
//	/**
//	 * 设置默认联系人地址信息
//	 */
//	public static String METHOD_ADDRESSCREATE_DEFAULT = ADDRESS + "SetDefault";
//
//	/**
//	 * 获取默认联系人地址
//	 */
//	public static String METHOD_ADDRESSCREATE_GETDEFAULT = ADDRESS + "Default";
//
//	/**
//	 * ---------------------------------品牌管理接口----------------------------------
//	 * ---------
//	 */
//
//	/**
//	 * 获取品牌信息详细
//	 */
//	public static String METHOD_BRANDMANAGEER_DETAIL = PRODUCT + "GetProductListByBrandId";
//
//	
//	/**
//	 * 复制商品
//	 */
//	public static String METHOD_PRODUCT_COPY = PRODUCT + "Copy";
//
//	/**
//	 * 修改商品
//	 */
//	public static String METHOD_PRODUCT_UPDATE = PRODUCT + "Update";
//
//	/**
//	 * 根据名称搜索标签
//	 */
//	public static String METHOD_GETPRODUCTTAG = PRODUCT + "GetProductTag";
//
//	/**
//	 * 获取品牌列表
//	 */
//	public static String METHOD_BRANDMANAGEER_ALL = BRAND + "All";
//
//	/**
//	 * 按照时间获取传入时间后的品牌列表
//	 */
//	public static String METHOD_BRANDMANAGEER_REFRESH = BRAND + "Refresh";
//
//	/**
//	 * 按照品牌首字母获取品牌列表
//	 */
//	public static String METHOD_BRANDMANAGEER_GROUPALL = BRAND + "GroupAll";
//
//	/**
//	 * 按照品牌首字母获取品牌列表
//	 */
//	public static String METHOD_BRANDMANAGEER_GROUPREFRESH = BRAND
//			+ "GroupRefresh";
//
//	/**
//	 * ---------------------------------商品相关接口----------------------------------
//	 * ---------
//	 */
//
//	/**
//	 * 收藏或取消商品
//	 */
//	public static String METHOD_PRODUCTMANAGER_FAVOR = PRODUCT + "Favorite";
//	/**
//	 * 喜欢或取消喜欢商品
//	 */
//	public static String METHOD_PRODUCTMANAGER_LIKE = PRODUCT + "Like";
//	/**
//	 * 获取主页商品列表(买手街)
//	 */
//	public static String METHOD_PRODUCTMANAGER_HOMELIST = PRODUCT + "Index";
//	
//	/**
//	 * 我的买手
//	 */
//	public static String METHOD_PRODUCTMANAGER_MYBUYER = PRODUCT + "MyBuyer";
//	
//	/**
//	 * 获取商品被喜欢的用户列表
//	 */
//	public static String METHOD_PRODUCTMANAGER_LIKEDUSERS = PRODUCT
//			+ "LikedUsers";
//	/**
//	 * 获取我收藏的买手的商品列表
//	 */
//	public static String METHOD_PRODUCTMANAGER_MYBUYERPRODUCTLIST = PRODUCT
//			+ "MyBuyerProductList";
//	/**
//	 * 获取商品详情(败家)
//	 */
//	public static String METHOD_PRODUCTMANAGER_DETAIL = PRODUCT + "GetProductDetail";
//	
//	/**
//	 * 上传商品信息(买手)
//	 */
//	public static String METHOD_PRODUCTMANAGER_CREATE = PRODUCT + "Create";
//	
//	/**
//	 * 上传商品信息(买手)
//	 */
//	public static String METHOD_PRODUCTMANAGER_UPDATE = PRODUCT + "Update";
//	
//	
//	
//	/**
//	 * 商品管理列表
//	 */
//	public static String METHOD_PRODUCTLIST = PRODUCT+"GetBuyerProductList";
//	
//	
//	/**
//	 * 商品上线、下线（买手）
//	 */
//	public static String METHOD_PRODUCTMANAGER_ONLINE = PRODUCT + "OnLine";
//	
//
//	/**
//	 * 删除商品
//	 */
//	public static String METHOD_PRODUCTMANAGER_DELETE = PRODUCT + "Delete";
//	
//	
//	/**
//	 * ---------------------------------买手相关接口----------------------------------
//	 * ---------
//	 */
//	
//	
//	
//	/**
//	 * 提现货款
//	 */
//	public static String METHOD_ASSISTANT_WithdrawGoods= BUYER + "WithdrawGoods";
//	/**
//	 * 获取提现历史
//	 */
//	public static String METHOD_ASSISTANT_INCOMEREQUESTREDPACK= ASSISTANT + "Income_Request_RedPack";
//	/**
//	 * 获取提现历史
//	 */
//	public static String METHOD_ASSISTANT_GETINCOMEHISTORY = ASSISTANT + "GetIncomeHistory";
//	/**
//	 * 收益明细
//	 */
//	public static String METHOD_ASSISTANT_GETINCOMEINFO = ASSISTANT + "GetIncomeInfo";
//	/**
//	 * 获取自己的品牌列表
//	 */
//	public static String METHOD_ASSISTANT_BRANDS = ASSISTANT + "Brands";
//	/**
//	 * 获取销售码
//	 */
//	public static String METHOD_ASSISTANT_SALESCODES = ASSISTANT + "SalesCodes";
//	/**
//	 * 添加销售码
//	 */
//	public static String METHOD_ASSISTANT_SALESCODE_ADD = ASSISTANT
//			+ "SalesCode_Add";
//	/**
//	 * 获取支持的银行列表
//	 */
//	public static String METHOD_ASSISTANT_AVAIL_BANKS = ASSISTANT
//			+ "Avail_Banks";
//	/**
//	 * 获取分类
//	 */
//	public static String METHOD_ASSISTANT_AVAIL_TAG = ASSISTANT + "Avail_Tag";
//	/**
//	 * 根据品牌获取分类
//	 */
//	public static String METHOD_ASSISTANT_AVAIL_TAGBYBRAND = ASSISTANT
//			+ "Avail_TagByBrand";
//	/**
//	 * 导购获取收入信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME = ASSISTANT + "Income";
//	/**
//	 * 导购提现信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_RECEIVE = ASSISTANT
//			+ "Income_Received";
//	/**
//	 * 导购正在申请的提现信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUESTING = ASSISTANT
//			+ "Income_Requesting";
//	/**
//	 * 导购申请的提现成功的信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUEST_COMPLETED = ASSISTANT
//			+ "Income_RequestCompleted";
//	/**
//	 * 导购申请的提现失败的信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUESTFAILD = ASSISTANT
//			+ "Income_RequestFaild";
//	/**
//	 * 导购历史提现信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_HAISTORY = ASSISTANT
//			+ "Income_History";
//	/**
//	 * 导购冻结状态的提成信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_FROZEN = ASSISTANT
//			+ "Income_Frozen";
//	/**
//	 * 导购可提现状态的提成信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_AVAIL = ASSISTANT
//			+ "Income_Avail";
//	/**
//	 * 导购无效现状态的提成信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_VOID = ASSISTANT
//			+ "Income_Void";
//	/**
//	 * 导购提现
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUEST = ASSISTANT
//			+ "Income_Request";
//	/**
//	 * 导购提现 红包发放
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUEST_REDPACK = ASSISTANT
//			+ "Income_Request_RedPack";
//	/**
//	 * 导购提现信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_RECEIVED_REDPACK = ASSISTANT
//			+ "Income_Received_RedPack";
//	/**
//	 * 导购正在申请的提现信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUESTING_REDPACK = ASSISTANT
//			+ "Income_Requesting_RedPack";
//	/**
//	 * 导购申请的提现成功的信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUESTCOMPLETED_REDPACK = ASSISTANT
//			+ "Income_RequestCompleted_RedPack";
//
//	/**
//	 * 导购申请的提现失败的信息
//	 */
//	public static String METHOD_ASSISTANT_INCOME_REQUESTFAILD_REDPACK = ASSISTANT
//			+ "Income_RequestFaild_RedPack";
//	/**
//	 * 导购更新店铺名称 对应user的nickname
//	 */
//	public static String METHOD_ASSISTANT_UPDETE_NAME = ASSISTANT
//			+ "Update_Name";
//	/**
//	 * 导购更新手机
//	 */
//	public static String METHOD_ASSISTANT_UPDATE_MOBILE = ASSISTANT
//			+ "Update_Mobile";
//	/**
//	 * 导购更新模板
//	 */
//	public static String METHOD_ASSISTANT_UPDATE_TEMPLATE = ASSISTANT
//			+ "Update_Template";
//	/**
//	 * 导购获取最后一次使用的银行信息
//	 */
//	public static String METHOD_ASSISTANT_LATEST_BANKINFO = ASSISTANT
//			+ "Latest_BankInfo";
//
//	
//	
//	
//	/**
//	 * 获取订单列表
//	 */
//	
//	public static String METHOD_ORDER_GETALLORDERFORBUYER = Order
//			+ "GetOrderList";
//	
//	/**
//	 * 确认退款
//	 */
//	
//	public static String METHOD_ORDER_RMAConfirm = Order
//			+ "RMAConfirm";
//	  
//	/**
//	 * 开小票
//	 */
//	public static String METHOD_ORDER_CREATEGENERALORDER = Order
//			+ "CreateGeneralOrder";
//	  
//	
//	
//	/**
//	 * 获取订单详情
//	 */
//	
//	public static String METHOD_ORDER_GETORDERDETAIL = Order
//			+ "GetBuyerOrderDetail";
//	/**
//	 * 社交管理--获取圈子列表
//	 */
//	
//	public static String METHOD_CIRCLE_GETBUYERGROUPS = Circle
//			+ "GetBuyerGroups";
//	
//	/**
//	 * 社交管理--修改圈子名称
//	 */
//	public static String METHOD_CIRCLE_RENAMEGROUP = Circle
//			+ "RenameGroup";
//	
//	
//	/**
//	 * 社交管理--修改圈子名称
//	 */
//	public static String METHOD_CIRCLE_GETVALIDFANSLISTTOGROUP = Circle
//			+ "GetValidFansListToGroup";
//	
//
//	
//	/**
//	 * 社交管理--删除圈子成员
//	 */
//	public static String METHOD_CIRCLE_REMOVEGROUPMEMBER = Circle
//			+ "RemoveGroupMember";
//	/**
//	 * 社交管理--获取圈子详情
//	 */
//	
//	public static String METHOD_CIRCLE_GETBUYERGROUPDETAIL = Circle
//			+ "GetGroupDetail";
//	/**
//	 * 社交管理--邀请粉丝加入圈子
//	 */
//	
//	public static String METHOD_CIRCLE_ADDFANSTOGROUP = Circle
//			+ "AddFansToGroup";
//	/**
//	 * 社交管理--新建圈子
//	 */
//	
//	public static String METHOD_CIRCLE_CREATEGROUP = Circle
//			+ "CreateGroup";
//	
//	/**
//	 * 社交管理--修改圈子头像
//	 */
//	
//	public static String METHOD_CIRCLE_CHANGEGROUPLOGO = Circle
//			+ "ChangeGroupLogo";
//	 
//	
//	
//	/**
//	 * 社交管理--删除圈子
//	 */
//	
//	public static String METHOD_CIRCLE_DELETEGROUP = Circle
//			+ "DeleteGroup";
//	 
//	/**
//	 * 败家--加入圈子
//	 */
//	
//	public static String METHOD_CIRCLE_ADDGROUP = Circle+ "JoinGroup";
//	
//	/**
//	 * 败家--退出圈子
//	 */
//	
//	public static String METHOD_CIRCLE_EXITGROUP = Circle+ "ExitGroup";
//	
//	
//	/**
//	 *  任务奖励---获取当前用户的活动列表
//	 */
//	
//	public static String METHOD_PROMOTION_LIST = Promotion
//			+ "List";
//	
//	/**
//	 *  任务奖励---获取当前用户的活动列表
//	 */
//	
//	public static String METHOD_PROMOTION_DETAIL = Promotion
//			+ "Detail";
//	
//	
//	/**
//	 * buyer相关的接口
//	 */
//	
//	/**
//	 * 货款管理的信息
//	 */
//	public static String METHOD_BUYER_PAYMENTGOODS = BUYER
//	+ "PaymentGoods";
//	
//	/**
//	 * 货款管理列表
//	 */
//	public static String METHOD_BUYER_PAYMENTGOODLIST = BUYER
//	+ "PaymentGoodsList";
//	
//	
//	
//	/**
//	 * 获取门店列表 （养家）
//	 */
//	public static String METHOD_BUYER_GET_STORE_LIST = BUYER
//	+ "GetStoreList";
//	
//	/**
//	 * 买手首页统计信息
//	 */
//	public static String METHOD_BUYER_CREATE_AUTH_BUYER = BUYER
//			+ "CreateAuthBuyer";
//	
//	
//	/**
//	 * 买手首页统计信息
//	 */
//	public static String METHOD_BUYER_INDEX = BUYER
//			+ "Index";
//	
//	/**
//	 * 设置店铺说明
//	 */
//	public static String METHOD_BUYER_SETSTOREDESCRIPTION = BUYER
//			+ "SetStoreDescription";
//	
//	/**
//	 * 败家 -获取订单列表
//	 */
//	
//	public static String GETORDERLIST = Order+ "GetOrderListByState";
//
//	/**
//	 * 败家 -获取用户信息（新）
//	 */
//	
//	public static String GETUSERINFO = USERURL+ "GetUserInfo";
//	
//	
//	/**
//	 * 败家 -获取订单详情
//	 */
//	
//	public static String GETORDERLISTDETAILS = Order+ "GetUserOrderDetail";
//	
//	/**
//	 * 败家 -获取用户是否可以养家等操作
//	 */
//	
//	public static String METHODCHECKBUYERSTATUS = USERURL+ "CheckBuyerStatus";
//	
//	
//	
//	
//	
//	/**
//	 * 败家 -获取订单详情
//	 */
//	
//	public static String GETCOMPUTEAMOUNT = PRODUCT+ "ComputeAmount";
//	
//	/**
//	 * 败家 -创建订单（生成订单 （败家）-新）
//	 */
//	
//	public static String CREATEORDER = Order+ "CreateOrder";
//	
//	/**
//	 * 败家 -我的圈子
//	 */
//	
//	public static String GETMYCIRCLE = Circle+ "GetMyGroup";
//	
//	/**
//	 * 获取用户的圈子(如果是买手显示由买手创建的圈子，如果是普通用户则显示用户加入的圈子)
//	 */
//	
//	public static String GETUSERGROUPS = Circle+ "GetUserGroups";
//	
//	/**
//	 * 修改推送状态
//	 */
//	
//	public static String changePushState = USERURL+ "changePushState";
//	
//	
//	/**
//	 * 败家 -我的圈子
//	 */
//	
//	public static String GETRECOMMENDGROUP = Circle+ "GetRecommendGroup";
//	
//	/**
//	 * 获取按品牌划分的商品列表（发现→品牌界面）(败家)
//	 */
//	
//	public static String GETBRANDPRODUCTLIST = PRODUCT+ "GetBrandProductList";
//	
//	/**
//	 * 获取同城用户的商品列表（发现→同城界面）(败家)
//	 */
//	
//	public static String GETCITYPRODUCTLIST = PRODUCT+ "GetCityProductList";
//	
//	/**
//	 * 获取我收藏的商品列表-败家(新)
//	 */
//	
//	public static String GETMYFAVORITEPRODUCTLIST = PRODUCT+ "GetMyFavoriteProductList";
//	
//	
//	/**
//	 * 败家 -获取用户头像
//	 */
//	
//	public static String GETUSERICON = USERURL+ "GetUserLogo";
//	
//	/**
//	 *获取用户收藏的商品列表
//	 */
//	
//	public static String GETUSERFAVORITELIST = PRODUCT+"GetUserFavoriteList";
//	
//	/**
//	 * 败家申请退款 （认证买手商品退款）
//	 */
//	
//	public static String METHOD_APPLY_RMA = Order+ "Apply_Rma";
//	
//	/**
//	 * 败家取消订单
//	 */
//	
//	public static String METHOD_CANCELORDER = Order+ "Void";
//	
//	/**
//	 * 败家 确认提货
//	 */
//	
//	public static String METHOD_CONFIRMGOODS = Order+ "ConfirmGoods";
//	
//	/**
//	 * 败家 认证买手商品取消退款
//	 */
//	
//	public static String METHOD_CANCELRMA = Order+ "CancelRma";
//	
//	/*****
//	 * 客户获取自己的基本信息（新）
//	 * **/
//	public static String METHOD_MYINFO = USERURL+ "GetMyInfo";
//	
//	/*****
//	 * 获取买手的商品列表、上新商品列表
//	 * **/
//	public static String METHOD_GETUSERPRODUCTLIST = PRODUCT+ "GetUserProductList";
//	
//	/*****
//	 * 获取败家消息列表
//	 * **/
//	public static String METHOD_GETMESSAGELIST = Circle+ "GetMessagesList";
//	
//	/*****
//	 * 获取用户动态
//	 * **/
//	public static String METHOD_GETUSERDYNAMIC = Circle+ "UserDynamic";
//	
//	/*****
//	 *根据品牌名称检索品牌
//	 * **/
//	public static String METHOD_SEARCH = SEARCH+ "Search";
//	
//	/*****
//	 *获取圈子房间号
//	 * **/
//	public static String METHOD_GETROOMID = Circle+ "GetRoom";
//	
//	/*****
//	 *分享商品
//	 * **/
//	public static String METHOD_PRODUCT_CREATESHARE = PRODUCT+ "CreateShare";
//	
//	
//	/*****
//	 *现金分享  （分享订单分享订单可获得红包）
//	 * **/
//	public static String METHOD_ORDER_CREATESHARE = Order+ "CreateShare";
//	
//	
//	/*****
//	 *获取房间消息
//	 * **/
//	public static String METHOD_GETROOMMESSAE = Circle+ "GetMessages";
//	
//	/*****
//	 *聊天发送图片
//	 * **/
//	public static String METHOD_UPOADLCHATIMAGE = Circle+ "UploadChatImage";
//	
//	
//	/*****
//	 *微信支付回调url
//	 * **/
//	public static String METHOD_WEIXINCALLURL = weixinPAYCallBackUrl+ "WeiXinPayResult";
//	
//	/*****
//	 *充值并退款微信回调地址
//	 * **/
//	public static String METHOD_PayAndDoRmaResult = weixinPAYCallBackUrl+ "PayAndDoRmaResult";
	
	
	
	/**
	 * 获取写数据接口的url
	 * @return
	 */
	private static String getServerUrlForWrite(){
		if(Constants.PublishStatus.equals("1")){//开发环境
			return "http://123.57.52.187:8070/app/";
		}else if(Constants.PublishStatus.equals("2")){//正式环境
			return "http://appw.joybar.com.cn/app/";
		}else if(Constants.PublishStatus.equals("3")){//测试环境
			return "http://123.57.77.86:8080/app/";
		}else{
			return "";
		}
	}
	/**
	 * 获取读数据接口的url
	 * @return
	 */
	private static String getServerUrlForRead(){
		if(Constants.PublishStatus.equals("1")){//开发环境
			return "http://123.57.52.187:8070/app/";
		}else if(Constants.PublishStatus.equals("2")){//正式环境
			return "http://appr.joybar.com.cn/app/";
		}else if(Constants.PublishStatus.equals("3")){//测试环境
			return "http://123.57.77.86:8080/app/";
		}else{
			return "";
		}
	}
}
