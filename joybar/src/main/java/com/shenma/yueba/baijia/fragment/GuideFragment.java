package com.shenma.yueba.baijia.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.shenma.yueba.baijia.activity.AttationListActivity;
import com.shenma.yueba.baijia.activity.BaijiaProductInfoActivity;
import com.shenma.yueba.baijia.activity.ShopMainActivity;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.RecommondBuyerlistBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.JazzyViewPager;
import com.shenma.yueba.view.MyViewPager;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;
import com.shenma.yueba.yangjia.modle.HuoKuanListBackBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 找导购--导购
 * Created by a on 2015/11/23.
 */
public class GuideFragment extends BaseFragment {
    private int page = 1;
    private JazzyViewPager jazzy_pager;
    private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
    private ViewPagerAdapter pageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_layout, null);
        jazzy_pager = (JazzyViewPager) view.findViewById(R.id.jazzy_pager);
        jazzy_pager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
        jazzy_pager.setPageMargin(30);
        pageAdapter = new ViewPagerAdapter(mList);
        jazzy_pager.setAdapter(pageAdapter);
        jazzy_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position){
                if(mList.size()>=5 && position == mList.size()-3){
                    page++;
                    getRecommondBuyerlist(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getRecommondBuyerlist(true);
        return view;
    }


    /**
     * 推荐的买手/导购
     */
    public void getRecommondBuyerlist(boolean first) {
        HttpControl httpControl = new HttpControl();
        httpControl.getRecommondBuyerlist(first,page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RecommondBuyerlistBackBean bean = (RecommondBuyerlistBackBean) obj;
                if (bean != null && bean.getData() != null) {
                    List<BuyerInfo> buyers = bean.getData().getBuyers();
                    mList.addAll(buyers);
                    pageAdapter.notifyDataSetChanged();

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
            TextView tv_brand_name = (TextView) view.findViewById(R.id.tv_brand_name);
            RoundImageView riv_head = (RoundImageView) view.findViewById(R.id.riv_head);
            TextView tv_buyer_name = (TextView) view.findViewById(R.id.tv_buyer_name);
            TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
            final TextView tv_attention = (TextView) view.findViewById(R.id.tv_attention);
            if (mList.get(position) != null && mList.get(position).getProducts() != null) {
                if (mList.get(position).getProducts() != null && mList.get(position).getProducts().size() == 1) {
                    View imageView1 = View.inflate(getActivity(), R.layout.one_pic_layout, null);
                    ImageView iv_one = (ImageView) imageView1.findViewById(R.id.iv_one);
                    ViewGroup.LayoutParams params1 = iv_one.getLayoutParams();
                    params1.width = ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60);
                    params1.height = ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60);
                    iv_one.setLayoutParams(params1);
                    iv_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(0).getProductId());
                        }
                    });
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), iv_one);
                    guide_ll_container.addView(imageView1);
                } else if (mList.get(position).getProducts() != null && mList.get(position).getProducts().size() == 2) {
                    View imageView2 = View.inflate(getActivity(), R.layout.two_pic_layout, null);
                    ImageView iv_one = (ImageView) imageView2.findViewById(R.id.iv_one);
                    ImageView iv_two = (ImageView) imageView2.findViewById(R.id.iv_two);
                    ViewGroup.LayoutParams params1 = iv_one.getLayoutParams();
                    params1.width = (ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60)) / 2;
                    params1.height = ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60);
                    iv_one.setLayoutParams(params1);
                    iv_two.setLayoutParams(params1);
                    iv_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(0).getProductId());
                        }
                    });
                    iv_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(1).getProductId());
                        }
                    });
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), iv_one);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), iv_two);
                    guide_ll_container.addView(imageView2);
                } else if (mList.get(position).getProducts() != null && mList.get(position).getProducts().size() == 3) {
                    View imageView3 = View.inflate(getActivity(), R.layout.three_pic_layout, null);
                    ImageView iv_one = (ImageView) imageView3.findViewById(R.id.iv_one);
                    ImageView iv_two = (ImageView) imageView3.findViewById(R.id.iv_two);
                    ImageView iv_three = (ImageView) imageView3.findViewById(R.id.iv_three);
                    ViewGroup.LayoutParams params1 = iv_one.getLayoutParams();
                    params1.width = (ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60)) / 2;
                    params1.height = (ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60)) / 2;
                    iv_one.setLayoutParams(params1);
                    iv_two.setLayoutParams(params1);
                    iv_three.setLayoutParams(params1);
                    iv_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(0).getProductId());
                        }
                    });
                    iv_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(1).getProductId());
                        }
                    });
                    iv_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(2).getProductId());
                        }
                    });
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), iv_one);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), iv_two);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(2).getPic(), iv_three);
                    guide_ll_container.addView(imageView3);
                } else if (mList.get(position).getProducts() != null && mList.get(position).getProducts().size() >= 4) {
                    View imageView4 = View.inflate(getActivity(), R.layout.four_pic_layout, null);
                    ImageView iv_one = (ImageView) imageView4.findViewById(R.id.iv_one);
                    ImageView iv_two = (ImageView) imageView4.findViewById(R.id.iv_two);
                    ImageView iv_three = (ImageView) imageView4.findViewById(R.id.iv_three);
                    ImageView iv_four = (ImageView) imageView4.findViewById(R.id.iv_four);
                    ViewGroup.LayoutParams params1 = iv_one.getLayoutParams();
                    params1.width = (ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60)) / 2;
                    params1.height = (ToolsUtil.getDisplayWidth(getActivity()) - ToolsUtil.dip2px(getActivity(), 60)) / 2;
                    iv_one.setLayoutParams(params1);
                    iv_two.setLayoutParams(params1);
                    iv_three.setLayoutParams(params1);
                    iv_four.setLayoutParams(params1);
                    iv_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(0).getProductId());
                        }
                    });
                    iv_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(1).getProductId());
                        }
                    });
                    iv_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(),mList.get(position).getProducts().get(2).getProductId());
                        }
                    });
                    iv_four.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forwardProductInfoActivity(getActivity(), mList.get(position).getProducts().get(3).getProductId());
                        }
                    });
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(0).getPic(), iv_one);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(1).getPic(), iv_two);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(2).getPic(), iv_three);
                    MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getProducts().get(3).getPic(), iv_four);
                    guide_ll_container.addView(imageView4);
                }

            }
            tv_brand_name.setText(ToolsUtil.nullToString(mList.get(position).getBrandName()));
            tv_buyer_name.setText(ToolsUtil.nullToString(mList.get(position).getNickName()));
            tv_address.setText(ToolsUtil.nullToString(mList.get(position).getAddress()));
            MyApplication.getInstance().getImageLoader().displayImage(mList.get(position).getLogo(), riv_head);
            riv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forwardShopMainActivity(getActivity(),Integer.valueOf(mList.get(position).getBuyerId()));
                }
            });
            tv_attention.setText(mList.get(position).isFllowed() ? "已关注" : "关注");
            tv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).isFllowed()) {
                        sendAttation(position,mList.get(position).getBuyerId(), 0, tv_attention);
                    } else {
                        sendAttation(position,mList.get(position).getBuyerId(), 1, tv_attention);
                    }
                }
            });
            container.addView(view, MyViewPager.LayoutParams.MATCH_PARENT, MyViewPager.LayoutParams.MATCH_PARENT);
            jazzy_pager.setObjectForPosition(view, position);
            return view;
        }


        void sendAttation(final int position,final String userId, final int Status, final TextView tv_attention) {
            HttpControl httpControl = new HttpControl();
            httpControl.setFavoite(userId, Status, new HttpControl.HttpCallBackInterface() {
                @Override
                public void http_Success(Object obj) {
                    if (Status == 0) //1表示关注 0表示取消关注
                    {
                        tv_attention.setText("关注");
                        mList.get(position).setIsFllowed(false);
                    } else if (Status == 1) {
                        tv_attention.setText("已关注");
                        mList.get(position).setIsFllowed(true);
                    }
                }
                @Override
                public void http_Fails(int error, String msg) {
                    MyApplication.getInstance().showMessage(getActivity(), msg);
                }
            }, getActivity());
        }

        class Holder {
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



    /******
     * 跳转到商品详情
     *
     * @param id intd
     ****/
    public static void forwardProductInfoActivity(Context ctx, int id) {
        Intent intent = new Intent(ctx, BaijiaProductInfoActivity.class);
        intent.putExtra("productID", id);
        ctx.startActivity(intent);
    }

    /******
     * @param id int
     ****/
    public static void forwardShopMainActivity(Context ctx, int id) {
        ToolsUtil.forwardShopMainActivity(ctx, id);
    }
}
