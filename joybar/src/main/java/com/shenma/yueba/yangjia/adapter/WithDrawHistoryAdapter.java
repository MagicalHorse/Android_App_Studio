package com.shenma.yueba.yangjia.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.IncomeDetailListBean;
import com.shenma.yueba.yangjia.modle.IncomeHistoryItem;

public class WithDrawHistoryAdapter extends BaseAdapterWithUtil {
	private List<IncomeHistoryItem> mList;
	private int tag;
	public WithDrawHistoryAdapter(Context ctx,List<IncomeHistoryItem> mList) {
		super(ctx);
		this.mList = mList;
	}
	public WithDrawHistoryAdapter(Context ctx,List<IncomeHistoryItem> mList,int tag) {
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
			convertView = View.inflate(ctx, R.layout.withdraw_history_item, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.tv_money.setText("￥"+ToolsUtil.DounbleToString_2(mList.get(position).getAmount()));
		holder.tv_date.setText(ToolsUtil.nullToString(mList.get(position).getCreate_date()));
		return convertView;
	}
	
	
	class Holder{
		TextView tv_date;
		TextView tv_money;
	}

}
