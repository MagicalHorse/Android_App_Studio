package com.shenma.yueba.yangjia.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;

import java.util.List;

public class ProductSearchAdapter extends BaseAdapterWithUtil {
	private List<AttationAndFansItemBean> mList;

	public ProductSearchAdapter(Context ctx, List<AttationAndFansItemBean> mList) {
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
			convertView = View.inflate(ctx, R.layout.product_item_for_search,
					null);
			holder.iv_product = (ImageView) convertView
					.findViewById(R.id.iv_product);
			holder.tv_introduce = (TextView) convertView
					.findViewById(R.id.tv_introduce);
			holder.iv_collection = (ImageView) convertView
					.findViewById(R.id.iv_collection);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);

			holder.iv_collection.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		initBitmap(ToolsUtil.nullToString(mList.get(position).getUserLogo()), holder.iv_product);

		return convertView;
	}

	class Holder {
		ImageView iv_collection;
		ImageView iv_product;
		TextView tv_introduce;
		TextView tv_price;

	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
