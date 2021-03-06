package com.shenma.yueba.yangjia.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 店铺说明
 * 
 * @author a
 */
public class StoreIntroduceActivity extends BaseActivityWithTopView {
	private EditText et_modify_info;//店铺内容
	private TextView tv_retain;//剩余字数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.store_introduce);
		super.onCreate(savedInstanceState);
		initView();
	}

	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	
	private void initView() {
		//设置标题
		setTitle("店铺说明");
		//设置标题左侧返回点击事件
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//设置标题右侧点击事件
		setTopRightTextView("完成", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_modify_info.getText().toString()
						.trim())) {
					Toast.makeText(mContext, "店铺说明不能为空", 1000).show();
					return;
				}
				setStoreIntroduce();
			}
		});
		et_modify_info = (EditText) findViewById(R.id.et_modify_info);
		et_modify_info.setText(SharedUtil.getStringPerfernece(mContext,
				SharedUtil.user_Description));
		tv_retain = (TextView) findViewById(R.id.tv_retain);
		tv_retain.setText(200
				- et_modify_info.getText().toString().trim().length() + "字");
		et_modify_info.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_retain.setText(200 - s.toString().trim().length() + "字");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		FontManager.changeFonts(mContext, et_modify_info, tv_top_title,
				tv_retain, tv_top_right);
	}

	
	/**
	 * 联网设置店铺介绍内容
	 */
	private void setStoreIntroduce() {
		HttpControl httpControl = new HttpControl();
		httpControl.setStoreIntroduce(et_modify_info.getText().toString()
				.trim(), new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				Toast.makeText(mContext, "设置成功", 1000).show();
				SharedUtil.setStringPerfernece(mContext,
						SharedUtil.user_Description, et_modify_info.getText()
								.toString().trim());
				StoreIntroduceActivity.this.finish();
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(mContext, ToolsUtil.nullToString(msg), 1000)
						.show();
			}
		}, StoreIntroduceActivity.this, true, false);
	}


	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}
}
