package com.shenma.yueba.baijia.activity;

import im.control.SocketManger;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.shenma.yueba.R;
import com.shenma.yueba.UpdateManager;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.CheckVersionBackBean;
import com.shenma.yueba.baijia.modle.ModifyLogoBackBean;
import com.shenma.yueba.baijia.modle.ModifyLogoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.BindInter;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.FileUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.JpushUtils;
import com.shenma.yueba.util.PhotoUtils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.util.WXLoginUtil;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.view.SelectePhotoType;
import com.shenma.yueba.yangjia.activity.AboutActivity;
import com.shenma.yueba.yangjia.activity.ModifyNickNameActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.OauthHelper;


/*****
 * 用户设置
 * 
 * @author gyj @ date 2015-05-05 用户设置页面 用于查看用户头像 用户昵称 用户地址 用户收藏 账户密码 消息免打扰 以及
 *         切换到我要养家 退出登录等
 * 
 * *****/
public class UserConfigActivity extends BaseActivityWithTopView implements
		OnClickListener ,BindInter{

	// 头像图片
	private String littlePicPath;// 小图路径
	private String littlePicPath_cache;// 裁剪后图片存储的路径
	private CustomProgressDialog progressDialog;
	private TextView tv_head;
	private RoundImageView people_config_str2_imageview;
	private TextView tv_nickname_title;
	private TextView tv_nickname_value;
	private TextView rv_rename_password;
	private TextView tv_message_setting;
	private ImageView switchButton;
	private TextView tv_bind_phone_title;
	private TextView tv_bind_phone_value;
	private TextView tv_bind_wechat_title;
	private TextView tv_bind_wechat_value;
	private TextView tv_about;
	private Button user_config_exit_button;
	private TextView tv_check_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		super.onCreate(savedInstanceState);
		initView();
	}

	/****
	 * 初始化 视图控件 并设置 按键监听
	 * 
	 * **/
	void initView() {
		progressDialog = new CustomProgressDialog(mContext).createDialog(mContext);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		setTitle("设置");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserConfigActivity.this.finish();
			}
		});
		tv_head = getView(R.id.tv_head);
		people_config_str2_imageview = getView(R.id.people_config_str2_imageview);

		tv_check_update = getView(R.id.tv_check_update);
		tv_check_update.setOnClickListener(this);
		tv_nickname_title = getView(R.id.tv_nickname_title);
		tv_nickname_value = getView(R.id.tv_nickname_value);
		rv_rename_password = getView(R.id.rv_rename_password);

		tv_message_setting = getView(R.id.tv_message_setting);
		switchButton = getView(R.id.switchButton);
		if (SharedUtil.getBooleanPerfernece(mContext, SharedUtil.user_canPush)) {// 打开的
			switchButton.setBackgroundResource(R.drawable.on);
		} else {
			switchButton.setBackgroundResource(R.drawable.off);
		}
		switchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SharedUtil.getBooleanPerfernece(mContext,
						SharedUtil.user_canPush)) {// 打开的
					changePushState("0");
				} else {
					changePushState("1");
				}
			}
		});
		tv_bind_phone_title = getView(R.id.tv_bind_phone_title);
		tv_bind_phone_value = getView(R.id.tv_bind_phone_value);
		tv_bind_wechat_title = getView(R.id.tv_bind_wechat_title);
		tv_bind_wechat_value = getView(R.id.tv_bind_wechat_value);

		tv_about = getView(R.id.tv_about);

		RelativeLayout rl_head = getView(R.id.rl_head);
		RelativeLayout rl_nickName = getView(R.id.rl_nickName);
		RelativeLayout rl_rename_password = getView(R.id.rl_rename_password);
		RelativeLayout rl_bind_phone = getView(R.id.rl_bind_phone);
		RelativeLayout rl_bind_wechat = getView(R.id.rl_bind_wechat);

		rl_head.setOnClickListener(this);
		rl_nickName.setOnClickListener(this);
		rl_rename_password.setOnClickListener(this);
		rl_bind_phone.setOnClickListener(this);
		rl_bind_wechat.setOnClickListener(this);
		tv_about.setOnClickListener(this);

		user_config_exit_button = getView(R.id.user_config_exit_button);
		user_config_exit_button.setOnClickListener(this);
		// 设置字体样式
		FontManager.changeFonts(this, tv_top_title, tv_nickname_title,
				tv_nickname_value, rv_rename_password, tv_message_setting,
				tv_bind_phone_title, tv_bind_phone_value, tv_bind_wechat_title,
				tv_bind_wechat_value, user_config_exit_button, tv_about,tv_head,tv_check_update);
	}

	/**
	 * 弹出底部菜单(相机和图库)
	 */
	protected void showBottomDialog() {
		ToolsUtil.hideSoftInputKeyBoard(UserConfigActivity.this);
		ShowMenu showMenu = new ShowMenu(mContext, findViewById(R.id.parent),
				R.layout.camera_pic_popwindow);
		showMenu.createView();
	}

	/**
	 * 弹出底部菜单
	 * 
	 * @author
	 */
	class ShowMenu extends SelectePhotoType {
		public ShowMenu(Activity activity, View parent, int popLayout) {
			super(activity, parent, popLayout);
		}

		@Override
		public void onExitClick(View v) {
			canceView();
		}

		@Override
		public void onCamera(View v) {
			if (ToolsUtil.isAvailableSpace(mContext)) {
				littlePicPath = PhotoUtils.takePicture(mContext);
			}
			canceView();
		}

		@Override
		public void onPic(View v) {
			if (ToolsUtil.isAvailableSpace(mContext)) {
				PhotoUtils.selectPhoto(mContext);
			}
			canceView();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_ALBUM) {
			// 调用相册返回
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						littlePicPath = cursor.getString(column_index);
						// 裁剪之后存储的路径
						littlePicPath_cache = Environment
								.getExternalStorageDirectory().toString()
								+ File.separator
								+ UUID.randomUUID().toString()
								+ "littlePic.jpg";
						// 裁剪图片
						startActivityForResult(PhotoUtils.getZoomIntent(Uri
								.fromFile(new File(littlePicPath)), Uri
								.fromFile(FileUtils
										.createNewFile(littlePicPath_cache))),
								PhotoUtils.INTENT_REQUEST_CODE_CROP);
					}
				}
			}
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CROP) {// 裁剪后返回
			if (resultCode == RESULT_OK) {
				modifyUserLogo();

			}
		}
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CAMERA) {// 调用相机返回
			if (resultCode == RESULT_OK) {
				if (littlePicPath != null) {
					// 裁剪之后存储的路径
					littlePicPath_cache = Environment
							.getExternalStorageDirectory().toString()
							+ File.separator
							+ UUID.randomUUID().toString()
							+ "littlePic.jpg";
					// 裁剪图片
					startActivityForResult(PhotoUtils.getZoomIntent(Uri
							.fromFile(new File(littlePicPath)), Uri
							.fromFile(FileUtils
									.createNewFile(littlePicPath_cache))),
							PhotoUtils.INTENT_REQUEST_CODE_CROP);
				}
			}
		}
		if (requestCode == Constants.REQUESTCODE
				&& resultCode == Constants.RESULTCODE) {// 修改昵称返回
			// String newName = data.getStringExtra("newName");
			// tv_nickname_value.setText(ToolsUtil.nullToString(newName));
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 修改头像
	 */
	private void modifyUserLogo() {
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		final HttpControl httpControl = new HttpControl();
		httpControl.syncUpload(littlePicPath_cache, new SaveCallback() {

			@Override
			public void onProgress(String arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String arg0, OSSException arg1) {
				if (progressDialog!=null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

			@Override
			public void onSuccess(String arg0) {
				runOnUiThread(new Runnable() {
					public void run() {
						httpControl.modifyUserLogo(
								littlePicPath_cache.substring(
										littlePicPath_cache.lastIndexOf("/") + 1,
										littlePicPath_cache.length()), false,
								false, new HttpCallBackInterface() {
									@Override
									public void http_Success(Object obj) {
										if (progressDialog.isShowing()) {
											progressDialog.dismiss();
										}
										Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT)
												.show();
										Bitmap bm = BitmapFactory
												.decodeFile(littlePicPath_cache);
										people_config_str2_imageview
												.setImageBitmap(bm);
										ModifyLogoBackBean bean = (ModifyLogoBackBean) obj;
										ModifyLogoBean logoBean = bean.getData();
										if(logoBean!=null &&logoBean.getLogo()!=null){
											SharedUtil.setHeadImage(mContext, logoBean.getLogo());
										}
									}

									@Override
									public void http_Fails(int error, String msg) {
										Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
												.show();

									}
								}, UserConfigActivity.this);

					}
				});
			}
		});

	}

	@Override
	protected void onResume() {
		String iconUrl = SharedUtil.getStringPerfernece(UserConfigActivity.this, SharedUtil.user_logo);
		if(!TextUtils.isEmpty(iconUrl)){
			MyApplication.getInstance().getBitmapUtil().display(people_config_str2_imageview, iconUrl);
		}
		tv_nickname_value.setText(SharedUtil.getStringPerfernece(mContext,
				SharedUtil.user_names));
		tv_bind_phone_value.setText(SharedUtil.getBooleanPerfernece(mContext,
				SharedUtil.user_IsBindMobile) ? "已绑定" : "未绑定");
		tv_bind_wechat_value.setText(SharedUtil.getBooleanPerfernece(mContext,
				SharedUtil.user_IsBindWeiXin) ? "已绑定" : "未绑定");
		if(SharedUtil.getBooleanPerfernece(mContext, SharedUtil.user_canPush)){
			switchButton.setBackgroundResource(R.drawable.on);
		}else{
			switchButton.setBackgroundResource(R.drawable.off);
		}
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_check_update://检查更新
			checkVersion();
			break;
		case R.id.rl_head:// 修改头像
			showBottomDialog();
			break;
		case R.id.rl_nickName:// 修改昵称
			Intent intent = new Intent(mContext, ModifyNickNameActivity.class);
			startActivityForResult(intent, Constants.REQUESTCODE);
			break;
		case R.id.rl_rename_password:// 修改密码
			Intent intentModifyPassword = new Intent(UserConfigActivity.this,
					SetNewPasswordActivity.class);
			startActivity(intentModifyPassword);
			break;
		case R.id.rl_bind_phone:// 绑定手机号
			if (SharedUtil.getBooleanPerfernece(mContext,
					SharedUtil.user_IsBindMobile)) {
				Toast.makeText(mContext, "手机号已绑定", Toast.LENGTH_SHORT).show();
			} else {
				// 绑定手机号
				Intent intentBindPhone = new Intent(UserConfigActivity.this,
						FindPasswordActivity.class);
				intentBindPhone.putExtra("from", "bindPhone");
				startActivity(intentBindPhone);
			}
			break;
		case R.id.rl_bind_wechat:// 绑定微信
			if (SharedUtil.getBooleanPerfernece(mContext,
					SharedUtil.user_IsBindWeiXin)) {
				Toast.makeText(mContext, "微信号已绑定", Toast.LENGTH_SHORT).show();
			} else {
				// 绑定微信
				WXLoginUtil wxLoginUtil = new WXLoginUtil(UserConfigActivity.this);
				wxLoginUtil.initWeiChatLogin(false,false,true);
			}
			break;
		case R.id.user_config_exit_button:// 退出登录
			OauthHelper.remove(mContext, SHARE_MEDIA.WEIXIN);
			MyApplication.removeAllActivity();
			Intent intentLogin = new Intent(mContext, SplashActivity.class);
			HttpControl httpControl=new HttpControl();
			httpControl.setUnLoginInfo(UserConfigActivity.this);
			JpushUtils jpushUtils = new JpushUtils(mContext);
			jpushUtils.setAlias("");//取消别名设置
			SocketManger.the().disContentSocket();
			startActivity(intentLogin);
			break;
		case R.id.tv_about:// 关于我们
			Intent intentAbout = new Intent(mContext, AboutActivity.class);
			startActivity(intentAbout);
		default:
			break;
		}

	}

	/**
	 * 修改推送状态
	 * 
	 * @param state
	 */
	private void changePushState(final String state) {
		HttpControl httpControl = new HttpControl();
		httpControl.changePushState(state, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				if ("0".equals(state)) {// 关闭
					SharedUtil.setBooleanPerfernece(mContext,
							SharedUtil.user_canPush, false);
					switchButton.setBackgroundResource(R.drawable.off);
					Toast.makeText(mContext, "已关闭", Toast.LENGTH_SHORT).show();
				} else {
					SharedUtil.setBooleanPerfernece(mContext,
							SharedUtil.user_canPush, true);
					switchButton.setBackgroundResource(R.drawable.on);
					Toast.makeText(mContext, "已开启", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				// TODO Auto-generated method stub

			}
		}, mContext, true);
	}
	
	
	
	private void checkVersion() {
		HttpControl httpControl = new HttpControl();
		httpControl.checkVersion(new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				CheckVersionBackBean bean = (CheckVersionBackBean) obj;
				if(bean.getData()!=null){
					String versionRemote = bean.getData().getVersion();
					String localVersionStr = ToolsUtil.getVersionName(UserConfigActivity.this);
					if(!versionRemote.equals(localVersionStr)){
						UpdateManager manager = new UpdateManager(UserConfigActivity.this, versionRemote+"", bean.getData().getUrl(), bean.getData().getTitle(), bean.getData().getDetails());
						manager.startUpdate();
					}else{
						Toast.makeText(mContext, "当前是最新版本", Toast.LENGTH_SHORT).show();
					}
				}
				
				

			}

			@Override
			public void http_Fails(int error, String msg) {
				// TODO Auto-generated method stub

			}
		}, UserConfigActivity.this);

	}

	@Override
	public void refresh() {
		tv_bind_phone_value.setText(SharedUtil.getBooleanPerfernece(mContext,
				SharedUtil.user_IsBindMobile) ? "已绑定" : "未绑定");
		tv_bind_wechat_value.setText(SharedUtil.getBooleanPerfernece(mContext,
				SharedUtil.user_IsBindWeiXin) ? "已绑定" : "未绑定");
	}
	
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	
	  @Override
	    protected void onDestroy() {
	    	MyApplication.getInstance().removeActivity(this);//加入回退栈
	    	super.onDestroy();
	    }
}
