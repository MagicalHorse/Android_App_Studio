package com.shenma.yueba.baijia.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.yangjia.modle.HuoKuanListBackBean;

import java.util.ArrayList;

/**
 * 找导购--导购
 * Created by a on 2015/11/23.
 */
public class GuideFragment extends BaseFragment {
    private int page = 1;
    private ViewPager find_guide_viewpager;
    private ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<android.support.v4.app.Fragment>();
    private GuideItemFragment itemFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_layout, null);
        find_guide_viewpager = (ViewPager) view.findViewById(R.id.find_guide_viewpager);
        find_guide_viewpager.setAdapter(new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager()));
        return view;


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


    private void getDataFromNet(boolean showDialog, Context ctx) {
//        HttpControl httpContorl = new HttpControl();
//        httpContorl.getHuoKuanList(page, status, new HttpControl.HttpCallBackInterface() {
//            @Override
//            public void http_Success(Object obj) {
//                rlv.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rlv.onRefreshComplete();
//                    }
//                }, 100);
//                HuoKuanListBackBean bean = (HuoKuanListBackBean) obj;
//                if (isRefresh) {
//                    ids.clear();//清空保存的ID
//                    mList.clear();//清空列表
//                    adapter.clearCountList();//清空数据和价格
//                    tv_bottom.setText("提现货款");//初始化提现货款按钮
//                    if (bean.getData() != null
//                            && bean.getData().getItems() != null) {
//                        if (bean.getData().getItems().size() > 0) {
//                            tv_nodata.setVisibility(View.GONE);
//                        } else {
//                            tv_nodata.setVisibility(View.VISIBLE);
//                        }
//                        mList.addAll(bean.getData().getItems());
//                    } else {
//                        mList.clear();//清空列表
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    if (bean.getData().getItems() != null
//                            && bean.getData().getItems().size() > 0) {
//                        mList.addAll(bean.getData().getItems());
//                    } else {
//                        Toast.makeText(getActivity(), "没有更多数据了...", 1000)
//                                .show();
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void http_Fails(int error, String msg) {
//                rlv.onRefreshComplete();
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//            }
//        }, ctx, true, false);

    }
}
