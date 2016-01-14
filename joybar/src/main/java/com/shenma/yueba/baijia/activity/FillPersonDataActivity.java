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
import im.control.SocketManger;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.UserRequestBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;



/**
 * 注册第二步骤
 * @author a
 *
 */
public class FillPersonDataActivity extends BaseActivityWithTopView {

	private EditText et_username;
	private EditText et_password;
	private EditText et_repassword;
	private TextView tv_city;
	private TextView tv_change_city;
	private Button bt_commit;

	public final static int REQUESTCODE=200;
	//城市的名称
	public final static String CITY_NAME="CITY_NAME";
	//对应城市的 值
	public final static String CITY_VALUE="CITY_VALUE";
	Intent startintent;
	String moblie;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fill_person_data);
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		moblie=this.getIntent().getStringExtra(Constants.MOBILE);
		if(moblie==null)
		{
			MyApplication.getInstance().showMessage(this, "数据传输错误");
			finish();
			return;
		}
		setTitle("完善个人资料");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_repassword = (EditText) findViewById(R.id.et_repassword);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_change_city = (TextView) findViewById(R.id.tv_change_city);
		bt_commit = (Button) findViewById(R.id.bt_commit);
		FontManager.changeFonts(mContext, tv_top_title, et_username,
				et_password, et_repassword, tv_city, tv_change_city, bt_commit);
		
		tv_change_city.setOnClickListener(onClickListener);
		bt_commit.setOnClickListener(onClickListener);
	}
	
	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId())
			{
			case R.id.tv_change_city:
				startintent = new Intent(FillPersonDataActivity.this,CityListActivity.class);
				startActivityForResult(startintent, FillPersonDataActivity.REQUESTCODE);
				break;
			case R.id.bt_commit:
				commitData();
				break;
			}
			
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUESTCODE)
		{
			if(data!=null)
			{
				//获取选中的城市名称
				String cityname=data.getStringExtra(FillPersonDataActivity.CITY_NAME);
				//获取选中的城市的 id
				int cityvalue=data.getIntExtra(FillPersonDataActivity.CITY_VALUE, -1);
				if(cityname!=null && cityvalue!=-1)
				{
					tv_city.setText(cityname);
					tv_city.setTag(cityvalue);
				}
			}
		}
	};
	
	/*****
	 * 提交 注册数据
	 * ***/
	void commitData()
	{
		if (!ToolsUtil.isNetworkConnected(mContext)) {//判断网络是否可用
			Toast.makeText(mContext,getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
			return;
		}
		String name=et_username.getText().toString().trim();
		String pass=et_password.getText().toString().trim();
		String repass=et_repassword.getText().toString().trim();
		String cityname=tv_city.getText().toString().trim();
		
		int cityvalue=-1;
		if(tv_city.getTag()!=null && tv_city.getTag() instanceof Integer)
		{
			cityvalue=(Integer)tv_city.getTag();
		}
		
		if(TextUtils.isEmpty(name))
		{
			MyApplication.getInstance().showMessage(FillPersonDataActivity.this, "用户名称不能为空");
			return;
		}
		else if(TextUtils.isEmpty(pass))
		{
			MyApplication.getInstance().showMessage(FillPersonDataActivity.this, "密码不能为空");
			return;
		}else if(pass.length()<6)
		{
			MyApplication.getInstance().showMessage(FillPersonDataActivity.this, "密码长度不能小于6位");
			return;
		}
		else if(!(TextUtils.equals(pass, repass)))
		{
			MyApplication.getInstance().showMessage(FillPersonDataActivity.this, "两次密码不一致");
			return;
		}else if(TextUtils.isEmpty(cityname)|| cityvalue==-1)
		{
			MyApplication.getInstance().showMessage(FillPersonDataActivity.this, "请选择城市");
			return;
		}
		final HttpControl httpControl=new HttpControl();
		httpControl.registerUserInfo(moblie, name, pass, cityvalue, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				
				if(obj!=null && obj instanceof UserRequestBean)
				{
					finish();
					UserRequestBean bean=(UserRequestBean)obj;
					httpControl.setLoginInfo(FillPersonDataActivity.this, bean);
					Intent intent=new Intent(FillPersonDataActivity.this,MainActivityForBaiJia.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("forwarddata", bean);
					startActivity(intent);
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				
				MyApplication.getInstance().showMessage(FillPersonDataActivity.this, msg);
			}
		}, FillPersonDataActivity.this);
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
