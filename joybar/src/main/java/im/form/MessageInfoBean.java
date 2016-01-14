package im.form;

import com.shenma.yueba.baijia.modle.UserBuyerBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MessageInfoBean implements Serializable {
    int __v;// 0
    String id;//"568e1295a2f3319a677c2f65"
    String body;// "asdf"
    String creationDate;//"2016-01-07T07:24:05.895Z"
    int fromUserId;//30944
    boolean isRead;// 0
    int messageType;//0
    String roomId;//"845_30944"
    int toUserId;// 845
    MessageUserInfoBean user=new MessageUserInfoBean();

    public MessageUserInfoBean getUser() {
        return user;
    }

    public void setUser(MessageUserInfoBean user) {
        this.user = user;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }
}
