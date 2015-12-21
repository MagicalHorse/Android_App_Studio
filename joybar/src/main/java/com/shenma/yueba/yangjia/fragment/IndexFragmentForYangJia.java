package com.shenma.yueba.yangjia.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.MyFragmentPagerAdapter;
import com.shenma.yueba.baijia.fragment.BaseFragment;
import com.shenma.yueba.baijia.fragment.MyBuyerFragment;
import com.shenma.yueba.baijia.modle.BuyerIndexInfo;
import com.shenma.yueba.baijia.modle.BuyerIndexInfoBean;
import com.shenma.yueba.baijia.modle.Favorite;
import com.shenma.yueba.baijia.modle.Goodsamount;
import com.shenma.yueba.baijia.modle.Income;
import com.shenma.yueba.baijia.modle.Order;
import com.shenma.yueba.baijia.modle.Product;
import com.shenma.yueba.inter.IndexRefreshInter;
import com.shenma.yueba.util.Base64Coder;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ShareUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.yangjia.activity.BigImageShowActivity;
import com.shenma.yueba.yangjia.activity.EarningManagerActivity;
import com.shenma.yueba.yangjia.activity.HuoKuanManagerActivity;
import com.shenma.yueba.yangjia.activity.ProductManagerActivity;
import com.shenma.yueba.yangjia.activity.SalesManagerForBuyerActivity;
import com.shenma.yueba.yangjia.activity.SocialManagerActivity;

/**
 * 养家主界面
 * 
 * @author a
 */
