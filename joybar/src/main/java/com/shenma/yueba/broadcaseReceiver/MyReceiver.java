package com.shenma.yueba.broadcaseReceiver;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.JsonObject;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.NotificationUtils;
import com.shenma.yueba.util.SharedUtil;

public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private String type;//推送的消息类型
	private String roomid;//消息推送 房间号
	private String fromuserid;//发送人id
	private String username;//放松人名称
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){//自定义消息
			Bundle bundle = intent.getExtras();
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String title = bundle.getString(JPushInterface.EXTRA_TITLE);
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject jsonObject = new JSONObject(extra);
				Log.i("TAG", "jUSH MyReceiver:onReceive :"+jsonObject.toString());
				type = jsonObject.getString("type");
				if(jsonObject.has("roomid"))
				{
					roomid=jsonObject.getString("roomid");
				}
				if(jsonObject.has("fromuserid"))
				{
					fromuserid=jsonObject.getString("fromuserid");
				}
				if(jsonObject.has("username"))
				{
					username=jsonObject.getString("username");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
				NotificationUtils.showNotification(context,type,title,message,roomid,fromuserid,username);
		}
		if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){//系统通知
			
		}
	
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}


}