package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.AttationListActivity;
import com.shenma.yueba.baijia.activity.CircleListActivity;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.baijia.modle.UserInfoBean;
import com.shenma.yueba.broadcaseReceiver.ProductFavorBroadcase;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ShopMainFragment extends Fragment {
    View contantView;
    LayoutInflater layoutInflater;
    FragmentManager fragmentManager;
    //买手logo
    RoundImageView shop_main_layout_icon_imageview;
    //店铺名称
    TextView shop_main_layout_name_textview;
    //商场名称
    TextView shop_main_layout_market_textview;
    //私聊按钮
    Button shop_main_siliao_imagebutton;
    //关注按钮
    TextView shop_main_attention_imagebutton;
    //主要内容
    FrameLayout shop_main_layout_tabcontent_framelayout;
    //地址
    TextView shop_main_head_layout_address_textview;
    //商品描述
    TextView shap_main_description1_textview, shap_main_description2_textview, shap_main_description3_textview;
    LinearLayout shop_main_head_layout_tab_linearlayout;
    //商品  上新
    List<FragmentBean> fragmentBean_list = new ArrayList<FragmentBean>();
    List<View> view_list = new ArrayList<View>();
    PullToRefreshScrollView shop_main_layout_title_pulltorefreshscrollview;

    ProductFavorBroadcase productFavorBroadcase;//商品收藏广播监听
    boolean isregisterProductFavorListener = false;//是否注册商品收藏广播监听 true是  false 否
    int currId = -1;
    HttpControl httpControl = new HttpControl();
    UserInfoBean userInfoBean;
    int userID = -1;
    Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        userID = getArguments().getInt("userID", -1);
        layoutInflater = LayoutInflater.from(activity);
        fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contantView == null) {
            initView();
            getBaijiaUserInfo();//获取用户数据
        }
        //注册商品收藏广播监听
        registerProductFavorListener();
        ViewGroup parentview=(ViewGroup)contantView.getParent();
        if(parentview!=null)
        {
            parentview.removeView(contantView);
        }
        return contantView;
    }

    /*****
     * 初始化数据
     ***/
    void initView() {
        //加载显示的视图信息
        contantView = layoutInflater.inflate(R.layout.shopinfolayout, null);
        initPullRefresh();//下拉刷新 上啦加载
        initUserData();//初始化用户信息
        initPuBuliuData();//初始化 瀑布流数据

        /**************
         *圆圈中  关注 粉丝 圈子 的 文本信息  这里 用来 获取对象 改变文字形态
         * ********/
        TextView shop_main_attention_textview = (TextView) contantView.findViewById(R.id.shop_main_attention_textview);
        TextView shop_main_fans_textview = (TextView) contantView.findViewById(R.id.shop_main_fans_textview);
        TextView shop_main_praise_textview = (TextView) contantView.findViewById(R.id.shop_main_praise_textview);
        FontManager.changeFonts(activity, shop_main_layout_name_textview, shop_main_layout_market_textview, shap_main_description1_textview, shap_main_description2_textview, shap_main_description3_textview, shop_main_attention_textview, shop_main_fans_textview, shop_main_praise_textview, shop_main_attention_imagebutton, shop_main_siliao_imagebutton);

    }


    /******
     * 初始化下拉刷新 上啦加载 即操作
     ****/
    void initPullRefresh() {
        shop_main_layout_title_pulltorefreshscrollview = (PullToRefreshScrollView) contantView.findViewById(R.id.shop_main_layout_title_pulltorefreshscrollview);
        ToolsUtil.initPullResfresh(shop_main_layout_title_pulltorefreshscrollview, activity);
        shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
        shop_main_layout_title_pulltorefreshscrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                onAddData();
            }
        });
    }

    /******
     * 初始化用户信息
     ****/
    void initUserData() {

        /********************************************
         *  用户 头像  名称  按钮的信息
         * ******************************************/
        shop_main_layout_icon_imageview = (RoundImageView) contantView.findViewById(R.id.shop_main_layout_icon_imageview);//用户头像
        shop_main_layout_name_textview = (TextView) contantView.findViewById(R.id.shop_main_layout_name_textview);//名称
        shop_main_layout_market_textview = (TextView) contantView.findViewById(R.id.shop_main_layout_market_textview);//地址
        shop_main_siliao_imagebutton = (Button) contantView.findViewById(R.id.shop_main_siliao_imagebutton);//私聊按钮
        shop_main_siliao_imagebutton.setOnClickListener(onClickListener);
        shop_main_attention_imagebutton = (TextView) contantView.findViewById(R.id.shop_main_attention_imagebutton);//关注按钮
        shop_main_attention_imagebutton.setOnClickListener(onClickListener);
        shop_main_head_layout_address_textview=(TextView)contantView.findViewById(R.id.shop_main_head_layout_address_textview);//地址

        /********************************************
         *  店铺 描述的信息
         * ******************************************/
        shap_main_description1_textview = (TextView) contantView.findViewById(R.id.shap_main_description1_textview);//描述信息1
        shap_main_description2_textview = (TextView) contantView.findViewById(R.id.shap_main_description2_textview);//描述信息2
        shap_main_description3_textview = (TextView) contantView.findViewById(R.id.shap_main_description3_textview);//描述信息3

    }

    /*****
     * 初始化 瀑布流数据
     ****/
    void initPuBuliuData() {
        //瀑布流上面的 TAB切换 父视图
        shop_main_head_layout_tab_linearlayout = (LinearLayout) contantView.findViewById(R.id.shop_main_head_layout_tab_linearlayout);
        //瀑布流的 内容 即 对应的fragment
        shop_main_layout_tabcontent_framelayout = (FrameLayout) contantView.findViewById(R.id.shop_main_layout_tabcontent_framelayout);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.shop_main_siliao_imagebutton://私聊
                    if (!MyApplication.getInstance().isUserLogin(getActivity())) {
                        return;
                    }
                /*Intent siliaointent=new Intent(getActivity(),ChatActivity.class);
                siliaointent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				siliaointent.putExtra("Chat_NAME", userInfoBean.getUserName());
				siliaointent.putExtra("toUser_id",userID);
				startActivity(siliaointent);*/
                    ToolsUtil.forwardChatActivity(getActivity(), userInfoBean.getUserName(), userID, 0, null, null);
                    break;
                case R.id.shop_stay_layout_parent_linearlayout:
                    if (v.getTag() != null && v.getTag() instanceof Integer) {
                        //setItem(false, (Integer) v.getTag());//瀑布流tab按键
                    }
                    break;
                case R.id.shop_main_attention_imagebutton://关注
                    if (!MyApplication.getInstance().isUserLogin(getActivity())) {
                        return;
                    }
                    if (v.getTag() != null) {
                        UserInfoBean bean = (UserInfoBean) v.getTag();
                        if (bean.isIsFollowing())//如果是已关注
                        {
                            //取消关注
                            submitAttention(v, 0, bean);
                        } else {
                            //添加关注
                            submitAttention(v, 1, bean);
                        }
                    }
                    break;
            }
        }
    };


    /*****
     * 设置fragment显示的内容
     *
     * @param isfirst boolean true表示第一次添加  false 表示替换
     */
    void setItem(boolean isfirst, int _id) {
        if (currId == _id) {
            return;
        }

        currId = _id;
        //设置 TAB 视图
        for (int i = 0; i < view_list.size(); i++) {
            View parent = view_list.get(i);
            TextView tv1 = (TextView) parent.findViewById(R.id.shop_stay_layout_item_textview1);
            TextView tv2 = (TextView) parent.findViewById(R.id.shop_stay_layout_item_textview2);
            View shop_stay_layout_item_line_view = (View) parent.findViewById(R.id.shop_stay_layout_item_line_view);
            if (i == currId) {
                tv1.setTextColor(this.getResources().getColor(R.color.color_gray));
                tv2.setTextColor(this.getResources().getColor(R.color.color_gray));
                shop_stay_layout_item_line_view.setVisibility(View.GONE);
            } else {
                tv1.setTextColor(this.getResources().getColor(R.color.color_gray));
                tv2.setTextColor(this.getResources().getColor(R.color.color_gray));
                shop_stay_layout_item_line_view.setVisibility(View.GONE);
            }
        }

        try {
            if (isfirst) {
                if (!(((ShopPuBuliuFragment) fragmentBean_list.get(_id).getFragment()).isAdded())) {
                    fragmentManager.beginTransaction().add(R.id.shop_main_layout_tabcontent_framelayout, (ShopPuBuliuFragment) fragmentBean_list.get(_id).getFragment()).commitAllowingStateLoss();
                }
            } else {
                fragmentManager.beginTransaction().replace(R.id.shop_main_layout_tabcontent_framelayout, (ShopPuBuliuFragment) fragmentBean_list.get(_id).getFragment()).commitAllowingStateLoss();
            }
        } catch (Exception e) {

        }

    }


    /******
     * 下拉刷新
     ***/
    void onRefresh() {
        /*currId=-1;
    	getBaijiaUserInfo();*/
        if (fragmentBean_list != null && fragmentBean_list.size() > 0 && fragmentBean_list.size() > currId) {
            if (fragmentBean_list.get(currId).getFragment() != null && fragmentBean_list.get(currId).getFragment() instanceof ShopPuBuliuFragment) {
                ShopPuBuliuFragment fragment = (ShopPuBuliuFragment) fragmentBean_list.get(currId).getFragment();
                fragment.onPuBuliuRefersh();
            } else {
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
            }

        } else {
            ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
        }
    }

    /******
     * 上啦加载刷新
     ***/
    void onAddData() {

        if (fragmentBean_list != null && fragmentBean_list.size() > 0 && fragmentBean_list.size() > currId) {
            if (fragmentBean_list.get(currId).getFragment() != null && fragmentBean_list.get(currId).getFragment() instanceof ShopPuBuliuFragment) {
                ShopPuBuliuFragment fragment = (ShopPuBuliuFragment) fragmentBean_list.get(currId).getFragment();
                fragment.onPuBuliuaddData();
            } else {
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
            }

        } else {
            ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
        }
    }

    /*****
     * 下拉刷新监听
     **/
    public interface PubuliuFragmentListener {
        /***
         * 刷新
         **/
        void onPuBuliuRefersh();

        /**
         * 加载
         **/
        void onPuBuliuaddData();
    }

    /*****
     * 获取用户信息
     ***/

    void getBaijiaUserInfo() {
        httpControl.getBaijiaUserInfo(userID, true, new HttpControl.HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                if (obj != null && obj instanceof RequestUserInfoBean) {
                    RequestUserInfoBean bean = (RequestUserInfoBean) obj;
                    if (bean.getData() != null) {
                        userInfoBean = bean.getData();
                        setHeadValue();
                    } else {
                        http_Fails(500, "信息不存在");
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(getActivity(), msg);
                activity.finish();
            }
        }, getActivity());
    }

    /***
     * 负值
     **/
    void setHeadValue() {
        shop_main_attention_imagebutton.setTag(userInfoBean);
        if (userInfoBean.isIsFollowing()) {
            shop_main_attention_imagebutton.setText("取消");
            shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_unguanzhu), null, null, null);


        } else {
            shop_main_attention_imagebutton.setText("关注");
            shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_guanzhu), null, null, null);

        }
        fragmentBean_list.clear();
        shop_main_head_layout_tab_linearlayout.removeAllViews();
        MyApplication.getInstance().getBitmapUtil().display(shop_main_layout_icon_imageview, ToolsUtil.nullToString(userInfoBean.getLogo()));
        shop_main_layout_name_textview.setText(ToolsUtil.nullToString(userInfoBean.getUserName()));
        shop_main_layout_market_textview.setText(ToolsUtil.nullToString(userInfoBean.getAddress()));

        shap_main_description1_textview.setText(ToolsUtil.nullToString(userInfoBean.getDescription()));

        fragmentBean_list.clear();
        initBuyerPuBu();

        for (int i = 0; i < fragmentBean_list.size(); i++) {
            FragmentBean bean = fragmentBean_list.get(i);
            LinearLayout ll = (LinearLayout) LinearLayout.inflate(getActivity(), R.layout.shop_stay_layout, null);
            LinearLayout shop_stay_layout_parent_linearlayout = (LinearLayout) ll.findViewById(R.id.shop_stay_layout_parent_linearlayout);
            shop_stay_layout_parent_linearlayout.setTag(new Integer(i));
            shop_stay_layout_parent_linearlayout.setOnClickListener(onClickListener);
            TextView tv1 = (TextView) ll.findViewById(R.id.shop_stay_layout_item_textview1);
            tv1.setText(bean.getName());
            TextView tv2 = (TextView) ll.findViewById(R.id.shop_stay_layout_item_textview2);
            if (bean.getIcon() > 0) {
                tv2.setText(bean.getIcon() + "");
            } else {
                tv2.setText(0 + "");
            }
            FontManager.changeFonts(getActivity(), tv1, tv2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            shop_main_head_layout_tab_linearlayout.addView(ll, params);
            view_list.add(ll);
            if (i == fragmentBean_list.size() - 1) {
                View shop_stay_layout_tabline_relativelayout = (View) ll.findViewById(R.id.shop_stay_layout_tabline_relativelayout);
                shop_stay_layout_tabline_relativelayout.setVisibility(View.GONE);
            }

        }

        if (view_list.size() > 0) {
            setItem(true, 0);
        }

        if (view_list.size() == 1) {
            View shop_stay_layout_item_line_view = view_list.get(0).findViewById(R.id.shop_stay_layout_item_line_view);
            if (shop_stay_layout_item_line_view != null) {
                shop_stay_layout_item_line_view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /*****
     * 加载买手瀑布显示信息
     **/
    void initBuyerPuBu() {
        ShopPuBuliuFragment shopPuBuliuFragment1 = new ShopPuBuliuFragment(0, userID);
        fragmentBean_list.add(new FragmentBean("商品", userInfoBean.getProductCount(), shopPuBuliuFragment1));
        fragmentBean_list.add(new FragmentBean("粉丝", userInfoBean.getFollowerCount(),null));
        fragmentBean_list.add(new FragmentBean("成交", 0, null));
    }


    @Override
    public void onDestroy() {
        MyApplication.getInstance().removeActivity(getActivity());//加入回退栈
        super.onDestroy();
        unRegisterProductFavorListener();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    /*****
     * 同步数据  根据商品id 同步瀑布里中  商品信息的状态
     **/
    public void synchronizationData(int _id, int type) {
        for (int i = 0; i < fragmentBean_list.size(); i++) {
            if (i != currId) {
                ShopPuBuliuFragment fragment = (ShopPuBuliuFragment) fragmentBean_list.get(i).getFragment();
                fragment.synchronizationData(_id, type);
            }
        }

    }

    /****
     * 提交收藏与取消收藏商品
     **/
    void submitAttention(final View textview, final int Status, final UserInfoBean bean) {
        httpControl.setFavoite(userID, Status, new HttpControl.HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                switch (Status) {
                    case 0:
                        ((TextView) textview).setText("关注");
                        shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_guanzhu), null, null, null);
                        bean.setIsFollowing(false);
                        Toast.makeText(activity, "取消成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        ((TextView) textview).setText("取消");
                        shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_unguanzhu), null, null, null);
                        bean.setIsFollowing(true);
                        Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(getActivity(), msg);
            }
        }, getActivity());
    }


    /*******
     * 监听 商品 收藏 与 取消收藏的 广播
     ****/
    void registerProductFavorListener() {
        if (isregisterProductFavorListener) {
            return;
        }
        if (productFavorBroadcase == null) {
            productFavorBroadcase = new ProductFavorBroadcase(new ProductFavorBroadcase.ProductFavorBroadcaseListener() {

                @Override
                public void productFavor(boolean isFavor, int product_id) {
                    if (isFavor) {
                        synchronizationData(product_id, 0);
                    } else {
                        synchronizationData(product_id, 1);
                    }

                }
            });
        }
        getActivity().registerReceiver(productFavorBroadcase,new IntentFilter(Constants.PRODUCTFAVOR_INTENT_ACTION));
        isregisterProductFavorListener = true;
    }

    public void unRegisterProductFavorListener() {
        if (isregisterProductFavorListener && productFavorBroadcase != null) {
            getActivity().unregisterReceiver(productFavorBroadcase);
            isregisterProductFavorListener = false;
        }
    }
}
