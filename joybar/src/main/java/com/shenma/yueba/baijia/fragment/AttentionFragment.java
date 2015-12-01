package com.shenma.yueba.baijia.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.GalleryAdapter;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.ProductsInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.BuyerProductsBackBean;
import com.shenma.yueba.baijia.modle.newmodel.FavBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.OtherBuyersBackBean;
import com.shenma.yueba.baijia.modle.newmodel.RecommondBuyerlistBackBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.JazzyViewPager;
import com.shenma.yueba.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 找导购--关注
 * Created by a on 2015/11/23.
 */
public class AttentionFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private int page = 1;
    private GalleryAdapter galleryAdapter;
    private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
    private ImageView iv_arrow_left, iv_arrow_right;
    private Gallery gallery;
    private int position;
    private JazzyViewPager mJazzy;
    private List<ProductsInfoBean> products = new ArrayList<ProductsInfoBean>();
    private ViewPagerAdapter viewPagerAdapter;
    int Rlheight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.attention_layout, null);
        gallery = (Gallery) view.findViewById(R.id.gallery);
        iv_arrow_left = (ImageView) view.findViewById(R.id.iv_arrow_left);
        iv_arrow_right = (ImageView) view.findViewById(R.id.iv_arrow_right);
        iv_arrow_left.setOnClickListener(this);
        iv_arrow_right.setOnClickListener(this);
        gallery.setSpacing(10);
        gallery.setOnItemSelectedListener(this);
        galleryAdapter = new GalleryAdapter(getActivity(), mList);
        gallery.setAdapter(galleryAdapter);
        getAttentionBuyer();
        setupJazziness(view, JazzyViewPager.TransitionEffect.Tablet);
        // getBuyersProducts("30944");
        // getOtherStoreBuyers("30944");
        // getRecommondBuyerlist("30944");
        // touch("30944");
        return view;

    }


    /**
     * 获取关注的买手
     */
    public void getAttentionBuyer() {
        HttpControl httpControl = new HttpControl();
        httpControl.getFavBuyers(page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                FavBuyersBackBean bean = (FavBuyersBackBean) obj;
                if (bean != null && bean.getData() != null) {
                    List<BuyerInfo> dataList = bean.getData().getBuyers();
                    if (dataList != null && dataList.size() > 0) {
                        int size = mList.size();
                        mList.addAll(dataList);
                        getBuyersProducts(mList.get(size).getBuyerId(),true);
                        galleryAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }

    /**
     * 获取买手的产品数据
     */
    public void getBuyersProducts(String buyerId, final boolean isChange) {
        HttpControl httpControl = new HttpControl();
        httpControl.getBuyersProducts(buyerId, page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                BuyerProductsBackBean bean = (BuyerProductsBackBean) obj;
                if (bean != null && bean.getData() != null) {
                    if(isChange){
                        products.clear();
                        //viewPagerAdapter.notifyDataSetChanged();
                    }
                    List<ProductsInfoBean> dataList = bean.getData().getProducts();
                    products.addAll(dataList);
                    mJazzy.setAdapter(viewPagerAdapter);
                   // viewPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    /**
     * 同商场的其他买手
     */
    public void getOtherStoreBuyers(String buyerId) {
        HttpControl httpControl = new HttpControl();
        httpControl.getOtherStoreBuyers(buyerId, page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                OtherBuyersBackBean bean = (OtherBuyersBackBean) obj;


            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }




    /**
     * 戳一下
     */
    public void touch(String buyerId) {
        HttpControl httpControl = new HttpControl();
        httpControl.touch(buyerId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                BaseRequest bean = (BaseRequest) obj;
                if (bean.isSuccessful()) {
                    Toast.makeText(getActivity(), "提醒成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "提醒失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity());
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        galleryAdapter.setSelectItem(position);  //当滑动时，事件响应，调用适配器中的这个方法。
        AttentionFragment.this.position = position;
        if (position == mList.size() - 1) {
            page++;
            getAttentionBuyer();
        }
        getBuyersProducts(mList.get(position).getBuyerId(),true);


    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {//抽象方法，必须实现
        // TODO Auto-generated method stub

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrow_left://左箭头
                if (position > 0) {
                    gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
                }
                break;
            case R.id.iv_arrow_right://右箭头
                if (position < mList.size() - 1) {
                    gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                }
                break;
            default:
                break;
        }
    }


    private void setupJazziness(View view, JazzyViewPager.TransitionEffect effect) {
        viewPagerAdapter = new ViewPagerAdapter(products);
        mJazzy = (JazzyViewPager) view.findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(effect);
        mJazzy.setAdapter(viewPagerAdapter);
        mJazzy.setPageMargin(30);

        ViewGroup.LayoutParams params =  mJazzy.getLayoutParams();
//        View view2 = View.inflate(getActivity(), R.layout.attention_item_layout, null);
//        final RelativeLayout rl = (RelativeLayout)view2.findViewById(R.id.rl_content);
//        int height = rl.getLayoutParams().height;
//        rl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                Rlheight = rl.getMeasuredHeight();
//                rl.getViewTreeObserver().removeOnPreDrawListener(this);
//                return true;
//            }
//        });
        params.height = ToolsUtil.getDisplayWidth(getActivity())-ToolsUtil.dip2px(getActivity(), 60)+ToolsUtil.dip2px(getActivity(),80);
        mJazzy.setLayoutParams(params);
    }

    private class ViewPagerAdapter
            extends PagerAdapter {
        List<ProductsInfoBean> products;

        public ViewPagerAdapter(List<ProductsInfoBean> products) {
            ViewPagerAdapter.this.products = products;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = View.inflate(getActivity(), R.layout.attention_item_layout, null);
            ImageView iv_product = (ImageView) view.findViewById(R.id.iv_product);
            ViewGroup.LayoutParams params = iv_product.getLayoutParams();
            params.width = ToolsUtil.getDisplayWidth(getActivity())-ToolsUtil.dip2px(getActivity(), 60);
            params.height = ToolsUtil.getDisplayWidth(getActivity())-ToolsUtil.dip2px(getActivity(),60);
            iv_product.setLayoutParams(params);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
            ImageView iv_collection = (ImageView) view.findViewById(R.id.iv_collection);
            TextView tv_introduce = (TextView) view.findViewById(R.id.tv_introduce);
            MyApplication.getInstance().getImageLoader().displayImage(products.get(position).getPic(),iv_product);
            tv_price.setText(products.get(position).getPrice() + "");
            tv_introduce.setText(products.get(position).getProductName());
            iv_collection.setBackgroundDrawable(products.get(position).isFavite() ? getResources().getDrawable(R.drawable.collect) : getResources().getDrawable(R.drawable.uncollect));
            container.addView(view, MyViewPager.LayoutParams.MATCH_PARENT, MyViewPager.LayoutParams.MATCH_PARENT);
            mJazzy.setObjectForPosition(view, position);
            return view;
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
