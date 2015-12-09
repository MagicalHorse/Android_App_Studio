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
import com.shenma.yueba.baijia.modle.StoreItem;
import com.shenma.yueba.baijia.modle.newmodel.SearchMarketBean;
import com.shenma.yueba.baijia.modle.newmodel.StoreIndexItem;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.activity.SalesManagerForBuyerActivity;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;

import java.util.List;

import config.PerferneceConfig;

public class MarketForSearchAdapter extends BaseAdapterWithUtil {
	private List<StoreItem> mList;

	public MarketForSearchAdapter(Context ctx, List<StoreItem> mList) {
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
			convertView = View.inflate(ctx, R.layout.market_item_for_search,
					null);
			holder.iv_market_head = (ImageView) convertView
					.findViewById(R.id.iv_market_head);

			holder.tv_shop_name = (TextView) convertView
					.findViewById(R.id.tv_shop_name);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			convertView.setTag(holder);
				} else {
			holder = (Holder) convertView.getTag();
		}
		bitmapUtils.display(holder.iv_market_head,mList.get(position).getStoreLogo());
		holder.tv_shop_name.setText(mList.get(position).getStoreName());
		holder.tv_address.setText(mList.get(position).getStoreLocation());
		holder.tv_distance.setText(""+ToolsUtil.Distance(mList.get(position).getLat(),mList.get(position).getLon(),Double.valueOf(PerferneceUtil.getString(PerferneceConfig.LATITUDE)),Double.valueOf(PerferneceUtil.getString(PerferneceConfig.LONGITUDE))));
		return convertView;
	}

	class Holder {
		ImageView iv_market_head;
		TextView tv_shop_name;
		TextView tv_address;
		TextView tv_distance;

	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
