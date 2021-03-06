package com.shenma.yueba.baijia.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.MyCircleInfo;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

public class BaiJiaMyCircleAdapter extends BaseAdapterWithUtil {
	BitmapUtils bu;
	private List<MyCircleInfo> items;

	public BaiJiaMyCircleAdapter(Context ctx, List<MyCircleInfo> items) {
		super(ctx);
		this.items = items;
		bu=new BitmapUtils(ctx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.my_circle_item, null);
			holder.riv_head = (RoundImageView) convertView.findViewById(R.id.riv_head);
			holder.tv_msg_count = (TextView) convertView
					.findViewById(R.id.tv_msg_count);
			holder.tv_product_name = (TextView) convertView
					.findViewById(R.id.tv_product_name);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
			convertView.setTag(holder);
			ToolsUtil.setFontStyle(ctx, convertView, R.id.tv_product_name,R.id.tv_msg,R.id.tv_time,R.id.tv_msg_count);
		} else {
			holder = (Holder) convertView.getTag();
		}
		MyCircleInfo myCircleInfo = items.get(position);
		MyApplication.getInstance().getBitmapUtil().display(holder.riv_head, ToolsUtil.nullToString(myCircleInfo.getLogo()));
		
		holder.tv_product_name.setText(ToolsUtil.nullToString(myCircleInfo.getName()));
		int count=myCircleInfo.getUnReadCount();
		if(count>0)
		{
			holder.tv_msg_count.setText(Integer.toString(count));
			holder.tv_msg_count.setVisibility(View.VISIBLE);
		}else
		{
			holder.tv_msg_count.setText(Integer.toString(count));
			holder.tv_msg_count.setVisibility(View.GONE);
		}
		
		holder.tv_time.setText(myCircleInfo.getUnReadLastTime());
		holder.tv_msg.setText(myCircleInfo.getUnReadMessage());
		
		return convertView;
	}

	class Holder {
		RoundImageView riv_head;
		TextView tv_msg_count;
		TextView tv_product_name;
		TextView tv_msg;
		TextView tv_time;

	}

}
