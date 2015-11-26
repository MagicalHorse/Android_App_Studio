package com.shenma.yueba.baijia.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.util.ToolsUtil;

public class BrandAdapter extends BaseAdapterWithUtil {
	private List<BrandInfo> items;
	Context context;
	public BrandAdapter(Context ctx,List<BrandInfo> items) {
		super(ctx);
		this.items = items;
		this.context=ctx;
	}

	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.brand_list_item, null);
			holder.brandlist_item_imageview1 = (ImageView) convertView.findViewById(R.id.brandlist_item_imageview1);
			holder.brandlist_itemcolor_imageview = (ImageView) convertView.findViewById(R.id.brandlist_itemcolor_imageview);
			holder.brandlist_itemname_textview = (TextView) convertView.findViewById(R.id.brandlist_itemname_textview);
			ToolsUtil.setFontStyle(ctx, convertView, R.id.brandlist_itemname_textview);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels/2;
		if(width>0)
		{
			Log.i("TAG", "--->>WIDTH:"+width);
			int height=3*width/4;
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
			
			holder.brandlist_item_imageview1.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			//holder.brandlist_itemcolor_imageview.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			Log.i("TAG", "--->>WIDTH:"+width+"  height:"+height);
		}
		BrandInfo brandInfo=items.get(position);
		initPic(holder.brandlist_item_imageview1, ToolsUtil.nullToString(brandInfo.getPic()));
		holder.brandlist_itemname_textview.setText(ToolsUtil.nullToString(brandInfo.getBrandName()));
		return convertView;
	}
	
	
	class Holder{
		ImageView brandlist_item_imageview1;
		ImageView brandlist_itemcolor_imageview;
		TextView brandlist_itemname_textview;
		
	}
	
	void initPic(ImageView iv,String url)
	{
		if(iv!=null)
		{
			iv.setVisibility(View.VISIBLE);
			MyApplication.getInstance().getBitmapUtil().display(iv, url);
		}
	}

	
	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			
			}
		}
	};
}
