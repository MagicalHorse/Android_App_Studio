package com.shenma.yueba.yangjia.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.modle.HuoKuanManagerBackBean;
import com.shenma.yueba.inter.HuoKuanManagerRefreshInter;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.progressbar.NumberProgressBar;
import com.shenma.yueba.view.progressbar.NumberProgressBar.ProgressTextVisibility;
import com.shenma.yueba.view.progressbar.OnProgressBarListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 货款管理
 * 
 * @author a
 * 
 */
public class HuoKuanManagerActivity extends BaseActivityWithTopView implements
		OnClickListener, OnProgressBarListener, HuoKuanManagerRefreshInter {

	private PullToRefreshScrollView pull_refresh_scrollview;
	private TextView tv_week_title;
	private TextView tv_in_and_out;
	private TextView tv_tatal_money;
	private TextView tv_had_withdraw_ratio;
	private TextView tv_had_withdraw_money;
	private NumberProgressBar numberbar_had_withdraw, numberbar_week;
	private TextView tv_can_withdraw_ratio;
	private TextView tv_can_withdraw_money;
	private NumberProgressBar numberbar_can_withdraw;
	private TextView tv_freeze_ratio;
	private TextView tv_freeze_money;
	private NumberProgressBar numberbar_freeze;
	private TextView tv_back_ratio;
	private TextView tv_back_money;
	private NumberProgressBar numberbar_back;
	private TextView tv_tishi;
	int hadProgress = 0;
	private int canProgress = 0, freezeProgress = 0, backProgress = 0,
			weekProgress = 0;
	// private CountDownTimer timer;
	private TextView tv_tatal_title;// 总货款标题
	private TextView tv_week_money;// 本周贷款额度
	private TextView tv_had_used_persent;
	private TextView tv_week_amount;
	private int i = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int newHadProgress = (int) (((double) hadProgress * (double) i) / ((double) 100));
			numberbar_had_withdraw.setProgress(newHadProgress);
			int newCanProgress = (int) (((double) canProgress * (double) i) / ((double) 100));
			numberbar_can_withdraw.setProgress(newCanProgress);
			int newfreezeProgress = (int) (((double) freezeProgress * (double) i) / ((double) 100));
			numberbar_freeze.setProgress(newfreezeProgress);
			int newbackProgress = (int) (((double) backProgress * (double) i) / ((double) 100));
			numberbar_back.setProgress(newbackProgress);
			int newweekProgress = (int) (((double) weekProgress * (double) i) / ((double) 100));
			numberbar_week.setProgress(newweekProgress);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		MyApplication.getInstance().getHuoKuanManagerRefreshService()
				.addToList(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.huokuan_manager);
		super.onCreate(savedInstanceState);
		initView();
		getHuoKuanManagerInfo(true);
	}

	private void initView() {
		setTitle("货款管理");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HuoKuanManagerActivity.this.finish();
			}

		});
		pull_refresh_scrollview =  getView(R.id.pull_refresh_scrollview);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
    	if(ToolsUtil.isNetworkConnected(HuoKuanManagerActivity.this)){
    		//刷新数据
    		getHuoKuanManagerInfo(false);
    	}else{
			Toast.makeText(HuoKuanManagerActivity.this, "网络不可用，请稍后重试", Toast.LENGTH_SHORT).show();
		}
			}
		});
		tv_tishi = getView(R.id.tv_tishi);
		tv_week_title = getView(R.id.tv_week_title);
		tv_week_money = getView(R.id.tv_week_money);
		tv_had_used_persent = getView(R.id.tv_had_used_persent);
		tv_week_amount = getView(R.id.tv_week_amount);
		tv_tatal_title = getView(R.id.tv_tatal_title);
		tv_in_and_out = getView(R.id.tv_in_and_out);
		tv_in_and_out.setOnClickListener(this);
		tv_had_withdraw_ratio = getView(R.id.tv_had_withdraw_ratio);
		tv_had_withdraw_money = getView(R.id.tv_had_withdraw_money);
		tv_tatal_money = getView(R.id.tv_tatal_money);
		numberbar_week = (NumberProgressBar) findViewById(R.id.numberbar_week);
		numberbar_had_withdraw = (NumberProgressBar) findViewById(R.id.numberbar_had_withdraw);
		tv_can_withdraw_ratio = getView(R.id.tv_can_withdraw_ratio);
		tv_can_withdraw_money = getView(R.id.tv_can_withdraw_money);
		numberbar_can_withdraw = (NumberProgressBar) findViewById(R.id.numberbar_can_withdraw);
		tv_freeze_ratio = getView(R.id.tv_freeze_ratio);
		tv_freeze_money = getView(R.id.tv_freeze_money);
		numberbar_freeze = (NumberProgressBar) findViewById(R.id.numberbar_freeze);
		tv_back_ratio = getView(R.id.tv_back_ratio);
		tv_back_money = getView(R.id.tv_back_money);
		numberbar_back = (NumberProgressBar) findViewById(R.id.numberbar_back);

		numberbar_week.setOnProgressBarListener(this);
		numberbar_week.setReachedBarHeight(20);
		numberbar_week.setUnreachedBarHeight(0);
		numberbar_week.setReachedBarColor(getResources().getColor(
				R.color.main_color));
		numberbar_week
				.setProgressTextVisibility(ProgressTextVisibility.Invisible);

		numberbar_had_withdraw.setOnProgressBarListener(this);
		numberbar_had_withdraw.setReachedBarHeight(20);
		numberbar_had_withdraw.setUnreachedBarHeight(0);
		numberbar_had_withdraw.setReachedBarColor(getResources().getColor(
				R.color.main_color));
		numberbar_had_withdraw
				.setProgressTextVisibility(ProgressTextVisibility.Invisible);

		numberbar_can_withdraw.setReachedBarColor(getResources().getColor(
				R.color.main_color));
		numberbar_can_withdraw.setOnProgressBarListener(this);
		numberbar_can_withdraw.setReachedBarHeight(20);
		numberbar_can_withdraw.setUnreachedBarHeight(0);
		numberbar_can_withdraw
				.setProgressTextVisibility(ProgressTextVisibility.Invisible);

		numberbar_freeze.setReachedBarColor(getResources().getColor(
				R.color.main_color));
		numberbar_freeze.setOnProgressBarListener(this);
		numberbar_freeze.setReachedBarHeight(20);
		numberbar_freeze.setUnreachedBarHeight(0);
		numberbar_freeze
				.setProgressTextVisibility(ProgressTextVisibility.Invisible);

		numberbar_back.setReachedBarColor(getResources().getColor(
				R.color.main_color));
		numberbar_back.setOnProgressBarListener(this);
		numberbar_back.setReachedBarHeight(20);
		numberbar_back.setUnreachedBarHeight(0);
		numberbar_back
				.setProgressTextVisibility(ProgressTextVisibility.Invisible);

		FontManager.changeFonts(mContext, tv_top_title, tv_had_withdraw_ratio,
				tv_had_withdraw_money, tv_can_withdraw_ratio,
				tv_can_withdraw_money, tv_freeze_ratio, tv_freeze_money,
				tv_back_ratio, tv_back_money, tv_tatal_title, tv_week_money,
				tv_had_used_persent, tv_week_amount, tv_week_title,
				tv_tatal_money, tv_tishi);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_in_and_out:// 货款收支
			skip(HuoKuanIncomingAndOutgoingsActivity.class, false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChange(int current, int max) {
		// TODO Auto-generated method stub

	}

	private void getHuoKuanManagerInfo(boolean showDialog) {
		i = 0;
		HttpControl httpContorl = new HttpControl();
		httpContorl.getHuoKuanManagerInfo(new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				pull_refresh_scrollview.onRefreshComplete();
				HuoKuanManagerBackBean bean = (HuoKuanManagerBackBean) obj;
				if (bean != null && bean.getData() != null) {
					tv_had_withdraw_ratio.setText("已提现货款 "
							+ ToolsUtil.nullToString(bean.getData()
									.getPickedPercent()));
					tv_had_withdraw_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getPickedAmount()));
					try {
						double userdCredit = bean.getData()
								.getUsedCredit();
						double allCredit = bean.getData().getCredit();
						if ((userdCredit / allCredit) < 1
								&& (userdCredit / allCredit) > 0) {
							weekProgress = 1;
						} else {
							weekProgress = (int) (100 * (userdCredit / allCredit));
						}
					} catch (Exception e) {
						weekProgress = 0;
					}
					try {
						double had = (100 * (Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getPickedAmount())) / Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getTotalAmount()))));
						if (had > 0 && had < 1) {
							hadProgress = 1;
						} else {
							hadProgress = (int) had;
						}
						// if(hadProgress == 0){
						// hadProgress = 2;
						// }
					} catch (Exception e) {
						hadProgress = 0;
					}
					try {
						double can = (100 * (Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getCanPickAmount())) / bean
										.getData().getTotalAmount()));
						if (can > 0 && can < 1) {
							canProgress = 1;
						} else {
							canProgress = (int) can;
						}
						// if(canProgress == 0){
						// canProgress = 2;
						// }
					} catch (Exception e) {
						canProgress = 0;
					}
					try {
						double freeze = (100 * (Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getFrozenAmount())) / Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getTotalAmount()))));
						if (freeze > 0 && freeze < 1) {
							freezeProgress = 1;
						} else {
							freezeProgress = (int) freeze;
						}
						// if(freezeProgress == 0){
						// freezeProgress = 2;
						// }
					} catch (Exception e) {
						freezeProgress = 0;
					}
					try {
						double back = (100 * (Double.parseDouble(ToolsUtil.DounbleToString_2(bean.getData().getRmaAmount())) / Double
								.parseDouble(ToolsUtil.DounbleToString_2(bean
										.getData().getTotalAmount()))));
						if (back > 0 && back < 1) {
							backProgress = 1;
						} else {
							backProgress = (int) back;
						}
						// if(backProgress == 0){
						// backProgress = 2;
						// }
					} catch (Exception e) {
						backProgress = 0;
					}
					tv_had_used_persent.setText("已使用额度："
							+ ToolsUtil.nullToString(bean.getData()
									.getUsedCreditPercent()));
					tv_week_amount.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getUsedCredit()));
					tv_week_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData().getCredit()));
					tv_can_withdraw_ratio.setText("可提现货款 "
							+ ToolsUtil.nullToString(bean.getData()
									.getCanPickPercent()));
					tv_can_withdraw_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getCanPickAmount()));
					tv_freeze_ratio.setText("冻结货款 "
							+ ToolsUtil.nullToString(bean.getData()
									.getFrozenPercent()));
					tv_freeze_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getFrozenAmount()));
					tv_back_ratio.setText("退款 "
							+ ToolsUtil.nullToString(bean.getData()
									.getRmaPercent()));
					tv_back_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getRmaAmount()));
					tv_tatal_money.setText("￥"
							+ ToolsUtil.DounbleToString_2(bean.getData()
							.getTotalAmount()));
					// timer = new CountDownTimer(1000, 2) {
					// @Override
					// public void onTick(long millisUntilFinished) {
					// double progress =
					// ((double)(1000-millisUntilFinished)/(double)1000);
					// //
					// numberbar_had_withdraw.setProgress((int)hadProgress-(int)millisUntilFinished/10);
					// //
					// numberbar_can_withdraw.setProgress(canProgress-(int)millisUntilFinished/10);
					// //
					// numberbar_freeze.setProgress(freezeProgress-(int)millisUntilFinished/10);
					// //
					// numberbar_back.setProgress(backProgress-(int)millisUntilFinished/10);
					// //
					// numberbar_week.setProgress(weekProgress-(int)millisUntilFinished/10);
					//
					// numberbar_had_withdraw.setProgress(hadProgress);
					// numberbar_can_withdraw.setProgress(canProgress);
					// numberbar_freeze.setProgress(freezeProgress);
					// numberbar_back.setProgress(backProgress);
					// numberbar_week.setProgress(weekProgress);
					// }
					// @Override
					// public void onFinish() {
					// // TODO Auto-generated method stub
					//
					// }
					// }.start();

					final Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if (i >= 100) {
								timer.cancel();
							} else {
								i++;
								mHandler.sendEmptyMessage(0);
							}
						}
					}, 0, 6);
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				pull_refresh_scrollview.onRefreshComplete();

			}
		}, HuoKuanManagerActivity.this, showDialog, true);
	}

	@Override
	protected void onDestroy() {
		// if(timer!=null){
		// timer.cancel();
		// }
		MyApplication.getInstance().getHuoKuanManagerRefreshService()
				.removeFromList(this);
		MyApplication.getInstance().removeActivity(this);// 移除
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
	public void refresh() {
		getHuoKuanManagerInfo(false);

	}
}
