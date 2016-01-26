package com.shenma.yueba.baijia.view;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.modle.RequestTuiJianCircleInfoBean;
import com.shenma.yueba.baijia.modle.TuiJianCircleInfo;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleView extends BaseView{
	Activity activity;
	List<FragmentBean> fragment_list=new ArrayList<FragmentBean>();
	List<View> footer_list=new ArrayList<View>();
	//当前选中的id
	int currid=-1;
	View view;
	PullToRefreshGridView baijia_quanzi_layout_tanb1_gridbview;
	LinearLayout showloading_layout_view;
	List<TuiJianCircleInfo> items=new ArrayList<TuiJianCircleInfo>();
	LayoutInflater inflater;
	int currPage=Constants.CURRPAGE_VALUE;
	int pageSize=Constants.PAGESIZE_VALUE;
	boolean showDialog=true;
	boolean isruning=false;
	HttpControl httpCntrol=new HttpControl();
	boolean isImBroadcase=false;
	
	public CircleView(Activity activity)
	{
		if(view==null)
		{
			this.activity=activity;
			inflater=activity.getLayoutInflater();
			view=inflater.inflate(R.layout.circleview_layout, null);
			initPullView();
			initView(view);
			
		}
	}

	public View getView()
	{
		//this.activity=activity;
		baijia_quanzi_layout_tanb1_gridbview.setFocusable(false);
		baijia_quanzi_layout_tanb1_gridbview.setFocusableInTouchMode(false);
		return view;
	}
	
	void initPullView()
	{
		baijia_quanzi_layout_tanb1_gridbview=(PullToRefreshGridView)view.findViewById(R.id.baijia_quanzi_layout_tanb1_gridbview);
		baijia_quanzi_layout_tanb1_gridbview.setMode(Mode.BOTH);
		ToolsUtil.initPullResfresh(baijia_quanzi_layout_tanb1_gridbview, activity);
		 
		baijia_quanzi_layout_tanb1_gridbview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				
				requestFalshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				Log.i("TAG", "onPullUpToRefresh");
				requestData();
			}
		});
	}
	
	void initView(View v)
	{
		baijia_quanzi_layout_tanb1_gridbview=(PullToRefreshGridView)v.findViewById(R.id.baijia_quanzi_layout_tanb1_gridbview);
		showloading_layout_view=(LinearLayout)v.findViewById(R.id.showloading_layout_view);
		
		baijia_quanzi_layout_tanb1_gridbview.setAdapter(baseAdapter);
		baijia_quanzi_layout_tanb1_gridbview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(!MyApplication.getInstance().isUserLogin(activity))
				{
					return;
				}
				TuiJianCircleInfo myCircleInfo=items.get(arg2);
				/*Intent intent=new Intent(activity,ChatActivity.class);
				intent.putExtra("Chat_NAME",myCircleInfo.getName());//圈子名字
				intent.putExtra("circleId", myCircleInfo.getId());
				activity.startActivity(intent);*/
				ToolsUtil.forwardChatActivity(activity, myCircleInfo.getName(),myCircleInfo.getId());
			}
		});
	}
	
	
	
	
	BaseAdapter baseAdapter=new BaseAdapter()
	{

		@Override
		public int getCount() {
			
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Holder holder;
			if(convertView==null)
			{
				holder=new Holder();
				convertView=inflater.inflate(R.layout.circle_item, null);
				holder.v=convertView;
				holder.riv=(RoundImageView)convertView.findViewById(R.id.riv);
				holder.tv_circle_name=(TextView)convertView.findViewById(R.id.tv_circle_name);
				holder.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
				holder.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
				convertView.setTag(holder);
				ToolsUtil.setFontStyle(activity, convertView, R.id.tv_circle_name,R.id.tv_count,R.id.tv_address);
			}else
			{
				holder=(Holder)convertView.getTag();
				
			}
			TuiJianCircleInfo myCircleInfo=items.get(position);
			initPic(ToolsUtil.nullToString(myCircleInfo.getLogo()), holder.riv);
			holder.tv_circle_name.setText(ToolsUtil.nullToString(myCircleInfo.getName()));
			holder.tv_count.setText("人数："+myCircleInfo.getMemberCount());
			holder.tv_address.setText(ToolsUtil.nullToString(myCircleInfo.getAddress()));
			return convertView;
		}
		
	};
	
	class Holder
	{
		View v;
		RoundImageView riv;
		TextView tv_circle_name;
		TextView tv_count;
		TextView tv_address;
	}
	
	void requestData()
	{
		if(isruning)
		{
			return;
		}
		baijia_quanzi_layout_tanb1_gridbview.setRefreshing();
		isruning=true;
		sendHttp(currPage,1);
	}
	
	void requestFalshData()
	{
		if(isruning)
		{
			return;
		}
		baijia_quanzi_layout_tanb1_gridbview.setRefreshing();
		isruning=true;
		sendHttp(1,0);
	}
	
	
	void addData(RequestTuiJianCircleInfoBean bean)
	{
		isruning=false;
		showDialog=false;
		currPage++;
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				items.addAll(bean.getData().getItems());
			}
		}
		showloading_layout_view.setVisibility(View.GONE);
		if(baseAdapter!=null)
		{
			baseAdapter.notifyDataSetChanged();
		}
		
	}
	
	void falshData(RequestTuiJianCircleInfoBean bean)
	{
		showDialog=false;
		isruning=false;
		currPage++;
		items.clear();
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				items.addAll(bean.getData().getItems());
			}
		}
		showloading_layout_view.setVisibility(View.GONE);
		if(baseAdapter!=null)
		{
			baseAdapter.notifyDataSetChanged();
		}
	}
	
	
	void sendHttp(final int page,final int type)
	{
		ToolsUtil.showNoDataView(activity,view,false);
		httpCntrol.getRecommendGroup(page, pageSize, showDialog, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				currPage=page;
				isruning=false;
				ToolsUtil.pullResfresh(baijia_quanzi_layout_tanb1_gridbview);
				if(obj!=null && obj instanceof RequestTuiJianCircleInfoBean)
				{
					RequestTuiJianCircleInfoBean bean=(RequestTuiJianCircleInfoBean)obj;
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
					isruning=false;
					http_Fails(500, activity.getResources()
							.getString(R.string.errorpagedata_str));
				}
				
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(activity, msg);
				ToolsUtil.pullResfresh(baijia_quanzi_layout_tanb1_gridbview);
			}
		},activity );
	}
	
	
	
	void setPageStatus(RequestTuiJianCircleInfoBean data, int page) {
		if (page == 1 && (data.getData() == null
						|| data.getData().getItems() == null || data
						.getData().getItems().size() == 0)) {
			if(baijia_quanzi_layout_tanb1_gridbview!=null)
			{
				baijia_quanzi_layout_tanb1_gridbview.setMode(Mode.PULL_FROM_START);
			}
			
			ToolsUtil.showNoDataView(activity, view,true);
		} else if (page != 1
				&& (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {
			if(baijia_quanzi_layout_tanb1_gridbview!=null)
			{
				baijia_quanzi_layout_tanb1_gridbview.setMode(Mode.BOTH);
			}
			
			MyApplication.getInstance().showMessage(
					activity,
					activity.getResources().getString(
							R.string.lastpagedata_str));
		} else {
			if(baijia_quanzi_layout_tanb1_gridbview!=null)
			{
				baijia_quanzi_layout_tanb1_gridbview.setMode(Mode.BOTH);
			}
		}
	}
	
	
	/*****
	 * 首次加载
	 * ***/
	public void firstInitData()
	{
		if(items.size()<=0 && showDialog)
		{
			requestFalshData();
		}
	}
	
	
	/****
	 * 加载图片
	 * */
	void initPic(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
