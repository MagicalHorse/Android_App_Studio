package com.shenma.yueba.yangjia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.modle.Income;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RiseNumberTextView;
import com.umeng.analytics.MobclickAgent;

/**
 * 收益管理
 * @author a
 *
 */
public class EarningManagerActivity extends BaseActivityWithTopView implements OnClickListener {

	private Income income;//收益数据
	private TextView tv_earnings_today_title;
	private ImageView iv_help;
	private RiseNumberTextView tv_today_income;
	private RiseNumberTextView tv_withdraw_cash_money;
	private RiseNumberTextView tv_total_income_money;
	private TextView tv_withdraw_cash_title;
	private TextView tv_withdraw_cash_history;
	private TextView tv_apply_withdraw;
	private TextView tv_total_income_title;
	private TextView tv_income_detail;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.earnings_manager_layout);
		super.onCreate(savedInstanceState);
		getintentData();
		initView();
	}

	/**
	 * 获取传过来的数据
	 */
	private void getintentData() {
		Intent intent = getIntent();
		income = (Income) intent.getExtras().getSerializable("earningData");
	}

	private void initView() {
		setTitle("收益管理");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EarningManagerActivity.this.finish();
			}
		});
		tv_earnings_today_title = getView(R.id.tv_earnings_today_title);
		iv_help = getView(R.id.iv_help);
		tv_today_income = getView(R.id.tv_today_income);
		tv_withdraw_cash_title = getView(R.id.tv_withdraw_cash_title);
		tv_withdraw_cash_money = getView(R.id.tv_withdraw_cash_money);
		tv_withdraw_cash_history = getView(R.id.tv_withdraw_cash_history);
		tv_apply_withdraw = getView(R.id.tv_apply_withdraw);
		tv_total_income_title = getView(R.id.tv_total_income_title);
		tv_total_income_money = getView(R.id.tv_total_income_money);
		tv_income_detail = getView(R.id.tv_income_detail);
		try {

			// 设置数据
			tv_today_income.withNumber(Float.parseFloat(income!=null?ToolsUtil.DounbleToString_2(income.getToday_income()):""));
			// 设置动画播放时间
			tv_today_income.setDuration(1000);
			// 开始播放动画
			tv_today_income.start();
			
			// 设置数据
			tv_total_income_money.withNumber(Float.parseFloat(income!=null?ToolsUtil.DounbleToString_2(income.getTotal_income()):""));
			// 设置动画播放时间
			tv_total_income_money.setDuration(1000);
			// 开始播放动画
			tv_total_income_money.start();
			
			// 设置数据
			tv_withdraw_cash_money.withNumber(Float.parseFloat(income!=null?ToolsUtil.nullToString(income.getAvail_amount()):""));
			// 设置动画播放时间
			tv_withdraw_cash_money.setDuration(1000);
			// 开始播放动画
			tv_withdraw_cash_money.start();
		} catch (Exception e) {
		}
		tv_income_detail.setOnClickListener(this);
		tv_apply_withdraw.setOnClickListener(this);
		tv_withdraw_cash_history.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_top_title,tv_earnings_today_title,tv_today_income,tv_withdraw_cash_title,
				tv_withdraw_cash_money,tv_withdraw_cash_history,tv_apply_withdraw,tv_total_income_title
				,tv_total_income_money,tv_income_detail);
	}

	//刷新可提现收益
	public void refreshAvailAcount(String currentAcount){
		tv_withdraw_cash_money.setText(currentAcount);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_income_detail://收入明细
			skip(IncomeDetailActivity.class, false);
			break;
		case R.id.tv_withdraw_cash_history://提现历史
			skip(WithdrawHistoryActivity.class, false);
			break;
		case R.id.tv_apply_withdraw://申请提现
			Intent intent = new Intent(this,ApplyWithdrawActivity.class);
			intent.putExtra("money", tv_withdraw_cash_money.getText().toString().trim());
			startActivityForResult(intent, Constants.REQUESTCODE);
		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.REQUESTCODE && resultCode == Constants.RESULTCODE){
			if(data!=null){
				String acount = data.getStringExtra("data");
				try {
					double retain = Double.valueOf(income.getAvail_amount().toString().trim()) - Double.valueOf(acount);
					tv_withdraw_cash_money.setText(ToolsUtil.DounbleToString_2(retain));
					income.setAvail_amount(ToolsUtil.DounbleToString_2(retain));
					MyApplication.getInstance().getIndexRefreshService().refreshList();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
