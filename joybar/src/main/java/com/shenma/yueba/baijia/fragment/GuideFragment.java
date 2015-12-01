package com.shenma.yueba.baijia.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.RecommondBuyerlistBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.MyViewPager;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.modle.HuoKuanListBackBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 找导购--导购
 * Created by a on 2015/11/23.
 */
public class GuideFragment extends BaseFragment {
    private int page = 1;
    private ViewPager find_guide_viewpager;
    private GuideItemFragment itemFragment;
    private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
    private ViewPagerAdapter pageAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_layout, null);
        find_guide_viewpager = (ViewPager) view.findViewById(R.id.find_guide_viewpager);
        getRecommondBuyerlist();
        return view;
    }



    /**
     * 推荐的买手/导购
     */
    public void getRecommondBuyerlist() {
        HttpControl httpControl = new HttpControl();
        httpControl.getRecommondBuyerlist(page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RecommondBuyerlistBackBean bean = (RecommondBuyerlistBackBean) obj;
                if(bean!=null && bean.getData()!=null){
                    List<BuyerInfo> buyers =  bean.getData().getBuyers();
                    mList.addAll(buyers);
                    pageAdapter = new ViewPagerAdapter(mList);
                    find_guide_viewpager.setAdapter(pageAdapter);
                }

            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }



    private class ViewPagerAdapter
            extends PagerAdapter {
        List<BuyerInfo> products;

        public ViewPagerAdapter(List<BuyerInfo> products) {
            ViewPagerAdapter.this.products = products;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = View.inflate(getActivity(), R.layout.shopping_guide_layout, null);
            LinearLayout guide_ll_container = (LinearLayout) view.findViewById(R.id.guide_ll_container);
            TextView tv_brand_name = (TextView)view.findViewById(R.id.tv_brand_name) ;
            RoundImageView riv_head = (RoundImageView)view.findViewById(R.id.riv_head);
            TextView tv_buyer_name =(TextView) view.findViewById(R.id.tv_buyer_name);
            TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
            TextView tv_attention = (TextView) view.findViewById(R.id.tv_attention);
//            if(mList.get(position)!=null && mList.get(position).getProducts()!=null) {
//                if (mList.size() == 1) {
//                    View ImageView = View.inflate(getActivity(), R.layout.one_pic_layout, null);
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), (ImageView) ImageView.findViewById(R.id.iv_one));
//                    guide_ll_container.addView(ImageView);
//                } else if (mList.size() == 2) {
//                    View ImageView = View.inflate(getActivity(), R.layout.two_pic_layout, null);
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), (ImageView) ImageView.findViewById(R.id.iv_one));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), (ImageView) ImageView.findViewById(R.id.iv_two));
//                    guide_ll_container.addView(ImageView);
//                } else if (mList.size() == 3) {
//                    View ImageView = View.inflate(getActivity(), R.layout.three_pic_layout, null);
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), (ImageView) ImageView.findViewById(R.id.iv_one));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), (ImageView) ImageView.findViewById(R.id.iv_two));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(2).getPic(), (ImageView) ImageView.findViewById(R.id.iv_three));
//                    guide_ll_container.addView(ImageView);
//                } else if (mList.size() >= 4) {
//                    View ImageView = View.inflate(getActivity(), R.layout.four_pic_layout, null);
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), (ImageView) ImageView.findViewById(R.id.iv_one));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), (ImageView) ImageView.findViewById(R.id.iv_two));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(2).getPic(), (ImageView) ImageView.findViewById(R.id.iv_three));
//                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(3).getPic(), (ImageView) ImageView.findViewById(R.id.iv_four));
//                    guide_ll_container.addView(ImageView);
//                }
//
//            }
//            tv_brand_name.setText(ToolsUtil.nullToString(mList.get(position).getBrandName()));
//            tv_buyer_name.setText(ToolsUtil.nullToString(mList.get(position).getNickName()));
//            tv_address.setText(ToolsUtil.nullToString(mList.get(position).getAddress()));
//            MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getLogo(),riv_head);
            return view;
        }



        class Holder{
            LinearLayout guide_ll_container;
            TextView tv_brand_name;
            RoundImageView riv_head;
            TextView tv_buyer_name;
            TextView tv_address;
            TextView tv_attention;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView((View) obj);
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