public class IndexFragmentForYangJia extends BaseFragment implements
		OnClickListener ,IndexRefreshInter{
	private View view;
	private ImageView iv_cursor_left, iv_cursor_center, iv_cursor_right;
	private Button bt_cart;
	private ViewPager viewpager_main;
	private MyBuyerFragment myBuyerFragment;// 我的买手
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private MyFragmentPagerAdapter myFragmentPagerAdapter;
	private String codeUrl;
	private String shopName;
	private Income income;
	
	private BitmapUtils bitmapUtils;
	
	private ImageView iv_qr_code;
	private TextView tv_qr_name;
	private BuyerIndexInfo data;
	
	private TextView tv_huokuan_title;
	private TextView tv_today_huokuan_title;
	private TextView tv_today_huokuan_money;
	private TextView tv_today_huokuan_yuan;
	private TextView tv_all_huokuan_title;
	private TextView tv_all_huokuan_money;
	private TextView tv_all_huokuan_yuan;
	
	private TextView tv_earnings_title;
	private TextView tv_today_earnings_title;
	private TextView tv_today_earnings_money;
	private TextView tv_today_earnings_yuan;
	private TextView tv_all_earnings_title;
	private TextView tv_all_earnings_money;
	private TextView tv_all_earnings_yuan;

	private TextView tv_society_title;
	private TextView tv_my_society_title;
	private TextView tv_my_society_count;
	private TextView tv_my_circle_title;
	private TextView tv_my_circle_count;

	private TextView tv_products_title;
	private TextView tv_products_online_title;
	private TextView tv_products_online_count;
	private TextView tv_products_offlining_title;
	private TextView tv_products_offlining_count;

	private TextView tv_sales_title;
	private TextView tv_today_sales_title;
	private TextView tv_today_sales_count;
	private TextView tv_all_sales_title;
	private TextView tv_all_sales_count;
	private TextView tv_top_title;
	private Button bt_top_right;

	private TextView tv_share_title;
	private TextView tv_online_share_title;
	private TextView tv_online_share_count;
	private TextView tv_all_share_title;
	private TextView tv_all_share_count;
	
	private RelativeLayout rl_sales;//销售管理
	private RelativeLayout rl_huokuan;//货款管理
	private RelativeLayout rl_earnings;//收益管理
	private RelativeLayout rl_share;//分享管理---暂无
	private RelativeLayout rl_products;//商品管理
	private RelativeLayout rl_social;//社交管理
	
	private PullToRefreshScrollView mPullRefreshScrollView;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		bitmapUtils = new BitmapUtils(getActivity());
		MyApplication.getInstance().getIndexRefreshService().addToList(this);
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			initView(inflater);
			// initFragment();
			// initViewPager();
			getIndexInfo(true);//联网获取数据
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.index_fragment_for_yangjia, null);
		mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
    	if(ToolsUtil.isNetworkConnected(getActivity())){
    		//刷新数据
    		getIndexInfo(false);
    	}else{
			Toast.makeText(getActivity(), "网络不可用，请稍后重试", Toast.LENGTH_SHORT).show();
		}
			}
		});
		tv_top_title = (TextView) view.findViewById(R.id.tv_top_title);
		tv_top_title.setVisibility(View.VISIBLE);
		tv_top_title.setText("首页");
		bt_top_right = (Button) view.findViewById(R.id.bt_top_right);
		bt_top_right.setVisibility(View.VISIBLE);
		bt_top_right.setBackgroundResource(R.drawable.exit);
		bt_top_right.setOnClickListener(this);
		
		
		tv_qr_name = (TextView) view.findViewById(R.id.tv_qr_name);
		iv_qr_code = (ImageView) view.findViewById(R.id.iv_qr_code);
		iv_qr_code.setOnClickListener(this);
		
		tv_huokuan_title = (TextView) view
				.findViewById(R.id.tv_huokuan_title);
		tv_today_huokuan_title = (TextView) view
				.findViewById(R.id.tv_today_huokuan_title);
		tv_today_huokuan_money = (TextView) view
				.findViewById(R.id.tv_today_huokuan_money);
		tv_today_huokuan_yuan = (TextView) view
				.findViewById(R.id.tv_today_huokuan_yuan);
		tv_all_huokuan_title = (TextView) view
				.findViewById(R.id.tv_all_huokuan_title);
		tv_all_huokuan_money = (TextView) view
				.findViewById(R.id.tv_all_huokuan);
		tv_all_huokuan_yuan = (TextView) view
				.findViewById(R.id.tv_all_huokuan_yuan);
		
		
		
		
		
		tv_earnings_title = (TextView) view
				.findViewById(R.id.tv_earnings_title);
		tv_today_earnings_title = (TextView) view
				.findViewById(R.id.tv_today_earnings_title);
		tv_today_earnings_money = (TextView) view
				.findViewById(R.id.tv_today_earnings_money);
		tv_today_earnings_yuan = (TextView) view
				.findViewById(R.id.tv_today_earnings_yuan);
		tv_all_earnings_title = (TextView) view
				.findViewById(R.id.tv_all_earnings_title);
		tv_all_earnings_money = (TextView) view
				.findViewById(R.id.tv_all_earnings_money);
		tv_all_earnings_yuan = (TextView) view
				.findViewById(R.id.tv_all_earnings_yuan);
		
		

		tv_society_title = (TextView) view.findViewById(R.id.tv_society_title);
		tv_my_society_title = (TextView) view.findViewById(R.id.tv_my_society_title);
		tv_my_society_count = (TextView) view.findViewById(R.id.tv_my_society_count);
		tv_my_circle_title = (TextView) view
				.findViewById(R.id.tv_my_circle_title);
		tv_my_circle_count = (TextView) view
				.findViewById(R.id.tv_my_circle_count);

		tv_products_title = (TextView) view
				.findViewById(R.id.tv_products_title);
		tv_products_online_title = (TextView) view
				.findViewById(R.id.tv_products_online_title);
		tv_products_online_count = (TextView) view
				.findViewById(R.id.tv_products_online_count);
		tv_products_offlining_title = (TextView) view
				.findViewById(R.id.tv_products_offlining_title);
		tv_products_offlining_count = (TextView) view
				.findViewById(R.id.tv_products_offlining_count);

		tv_sales_title = (TextView) view.findViewById(R.id.tv_sales_title);
		tv_today_sales_title = (TextView) view
				.findViewById(R.id.tv_today_sales_title);
		tv_today_sales_count = (TextView) view
				.findViewById(R.id.tv_today_sales_count);
		tv_all_sales_title = (TextView) view
				.findViewById(R.id.tv_all_sales_title);
		tv_all_sales_count = (TextView) view
				.findViewById(R.id.tv_all_sales_count);

		tv_share_title = (TextView) view.findViewById(R.id.tv_share_title);
		tv_online_share_title = (TextView) view
				.findViewById(R.id.tv_online_share_title);
		tv_online_share_count = (TextView) view
				.findViewById(R.id.tv_online_share_count);
		tv_all_share_title = (TextView) view
				.findViewById(R.id.tv_all_share_title);
		tv_all_share_count = (TextView) view
				.findViewById(R.id.tv_all_share_count);

		rl_huokuan = (RelativeLayout) view.findViewById(R.id.rl_huokuan);
		rl_sales = (RelativeLayout) view.findViewById(R.id.rl_sales);
		rl_earnings = (RelativeLayout) view
				.findViewById(R.id.rl_earnings);
		rl_share = (RelativeLayout) view.findViewById(R.id.rl_share);
		rl_products = (RelativeLayout) view
				.findViewById(R.id.rl_products);
		rl_social = (RelativeLayout) view.findViewById(R.id.rl_social);

		rl_sales.setOnClickListener(this);
		rl_earnings.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		rl_products.setOnClickListener(this);
		rl_social.setOnClickListener(this);
		rl_huokuan.setOnClickListener(this);
		
		FontManager.changeFonts(getActivity(), tv_top_title, tv_earnings_title,
				tv_today_earnings_title, tv_today_earnings_money,
				tv_today_earnings_yuan, tv_all_earnings_title,
				tv_all_earnings_money, tv_all_earnings_yuan, tv_society_title,
				tv_my_society_title, tv_my_society_count, tv_my_circle_title,
				tv_my_circle_count, tv_products_title,
				tv_products_online_title, tv_products_online_count,
				tv_products_offlining_title, tv_products_offlining_count,
				tv_sales_title, tv_today_sales_title, tv_today_sales_count,
				tv_all_sales_title, tv_all_sales_count, tv_share_title,
				tv_online_share_title, tv_online_share_count,
				tv_all_share_title, tv_all_share_count,
				tv_huokuan_title,tv_today_huokuan_title,tv_today_huokuan_money,tv_today_huokuan_yuan,
				tv_all_huokuan_title,tv_all_huokuan_money,tv_all_huokuan_yuan,tv_qr_name);
	}


	private void initViewPager() {
		viewpager_main.setAdapter(myFragmentPagerAdapter);
		viewpager_main.setCurrentItem(0);
		viewpager_main.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			/*
			 * 页面跳转完成后调用的方法
			 */
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					iv_cursor_left.setVisibility(View.VISIBLE);
					iv_cursor_center.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.INVISIBLE);
				}
				if (arg0 == 1) {
					iv_cursor_center.setVisibility(View.VISIBLE);
					iv_cursor_left.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.INVISIBLE);
				}
				if (arg0 == 2) {
					iv_cursor_center.setVisibility(View.INVISIBLE);
					iv_cursor_left.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_qr_code://查看二維碼大图
			if(!TextUtils.isEmpty(codeUrl)){
				Intent intent = new Intent(getActivity(), BigImageShowActivity.class);
				intent.putExtra("title", tv_qr_name.getText().toString().trim());
				intent.putExtra("imageUrl", codeUrl);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(), "二维码为空", Toast.LENGTH_SHORT).show();
			}
