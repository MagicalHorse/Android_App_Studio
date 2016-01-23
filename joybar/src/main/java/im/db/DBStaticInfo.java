package im.db;

/**
 * Created by Administrator on 2016/1/20.
 */
public class DBStaticInfo {
    public final static String DBName = "imbuyer";
    public final static int DB_VERSION = 1;
    public final static String field_varchar20=" varchar(20) ";
    public final static String field_varchar100=" varchar(100) ";
    public final static String field_int=" int ";
    public final static String key_ID = " id ";//消息id
    /***********************
     * 聊天信息字段
     ******************************************/
    public final static String IM_DBTABLE = " im_messagetable ";
    public final static String IM_DBTABLE_MESSAGE_RoomId = " roomId ";//房间id
    public final static String IM_DBTABLE_MESSAGE_CREATETime = " messagecreatetime ";//消息创建时间
    public final static String IM_DBTABLE_MESSAGE_id = " messageid ";//消息id
    public final static String IM_DBTABLE_FromUserID = " fromuser_id ";//发送者id
    public final static String IM_DBTABLE_FromUser_Name = " fromuser_name ";//发送者用户名称
    public final static String IM_DBTABLE_FromUser_icon = " fromuser_icon ";//发送者用户头像
    public final static String IM_DBTABLE_FromUserType = " fromUserType ";//用户类型
    public final static String IM_DBTABLE_Chat_Type = " type ";//信息类型（如 文字  图片  链接 等）
    public final static String IM_DBTABLE_Message_Type = " messageType ";//信息类型 0、私聊  1 群聊）
    public final static String IM_DBTABLE_Message_status = " status ";//消息状态（如 发送成功，发送失败）
    public final static String IM_DBTABLE_Message_body = " body ";//消息内容
    public final static String IM_DBTABLE_Message_productID = " product_id ";//商品id(如果是商品链接 则包含商品id)
    public final static String IM_DBTABLE_Message_shareUrl = " shareUrl ";//分享链接
    public final static String IM_DBTABLE_toUser_ID = " touser_id ";//接收者id（可能多个 以，号区分
    public final static String IM_DBTABLE_toUser_Name = " touser_name ";//接受者用户名称
    public final static String IM_DBTABLE_toUser_icon = " touser_icon ";//接受者用户头像


}
