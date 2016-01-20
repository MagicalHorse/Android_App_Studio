package com.shenma.yueba.baijia.fragment;


import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.FindPasswordActivity;
import com.shenma.yueba.baijia.activity.MainActivityForBaiJia;
import com.shenma.yueba.baijia.modle.UserRequestBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.JpushUtils;
import com.shenma.yueba.util.WXLoginUtil;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import im.control.SocketManger;

public class LoginFragment extends BaseFragment implements OnClickListener {

	private TextView tv_mobile_title;
	private EditText et_mobile;
	private EditText et_password;
	private TextView tv_other;
	private TextView tv_wechat;
	private TextView tv_qq;
	private TextView tv_forget;
	private Button bt_login;
	private View view;
	private UMSocialService mController;
	private StringBuilder sb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.login_fragment_layout, null);
			tv_mobile_title = (TextView) view
					.findViewById(R.id.tv_mobile_title);
			bt_login = (Button) view.findViewById(R.id.bt_login);
			et_mobile = (EditText) view.findViewById(R.id.et_mobile);
			et_password = (EditText) view.findViewById(R.id.et_password);
//			et_mobile.setText("13011817836");
//			et_password.setText("11111111");
			tv_forget = (TextView) view.findViewById(R.id.tv_forget);
			tv_other = (TextView) view.findViewById(R.id.tv_other);
			tv_wechat = (TextView) view.findViewById(R.id.tv_wechat);
			tv_qq = (TextView) view.findViewById(R.id.tv_qq);
			tv_forget.setOnClickListener(this);
			bt_login.setOnClickListener(this);
			tv_wechat.setOnClickListener(this);
			tv_qq.setOnClickListener(this);
			FontManager.changeFonts(getActivity(), tv_mobile_title, tv_forget,
					tv_other, tv_wechat, bt_login);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_forget:// 找回密码
			Intent intent = new Intent(getActivity(),
					FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_login:// 登录
			if(TextUtils.isEmpty(et_mobile.getText().toString().trim())){
				Toast.makeText(getActivity(), "手机号不能为空", 1000).show();
				break;
			}
//			if(!ToolsUtil.checkPhone(et_mobile.getText().toString().trim())){
//				Toast.makeText(getActivity(), "手机号格式不正确", 1000).show();
//				break;
//			}
			
			if(et_mobile.getText().toString().trim().length()!=11){
				Toast.makeText(getActivity(), "请输入正确的手机号", 1000).show();
				break;
			}
			
			if(TextUtils.isEmpty(et_password.getText().toString().trim())){
				Toast.makeText(getActivity(), "密码不能为空", 1000).show();
				break;
			}
			final HttpControl httpControl = new HttpControl();
			httpControl.userLogin(et_mobile.getText().toString().trim(),
					et_password.getText().toString().trim(),
					new HttpCallBackInterface() {

						@Override
						public void http_Success(Object obj) {
							String flag = getActivity().getIntent().getStringExtra("flag");
							UserRequestBean bean = (UserRequestBean) obj;
							httpControl.setLoginInfo(getActivity(), bean);
							if("needLogin".equals(flag)){
								
							}else if (obj != null && obj instanceof UserRequestBean) {
								
								Intent intent = new Intent(getActivity(),MainActivityForBaiJia.class);
								//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								JpushUtils jpushUtils = new JpushUtils(getActivity());
								jpushUtils.setAlias(bean.getData().getId()+"");
								startActivity(intent);
								Log.i("TAG", "jUSH LoginFragment:"+bean.getData().getId());
							}
							getActivity().finish();
						}

						@Override
						public void http_Fails(int error, String msg) {

							MyApplication.getInstance().showMessage(
									getActivity(), msg);
						}
					}, getActivity());
			// }

			
			break;
		case R.id.tv_wechat:// 微信登录
			WXLoginUtil wxLoginUtil = new WXLoginUtil(getActivity());
			wxLoginUtil.initWeiChatLogin(true,true,false);
			break;
		case R.id.tv_qq:// QQ登录
//			initQQLogin();
			break;
		default:
			break;
		}
	}


//	private void initQQLogin() {
//		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
//				"100424468", "c7394704798a158208a74ab60104f0ba");
//		qqSsoHandler.addToSocialSDK();
//
//		mController.doOauthVerify(getActivity(), SHARE_MEDIA.QQ,
//				new UMAuthListener() {
//					@Override
//					public void onStart(SHARE_MEDIA platform) {
//						Toast.makeText(getActivity(), "正在授权，请稍后",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onError(SocializeException e,
//							SHARE_MEDIA platform) {
//						Toast.makeText(getActivity(), "授权错误",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onComplete(Bundle value, SHARE_MEDIA platform) {
//						Toast.makeText(getActivity(), "授权完成",
//								Toast.LENGTH_SHORT).show();
//						// 获取相关授权信息
//						mController.getPlatformInfo(getActivity(),
//								SHARE_MEDIA.QQ, new UMDataListener() {
//
//									@Override
//									public void onStart() {
//										Toast.makeText(getActivity(),
//												"获取平台数据开始...",
//												Toast.LENGTH_SHORT).show();
//									}
//
//									@Override
//									public void onComplete(int status,
//											Map<String, Object> info) {
//										if (status == 200 && info != null) {
//											sb = new StringBuilder();
//											Set<String> keys = info.keySet();
//											sb.append("{");
//											for (String key : keys) {
//												sb.append(key
//														+ ":"
//														+ info.get(key)
//																.toString());
//											}
//											sb.append("}");
//											Log.d("TestData", sb.toString());
//										} else {
//											Log.d("TestData", "发生错误：" + status);
//										}
//									}
//								});
//					}
//
//					@Override
//					public void onCancel(SHARE_MEDIA platform) {
//						Toast.makeText(getActivity(), "授权取消",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//
//	}

}
