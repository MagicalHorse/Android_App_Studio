package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.dialog.OrderPayDialog;
import com.shenma.yueba.baijia.dialog.OrderPayDialog.OrderPayOnClick_Listener;
import com.shenma.yueba.baijia.modle.BaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.baijia.modle.BaijiaPayInfoBean;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;
import com.shenma.yueba.baijia.modle.RequestBaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.OrderChangeRefreshObserver;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.wxapi.CreateWeiXinOrderManager;
import com.shenma.yueba.wxapi.WeiXinBasePayManager.WeiXinPayManagerListener;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author gyj
 * @version 创建时间：2015-6-16 上午10:03:01 程序的简单说明
 */

public class BaijiaPayActivity extends BaseActivityWithTopView implements
		OrderPayOnClick_Listener {
	private View parentView;
	private RelativeLayout rl_wechatpay;
	private List<BaijiaPayInfoBean> bean = 
			new ArrayList<BaijiaPayInfoBean>();
	private TextView baijiapay_layout_item_textview1;
	private TextView baijiapay_layout_item_textview2;
	private TextView baijiapay_layout_item_textview3;
	private TextView baijiapay_layout_item_textview4;
	private PayResponseFormBean payResponseFormBean;
	private OrderPayDialog orderPayDialog;
	private HttpControl httpControl = new HttpControl();
	private boolean isBroadcast = false;// 是否注册广播监听 支付结果
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		parentView = this.getLayoutInflater().inflate(R.layout.baijiapaylayout,null);
		setContentView(parentView);
		super.onCreate(savedInstanceState);
		if (this.getIntent().getSerializableExtra("PAYDATA") != null) {
			payResponseFormBean = (PayResponseFormBean) this.getIntent().getSerializableExtra("PAYDATA");
		} else {
			finish();
			MyApplication.getInstance().showMessage(BaijiaPayActivity.this,"数据错误，请从订单页面进入");
			return;
		}
		
		initView();
	}

	void initView() {
		setTitle("选择付款方式");
		FontManager.changeFonts(this, tv_top_title);
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaijiaPayActivity.this.finish();
			}
		});
		
		
		baijiapay_layout_item_textview1 = (TextView) findViewById(R.id.baijiapay_layout_item_textview1);
		baijiapay_layout_item_textview1.setText("请支付");
		baijiapay_layout_item_textview2 = (TextView) findViewById(R.id.baijiapay_layout_item_textview2);
		baijiapay_layout_item_textview2.setText("￥"+ToolsUtil.DounbleToString_2(payResponseFormBean.getPrice()));
		baijiapay_layout_item_textview3 = (TextView) findViewById(R.id.baijiapay_layout_item_textview3);
		baijiapay_layout_item_textview4 = (TextView) findViewById(R.id.baijiapay_layout_item_textview4);
		rl_wechatpay = getView(R.id.rl_wechatpay);
		FontManager.changeFonts(mContext, baijiapay_layout_item_textview1,baijiapay_layout_item_textview2,baijiapay_layout_item_textview3,baijiapay_layout_item_textview4);
		
		rl_wechatpay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 注册监听 支付结果
				registerBroadcast();
				// 启动微信支付
				startWenXinPay();
				
			}
		});
	}

	/****
	 * 广播 接受 支付结果 用于关闭页面及 后台查询确认支付结果
	 * ***/
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			} else if (intent.getAction().equals(CreateWeiXinOrderManager.WEIXINACTION_FILTER))// 如果接收到 支付的广播
			{
				String resutl_Code = intent.getStringExtra("Resutl_Code");
				// MyApplication.getInstance().showMessage(BaijiaPayActivity.this,
				// "接受到广播 Resutl_Code："+resutl_Code);
				queyPayStatus();
				/*
				 * if(resutl_Code==null) { return; }else
				 * if(resutl_Code.equals("SUCESS")) { //与服务器通信 查询 后台支付状态
				 * queyPayStatus(); }
				 */
				unRegisterBroadcast();
			}
		}
	};

     /*****
      * 查询订单状态
      * *****/
	void queyPayStatus() {
		showPayDialogLoading();
		httpControl.getBaijiaOrderDetails(payResponseFormBean.getOrderNo(),
				true, new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						if (obj == null) {
							http_Fails(500, "");
						} else if (obj instanceof RequestBaiJiaOrdeDetailsInfoBean
								&& ((RequestBaiJiaOrdeDetailsInfoBean) obj)
										.getData() != null) {
							RequestBaiJiaOrdeDetailsInfoBean orderDetailBackBean = (RequestBaiJiaOrdeDetailsInfoBean) obj;
							BaiJiaOrdeDetailsInfoBean baiJiaOrdeDetailsInfoBean = orderDetailBackBean
									.getData();
							// MyApplication.getInstance().showMessage(BaijiaPayActivity.this,
							// "查询到订单数据 status:"+baiJiaOrdeDetailsInfoBean.getOrderStatus());
							if (baiJiaOrdeDetailsInfoBean.getOrderStatus() == 1)// 如果当前订单状态是
																				// 已付款
							{
								showSucessDialog();
							} else {
								showFailsDailog();
							}
							OrderChangeRefreshObserver.getInstance().refreshList(0,null);
                           //setResult(200, BaijiaPayActivity.this.getIntent().putExtra("PAYRESULT", "SUCESS"));
						} else {
							http_Fails(500, "");
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						showFailsDailog();
						MyApplication.getInstance().showMessage(BaijiaPayActivity.this, "查询失败，请在订单页面查看订单状态");
					}
				}, BaijiaPayActivity.this);
	}

	/***
	 * 支付注册广播监听
	 * ***/
	void registerBroadcast() {
		if (!isBroadcast) {
			IntentFilter filter = new IntentFilter(
					CreateWeiXinOrderManager.WEIXINACTION_FILTER);
			BaijiaPayActivity.this.registerReceiver(broadcastReceiver, filter);
			isBroadcast = true;
		}
	}

	/***
	 * 支付注销广播监听
	 * ***/
	void unRegisterBroadcast() {
		if (isBroadcast) {
			BaijiaPayActivity.this.unregisterReceiver(broadcastReceiver);
			isBroadcast = false;
		}

	}

	/*******
	 * 启动微信支付
	 * ***/
	void startWenXinPay() {
		CreateWeiXinOrderManager cwxm = new CreateWeiXinOrderManager(BaijiaPayActivity.this, new WeiXinPayManagerListener() {

					@Override
					public void success(Object obj) {

					}

					@Override
					public void fails(String msg) {
						MyApplication.getInstance().showMessage(BaijiaPayActivity.this, msg);
					}
				}, payResponseFormBean);
		cwxm.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);// 加入回退栈
		unRegisterBroadcast();
	}

	/***
	 * 显示查询中
	 * **/
	public void showPayDialogLoading() {
		if (orderPayDialog == null) {
			orderPayDialog = new OrderPayDialog(payResponseFormBean,BaijiaPayActivity.this, this,true);
		}
		orderPayDialog.showLoading();

	}

	/***
	 * 显示查询成功（即支付成功）
	 * **/
	public void showSucessDialog() {
		if (orderPayDialog == null) {
			orderPayDialog = new OrderPayDialog(payResponseFormBean,BaijiaPayActivity.this, this,true);
		}
		orderPayDialog.showSucess();

	}

	/***
	 * 显示查询失败
	 * **/
	public void showFailsDailog() {
		if (orderPayDialog == null) {
			orderPayDialog = new OrderPayDialog(payResponseFormBean,BaijiaPayActivity.this, this,true);
		}
		orderPayDialog.showFails();

	}

	public void cancelPayDialog() {
		if (orderPayDialog != null) {
			orderPayDialog.dismiss();
		}
	}

	@Override
	public void on_Click() {
		cancelPayDialog();
		finish();
	}

	@Override
	public void finish() {
		super.finish();
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
