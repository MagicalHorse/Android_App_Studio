package im.form;

import android.content.Context;

import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.io.Serializable;

import im.control.ChatBaseManager;

/**
 * @author gyj
 * @version 创建时间：2015-7-3 下午3:56:30
 *          程序的简单说明  基础信息类
 */

public abstract class BaseChatBean extends MessageBean implements Serializable {
    transient Context context;

    /*****
     * @param chattype ChatType 发送消息的文本类型(根据此类型 设置 视图的样式)
     * @param type     String （消息的类型  即 发送消息时 type 对应的信息）
     ***/
    public BaseChatBean(ChatType chattype, String type, Context context) {
        this.chattype = chattype;
        this.type = type;
        this.context = context;
    }

    /***
     * 发送消息的文本类型
     **/
    public enum ChatType {
        link_type,//链接信息
        pic_type,//图片信息
        text_trype,//文本信息
        notice_type//推广信息
    }


    public enum SendStatus {
        send_unsend,//未发送
        send_sucess,//发送成功
        send_loading,//发送中
        send_fails//发送失败
    }

    String room_No = "";//房间号
    transient boolean isoneself = false;//是否是自己发送的信息

    transient ChatBaseManager baseManager;
    transient ChatType chattype;//信息类型
    transient SendStatus sendStatus = SendStatus.send_unsend;

    /******
     * 发送数据
     ***/
    public abstract void sendData();

    /***
     * 赋值 讲 接收到的消息数据 进行赋值
     **/
    public abstract void setValue(Object bean);

    /*****
     * 接受到 信息的统一赋值
     ***/
    public void setParentView(Object obj) {
        if (obj != null && obj instanceof RequestMessageBean) {
            RequestMessageBean bean = (RequestMessageBean) obj;
            fromUserId = bean.getFromUserId();
            toUserId = bean.getToUserId();
            room_No = bean.getRoomId();
            messageType = bean.getMessageType();
            type = bean.getType();
            _id=bean.get_id();
            if(bean.getUser()!=null)
            {
                userName = ToolsUtil.nullToString(bean.getUser().getNickName());
                logo = ToolsUtil.nullToString(bean.getUser().getLogo());
                creationDate = ToolsUtil.nullToString(bean.getUser().getCreationDate());
            }

            productId = bean.getProductId();
            body = bean.getBody();
            String user_id = SharedUtil.getStringPerfernece(MyApplication.getInstance().getApplicationContext(), SharedUtil.user_id);
            if (user_id.equals(Integer.toString(fromUserId))) {
                isoneself = true;
            } else {
                isoneself = false;
            }
            sendStatus = SendStatus.send_sucess;
        }
    }


    public ChatType getChattype() {
        return chattype;
    }

    public void setChattype(ChatType chattype) {
        this.chattype = chattype;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public boolean isoneself() {
        return isoneself;
    }

    public void setIsoneself(boolean isoneself) {
        this.isoneself = isoneself;
    }

    public String getRoom_No() {
        return room_No;
    }

    public void setRoom_No(String room_No) {
        this.room_No = room_No;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public ChatBaseManager getBaseManager() {
        return baseManager;
    }

    public void setBaseManager(ChatBaseManager baseManager) {
        this.baseManager = baseManager;
    }


    /*****
     * 获取 传递消息的对象
     ****/
    public MessageBean getMessageBean() {
        MessageBean bean = new MessageBean();
        bean.setRoomId(room_No);
        bean.setBody(body);
        bean.setFromUserId(fromUserId);
        bean.setProductId(productId);
        bean.setToUserId(toUserId);
        bean.setMessageType(messageType);
        bean.setType(type);
        bean.setUserName(userName);
        bean.setLogo(logo);
        bean.setSharelink(body);
        return bean;
    }
}
