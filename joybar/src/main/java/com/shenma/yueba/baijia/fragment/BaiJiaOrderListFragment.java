package com.shenma.yueba.baijia.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaiJiaOrderDetailsActivity;
import com.shenma.yueba.baijia.adapter.BaiJiaOrderListAdapter;
import com.shenma.yueba.baijia.modle.BaiJiaOrderListInfo;
import com.shenma.yueba.baijia.modle.RequestBaiJiaOrderListInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.InterfaceOrderRefreshListener;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.OrderChangeRefreshObserver;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-6-2 上午11:54:43 程序的简单说明 败家订单列表
 */


public class BaiJiaOrderListFragment extends Fragment implements InterfaceOrderRefreshListener {
    View parentView;
    PullToRefreshListView pull_refresh_list;
    BaiJiaOrderListAdapter baiJiaOrderListAdapter;
    List<BaiJiaOrderListInfo> object_list = new ArrayList<BaiJiaOrderListInfo>();
    int currpage = Constants.CURRPAGE_VALUE;
    int pagersize = Constants.PAGESIZE_VALUE;
    int state = -1;// 订单类型 全部订单 0，待付款 1， 专柜自提 2， 售后 3

    LinearLayout showloading_layout_view;//加载进度条
    HttpControl httpControl = new HttpControl();
    RequestBaiJiaOrderListInfoBean requestBaiJiaOrderListInfoBean;
    boolean ishow = true;
    boolean isBroadcase = false;
    boolean isruning = false;//是否执行网络访问  true是  false 否

    public BaiJiaOrderListFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public BaiJiaOrderListFragment(int state) {
        super();
        this.state = state;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i("TAG", "--->> onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("TAG", "--->> onCreate");
        super.onCreate(savedInstanceState);
        OrderChangeRefreshObserver.getInstance().addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("TAG", "--->> onCreateView");
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.baijiaorderlistfragment_layout, null);
            initView();
            requestFalshData();
        }
        ViewGroup vp = (ViewGroup) parentView.getParent();
        if (vp != null) {
            vp.removeView(parentView);
        }
        return parentView;
    }


    @Override
    public void onResume() {
        Log.i("TAG", "--->> onResume");
        super.onResume();
    }

    void initView() {
        showloading_layout_view = (LinearLayout) parentView.findViewById(R.id.showloading_layout_view);
        pull_refresh_list = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);

        // 设置标签显示的内容
        baiJiaOrderListAdapter = new BaiJiaOrderListAdapter(object_list, getActivity());
        pull_refresh_list.setAdapter(baiJiaOrderListAdapter);
        pull_refresh_list.setMode(Mode.PULL_FROM_START);
        ToolsUtil.initPullResfresh(pull_refresh_list, getActivity());

        pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                requestData();
            }
        });

        pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (object_list != null) {
                    BaiJiaOrderListInfo info = object_list.get(arg2 - 1);
                    Intent intent = new Intent(getActivity(),
                            BaiJiaOrderDetailsActivity.class);
                    intent.putExtra("ORDER_ID", info.getOrderNo());
                    startActivity(intent);
                }

            }
        });
    }

    /*****
     * 请求加载数据
     ***/
    public void requestData() {
        if (isruning) {
            return;
        }
        sendRequestData(currpage, pagersize, 1);
    }

    /*****
     * 请求刷新数据
     ***/
    public void requestFalshData() {
        if (isruning) {
            return;
        }
        sendRequestData(1, pagersize, 0);
    }


    /*****
     * 请求刷新数据(根据 当前页 加载 当前 指定页数的 数据  即   当前页  为1    加载的数据为 1*page)
     ***/
    public void requestFalshData(int currpage) {
        if (isruning) {
            return;
        }
        if ((currpage - 1) > 0) {
            sendRequestData(1, (currpage - 1) * pagersize, 0);
        } else {
            sendRequestData(1, currpage * pagersize, 0);
        }

    }

    /******
     * 与网络通信请求数据
     *
     * @param page int 访问页
     * @param type int 0 刷新 1 加载
     ***/
    void sendRequestData(final int page, final int _pagersize, final int type) {
        isruning = true;
        ToolsUtil.showNoDataView(getActivity(), parentView, false);
        Log.i("TAG", "currpage=" + page + "   pagesize=" + pagersize);
        httpControl.getBaijiaOrderList(page, _pagersize, state, ishow,
                new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        isruning = false;
                        if (showloading_layout_view != null) {
                            showloading_layout_view.setVisibility(View.GONE);
                        }
                        ToolsUtil.pullResfresh(pull_refresh_list);
                        if (_pagersize == pagersize) {
                            currpage = page;
                        } else {
                            currpage = _pagersize / 10;
                        }

                        ishow = false;
                        if (obj != null && obj instanceof RequestBaiJiaOrderListInfoBean) {
                            requestBaiJiaOrderListInfoBean = (RequestBaiJiaOrderListInfoBean) obj;
                            switch (type) {
                                case 0:
                                    falshData(requestBaiJiaOrderListInfoBean);
                                    break;
                                case 1:
                                    addData(requestBaiJiaOrderListInfoBean);
                                    break;
                            }
                            setPageStatus(requestBaiJiaOrderListInfoBean, page);

                        } else {
                            http_Fails(500, getActivity().getResources().getString(R.string.errorpagedata_str));
                        }

                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        isruning = false;
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                        ToolsUtil.pullResfresh(pull_refresh_list);
                        if (showloading_layout_view != null) {
                            showloading_layout_view.setVisibility(View.GONE);
                        }
                    }
                }, getActivity());

    }


    void setPageStatus(RequestBaiJiaOrderListInfoBean data, int page) {
        if (page == 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.PULL_FROM_START);
            }

            ToolsUtil.showNoDataView(getActivity(), parentView, true);
        } else if (page != 1 && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.BOTH);
            }

            MyApplication.getInstance().showMessage(getActivity(), getActivity().getResources().getString(R.string.lastpagedata_str));
        } else {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.BOTH);
            }
        }
    }


    /***
     * 刷新viewpager数据
     ***/
    void falshData(RequestBaiJiaOrderListInfoBean bean) {
        ishow = false;
        currpage++;
        object_list.clear();
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                object_list.addAll(bean.getData().getItems());
            }
        }

        if (baiJiaOrderListAdapter != null) {
            baiJiaOrderListAdapter.notifyDataSetChanged();
        }

    }

    /***
     * 加载数据
     **/
    void addData(RequestBaiJiaOrderListInfoBean bean) {
        ishow = false;
        currpage++;
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                object_list.addAll(bean.getData().getItems());
            }
        }

        if (baiJiaOrderListAdapter != null) {
            baiJiaOrderListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDetach() {
        Log.i("TAG", "----->>onDetach");
        super.onDetach();

    }

    @Override
    public void onPause() {
        Log.i("TAG", "--->> onPause");
        super.onPause();
    }


    @Override
    public void refreshOrderList(int index, Object obj) {
        ishow = false;
        showloading_layout_view.setVisibility(View.VISIBLE);
        requestFalshData(currpage);
    }
}
