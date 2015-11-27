package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.BaseFragmentActivity;
import com.shenma.yueba.R;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.yangjia.fragment.IncomeDetailFragment;

import java.util.ArrayList;


public class FindShoppingGuideFragmnet extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView tv_guide;
    private TextView tv_attention;
    private ViewPager find_guide_viewpager;
    private GuideFragment guideFragment;// 导购
    private AttentionFragment attentionFragment;// 关注
    private ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<android.support.v4.app.Fragment>();
    private ArrayList<ImageView> cursorImageList = new ArrayList<ImageView>();
    private ArrayList<TextView> titleTextList = new ArrayList<TextView>();
    private ImageView iv_cursor_right;
    private ImageView iv_cursor_left;
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FindShoppingGuideFragmnet.
     */
    public FindShoppingGuideFragmnet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initView();
        initViewPager();
        return view;
    }

    private void initView() {
        guideFragment = new GuideFragment();
        attentionFragment = new AttentionFragment();
        fragmentList.add(guideFragment);
        fragmentList.add(attentionFragment);
        view = View.inflate(getActivity(), R.layout.find_shopping_guide_layout, null);
        find_guide_viewpager = (ViewPager) view.findViewById(R.id.find_guide_viewpager);
        tv_guide = (TextView) view.findViewById(R.id.tv_guide);
        tv_attention = (TextView) view.findViewById(R.id.tv_attention);
        iv_cursor_right = (ImageView) view.findViewById(R.id.iv_cursor_right);
        iv_cursor_left = (ImageView) view.findViewById(R.id.iv_cursor_left);
        cursorImageList.add(iv_cursor_left);
        cursorImageList.add(iv_cursor_right);
        iv_cursor_left.setVisibility(View.VISIBLE);
        tv_guide.setTextSize(20);
        tv_guide.setTextColor(getResources().getColor(R.color.main_color));
        titleTextList.add(tv_guide);
        titleTextList.add(tv_attention);
        tv_guide.setOnClickListener(this);
        tv_attention.setOnClickListener(this);


    }


    private void initViewPager() {
        find_guide_viewpager.setAdapter(new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager()));
        find_guide_viewpager.setCurrentItem(0);
        find_guide_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            /*
             * 页面跳转完成后调用的方法
             */
            public void onPageSelected(int arg0) {
//                fragmentList.get(arg0).getData(false, arg0, getActivity());
//                for (int i = 0; i < fragmentList.size(); i++) {
//                    if (arg0 != i) {
//                        fragmentList.get(arg0).tv_nodata.setVisibility(View.GONE);
//                    }
//                }
                setCursorAndText(arg0, cursorImageList, titleTextList);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    /**
     * ViewPager适配器
     *
     * @author len
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide://导购
                find_guide_viewpager.setCurrentItem(0);
                break;
            case R.id.tv_attention://关注
                find_guide_viewpager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }


}