//			Intent imageShowIntent = new Intent(getActivity(), ImageShowActivity.class);
//			imageShowIntent.putStringArrayListExtra(ImageShowActivity.BIGIMAGES,urlList);
//			imageShowIntent.putExtra("type", "base64");
//			imageShowIntent.putExtra(ImageShowActivity.IMAGE_INDEX, 0);
//			getActivity().startActivity(imageShowIntent);
			break;
		case R.id.rl_huokuan:// 货款管理
			
			Intent intentHuoKuan = new Intent(getActivity(), HuoKuanManagerActivity.class);
			startActivity(intentHuoKuan);
			
//			Intent intentSales = new Intent(getActivity(), SalesManagerForSupperManActivity.class);
//			startActivity(intentSales);
			break;
		case R.id.rl_sales:// 销售管理
			
			
			
			Intent intentSales = new Intent(getActivity(), SalesManagerForBuyerActivity.class);
			startActivity(intentSales);
			
			
//			Intent intentSales = new Intent(getActivity(), SalesManagerForSupperManActivity.class);
//			startActivity(intentSales);
			break;
		case R.id.rl_earnings://收益管理
			Intent intentEarning = new Intent(getActivity(), EarningManagerActivity.class);
			intentEarning.putExtra("earningData", income);
			startActivity(intentEarning);
			
			break;
		case R.id.rl_share://分享管理
			break;
		case R.id.rl_products://商品管理
			Intent intentProductManager = new Intent(getActivity(), ProductManagerActivity.class);
			startActivity(intentProductManager);
			break;
		case R.id.rl_social://粉丝管理
			Intent intentSocialManager = new Intent(getActivity(), SocialManagerActivity.class);
			startActivity(intentSocialManager);
			break;
		case R.id.bt_top_right:
