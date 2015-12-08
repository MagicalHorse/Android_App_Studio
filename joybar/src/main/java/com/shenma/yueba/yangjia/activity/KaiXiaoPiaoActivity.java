package com.shenma.yueba.yangjia.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.dialog.QRCodeShareDialog;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.KaiXiaoPiaoBackBean;
import com.shenma.yueba.yangjia.modle.kaixiaoPiaoBean;
import com.umeng.analytics.MobclickAgent;

/**
 * 开小票
 * 
 * @author a
 */

public class KaiXiaoPiaoActivity extends BaseActivityWithTopView implements
		OnClickListener {
	private EditText et_product_number;
	private EditText et_product_price;
	private TextView tv_tishi;
	private TextView tv_facetoface_product;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);// 加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kaixiaopiao_layout);
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		ToolsUtil.hideSoftInputKeyBoard(this);//隐藏软键盘
		setTitle("开小票");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KaiXiaoPiaoActivity.this.finish();
			}
		});
		setTopRightTextView("创建", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_product_number.getText().toString())) {
					Toast.makeText(mContext, "货号不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(et_product_price.getText().toString())) {
					Toast.makeText(mContext, "售价不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!ToolsUtil.isDecimal(et_product_price.getText().toString())) {
					Toast.makeText(mContext, "售价不合法", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!ToolsUtil.isPointTwo(et_product_price.getText().toString())){
					Toast.makeText(mContext, "售价小数点后最多两位", Toast.LENGTH_SHORT).show();
					return;
				}
				checkCreateGeneralOrder();
			}
		});
		et_product_number = (EditText) findViewById(R.id.et_product_number);
		et_product_price = (EditText) findViewById(R.id.et_product_price);
		tv_tishi = (TextView) findViewById(R.id.tv_tishi);
		tv_facetoface_product = (TextView) findViewById(R.id.tv_facetoface_product);
		tv_facetoface_product.setOnClickListener(this);
		FontManager.changeFonts(this, tv_top_title,et_product_number, et_product_price,
				tv_tishi,tv_facetoface_product);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_facetoface_product:// 创建面对面小票
			if (TextUtils.isEmpty(et_product_number.getText().toString())) {
				Toast.makeText(mContext, "货号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(et_product_price.getText().toString())) {
				Toast.makeText(mContext, "售价不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!ToolsUtil.isDecimal(et_product_price.getText().toString())) {
				Toast.makeText(mContext, "售价不合法", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!ToolsUtil.isPointTwo(et_product_price.getText().toString())){
				Toast.makeText(mContext, "售价小数点后最多两位", Toast.LENGTH_SHORT).show();
				return;
			}
			checkCreateGeneralOrder();
			break;

		default:
			break;
		}

	}
	
	
	
	/**
	 * 判断是否可以开小票
	 */
	private void checkCreateGeneralOrder() {
		HttpControl httpcontrol = new HttpControl();
		httpcontrol.checkCreateGeneralOrder(new HttpCallBackInterface() {
					@Override
					public void http_Success(Object obj) {
						BaseRequest back = (BaseRequest) obj;
						if(back!=null && back.isSuccessful()){
							KaiXiaoPiao();
						}else{
							Toast.makeText(mContext, "当前时间不可以开小票", Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

					}
				}, mContext, true);
	}
	
	
	/**
	 * 开小票
	 */
	private void KaiXiaoPiao() {
		HttpControl httpcontrol = new HttpControl();
		httpcontrol.createGeneralOrder(et_product_price.getText().toString()
				.trim(), et_product_number.getText().toString().trim(),
				new HttpCallBackInterface() {
					@Override
					public void http_Success(Object obj) {
						KaiXiaoPiaoBackBean back = (KaiXiaoPiaoBackBean) obj;
						kaixiaoPiaoBean bean = back.getData();
						bean.setProductName("商品名："+bean.getProductName());
						QRCodeShareDialog dialog = new QRCodeShareDialog(mContext, bean);
						dialog.show();
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

					}
				}, mContext, true);
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
}
