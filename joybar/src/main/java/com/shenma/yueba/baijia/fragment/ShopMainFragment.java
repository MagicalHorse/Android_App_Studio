package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfoBean;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListPic;
import com.shenma.yueba.baijia.modle.RequestMyFavoriteProductListInfoBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.baijia.modle.UserInfoBean;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.CollectobserverManage;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PubuliuManager;
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
    LinearLayout shop_main_layout_tabcontent_framelayout;
    //地址
    TextView shop_main_head_layout_address_textview;
    //商品描述
    TextView shap_main_description1_textview, shap_main_description2_textview, shap_main_description3_textview;
    List<View> view_list = new ArrayList<View>();
    PullToRefreshScrollView shop_main_layout_title_pulltorefreshscrollview;

    boolean isrunning=false;
    HttpControl httpControl = new HttpControl();
    UserInfoBean userInfoBean;
    int userID = -1;
    Activity activity;
    PubuliuManager pubuliuManager;
    boolean ishow=true;
    int currPage = Constants.CURRPAGE_VALUE;
    int pageSize = Constants.PAGESIZE_VALUE;
    List<PubuliuBeanInfo> item=new ArrayList<PubuliuBeanInfo>();

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
            requestRefreshData();
        }

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
                requestRefreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                requestaddData();
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
        //瀑布流的 内容 即 对应的fragment

        shop_main_layout_tabcontent_framelayout = (LinearLayout) contantView.findViewById(R.id.shop_main_layout_tabcontent_framelayout);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.shop_main_siliao_imagebutton://私聊
                    if (!MyApplication.getInstance().isUserLogin(getActivity())) {
                        return;
                    }
                    ToolsUtil.forwardChatActivity(getActivity(), userInfoBean.getUserName(), userID, 0, null, null,null);
                    break;
                case R.id.shop_main_attention_imagebutton://关注
                    if(!MyApplication.getInstance().isUserLogin(getActivity()))
                    {
                        return;
                    }
                    if(v.getTag()!=null)
                    {
                        if(userInfoBean.isIsFollowing())//如果是已关注
                        {
                            //取消关注
                            submitAttention(v,0,userInfoBean);
                        }else
                        {
                            //添加关注
                            submitAttention(v,1,userInfoBean);
                        }
                    }
                    break;
            }
        }
    };




    /****
     * 提交收藏与取消收藏商品
     * @param Status int   0表示取消收藏   1表示收藏
     * @param bean UserInfoBean  商品对象
     * **/
    void submitAttention(final View textview,final int Status,final UserInfoBean bean)
    {
        httpControl.setFavoite(Integer.toString(userID), Status, new HttpControl.HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                switch(Status)
                {
                    case 0:
                        ((TextView)textview).setText("关注");
                        shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_guanzhu), null, null, null);
                        bean.setIsFollowing(false);
                        MyApplication.getInstance().showMessage(getActivity(), "取消成功");
                        break;
                    case 1:
                        ((TextView)textview).setText("已关注");
                        shop_main_attention_imagebutton.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(R.drawable.shop_unguanzhu), null, null, null);
                        bean.setIsFollowing(true);
                        MyApplication.getInstance().showMessage(getActivity(), "关注成功");
                        break;
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(getActivity(), msg);
            }
        }, getActivity());
    }




    /******
     * 下拉刷新
     ***/
    void requestRefreshData() {
        if(isrunning)
        {
            return;
        }
        ishow =true;
        getBaijiaUserInfo();
        sendReuqestAllProductHttp(0, 0);
    }

    /******
     * 上啦加载刷新
     ***/
    void requestaddData() {
        if(isrunning)
        {
            return;
        }
        sendReuqestAllProductHttp(currPage, 1);
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

            shop_main_attention_imagebutton.setText("已关注");
        } else {

            shop_main_attention_imagebutton.setText("关注");
        }

        MyApplication.getInstance().getBitmapUtil().display(shop_main_layout_icon_imageview, ToolsUtil.nullToString(userInfoBean.getLogo()));
        shop_main_layout_name_textview.setText(ToolsUtil.nullToString(userInfoBean.getUserName()));
        shop_main_layout_market_textview.setText(ToolsUtil.nullToString(userInfoBean.getAddress()));

        shap_main_description1_textview.setText(ToolsUtil.nullToString(userInfoBean.getDescription()));
        TextView tv_product_count = (TextView) contantView.findViewById(R.id.tv_product_count);
        tv_product_count.setText("商品：" + userInfoBean.getProductCount());

        shop_main_head_layout_address_textview.setText(ToolsUtil.nullToString(userInfoBean.getAddress()));
    }


    @Override
    public void onDestroy() {
        MyApplication.getInstance().removeActivity(getActivity());//加入回退栈
        CollectobserverManage.getInstance().removeObserver(pubuliuManager);
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }


    void onRefresh(MyFavoriteProductListInfoBean bean)
    {
        if(bean!=null && bean.getItems()!=null && bean.getItems().size()>0)
        {
            item.clear();
            item.addAll(getTransformData(bean.getItems()));
            if(pubuliuManager==null)
            {
                pubuliuManager=new PubuliuManager(activity,shop_main_layout_tabcontent_framelayout);
                CollectobserverManage.getInstance().addObserver(pubuliuManager);
            }
            pubuliuManager.onResher(item);
        }
    }


    void onAddData(MyFavoriteProductListInfoBean bean)
    {
        if(bean!=null && bean.getItems()!=null && bean.getItems().size()>0)
        {
            List<PubuliuBeanInfo> childitem=getTransformData(bean.getItems());
            item.addAll(childitem);
            pubuliuManager.onaddData(childitem);
        }
    }



    /******
     * 数据进行转换
     * *****/
    List<PubuliuBeanInfo> getTransformData(List<MyFavoriteProductListInfo> brandInfoInfos)
    {
        List<PubuliuBeanInfo> pubuliuBeanInfos=new ArrayList<PubuliuBeanInfo>();
        if(brandInfoInfos!=null)
        {
            for(int i=0;i<brandInfoInfos.size();i++)
            {
                PubuliuBeanInfo pubuliuBeanInfo=new PubuliuBeanInfo();
                MyFavoriteProductListInfo myFavoriteProductListInfo= brandInfoInfos.get(i);
                pubuliuBeanInfo.setFavoriteCount(myFavoriteProductListInfo.getFavoriteCount());
                pubuliuBeanInfo.setId(Integer.toString(myFavoriteProductListInfo.getId()));
                pubuliuBeanInfo.setIscollection(false);
                pubuliuBeanInfo.setName(myFavoriteProductListInfo.getName());
                MyFavoriteProductListPic myFavoriteProductListPic=myFavoriteProductListInfo.getPic();
                if(myFavoriteProductListPic!=null)
                {
                    pubuliuBeanInfo.setPicurl(myFavoriteProductListPic.getPic());
                    pubuliuBeanInfo.setRation(myFavoriteProductListPic.getRatio());
                }else
                pubuliuBeanInfo.setPrice(myFavoriteProductListInfo.getPrice());
                pubuliuBeanInfos.add(pubuliuBeanInfo);
            }
        }
        return pubuliuBeanInfos;
    }

    /******
     * 访问网络获取 全部商品或上新商品
     *
     * @param page
     *            访问的页数
     * @param type
     *            int 0:刷新 1：加载
     * ****/
    void sendReuqestAllProductHttp(final int page,final int type)
    {
        isrunning=true;
        ToolsUtil.showNoDataView(getActivity(), false);
        httpControl.GetBaijiaGetUserProductList(userID,page, pageSize, 0, ishow, new HttpControl.HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
                RequestMyFavoriteProductListInfoBean bean=(RequestMyFavoriteProductListInfoBean)obj;
                    switch(type)
                    {
                        case 0:
                            onRefresh(bean.getData());
                            break;
                        case 1:
                            onAddData(bean.getData());
                            break;
                    }
                ishow=false;
                setPageStatus(bean, page);
                isrunning=false;
            }

            @Override
            public void http_Fails(int error, String msg) {
                isrunning=false;
                ToolsUtil.pullResfresh(shop_main_layout_title_pulltorefreshscrollview);
                if(getActivity()!=null)
                {
                    MyApplication.getInstance().showMessage(getActivity(), msg);
                }

            }
        }, getActivity());
    }


    void setPageStatus(RequestMyFavoriteProductListInfoBean data, int page) {
        if (page == 1 && (data.getData() == null
                || data.getData().getItems() == null || data
                .getData().getItems().size() == 0)) {
            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }

            ToolsUtil.showNoDataView(activity, true);
        } else if (page != 1
                && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
            MyApplication.getInstance().showMessage(
                    activity,
                    activity.getResources().getString(
                            R.string.lastpagedata_str));
        } else {
            if (shop_main_layout_title_pulltorefreshscrollview != null) {
                shop_main_layout_title_pulltorefreshscrollview.setMode(PullToRefreshBase.Mode.BOTH);
            }
        }
    }
}
