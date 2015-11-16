package com.shenma.yueba.baijia.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaijiaBrandListActivity;
import com.shenma.yueba.baijia.adapter.ImageTextlAdapter;
import com.shenma.yueba.baijia.modle.BrandSearchInfo;
import com.shenma.yueba.baijia.modle.RequestBrandSearchInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

/**
 * 品牌
 * @author a
 *
 */
public class BrandFragment extends BaseFragment{
	private ImageTextlAdapter imageTextlAdapter;
	private List<BrandSearchInfo> mList = new ArrayList<BrandSearchInfo>();
	private View view;
	private PullToRefreshListView pull_refresh_list;
	LinearLayout showloading_layout_view;
	HttpControl httpControl=new HttpControl();
	int state=0;
	String key="";
	int currPage=0;

	boolean ishowStatus=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (view == null) {
			view = inflater.inflate(R.layout.refresh_listview_without_title_layout, null);
			initPullView();
			//requestFalshData();
		}
		
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}
	
	void initPullView()
	{
		pull_refresh_list=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		showloading_layout_view=(LinearLayout)view.findViewById(R.id.showloading_layout_view);
		imageTextlAdapter=new ImageTextlAdapter(getActivity(), mList);
		pull_refresh_list.setAdapter(imageTextlAdapter);
		pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if((arg2-1)>=0 && (arg2-1)<mList.size())
				{
					BrandSearchInfo brandSearchInfo=mList.get(arg2-1);
					Intent intent=new Intent(getActivity(),BaijiaBrandListActivity.class);
					intent.putExtra("BrandList_type", BaijiaBrandListActivity.BrandList_type.Type_Brand);
					intent.putExtra("BrandId", brandSearchInfo.getId());
					intent.putExtra("BrandName", brandSearchInfo.getName());
					getActivity().startActivity(intent);
				}
				
			}
		});
		
		pull_refresh_list.setMode(Mode.PULL_FROM_START);
		ToolsUtil.initPullResfresh(pull_refresh_list, getActivity());
		 
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
	
	/***
	 * 请求
	 * **/
	 void requestData()
	{
		 if(key.equals(""))
		 {
			 MyApplication.getInstance().showMessage(getActivity(), "请输入关键字");
			 ToolsUtil.pullResfresh(pull_refresh_list);
			 return;
		 }
		sendHttp(1,currPage, state, key);
	}
	
	/***
	 * 刷新
	 * **/
	void requestFalshData()
	{
		if(key.equals(""))
		 {
			 MyApplication.getInstance().showMessage(getActivity(), "请输入关键字");
			 ToolsUtil.pullResfresh(pull_refresh_list);
			 return;
		 }
		mList.clear();
		sendHttp(0,1, state, key);
	}
	
	/***
	 * 加载更多数据
	 * **/
	void addData(RequestBrandSearchInfoBean bean)
	{
		ishowStatus=false;
		currPage++;
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
		}
		
		showloading_layout_view.setVisibility(View.GONE);
		if(imageTextlAdapter!=null)
		{
			imageTextlAdapter.notifyDataSetChanged();
		}
		
	}
	
	/***
	 * 刷新数据
	 * **/
	void falshData(RequestBrandSearchInfoBean bean)
	{
		ishowStatus=false;
		currPage++;
		mList.clear();
		if(bean.getData()!=null)
		{
			if(bean.getData().getItems()!=null)
			{
				mList.addAll(bean.getData().getItems());
			}
		}
		
		showloading_layout_view.setVisibility(View.GONE);
		if(imageTextlAdapter!=null)
		{
			imageTextlAdapter.notifyDataSetChanged();
		}
		
	}
	
	/******
	 * 访问网络
	 * @param type int 类型 0：刷新  1： 加载
	 * @param page int 当前页
	 * @param state  int 0: 搜索品牌列表, 1:搜索买手列表
	 * @param key  string类型，关键字
	 * ***/
	void sendHttp(final int type ,final int page,int state,String key)
	{
		ToolsUtil.showNoDataView(getActivity(),view ,false);
		httpControl.searchbrandList(page,Constants.PAGESIZE_VALUE, state,key,ishowStatus, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				ToolsUtil.pullResfresh(pull_refresh_list);
				currPage=page;
				ishowStatus=false;
				if(obj!=null &&  obj instanceof RequestBrandSearchInfoBean)
				{
					RequestBrandSearchInfoBean bean=(RequestBrandSearchInfoBean)obj;
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
					http_Fails(500, getActivity().getResources()
							.getString(R.string.errorpagedata_str));
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(getActivity(), msg);
				ToolsUtil.pullResfresh(pull_refresh_list);
			}
		}, getActivity());
	}
	
	
	void setPageStatus(RequestBrandSearchInfoBean data, int page) {
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
	
	public void searchData(String key)
	{
		if(key==null || key.equals(""))
		{
			return;
		}
		ishowStatus=true;
		imageTextlAdapter=new ImageTextlAdapter(getActivity(), mList);
		pull_refresh_list.setAdapter(imageTextlAdapter);
		this.key=key;
		requestFalshData();
	}
}
