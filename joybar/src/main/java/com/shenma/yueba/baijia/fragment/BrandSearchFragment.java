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
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.newmodel.SearchBrandBackBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.adapter.BrandSearchAdapter;
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
public class BrandSearchFragment extends BaseFragment {

	private PullToRefreshListView pull_refresh_list;
	private BrandSearchAdapter adapter;
	private List<BrandInfo> mList = new ArrayList<BrandInfo>();
	private int page = 1;
	private boolean isRefresh = true;
	private int status = 1;// 0表示我关注的人   1表示我的粉丝
	public TextView tv_nodata;
	private String key;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}



	public BrandSearchFragment(String key){
		this.key =  key;
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
		adapter = new BrandSearchAdapter(getActivity(),
				mList);
		pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
		pull_refresh_list.setAdapter(adapter);
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page = 1;
				isRefresh = true;
				getBrand(getActivity(), false,false,key);
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page ++;
				isRefresh = false;
				getBrand(getActivity(), false,false,key);
			}
		});
		return view;
	}
	

	
	
	/**
	 * 获取品牌列表
	 */
	public void getBrand(Context ctx,boolean showDialog,boolean focusRefresh,String key){
		this.key = key;
		if(showDialog && mList!=null && mList.size()>0 && !focusRefresh){
			return;
		}
		HttpControl httpControl = new HttpControl();
		int userID=Integer.parseInt(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id));
		//当前登录的用户id
		int CurrentUserId=Integer.parseInt(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id));

		httpControl.searchbrand(key,page,showDialog,new HttpCallBackInterface(){
			@Override
			public void http_Success(Object obj) {
				pull_refresh_list.postDelayed(new Runnable() {
					@Override
					public void run() {
						pull_refresh_list.onRefreshComplete();
					}
				}, 100);
				SearchBrandBackBean bean = (SearchBrandBackBean) obj;
				if (isRefresh) {
					mList.clear();
					if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
						mList.addAll(bean.getData().getItems());
						tv_nodata.setVisibility(View.GONE);
					} else {
						tv_nodata.setVisibility(View.VISIBLE);
					}
					adapter.notifyDataSetChanged();
				} else {
					if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
						mList.addAll(bean.getData().getItems());
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		},ctx);
	}
}
