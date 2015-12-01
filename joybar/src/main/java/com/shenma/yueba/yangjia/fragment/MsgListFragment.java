package com.shenma.yueba.yangjia.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.MsgAdapter;
import com.shenma.yueba.baijia.fragment.BaseFragment;
import com.shenma.yueba.baijia.modle.MsgListInfo;
import com.shenma.yueba.baijia.modle.RequestMsgListInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import im.broadcast.ImBroadcastReceiver;
import im.broadcast.ImBroadcastReceiver.ImBroadcastReceiverLinstener;
import im.broadcast.ImBroadcastReceiver.RECEIVER_type;
import im.form.RequestMessageBean;


/**
 * 消息列表Fragment
 * @author a
 *
 */
public class MsgListFragment extends BaseFragment implements ImBroadcastReceiverLinstener{
	private MsgAdapter msgAdapter;
	boolean showDialog=true;
	boolean  isfirststatus=false;
	// 当前页
	int currpage = Constants.CURRPAGE_VALUE;
	// 每页显示的条数
	//int pagesize = Constants.PAGESIZE_VALUE;
	int pagesize = 100000;
	private List<MsgListInfo> mList = new ArrayList<MsgListInfo>();
	private View view;
	private PullToRefreshListView pull_refresh_list;
	boolean isImBroadcase=false;
	ImBroadcastReceiver imBroadcastReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		if (view == null) {
			view = inflater.inflate(R.layout.refresh_listview_without_title_layout, null);
			imBroadcastReceiver=new ImBroadcastReceiver(this);
			initPullView();
			registImBroacase();
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		firstData();
		Log.i("msg", "msg1");
	}
	
	void initPullView()
	{
		pull_refresh_list=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		pull_refresh_list.setMode(Mode.BOTH);
		pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				MsgListInfo msgListInfo=mList.get(arg2-1);
				/*Intent intent=new Intent(getActivity(),ChatActivity.class);
				intent.putExtra("toUser_id", msgListInfo.getId());
				intent.putExtra("Chat_NAME",msgListInfo.getName());//名字
				intent.putExtra("Chat_RoomID",msgListInfo.getRoomId());//roomid
				getActivity().startActivity(intent);*/
				ToolsUtil.forwardChatActivity(getActivity(),msgListInfo.getName(), msgListInfo.getId(),0, msgListInfo.getRoomId(),null,null);
			}
		});
		ToolsUtil.initPullResfresh(pull_refresh_list, getActivity());
		 
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
		msgAdapter=new MsgAdapter(getActivity(), mList);
		pull_refresh_list.setAdapter(msgAdapter);
	}
	
	
	
	/*****
	 * 请求加载数据
	 * ***/
	public void requestData() {
		isfirststatus=true;
		sendHttp(currpage, 1);
	}

	/*****
	 * 请求刷新数据
	 * ***/
	public void requestFalshData() {
		isfirststatus=true;
		sendHttp(1, 0);
	}
	

	
	/******
	 * 访问网络
	 * @param page int 当问的页数
	 * @param type int 类型
	 * ***/
	void sendHttp(final int page,final int type)
	{
		
		ToolsUtil.showNoDataView(getActivity(), view,  false);
		HttpControl httpControl=new HttpControl();
		httpControl.GetBaijiaMessageList(page, pagesize, showDialog, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				isfirststatus=false;
				currpage=page;
				showDialog=false;
				ToolsUtil.pullResfresh(pull_refresh_list);
				if(obj!=null && obj instanceof RequestMsgListInfoBean)
				{
					RequestMsgListInfoBean bean=(RequestMsgListInfoBean)obj;
					switch (type) {
					case 0:
						falshData(bean);
						break;
					case 1:
						addData(bean);
						break;
					}
					setPageStatus(bean, page);
				}else {
					http_Fails(500, getActivity().getResources()
							.getString(R.string.errorpagedata_str));
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				isfirststatus=false;
				MyApplication.getInstance().showMessage(getActivity(),msg);
				ToolsUtil.pullResfresh(pull_refresh_list);
			}
		}, getActivity());
	}
	
	
	
	void setPageStatus(RequestMsgListInfoBean data, int page) {
		if (page == 1 && (data.getData() == null
						|| data.getData().getItems() == null || data
						.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.PULL_FROM_START);
			}
			ToolsUtil.showNoDataView(getActivity(), view,true);
		} else if (page != 1
				&& (data.getData() == null || data.getData().getItems()==null || data.getData().getItems().size() == 0)) {
			if(pull_refresh_list!=null)
			{
				pull_refresh_list.setMode(Mode.BOTH);
			}
			MyApplication.getInstance().showMessage(
					getActivity(),
					getActivity().getResources().getString(
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
		mList.clear();
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

	/***
	 * 加载数据
	 * **/
	void addData(RequestMsgListInfoBean bean) {
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
	
	public void firstData()
	{
		if(isfirststatus)
		{
			return;
		}
		requestFalshData();
	}

	@Override
	public void newMessage(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomMessage(Object obj) {
		if(obj!=null && obj instanceof RequestMessageBean)
		{
			RequestMessageBean	bean=(RequestMessageBean)obj;
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
	public void clearMsgNotation(RECEIVER_type type) {
		// TODO Auto-generated method stub
		
	}
	
	void registImBroacase()
	{
		if(!isImBroadcase)
		{
			if(getActivity()!=null)
			{
				isImBroadcase=true;
				getActivity().registerReceiver(imBroadcastReceiver, new IntentFilter(ImBroadcastReceiver.IntentFilterRoomMsg));
			}
		}
	}
	
	
	void unRegistImBroacase()
	{
		if(isImBroadcase)
		{
			if(getActivity()!=null)
			{
				isImBroadcase=false;
				getActivity().unregisterReceiver(imBroadcastReceiver);
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		unRegistImBroacase();
	}
}