//			SocicalShareUtil shareUtil = new SocicalShareUtil(getActivity());
//			shareUtil.showShareDialog();
//			Log.i("share", "---desc--"+data.getShare().getDesc());
//			Log.i("share", "---link--"+data.getShare().getShare_link());
//			Log.i("share", "---logo--"+data.getShare().getLogo());
			if(data!=null && data.getShare()!=null){
				ShareUtil.shareAll(getActivity(),data.getShare().getTitle(), data.getShare().getDesc(),  data.getShare().getShare_link(),  ToolsUtil.getImage(data.getShare().getLogo(), 200, 200),null);
			}
			
			
		default:
			break;
		}

	}

	
	
	/**
	 * 联网获取首页信息
	 */
	public void getIndexInfo(boolean showProgress){
		HttpControl httpControl=new HttpControl();
		httpControl.getBuyerIndexInfo(new HttpCallBackInterface() {
			

			@Override
			public void http_Success(Object obj) {
				mPullRefreshScrollView.onRefreshComplete();
				BuyerIndexInfoBean bean = (BuyerIndexInfoBean) obj;
				data = bean.getData();
				codeUrl = data.getBarcode();
				shopName = data.getShopname();
				Goodsamount goodsamount = data.getGoodsamount();
				tv_today_huokuan_money.setText(ToolsUtil.DounbleToString_2(goodsamount.getTodaygoodsamount()));
				tv_all_huokuan_money.setText(ToolsUtil.DounbleToString_2(goodsamount.getTotalgoodsamount()));
				byte[] bytes = Base64Coder.decode(codeUrl);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				iv_qr_code.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options));
				tv_qr_name.setText(ToolsUtil.nullToString(shopName));
				
				//处理返回来的数据
				Product product = data.getProduct();//商品管理
				tv_products_online_count.setText(ToolsUtil.nullToString(product!=null?product.getOnlineCount():""));
				tv_products_offlining_count.setText(ToolsUtil.nullToString(product!=null?product.getSoonDownCount():""));
				
				Favorite favorite = data.getFavorite();//社交管理
				tv_my_society_count.setText(ToolsUtil.nullToString(favorite!=null?favorite.getFavoritecount():""));
				tv_my_circle_count.setText(ToolsUtil.nullToString(favorite!=null?favorite.getGroupcount():""));
				
				Order order= data.getOrder();//销售管理
				tv_today_sales_count.setText(ToolsUtil.nullToString(order!=null?order.getTodayorder():""));
				tv_all_sales_count.setText(ToolsUtil.nullToString(order!=null?order.getAllorder():""));
				
				income = data.getIncome();
				tv_today_earnings_money.setText(ToolsUtil.DounbleToString_2(income.getToday_income()));
				tv_all_earnings_money.setText(ToolsUtil.DounbleToString_2(income.getTotal_income()));
				
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
				
			}
		}, getActivity(), showProgress, true);
		
		
		}
	@Override
	public void refresh() {
		getIndexInfo(false);
	}
	
	
	@Override
	public void onDestroy() {
		MyApplication.getInstance().getIndexRefreshService().removeFromList(this);
		super.onDestroy();
	}
	
	
	
	}
