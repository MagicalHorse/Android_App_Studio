package com.shenma.yueba.baijia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaOrderListInfo;
import com.shenma.yueba.baijia.modle.ProductInfoBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.OrderChangeRefreshObserver;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-24 下午6:59:13  
 * 程序的简单说明  申请退款
 */

public class ApplyForRefundActivity extends BaseActivityWithTopView implements OnClickListener{
View parentView;
ImageView affirmorder_item_icon_imageview;//商品图片
TextView  affirmorder_item_productname_textview;//商品名称
TextView  affirmorder_item_productsize_textview;//商品颜色规格
TextView  affirmorder_item_productcount_textview;//商品数量
TextView  affirmorder_item_productprice_textview;//商品价格
TextView  applyforrefund_layout_refundprivevalue_textview;//退款金额
Button  create_dialog_jian_button;//减数量
Button  create_dialog_jia_button;//加数量
Button  applyforrefund_layout_footersubmit_button;//申诉按钮
EditText createorder_dialog_layout_countvalue_edittext;//数量
EditText applyforrefund_layout_appealreason_textview;//申诉理由
BaiJiaOrderListInfo baiJiaOrderListInfo;//订单信息

int maxValue=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		parentView=RelativeLayout.inflate(this, R.layout.applyforrefund_layout, null);
		setContentView(parentView);
		super.onCreate(savedInstanceState);
		if(this.getIntent().getSerializableExtra("DATA")==null || !(this.getIntent().getSerializableExtra("DATA") instanceof BaiJiaOrderListInfo))
		{
			MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, "数据错误，请重试");
			finish();
			return;
		}else
		{
			baiJiaOrderListInfo=(BaiJiaOrderListInfo)this.getIntent().getSerializableExtra("DATA");
		}
		initView();
	}
	
	void initView()
	{
		setTitle("申请退款");
		FontManager.changeFonts(ApplyForRefundActivity.this, tv_top_title);
		setLeftTextView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		affirmorder_item_icon_imageview=(ImageView)parentView.findViewById(R.id.affirmorder_item_icon_imageview);
		affirmorder_item_icon_imageview.setOnClickListener(this);
		affirmorder_item_productname_textview=(TextView)parentView.findViewById(R.id.affirmorder_item_productname_textview);
		affirmorder_item_productsize_textview=(TextView)parentView.findViewById(R.id.affirmorder_item_productsize_textview);
		affirmorder_item_productcount_textview=(TextView)parentView.findViewById(R.id.affirmorder_item_productcount_textview);
		affirmorder_item_productprice_textview=(TextView)parentView.findViewById(R.id.affirmorder_item_productprice_textview);
		applyforrefund_layout_refundprivevalue_textview=(TextView)parentView.findViewById(R.id.applyforrefund_layout_refundprivevalue_textview);
		create_dialog_jian_button=(Button)parentView.findViewById(R.id.create_dialog_jian_button);
		create_dialog_jian_button.setVisibility(View.GONE);
		create_dialog_jian_button.setOnClickListener(this);
		create_dialog_jia_button=(Button)parentView.findViewById(R.id.create_dialog_jia_button);
		create_dialog_jia_button.setVisibility(View.GONE);
		create_dialog_jia_button.setOnClickListener(this);
		applyforrefund_layout_footersubmit_button=(Button)parentView.findViewById(R.id.applyforrefund_layout_footersubmit_button);
		applyforrefund_layout_footersubmit_button.setOnClickListener(this);
		createorder_dialog_layout_countvalue_edittext=(EditText)parentView.findViewById(R.id.createorder_dialog_layout_countvalue_edittext);
		createorder_dialog_layout_countvalue_edittext.setEnabled(false);
		createorder_dialog_layout_countvalue_edittext.setFocusable(false);
		createorder_dialog_layout_countvalue_edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
			}
		});
		applyforrefund_layout_appealreason_textview=(EditText)parentView.findViewById(R.id.applyforrefund_layout_appealreason_textview);
		ToolsUtil.setFontStyle(this, parentView, R.id.affirmorder_item_productname_textview,R.id.affirmorder_item_productsize_textview,R.id.affirmorder_item_productcount_textview,R.id.affirmorder_item_productprice_textview,R.id.applyforrefund_layout_server_textview,R.id.applyforrefund_layout_servervalue_textview,R.id.applyforrefund_layout_refundprive_textview,R.id.applyforrefund_layout_refundprivevalue_textview,R.id.applyforrefund_layout_count_textview,R.id.create_dialog_jian_button,R.id.createorder_dialog_layout_countvalue_edittext,R.id.create_dialog_jia_button,R.id.applyforrefund_layout_appealreason_textview,R.id.applyforrefund_layout_footersubmit_button);
		ProductInfoBean productInfoBean=baiJiaOrderListInfo.getProduct();
		MyApplication.getInstance().getBitmapUtil().display(affirmorder_item_icon_imageview, ToolsUtil.getImage(ToolsUtil.nullToString(productInfoBean.getImage()), 320, 0));
		affirmorder_item_productname_textview.setText(ToolsUtil.nullToString(productInfoBean.getName()));
		affirmorder_item_productsize_textview.setText(ToolsUtil.nullToString(productInfoBean.getProductdesc()));
		affirmorder_item_productcount_textview.setText("x"+Integer.toString(productInfoBean.getProductCount()));
		maxValue=baiJiaOrderListInfo.getOrderProductCount();
		createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(baiJiaOrderListInfo.getOrderProductCount()));
		affirmorder_item_productprice_textview.setText("￥"+Double.toString(productInfoBean.getPrice()));
		applyforrefund_layout_refundprivevalue_textview.setText("￥"+Double.toString(baiJiaOrderListInfo.getAmount()));
		
        createorder_dialog_layout_countvalue_edittext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CharSequence text = createorder_dialog_layout_countvalue_edittext.getText();				
				if (text instanceof Spannable) {
					Spannable spanText = (Spannable)text;	
					Selection.setSelection(spanText, text.length());				
					}
				if(s.toString().equals(""))
				{
					createorder_dialog_layout_countvalue_edittext.setText(1+"");
					return;
				}
				int value=Integer.parseInt(s.toString());
				if(value<0)
				{
					createorder_dialog_layout_countvalue_edittext.setText(1+"");
				}else if(value>maxValue)
				{
					createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(maxValue));
				}
				calculateRefundPrice(Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString().trim()));
				isTextButtonEnable();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.create_dialog_jian_button://减
			int value=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
			value--;
			if(value<=1)
			{
				value=1;
			}
			//计算 退款金额
			calculateRefundPrice(value);
			createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(value));
			break;
		case R.id.create_dialog_jia_button://加
			int values=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
			values++;
			if(values>=maxValue)
			{
				values=maxValue;
			}
			createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(values));
			break;
		case R.id.applyforrefund_layout_footersubmit_button://申请按钮
			submit();
			break;
		case R.id.affirmorder_item_icon_imageview:
			ToolsUtil.forwardProductInfoActivity(ApplyForRefundActivity.this, baiJiaOrderListInfo.getProduct().getProductId());
			break;
		}
	}
	
	/****
	 * 提交申请
	 * **/
	void submit()
	{
		int refundcount=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
		if(refundcount<=0)
		{
			MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, "退货数量必须大于0");
			return;
		}else if(refundcount>baiJiaOrderListInfo.getOrderProductCount())
		{
			MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, "退货数量必须小于购买总数");
			return;
		}
		//退款理由
		String refundReason=applyforrefund_layout_appealreason_textview.getText().toString().trim();
		if(refundReason.length()<3)
		{
			MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, "请输入退货说明 （3-200个文字）");
			return;
		}
		HttpControl httpControl=new HttpControl();
		httpControl.applyForRefund(baiJiaOrderListInfo.getOrderNo(), refundcount, refundReason, true, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				if(obj==null)
				{
					http_Fails(500, "申请失败");
				}else
				{
					MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, "申请成功");
					//setResult(200, ApplyForRefundActivity.this.getIntent().putExtra("PAYRESULT", "SUCESS"));
					OrderChangeRefreshObserver.getInstance().refreshList(0,null);
					finish();
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(ApplyForRefundActivity.this, msg);
			}
		}, ApplyForRefundActivity.this);
	}
	
	
	
	/*****
	 * 计算 退款金额
	 * @param count int 商品退订个数
	 * **/
	void calculateRefundPrice(int count)
	{
		//总价格
		double allprice=baiJiaOrderListInfo.getAmount();
		//平均价格
		double avgprice=allprice/baiJiaOrderListInfo.getOrderProductCount();
		double refundprice=avgprice*count;
		String str=ToolsUtil.DounbleToString_2(refundprice);
		applyforrefund_layout_refundprivevalue_textview.setText("￥"+str);
		if(count==baiJiaOrderListInfo.getOrderProductCount())
		{
			applyforrefund_layout_refundprivevalue_textview.setText(Double.toString(baiJiaOrderListInfo.getAmount()));
		}
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
	
	/*****
	 * 根据库存 即 商品数量 设置 按钮颜色
	 * ***/
	void isTextButtonEnable()
	{
		create_dialog_jian_button.setSelected(true);
		create_dialog_jia_button.setSelected(true);
		//库存
		int Stock=maxValue;
		//当前选择的购买数量
		int count=Integer.valueOf(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
		if(Stock<=1)
		{
			create_dialog_jian_button.setSelected(false);
			create_dialog_jia_button.setSelected(false);
		}else if(count<=1)
		{
			create_dialog_jian_button.setSelected(false);
		}else if(count>=Stock)
		{
			create_dialog_jia_button.setSelected(false);
		}
		
	}
}
