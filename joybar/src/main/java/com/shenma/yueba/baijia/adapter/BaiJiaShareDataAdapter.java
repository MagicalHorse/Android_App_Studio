package com.shenma.yueba.baijia.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaShareInfoBean;
import com.shenma.yueba.util.ToolsUtil;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-2 下午7:26:13  
 * 程序的简单说明  
 */

public class BaiJiaShareDataAdapter extends BaseAdapter{
	int maxCount=3;
	List<BaiJiaShareInfoBean> bean_array=new ArrayList<BaiJiaShareInfoBean>();
	Context context;
	public BaiJiaShareDataAdapter(Context context,List<BaiJiaShareInfoBean> bean_array)
	{
		this.bean_array=bean_array;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		
		return bean_array.size();
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
			convertView=(LinearLayout)LinearLayout.inflate(context, R.layout.baijiashare_layout_item, null);
			ToolsUtil.setFontStyle(context, convertView, R.id.baijiashare_layout_item_names_textview,R.id.baijiashare_layout_item_prices_textview);
			holder.baijiashare_layout_item_heads_imageview=(ImageView)convertView.findViewById(R.id.baijiashare_layout_item_heads_imageview);
			holder.baijiashare_layout_item_icons_imageview=(ImageView)convertView.findViewById(R.id.baijiashare_layout_item_icons_imageview);
			holder.baijiashare_layout_item_names_textview=(TextView)convertView.findViewById(R.id.baijiashare_layout_item_names_textview);
			holder.baijiashare_layout_item_prices_textview=(TextView)convertView.findViewById(R.id.baijiashare_layout_item_prices_textview);
			holder.baijiashare_layout_item_parent_linearlayout=(LinearLayout)convertView.findViewById(R.id.baijiashare_layout_item_parent_linearlayout);
			holder.baijiashare_layout_item_parent_linearlayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					ImageView baijiashare_layout_item_heads_imageview=(ImageView)v.findViewById(R.id.baijiashare_layout_item_heads_imageview);
					if(baijiashare_layout_item_heads_imageview!=null && baijiashare_layout_item_heads_imageview.getTag()!=null)
					{
						BaiJiaShareInfoBean bean=(BaiJiaShareInfoBean)baijiashare_layout_item_heads_imageview.getTag();
						if(bean.isIscheck())
						{
							bean.setIscheck(false);
							baijiashare_layout_item_heads_imageview.setSelected(false);
						}else
						{
							//获取已经选中的个数
							if(getCheckCount()>=maxCount)
							{
								MyApplication.getInstance().showMessage(context, "一次最多分享"+maxCount+"个商品");
								return;
							}
							bean.setIscheck(true);
							baijiashare_layout_item_heads_imageview.setSelected(true);
						}
					}
				}
			});
			convertView.setTag(holder);
		}else
		{
			holder=(Holder)convertView.getTag();
		}
	    setValue(holder,position);
		return convertView;
	}

	class Holder
	{
		ImageView baijiashare_layout_item_heads_imageview;
		ImageView baijiashare_layout_item_icons_imageview;
		TextView baijiashare_layout_item_names_textview;
		TextView baijiashare_layout_item_prices_textview;
		LinearLayout baijiashare_layout_item_parent_linearlayout;
	}
	
	void setValue(Holder holder,int position)
	{
		BaiJiaShareInfoBean bean =bean_array.get(position);
		holder.baijiashare_layout_item_heads_imageview.setTag(bean);
		if(bean.isIscheck())
		{
			holder.baijiashare_layout_item_heads_imageview.setSelected(true);
		}else
		{
			holder.baijiashare_layout_item_heads_imageview.setSelected(false);
		}
		MyApplication.getInstance().getBitmapUtil().display(holder.baijiashare_layout_item_icons_imageview, ToolsUtil.getImage(ToolsUtil.nullToString(bean.getLogo()), 320, 0));
		holder.baijiashare_layout_item_names_textview.setText(ToolsUtil.nullToString(bean.getName()));
		//holder.baijiashare_layout_item_name_textview.setText("111");
		holder.baijiashare_layout_item_prices_textview.setText("￥"+Double.toString(bean.getPrice()));
	}
	
	int getCheckCount()
	{
		int checked=0;
		for(int i=0;i<bean_array.size();i++)
		{
			if(bean_array.get(i).isIscheck())
			{
				checked++;
			}
		}
		return checked;
	}
	
}
