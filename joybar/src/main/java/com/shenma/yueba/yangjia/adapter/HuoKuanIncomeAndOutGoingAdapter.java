package com.shenma.yueba.yangjia.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.activity.HuoKuanIncomingAndOutgoingsActivity;
import com.shenma.yueba.yangjia.modle.HuoKuanItem;
import com.shenma.yueba.yangjia.modle.OrderItem;

public class HuoKuanIncomeAndOutGoingAdapter extends BaseAdapterWithUtil {
	private List<HuoKuanItem> mList;
	private int tag;// 标记不同的订单状态
	private double prices;

	public HuoKuanIncomeAndOutGoingAdapter(Context ctx,
			List<HuoKuanItem> mList, int tag) {
		super(ctx);
		this.mList = mList;
		this.tag = tag;
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

	public void clearCountList() {
		if (mList != null) {
			mList.clear();
		}
		prices = 0;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(ctx,
					R.layout.huokuan_income_and_outgoing_item, null);
			holder.tv_earning_money_title = (TextView) convertView
					.findViewById(R.id.tv_earning_money_title);
			holder.tv_money_number = (TextView) convertView
					.findViewById(R.id.tv_money_number);
			holder.tv_status = (TextView) convertView
					.findViewById(R.id.tv_status);
			holder.tv_order_number_title = (TextView) convertView
					.findViewById(R.id.tv_order_number_title);
			holder.tv_order_muber = (TextView) convertView
					.findViewById(R.id.tv_order_muber);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.iv_check = (ImageView) convertView
					.findViewById(R.id.iv_check);
			if (tag == 0) {// 可提现
				holder.iv_check.setVisibility(View.VISIBLE);
				holder.tv_status.setVisibility(View.GONE);
			} else {
				holder.iv_check.setVisibility(View.GONE);
				holder.tv_status.setVisibility(View.VISIBLE);
			}
			FontManager.changeFonts(ctx, holder.tv_earning_money_title,
					holder.tv_money_number, holder.tv_status,
					holder.tv_order_number_title, holder.tv_order_muber,
					holder.tv_date);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if (!mList.get(position).isChecked()) {
			holder.iv_check.setBackgroundResource(R.drawable.radio_normal);
		} else {
			holder.iv_check.setBackgroundResource(R.drawable.radio_selected);
		}
		holder.iv_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(ctx,
				// Double.parseDouble(mList.get(position).getAmount())+"",
				// 1000).show();
				if (mList.get(position).isChecked()) {
					mList.get(position).setChecked(false);
					((HuoKuanIncomingAndOutgoingsActivity) ctx).removeIds(mList
							.get(position).getOrderNo());
					prices = prices
							- mList.get(position)
									.getAmount();
					if (prices > 0) {
						((HuoKuanIncomingAndOutgoingsActivity) ctx)
								.setHuoKuanCount("提现货款"
										+ ToolsUtil.DounbleToString_2(prices)
										+ "元",prices);
					}
					if(prices == 0){
						((HuoKuanIncomingAndOutgoingsActivity) ctx)
						.setHuoKuanCount("提现货款",prices);
					}
				} else {
					mList.get(position).setChecked(true);
					((HuoKuanIncomingAndOutgoingsActivity) ctx).setIds(mList
							.get(position).getOrderNo());
					prices = prices
							+ mList.get(position)
									.getAmount();
					if (prices > 0) {
						((HuoKuanIncomingAndOutgoingsActivity) ctx)
								.setHuoKuanCount("提现货款"
										+ ToolsUtil.DounbleToString_2(prices)
										+ "元",prices);
					}
					if(prices == 0){
						((HuoKuanIncomingAndOutgoingsActivity) ctx)
						.setHuoKuanCount("提现货款",prices);
					}
				}
				notifyDataSetChanged();
			}
		});

		holder.tv_money_number.setText("￥"
				+ ToolsUtil.DounbleToString_2(mList.get(position).getAmount()));
		holder.tv_status.setText(ToolsUtil.nullToString(mList.get(position)
				.getStatusName()));
		holder.tv_order_muber.setText(ToolsUtil.nullToString(mList
				.get(position).getOrderNo()));
		holder.tv_date.setText(ToolsUtil.nullToString(mList.get(position)
				.getCreateDate()));
		return convertView;
	}

	class Holder {
		TextView tv_earning_money_title;//
		TextView tv_order_status;// 订单状态
		TextView tv_date;// 商品描述
		TextView tv_order_muber;// 商品个数
		TextView tv_order_number_title;// 货号
		TextView tv_money_number;// 规格
		TextView tv_status;// 规格
		ImageView iv_check;
	}

}
