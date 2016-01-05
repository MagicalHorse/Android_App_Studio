package com.shenma.yueba.constants;

import android.os.Environment;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;

public class Constants {
    //public static final String PublishStatus = "1";//开发环境
    //public static final String PublishStatus = "2";//生产环境
      public static final String PublishStatus = "3";//测试环境


    public static final String aliyunDirForDev = "apprss";//阿里云目录，开发环境
    public static final String aliyunDirForWork = "app-prod-rss";//阿里云目录，生产环境


    // 常量字段
    public static final int CURRPAGE_VALUE = 1;// 当前页
    public static final int PAGESIZE_VALUE = 10;// 没有显示的个数

    public static final int REQUESTCODE = 100;
    public static final int RESULTCODE = 101;
    public static final int RESULTCODE2 = 102;

    public static final int title_text_normal_size = 17;
    public static final int title_text_selected_size = 20;

    // 网络请求的字段
    public static final String HTTPCHANNEL = "channel";// 附加参数
    public static final String ANDROID = "Android";// 附加参数
    public static final String PRIVATEKEY = "97CCE28B-345C-4C83-ACE3-C2E349BF53D7";// 私钥
    public static final String PageSize = "10";

    // appid
    // 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
    // android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String WX_APP_ID = Constants.PublishStatus.equals("2") ? MyApplication.getInstance().getResources().getString(R.string.wenxin_appid_work) : MyApplication.getInstance().getResources().getString(R.string.wenxin_appid);
    // 商户号
    public static final String WX_MCH_ID = Constants.PublishStatus.equals("2") ? MyApplication.getInstance().getResources().getString(R.string.wenxin_shanghuno_work) : MyApplication.getInstance().getResources().getString(R.string.wenxin_shanghuno);
    // 商户号
    public static final String WX_SECRET = Constants.PublishStatus.equals("2") ? MyApplication.getInstance().getResources().getString(R.string.wenxin_appsecret_work) : MyApplication.getInstance().getResources().getString(R.string.wenxin_appsecret);

    // API密钥，在商户平台设置
    public static final String WX_API_KEY = Constants.PublishStatus.equals("2") ? MyApplication.getInstance().getResources().getString(R.string.wenxin_api_key_work) : MyApplication.getInstance().getResources().getString(R.string.wenxin_api_key);
    //普通支付微信回调通知服务器的url
    public static final String WX_NOTIFY_URL = HttpConstants.METHOD_WEIXINCALLURL;
    //充值并退款微信回调通知服务器的url
    public static final String WX_PAYAREFUND_URL = HttpConstants.METHOD_PayAndDoRmaResult;


    public static final String KEY = "key";// 阿里云上传图片需要的key
    public static final String MOBILE = "mobile";// 手机号
    public static final String isverifymobile = "isverifymobile";// 是否校验
    public static final String CODE = "code";// 验证码
    public static final String NAME = "name";// 用户名
    public static final String LOGO = "logo";// 头像
    public static final String PARENTID = "parentId";// 父ID
    public static final String _PASSWORD = "password";// 密码
    public static final String _OLDPASSWORD = "oldpassword";// 密码
    public static final String CITYID = "cityid";// 城市ID
    public static final String CLIENTVERSION = "client_version";// 客户端版本
    public static final String SIGN = "sign";// MD5加密
    public static final String UUID = "uid";// 随机数
    public static final String TOKEN = "token";// 随机数
    public static final String PAGE = "Page";// 第几页，页码
    public static final String AMOUNT = "Amount";// 账户
    public static final String ORDERNOS = "orderNos";//订单号
    public static final String INCOMESTATUS = "IncomeStatus";// 收入明细状态
    public static final String INCOMETRANSFERSTATUS = "IncomeTransferStatus";// 收入明细状态
    public static final String PAGESIZE = "Pagesize";// 页size
    public static final String LONGITUDE = "Longitude";//经度
    public static final String SORT = "sort";//排序方式
    public static final String LATITUDE = "Latitude";//维度
    public static final String ID = "Id";
    public static final String USERIDSTR = "useridstr";
    public static final String ORDER_NO = "OrderNo";
    public static final String CustomerId = "CustomerId";
    public static final String OrderProductType = "OrderProductType";
    public static final String STATUS = "Status";
    public static final String GROUPID = "groupid";
    public static final String DESCRIPTION = "description";

