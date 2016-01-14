package com.shenma.yueba.baijia.view;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.MsgAdapter;
import com.shenma.yueba.baijia.modle.MsgListInfo;
import com.shenma.yueba.baijia.modle.RequestMsgListInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;

import android.app.Activity;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import im.control.SocketObserverManager;
import im.form.RequestMessageBean;

/**  
 * @author gyj   我的消息
 * @version 创建时间：2015-5-19 下午2:03:37  
 * 程序的简单说明  
 */

public class MsgListView extends BaseView implements SocketObserverManager.SocketNoticationListener{
	Activity activity;
	LayoutInflater layoutInflater;
	boolean showDialog=true;
	boolean isruning=false;
	// 当前页
	int currpage = Constants.CURRPAGE_VALUE;
	// 每页显示的条数
	int pagesize = Constants.PAGESIZE_VALUE;
	private List<MsgListInfo> mList = new ArrayList<MsgListInfo>();
	private View view;
	private PullToRefreshListView pull_refresh_list;
	LinearLayout showloading_layout_view;
	MsgAdapter msgAdapter;
	boolean isImBroadcase=false;
	public MsgListView(Activity activity)
	{
		this.activity=activity;
	}
	
	public View getView()
	{
		if(view==null)
		{
			layoutInflater=activity.getLayoutInflater();
			initView();
			initPullView();
			firstInitData();
			SocketObserverManager.getInstance().addSocketObserver(this);
		}
		return view;
	}
	
	void initView()
	{
		view=layoutInflater.inflate(R.layout.refresh_listview_without_title_layout, null);
	}
	
	void initPullView()
	{
		pull_refresh_list=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		showloading_layout_view=(LinearLayout)view.findViewById(R.id.showloading_layout_view);
		pull_refresh_list.setMode(Mode.BOTH);
		pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				MsgListInfo msgListInfo=mList.get(arg2-1);
				//Intent intent=new Intent(activity,ChatActivity.class);
				int to_userid=msgListInfo.getId();
				Log.i("TAG", "----->>to_userid:"+to_userid);
				/*intent.putExtra("toUser_id", to_userid);
				intent.putExtra("Chat_NAME",msgListInfo.getName());//名字
				intent.putExtra("Chat_RoomID",msgListInfo.getRoomId());//roomid
				activity.startActivity(intent);*/
				ToolsUtil.forwardChatActivity(activity, msgListInfo.getName(), to_userid,0, msgListInfo.getRoomId(),null,null);
			}
		});
		
		ToolsUtil.initPullResfresh(pull_refresh_list, activity);
		 
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

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
		msgAdapter = new MsgAdapter(activity, mList);
		pull_refresh_list.setAdapter(msgAdapter);
	}
	
	
	/*****
	 * 请求加载数据
	 * ***/
	public void requestData() {
		if (isruning)
		{
			return;
		}
		isruning=true;
		sendHttp(currpage, 1);
	}

	/*****
	 * 请求刷新数据
	 * ***/
	public void requestFalshData() {
		if(isruning)
		{
			return;
		}
		isruning=true;
		sendHttp(1, 0);
	}
	

	
	/******
	 * 访问网络
	 * @param page int 当问的页数
	 * @param type int 类型
	 * ***/
	void sendHttp(final int page,final int type)
	{
		ToolsUtil.showNoDataView(activity, view, false);
		HttpControl httpControl=new HttpControl();
		httpControl.GetBaijiaMessageList(page, pagesize, showDialog, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				isruning=false;
				currpage=page;
				ToolsUtil.pullResfresh(pull_refresh_list);
				if(obj!=null && obj instanceof RequestMsgListInfoBean)
				{
					RequestMsgListInfoBean msgbean=(RequestMsgListInfoBean)obj;
					switch (type) {
					case 0:
						falshData(msgbean);
						break;
					case 1:
						addData(msgbean);
						break;
					}

					setPageStatus(msgbean, page);
				}else {
					http_Fails(500, activity.getResources()
							.getString(R.string.errorpagedata_str));
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				isruning=false;
				MyApplication.getInstance().showMessage(activity,msg);
				ToolsUtil.pullResfresh(pull_refresh_list);
			}
		}, activity);
	}
	
	
	void setPageStatus(RequestMsgListInfoBean data, int page) {
		if (page == 1 && (data.getData() == null
						|| data.getData().getItems() == null || data
						.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.PULL_FROM_START);
			}
			ToolsUtil.showNoDataView(activity, view,true);
		} else if (page != 1
				&& (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
			MyApplication.getInstance().showMessage(
					activity,
					activity.getResources().getString(
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
	void falshData(RequestMsgListInfoBean bean) {
		currpage++;
		showDialog = false;
		mList.clear();
		if (bean.getData() != null) {
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
			
		}
		if (msgAdapter != null) {
			msgAdapter.notifyDataSetChanged();
		}
		
	}

	/***
	 * 加载数据
	 * **/
	synchronized void addData(RequestMsgListInfoBean bean) {
		currpage++;
		showDialog = false;
		if (bean.getData() != null) {
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
			
		}
		if (msgAdapter != null) {
			msgAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void firstInitData() {
		requestFalshData();
	}


	@Override
	public void socketConnectSucess() {

	}

	@Override
	public void socketConnectFails() {

	}

	@Override
	public void receiveMsgFromRoom(RequestMessageBean bean) {

	}

	@Override
	public void receiveMsgFromUnRoom(RequestMessageBean bean) {
		if(bean!=null )
		{
			int touserid=bean.getToUserId();
			if(touserid>0)
			{
				String roomid=bean.getRoomId();
				if(roomid!=null)
				{
					requestFalshData();
				}
			}
		}
	}

	@Override
	public void sendStatusChaneg() {

	}
}
