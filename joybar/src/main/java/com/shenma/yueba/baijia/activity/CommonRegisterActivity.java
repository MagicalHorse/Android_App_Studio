package com.shenma.yueba.baijia.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.ParserJson;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.util.UserBean;
import com.umeng.analytics.MobclickAgent;

/***
 * 普通注册界面
 * 
 * @author Administrator
 * 
 */
public class CommonRegisterActivity extends BaseActivityWithTopView implements
		OnClickListener, OnCheckedChangeListener {
	private EditText et_username, et_password, et_repassword;
	private Button bt_register;
	private CheckBox cb_shop;
	private String showContent;
	private boolean boolIfShow = true;
	private LinearLayout ll_shop;
	private TextView tv_had_agreen;
	private LinearLayout ll_address;// 选择地址
	private TextView tv_address;
	private String province;// 省
	private String city;// 市
	private String region;// 县
	private String addressId;
	private LinearLayout ll_verification;// 验证码区域
	private String getCodeString = "获取验证码";
	private int maxSecond = 90;// 定时时间
	private Button bt_reget;
	private EditText et_code;
	private String email, mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.common_register_layout2);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setTitle("普通注册");
		setLeftTextView("返回", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		findView();
	}

	private void findView() {
		bt_reget = getView(R.id.bt_reget);
		ll_verification = getView(R.id.ll_verification);
		tv_had_agreen = getView(R.id.tv_had_agreen);
		et_username = getView(R.id.et_username);
		et_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2,
					int arg3) {
				if (ToolsUtil.checkPhone(text.toString())) {// 如果是手机号
					ll_verification.setVisibility(View.VISIBLE);
					mobile = text.toString();
					email = "";
				} else {
					ll_verification.setVisibility(View.GONE);
				}
				if (ToolsUtil.checkEmail(text.toString())) {// 如果是邮箱
					email = text.toString();
					mobile = "";
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		et_password = getView(R.id.et_password);
		et_repassword = getView(R.id.et_repassword);
		cb_shop = getView(R.id.cb_shop);
		bt_register = getView(R.id.bt_register);
		ll_shop = getView(R.id.ll_shop);
		ll_address = getView(R.id.ll_address);
		tv_address = getView(R.id.tv_address);
		ll_address.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		ll_shop.setOnClickListener(this);
		cb_shop.setOnCheckedChangeListener(this);
		checkBoxListenner();
		bt_register.setText("注册");
		et_code = getView(R.id.et_code);
		bt_reget.setOnClickListener(this);
	}

	private void checkBoxListenner() {
		if (cb_shop.isChecked()) {
			tv_had_agreen.setVisibility(View.VISIBLE);
		} else {
			tv_had_agreen.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.ll_shop:// 商城服务条款
			intent = new Intent(mContext, WebActivity.class);
//			intent.putExtra("url", NetUtil.url_for_wap_pay
//					+ "/fg/user/userRegProtocol.ftl");
//			intent.putExtra("title", "服务条款");
			startActivity(intent);
			break;
		case R.id.ll_address:// 选择地址
//			skipForResult(AddressListOne.class,
//					Constants.requestCodeToProductInfo);
			break;
		case R.id.bt_register:// 注册
			if ("注册中".equals(bt_register.getText().toString().trim())) {
				return;
			}
			if(!ToolsUtil.checkEmail(et_username.getText().toString()) && !ToolsUtil.checkPhone(et_username.getText().toString())){
				Toast.makeText(mContext, "手机号或者邮箱不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			final String username = String.valueOf(et_username.getText());
			final String password = String.valueOf(et_password.getText());
			final String repassword = String.valueOf(et_repassword.getText());
			if (username == null || "".equals(username)) {
				Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
			}else if (password == null || "".equals(password)) {
				Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
			} else if (repassword == null || "".equals(repassword)) {
				Toast.makeText(mContext, "请输入确认密码", Toast.LENGTH_SHORT).show();
			} else if (!password.equals(repassword)) {
				Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
			}
			// else if (mobile == null || "".equals(mobile)) {
			// Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
			// }
			else if (!cb_shop.isChecked()) {
				Toast.makeText(mContext, "请同意商城服务条款", Toast.LENGTH_SHORT)
						.show();
			} else if (password.length() < 6 || password.length() > 20) {
				Toast.makeText(mContext, "密码6到20位", Toast.LENGTH_SHORT).show();
			} else {
				bt_register.setText("注册中");
//				TaskManagerFactory.createDataTaskManager().addTask(new ITask() {
//
//					@Override
//					public void onTaskNumChanged(int taskNum) {
//					}
//
//					@Override
//					public void execute() {
//						final String result = NetUtil.reg(mContext, et_username
//								.getText().toString(), et_password.getText()
//								.toString(), mobile, email, et_code.getText()
//								.toString());
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								bt_register.setText("注    册");
//								UserBean bean = ParserJson.user_detail(result);
//								showContent = Constants.connect_error;
//								if (bean != null && bean.getQuery() != null) {
//									if (ToolsUtil.isQuerySeccess(bean
//											.getQuery())) {
//										Toast.makeText(mContext, "注册成功", 1000)
//												.show();
//										String id = bean.getUser_list()
//												.getUser_id();
//										SharedUtil.setUserId(mContext, id);
//										SharedUtil.setUserName(mContext,
//												username);
//										SharedUtil.setUserPassword(mContext,
//												password);
//										SharedUtil.setUserType(mContext,
//												Constants.defalt_buy_user);
//										SharedUtil.setSessionId(mContext, bean
//												.getUser_list().getSessionId());
//										// setResult(202);
//										// CommonRegisterActivity.this.finish();
//										skip(MainActivity.class, true);
//									} else {
//										String reson = bean.getQuery()
//												.getReson();
//										Toast.makeText(mContext, reson, 1000)
//												.show();
//									}
//								}
//							}
//						});
//					}
//				});
			}
			break;
		case R.id.bt_reget:// 重新获取验证码
			if (!getCodeString.equals(bt_reget.getText().toString())) {
				return;
			}
			if ("正在获取验证码".equals(bt_reget.getText().toString())) {
				return;
			}
			submitCodeByMobile();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == Constants.requestCodeToProductInfo
//				&& resultCode == Constants.responseCodeFromProductInfo) {
//			String name = data.getStringExtra("name");
//			addressId = data.getStringExtra("id");
//			tv_address.setText(name);
//			province = addressId.split("-")[0];
//			city = addressId.split("-")[1];
//			if (addressId.split("-").length == 3) {
//				region = addressId.split("-")[2];
//			} else {
//				region = "";
//			}
//		}
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean arg1) {
		switch (view.getId()) {
		case R.id.cb_shop:// 商城服务条款
			checkBoxListenner();
			break;
		default:
			break;
		}

	}

	/**
	 * 获取验证码
	 */
	private void submitCodeByMobile() {
		if ("正在获取验证码".equals(bt_reget.getText().toString())) {
			return;
		}
		bt_reget.setText("正在获取验证码");
//		TaskManagerFactory.createDataTaskManager().addTask(new ITask() {
//			@Override
//			public void onTaskNumChanged(int taskNum) {
//			}
//
//			@Override
//			public void execute() {
//				String result = NetUtil.mobile_validate_code_register(mContext, mobile);
//				final FindPasswordbackBean bean = ParserJson
//						.findPasswordByMobile(result);
//				runOnUiThread(new Runnable() {
//					public void run() {
//						if (bean != null && bean.getQuery() != null) {
//							if (ToolsUtil.isQuerySeccess(bean.getQuery())) {
//								Toast.makeText(mContext, "获取成功", 1000).show();
//								new MyCountDown(maxSecond * 1000, 1000,
//										bt_reget, getCodeString).start();
//							} else {
//								bt_reget.setText("获取验证码");
//								Toast.makeText(mContext,
//										bean.getQuery().getReson(), 1000)
//										.show();
//							}
//						} else {
//							runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//									bt_reget.setText("获取验证码");
//									Toast.makeText(mContext,
//											getString(R.string.request_fail),
//											1000).show();
//								}
//							});
//						}
//					}
//				});
//			}
//		});
	}
	
	
	  @Override
	    protected void onDestroy() {
	    	MyApplication.getInstance().removeActivity(this);//加入回退栈
	    	super.onDestroy();
	    }
	  
	  
	  

		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			}
			public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			}
}
