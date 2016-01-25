package im.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.form.MessageUserInfoBean;
import im.form.RequestMessageBean;

/**
 * Created by Administrator on 2016/1/20.
 * 聊天数据 存储管理
 */
public class ImDataBaseManager {
    static ImDataBaseManager imDataBaseManager;

    private ImDataBaseManager() {
    }

    public static ImDataBaseManager getInstance() {
        if (imDataBaseManager == null) {
            imDataBaseManager = new ImDataBaseManager();
        }
        return imDataBaseManager;
    }

    /*********
     * 向数据库中写入数据
     * @param requestMessageBean RequestMessageBean 消息数据
     ******/
    public void writeImMessageData(RequestMessageBean requestMessageBean) {
        if(true)
        {
            return;
        }
        if (requestMessageBean != null) {
            if (requestMessageBean != null) {
                DBHelper dBHelper = new DBHelper();
                UserDbManager userDbManager = new UserDbManager(dBHelper);
                int fromUserId = requestMessageBean.getFromUserId();
                String body = requestMessageBean.getBody();
                String chattype = requestMessageBean.getType();//信息类型 如 文本 图片 链接
                String createtime = requestMessageBean.getCreationDate();//创建时间
                String fromUserType = requestMessageBean.getFromUserType();//用户类型

                int messageType = requestMessageBean.getMessageType();//消息类型 0、私聊  1 群聊
                int productId = requestMessageBean.getProductId();//商品id
                String roomId = requestMessageBean.getRoomId();//房间号
                String sharelink = requestMessageBean.getSharelink();//分享链接
                int touserid = requestMessageBean.getToUserId();//接收方id
                String messageid = requestMessageBean.get_id();//接收方id
                String fromusername = "";
                String fromUserLogo="";
                MessageUserInfoBean messageUserInfoBean = requestMessageBean.getUser();
                if (messageUserInfoBean != null) {
                    fromUserLogo= messageUserInfoBean.getLogo();
                    fromusername = messageUserInfoBean.getNickName();
                }
                //从数据库中 查询是否存在 指定的 用户id 信息 如果没有则创建
                Map<String, String> map = new HashMap<String, String>();
                map.put(DBStaticInfo.IM_DBTABLE_FromUser_icon.trim(), fromUserLogo);
                map.put(DBStaticInfo.IM_DBTABLE_FromUser_Name.trim(), fromusername);
                map.put(DBStaticInfo.IM_DBTABLE_FromUserID.trim(), Integer.toString(fromUserId));
                map.put(DBStaticInfo.IM_DBTABLE_FromUserType.trim(), fromUserType);
                map.put(DBStaticInfo.IM_DBTABLE_Message_body.trim(), body);
                map.put(DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime.trim(), createtime);
                map.put(DBStaticInfo.IM_DBTABLE_Message_productID.trim(), Integer.toString(productId));
                map.put(DBStaticInfo.IM_DBTABLE_Message_shareUrl.trim(), sharelink);
                map.put(DBStaticInfo.IM_DBTABLE_Message_status.trim(), "sucess");
                map.put(DBStaticInfo.IM_DBTABLE_Chat_Type.trim(),chattype);
                map.put(DBStaticInfo.IM_DBTABLE_Message_Type.trim(), Integer.toString(messageType));
                map.put(DBStaticInfo.IM_DBTABLE_toUser_ID.trim(), Integer.toString(touserid));
                map.put(DBStaticInfo.IM_DBTABLE_toUser_icon.trim(), "");
                map.put(DBStaticInfo.IM_DBTABLE_toUser_Name.trim(), "");
                map.put(DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId.trim(), roomId);
                map.put(DBStaticInfo.IM_DBTABLE_MESSAGE_id.trim(), messageid);
                userDbManager.addMessageInfo(map);
            }
        }
    }

    /*********
     * 向数据库中读取数据
     * @param curpage int 当前页
     * @param pagesize int 每页显示的个数
     * @param roomid String 房间号
     ******/
    public List<RequestMessageBean> readImMessageAllData(int curpage,int pagesize,String roomid) {
        if(true)
        {
            return null;
        }
        DBHelper dBHelper = new DBHelper();
        UserDbManager userDbManager = new UserDbManager(dBHelper);
        List<RequestMessageBean> list =userDbManager.queryMessageById(roomid, curpage, pagesize);
        return list;
    }
}
