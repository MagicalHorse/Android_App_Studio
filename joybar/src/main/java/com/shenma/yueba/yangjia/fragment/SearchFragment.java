package com.shenma.yueba.yangjia.fragment;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.MyFragmentPagerAdapter;
import com.shenma.yueba.baijia.fragment.BaseFragment;
import com.shenma.yueba.baijia.fragment.BrandFragment;
import com.shenma.yueba.util.FontManager;

/**
 * 发现
 * 
 * @author a
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchFragment extends BaseFragment implements OnClickListener {
	private BrandFragment brandFragment;// 品牌
	private TagFragment tagFragment;// 标签
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("CircleFragment", "oncreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("CircleFragment", "oncreateView");

		if (view == null) {
			initViews(inflater);
			initFragment();
			initViewPager();
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initViewPager() {
		viewpager_search.setAdapter(myFragmentPagerAdapter);
		viewpager_search.setCurrentItem(0);
		viewpager_search.setOnPageChangeListener(new OnPageChangeListener() {

			// private boolean isScrolled = false;

			// public void onPageScrollStateChanged(int arg0) {
			// /*
			// * 页卡正常滑动时，会经历 1-2-0的三个阶段；
			// *
			// * 页卡在最后一页向右滑，或者第一页向左滑经历 1-0-2-0的阶段；
			// *
			// * 直接调用 setCurrentItem则只是经历 2-0的阶段
			// */
			// switch (arg0) {
			// /*
			// * 每一次的滑动arg0都会经历1-2-0的阶段，但是在最后一个页面向右滑，或者第一个页面向左滑时会经历 1-0-2-0的阶段
			// * 例如： 最后页面右滑时
			// * ，刚刚开始会是1，这时isScrolled为false，但是没有下一页面，所以此时的页面还是最后一个页面，满足if条件
			// */
			// case 0:
			//
			// if (viewPager.getCurrentItem() == viewPager.getAdapter()
			// .getCount() - 1 && !isScrolled)
			// viewPager.setCurrentItem(0);
			// else if (viewPager.getCurrentItem() == 0 && !isScrolled) {
			// viewPager.setCurrentItem(viewPager.getAdapter()
			// .getCount() - 1);
			// }
			// break;
			// case 1:
			// isScrolled = false;
			// break;
			// case 2:
			// isScrolled = true;
			// break;
			// }
			// }

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
					iv_cursor_left.setVisibility(View.INVISIBLE);
					iv_cursor_center.setVisibility(View.VISIBLE);
					iv_cursor_right.setVisibility(View.INVISIBLE);
				}
				if (arg0 == 2) {
					iv_cursor_left.setVisibility(View.INVISIBLE);
					iv_cursor_center.setVisibility(View.INVISIBLE);
					iv_cursor_right.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		// viewpager_circle.setOnPageChangeListener(new OnPageChangeListener() {
		// @Override
		// public void onPageSelected(int arg0) {
		// if (arg0 == 1) {
		// iv_cursor_right.setVisibility(View.VISIBLE);
		// iv_cursor_left.setVisibility(View.INVISIBLE);
		// }
		// if (arg0 == 0) {
		// iv_cursor_right.setVisibility(View.INVISIBLE);
		// iv_cursor_left.setVisibility(View.VISIBLE);
		// }
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		//
		//
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	private void initFragment() {
		brandFragment = new BrandFragment();
		tagFragment = new TagFragment();
		buyerFragment = new BuyerFragment();
		fragmentList.add(brandFragment);
		fragmentList.add(tagFragment);
		fragmentList.add(buyerFragment);
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(
				getChildFragmentManager(), fragmentList);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_brand:// 品牌
			viewpager_search.setCurrentItem(0);
			break;
		case R.id.tv_tag:// 标签
			viewpager_search.setCurrentItem(1);
			break;
		case R.id.tv_buyer:// 买手
			viewpager_search.setCurrentItem(2);
			break;
		default:
			break;
		}

	}

	/**
	 * 初始化view
	 */
	private void initViews(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.search_layout, null);
		et_search = (EditText) view.findViewById(R.id.et_search);
		bt_search = (Button) view.findViewById(R.id.bt_searchs);
		viewpager_search = (ViewPager) view.findViewById(R.id.viewpager_search);
		iv_cursor_left = (ImageView) view.findViewById(R.id.iv_cursor_left);
		iv_cursor_left.setVisibility(View.VISIBLE);
		iv_cursor_center = (ImageView) view.findViewById(R.id.iv_cursor_center);
		iv_cursor_right = (ImageView) view.findViewById(R.id.iv_cursor_right);
		iv_cursor_left.setVisibility(View.VISIBLE);
		tv_brand = (TextView) view.findViewById(R.id.tv_brand);
		tv_tag = (TextView) view.findViewById(R.id.tv_tag);
		tv_buyer = (TextView) view.findViewById(R.id.tv_buyer);
		bt_search.setOnClickListener(this);
		tv_brand.setOnClickListener(this);
		tv_tag.setOnClickListener(this);
		tv_buyer.setOnClickListener(this);
		FontManager.changeFonts(getActivity(), et_search, bt_search, tv_brand,
				tv_tag, tv_buyer);
	}

	@Override
	public void onResume() {
		Log.i("CircleFragment", "onResume");
		super.onResume();
	}
}