    public static final String BRANDID = "BrandId";// 品牌编号
    public static final String TYPE = "Type";// 品牌编号
    public static final String REFRESHTS = "Refreshts";// 品牌编号
    public static final String PRODUCTID = "productId";// 商品编号
    public static final String PRODUCTI_USERID = "UserId";// 当前登录的用户id
    public static final String PROMOTIONID = "promotionId";// 活动id
    public static final String JSON = "json";// json数据

    public static int SCREENWIDTH = 0;//
    public static int SCREENHEITH = 0;//

    public static String PROJECT_NAME = "joybar";
    /**
     * 存放json缓存的路径
     */
    public final static String JsonPath = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/json/";

    public static final String PATH = Environment.getExternalStorageDirectory()
            + "/anmoke/Record/";// 语音存放的路径
    public static final String ISRECOMMNET_STORE_FLAG = "1";// 是推荐的按摩店
    public static final String NOTRECOMMNET_STORE_FLAG = "0";// 不是推荐的按摩店
    public static final String ISFAMOUS_FLAG = "1";// 名医店
    public static final String NOTFAMOUS_FLAG = "0";// 不是名医店

    public static String AUTOLOGIN = "autoLogin";
    public static String ISNOT_FIRST_INSTALL = "isNotFristInstall";// 不是第一次安装

    public static String NORMAL = "normal";// 普通消息的类型
    public static String SAYHELLO = "sayHello";
    public static String AUDIO = "audio";// 语音类型
    public static String ORDER = "order";// 订单的消息
    public static String IMAGE = "image";// 图片类型
    public static String SharedPerferenceName = "anmoke_sp";

    public static String oneCode = "";
    public static String twoCode = "";
    public static String threeCode = "";

    /**
     * 保存用户信息的字段
     */
    public static String SIGNATURE = "signature";// 签名
    public static String USERIMAGE = "userImage";// 头像
    public static String USERSEX = "userSex";// 性别
    public static String USERAGE = "userAge";// 年龄
    public static String USERNICKNAME = "userNickName";// 昵称
    public static String USERAREA = "userArea";// 所在地区
    public static String NEED_BACK = "needBack";// 需要登陆后返回

    // 网络状态的参数
    public static int NONet = -1;// 没有网络连接
    public static int WIFI = 1;// WIFI网络
    public static int CMNET = 2;// net网络
    public static int CMWAP = 3;// wap网络

    public static String CUSTOMER_ID = "customer_id";// 意见反馈里的
    public static String FAMOUS = "famous";// 是否是名医店
    public static String RECOMMEND = "recommend";// 是否是推荐
    public static String USERID = "userid";// 用户id
    public static String FROMID = "fromId";
    public static String USER_AGE = "user_age";// 用户年龄
    public static String USER_HEAD_IMAGE = "user_head_image";// 用户头像
    public static String USER_ADDRESS = "user_address";// 用户地址
    public static String USER_MOBILE = "user_mobile";// 用户电话
    public static String STOREENVIRO = "store_enviro";// 店名环境
    public static String TELEPHONENUM = "telephoneNum";// 电话号码
    public static String LEVEL = "level";// 用户id 1  普通用户  4  专柜买手    8 认证买手
    public static String AREA = "area";// 地区

    public static String USERNAME = "username";// 用戶名
    public static String USER_NAME = "user_name";// 用戶名

    public static final int ChOOSE_CITY_TWO_ACTIVITY = 19;
    public static final int ChOOSE_CITY_TWO_ACTIVITY2 = 20;

    public static final int MODIFY_NICKNAME = 1;// 修改昵称
    public static final int MODIFY_SIGN = 2;// 修改签名
    public static final int MODIFY_SEX = 3;// 修改性别
    public static final int MODIFY_SUCCESS = 4;// 修改成功

    public static String CHANNEL = "2";
    public static String PLATFORM = "android";
    public static String IMEI = "";
    public static final String COMMUNITYID = "1";
    public static String PHONENUM = "";
    public static String TOKENSTR = "";

    // INTENT跳转的KEY
    public final static String INTENT_USER_KEY = "User";
    public final static String INTENT_PET_KEY = "Pet";
    public final static String INTENT_KEY_FROM_FOURACTIVITY = "FourActivity";
    public final static String INTENT_KEY_FROM_CHATACTIVITY = "ChatActivity";
    public final static String INTENT_KEY_ISMODIFY_NOTADDNEW = "ModifyNotAddnew";
    public final static String INTENT_INDEX_PET_IN_USER = "Index";
    public final static String INTENT_USER_OR_PET_IMGPATH = "PetOrUser";

