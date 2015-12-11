package com.shenma.yueba.util;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import im.control.SocketManger;

import com.shenma.yueba.baijia.activity.MainActivityForBaiJia;
import com.shenma.yueba.baijia.modle.UserRequestBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.BindInter;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class WXLoginUtil {

	private Context ctx;
	private UMSocialService mController;
	private boolean hasCallBack;
	
	private boolean closeSelf;
	public WXLoginUtil(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * 初始化微信第三方登录
	 */
	public void initWeiChatLogin(boolean closeSelf,final boolean isLogin,boolean hasCallBack) {
		this.closeSelf = closeSelf;
		this.hasCallBack = hasCallBack;
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ctx, Constants.WX_APP_ID,
				Constants.WX_SECRET);
		wxHandler.addToSocialSDK();
		wxHandler.setRefreshTokenAvailable(false);
		/*
		 * mController.doOauthVerify(ctx, SHARE_MEDIA.WEIXIN, new
		 * UMAuthListener() {
		 * 
		 * @Override public void onError(SocializeException e, SHARE_MEDIA
		 * platform) { }
		 * 
		 * @Override public void onComplete(Bundle value, SHARE_MEDIA platform)
		 * { if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
		 * Toast.makeText(ctx, "授权成功.", Toast.LENGTH_SHORT).show(); } else {
		 * Toast.makeText(ctx, "授权失败", Toast.LENGTH_SHORT).show(); } }
		 * 
		 * @Override public void onCancel(SHARE_MEDIA platform) { }
		 * 
		 * @Override public void onStart(SHARE_MEDIA platform) { } });
		 */
		mController.doOauthVerify(ctx, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(ctx, "授权开始", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(ctx, "授权错误", Toast.LENGTH_SHORT).show();
						Log.i("wx", e.getMessage()+"-----"+e.getStackTrace());
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(ctx, "授权完成", Toast.LENGTH_SHORT).show();
						// 获取相关授权信息
						mController.getPlatformInfo(ctx, SHARE_MEDIA.WEIXIN,
								new UMDataListener() {
									@Override
									public void onStart() {
										//Toast.makeText(ctx, "获取平台数据开始...",Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											final StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											sb.append("{");
											for (String key : keys) {
												sb.append("'");
												sb.append(key);
												sb.append("'");
												sb.append(":");
												sb.append("'");
												sb.append(info.get(key)
														.toString());
												sb.append("'");
												sb.append(",");
											}
											sb.append("}");
											((Activity)ctx).runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if(isLogin){//微信登录
														wxLogin(sb);
														Log.i("wechat",sb.toString());
													}else{//微信绑定
														bindWeChat(sb);
													}
													
												}
											});
											//Toast.makeText(ctx, sb.toString(),1000).show();
											Log.d("TestData", sb.toString());
										} else {
											Log.d("TestData", "发生错误：" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(ctx, "授权取消", Toast.LENGTH_SHORT).show();
					}
				});
	}

	/**
	 * 微信登录
	 */
	private void wxLogin(StringBuilder sb) {
		final HttpControl httpcon = new HttpControl();
		httpcon.wxLongin(sb.toString(), new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				if (obj != null && obj instanceof UserRequestBean) {
					UserRequestBean bean = (UserRequestBean) obj;
					httpcon.setLoginInfo(ctx, bean);
					SocketManger.the().onLineToUserID();
					Intent intent = new Intent(ctx,
							MainActivityForBaiJia.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ctx.startActivity(intent);
					if(closeSelf){
						((Activity) ctx).finish();
					}
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

			}
		}, ctx, true);
	}
	
	/**
	 * 绑定微信
	 */
	private void bindWeChat(StringBuilder sb) {
		final HttpControl httpcon = new HttpControl();
		httpcon.bindWeChat(sb.toString(), new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				
				Toast.makeText(ctx, "绑定成功", Toast.LENGTH_SHORT).show();
				SharedUtil.setBooleanPerfernece(ctx, SharedUtil.user_IsBindWeiXin, true);
				if(hasCallBack){
					((BindInter)ctx).refresh();
				}
					if(closeSelf){
						((Activity) ctx).finish();
					}
				};

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

			}
		}, ctx, true);
	}
	
	
	
}
