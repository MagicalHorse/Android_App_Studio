package im.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.shenma.yueba.baijia.modle.MsgListInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import im.form.MessageUserInfoBean;
import im.form.RequestMessageBean;

/**
 * Created by Administrator on 2016/1/21.
 * 用户表的 添加 删除 修改
 */
public class UserDbManager {
    DBHelper dBHelper;

    public UserDbManager(DBHelper dBHelper) {
        this.dBHelper = dBHelper;
    }

    /********
     * 根据房间id 读取消息表中的数据
     *
     * @param roomid String 房间id
     * @param curpage int
     * @param pagesize int
     *****/
    public List<RequestMessageBean> queryMessageById(String roomid,int curpage,int pagesize) {
        List<RequestMessageBean> list = new ArrayList<RequestMessageBean>();
        SQLiteDatabase sqLiteDatabase = dBHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        String sql = "select * from " + DBStaticInfo.IM_DBTABLE + " where " + DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId + "='"+roomid+"'  order by " + DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime+ "desc  limit "+((curpage-1)*pagesize)+" , "+(curpage*pagesize);
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    RequestMessageBean requestMessageBean = new RequestMessageBean();
                    int _id = cursor.getColumnIndex(DBStaticInfo.key_ID.trim());
                    int createtime = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime.trim());
                    int FromUserID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUserID.trim());
                    int FromUser_Name = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUser_Name.trim());
                    int FromUser_icon = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUser_icon.trim());
                    int Chat_Type = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Chat_Type.trim());
                    int Message_Type = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_Type.trim());
                    int Message_body = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_body.trim());
                    int FromUserType = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUserType.trim());
                    int Message_productID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_productID.trim());
                    int Message_shareUrl = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_shareUrl.trim());
                    int toUser_ID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_toUser_ID.trim());
                    int RoomId = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId.trim());
                    int status = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_status.trim());
                    int MESSAGE_id = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_id.trim());

                    int currid = cursor.getInt(_id);
                    String str_createtime = cursor.getString(createtime);
                    String str_FromUserID = cursor.getString(FromUserID);
                    String str_FromUser_Name = cursor.getString(FromUser_Name);
                    String str_FromUser_icon = cursor.getString(FromUser_icon);
                    String str_FromUserType = cursor.getString(FromUserType);
                    String str_Chat_Type = cursor.getString(Chat_Type);
                    int str_Message_Type = cursor.getInt(Message_Type);
                    String str_Message_body = cursor.getString(Message_body);
                    String str_Message_productID = cursor.getString(Message_productID);
                    String str_Message_shareUrl = cursor.getString(Message_shareUrl);
                    String str_toUser_ID = cursor.getString(toUser_ID);
                    String str_RoomId = cursor.getString(RoomId);
                    String str_status = cursor.getString(status);
                    String str_MESSAGE_id = cursor.getString(MESSAGE_id);


                    requestMessageBean.setBody(str_Message_body);
                    requestMessageBean.setFromUserId(Integer.parseInt(str_FromUserID));
                    requestMessageBean.setToUserId(Integer.parseInt(str_toUser_ID));
                    requestMessageBean.setCreationDate(str_createtime);
                    requestMessageBean.setFromUserType(str_FromUserType);
                    requestMessageBean.setLogo(str_FromUser_icon);
                    requestMessageBean.setUserName(str_FromUser_Name);
                    requestMessageBean.setProductId(Integer.parseInt(str_Message_productID));
                    requestMessageBean.setRoomId(str_RoomId);
                    requestMessageBean.setSharelink(str_Message_shareUrl);
                    requestMessageBean.setMessageType(str_Message_Type);
                    requestMessageBean.setType(str_Chat_Type);
                    requestMessageBean.set_id(str_MESSAGE_id);
                    MessageUserInfoBean user = new MessageUserInfoBean();
                    user.setLogo(str_FromUser_icon);
                    user.setNickName(str_FromUser_Name);
                    user.setUserId(Integer.parseInt(str_toUser_ID));
                    requestMessageBean.setUser(user);

                    list.add(requestMessageBean);
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.endTransaction();
        }
        return list;
    }




    /********
     * 获取数据 根据roodId 分组  去重
     *****/
    public List<MsgListInfo> queryMessageGroup() {
        List<MsgListInfo> list = new ArrayList<MsgListInfo>();
        SQLiteDatabase sqLiteDatabase = dBHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        String sql = "select * from "+DBStaticInfo.IM_DBTABLE+"  group by "+DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId+" order by "+DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime+" desc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MsgListInfo msgListInfo = new MsgListInfo();
                    int _id = cursor.getColumnIndex(DBStaticInfo.key_ID.trim());
                    int createtime = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_CREATETime.trim());
                    int FromUserID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUserID.trim());
                    int FromUser_Name = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUser_Name.trim());
                    int FromUser_icon = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUser_icon.trim());
                    int Chat_Type = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Chat_Type.trim());
                    int Message_Type = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_Type.trim());
                    int Message_body = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_body.trim());
                    int FromUserType = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_FromUserType.trim());
                    int Message_productID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_productID.trim());
                    int Message_shareUrl = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_shareUrl.trim());
                    int toUser_ID = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_toUser_ID.trim());
                    int RoomId = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_RoomId.trim());
                    int status = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_Message_status.trim());
                    int MESSAGE_id = cursor.getColumnIndex(DBStaticInfo.IM_DBTABLE_MESSAGE_id.trim());

                    int currid = cursor.getInt(_id);
                    String str_createtime = cursor.getString(createtime);
                    String str_FromUserID = cursor.getString(FromUserID);
                    String str_FromUser_Name = cursor.getString(FromUser_Name);
                    String str_FromUser_icon = cursor.getString(FromUser_icon);
                    String str_FromUserType = cursor.getString(FromUserType);
                    String str_Chat_Type = cursor.getString(Chat_Type);
                    int str_Message_Type = cursor.getInt(Message_Type);
                    String str_Message_body = cursor.getString(Message_body);
                    String str_Message_productID = cursor.getString(Message_productID);
                    String str_Message_shareUrl = cursor.getString(Message_shareUrl);
                    String str_toUser_ID = cursor.getString(toUser_ID);
                    String str_RoomId = cursor.getString(RoomId);
                    String str_status = cursor.getString(status);
                    String str_MESSAGE_id = cursor.getString(MESSAGE_id);


                    msgListInfo.setLogo(str_FromUser_icon);
                    msgListInfo.setId(str_MESSAGE_id);
                    msgListInfo.setName(str_FromUser_Name);
                    msgListInfo.setRoomId(str_RoomId);
                    msgListInfo.setRoomType(str_Message_Type);// 消息的类型      0 私聊消息    1群聊/圈子消息    2 系统消息
                    msgListInfo.setUnReadCount(0);
                    msgListInfo.setUnReadLastTime(str_createtime);
                    msgListInfo.setUnReadMessage(str_Message_body);

                    list.add(msgListInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqLiteDatabase.endTransaction();
        }
        return list;
    }


    /********
     * 向聊天信息表中添加信息
     *
     * @return boolean true 成功 false 失败
     *****/
    public boolean addMessageInfo(Map<String, String> map) {
        boolean b = false;
        SQLiteDatabase sqLiteDatabase = dBHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        if (map != null) {
            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                contentValues.put(key, value);
            }
        }
        long i = sqLiteDatabase.insert(DBStaticInfo.IM_DBTABLE.trim(), null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        if (i > 0) {
            b = true;
        }
        return b;
    }


    /********
     * 更新聊天信息表中的信息
     *
     * @param map Map<String, Object> 修改的k-v
     * @param id  int 消息id
     * @return boolean true 成功  false 失败
     *****/
    public boolean updateMessageInfo(int id, Map<String, Object> map) {
        boolean b = false;
        SQLiteDatabase sqLiteDatabase = dBHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        if (map != null) {
            Set<String> set = map.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = map.get(key);
                if (value instanceof Integer) {
                    contentValues.put(key, (Integer) value);
                } else if (value instanceof String) {
                    contentValues.put(key, (String) value);
                }
            }
        }

        String whereClause = DBStaticInfo.key_ID + "=" + id;
        String[] selectionArgs = null;
        int i = sqLiteDatabase.update(DBStaticInfo.IM_DBTABLE.trim(), contentValues, whereClause, selectionArgs);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        if (i > 0) {
            b = true;
        }
        return b;
    }
}
