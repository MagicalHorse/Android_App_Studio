package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.MyFragmentPagerAdapter;
import com.shenma.yueba.baijia.fragment.BrandFragment;
import com.shenma.yueba.baijia.fragment.BuyerFragment;
import com.shenma.yueba.util.FontManager;
import com.umeng.analytics.MobclickAgent;



/**
 * 查询的界面
 * @author a
 *
 */
public class SearchActivity extends FragmentActivity implements OnClickListener {
	
	private BrandFragment brandFragment;// 品牌
	private BuyerFragment buyerFragment;// 买手
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private ViewPager viewpager_search;
	private ImageView iv_cursor_left, iv_cursor_center, iv_cursor_right;
	private Button bt_search, bt_msg;
	private TextView tv_recommended_circle;
	private RelativeLayout rl_my_circle;
	private View view;
	private MyFragmentPagerAdapter myFragmentPagerAdapter;
	private EditText et_search;
	private TextView tv_brand;//品牌
	private TextView tv_tag;//标签
	private TextView tv_buyer;//买手
	int currID=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);
		MyApplication.getInstance().addActivity(this);
		
		initView();
		initFragment();
		initViewPager();
	}

	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		bt_search = (Button) findViewById(R.id.bt_searchs);
		viewpager_search = (ViewPager) findViewById(R.id.viewpager_search);
		iv_cursor_left = (ImageView) findViewById(R.id.iv_cursor_left);
		iv_cursor_left.setVisibility(View.VISIBLE);
		iv_cursor_center = (ImageView) findViewById(R.id.iv_cursor_center);
		iv_cursor_right = (ImageView) findViewById(R.id.iv_cursor_right);
		iv_cursor_left.setVisibility(View.VISIBLE);
		tv_brand = (TextView) findViewById(R.id.tv_brand);
		tv_tag = (TextView) findViewById(R.id.tv_tag);
		tv_buyer = (TextView) findViewById(R.id.tv_buyer);
		bt_search.setOnClickListener(this);
		tv_brand.setOnClickListener(this);
		tv_tag.setOnClickListener(this);
		tv_buyer.setOnClickListener(this);
		FontManager.changeFonts(this, et_search, bt_search, tv_brand,
				tv_tag, tv_buyer);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_brand:// 品牌
			currID=0;
			viewpager_search.setCurrentItem(0);
			break;
		case R.id.tv_tag:// 标签
			//viewpager_search.setCurrentItem(1);
			break;
		case R.id.tv_buyer:// 买手
			currID=1;
			viewpager_search.setCurrentItem(1);
			break;
		case R.id.bt_searchs://搜索按钮
			searchData();
			break;
		default:
			break;
		}

	}
	
	/****
	 * 查询数据
	 * **/
	void searchData()
	{
		String value=et_search.getText().toString().trim();
		if(value==null || value.equals(""))
		{
			return;
		}
		if(currID>-1 &&  currID<fragmentList.size())
		{
			switch(currID)
			{
			case 0:
				brandFragment.searchData(value);
				break;
			case 1:
				buyerFragment.searchData(value);
				break;
			}
		}
	}
	
	private void initFragment() {
		brandFragment = new BrandFragment();
		buyerFragment = new BuyerFragment();
		fragmentList.add(brandFragment);
		fragmentList.add(buyerFragment);
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

	}
	
	private void initViewPager() {
		viewpager_search.setAdapter(myFragmentPagerAdapter);
		currID=0;
		viewpager_search.setCurrentItem(0);
		viewpager_search.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			/*
			 * 页面跳转完成后调用的方法
			 */
			public void onPageSelected(int arg0) {
				currID=arg0;
				switch(arg0)
				{
				case 0:
					iv_cursor_left.setVisibility(View.VISIBLE);
					//iv_cursor_center.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.INVISIBLE);
					break;
				case 1:
					iv_cursor_left.setVisibility(View.INVISIBLE);
					//iv_cursor_center.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.VISIBLE);
					break;
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});

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
	    	MyApplication.getInstance().removeActivity(this);//加入回退栈
	    	super.onDestroy();
	    }
	
}
