package com.shenma.yueba.baijia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaOrderListInfo;
import com.shenma.yueba.baijia.modle.ProductInfoBean;
import com.shenma.yueba.util.ButtonManager;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author gyj
 * @version 创建时间：2015-6-2 下午1:10:24 程序的简单说明
 */

public class BaiJiaOrderListAdapter extends BaseAdapter {
	
	List<BaiJiaOrderListInfo> object_list = new ArrayList<BaiJiaOrderListInfo>();
	Context context;
    HttpControl httpControl=new HttpControl();
	public BaiJiaOrderListAdapter(List<BaiJiaOrderListInfo> object_list,Context context) {
		this.object_list = object_list;
		this.context = context;

	}

	@Override
	public int getCount() {

		return object_list.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if (arg1 == null) {
			holder = new Holder();
			arg1 = LinearLayout.inflate(context,
					R.layout.baijia_orderlist_layout_item, null);
			setFont(arg1);
			holder.baijia_orderlayout_item_nickname_textview = (TextView) arg1
					.findViewById(R.id.baijia_orderlayout_item_nickname_textview);
			holder.baijia_orderlayout_item_status_textview = (TextView) arg1
					.findViewById(R.id.baijia_orderlayout_item_status_textview);
			holder.affirmorder_item_icon_imageview = (ImageView) arg1
					.findViewById(R.id.affirmorder_item_icon_imageview);
			holder.affirmorder_item_productname_textview = (TextView) arg1
					.findViewById(R.id.affirmorder_item_productname_textview);
			holder.affirmorder_item_productsize_textview = (TextView) arg1
					.findViewById(R.id.affirmorder_item_productsize_textview);
			holder.affirmorder_item_productcount_textview = (TextView) arg1
					.findViewById(R.id.affirmorder_item_productcount_textview);
			holder.affirmorder_item_productprice_textview = (TextView) arg1
					.findViewById(R.id.affirmorder_item_productprice_textview);
			holder.orderlist_item_desc_textview = (TextView) arg1
					.findViewById(R.id.orderlist_item_desc_textview);
			holder.baijia_orderlayout_item_pricevalue_textview = (TextView) arg1
					.findViewById(R.id.baijia_orderlayout_item_pricevalue_textview);
			holder.baijia_orderlayout_item_price_textview = (TextView) arg1
					.findViewById(R.id.baijia_orderlayout_item_price_textview);
			holder.baijia_orderdetails_lianxibuyer_textview = (TextView) arg1
					.findViewById(R.id.baijia_orderdetails_lianxibuyer_textview);
			holder.baijia_orderlayout_item_price_textview.setText("实付:");
			holder.baijiaorder_layout_item_status=(LinearLayout)arg1.findViewById(R.id.baijiaorder_layout_item_status);
			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}
		setValue(arg0, holder);
		return arg1;
	}

