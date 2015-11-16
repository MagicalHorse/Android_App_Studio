package com.shenma.yueba.yangjia.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.BaseAdapterWithUtil;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.util.FontManager;

import java.util.List;

public class CityListAdapter extends BaseAdapterWithUtil {
	private List<CityListItembean> mList;
	public CityListAdapter(Context ctx,List<CityListItembean> mList) {
		super(ctx);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.city_list_item, null);
			holder.tv_city_name = (TextView) convertView.findViewById(R.id.tv_city_name);
			FontManager.changeFonts(ctx, holder.tv_city_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.tv_city_name.setText(mList.get(position).getName());
		return convertView;
	}
	
	
	class Holder{
		TextView tv_city_name;
	}

}
