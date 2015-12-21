package com.shenma.yueba.baijia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.baijia.modle.OrderPromotions;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-1 上午11:22:39  
 * 程序的简单说明   定义 订单确认 的商品列表适配器
 */

public class BaijiaOrderDetailsAdapter extends BaseAdapter{
Context context;
List<BaiJiaOrdeDetailsInfoBean> obj_list=new ArrayList<BaiJiaOrdeDetailsInfoBean>();
	public BaijiaOrderDetailsAdapter(Context context,List<BaiJiaOrdeDetailsInfoBean> obj_list)
	{
		this.context=context;
		this.obj_list=obj_list;
	}
	
	@Override
	public int getCount() {
		
		return obj_list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView==null)
		{
			holder=new Holder();
			convertView=(LinearLayout)LinearLayout.inflate(context, R.layout.baijiaorderdetails_item, null);
			holder.affirmorder_item_productname_textview=(TextView)convertView.findViewById(R.id.affirmorder_item_productname_textview);
			holder.affirmorder_item_productsize_textview=(TextView)convertView.findViewById(R.id.affirmorder_item_productsize_textview);
			holder.affirmorder_item_productcount_textview=(TextView)convertView.findViewById(R.id.affirmorder_item_productcount_textview);
			holder.affirmorder_item_productprice_textview=(TextView)convertView.findViewById(R.id.affirmorder_item_productprice_textview);
			holder.affirmorder_item_icon_imageview=(ImageView)convertView.findViewById(R.id.affirmorder_item_icon_imageview);
			holder.affirmorder_item_icon_imageview.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v.getTag()!=null && v.getTag() instanceof BaiJiaOrdeDetailsInfoBean)
					{
					   BaiJiaOrdeDetailsInfoBean bean=(BaiJiaOrdeDetailsInfoBean)v.getTag();
					   ToolsUtil.forwardProductInfoActivity(context,bean.getProductId());
					}
				}
			});
			holder.productinfolist_layout_huodong_linearlayout=(LinearLayout)convertView.findViewById(R.id.productinfolist_layout_huodong_linearlayout);
			
			convertView.setTag(holder);
			FontManager.changeFonts(context,holder.affirmorder_item_productname_textview,holder.affirmorder_item_productsize_textview,holder.affirmorder_item_productcount_textview,holder.affirmorder_item_productprice_textview);
		}else
		{
			holder=(Holder)convertView.getTag();
		}
		setValue(holder,position);
		return convertView;
	}

 class	Holder 
 {
	 LinearLayout productinfolist_layout_huodong_linearlayout;//活动
	 ImageView affirmorder_item_icon_imageview;//商品图片
	 TextView affirmorder_item_productname_textview;//商品名称
	 TextView affirmorder_item_productsize_textview;//尺寸大小等信息
	 TextView affirmorder_item_productcount_textview;//购买数量
	 TextView affirmorder_item_productprice_textview;//单价
 }
 
 void setValue(Holder holder,int position)
 {
	 holder.productinfolist_layout_huodong_linearlayout.removeAllViews();
	 BaiJiaOrdeDetailsInfoBean baiJiaOrdeDetailsInfoBean= obj_list.get(position);
	 holder.affirmorder_item_icon_imageview.setTag(baiJiaOrdeDetailsInfoBean);
	 MyApplication.getInstance().getBitmapUtil().display(holder.affirmorder_item_icon_imageview, ToolsUtil.getImage(ToolsUtil.nullToString(baiJiaOrdeDetailsInfoBean.getProductPic()), 320, 0));
	 holder.affirmorder_item_productname_textview.setText(ToolsUtil.nullToString(baiJiaOrdeDetailsInfoBean.getProductName()));
	 holder.affirmorder_item_productsize_textview.setText(ToolsUtil.nullToString(baiJiaOrdeDetailsInfoBean.getSizeName()+"："+baiJiaOrdeDetailsInfoBean.getSizeValue()));
	 holder.affirmorder_item_productcount_textview.setText(ToolsUtil.nullToString("x"+baiJiaOrdeDetailsInfoBean.getProductCount()));
	 holder.affirmorder_item_productprice_textview.setText(ToolsUtil.nullToString("￥"+ToolsUtil.DounbleToString_2(baiJiaOrdeDetailsInfoBean.getPrice())));
	 List<OrderPromotions> promotion_array=baiJiaOrdeDetailsInfoBean.getPromotions();
	 
	 if(promotion_array!=null)
	 {
		 addHuoDong(holder.productinfolist_layout_huodong_linearlayout,promotion_array);
	 }
 }
 
 
 void addHuoDong(LinearLayout ll,List<OrderPromotions> huodonglist)
 {
	 for(int i=0;i<huodonglist.size();i++)
	 {
		 View v=gethuoDongView();
		 TextView tv=(TextView)v.findViewById(R.id.buyersteetfragment_item_footer_linearlyout_content_textview);
		 tv.setText(ToolsUtil.nullToString(huodonglist.get(i).getPromotionName() +" 立减 "+huodonglist.get(i).getAmount()+"元"));
		 LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		 params.topMargin=5;
		 ll.addView(v,params);
	 }
 }
 
 
 View gethuoDongView()
 {
	 LinearLayout huodong=(LinearLayout)LinearLayout.inflate(context, R.layout.huodong_layout, null);
	 return huodong;
 }
 
}
