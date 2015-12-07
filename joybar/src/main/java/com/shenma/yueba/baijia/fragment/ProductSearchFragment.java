package com.shenma.yueba.baijia.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.baijia.activity.BaijiaProductInfoActivity;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.SearchProductBackBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.adapter.MyAttentionAndFansForSocialAdapter;
import com.shenma.yueba.yangjia.adapter.ProductSearchAdapter;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;
import com.shenma.yueba.yangjia.modle.AttationAndFansListBackBean;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;

/**
 * 社交管理中的-----我的关注 and 我的粉丝
 * 
 * @author a
 * 
 */
public class ProductSearchFragment extends BaseFragment {

	private PullToRefreshListView pull_refresh_list;
	private ProductSearchAdapter adapter;
	private List<ProductsInfoBean> mList = new ArrayList<ProductsInfoBean>();
	private int page = 1;
	private boolean isRefresh = true;
	public TextView tv_nodata;
	private String key,storeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	public static ProductSearchFragment getInstance(String key, String storeId) {
		ProductSearchFragment instance = new ProductSearchFragment();
		instance.key = key;
		instance.storeId = storeId;
		return instance;
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
		adapter = new ProductSearchAdapter(getActivity(),
				mList);
		pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
		pull_refresh_list.setAdapter(adapter);
		pull_refresh_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position>0){
					Intent intent = new Intent(getActivity(), BaijiaProductInfoActivity.class);
					intent.putExtra("productID", mList.get(position-1).getProductId());
					getActivity().startActivity(intent);
				}
			}
		});
		pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page = 1;
				isRefresh = true;
				getProductList(getActivity(), false, true, key);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page++;
				isRefresh = false;
				getProductList(getActivity(), false, false, key);
			}
		});
		return view;
	}




	/**
	 * 搜索商品列表
	 */
	public void getProductList( Context ctx, boolean showDialog,boolean focusRefresh,String key){
		this.key = key;
		if(showDialog && mList!=null && mList.size()>0 && !focusRefresh){
			return;
		}
		HttpControl httpControl = new HttpControl();
		String cityId = SharedUtil.getStringPerfernece(getActivity(), SharedUtil.getStringPerfernece(getActivity(), PerferneceConfig.SELECTED_CITY_ID));
		httpControl.searchProducts(key, SharedUtil.getUserId(ctx), cityId, storeId, "0", showDialog,page, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				pull_refresh_list.postDelayed(new Runnable() {
					@Override
					public void run() {
						pull_refresh_list.onRefreshComplete();
					}
				}, 100);
				SearchProductBackBean bean = (SearchProductBackBean) obj;
				if (isRefresh) {
					mList.clear();
					if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
						mList.addAll(bean.getData().getItems());
						tv_nodata.setVisibility(View.GONE);
					} else {
						tv_nodata.setVisibility(View.VISIBLE);
					}
					adapter = new ProductSearchAdapter(getActivity(), mList);
					pull_refresh_list.setAdapter(adapter);
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