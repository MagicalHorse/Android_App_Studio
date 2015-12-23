package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BaiJiaMyCircleAdapter;
import com.shenma.yueba.baijia.modle.MyCircleInfo;
import com.shenma.yueba.baijia.modle.RequestMyCircleInfoBean;
import com.shenma.yueba.baijia.view.MyCircleView;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;


/**  
 * @author gyj  
 * @version 创建时间：2015-5-20 上午11:24:51  
 * 程序的简单说明  
 */

public class CircleListActivity extends BaseActivityWithTopView{
	private View view;
	private PullToRefreshListView pull_refresh_list;
	LinearLayout showloading_layout_view;
	static MyCircleView myCircleView;
	LayoutInflater inflater;
	BaiJiaMyCircleAdapter myCircleAdapter;
	int currPage=Constants.CURRPAGE_VALUE;
	int pageSize=Constants.PAGESIZE_VALUE;
	boolean showDialog=true;
	HttpControl httpCntrol=new HttpControl();
	List<MyCircleInfo> items=new ArrayList<MyCircleInfo>();
	int userID=-1;
	
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		inflater=this.getLayoutInflater();
		view = inflater.inflate(R.layout.circlelist_layout, null);
		setContentView(view);
		super.onCreate(savedInstanceState);
		userID=this.getIntent().getIntExtra("userID", -1);
		if(userID<0)
		{
			MyApplication.getInstance().showMessage(this, "用户信息错误");
			finish();
			return;
		}
		initPullView();
		requestFalshData();
	}
	
	void initPullView()
	{
		setTitle("我的圈子");
		FontManager.changeFonts(CircleListActivity.this, tv_top_title);
		setLeftTextView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CircleListActivity.this.finish();
			}
		});
		pull_refresh_list=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		showloading_layout_view=(LinearLayout)view.findViewById(R.id.showloading_layout_view);
		pull_refresh_list.setMode(Mode.PULL_FROM_START);
		ToolsUtil.initPullResfresh(pull_refresh_list, CircleListActivity.this);
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				
				requestFalshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				
				requestData();
			}
		});
		myCircleAdapter=new BaiJiaMyCircleAdapter(CircleListActivity.this, items);
		pull_refresh_list.setAdapter(myCircleAdapter);
		pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				MyCircleInfo myCircleInfo=items.get(arg2-1);
				/*Intent intent=new Intent(CircleListActivity.this,ChatActivity.class);
				intent.putExtra("Chat_NAME",myCircleInfo.getName());//圈子名字
				intent.putExtra("circleId",myCircleInfo.getId());
				startActivity(intent);*/
				ToolsUtil.forwardChatActivity(CircleListActivity.this, myCircleInfo.getName(), 0, myCircleInfo.getId(), null,null,null);
			}
		});
	}
	
	
	void requestData()
	{
		sendHttp(currPage,1);
	}
	
	void requestFalshData()
	{
		sendHttp(1,0);
	}
	
	
	void addData(RequestMyCircleInfoBean bean)
	{
		currPage++;
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				items.addAll(bean.getData().getItems());
			}
		}
		
		showloading_layout_view.setVisibility(View.GONE);
		if(myCircleAdapter!=null)
		{
			myCircleAdapter.notifyDataSetChanged();
		}
	}
	
	void falshData(RequestMyCircleInfoBean bean)
	{
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
		if(myCircleAdapter!=null)
		{
			myCircleAdapter.notifyDataSetChanged();
		}
	}
	
	/******
	 * @param page int 访问页
	 * @param type int 0：刷新  1：加载
	 * 
	 * **/
	void sendHttp(final int page,final int type)
	{
		ToolsUtil.showNoDataView(CircleListActivity.this,view,false);
		httpCntrol.getUserGroups(userID,page, pageSize, showDialog, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				showDialog=false;
				currPage=page;
				ToolsUtil.pullResfresh(pull_refresh_list);
				
				if(obj!=null && obj instanceof RequestMyCircleInfoBean)
				{
					RequestMyCircleInfoBean bean=(RequestMyCircleInfoBean)obj;
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
						
					http_Fails(500, CircleListActivity.this.getResources().getString(R.string.errorpagedata_str));
				}
				
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(CircleListActivity.this, msg);
				CircleListActivity.this.finish();
				ToolsUtil.pullResfresh(pull_refresh_list);
			}
		},CircleListActivity.this );
	}
	
	
	void setPageStatus(RequestMyCircleInfoBean data, int page) {
		if (page == 1 && (data.getData()==null || data.getData().getItems() == null || data.getData().getItems().size()==0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.PULL_FROM_START);
			}
			
			ToolsUtil.showNoDataView(CircleListActivity.this,view,true);
		} else if (page != 1 && (data.getData()==null || data.getData().getItems()== null || data.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
			
			MyApplication.getInstance().showMessage(CircleListActivity.this,CircleListActivity.this.getResources().getString(R.string.lastpagedata_str));
		}else
		{
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
		}
	}
	
	
	  @Override
	    protected void onDestroy() {
	    	MyApplication.getInstance().removeActivity(this);//加入回退栈
	    	super.onDestroy();
	    }
	  
	  

		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			}
			public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			}
}
