package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * @author gyj
 * @version 创建时间：2015-7-6 下午6:55:11
 *          程序的简单说明  败家消息对象
 */

public class MsgListInfo implements Serializable {

    String Name = "";// 用户名字
    String Logo = "";//用户头像,
    int UnReadCount;//未读条数
    String UnReadMessage = "";//最后一条信息内容，如果没有则返回  "暂无消息"
    String UnReadLastTime = "";//最后一条时间   如果没有则返回当前时间
    String RoomId;//当前聊天的roomId
    int ToUserId;

    public int getToUserId() {
        return ToUserId;
    }

    public void setToUserId(int toUserId) {
        ToUserId = toUserId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public int getUnReadCount() {
        return UnReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        UnReadCount = unReadCount;
    }

    public String getUnReadMessage() {
        return UnReadMessage;
    }

    public void setUnReadMessage(String unReadMessage) {
        UnReadMessage = unReadMessage;
    }

    public String getUnReadLastTime() {
        return UnReadLastTime;
    }

    public void setUnReadLastTime(String unReadLastTime) {
        UnReadLastTime = unReadLastTime;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

}
