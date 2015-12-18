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
import com.shenma.yueba.baijia.modle.newmodel.FavBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.SearchBuyerBackBean;
import com.shenma.yueba.inter.AttentionEvent;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.adapter.ProductSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;
import de.greenrobot.event.EventBus;

/**
 *
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
		EventBus.getDefault().register(this);

	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(AttentionEvent event) {
		for (int i = 0;i<mList.size();i++){
			if(mList.get(i).getUserId().equals(event.getMsg())){
				mList.remove(i);
				break;
			}
		}
		adapter.notifyDataSetChanged();
		String msg = "onEventMainThread收到了消息：" + event.getMsg();
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

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
				getAttentionBuyer(false,true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				isRefresh = false;
				getAttentionBuyer(false,false);
			}
		});
		return view;
	}


	/**
	 * 获取关注的买手
	 */
	public void getAttentionBuyer(boolean showDialog,final boolean foucusRefresh) {
		if(showDialog && mList!=null && mList.size()>0 && !foucusRefresh){
			return;
		}
		HttpControl httpControl = new HttpControl();
		httpControl.getFavBuyers(page, new HttpControl.HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				pull_refresh_list.postDelayed(new Runnable() {
					@Override
					public void run() {
						pull_refresh_list.onRefreshComplete();
					}
				}, 100);
				FavBuyersBackBean bean = (FavBuyersBackBean) obj;
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
		}, getActivity());
	}



}
