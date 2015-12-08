package com.shenma.yueba.yangjia.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.modle.OrderDetailBackBean;
import com.shenma.yueba.yangjia.modle.OrderDetailBean;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * 
 * @author a
 * 
 */

public class OrderDetailActivity extends BaseActivityWithTopView {

	private TextView order_no_title;
	private TextView order_no_content;
	private TextView order_wating_title;
	private TextView order_wating_content;
	private TextView order_money_title;
	private TextView order_money_count;
	private TextView tv_shifu;
	private TextView order_commission_title;
	private TextView order_commission_count;
	private TextView order_date_title;
	private TextView order_date_count;
	private TextView customer_account_title;
	private TextView customer_account_content;
	private RoundImageView riv_customer_head;
	private TextView tv_customer_phone_title;
	private TextView tv_customer_phone_content;
	private TextView tv_get_address_title;
	private TextView tv_get_address_content;
	private TextView tv_connection;
	private String orderId;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_detail);
		super.onCreate(savedInstanceState);
		orderId = getIntent().getStringExtra("orderId");
		initView();
		getData();
	}

	private void initView() {
		setTitle("订单详情");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderDetailActivity.this.finish();
			}
		});
		order_no_title = (TextView) findViewById(R.id.order_no_title);
		order_no_content = (TextView) findViewById(R.id.order_no_content);
		order_wating_title = (TextView) findViewById(R.id.order_wating_title);
		order_wating_content = (TextView) findViewById(R.id.order_wating_content);
		order_money_title = (TextView) findViewById(R.id.order_money_title);
		order_money_count = (TextView) findViewById(R.id.order_money_count);
		tv_shifu = (TextView) findViewById(R.id.tv_shifu);
		order_commission_title = (TextView) findViewById(R.id.order_commission_title);
		order_commission_count = (TextView) findViewById(R.id.order_commission_count);
		order_date_title = (TextView) findViewById(R.id.order_date_title);
		order_date_count = (TextView) findViewById(R.id.order_date_count);
		customer_account_title = (TextView) findViewById(R.id.customer_account_title);
		customer_account_content = (TextView) findViewById(R.id.customer_account_content);
		riv_customer_head = (RoundImageView) findViewById(R.id.riv_customer_head);
		tv_customer_phone_title = (TextView) findViewById(R.id.tv_customer_phone_title);
		tv_customer_phone_content = (TextView) findViewById(R.id.tv_customer_phone_content);
		tv_get_address_title = (TextView) findViewById(R.id.tv_get_address_title);
		tv_get_address_content = (TextView) findViewById(R.id.tv_get_address_content);
		tv_connection = (TextView) findViewById(R.id.tv_connection);

		FontManager.changeFonts(mContext, order_no_title, order_no_content, order_wating_title, order_wating_content,
				order_money_title, order_money_count, tv_shifu, order_commission_title, order_commission_count,
				order_date_title, order_date_count, customer_account_title, customer_account_content, riv_customer_head,
				tv_customer_phone_title, tv_customer_phone_content, tv_get_address_title, tv_get_address_content,
				tv_connection);

	}

	/**
	 * 联网获取数据
	 *
	 *
	 */
	public void getData() {
		HttpControl hControl = new HttpControl();
		hControl.getOrderDetail(orderId, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				OrderDetailBackBean bean = (OrderDetailBackBean) obj;
				OrderDetailBean orderDetail = bean.getData();
				order_no_content.setText(ToolsUtil.nullToString(orderDetail.getOrderNo()));
				order_wating_content.setText(ToolsUtil.nullToString(orderDetail.getStatusName()));
				order_money_count.setText(ToolsUtil.nullToString(orderDetail.getRecAmount()));
				order_commission_count.setText(ToolsUtil.nullToString(orderDetail.getInCome()));
				order_date_count.setText(ToolsUtil.nullToString(orderDetail.getCreateTime()));
				customer_account_content.setText(ToolsUtil.nullToString(orderDetail.getCustomerName()));
				tv_customer_phone_content.setText(ToolsUtil.nullToString(orderDetail.getCustomerMobile()));
				phone = tv_customer_phone_content.getText().toString().trim();
				if(TextUtils.isEmpty(phone)){
					tv_connection.setVisibility(View.GONE);
				}else{
					tv_connection.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (TextUtils.isEmpty(phone)) {
								Toast.makeText(mContext, "暂无联系电话", Toast.LENGTH_SHORT).show();
							} else {
								ToolsUtil.callActivity(mContext, phone);
							}
						}
					});
				}

				tv_get_address_content.setText(ToolsUtil.nullToString(orderDetail.getCustomerAddress()));
				initBitmap(ToolsUtil.nullToString(orderDetail.getCustomerLogo()), riv_customer_head);
			}

			@Override
			public void http_Fails(int error, String msg) {

			}
		}, mContext);
	}

	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);
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

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
