package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.BaseFragmentActivity;
import com.shenma.yueba.R;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.yangjia.fragment.IncomeDetailFragment;

import java.util.ArrayList;


public class FindShoppingGuideFragmnet extends BaseFragment implements View.OnClickListener {
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private TextView tv_guide;
    private TextView tv_attention;
    private GuideFragment guideFragment;// 导购
    private AttentionFragment attentionFragment;// 关注
    private ArrayList<ImageView> cursorImageList = new ArrayList<ImageView>();
    private ArrayList<TextView> titleTextList = new ArrayList<TextView>();
    private ImageView iv_cursor_right;
    private ImageView iv_cursor_left;
    private String mParam1;
    private String mParam2;
    View parentView;

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
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.find_shopping_guide_layout, null);
            initView();
        }
        ViewGroup vp = (ViewGroup) parentView.getParent();
        if (vp != null) {
            vp.removeView(parentView);
        }
        return parentView;

    }

    private void initView() {
        guideFragment = new GuideFragment();
        attentionFragment = new AttentionFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction =  manager.beginTransaction();
        transaction.replace(R.id.ll_contener, guideFragment);
        transaction.commit();
        LinearLayout ll_contener = (LinearLayout)parentView.findViewById(R.id.ll_contener);
        tv_guide = (TextView) parentView.findViewById(R.id.tv_guide);
        tv_attention = (TextView) parentView.findViewById(R.id.tv_attention);
        iv_cursor_right = (ImageView) parentView.findViewById(R.id.iv_cursor_right);
        iv_cursor_left = (ImageView) parentView.findViewById(R.id.iv_cursor_left);
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




    public void onClick(View v) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction =  manager.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_guide://导购
                setCursorAndText(0, cursorImageList, titleTextList);
                transaction.replace(R.id.ll_contener,guideFragment);
                transaction.commit();
                break;
            case R.id.tv_attention://关注
                setCursorAndText(1, cursorImageList, titleTextList);
                transaction.replace(R.id.ll_contener,attentionFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }


}
