package com.shenma.yueba.baijia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.adapter.MyAttentionAndFansForSocialAdapter;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;
import com.shenma.yueba.yangjia.modle.AttationAndFansListBackBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 社交管理中的-----我的关注 and 我的粉丝
 * 
 * @author a
 * 
 */
public class MarketSearchFragment extends BaseFragment {

	private PullToRefreshListView pull_refresh_list;
	private MyAttentionAndFansForSocialAdapter adapter;
	private List<AttationAndFansItemBean> mList = new ArrayList<AttationAndFansItemBean>();
	private int page = 1;
	private boolean isRefresh = true;
	private int status = 1;// 0表示我关注的人   1表示我的粉丝
	public TextView tv_nodata;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.refresh_listview_without_title_layout, null);
		pull_refresh_list = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		tv_nodata = (TextView) view
				.findViewById(R.id.tv_nodata);
		adapter = new MyAttentionAndFansForSocialAdapter(getActivity(),
				mList);
		pull_refresh_list.setAdapter(adapter);
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page = 1;
				isRefresh = true;
				getAttationOrFansList(status, getActivity(), false);
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page ++;
				isRefresh = false;
				getAttationOrFansList(status, getActivity(), false);
			}
		});
		return view;
	}
	
	
	public void getData(int status,Context ctx,boolean showDialog){
		if(mList.size() == 0){
			getAttationOrFansList(status, ctx,showDialog);
		}
	}
	
	
	/**
	 * 获取关注列表和fans列表
	 */
	public void getAttationOrFansList(int status,Context ctx,boolean showDialog){
		HttpControl httpControl = new HttpControl();
		int userID=Integer.parseInt(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id));
		//当前登录的用户id
		int CurrentUserId=Integer.parseInt(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id));
		httpControl.getAttationOrFansList(CurrentUserId,userID,-1,status, page, Constants.PAGESIZE_VALUE,new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				pull_refresh_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    	pull_refresh_list.onRefreshComplete();
                    }
            }, 100);
				AttationAndFansListBackBean bean = (AttationAndFansListBackBean) obj;
				if (isRefresh) {
					if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null && bean.getData().getItems().size()>0){
						mList.clear();
						mList.addAll(bean.getData().getItems());
						tv_nodata.setVisibility(View.GONE);
						adapter = new MyAttentionAndFansForSocialAdapter(getActivity(), mList);
						pull_refresh_list.setAdapter(adapter);
					}else{
						tv_nodata.setVisibility(View.VISIBLE);
					}
				} else {
					if(bean!=null && bean.getData()!=null && bean.getData().getItems()!=null&& bean.getData().getItems().size()>0){
						mList.addAll(bean.getData().getItems());
						adapter.notifyDataSetChanged();
					}else{
						Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
			}
		}, ctx,showDialog);
	}
}
