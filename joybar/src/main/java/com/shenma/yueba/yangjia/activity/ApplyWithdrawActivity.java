package com.shenma.yueba.yangjia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ApplyResultActivity;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.dialog.WeChatDialog;
import com.shenma.yueba.baijia.modle.GetUserFlowStatusBackBean;
import com.shenma.yueba.baijia.modle.UserFlowStatusBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.constants.HttpConstants;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.WXLoginUtil;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.umeng.analytics.MobclickAgent;

/**
 * 申请提现的界面
 * 
 * @author a
 * 
 */

public class ApplyWithdrawActivity extends BaseActivityWithTopView implements
		OnClickListener {

	private TextView tv_retan_money;
	private EditText et_money;
	private TextView tv_yuan;
	private TextView tv_introduce;
	private TextView tv_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.applay_withdraw_layout);
		super.onCreate(savedInstanceState);
		initView();
		getIntentData();
	}

	private void getIntentData() {
		String money = getIntent().getStringExtra("money");
		tv_retan_money.setText("可提现收益" + money + "元");
	}

	private void initView() {
		setTitle("申请提现");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplyWithdrawActivity.this.finish();
			}
		});
		tv_retan_money = getView(R.id.tv_retan_money);
		et_money = getView(R.id.et_money);
		tv_yuan = getView(R.id.tv_yuan);
		tv_introduce = getView(R.id.tv_introduce);
		tv_sure = getView(R.id.tv_sure);
		tv_sure.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_retan_money, et_money, tv_yuan,
				tv_introduce,tv_sure,tv_top_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:// 确认提现
			String money = et_money.getText().toString().trim();
			if (money.matches("[0-9]+")) {
				
				if(SharedUtil.getBooleanPerfernece(mContext, SharedUtil.user_IsBindWeiXin)){
					GetUserFlowStatus();
				}else{
					DialogUtils dialog = new DialogUtils();
					dialog.alertDialog(mContext, "提示", "您需要绑定微信才可以提款，现在去绑定吗？", new DialogUtilInter() {
						@Override
						public void dialogCallBack(int... which) {
							Toast.makeText(mContext, "正在绑定微信", Toast.LENGTH_SHORT).show();
							// 绑定手机号
							WXLoginUtil wxLoginUtil = new WXLoginUtil(mContext);
							wxLoginUtil.initWeiChatLogin(false,false,false);
						}
					}, true, "绑定", "取消", false, true);
				
				}
			} else {
				Toast.makeText(mContext, "请输入整数", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	public void getIncomeRedPack() {
		HttpControl httpControl = new HttpControl();
		httpControl.getIncomeRedPack(et_money.getText().toString().trim(),
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						Intent intent = new Intent(ApplyWithdrawActivity.this,
								ApplyResultActivity.class);
						intent.putExtra("flag", "applaywithdraw");// 申请提现
						startActivityForResult(intent, Constants.REQUESTCODE);
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, 1000).show();

					}
				}, ApplyWithdrawActivity.this);
	}
	
	
	
	
	
	/**
	 * 关注微信账号
	 */
	private void GetUserFlowStatus() {
		final HttpControl httpcon = new HttpControl();
		httpcon.getUserFlowStatus(new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				GetUserFlowStatusBackBean bean = (GetUserFlowStatusBackBean) obj;
				UserFlowStatusBean data = bean.getData();
				if(data!=null){
					boolean isFlow = data.isIsFlow();
						if(isFlow){//已經关注
							getIncomeRedPack();
						}else{//没有关注
							WeChatDialog dialog = new WeChatDialog(ApplyWithdrawActivity.this,data.getQRCode(),data.getName());
							dialog.show();
						}
					}
				};

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ApplyWithdrawActivity.this, msg, 1000).show();
			}
		}, ApplyWithdrawActivity.this, true);
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
			
			
			
			
			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				if(requestCode == Constants.REQUESTCODE && resultCode == Constants.RESULTCODE){
					Intent intent = new Intent();
					intent.putExtra("data", et_money.getText().toString().trim());
					setResult(Constants.RESULTCODE,intent);
					finish();
				}
				super.onActivityResult(requestCode, resultCode, data);
			}
}
