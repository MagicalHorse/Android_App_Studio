package com.shenma.yueba.yangjia.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.SearchProductBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.fragment.MsgListFragment;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;

import java.util.List;

public class ProductSearchAdapter extends BaseAdapterWithUtil {
	private List<ProductsInfoBean> mList;
	RotateAnimation animation;
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
			holder.iv_collection = (ImageView) convertView
					.findViewById(R.id.iv_collection);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		initBitmap(ToolsUtil.nullToString(mList.get(position).getPic()), holder.iv_product);
		holder.tv_introduce.setText(mList.get(position).getProductName());
		holder.tv_price.setText("￥" + mList.get(position).getPrice());
		holder.iv_collection.setBackgroundResource(mList.get(position).isFavorite() ? R.drawable.collect : R.drawable.uncollect);
		holder.iv_collection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MyApplication.getInstance().isUserLogin(
						ctx)) {
					return;
				}
				setFavor(v,position, mList.get(position).getProductId(), mList.get(position).isFavorite());
			}
		});
		return convertView;
	}

	class Holder {
		ImageView iv_collection;
		ImageView iv_product;
		TextView tv_introduce;
		TextView tv_price;

	}


	/**
	 * 收藏或者取消收藏
	 * @param id
	 * @param isFavite
	 */
	private void setFavor(final View v,final int position,int id, final boolean isFavite){
		startAnimation((ImageView)v);
		HttpControl httpControl = new HttpControl();
		httpControl.setFavor(id + "", isFavite ? 0 : 1, new HttpControl.HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				if(isFavite){
					Toast.makeText(ctx, "取消收藏成功", Toast.LENGTH_SHORT).show();
					mList.get(position).setIsFavorite(false);
				}else{
					Toast.makeText(ctx, "收藏成功", Toast.LENGTH_SHORT).show();
					mList.get(position).setIsFavorite(true);
				}
				notifyDataSetChanged();
				stopAnimation(v);
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
				stopAnimation((ImageView)v);
			}
		}, ctx);
	}


	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}


	void startAnimation(View v)
	{
		if(animation==null)
		{
			animation = new RotateAnimation(360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setDuration(800);
			animation.setRepeatMode(Animation.RESTART);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setFillAfter(true);
		}
		v.startAnimation(animation);
	}

	void stopAnimation(View v)
	{
		if(animation!=null)
		{
			v.clearAnimation();
		}
	}
}
