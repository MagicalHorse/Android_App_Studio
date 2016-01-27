package com.shenma.yueba.baijia.modle;

import java.io.Serializable;

/**
 * @author gyj
 * @version 创建时间：2015-7-6 下午6:55:11
 *          程序的简单说明  败家消息对象
 */

public class MsgListInfo implements Serializable {

    String id;//消息id
    int ToUserId;//
    String Name;// 用户名字
    String UpdateTime;// 更新时间，
    String Logo;//用户头像,
    int UnReadCount;//未读条数
    String UnReadMessage;//最后一条信息内容，如果没有则返回  "暂无消息"
    String UnReadLastTime;//最后一条时间   如果没有则返回当前时间
    String RoomId;//：当前聊天的roomId
    int RoomType;// 消息的类型      0 私聊消息    1群聊/圈子消息    2 系统消息
    public int getRoomType() {
        return RoomType;
    }

    public void setRoomType(int roomType) {
        RoomType = roomType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
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
