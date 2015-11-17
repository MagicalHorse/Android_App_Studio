package com.shenma.yueba.yangjia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.activity.SalesManagerForBuyerActivity;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;

import java.util.List;

public class BrandSearchAdapter extends BaseAdapterWithUtil {
	private List<AttationAndFansItemBean> mList;

	public BrandSearchAdapter(Context ctx, List<AttationAndFansItemBean> mList) {
		super(ctx);
		this.ctx = ctx;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.brand_item_for_search,
					null);
			holder.tv_brand_name = (TextView) convertView
					.findViewById(R.id.tv_brand_name);
			holder.iv_brand_head = (ImageView) convertView
					.findViewById(R.id.iv_brand_head);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
//		initBitmap(ToolsUtil.nullToString(mList.get(position).getUserLogo()), holder.riv_head);
//		holder.tv_name.setText(ToolsUtil.nullToString(mList.get(position).getUserName()));
//		holder.tv_fans_count.setText("粉丝 "+ToolsUtil.nullToString(mList.get(position).getFansCount()));
//		holder.tv_atttention_count.setText("关注  "+ToolsUtil.nullToString(mList.get(position).getFavoiteCount()));
//		if(mList.get(position).isFavorite()){
//			holder.tv_has_attention.setVisibility(View.VISIBLE);
//			holder.tv_attention.setVisibility(View.GONE);
//		}else{
//			holder.tv_has_attention.setVisibility(View.GONE);
//			holder.tv_attention.setVisibility(View.VISIBLE);
//		}
		return convertView;
	}

	class Holder {
		ImageView iv_brand_head;
		TextView tv_brand_name;

	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
