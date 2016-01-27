package com.shenma.yueba.util;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaiJiaOrderListActivity;
import com.shenma.yueba.baijia.activity.MyDefaultCircleActivity;
import com.shenma.yueba.yangjia.activity.EarningManagerActivity;
import com.shenma.yueba.yangjia.activity.HuoKuanManagerActivity;
import com.shenma.yueba.yangjia.activity.MainActivityForYangJia;
import com.shenma.yueba.yangjia.activity.SalesManagerForBuyerActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;


/**
 * notificaion工具类
 * 
 * @author Administrator
 * 
 */
public class NotificationUtils {
	@SuppressLint("NewApi")
	public static void showNotification(Context ctx, String... text) {
		// 创建一个NotificationManager的引用
		NotificationManager notificationManager = (NotificationManager) ctx
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.icon, text[1],
				System.currentTimeMillis());
		// FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
		// FLAG_NO_CLEAR 该通知不能被状态栏的清除按钮给清除掉
		// FLAG_ONGOING_EVENT 通知放置在正在运行
		// FLAG_INSISTENT 是否一直进行，比如音乐一直播放，知道用户响应
		// notification.flags |= Notification.FLAG_ONGOING_EVENT; //
		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中,加上该属性就不可以滑动删除了
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// DEFAULT_ALL 使用所有默认值，比如声音，震动，闪屏等等
		// DEFAULT_LIGHTS 使用默认闪光提示
		// DEFAULT_SOUNDS 使用默认提示声音
		// DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission
		// android:name="android.permission.VIBRATE" />权限
		// 叠加效果常量
		notification.defaults = Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 60000; // 闪光时间，毫秒
		if (text.length >= 2) {
			// 设置通知的事件消息
			CharSequence type = text[0]; // 通知类型
			CharSequence title = text[1]; // 通知标题
			CharSequence content = text[2]; // 通知内容
			CharSequence roomid = text[3]; // 房间id
			CharSequence toUser_id = text[4]; //发送者id
			CharSequence username = text[5]; //方式这名称
			Intent notificationIntent = null;
			if ("2".equals(type)) {// 认证通过
				SharedUtil.setAuditStatus(ctx, "1");
				notificationIntent = new Intent(ctx,
						MainActivityForYangJia.class); // 点击该通知后要跳转的Activity
			}
			if ("3".equals(type)) {// 认证被拒绝
				SharedUtil.setAuditStatus(ctx, "-2");
			}
			if ("4".equals(type)) {// 用户支付-推送给买手
				notificationIntent = new Intent(ctx,
						SalesManagerForBuyerActivity.class); // 点击该通知后要跳转的Activity
//				notificationIntent.putExtra("index", 2);
			}
//			if ("5".equals(type)) {// 用户支付--推送给用户
//				notificationIntent = new Intent(ctx,
//						BaiJiaOrderListActivity.class); // 点击该通知后要跳转的Activity
//				notificationIntent.putExtra("CURRID", 2);//专柜自提
//			}
			if ("6".equals(type)) {// 用户申请退款-推送给买手
				notificationIntent = new Intent(ctx,
						SalesManagerForBuyerActivity.class); // 点击该通知后要跳转的Activity
//				notificationIntent.putExtra("index", 3);
			}

//			if ("7".equals(type)) {// 用户申请退款--推送给用户
//				notificationIntent = new Intent(ctx,
//						BaiJiaOrderListActivity.class); // 点击该通知后要跳转的Activity
//				notificationIntent.putExtra("CURRID", 3);//售后
//			}

			if ("8".equals(type)) {// 用户收货--推送给买手
				MyApplication.getInstance().getIndexRefreshService().refreshList();
				notificationIntent = new Intent(ctx,
						SalesManagerForBuyerActivity.class); // 点击该通知后要跳转的Activity
			}

//			if ("9".equals(type)) {// 用户收货，推送给用户
//				notificationIntent = new Intent(ctx,
//						BaiJiaOrderListActivity.class); // 点击该通知后要跳转的Activity
//				notificationIntent.putExtra("CURRID", 0);//全部订单
//			}

			if ("10".equals(type)) {// 提现货款 ,
				notificationIntent = new Intent(ctx,
						HuoKuanManagerActivity.class); // 点击该通知后要跳转的Activity
			}

			if ("11".equals(type)) {// 提现收益
				notificationIntent = new Intent(ctx,
						EarningManagerActivity.class); // 点击该通知后要跳转的Activity
			}
			if ("12".equals(type)) {// 买手同意退货,
				notificationIntent = new Intent(ctx,BaiJiaOrderListActivity.class); // 点击该通知后要跳转的Activity
				notificationIntent.putExtra("CURRID", 0);//全部订单
				}
			if ("13".equals(type)) {// 买手不同意退货
				notificationIntent = new Intent(ctx,BaiJiaOrderListActivity.class); // 点击该通知后要跳转的Activity
				notificationIntent.putExtra("CURRID", 3);//售后
			}
			if("14".equals(type))//未读消息
			{
			
				if(!ToolsUtil.isApplicationBroughtToBackground(MyApplication.getInstance().getPackageName().toString()+".ChatActivity"))
				{
					MyApplication.getInstance().finishActivity(MyDefaultCircleActivity.class);
				}
				notificationIntent = new Intent(ctx,MyDefaultCircleActivity.class); // 点击该通知后要跳转的Activity
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				notificationIntent.putExtra("chatType", roomid.toString());
				notificationIntent.putExtra("Chat_NAME", username.toString());
				notificationIntent.putExtra("toUser_id", Integer.valueOf(toUser_id.toString()));
			}

			if(notificationIntent!=null){
				PendingIntent contentItent = PendingIntent.getActivity(ctx, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				notification.setLatestEventInfo(ctx, title, content, contentItent);
				// 把Notification传递给NotificationManager
				notificationManager.notify(0, notification);
			}
		}
	}
}