    // 存放国标一级汉字不同读音的起始区位码
    public static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    public static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'w', 'x', 'y', 'z'};

    // SD卡路径(有"/")
    public static String SDCARDPATH = Environment.getExternalStorageDirectory()
            + "/";
    // 存贮图片的app文件夹名
    public final static String APP_WENJIANJIA = "joybar/";
    public final static String APP_WENJIANJIA_PIC = "anmoke/chatpic/";// 聊天的圖片
    public final static String SD = Constants.SDCARDPATH
            + Constants.APP_WENJIANJIA;
    public final static String SD_PIC = Constants.SDCARDPATH
            + Constants.APP_WENJIANJIA_PIC;
    // apk新版本要存的名字
    public final static String apkName = "joybar.apk";

    // 本地图片存贮后缀名
    public final static String IMG_USER_EXTENSION_PNG = "u.png";// 原图
    public final static String IMG_PET_EXTENSION_PNG = "p.png";
    public final static String IMG_USER_X_EXTENSION_PNG = "xu.png";// 小图
    public final static String IMG_USER_D_EXTENSION_PNG = "du.png";// 大图
    public final static String IMG_PET_X_EXTENSION_PNG = "xp.png";
    public final static String IMG_PET_D_EXTENSION_PNG = "dp.png";

    // public static String XMPPSERVER = "pc-20130626uzwu";// 服务器名 云凯
    // // //云凯服务器
    // public final static String URL_POST =
    // "http://192.168.1.109/baojiantong/amk/appCustomer.do";
    // public final static String IP = "192.168.1.109";//云凯服务器
    // 云凯服务器
    // public final static String URL_POST =
    // "http://192.168.0.239/baojiantong/amk/appCustomer.do";
    // public final static String IP = "192.168.0.239";//云凯服务器

    public static String XMPPSERVER = "pc-20131023tfjn";// 服务器名
    public final static String URL_POST = "http://www.51jlt.com/amk/appCustomer.do";
    public final static String IP = "www.51jlt.com";

    // public final static String URL_POST =
    // "http://www.51jlt.com/amk/appCustomer.do";
    // public final static String IP = "www.51jlt.com";

    //
    // 东旭服务器如下
    // public final static String IP = "192.168.0.89";
    // public final static String URL_POST =
    // "http://192.168.0.89/baojiantong/amk/appCustomer.do";

    // 20服务器如下
    // public final static String URL_POST =
    // "http://192.168.0.20:8880/baojiantong/amk/appCustomer.do";
    // public final static String IP = "192.168.0.20";

    // 公网服务器，正式线，如下：
    // public final static String URL_POST =
    // "http://182.92.66.194:8080/amk/appCustomer.do";
    // public final static String IP = "182.92.66.194";

    // URL SERVICE
    public final static String SERVICE = "service";
    // URL 认证服务SERVICE_SSO
    public final static String SERVICE_SSO = "service.uri.pet_sso";
    // URL 用户中心服务SERVICE_USER
    public final static String SERVICE_USER = "service.uri.pet_user";
    // URL 圈子模块 注册名：service.uri.pet_bbs
    public final static String SERVICE_Circle = "service.uri.pet_bbs";
    // URL 用户反馈 注册名：service.uri.pet_bbs
    public final static String USER_FEEDBACK = "service.uri.pet_feedback";
    // URL 用户赞 注册名：service.uri.pet_bbs
    public final static String USER_ZAN = "service.uri.pet_pat";
    // RUL 用户动态 注册名：service.uri.pet_states
    public final static String USER_STATES = "service.uri.pet_states";
    // RUL 公共美图 注册名：service.uri.pet_albums
    public final static String USER_PICTURE = "service.uri.pet_albums";
    // RUL 宠物品种 注册名：service.uri.pet_ency
    public final static String PET_VARIETY = "service.uri.pet_ency";
    // URL 养宠经验 注册名：service.uri.pet_exper
    public final static String PET_EXPER = "service.uri.pet_exper";

    // 接收参数
    public static String SUCCESS = "1";

    // 开机图片的名字
    public static String STARTIMAGE = "startimage";
    // share里存的versionNane
    public static final String VERSIONNAME = "versionNane";
    public static final String VERSIONCODE = "versionCode";
    // share里存的FirstTime 判断是不是第一次安装
    public static final String FIRSTTIME = "FirstTime";

    public static final String CRICLE_PAGE_NUMBER = "pageNo";// 第几页
    public static final String CRICLE_PAGESIZE = "pageSize";// 一页多少条
    public final static String NICKNAME = "nickname";
    public final static String USER_NICKNAME = "user_nickName";
    public final static String USER_SEX = "user_sex";
    public final static String HOBBY = "hobby";
    public final static String STATE = "state";
    public final static String WORK_STATUS = "workstatus";
    public final static String USER_SIGNATURE = "user_signature";

    public final static String PETTYPE_JSON_KEY_URL = "petType";
    public final static String PERSON_OR_PET_JSON_KEY_URL = "personOrPet";
    public final static String SERVICE_JSON_KEY_URL = "service";

    public final static String PET_IMG_ID = "index";
    public final static String IS_FROM_NEARBYPELPLE = "IS_FROM_NEARBYPELPLE";

    public final static int DELETE_REPLY = 1;
    public final static int DELETE_ZANDYNAMIC = 2;

    public static String REPLY = "reply";
    public static String ZANDYNAMIC = "zanDynamic";
    public static String DYNAMIC = "dynamic";
    public static String TOPIC = "topic";

    public static final String SEX = "sex";

    public static final String FIRST_START_PUSH = "first_start_push";
    public static final String FIRST_START_PUSH2 = "first_start_push2";
    public static final String FIRST_START_PUSH3 = "first_start_push3";

    public static final String ACTION_SHOW_NOTIFICATION = "petcircle.ui.SHOW_NOTIFICATION";
    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

    public static final int PAGESIZE_FOR_PUBLIC = 10;// 公用的每页显示条数
    public static final int PAGE_FOR_PUBLIC = 1;// 公用的从加载第几页

    public static final String ANMODIANTYPE = "anmodianType";// 按摩店类型
    public static final String USER_LAT = "user_lat";// 经度
    public static final String USER_LON = "user_lon";// 维度
    public static final String STOREID = "storeid";// 店铺id
    public static final String ACTIONTYPE = "actionType";// 方法名
    public static final String STORE_ID = "store_id";// 店铺id
    public static final String STORE_NAME = "store_name";// 店铺名称
    public static final String STORE_TYPE_ID = "storetype_id";// 店铺类型
    public static final String AREA_ID = "area_id";// 店铺所在地区
    public static final String STORE_ADDRESS = "store_address";// 店铺地区

    public static final String STORE_ADDRESS_DETAIL = "address_detail";// 店铺具体位置
    public static final String STORE_NUMBER_COUNT = "number";// 店铺技师人数,店铺房间数
    public static final String STORE_MOBILE = "store_mobile";// 店铺电话号码
    public static final String STORE_PRICE = "store_leve";// 店铺服务价格
    public static final String STORE_DESC = "store_desc";// 店铺简介
    public static final String USER_NEW_PASSWORD = "user_newPassword";// 店铺新密码
    public static final String USER_OLD_PASSWORD = "user_oldPassword";// 店铺旧密码
    public static final String STORE_MASSID = "store_massid";// 店铺技师id
    public static final String MASSID = "massid";// 技师id
    public static final String MASS_ID = "mass_id";// 技师id,预定按摩师的接口里的
    public static final String MASS_PEOPLE_COUNT = "peopleCount";// 预订界面里的按摩客人数

    public static final String SERVICEID = "serviceId";// 服务项目的id
    public static final String DATE = "date";//

    public static final String STORE_MASS_STATE = "store_massState";// 技师审核状态
    public static final String STORE_IMAGE = "store_massState";// 店面图片
    public static final String STORE_STAR = "store_massState";// 店面星级
    public static final String USER_KEY = "user_key";
    public static final String PAGENUMBER = "pageNmuber";
    public static final String MASSLEVEL = "mass_level";
    public static final String PAGECOUNT = "pageCount";
    public static final String MASSCOMMUN = "mass_commun";
    public static final String PAGE_NO = "page_no";
    public static final String PAGE_COUNT = "page_count";

    public static final String MOBILENUM = "mobile";
    public static final String VALIDATE_CODE = "validate_code";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_FUN = "user_fun";
    public static final String STORETYPE = "anmodianType";
    public static final String DISCOUNT = "discount";
    public static final String PRICERANGE = "priceRange";
    public static final String PRICE = "price";
    public static final String SALECODE = "saleCode";
    public static final String STORE_TITLE = "title_store";
    public static final String MASS_TITLE = "title_masseur";
    public static final String STORE_CONTENT = "note_store";
    public static final String MASS_CONTENT = "note_masseur";
    public static final String STARTPRICERANGE = "startPriceRange";
    public static final String ENDPRICERANGE = "endPriceRange";

    public static final String STORE_MASS_COUNT = "store_masscount";//
    public static final String STORE_ROOM_COUNT = "store_roomcount";//
    public static final String STORE_TYPE = "store_type";//
    public static final String STORE_AREA = "store_area";//
    public static final String STOREADDRESS = "store_address";//
    public static final String STOREPRICE = "store_price"; //
    public static final String STORESERVE = "store_serve"; //
    public static final String MASSSERVE = "mass_serve";
    public static final String EMAIL = "email"; //
    public static final String PWD = "pwd"; //
    public static final String PASSWORD = "password"; //
    public static final String ISLOGIN = "is_login"; //
    public static final String SESSION_ID = "session_Id"; //
    public static final String VALIDATE = "validate_code"; //
    public static final String STOREADDRESSDETAIL = "store_addressdetail";

    /**
     * 数据库--消息表----所需要的字段
     */

    public static final String MsgInfo = "msgInfo"; // 存贮消息的表明
    public static final String MYID = "myid"; // 登陆用户电话
    public static final String OTHERID = "otherId";// 给用户聊天的电话
    // public static final String TO_MOBILE="to_mobile"; //
    // public static final String FROM_MOBILE="from_mobile"; //消息来源的电话，也就是jid
    public static final String MsgContent = "msgContent";// 消息内容
    public static final String IsLeft = "isLeft";// 判断是自己的消息还是他人的,true就是他人的
    public static final String IsRead = "isRead";// 已读和未读的状态
    public static final String MSG_TYPE = "msg_type";// 消息类型
    public static final String MSG_TIME = "msg_time";// 消息时间
    public static final String AUDIO_PATH = "audio_path";// 语音信息的地址
    public static final String IMAGE_PATH = "image_path";// 图片信息的地址
    public static final String LON = "lon";// 经度
    public static final String LAT = "lat";// 纬度

    /**
     * 数据库--订单表----所需要的字段
     */
    public static final String OrderInfo = "orderInfo"; // 订单表名

    public static final String ORDER_ID = "orderId"; // 订单id
    public static final String ORDER_STORE_ID = "storeId"; // 预定的店面id
    public static final String ORDER_MASS_ID = "massId";// 预定在技师id
    public static final String ORDER_CUSTOMER_ID = "customerId";// 顾客id
    public static final String ORDER_SERVICE_ID = "serviceId";// 服务id
    public static final String ORDER_SERVICE_NAME = "serviceName";// 服务名称
    public static final String ORDER_PERSON_COUNT = "personCount";// 预定人数
    public static final String ORDER_INCOME_TIME = "incomeTime";// 来店时间
    public static final String ORDER_PRICE = "price";// 订单价格

    /**
     * 数据库--发消息的人的基本信息
     */
    public static final String CHAT_USER_INFO = "ChatUserInfo"; // 存贮聊天列表中消息的表名
    public static final String FRIEND_SEX = "friend_sex"; // 性别
    public static final String FRIEND_NICKNAME = "friend_nickName";// 昵称
    public static final String FRIEND_DISTANCE = "friend_distance";// 距离
    public static final String FRIEND_HEAD_IMAGE = "friend_headImage";// 头像
    public static final String USER_ID = "user_id";// 用户id
    public static final String FRIEND_JID = "friend_jid";// 好友jid,在这里是电话
    public static final String FRIENDID = "friendId";//

    public static final String MsgInfoForLastMsg = "msgInfoForLastMsg"; // 存贮所有人消息的最后一条

    /**
     * 各个表名
     */
    public static final String MESSAGE = "Message";// 消息表名
    public static final String CONTACT = "Contact";// 联系人表名

    public static final String IsMass = "1";// 是技师
    public static final String IsStore = "2";// 是店面
    public static final String IsUser = "3";// 是客人
    /***
     * XlistView下拉刷新
     */
    public static final int FIRST_OR_REFRESH = 1;
    public static final int LOAD_MORE = 2;
    public static final int SHOW_TOAST = 3;
    public static final int SHOW_TOAST2 = 4;

    public static final int SRORETYPE = 1;// 店铺类型

    public static final int REGISTER_ACTIVITY = 0;
    public static final int REGISTER_ACTIVITY2 = 1;
    public static final int REGISTER2_ACTIVITY = 2;
    public static final int REGISTER2_ACTIVITY2 = 3;
    public static final int REGISTER3_ACTIVITY = 4;
    public static final int REGISTER3_ACTIVITY2 = 5;
    public static final int REGISTER4_ACTIVITY = 6;
    public static final int REGISTER4_ACTIVITY2 = 7;
    public static final int REGISTER5_ACTIVITY = 8;
    public static final int REGISTER5_ACTIVITY2 = 9;
    public static final int REGISTER5_ACTIVITY3 = 10;
    public static final int EDIT_INFO_ACTIVITY = 11;
    public static final int EDIT_INFO_ACTIVITY2 = 12;
    public static final int EDIT_INFO_ACTIVITY3 = 13;
    public static final int EDIT_INFO_ACTIVITY4 = 14;
    public static final int ChOOSE_CITY_ONE_ACTIVITY = 15;
    public static final int ChOOSE_CITY_ONE_ACTIVITY2 = 16;
    public static final int EDIT_INFO_ACTIVITY5 = 17;
    public static final int EDIT_INFO_ACTIVITY6 = 18;
    public static final int EDIT_INFO_ACTIVITY7 = 19;
    public static final int EDIT_INFO_ACTIVITY8 = 20;
    public static final int EDIT_INFO_ACTIVITY9 = 21;
    public static final int EDIT_INFO_ACTIVITY10 = 22;
    public static final int EDIT_INFO_ACTIVITY11 = 23;
    public static final int EDIT_INFO_ACTIVITY12 = 24;
    public static final int EDIT_INFO_ACTIVITY13 = 25;
    public static final int EDIT_INFO_ACTIVITY14 = 26;
    public static final int EDIT_INFO_ACTIVITY15 = 27;
    public static final int EDIT_INFO_ACTIVITY16 = 28;
    public static final int EDIT_INFO_ACTIVITY17 = 31;
    public static final int EDIT_INFO_ACTIVITY18 = 32;
    public static final int MY_STORE_ACTIVITY = 33;
    public static final int REMIND_ACTIVITY = 34;
    public static final int REMIND_ACTIVITY2 = 35;
    public static final int VIRTUAL_ACCOUNT_MANAGE_ACTIVITY = 36;
    public static final int VIRTUAL_ACCOUNT_MANAGE_ACTIVITY2 = 37;
    public static final int VIRTUAL_ACCOUNT_DEPOSIT_ACTIVITY = 38;
    public static final int VIRTUAL_ACCOUNT_DEPOSIT_ACTIVITY2 = 39;
    public static final int VIRTUAL_ACCOUNT_SUCCESS_ACTIVITY = 40;
    public static final int VIRTUAL_ACCOUNT_SUCCESS_ACTIVITY2 = 41;
    public static final int MASSAGE_STORE_ORDER_ACTIVITY = 42;
    public static final int MASSAGE_STORE_ORDER_ACTIVITY2 = 43;
    public static final int MASSAGE_STORE_ORDER_ACTIVITY3 = 44;
    public static final int MASSAGE_STORE_DETAIL_ACTIVITY = 45;
    public static final int MY_ORDER_ADAPTER = 46;
    public static final int MY_ORDER_ACTIVITY = 47;
    public static final int MY_ORDER_ACTIVITY2 = 48;

    public static String messgeActivityState = "";// 存储消息监听所在界面，用于判断红点是否显示

    public static final String VERSION = "version";
    public static String VERSION_NAME = "";
    public static String VERSION_CODE = "";
    public static final String COMMON_SP = "common_shared_preferences";


    /**
     * 推送
     */
    public static String PUSH_TAG_ADDFRIEND = "addFriend";
    public static String PUSH_TAG_SYSTEM = "system";
    public static String PUSH_TAG_AGREE_FRIEND = "agreeFriend";
    public static String PUSH_TAG_REFUSE_FRIEND = "refuseFriend";
    public static String PUSH_SOCIAL = "social";
    public static String PUSH_ASK = "askPrice";
    public static String PUSH_ORFER = "offerPrice";
    public static String ADD = "0";
    public static String AGREE = "1";
    public static String REFUSE = "2";
    public static String FRIEND_TITLE = "好友通知";
    public static String SYSTEM_TITLE = "系统通知";
    public static String SOCIAL_TITLE = "社交消息";

    public static final String PRODUCTFAVOR_INTENT_ACTION = "com.shenma.yueba.product_favor";//广播通知商品收藏
    public final static String IntentFilterChatAtConnect="com.shenma.yueba.ChatActivity.connect";//socketio已连接
    public final static String IntentFilterChatAtUnConnect="com.shenma.yueba.ChatActivity.unconnect";//socketio已断开
}
