package com.shenma.yueba.baijia.adapter;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BrandCityWideInfo;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SameCityAdapter extends BaseAdapterWithUtil {
	private List<BrandCityWideInfo> items;
	HttpControl httpControl =new HttpControl();
	Context ctx;
	PullToRefreshListView pull_refresh_list;
	public SameCityAdapter(Context ctx,List<BrandCityWideInfo> items,PullToRefreshListView pull_refresh_list) {
		super(ctx);
		this.ctx=ctx;
		this.items = items;
		this.pull_refresh_list=pull_refresh_list;
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

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			holder = new Holder();
			convertView = View.inflate(ctx, R.layout.same_city_list_item, null);
			ToolsUtil.setFontStyle(ctx, convertView, R.id.tv_nick_name,R.id.tv_belong,R.id.tv_attention);
			holder.iv_head = (RoundImageView) convertView.findViewById(R.id.iv_head);
			holder.iv_head.setOnClickListener(onClickListener);
			holder.nick_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
			holder.tv_belong = (TextView) convertView.findViewById(R.id.tv_belong);
			holder.tv_attention = (TextView) convertView.findViewById(R.id.tv_attention);
			holder.brandlist_item_imageview1_linearlayout=(LinearLayout)convertView.findViewById(R.id.brandlist_item_imageview1_linearlayout);
			holder.brandlist_item_imageview2_linearlayout=(LinearLayout)convertView.findViewById(R.id.brandlist_item_imageview2_linearlayout);
			holder.brandlist_item_imageview3_linearlayout=(LinearLayout)convertView.findViewById(R.id.brandlist_item_imageview3_linearlayout);
			holder.brandlist_item_imageview1=(ImageView)convertView.findViewById(R.id.brandlist_item_imageview1);
			holder.brandlist_item_imageview2=(ImageView)convertView.findViewById(R.id.brandlist_item_imageview2);
			holder.brandlist_item_imageview3=(ImageView)convertView.findViewById(R.id.brandlist_item_imageview3);
		    /*DisplayMetrics dm=new DisplayMetrics();
			((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
			int imgwidth=dm.widthPixels/3;
			if(imgwidth>0)
			{
				int imhheight=imgwidth/2;
				Log.i("TAG", "imhheight="+imhheight);
				LayoutParams  layoutParams =(LayoutParams) holder.brandlist_item_imageview1.getLayoutParams();
				layoutParams.height=imhheight;
				holder.brandlist_item_imageview1.setLayoutParams(layoutParams);
				holder.brandlist_item_imageview2.setLayoutParams(layoutParams);
				holder.brandlist_item_imageview3.setLayoutParams(layoutParams);
			}*/
			holder.tv_attention.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!MyApplication.getInstance().isUserLogin(ctx))
					{
						return;
					}
					if(v.getTag()!=null)
					{
						BrandCityWideInfo brandCityWideInfo=(BrandCityWideInfo)v.getTag();
						if(brandCityWideInfo.isIsFavorite())//如果是已关注
						{
							//取消关注
							submitAttention(0,brandCityWideInfo);
						}else
						{
							//添加关注
							submitAttention(1,brandCityWideInfo);
						}
					}
				}
			});
			
			
			holder.brandlist_item_imageview1.setOnClickListener(onClickListener);
			holder.brandlist_item_imageview2.setOnClickListener(onClickListener);
			holder.brandlist_item_imageview3.setOnClickListener(onClickListener);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.brandlist_item_imageview1_linearlayout.setVisibility(View.INVISIBLE);
		holder.brandlist_item_imageview2_linearlayout.setVisibility(View.INVISIBLE);
		holder.brandlist_item_imageview3_linearlayout.setVisibility(View.INVISIBLE);
		
		BrandCityWideInfo brandCityWideInfo=items.get(position);
		holder.iv_head.setTag(brandCityWideInfo.getUserId());
		initBitmap(ToolsUtil.nullToString(brandCityWideInfo.getBuyerLogo()), holder.iv_head);
		holder.nick_name.setText(ToolsUtil.nullToString(brandCityWideInfo.getUserName()));
		holder.tv_belong.setText(ToolsUtil.nullToString(brandCityWideInfo.getAddress()));
		holder.tv_attention.setTag(brandCityWideInfo);
		if(brandCityWideInfo.isIsFavorite())//如果已经关注
		{
			holder.tv_attention.setText("取消关注");
		}else
		{
			holder.tv_attention.setText("关注");
		}
		
		String[] pic_array=brandCityWideInfo.getPic();
		holder.brandlist_item_imageview1.setImageResource(R.drawable.default_pic);
		holder.brandlist_item_imageview2.setImageResource(R.drawable.default_pic);
		holder.brandlist_item_imageview3.setImageResource(R.drawable.default_pic);
		holder.brandlist_item_imageview1.setTag(brandCityWideInfo.getProductId());
		holder.brandlist_item_imageview2.setTag(brandCityWideInfo.getProductId());
		holder.brandlist_item_imageview3.setTag(brandCityWideInfo.getProductId());
		if(pic_array!=null)
		{
			for(int i=0;i<pic_array.length;i++)
			{
			  switch(i)
			  {
			  case 0:
				  holder.brandlist_item_imageview1_linearlayout.setVisibility(View.VISIBLE);
				  initBitmap(ToolsUtil.getImage(ToolsUtil.nullToString(pic_array[0]), 320, 0), holder.brandlist_item_imageview1);
				  break;
			  case 1:
				  holder.brandlist_item_imageview2_linearlayout.setVisibility(View.VISIBLE);
				  initBitmap(ToolsUtil.getImage(ToolsUtil.nullToString(pic_array[1]), 320, 0), holder.brandlist_item_imageview2);
				  break;
			  case 2:
				  holder.brandlist_item_imageview3_linearlayout.setVisibility(View.VISIBLE);
				  initBitmap(ToolsUtil.getImage(ToolsUtil.nullToString(pic_array[2]), 320, 0), holder.brandlist_item_imageview3);
				  break;
			  }
			}
		}
		return convertView;
	}
	
	
	class Holder{
		RoundImageView iv_head;
		TextView nick_name;
		TextView tv_belong;
		TextView tv_attention;
		LinearLayout brandlist_item_imageview1_linearlayout,brandlist_item_imageview2_linearlayout,brandlist_item_imageview3_linearlayout;
		ImageView brandlist_item_imageview1,brandlist_item_imageview2,brandlist_item_imageview3;
		
	}

	
	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.brandlist_item_imageview1:
			case R.id.brandlist_item_imageview2:
			case R.id.brandlist_item_imageview3:
				ToolsUtil.forwardProductInfoActivity(ctx,(Integer)v.getTag());
				break;
			case R.id.iv_head://头像
			    if(v.getTag()==null)
			    {
			    	return;
			    }
			    ToolsUtil.forwardShopMainActivity(ctx,(Integer)v.getTag());
			}
		}
	};
	
	/****
	 * 提交收藏与取消收藏商品
	 0表示取消收藏   1表示收藏
	 * @param brandCityWideInfo BrandCityWideInfo  商品对象
	 * **/
	void submitAttention(final int Status,final BrandCityWideInfo brandCityWideInfo)
	{
		httpControl.setFavoite(Integer.toString(brandCityWideInfo.getUserId()), Status, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				View v=pull_refresh_list.findViewWithTag(brandCityWideInfo);
				if(v!=null && v instanceof TextView)
				{
					switch(Status)
					{
					case 0:
						((TextView)v).setText("关注");
						brandCityWideInfo.setIsFavorite(false);
						break;
					case 1:
						((TextView)v).setText("取消关注");
						brandCityWideInfo.setIsFavorite(true);
						break;
					}
					
				}
				//SameCityAdapter.this.notifyDataSetChanged();
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(ctx, msg);
			}
		}, ctx);
	}
	
	void initBitmap(final String url, final ImageView iv)
	{
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
	
}
