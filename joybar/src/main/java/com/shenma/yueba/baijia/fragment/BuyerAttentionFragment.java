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
import com.shenma.yueba.baijia.adapter.BuyerSearchAdapter;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.SearchBuyerBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;

/**
 * 社交管理中的-----我的关注 and 我的粉丝
 * 
 * @author a
 * 
 */
public class BuyerAttentionFragment extends BaseFragment {

	private PullToRefreshListView pull_refresh_list;
	private BuyerSearchAdapter adapter;
	private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
	private int page = 1;
	private boolean isRefresh = true;
	public TextView tv_nodata;
	private String key;
	private String storeId;
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
		adapter = new BuyerSearchAdapter(getActivity(),mList);
		pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
		pull_refresh_list.setAdapter(adapter);
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page = 1;
				isRefresh = true;
				getSearchBuyerList(getActivity(),storeId, false,false,key);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				isRefresh = false;
				getSearchBuyerList(getActivity(),storeId, false,false,key);
			}
		});
		return view;
	}
	


	/**
	 * 获取搜到的买手列表
	 */
	public void getSearchBuyerList(Context ctx,String storeId,boolean showDialog,boolean focusRefresh,String key){
		this.key = key;
		if(showDialog && mList!=null && mList.size()>0 && !focusRefresh){
			return;
		}
		HttpControl httpControl = new HttpControl();
		String cityId = SharedUtil.getStringPerfernece(getActivity(), SharedUtil.getStringPerfernece(getActivity(), PerferneceConfig.SELECTED_CITY_ID));
		String latitude = PerferneceUtil.getString(PerferneceConfig.LATITUDE);
		String longitude = PerferneceUtil.getString(PerferneceConfig.LONGITUDE);
		httpControl.searchBuyer(key, SharedUtil.getUserId(ctx), cityId, storeId, longitude, latitude,showDialog ,page, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				pull_refresh_list.postDelayed(new Runnable() {
					@Override
					public void run() {
						pull_refresh_list.onRefreshComplete();
					}
				}, 100);
				SearchBuyerBackBean bean = (SearchBuyerBackBean) obj;
				if (isRefresh) {
					if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
						mList.clear();
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
		}, ctx);

	}
}
