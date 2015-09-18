package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.MyCountDown;
import com.umeng.analytics.MobclickAgent;

/**
 * 找回密码
 * 
 * @author a
 * 
 */
public class FindPasswordActivity extends BaseActivityWithTopView implements
		OnClickListener, TextWatcher {
	private TextView tv_mobile_title;
	private EditText et_mobile;
	private EditText et_code;
	private Button bt_sure;
	private TextView tv_getcode;// 获取验证码
	private int maxSecond = 90;
	private String getCodeString = "获取验证码";
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.find_password_layout);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		from = getIntent().getStringExtra("from");
			setTitle("验证手机号");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FindPasswordActivity.this.finish();
			}
		});
		tv_mobile_title = (TextView) findViewById(R.id.tv_mobile_title);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_mobile.addTextChangedListener(this);
		tv_getcode = (TextView) findViewById(R.id.tv_getcode);
		et_code = (EditText) findViewById(R.id.et_code);
		bt_sure = (Button) findViewById(R.id.bt_sure);
		bt_sure.setText("下一步");
		bt_sure.setOnClickListener(this);
		tv_getcode.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_mobile_title, et_mobile,
				tv_getcode, et_code, bt_sure, tv_top_title);
	}

	@Override
	public void onClick(View v) {
		final String phone = et_mobile.getText().toString().trim();
		final String code = et_code.getText().toString().trim();
		switch (v.getId()) {
		case R.id.bt_sure:// 确定找回密码

			if (TextUtils.isEmpty(phone)) {
				MyApplication.getInstance().showMessage(
						FindPasswordActivity.this, "手机号码不能为空");
				return;
			} else if (TextUtils.isEmpty(code)) {
				MyApplication.getInstance().showMessage(
						FindPasswordActivity.this, "验证码不能为空");
				return;
			}
			HttpControl httpControl = new HttpControl();
			httpControl.validVerifyCode(phone, code,
					new HttpCallBackInterface() {

						@Override
						public void http_Success(Object obj) {
							if ("bindPhone".equals(from)) {// 绑定手机号码
								bindMobile();
							} else {
								skip(Constants.USER_MOBILE, phone,
										ResetPasswordActivity.class, true);
							}
						}

						@Override
						public void http_Fails(int error, String msg) {

							MyApplication.getInstance().showMessage(
									FindPasswordActivity.this, msg);
						}
					}, FindPasswordActivity.this);

			break;
		case R.id.tv_getcode:// 获取验证码
			if (TextUtils.isEmpty(phone)) {
				MyApplication.getInstance().showMessage(
						FindPasswordActivity.this, "手机号码不能为空");
				return;
			}
			HttpControl hControl = new HttpControl();
			hControl.sendPhoeCode(phone, "bindPhone".equals(from)?"2":"1", new HttpCallBackInterface() {

				@Override
				public void http_Success(Object obj) {
					MyApplication.getInstance().showMessage(
							FindPasswordActivity.this, "验证码发送成功请注意查收");
					new MyCountDown(maxSecond * 1000, 1000, tv_getcode,
							getCodeString).start();
				}

				@Override
				public void http_Fails(int error, String msg) {

					MyApplication.getInstance().showMessage(
							FindPasswordActivity.this, msg);
				}
			}, FindPasswordActivity.this);
			break;
		default:
			break;
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		if (arg0.length() > 0) {
			et_mobile.setVisibility(View.VISIBLE);
		} else {
			et_mobile.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	private void bindMobile() {
		HttpControl httpControl = new HttpControl();
		httpControl.bindMobile(et_mobile.getText().toString().trim(),
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
						SharedUtil.setBooleanPerfernece(mContext,
								SharedUtil.user_IsBindMobile, true);
						FindPasswordActivity.this.finish();
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
					}
				}, mContext, true);
	}
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
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
