package com.shenma.yueba.baijia.dialog;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.AffirmOrderActivity;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.ProductColorTypeBean;
import com.shenma.yueba.baijia.modle.ProductSPECbean;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.XCFlowLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-26 下午3:01:52  
 * 程序的简单说明   下单对话框
 */

public class CreateOrderDialog extends Dialog implements android.view.View.OnClickListener{
	Context context;
	RelativeLayout ll;
	EditText createorder_dialog_layout_countvalue_edittext;
	RequestCKProductDeatilsInfo bean;
	RequestCk_SPECDetails standard_data;
	RequestCk_SPECDetails requestCk_SPECDetails;
	CKProductDeatilsInfoBean productsDetailsInfoBean;
	Button create_dialog_jian_button,create_dialog_jia_button;
	//库存最大值
	int maxValue=0;
	//当前选中的尺寸对象
	ProductSPECbean currCheckedFouce=null;
	Button createorder_dialog_layout_submit_button;
	List<View> view_array=new ArrayList<View>();

	TextView createorder_dialog_layout_repertoryvalue_textview;
	List<ProductSPECbean> size_list=new ArrayList<ProductSPECbean>();
	XCFlowLayout flowlayout;
	
	public CreateOrderDialog(Context context,RequestCKProductDeatilsInfo bean,RequestCk_SPECDetails standard_data) {
		super(context,R.style.MyDialog);

		this.context=context;
		this.bean=bean;
		this.requestCk_SPECDetails=standard_data;
		setOwnerActivity((Activity)context);
		productsDetailsInfoBean=bean.getData();
		if(bean==null || productsDetailsInfoBean==null)
		{
			this.cancel();
			MyApplication.getInstance().showMessage(context, "数据为空");
		}
        //this.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.color_transparent));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ll=(RelativeLayout)RelativeLayout.inflate(context, R.layout.createorder_dialog_layout, null);
		setContentView(ll);
		initView();
	}
	
