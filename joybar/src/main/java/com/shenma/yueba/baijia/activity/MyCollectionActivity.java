package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListPic;
import com.shenma.yueba.baijia.modle.RequestMyFavoriteProductListInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的收藏
 * 
 * @author gyj
 * @version 创建时间：2015-5-20 上午11:14:57 程序的简单说明
 */


public class MyCollectionActivity extends BaseActivityWithTopView {
	HttpControl httpCntrol = new HttpControl();
	int currPage = Constants.CURRPAGE_VALUE;
	int pageSize = Constants.PAGESIZE_VALUE;
	boolean showDialog = true;
	PullToRefreshScrollView shop_main_layout_title_pulltorefreshscrollview;
	LinearLayout pubuliy_left_linearlayout, pubuliy_right_linearlayout;
	int leftHeight;// 左侧高度
	int rightHeight;// 右侧高度
	List<MyFavoriteProductListInfo> arr_list = new ArrayList<MyFavoriteProductListInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.shop_main_layoutbak);
		super.onCreate(savedInstanceState);
		initView();
		requestFalshData();
	}

	void initView() {
		setTitle("我的收藏");
		FontManager.changeFonts(MyCollectionActivity.this, tv_top_title);
		setLeftTextView(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MyCollectionActivity.this.finish();
			}
		});

		setLeftTextView(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyCollectionActivity.this.finish();
			}
		});
		RelativeLayout pubuliu_layout_include = (RelativeLayout) findViewById(R.id.pubuliu_layout_include);
		initPuBuView(pubuliu_layout_include);
		pubuliy_left_linearlayout = (LinearLayout) findViewById(R.id.pubuliy_left_linearlayout);
		pubuliy_right_linearlayout = (LinearLayout) findViewById(R.id.pubuliy_right_linearlayout);
		shop_main_layout_title_pulltorefreshscrollview = (PullToRefreshScrollView) findViewById(R.id.shop_main_layout_title_pulltorefreshscrollview);
		ToolsUtil.initPullResfresh(shop_main_layout_title_pulltorefreshscrollview, MyCollectionActivity.this);
		shop_main_layout_title_pulltorefreshscrollview.setMode(Mode.PULL_FROM_START);
		shop_main_layout_title_pulltorefreshscrollview.setOnRefreshListener(new OnRefreshListener2() {

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

	void requestData() {
		// shop_main_layout_title_pulltorefreshscrollview.setRefreshing();
		sendHttp(currPage, 1);
	}

	void requestFalshData() {
		// shop_main_layout_title_pulltorefreshscrollview.setRefreshing();
		sendHttp(1, 0);
	}

	/****
	 * 添加
	 ***/
	void addData(RequestMyFavoriteProductListInfoBean bean) {
		currPage++;
		if (bean.getData() != null) {
			if (bean.getData().getItems() != null) {
				arr_list.addAll(bean.getData().getItems());
				onaddData(bean.getData().getItems());
			}
		}

	}

	/****
	 * 刷新
	 ***/
	void falshData(RequestMyFavoriteProductListInfoBean bean) {
		currPage++;
		pubuliy_right_linearlayout.removeAllViews();
		pubuliy_left_linearlayout.removeAllViews();
		leftHeight = 0;
		rightHeight = 0;
		arr_list.clear();
		if (bean.getData() != null) {
			if (bean.getData().getItems() != null) {
				arr_list.addAll(bean.getData().getItems());
			}
		}
		onResher(arr_list);
	}

	/***
	 * 加载数据获取我收藏的商品
	 * 
	 * @param page
	 *            int 访问页
	 * @param type
	 *            int 0: 刷新 1:加载更多
	 **/
	void sendHttp(final int page, final int type) {
		ToolsUtil.showNoDataView(MyCollectionActivity.this, false);
		Log.i("TAG", "currpage=" + page + "   pagesize=" + pageSize);
		int userid = -1;
		if (SharedUtil.getStringPerfernece(this, SharedUtil.user_id) != null) {
			userid = Integer.parseInt(SharedUtil.getStringPerfernece(this, SharedUtil.user_id));
		}
		httpCntrol.getUserFavoriteProductList(userid, page, pageSize, showDialog, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				currPage = page;
				ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
				showDialog = false;
				if (obj != null && obj instanceof RequestMyFavoriteProductListInfoBean) {
					RequestMyFavoriteProductListInfoBean bean = (RequestMyFavoriteProductListInfoBean) obj;
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

					http_Fails(500, MyCollectionActivity.this.getResources().getString(R.string.errorpagedata_str));
				}

			}

			@Override
			public void http_Fails(int error, String msg) {
				ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
				MyApplication.getInstance().showMessage(MyCollectionActivity.this, msg);
			}
		}, MyCollectionActivity.this);
	}

	/*****
	 * 初始化瀑布流信息
	 ***/
	void initPuBuView(View parent) {
		LinearLayout pubuliy_left_linearlayout = (LinearLayout) parent.findViewById(R.id.pubuliy_left_linearlayout);
		LinearLayout pubuliy_right_linearlayout = (LinearLayout) parent.findViewById(R.id.pubuliy_right_linearlayout);

	}

	/****
	 * 设置刷新
	 * 
	 * @param item
	 *            List<MyFavoriteProductListInfo>
	 ***/
	public void onResher(List<MyFavoriteProductListInfo> item) {
		leftHeight=0;
		rightHeight=0;
		pubuliy_left_linearlayout.removeAllViews();
		pubuliy_right_linearlayout.removeAllViews();
		addItem(item);
	}

	void setPageStatus(RequestMyFavoriteProductListInfoBean data, int page) {
		if (page == 1 && (data.getData() == null || data.getData().getItems() == null
				|| data.getData().getItems().size() == 0)) {
			if (shop_main_layout_title_pulltorefreshscrollview != null) {
				shop_main_layout_title_pulltorefreshscrollview.setMode(Mode.PULL_FROM_START);
			}

			ToolsUtil.showNoDataView(MyCollectionActivity.this, true);
		} else if (page != 1 && (data.getData() == null || data.getData().getItems() == null
				|| data.getData().getItems().size() == 0)) {
			if (shop_main_layout_title_pulltorefreshscrollview != null) {
				shop_main_layout_title_pulltorefreshscrollview.setMode(Mode.BOTH);
			}

			MyApplication.getInstance().showMessage(MyCollectionActivity.this,
					MyCollectionActivity.this.getResources().getString(R.string.lastpagedata_str));
		} else {
			if (shop_main_layout_title_pulltorefreshscrollview != null) {
				shop_main_layout_title_pulltorefreshscrollview.setMode(Mode.BOTH);
			}
		}
	}

	/****
	 * 加载数据
	 * 
	 * @param item
	 *            List<MyFavoriteProductListInfo>
	 ****/
	public void onaddData(List<MyFavoriteProductListInfo> item) {
		addItem(item);
	}

	/*******
	 * 设置 瀑布流的 高度
	 * 
	 *****/
	void addItem(List<MyFavoriteProductListInfo> item) {
		if (item != null) {

			for (int i = 0; i < item.size(); i++) {
				MyFavoriteProductListInfo bean = item.get(i);
				int witdh = pubuliy_left_linearlayout.getWidth();
				MyFavoriteProductListPic myFavoriteProductListPic = bean.getPic();
				if (myFavoriteProductListPic == null) {
					myFavoriteProductListPic = new MyFavoriteProductListPic();
				}
				int height = (int) (witdh * myFavoriteProductListPic.getRatio());
				View parentview = LinearLayout.inflate(this, R.layout.pubuliu_item_layout, null);
				// 价格
				TextView pubuliu_item_layout_pricevalue_textview = (TextView) parentview
						.findViewById(R.id.pubuliu_item_layout_pricevalue_textview);
				pubuliu_item_layout_pricevalue_textview.setText(bean.getPrice() + "");
				// 商品名称
				TextView pubuliu_item_layout_name_textview = (TextView) parentview
						.findViewById(R.id.pubuliu_item_layout_name_textview);
				pubuliu_item_layout_name_textview.setText(bean.getName());
				// 收藏
				LinearLayout pubuliu_item_layout_like_linearlayout = (LinearLayout) parentview
						.findViewById(R.id.pubuliu_item_layout_like_linearlayout);
				TextView pubuliu_item_layout_like_textview = (TextView) parentview
						.findViewById(R.id.pubuliu_item_layout_like_textview);
				ImageView pubuliu_item_layout_like_imageview = (ImageView) parentview
						.findViewById(R.id.pubuliu_item_layout_like_imageview);
				pubuliu_item_layout_like_linearlayout.setOnClickListener(onClickListener);

				pubuliu_item_layout_like_imageview.setSelected(bean.isIsFavorite());
				pubuliu_item_layout_like_textview.setText(Integer.toString(bean.getFavoriteCount()));
				pubuliu_item_layout_like_linearlayout.setTag(bean);
				/*
				 * if(bean.getLikeUser()!=null) { MyFavoriteProductListLikeUser
				 * likeuser=bean.getLikeUser();
				 * pubuliu_item_layout_like_textview.setSelected(likeuser.
				 * isIsLike());
				 * pubuliu_item_layout_like_textview.setText(likeuser.getCount()
				 * +""); pubuliu_item_layout_like_textview.setTag(bean); }
				 */
				ImageView iv = (ImageView) parentview.findViewById(R.id.pubuliu_item_layout_imageview);
				if (height > 0) {
					Log.i("TAG",
							"height=" + height + " witdh=" + witdh + "ration=" + myFavoriteProductListPic.getRatio());

					iv.getLayoutParams().height = height;
				} else {
					height = witdh;
					Log.i("TAG",
							"height=" + height + " witdh=" + witdh + "ration=" + myFavoriteProductListPic.getRatio());

					iv.getLayoutParams().height = height;
				}
				iv.setTag(bean);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.getTag() == null) {
							return;
						}
						MyFavoriteProductListInfo bean = (MyFavoriteProductListInfo) v.getTag();
						ToolsUtil.forwardProductInfoActivity(MyCollectionActivity.this,bean.getId());
					}
				});
				iv.setImageResource(R.drawable.default_pic);
				iv.setScaleType(ScaleType.FIT_XY);
				android.util.Log.i("TAG", "leftHeight=" + leftHeight + "   rightHeight=" + rightHeight);
				// 根据 左右高度判断
				if (leftHeight <= rightHeight) {
					leftHeight += height;
					pubuliy_left_linearlayout.addView(parentview, new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				} else {
					rightHeight += height;
					pubuliy_right_linearlayout.addView(parentview, new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				}
				ToolsUtil.setFontStyle(MyCollectionActivity.this, parentview,
						R.id.pubuliu_item_layout_pricevalue_textview, R.id.pubuliu_item_layout_name_textview,
						R.id.pubuliu_item_layout_like_textview, R.id.pubuliu_item_layout_price_textview);
				initPic(ToolsUtil.getImage(ToolsUtil.nullToString(myFavoriteProductListPic.getPic()), 320, 0), iv);
			}
		}
	}

	
       OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.pubuliu_item_layout_like_linearlayout:
				
				if(v.getTag()!=null && v.getTag() instanceof MyFavoriteProductListInfo)
				{
					MyFavoriteProductListInfo  bean=(MyFavoriteProductListInfo)v.getTag();
					submitAttention(0,bean,v);
				}
				break;
			}
		}
	};
	

	/****
	 * 提交收藏与取消收藏商品
	 * 

	 *            int 0表示取消收藏 1表示收藏
	 *            BrandCityWideInfo 商品对象
	 * @param v
	 *            View 商品对象
	 **/
	void submitAttention(final int Status, final MyFavoriteProductListInfo myFavoriteProductListInfo, final View v) {
		httpCntrol.setFavor(myFavoriteProductListInfo.getId(), Status, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				/*if(v!=null)
				{
					try
					{
						View parentview=(View)v.getParent().getParent();
						if(parentview.getParent() ==pubuliy_left_linearlayout)
						{
							leftHeight-=parentview.getHeight();
						}else if(parentview.getParent() ==pubuliy_right_linearlayout)
						{
							rightHeight-=parentview.getHeight();
						}
						parentview.setVisibility(View.GONE);
					}catch(Exception e)
					{
						
					}
				}*/
				if(arr_list.contains(myFavoriteProductListInfo))
				{
					arr_list.remove(myFavoriteProductListInfo);
				}
				onResher(arr_list);
			}

			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(MyCollectionActivity.this, msg);
			}
		}, MyCollectionActivity.this);
	}

	/****
	 * 加载图片
	 */
	void initPic(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
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
		MyApplication.getInstance().removeActivity(this);// 加入回退栈
		super.onDestroy();
	}
}
