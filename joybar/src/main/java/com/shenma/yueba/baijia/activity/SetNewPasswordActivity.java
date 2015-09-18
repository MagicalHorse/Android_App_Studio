package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.umeng.analytics.MobclickAgent;
/**
 * 设置新密码
 * @author a
 *
 */
public class SetNewPasswordActivity extends BaseActivityWithTopView implements OnClickListener{

	private EditText et_new_password;
	private EditText et_new_repassword;
	private EditText et_old_password;
	private Button bt_sure;
	private TextView tv_old_password_title;
	private TextView tv_new_password_title;
	private TextView tv_confirm_passwrod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_new_password_layout);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		
		initView();
	}

	private void initView() {
		setTitle("设置新密码");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SetNewPasswordActivity.this.finish();
			}
		});
		tv_top_right.setText("完成");
		tv_top_right.setVisibility(View.VISIBLE);
		tv_top_right.setOnClickListener(this);
		tv_old_password_title = getView(R.id.tv_old_password_title);
		tv_new_password_title = getView(R.id.tv_new_password_title);
		tv_confirm_passwrod = getView(R.id.tv_confirm_passwrod);
		et_old_password=(EditText)findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_new_repassword = (EditText) findViewById(R.id.et_new_repassword);
		//bt_sure = (Button) findViewById(R.id.bt_sure);
		//bt_sure.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_old_password_title,tv_new_password_title,tv_confirm_passwrod,tv_top_right,et_new_password,et_new_repassword,bt_sure,tv_top_title,et_old_password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_right://找回
			//判断用户是否登录
			if(!(MyApplication.getInstance().isUserLogin(SetNewPasswordActivity.this)))
			{
				return;
			}
			String oldpwd=et_old_password.getText().toString().trim();
			String newpwd=et_new_password.getText().toString().trim();
			String renewpwd=et_new_repassword.getText().toString().trim();
			if(TextUtils.isEmpty(oldpwd))
			{
				MyApplication.getInstance().showMessage(SetNewPasswordActivity.this, "旧密码不能为空");
				return;
			}else if(TextUtils.isEmpty(newpwd))
			{
				MyApplication.getInstance().showMessage(SetNewPasswordActivity.this, "新密码不能为空");
				return;
			}else if(!(TextUtils.equals(newpwd,renewpwd)))
			{
				MyApplication.getInstance().showMessage(SetNewPasswordActivity.this, "两次密码输入不一致");
				return;
			}
			String phone=SharedUtil.getStringPerfernece(SetNewPasswordActivity.this, SharedUtil.user_mobile);
			if(phone==null)
			{
				MyApplication.getInstance().showMessage(SetNewPasswordActivity.this, "未获取的注册的手机号码");
				return;
			}
			HttpControl httpControl=new HttpControl();
			httpControl.updateLoginPwd(phone, newpwd, oldpwd, new HttpCallBackInterface() {
				
				@Override
				public void http_Success(Object obj) {
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
					finish();
				}
				
				@Override
				public void http_Fails(int error, String msg) {
					
					MyApplication.getInstance().showMessage(SetNewPasswordActivity.this, msg);
				}
			}, SetNewPasswordActivity.this);
			break;

		default:
			break;
		}
		
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
