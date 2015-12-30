package com.shenma.yueba.baijia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.baijia.modle.BaijiaOrderDetailsInfo;
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
List<BaijiaOrderDetailsInfo> obj_list=new ArrayList<BaijiaOrderDetailsInfo>();
	public BaijiaOrderDetailsAdapter(Context context,List<BaijiaOrderDetailsInfo> obj_list)
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
					if(v.getTag()!=null && v.getTag() instanceof BaijiaOrderDetailsInfo)
					{
						BaijiaOrderDetailsInfo bean=(BaijiaOrderDetailsInfo)v.getTag();
					   ToolsUtil.forwardProductInfoActivity(context,bean.getProductId());
					}
				}
			});
			convertView.setTag(holder);
			FontManager.changeFonts(context,holder.affirmorder_item_productname_textview,holder.affirmorder_item_productsize_textview,holder.affirmorder_item_productcount_textview,holder.affirmorder_item_productprice_textview);
		}else
		{
			holder=(Holder)convertView.getTag();
		}
		setValue(holder, position);
		return convertView;
	}

 class	Holder 
 {
	 ImageView affirmorder_item_icon_imageview;//商品图片
	 TextView affirmorder_item_productname_textview;//商品名称
	 TextView affirmorder_item_productsize_textview;//尺寸大小等信息
	 TextView affirmorder_item_productcount_textview;//购买数量
	 TextView affirmorder_item_productprice_textview;//单价
 }
 
 void setValue(Holder holder,int position)
 {
	 BaijiaOrderDetailsInfo infobean= obj_list.get(position);
	 holder.affirmorder_item_icon_imageview.setTag(infobean);
	 MyApplication.getInstance().getBitmapUtil().display(holder.affirmorder_item_icon_imageview, ToolsUtil.getImage(ToolsUtil.nullToString(infobean.getProductPic()), 320, 0));
	 holder.affirmorder_item_productname_textview.setText(ToolsUtil.nullToString(infobean.getProductName()));
	 holder.affirmorder_item_productsize_textview.setText(ToolsUtil.nullToString(infobean.getSizeName()+"："+infobean.getSizeValue()));
	 holder.affirmorder_item_productcount_textview.setText(ToolsUtil.nullToString("x"+infobean.getProductCount()));
	 holder.affirmorder_item_productprice_textview.setText(ToolsUtil.nullToString("￥"+ToolsUtil.DounbleToString_2(infobean.getPrice())));
 }

}
