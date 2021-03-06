package com.shenma.yueba.baijia.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.UsersInfoBean;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * @author gyj
 * @version 创建时间：2015-5-28 下午4:33:31 程序的简单说明 用户头像列表适配器（横排最多显示8个数据）
 */

public class UserIconAdapter extends BaseAdapter {
	int maxCount = 8;
	List<UsersInfoBean> bean = new ArrayList<UsersInfoBean>();
	Context context;
    GridView gridbview;
	public UserIconAdapter(List<UsersInfoBean> bean, Context context,GridView gridbview) {
		if(bean!=null)
		{
			this.bean = bean;
			
		}
		this.context = context;
		this.gridbview=gridbview;
	}

	@Override
	public int getCount() {
		if (bean.size() >= maxCount) {
			return maxCount;
		} else {
			return bean.size();
		}

	}

	@Override
	public Object getItem(int position) {

		return bean.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UsersInfoBean usersInfoBean = bean.get(position);
		RoundImageView riv = new RoundImageView(context);
		riv.setTag(usersInfoBean.getUserId());
		int width=gridbview.getWidth();
		width=width/maxCount;
		Log.i("TAG", "WIDTH:"+width);
		if (position == (maxCount - 1)) {
			riv.setImageResource(R.drawable.test003);
		} else {
			riv.setImageResource(R.drawable.default_pic);
		}
		riv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getTag()==null)
				{
					return;
				}
				ToolsUtil.forwardShopMainActivity(context,(Integer)v.getTag());
			}
		});
		return riv;
	}
}
