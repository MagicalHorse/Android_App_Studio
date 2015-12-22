package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.ScrollViewPagerAdapter;
import com.shenma.yueba.baijia.modle.CKProductCountDownBean;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsPromotion;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagsInfo;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.FixedSpeedScroller;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.view.TagImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author gyj
 * @version 创建时间：2015-5-20 下午6:02:36 程序的简单说明 定义认证买手 商品详情页
 */

public class ApproveBuyerDetailsFragment extends Fragment implements OnClickListener ,CollectobserverManage.ObserverListener{
	// 当前选中的id （ViewPager选中的id）
	int currid = -1;
	// 滚动图片
	ViewPager appprovebuyer_viewpager;
	// 滚动图像下面的 原点
	LinearLayout appprovebuyer_viewpager_footer_linerlayout;

	// 滚动视图 主要内容
	ScrollView approvebuyerdetails_srcollview;
	// 底部购物车父视图
	LinearLayout approvebuyerdetails_footer;
	TextView approvebuyerdetails_closeingtime_textview;// 打烊时间
	TextView approvebuyerdetails_closeinginfo_textview;// 打烊信息
	int childWidth = 0;
	int maxcount = 8;
	ScrollViewPagerAdapter customPagerAdapter;
	// 商品id;
	int productID = -1;
	HttpControl httpControl = new HttpControl();
	RelativeLayout appprovebuyer_viewpager_relativelayout;
	// 商品信息对象
	CKProductDeatilsInfoBean Data;
	// 头像
	RoundImageView approvebuyerdetails_icon_imageview;
	// 私聊
	TextView approvebuyerdetails_layout_siliao_linerlayout_textview;
	TextView approvebuyerdetails_layout_shoucang_linerlayout_textview;//收藏

	// 加入购物车
	Button approvebuyer_addcartbutton;
	// 直接购买
	Button approvebuyerbuybutton;
	List<View> viewlist = new ArrayList<View>();
	LinearLayout approvebuyerdetails_closeingtime_linearlayout;
	RequestCKProductDeatilsInfo bean;
	LinearLayout ll_attentionpeople_contener;

