package com.shenma.yueba.yangjia.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.SearchProductBackBean;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.fragment.MsgListFragment;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;

import java.util.List;

public class ProductSearchAdapter extends BaseAdapterWithUtil {
	private List<ProductsInfoBean> mList;

	public ProductSearchAdapter(Context ctx, List<ProductsInfoBean> mList) {
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
			holder.cb_collection = (CheckBox) convertView
					.findViewById(R.id.cb_collection);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		initBitmap(ToolsUtil.nullToString(mList.get(position).getPic()), holder.iv_product);
		holder.tv_introduce.setText(mList.get(position).getProductName());
		holder.tv_price.setText(""+mList.get(position).getPrice());
		holder.cb_collection.setChecked(mList.get(position).isFavite());
		return convertView;
	}

	class Holder {
		CheckBox cb_collection;
		ImageView iv_product;
		TextView tv_introduce;
		TextView tv_price;

	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
