package com.shenma.yueba.yangjia.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.IncomeDetailItem;

public class IncomeDetailAdapter extends BaseAdapterWithUtil {
	private List<IncomeDetailItem> mList;
	private int tag;
	public IncomeDetailAdapter(Context ctx,List<IncomeDetailItem> mList) {
		super(ctx);
		this.mList = mList;
	}
	public IncomeDetailAdapter(Context ctx,List<IncomeDetailItem> mList,int tag) {
		super(ctx);
		this.mList = mList;
		tag = tag;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.income_detail_item, null);
			holder.tv_earning_money_title = (TextView) convertView.findViewById(R.id.tv_earning_money_title);
			holder.tv_earning_money = (TextView) convertView.findViewById(R.id.tv_earning_money);
			holder.tv_from = (TextView) convertView.findViewById(R.id.tv_from);
			holder.tv_order_money_title = (TextView) convertView.findViewById(R.id.tv_order_money_title);
			holder.tv_money_number = (TextView) convertView.findViewById(R.id.tv_money_number);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_order_number_title = (TextView) convertView.findViewById(R.id.tv_order_number_title);
			holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			FontManager.changeFonts(ctx, holder.tv_earning_money_title,holder.tv_earning_money,holder.tv_from,
					holder.tv_order_money_title,holder.tv_money_number,holder.tv_date,
					holder.tv_order_number_title,holder.tv_order_number,holder.tv_status);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.tv_earning_money.setText("￥"+ToolsUtil.DounbleToString_2(mList.get(position).getIncome_amount()));
		holder.tv_from.setText("1".equals(ToolsUtil.nullToString(mList.get(position).getOrder_type()))?"来源：奖励":"来源：订单");
		holder.tv_money_number.setText("￥"+ToolsUtil.DounbleToString_2(mList.get(position).getAmount()));
		holder.tv_date.setText(ToolsUtil.nullToString(mList.get(position).getCreate_date()));
		holder.tv_order_number.setText(ToolsUtil.nullToString(mList.get(position).getOrder_no()));
		holder.tv_status.setText(ToolsUtil.nullToString(mList.get(position).getOrderstatus_s()));
		
		return convertView;
	}
	
	
	class Holder{
		TextView tv_earning_money_title;
		TextView tv_earning_money;
		TextView tv_from;
		TextView tv_order_money_title;
		TextView tv_money_number;
		TextView tv_date;
		TextView tv_order_number_title;
		TextView tv_order_number;
		TextView tv_status;
	}

}