	void initView()
	{
		ToolsUtil.setFontStyle(context, ll, R.id.chat_product_head_layout_name_textview,R.id.chat_product_head_layout_price_textview,R.id.createorder_dialog_layout_color_textview,R.id.createorder_dialog_layout_colorvalue_textview,R.id.createorder_dialog_layout_size_textview,R.id.createorder_dialog_layout_count_textview,R.id.create_dialog_jian_button,R.id.createorder_dialog_layout_countvalue_edittext,R.id.create_dialog_jia_button,R.id.createorder_dialog_layout_cancell_button,R.id.createorder_dialog_layout_submit_button,R.id.createorder_dialog_layout_repertory_textview,R.id.createorder_dialog_layout_repertoryvalue_textview);
		//头像
		ImageView chat_product_head_layout_imageview=(ImageView)ll.findViewById(R.id.chat_product_head_layout_imageview);
		List<ProductsDetailsTagInfo> productsDetailsTagInfo_list=productsDetailsInfoBean.getProductPic();
		if(productsDetailsTagInfo_list!=null && productsDetailsTagInfo_list.size()>0)
		{
			initBitmap(ToolsUtil.getImage(ToolsUtil.nullToString(productsDetailsTagInfo_list.get(0).getLogo()), 320, 0), chat_product_head_layout_imageview);
		}
		//产品名称
		TextView chat_product_head_layout_name_textview=(TextView)ll.findViewById(R.id.chat_product_head_layout_name_textview);
		chat_product_head_layout_name_textview.setText(ToolsUtil.nullToString(productsDetailsInfoBean.getProductName()));
		//产品价格
		TextView chat_product_head_layout_price_textview=(TextView)ll.findViewById(R.id.chat_product_head_layout_price_textview);
		chat_product_head_layout_price_textview.setText("￥"+ToolsUtil.DounbleToString_2(productsDetailsInfoBean.getPrice()));
		//颜色类型
		TextView createorder_dialog_layout_colorvalue_textview=(TextView)ll.findViewById(R.id.createorder_dialog_layout_colorvalue_textview);
		//设置尺寸
		if(requestCk_SPECDetails.getData()!=null && requestCk_SPECDetails.getData().size()>0)
		{
			ProductColorTypeBean productColorTypeBean=requestCk_SPECDetails.getData().get(0);
			size_list.clear();
			if(productColorTypeBean!=null)
			{
				size_list.addAll(productColorTypeBean.getSize());
			}


		}
		flowlayout=(XCFlowLayout)ll.findViewById(R.id.flowlayout);
		//减
		create_dialog_jian_button=(Button)ll.findViewById(R.id.create_dialog_jian_button);
		create_dialog_jian_button.setOnClickListener(this);
		//加
		 create_dialog_jia_button=(Button)ll.findViewById(R.id.create_dialog_jia_button);
		create_dialog_jia_button.setOnClickListener(this);
		//提交
		createorder_dialog_layout_submit_button=(Button)ll.findViewById(R.id.createorder_dialog_layout_submit_button);
		createorder_dialog_layout_submit_button.setOnClickListener(this);
		//取消
		Button createorder_dialog_layout_cancell_button=(Button)ll.findViewById(R.id.createorder_dialog_layout_cancell_button);
		createorder_dialog_layout_cancell_button.setOnClickListener(this);
		//库存
		createorder_dialog_layout_repertoryvalue_textview=(TextView)ll.findViewById(R.id.createorder_dialog_layout_repertoryvalue_textview);
		
		//数量
		createorder_dialog_layout_countvalue_edittext=(EditText)ll.findViewById(R.id.createorder_dialog_layout_countvalue_edittext);
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
					createorder_dialog_layout_countvalue_edittext.setText(0+"");
					return;
				}
				int value=Integer.parseInt(s.toString());
				if(value<0)
				{
					createorder_dialog_layout_countvalue_edittext.setText(0+"");
					createorder_dialog_layout_submit_button.setEnabled(false);
				}else if(value>maxValue)
				{
					createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(maxValue));
					createorder_dialog_layout_submit_button.setEnabled(true);
				}
				if(s.toString().length()>1)//如果位数大于1位
				{
					char c=s.toString().charAt(0);
					if(c==0)
					{
						createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(value));
					}
				}
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

		addstandardButton();
	}
	
	@Override
	public void show() {
		
		super.show();
		WindowManager.LayoutParams param=this.getWindow().getAttributes();
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		param.width=dm.widthPixels;
	    //param.height=;
	    Log.i("TAG", "HEIGHT----->:"+dm.heightPixels);
		this.getWindow().setAttributes(param);
		this.getWindow().setBackgroundDrawableResource(R.color.color_transparent);
	}

	
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.create_dialog_jian_button://减
			if(currCheckedFouce==null)
			{
				MyApplication.getInstance().showMessage(context, "请先选择商品规格");
				return;
			}
			int value=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
			value--;
			if(value<=1)
			{
                value=1;
			}
			if(maxValue<=0)
			{
				value=0;
			}

			createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(value));
			isTextButtonEnable();
			break;
		case R.id.create_dialog_jia_button://加
			if(currCheckedFouce==null)
			{
				MyApplication.getInstance().showMessage(context, "请先选择商品规格");
				return;
			}
			int values=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString());
			values++;
			if(values>=maxValue)
			{
				values=maxValue;
			}
			createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(values));
			isTextButtonEnable();
			break;
		case R.id.createorder_dialog_layout_cancell_button://取消
			CreateOrderDialog.this.cancel();
			break;
		case R.id.createorder_dialog_layout_submit_button://提交
			int count=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
			if(count<=0)
			{
				//商品数量必须大于零
				MyApplication.getInstance().showMessage(context, "商品数量必须大于零");
				return;
			}else if(currCheckedFouce==null)
			{
				MyApplication.getInstance().showMessage(context, "请选择商品规格");
				return;
			}
			Intent intent=new Intent(context,AffirmOrderActivity.class);
			intent.putExtra("COUNT", count);//购买数量
			intent.putExtra("CHECKEDPrioductSizes", currCheckedFouce);//选择的规格尺寸
			intent.putExtra("DATA", bean);//商品信息
			context.startActivity(intent);
			CreateOrderDialog.this.cancel();
			break;
		
		}
	}
	
	
	/******
	 * 设置当前选中的尺寸样式
	 * ***/
	void setCheckFouse()
	{
		for(int i=0;i<view_array.size();i++)
		{
			view_array.get(i).setSelected(false);
		}
		//根据currCheckedFouce 值设置 库存组大值
		if(currCheckedFouce!=null)
		{
			int inventory=currCheckedFouce.getInventory();//库存
			if(inventory<0)
			{
				inventory=0;
			}
			
			maxValue=inventory;
			createorder_dialog_layout_repertoryvalue_textview.setText(Integer.toString(inventory));
			int currvalue=Integer.parseInt(createorder_dialog_layout_countvalue_edittext.getText().toString().trim());
			if(currvalue>maxValue)
			{
				createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(maxValue));
			}else if(currvalue==0 && maxValue>0)
			{
				createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(1));
			}else
			{
				createorder_dialog_layout_countvalue_edittext.setText(Integer.toString(currvalue));
			}
			
		}
		isTextButtonEnable();
	}
	
	/*********
	 * 设置 标签按钮
	 * ***/
	void addstandardButton()
	{
		for(int i=0;i<size_list.size();i++)
		{
			ProductSPECbean productSPECbean=size_list.get(i);
			TextView btn=new TextView(context);
			btn.setGravity(Gravity.CENTER);
			FontManager.changeFonts(context, btn);
			btn.setText(productSPECbean.getSizeName());
			btn.setTag(productSPECbean);
			btn.setTextSize(12);
			btn.setBackgroundResource(R.drawable.gridviewitem_background);
			MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 5, 5,5);
	        btn.setPadding(30, 13, 30, 13);
	        btn.setGravity(Gravity.CENTER);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setStyleValue(v);
				}
			});
			flowlayout.addView(btn, lp);
			if(!view_array.contains(btn))
			{
				view_array.add(btn);
			}
			setCheckFouse();
		}
		if(view_array.size()>0)
		{
			setStyleValue(view_array.get(0));
		}
	}
	
	/******
	 * 设置当前相中的规格
	 * ***/
	void setStyleValue(View v)
	{
		currCheckedFouce=(ProductSPECbean)v.getTag();
		setCheckFouse();
		v.setSelected(true);
	}
	
	
	/*****
	 * 根据库存 即 商品数量 设置 按钮颜色
	 * ***/
	void isTextButtonEnable()
	{
		create_dialog_jian_button.setSelected(true);
		create_dialog_jia_button.setSelected(true);
		//库存
		int Stock=Integer.valueOf(createorder_dialog_layout_repertoryvalue_textview.getText().toString().trim());
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
	
	void initBitmap(final String url, final ImageView iv)
	{
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
