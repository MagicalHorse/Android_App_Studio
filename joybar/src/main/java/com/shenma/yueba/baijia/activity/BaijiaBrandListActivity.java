package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.BrandInfoInfo;
import com.shenma.yueba.baijia.modle.BrandInfoInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandInfoBean;
import com.shenma.yueba.baijia.modle.RequestBrandInfoInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-15 下午1:59:52  
 * 程序的简单说明  
 */

public class BaijiaBrandListActivity extends BaseActivityWithTopView{
View parenetView;
PullToRefreshGridView brandlist_layout_pullTorefreshgridview;
HttpControl httpControl;
int currPage=Constants.CURRPAGE_VALUE;
int pageSize=20;
boolean showDialog=true;
int BrandId=-1;//品牌id
String BrandName=null;//品牌名字
String TextName=null;//普通标签名字
public enum BrandList_type
{
	Type_Brand,//品牌
	Type_Text //普通标签
}

List<BrandInfoInfo> object_list=new ArrayList<BrandInfoInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		parenetView=this.getLayoutInflater().inflate(R.layout.brandlist_layout, null);
		setContentView(parenetView);
		super.onCreate(savedInstanceState);
		httpControl=new HttpControl();
		
		//判断当前传递的参数是 品牌还是  普通标签
		if(this.getIntent().getSerializableExtra("BrandList_type")==null)
		{
			MyApplication.getInstance().showMessage(this, "参数错误");
			finish();
			return ;
		}
		BrandList_type type=(BrandList_type)this.getIntent().getSerializableExtra("BrandList_type");
		switch(type)
		{
		case Type_Brand:
			BrandId=this.getIntent().getIntExtra("BrandId", -1);
			BrandName=this.getIntent().getStringExtra("BrandName");
			setTitle(ToolsUtil.nullToString(BrandName));
			break;
		case Type_Text:
			TextName=this.getIntent().getStringExtra("TextName");
			setTitle(ToolsUtil.nullToString(TextName));
			break;
			
		}
		initView();
		requestFalshData();
	}
	
	void initView()
	{
		FontManager.changeFonts(mContext,tv_top_title);
		setLeftTextView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaijiaBrandListActivity.this.finish();
			}
		});
		
		brandlist_layout_pullTorefreshgridview=(PullToRefreshGridView)parenetView.findViewById(R.id.brandlist_layout_pullTorefreshgridview);
		brandlist_layout_pullTorefreshgridview.setMode(Mode.PULL_FROM_START);
		brandlist_layout_pullTorefreshgridview.setAdapter(baseAdapter);
		ToolsUtil.initPullResfresh(brandlist_layout_pullTorefreshgridview, BaijiaBrandListActivity.this);
		 
		brandlist_layout_pullTorefreshgridview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				
				//SystemClock.sleep(100);
				Log.i("TAG", "onPullDownToRefresh");
				requestFalshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				Log.i("TAG", "onPullUpToRefresh");
				requestData();
			}
		});
		
		
		
		
		brandlist_layout_pullTorefreshgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				BrandInfoInfo brandInfoInfo=object_list.get(arg2);
				ToolsUtil.forwardProductInfoActivity(BaijiaBrandListActivity.this,brandInfoInfo.getProductId());
			}
		});
	}
	
	
	/*****
	 *请求加载数据
	 * ***/
	void requestData()
	{
		sendHttp(currPage,1);
	}
	
	/*****
	 *请求刷新数据
	 * ***/
	void requestFalshData()
	{
		currPage=Constants.CURRPAGE_VALUE;
		sendHttp(1,0);
	}
	
	
	
	
	
	BaseAdapter baseAdapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView==null)
			{
				DisplayMetrics dm=new DisplayMetrics();
				BaijiaBrandListActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width=dm.widthPixels;
				int itemwidth=(width/3)-(2*5);
				
				holder=new Holder();
				convertView=(LinearLayout)LinearLayout.inflate(BaijiaBrandListActivity.this, R.layout.brandlist_item_layout, null);
				holder.imageview=(ImageView)convertView.findViewById(R.id.brandlist_item_layout_imageview);
				convertView.setTag(holder);
				
				Log.i("TAG", "BaiJiaBrandListActivity-->> w:"+itemwidth);
				holder.imageview.setLayoutParams(new LinearLayout.LayoutParams(itemwidth, itemwidth));
			}else
			{
				holder=(Holder)convertView.getTag();
			}
			
			String url="";
			if(position<=(object_list.size()-1))
			{
				BrandInfoInfo bean=object_list.get(position);
				url=bean.getPic();
			}
			
			initPic(ToolsUtil.getImage(ToolsUtil.nullToString(url), 320, 0), holder.imageview);
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			
			return 0;
		}
		
		@Override
		public Object getItem(int arg0) {
			
			return null;
		}
		
		@Override
		public int getCount() {
			
			return object_list.size();
		}
	};
	
	class Holder
	{
	  ImageView imageview;
	}
	/*****
	 * 请求数据
	 * @param page int 当前页
	 * ***/
	void sendHttp(final int page,final int type)
	{
		ToolsUtil.showNoDataView(BaijiaBrandListActivity.this,parenetView,false);
		httpControl.getBaijiaBrandDetails(page, pageSize, BrandId, TextName,showDialog, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				ToolsUtil.pullResfresh(brandlist_layout_pullTorefreshgridview);
				currPage=page;
				showDialog=false;
				if(obj!=null)
				{
					RequestBrandInfoInfoBean bean=(RequestBrandInfoInfoBean)obj;
					switch (type) {
					case 0:
						falshData(bean);
						break;
					case 1:
						addData(bean);
						break;
					}
					setPageStatus(bean, page);
					
				} else {
					http_Fails(500, BaijiaBrandListActivity.this.getResources()
							.getString(R.string.errorpagedata_str));
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(BaijiaBrandListActivity.this, msg);
				ToolsUtil.pullResfresh(brandlist_layout_pullTorefreshgridview);
			}
		}, BaijiaBrandListActivity.this);
	}
	
	
	void setPageStatus(RequestBrandInfoInfoBean data, int page) {
		if (page == 1 && (data.getData() == null
						|| data.getData().getItems() == null || data
						.getData().getItems().size() == 0)) {
			if(brandlist_layout_pullTorefreshgridview!=null)
			{
				brandlist_layout_pullTorefreshgridview.setMode(Mode.PULL_FROM_START);
			}
			ToolsUtil.showNoDataView(BaijiaBrandListActivity.this, parenetView,true);
		} else if (page != 1
				&& (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {
			
			if(brandlist_layout_pullTorefreshgridview!=null)
			{
				brandlist_layout_pullTorefreshgridview.setMode(Mode.BOTH);
			}
			
			MyApplication.getInstance().showMessage(
					BaijiaBrandListActivity.this,
					BaijiaBrandListActivity.this.getResources().getString(
							R.string.lastpagedata_str));
		} else {
			if(brandlist_layout_pullTorefreshgridview!=null)
			{
				brandlist_layout_pullTorefreshgridview.setMode(Mode.BOTH);
			}
		}
	}
	
	
	
	/***
	 * 刷新viewpager数据
	 * ***/
	void falshData(RequestBrandInfoInfoBean bean) {
		currPage++;
		object_list.clear();
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				object_list.addAll(bean.getData().getItems());
			}
			
		}
		if(baseAdapter!=null)
		{
			baseAdapter.notifyDataSetChanged();
		}
		showDialog=false;
	}
	
	
	/***
	 * 加载数据
	 * **/
	void addData(RequestBrandInfoInfoBean bean) {
		currPage++;
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				object_list.addAll(bean.getData().getItems());
			}
			
		}
		if(baseAdapter!=null)
		{
			baseAdapter.notifyDataSetChanged();
		}
	}
	
	void initPic(String url,ImageView iv)
	{
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
	
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
	
	
	
	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);//加入回退栈
		super.onDestroy();
	}
}