	Activity activity;
	LayoutInflater layoutInflater;
	View parentView;
	RequestCk_SPECDetails requestCk_SPECDetails;
	Timer timer;
	CKProductCountDownBean ckProductCountDownBean;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		layoutInflater = LayoutInflater.from(activity);
		bean = (RequestCKProductDeatilsInfo) activity.getIntent().getSerializableExtra("ProductInfo");
		Data=bean.getData();
		productID=Integer.valueOf(Data.getProductId());
		if(ckProductCountDownBean==null)
		{
			ckProductCountDownBean=new CKProductCountDownBean();
			ckProductCountDownBean.setCkProductDeatilsInfoBean(bean.getData());
			ckProductCountDownBean.startTimer();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (parentView == null) {
			parentView = layoutInflater.inflate(R.layout.approvebuyerdetails_layout, null);
			initViews();
			setDatValue();
			setFont();
			getCkrProductSPECDetails();
			CollectobserverManage.getInstance().addObserver(this);
		}
		if (parentView.getParent() != null) {
			((ViewGroup) parentView.getParent()).removeView(parentView);
		}
		dangyanggouTime();
		return parentView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void initViews() {

		//返回
		ImageView back_grey_imageview=(ImageView)parentView.findViewById(R.id.back_grey_imageview);
		back_grey_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		//分享
		ImageView share_grey_imageview=(ImageView)parentView.findViewById(R.id.share_grey_imageview);
		share_grey_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MyApplication.getInstance().isUserLogin(activity)) {
					return;
				}

				if(bean!=null)
				{
					CKProductDeatilsInfoBean productinfobean=bean.getData();
					if(productinfobean!=null)
					{
						String content = ToolsUtil.nullToString(productinfobean.getShareDesc());
						String url = productinfobean.getShareLink();
						List<ProductsDetailsTagInfo> piclist=productinfobean.getProductPic();
						String img_name="";
						if(piclist.size()>0)
						{
							img_name=piclist.get(0).getLogo();
						}
						String icon = ToolsUtil.getImage(ToolsUtil.nullToString(img_name),320, 0);
						ToolsUtil.shareUrl(activity, Integer.valueOf(productinfobean.getProductId()), "", content, url, icon);
					}
				}
			}
		});

		// 收藏按钮
	    approvebuyerdetails_layout_shoucang_linerlayout_textview = (TextView)parentView.findViewById(R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview);
		//头像包裹视图
		ll_attentionpeople_contener=(LinearLayout)parentView.findViewById(R.id.ll_attentionpeople_contener);
		// 活动父视图
		approvebuyerdetails_closeingtime_linearlayout = (LinearLayout)parentView.findViewById(R.id.approvebuyerdetails_closeingtime_linearlayout);
		// 打烊时间
		approvebuyerdetails_closeingtime_textview = (TextView)parentView.findViewById(R.id.approvebuyerdetails_closeingtime_textview);
		// 打烊信息
		approvebuyerdetails_closeinginfo_textview = (TextView)parentView.findViewById(R.id.approvebuyerdetails_closeinginfo_textview);
		approvebuyerdetails_footer = (LinearLayout)parentView.findViewById(R.id.approvebuyerdetails_footer);
		approvebuyerdetails_srcollview = (ScrollView)parentView.findViewById(R.id.approvebuyerdetails_srcollview);

		appprovebuyer_viewpager_footer_linerlayout = (LinearLayout)parentView.findViewById(R.id.appprovebuyer_viewpager_footer_linerlayout);
		appprovebuyer_viewpager = (ViewPager)parentView.findViewById(R.id.appprovebuyer_viewpager);
		appprovebuyer_viewpager_relativelayout = (RelativeLayout)parentView.findViewById(R.id.appprovebuyer_viewpager_relativelayout);
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = width;
		appprovebuyer_viewpager_relativelayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));

		appprovebuyer_viewpager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						stopTimerToViewPager();
						break;
					case MotionEvent.ACTION_UP:
						startTimeToViewPager();
						break;
				}
				return false;
			}
		});
		appprovebuyer_viewpager
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						currid = arg0;
						setcurrItem(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		approvebuyerdetails_icon_imageview = (RoundImageView)parentView.findViewById(R.id.approvebuyerdetails_icon_imageview);
		approvebuyerdetails_icon_imageview.setOnClickListener(this);

		approvebuyerdetails_layout_siliao_linerlayout_textview = (TextView)parentView.findViewById(R.id.approvebuyerdetails_layout_siliao_linerlayout_textview);
		approvebuyerdetails_layout_siliao_linerlayout_textview.setOnClickListener(this);

		approvebuyer_addcartbutton = (Button)parentView.findViewById(R.id.approvebuyer_addcartbutton);
		approvebuyerbuybutton = (Button)parentView.findViewById(R.id.approvebuyerbuybutton);

		approvebuyerbuybutton.setOnClickListener(this);
		//喜欢的人的 头像列表
		LinearLayout approvebuyerdetails_attention_linearlayout=(LinearLayout)parentView.findViewById(R.id.approvebuyerdetails_attention_linearlayout);
		approvebuyerdetails_attention_linearlayout.setVisibility(View.GONE);

		/*****
		 * 进圈按钮
		 * ***/
		Button in_circle_button=(Button)parentView.findViewById(R.id.in_circle_button);
		in_circle_button.setOnClickListener(this);
	}



	void  dangyanggouTime()
	{
		if(ckProductCountDownBean!=null)
		{
			ckProductCountDownBean.setTimerLinstener(new CKProductCountDownBean.TimerLinstener() {
				@Override
				public void timerCallBack() {
					if (approvebuyerbuybutton != null) {
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									//如果打样够开始
									if (ckProductCountDownBean.isDayangGou()) {
										approvebuyerbuybutton.setText("立即购买");
									} else {
										approvebuyerbuybutton.setText(activity.getString(R.string.str_shopping_start) + ckProductCountDownBean.getShowstr());
									}
								}
							});
						}
					}
				}
			});
		}

	}


	void startChatActivity() {
		if (!MyApplication.getInstance().isUserLogin(
				activity)) {
			return;
		}
		if(bean!=null)
		{
			/*Intent intent = new Intent(ApproveBuyerDetailsActivity.this,ChatActivity.class);
			intent.putExtra("Chat_NAME", bean.getData().getBuyerName());// 圈子名字
			intent.putExtra("toUser_id", bean.getData().getBuyerId());// 私聊的话需要传对方id
			intent.putExtra("DATA", bean);
			startActivity(intent);*/
			ToolsUtil.forwardChatActivity(activity, bean.getData().getBuyerName(), Integer.valueOf(bean.getData().getBuyerId()), 0, null, bean, requestCk_SPECDetails);
		}
	}

	/***
	 * 设置文本值
	 *
	 * @param res
	 *            int 视图id
	 * @param str
	 *            要显示内容 null 不进行负值
	 * ***/
	void setdataValue(int res, String str) {
		View v = parentView.findViewById(res);
		if (v != null && v instanceof TextView) {
			if (str != null) {
				((TextView) v).setText(str);
			}
			FontManager.changeFonts(activity, ((TextView) v));
		}
	}

	/*****
	 * 设置数据
	 * ****/
	void setDatValue() {

		// 自提地址
		String address = ToolsUtil.nullToString(Data.getPickAddress());
		// 买手头像
		String usericon = ToolsUtil.nullToString(Data.getBuyerLogo());
		// 买手昵称
		String username =ToolsUtil.nullToString(Data.getBuyerName()) ;
		//城市
		String cityAddress=ToolsUtil.nullToString(Data.getCityName());
		setdataValue(R.id.address_name_textview,cityAddress);
		// 价格
		double price = Data.getPrice();
		double unitPrice=Data.getUnitPrice();
		//吊牌价
		setdataValue(R.id.hangtag_price_textview, "￥" + ToolsUtil.DounbleToString_2(unitPrice));
		TextView hangtag_price_textview = (TextView) parentView.findViewById(R.id.hangtag_price_textview);
		hangtag_price_textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		hangtag_price_textview.setVisibility(View.VISIBLE);


		// 商品名称
		String productName = ToolsUtil.nullToString(Data.getProductName());
		initPic(usericon, approvebuyerdetails_icon_imageview);

		TextView approvebuyerdetails_buyeraddress_textview=(TextView)parentView.findViewById(R.id.approvebuyerdetails_buyeraddress_textview);
		String buyeraddress=ToolsUtil.nullToString(Data.getPickAddress());
		if(buyeraddress.equals(""))
		{
			buyeraddress="未知";
		}
		approvebuyerdetails_buyeraddress_textview.setText(buyeraddress);
		// 自提地址
		setdataValue(R.id.approvebuyerdetails_addressvalue_textview, address);

		setdataValue(R.id.approvebuyerdetails_name_textview, username);
		// 金额
		setdataValue(R.id.approvebuyerdetails_price_textview,"￥" + ToolsUtil.DounbleToString_2(price));
		// 商品名称
		setdataValue(R.id.approvebuyerdetails_producename_textview, productName);
        // 收藏按钮
		approvebuyerdetails_layout_shoucang_linerlayout_textview.setOnClickListener(this);
		approvebuyerdetails_layout_shoucang_linerlayout_textview.setTag(Data);
		if(Data.isFavorite())
		{
			approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("已收藏");
		}else
		{
			approvebuyerdetails_layout_shoucang_linerlayout_textview.setText("收藏");

		}
		approvebuyerdetails_layout_shoucang_linerlayout_textview.setSelected(Data.isFavorite());

		if (Data.getProductPic() != null && Data.getProductPic().size() > 0) {
			//图片和标签
			List<ProductsDetailsTagInfo> productsDetailsTagInfo_list=  Data.getProductPic();
			for (int i = 0; i < productsDetailsTagInfo_list.size(); i++) {
				RelativeLayout rl=new RelativeLayout(activity);
				ImageView iv = new ImageView(activity);
				iv.setBackgroundColor(activity.getResources().getColor(R.color.color_lightgrey));
				iv.setScaleType(ScaleType.CENTER_CROP);
				rl.addView(iv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
				TagImageView tiv=new TagImageView(activity);
				//tiv.setBackgroundColor(ApproveBuyerDetailsActivity.this.getResources().getColor(R.color.color_blue));
				//tiv.setAlpha(0.5f);
				List<ProductsDetailsTagsInfo> productsDetailsTagsInfo_list=productsDetailsTagInfo_list.get(i).getTags();
				if(productsDetailsTagsInfo_list!=null && productsDetailsTagsInfo_list.size()>0)
				{
				  for(int j=0;j<productsDetailsTagsInfo_list.size();j++)
				  {
					  DisplayMetrics dm = new DisplayMetrics();
						activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
						int width = dm.widthPixels;
						int height = width;
					  ProductsDetailsTagsInfo pdtinfo= productsDetailsTagsInfo_list.get(j);
					  int tagx=(int)(pdtinfo.getPosX()*width);
					  int tagy=(int)(pdtinfo.getPosY()*height);
					  tiv.addTextTagCanNotMove(ToolsUtil.nullToString(pdtinfo.getName()), tagx, tagy,pdtinfo);
				  }
				}
				rl.addView(tiv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
				viewlist.add(rl);
				initPic(ToolsUtil.getImage(ToolsUtil.nullToString(productsDetailsTagInfo_list.get(i).getLogo()), 320, 0), iv);
			}
			customPagerAdapter = new ScrollViewPagerAdapter(activity, viewlist);
			appprovebuyer_viewpager.setAdapter(customPagerAdapter);
			setcurrItem(0);
			startTimeToViewPager();

		} else {
			appprovebuyer_viewpager_relativelayout.setVisibility(View.VISIBLE);
		}
		ProductsDetailsPromotion productsDetailsPromotion = Data.getPromotion();
		if(productsDetailsPromotion != null)
		{
			approvebuyerdetails_closeingtime_linearlayout.setVisibility(View.VISIBLE);
			approvebuyerdetails_closeingtime_textview.setText(ToolsUtil.nullToString(productsDetailsPromotion.getDescriptionText()));
			approvebuyerdetails_closeinginfo_textview.setText(ToolsUtil.nullToString(productsDetailsPromotion.getTipText()));
		}

		setFont();
		approvebuyerdetails_srcollview.smoothScrollTo(0, 0);
		approvebuyerbuybutton.setVisibility(View.VISIBLE);
	}


	@Override
	public void onResume() {
		super.onResume();
		startTimeToViewPager();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTimerToViewPager();
	}

	/***
	 * 添加原点
	 * 
	 * @param size
	 *            int 原点的个数
	 * @param value
	 *            int 当前选中的tab
	 * **/
	void addTabImageView(int size, int value) {
		appprovebuyer_viewpager_footer_linerlayout.removeAllViews();
		((RelativeLayout.LayoutParams) appprovebuyer_viewpager_footer_linerlayout.getLayoutParams()).bottomMargin = 40;
		if(size<=1)
		{
			return;
		}
		for (int i = 0; i < size; i++) {
			View v = new View(activity);
			v.setBackgroundResource(R.drawable.tabround_background);
			int width = (int) activity.getResources()
					.getDimension(R.dimen.shop_main_lineheight8_dimen);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					width, width);
			params.leftMargin = (int) activity
					.getResources()
					.getDimension(R.dimen.shop_main_width3_dimen);
			appprovebuyer_viewpager_footer_linerlayout.addView(v, params);
			if (i == value % size) {
				v.setSelected(true);
			} else {
				v.setSelected(false);
			}
		}
	}

	/****
	 * 设置当前显示的 item
	 * **/
	void setcurrItem(int i) {
		appprovebuyer_viewpager.setCurrentItem(i);
		addTabImageView(viewlist.size(), i);
	}

	/****
	 * 加载图片
	 * */
	void initPic(final String url, final ImageView iv) {
		Log.i("TAG", "URL:" + url);
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}

	void setFont() {
		setdataValue(R.id.approvebuyerdetails_closeingtime_textview, null);
		setdataValue(R.id.approvebuyerdetails_closeinginfo_textview, null);
		setdataValue(R.id.tv_top_title, null);
		// 设置昵称
		setdataValue(R.id.approvebuyerdetails_name_textview, null);
		// 金额
		setdataValue(R.id.approvebuyerdetails_price_textview, null);
		// 商品名称
		setdataValue(R.id.approvebuyerdetails_producename_textview, null);
		// 提货提醒
		setdataValue(R.id.approvebuyerdatails_layout_desc1_textview, null);
		// 自提地点
		setdataValue(R.id.approvebuyerdetails_address_textview, null);
		// 自提地址
		setdataValue(R.id.approvebuyerdetails_addressvalue_textview, null);
		// 收藏
		setdataValue(R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview,null);	
		// 私聊
		setdataValue(R.id.approvebuyerdetails_layout_siliao_linerlayout_textview,null);
		// 喜欢人数
		setdataValue(R.id.approvebuyerdetails_attention_textview, null);
		FontManager.changeFonts(activity, approvebuyer_addcartbutton);
		FontManager.changeFonts(activity, approvebuyerbuybutton);

	}

	/*****
	 * 启动自动滚动
	 * **/
	void startTimeToViewPager() {

		stopTimerToViewPager();
		if (viewlist == null || viewlist.size() <= 2) {
			return;
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				currid++;
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setViewPagerDuration(1000);
						setcurrItem(currid);
					}
				});
			}
		}, 2000, 3000);
	}

	/*****
	 * 停止自动滚动
	 * **/
	void stopTimerToViewPager() {
		setViewPagerDuration(500);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	// 设置滑动速度
	void setViewPagerDuration(int value) {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(appprovebuyer_viewpager.getContext(),new AccelerateInterpolator());
			field.set(appprovebuyer_viewpager, scroller);
			scroller.setmDuration(value);
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.approvebuyerdetails_icon_imageview:// 头像
//			if(!MyApplication.getInstance().isUserLogin(activity))
//			{
//				return;
//			}
			try
			{
				ToolsUtil.forwardShopMainActivity(activity, Integer.valueOf(bean.getData().getBuyerId()));
			}catch(Exception e)
			{
				MyApplication.getInstance().showMessage(getActivity(),"商户id错误");
			}

			break;
		case R.id.approvebuyerdetails_layout_siliao_linerlayout_textview:
			forwardSiLiao();
			break;
		case R.id.approvebuyerbuybutton:
			if(!ckProductCountDownBean.isDayangGou())
			{
				MyApplication.getInstance().showMessage(getActivity(),"活动还没开始");
				return;
			}
			startChatActivity();
			break;
		case R.id.approvebuyerdetails_layout_shoucang_linerlayout_textview:
			if (!MyApplication.getInstance().isUserLogin(
					activity)) {
				return;
			}
			if (v.getTag() != null && v.getTag() instanceof CKProductDeatilsInfoBean) {
				CKProductDeatilsInfoBean Data = (CKProductDeatilsInfoBean) v.getTag();
				if (Data != null) {
					if (Data.isFavorite()) {
						submitAttention(0, Data, v);
					} else {
						submitAttention(1, Data, v);
					}
				}

			}
			break;
			case R.id.in_circle_button://进圈
				if (!MyApplication.getInstance().isUserLogin(activity)) {
					return;
				}
				try
				{
					ToolsUtil.forwardShopMainCircleActivity(getActivity(),Integer.valueOf(bean.getData().getBuyerId()));
				}catch(Exception e)
				{
					MyApplication.getInstance().showMessage(getActivity(),"商户id错误");
				}
				break;
		}

	}

	void forwardSiLiao() {
		if (!MyApplication.getInstance().isUserLogin(activity)) {
			return;
		}

		ToolsUtil.forwardChatActivity(getActivity(),ToolsUtil.nullToString(bean.getData().getBuyerName()),Integer.valueOf(bean.getData().getBuyerId()),0,null,null,null);
	}

	/****
	 * 提交收藏与取消收藏商品
	 * 
	 * @param Status
	 *            int 0表示取消收藏 1表示收藏
	 * @param bean
	 *            ProductsDetailsInfoBean 商品对象
	 * **/
	void submitAttention(final int Status, final CKProductDeatilsInfoBean bean,
			final View v) {
		httpControl.setFavor(bean.getProductId(), Status,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						if (v != null && v instanceof TextView) {
							switch (Status) {
								case 0:
									v.setSelected(false);
									((TextView) v).setText("收藏");
									bean.setIsFavorite(false);
									break;
								case 1:
									((TextView) v).setText("已收藏");
									v.setSelected(true);
									bean.setIsFavorite(true);
									break;
							}

							//观察者通知数据改变
							PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
							pubuliuBeanInfo.setId(bean.getProductId());
							pubuliuBeanInfo.setIscollection(bean.isFavorite());
							CollectobserverManage.getInstance().dataChangeNotication(pubuliuBeanInfo);
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						MyApplication.getInstance().showMessage(
								activity, msg);
					}
				}, activity);
	}


	/**********
	 * 获取商品规格信息
	 ******/
	void getCkrProductSPECDetails() {
		httpControl.getCkrProductSPECDetails(productID, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				if (obj instanceof RequestCk_SPECDetails) {
					requestCk_SPECDetails = (RequestCk_SPECDetails) obj;
				}
				if(approvebuyerdetails_srcollview!=null)
				{
					approvebuyerdetails_srcollview.smoothScrollTo(0,0);
				}

			}

			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(
						activity, msg);
				activity.finish();
			}
		}, activity, true, true);
	}

	@Override
	public void observerCallNotification(PubuliuBeanInfo pubuliuBeanInfo) {
		if(pubuliuBeanInfo!=null)
		{
			if(pubuliuBeanInfo.getId().equals(bean.getData().getProductId()))
			{
				bean.getData().setIsFavorite(pubuliuBeanInfo.iscollection());
				if(approvebuyerdetails_layout_shoucang_linerlayout_textview!=null)
				{
					approvebuyerdetails_layout_shoucang_linerlayout_textview.setSelected(bean.getData().isFavorite());
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CollectobserverManage.getInstance().removeObserver(this);
	}
}
