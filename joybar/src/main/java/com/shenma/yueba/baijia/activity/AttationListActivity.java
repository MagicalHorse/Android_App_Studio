package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.AttationListAdapter;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.AttationListInter;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;
import com.shenma.yueba.yangjia.modle.AttationAndFansListBackBean;
import com.umeng.analytics.MobclickAgent;


/**
 * 关注列表
 * @author a
 *
 */
public class AttationListActivity extends BaseActivityWithTopView implements AttationListInter {
	public static final String TYPE_FANS="FANS";//粉丝
	public static final String TYPE_ATTATION="ATTATION";//关注
	private AttationListAdapter brandAdapter;
	private List<AttationAndFansItemBean> mList = new ArrayList<AttationAndFansItemBean>();
	private View view;
	private PullToRefreshListView pull_refresh_list;
	HttpControl httoControl=new HttpControl();
	boolean showDialog=true;
	int status=0;
	int userID=-1;
	int currpage;
	int UserLevel=-1;
	AttationAndFansListBackBean bean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.refresh_listview_with_title_layout);
		super.onCreate(savedInstanceState);
		
		userID=this.getIntent().getIntExtra("userID", -1);
		if(userID<0 || this.getIntent().getStringExtra("TYPE")==null)
		{
			MyApplication.getInstance().showMessage(this, "参数错误");
			finish();
			return;
		}
		if(this.getIntent().getStringExtra("TYPE").equals(TYPE_FANS))//如果是粉丝
		{
			status=1;
		}else if(this.getIntent().getStringExtra("TYPE").equals(TYPE_ATTATION))//如果是关注
		{
			status=0;
		}else
		{
			MyApplication.getInstance().showMessage(this, "参数错误");
			finish();
			return;
		}
		initView();
		requestFalshData();
	}

	private void initView() {
		String title="";
		if(status==0)
		{
			title="我的关注";
		}else if(status==1)
		{
			title="粉丝";
		}
		setTitle(title);
		FontManager.changeFonts(this, tv_top_title);
		setLeftTextView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pull_refresh_list = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		pull_refresh_list.setMode(Mode.PULL_FROM_START);
		brandAdapter=new AttationListAdapter(this, mList,status,pull_refresh_list);
		pull_refresh_list.setAdapter(brandAdapter);
        ToolsUtil.initPullResfresh(pull_refresh_list, AttationListActivity.this);

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
	
	
	
	/*****
	 * 请求加载数据
	 * ***/
	public void requestData() {
		sendHttp(userID,UserLevel,status,currpage, 1);
	}

	/*****
	 * 请求刷新数据
	 * ***/
	public void requestFalshData() {
		sendHttp(userID,UserLevel,status,1, 0);
	}
	
	
	void sendHttp(int UserID,int UserLevel,int status,final int page,final int type)
	{
		ToolsUtil.showNoDataView(AttationListActivity.this, false);
		//当前登录的用户id
		int CurrentUserId=Integer.parseInt(SharedUtil.getStringPerfernece(this, SharedUtil.user_id));
		httoControl.getAttationOrFansList(CurrentUserId,UserID, UserLevel, status, page,Constants.PAGESIZE_VALUE, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				ToolsUtil.pullResfresh(pull_refresh_list);
				currpage=page;
				showDialog = false;
				if (obj != null&& obj instanceof AttationAndFansListBackBean) 
				{
					bean = (AttationAndFansListBackBean) obj;
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
					http_Fails(500, AttationListActivity.this.getResources()
							.getString(R.string.errorpagedata_str));
				}	

					

			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(AttationListActivity.this,msg);
				ToolsUtil.pullResfresh(pull_refresh_list);
			}
		}, this, showDialog);
	}
	
	
	void setPageStatus(AttationAndFansListBackBean data, int page) {
		if (page == 1 && (data.getData() == null
						|| data.getData().getItems() == null || data
						.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.PULL_FROM_START);
			}
			ToolsUtil.showNoDataView(AttationListActivity.this, true);
		} else if (page != 1
				&& (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
			
			MyApplication.getInstance().showMessage(
					AttationListActivity.this,
					AttationListActivity.this.getResources().getString(
							R.string.lastpagedata_str));
		} else {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
		}
	}
	
	
	/***
	 * 刷新viewpager数据
	 * ***/
	void falshData(AttationAndFansListBackBean bean) {
		currpage++;
		mList.clear();
		showDialog = false;
		if (bean.getData()!= null) {
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
			
		}
		if (brandAdapter != null) {
			brandAdapter.notifyDataSetChanged();
		}
		
	}

	/***
	 * 加载数据
	 * **/
	void addData(AttationAndFansListBackBean bean) {
		showDialog = false;
		currpage++;
		if (bean.getData()!= null) {
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
			
		}
		if (brandAdapter != null) {
			brandAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void refresh() {
		showDialog = true;
		requestFalshData();
	}
}