	void setValue(int i, Holder holder) {
		BaiJiaOrderListInfo bean = object_list.get(i);
		
		String buyerName = "";// 买手昵称
		String orderStatus = "未知";// 订单状态
		String prodcutName = "";// 产品名称
		String productDesc = "";// 产品描述
		int procuctCount = 0;// 产品个数
		double productPrice = 0.0;// 产品价格
		String productAddress = "";// 地址
		int allCount = 0;// 总件数
		double allPrice = 0.0;// 总金额
		String productUrl = "";// 产品图片地址

		if(bean==null)
		{
			bean=new BaiJiaOrderListInfo();
			/*holder.baijia_orderlayout_item_nickname_textview.setText(ToolsUtil
					.nullToString(buyerName));
			holder.baijia_orderlayout_item_status_textview.setText(ToolsUtil
					.nullToString(orderStatus));
			holder.affirmorder_item_productname_textview.setText(ToolsUtil
					.nullToString(prodcutName));
			holder.affirmorder_item_productsize_textview.setText(ToolsUtil
					.nullToString(productDesc));
			holder.affirmorder_item_productcount_textview.setText("x"
					+ procuctCount);
			holder.affirmorder_item_productprice_textview.setText("x"
					+ productPrice);
			holder.baijia_orderdetails_lianxibuyer_textview.setText(ToolsUtil
					.nullToString(productAddress));
			holder.baijia_orderlayout_item_pricevalue_textview.setText(allPrice + "");
			holder.affirmorder_item_icon_imageview
					.setImageResource(R.drawable.default_pic);
			holder.affirmorder_item_icon_imageview.setTag(null);
			holder.orderlist_item_desc_textview.setText("总共"+bean.getOrderProductCount()+"件商品");
			initPic(holder.affirmorder_item_icon_imageview,ToolsUtil.getImage(productUrl, 320, 0));
			setButtonStatus(holder,bean);
			return;*/
		}
		
		
		
		
		ProductInfoBean productInfoBean = bean.getProduct();
		if (bean != null) {
			if (productInfoBean == null) {
				productInfoBean = new ProductInfoBean();
			}
			buyerName = bean.getBuyerName();
			orderStatus = bean.getOrderStatusStr();
			productAddress = bean.getAddress();
			allCount = bean.getOrderProductCount();
			allPrice = bean.getAmount();

			prodcutName = productInfoBean.getName();
			productDesc = productInfoBean.getProductdesc();
			procuctCount = productInfoBean.getProductCount();
			productPrice = productInfoBean.getPrice();
			productUrl = productInfoBean.getImage();

		}

		holder.baijia_orderlayout_item_nickname_textview.setText(ToolsUtil
				.nullToString(buyerName));
		holder.baijia_orderlayout_item_status_textview.setText(ToolsUtil
				.nullToString(orderStatus));
		holder.affirmorder_item_productname_textview.setText(ToolsUtil
				.nullToString(prodcutName));
		holder.affirmorder_item_productsize_textview.setText(ToolsUtil
				.nullToString(productDesc));
		holder.affirmorder_item_productcount_textview.setText("x"+ procuctCount);
		holder.affirmorder_item_productprice_textview.setText("￥"+ToolsUtil.DounbleToString_2( productPrice));
		holder.baijia_orderdetails_lianxibuyer_textview.setText(ToolsUtil
				.nullToString(productAddress));
		holder.baijia_orderlayout_item_pricevalue_textview.setText("￥"+ToolsUtil.DounbleToString_2(allPrice));
		holder.affirmorder_item_icon_imageview
				.setImageResource(R.drawable.default_pic);
		holder.affirmorder_item_icon_imageview.setTag(productInfoBean
				.getProductId());
		holder.orderlist_item_desc_textview.setText("总共"+bean.getOrderProductCount()+"件商品");
		initPic(holder.affirmorder_item_icon_imageview,ToolsUtil.getImage(productUrl, 320, 0));
		setButtonStatus(holder,bean);
	}

	class Holder {
		TextView baijia_orderlayout_item_nickname_textview;// 买手昵称
		TextView baijia_orderlayout_item_status_textview;// 货物状态
		ImageView affirmorder_item_icon_imageview;// 产品图片
		TextView affirmorder_item_productname_textview;// 产品名称
		TextView affirmorder_item_productsize_textview;// 产品尺寸规格
		TextView affirmorder_item_productcount_textview;// 产品数量
		TextView affirmorder_item_productprice_textview;// 产品单价
		TextView orderlist_item_desc_textview;// 产品件数
		TextView baijia_orderlayout_item_pricevalue_textview;// 产品总计
		TextView baijia_orderlayout_item_price_textview;// 实付提示
		TextView baijia_orderdetails_lianxibuyer_textview;// 地址
		LinearLayout baijiaorder_layout_item_status;//操作按钮的父类
	}

	/******
	 * 根据订单类型设置显示的按钮
	 * ***/
	void setButtonStatus(Holder holder,BaiJiaOrderListInfo bean) {
		holder.baijiaorder_layout_item_status.removeAllViews();
		List<View> view_list= ButtonManager.getButton((Activity)context, bean.getOrderStatus());
		if(view_list!=null)
		{
			for(int i=0;i<view_list.size();i++)
			{
				View button=view_list.get(i);
				Button btn=(Button)button.findViewById(R.id.baijia_orderdetails_sqtk_button);
				FontManager.changeFonts(context, btn);
				btn.setTag(bean);
				LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				param.leftMargin=10;
				holder.baijiaorder_layout_item_status.addView(button,param);
			}
		}
	}

	/****
	 * 设置字体
	 * **/
	void setFont(View v) {
		ToolsUtil.setFontStyle(context, v,
				R.id.baijia_orderdetails_lianxibuyer_textview,
				R.id.baijia_orderlayout_item_price_textview,
				R.id.baijia_orderlayout_item_nickname_textview,
				R.id.baijia_orderlayout_item_status_textview,
				R.id.affirmorder_item_productname_textview,
				R.id.affirmorder_item_productsize_textview,
				R.id.affirmorder_item_productcount_textview,
				R.id.affirmorder_item_productprice_textview,
				R.id.orderlist_item_desc_textview,
				R.id.baijia_orderlayout_item_pricevalue_textview);
	}

	OnClickListener onclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
		}
	};

	void initPic(ImageView iv, String url) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
