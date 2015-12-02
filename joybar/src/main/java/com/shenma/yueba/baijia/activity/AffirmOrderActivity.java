package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.AffirmAdapter;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;
import com.shenma.yueba.baijia.modle.PrioductSizesInfoBean;
import com.shenma.yueba.baijia.modle.ProductSPECbean;
import com.shenma.yueba.baijia.modle.ProductsDetailsPromotion;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestComputeAmountInfoBean;
import com.shenma.yueba.baijia.modle.RequestCreatOrderInfoBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ListViewUtils;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-6-1 上午10:19:39 程序的简单说明
 */

public class AffirmOrderActivity extends BaseActivityWithTopView implements
		OnClickListener {


	// 头像
	RoundImageView affirmorder_layout_icon_roundimageview;
	// 拨打电话
	ImageView affirmorder_layout_icon_imageview;
	View parentview;
	ListView affirmorder_layout_product_listview;
	// 提货人手机号
	TextView affirmorder_item_tihuophonevalue_textview;
	// 买手号码
	TextView affirmorder_layout_novalue_textview;
	// 买手电话
	TextView affirmorder_layout_phonevalue_textview;
	// 提货地址
	TextView affirmorder_layout_pickupvalue_textview;
	// 商品个数
	TextView affirmorder_item_allcount_textview;
	// 实际付款
	TextView affirmorder_item_pricevalue_textview;
	// 打烊
	TextView affirmorder_item_youhuicontext_textview;
	// 打烊购
	RelativeLayout affirmorder_item_youhui_linearlayout;
	// 提交
	Button affrimorder_layout_footer_sumit_button;
	HttpControl httpControl = new HttpControl();
	List<CKProductDeatilsInfoBean> productlist = new ArrayList<CKProductDeatilsInfoBean>();
	CKProductDeatilsInfoBean productsDetailsInfoBean;
	// 优惠信息
	ProductsDetailsPromotion productsDetailsPromotion;

	int buyCount = -1;// 购买数量
	ProductSPECbean currCheckedFouce = null;// 选择的尺寸数据
	RequestComputeAmountInfoBean requestComputeAmountInfoBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		parentview = this.getLayoutInflater().inflate(
				R.layout.affirmorder_layout, null);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(parentview);
		super.onCreate(savedInstanceState);
		if (this.getIntent().getSerializableExtra("DATA") != null
				&& this.getIntent().getSerializableExtra("DATA") instanceof RequestCKProductDeatilsInfo) {
			RequestCKProductDeatilsInfo bean = (RequestCKProductDeatilsInfo) this
					.getIntent().getSerializableExtra("DATA");
			productsDetailsInfoBean = bean.getData();
			productlist.add(productsDetailsInfoBean);
			buyCount = this.getIntent().getIntExtra("COUNT", -1);
			if (buyCount <= 0
					|| this.getIntent().getSerializableExtra(
							"CHECKEDPrioductSizes") == null) {
				MyApplication.getInstance().showMessage(this, "数据错误");
				finish();
				return;
			}
			currCheckedFouce = (ProductSPECbean) this.getIntent().getSerializableExtra("CHECKEDPrioductSizes");
			if (productsDetailsInfoBean != null) {
				initView();

			} else {
				MyApplication.getInstance().showMessage(this, "数据错误");
				finish();
				return;
			}

		} else {
			MyApplication.getInstance().showMessage(this, "数据错误");
			finish();
			return;
		}

		getBaijiaOrderPrice(Integer.valueOf(productsDetailsInfoBean.getProductId()), buyCount);
	}

	void initView() {
		setTitle("确认订单");
		setLeftTextView(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ToolsUtil.setFontStyle(AffirmOrderActivity.this, parentview,
				R.id.affirmorder_item_youhuititle_textview,
				R.id.affirmorder_item_youhuicontext_textview);
		// 打烊购
		affirmorder_item_youhui_linearlayout = (RelativeLayout) findViewById(R.id.affirmorder_item_youhui_linearlayout);
		// 买手号码
		affirmorder_layout_novalue_textview = (TextView) findViewById(R.id.affirmorder_layout_novalue_textview);
		// 买手电话
		affirmorder_layout_phonevalue_textview = (TextView) findViewById(R.id.affirmorder_layout_phonevalue_textview);
		affirmorder_layout_phonevalue_textview.setOnClickListener(this);
		// 提货地址
		affirmorder_layout_pickupvalue_textview = (TextView) findViewById(R.id.affirmorder_layout_pickupvalue_textview);
		// 商品总个数
		affirmorder_item_allcount_textview = (TextView) findViewById(R.id.affirmorder_item_allcount_textview);
		// 实际付款
		affirmorder_item_pricevalue_textview = (TextView) findViewById(R.id.affirmorder_item_pricevalue_textview);
		// 打烊
		affirmorder_item_youhuicontext_textview = (TextView) findViewById(R.id.affirmorder_item_youhuicontext_textview);

		// 提货电话
		affirmorder_item_tihuophonevalue_textview = (TextView) findViewById(R.id.affirmorder_item_tihuophonevalue_textview);
		affirmorder_layout_icon_roundimageview = (RoundImageView) findViewById(R.id.affirmorder_layout_icon_roundimageview);
		affirmorder_layout_icon_roundimageview.setTag(Integer.valueOf(productsDetailsInfoBean.getBuyerId()));
		affirmorder_layout_icon_roundimageview.setOnClickListener(onClickListener);
		MyApplication.getInstance().getBitmapUtil().display(affirmorder_layout_icon_roundimageview, ToolsUtil.nullToString(productsDetailsInfoBean.getBuyerLogo()));

		affirmorder_layout_icon_imageview = (ImageView) findViewById(R.id.affirmorder_layout_icon_imageview);
		// 设置联系电话
		affirmorder_layout_icon_imageview.setTag(ToolsUtil
				.nullToString(productsDetailsInfoBean.getBuyerMobile()));
		// 电话
		affirmorder_layout_icon_imageview.setOnClickListener(this);

		affirmorder_layout_product_listview = (ListView) findViewById(R.id.affirmorder_layout_product_listview);
		AffirmAdapter affirmAdapter = new AffirmAdapter(this, productlist,currCheckedFouce, buyCount);
		affirmorder_layout_product_listview.setAdapter(affirmAdapter);
		ListViewUtils
				.setListViewHeightBasedOnChildren(affirmorder_layout_product_listview);

		ToolsUtil.setFontStyle(this, parentview, R.id.tv_top_title, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_no_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_phone_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_pickup_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affrimorder_layout_footer_countpricename_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affrimorder_layout_footer_sumit_button, null);

		// 需要负值的
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_novalue_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_phonevalue_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_pickupvalue_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_item_allcount_textview, null);
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_item_price_textview, null);
		double allPrice = 0.00;
		allPrice = buyCount * productsDetailsInfoBean.getPrice();
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_item_pricevalue_textview, null);

		// 确定
		affrimorder_layout_footer_sumit_button = (Button) parentview
				.findViewById(R.id.affrimorder_layout_footer_sumit_button);
		affrimorder_layout_footer_sumit_button
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (affirmorder_item_tihuophonevalue_textview.getText()
								.toString().trim().equals("")) {
							MyApplication.getInstance().showMessage(
									AffirmOrderActivity.this, "请输入提货人手机号");
							return;
						}
						submitData();
					}
				});
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affrimorder_layout_footer_countprice_textview, null);

		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_item_tihuophone_textview,
				R.id.affirmorder_item_tihuophonevalue_textview,
				R.id.affirmorder_item_tihuophonetitle_textview);
		productsDetailsPromotion = productsDetailsInfoBean.getPromotion();

	}

	/******
	 * 提交 确认商品的数据
	 * ***/
	void submitData() {
		String phone = affirmorder_item_tihuophonevalue_textview.getText()
				.toString().trim();
		httpControl.createProductOrder(phone,Integer.valueOf(productsDetailsInfoBean.getProductId()), buyCount,
				Integer.valueOf(currCheckedFouce.getSizeId()), true,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						if (obj != null
								&& obj instanceof RequestCreatOrderInfoBean) {
							RequestCreatOrderInfoBean requestCreatOrderInfoBean = (RequestCreatOrderInfoBean) obj;
							if (requestCreatOrderInfoBean.getData() != null) {
								MyApplication.getInstance().showMessage(AffirmOrderActivity.this, "下单成功");
								ToolsUtil.frowardPayActivity(AffirmOrderActivity.this,productsDetailsInfoBean.getProductName(),buyCount,requestCreatOrderInfoBean.getData().getOrderNo(),requestCreatOrderInfoBean.getData().getActualAmount());
								ToolsUtil.sendOrderBroadcase();
							}
							finish();
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						MyApplication.getInstance().showMessage(
								AffirmOrderActivity.this, msg);
					}
				}, AffirmOrderActivity.this);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.affirmorder_layout_icon_roundimageview:
				if (v.getTag() != null && v.getTag() instanceof Integer) {
					ToolsUtil.forwardShopMainActivity(mContext,(Integer) v.getTag());
				}

				break;
			}
		}
	};

	/*****
	 * 获取订单实际金额
	 * **/
	void getBaijiaOrderPrice(int ProductId, int Quantity) {
		httpControl.getBaijiaOrderPrice(ProductId, Quantity, true,
				new HttpCallBackInterface() {
					@Override
					public void http_Success(Object obj) {
						if (obj == null
								|| !(obj instanceof RequestComputeAmountInfoBean)
								|| ((RequestComputeAmountInfoBean) obj)
										.getData() == null) {
							http_Fails(500, "获取数据失败");
						} else {
							requestComputeAmountInfoBean = (RequestComputeAmountInfoBean) obj;
							setValue();
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						MyApplication.getInstance().showMessage(
								AffirmOrderActivity.this, msg);
						AffirmOrderActivity.this.finish();
					}
				}, AffirmOrderActivity.this);
	}

	/*****
	 * 负值
	 * ****/
	void setValue() {
		// 需要负值的
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_novalue_textview,
				productsDetailsInfoBean.getBuyerName());
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_layout_phonevalue_textview,
				productsDetailsInfoBean.getBuyerMobile());
		ToolsUtil
				.setFontStyle(this, parentview,
						R.id.affirmorder_layout_pickupvalue_textview, ToolsUtil
								.nullToString(productsDetailsInfoBean
										.getPickAddress()));
		ToolsUtil
				.setFontStyle(this, parentview,
						R.id.affirmorder_item_allcount_textview, "共" + buyCount
								+ "件商品");
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affirmorder_item_pricevalue_textview, "￥"+Double
						.toString(requestComputeAmountInfoBean.getData()
								.getTotalamount()));
		ToolsUtil.setFontStyle(this, parentview,
				R.id.affrimorder_layout_footer_countprice_textview, ToolsUtil
						.nullToString(Double
								.toString(requestComputeAmountInfoBean
										.getData().getSaletotalamount())));
		if (productsDetailsPromotion != null) {
			// 设置优惠名称
			ToolsUtil.setFontStyle(this, parentview,
					R.id.affirmorder_item_youhuititle_textview,
					ToolsUtil.nullToString(productsDetailsPromotion.getName()));

			if (requestComputeAmountInfoBean.getData() != null) {
				if (requestComputeAmountInfoBean.getData().getDiscountamount() > 0) {
					affirmorder_item_youhui_linearlayout
							.setVisibility(View.VISIBLE);
				}

				// 优惠的金额
				ToolsUtil.setFontStyle(
						this,
						parentview,
						R.id.affirmorder_item_youhuicontext_textview,
						ToolsUtil.nullToString("立减:￥"
								+ Double.toString(requestComputeAmountInfoBean
										.getData().getDiscountamount())));

			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.affirmorder_layout_phonevalue_textview:// 买的电话
		case R.id.affirmorder_layout_icon_imageview:
			String phoneNo = (String) v.getTag();
			if (!TextUtils.isEmpty(phoneNo)) {
				ToolsUtil.callActivity(AffirmOrderActivity.this, phoneNo);
			} else {
				Toast.makeText(mContext, "该买手暂无联系电话", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);// 加入回退栈
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
