package com.shenma.yueba.baijia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.BrandAdapter;
import com.shenma.yueba.baijia.modle.BrandInfo;
import com.shenma.yueba.baijia.modle.RequestBrandInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 查询更多品牌列表页
 */


public class SearchBrandListActivity extends BaseActivityWithTopView {
    Activity activity;
    LayoutInflater layoutInflater;
    private PullToRefreshGridView pull_refresh_list;
    private BrandAdapter brandAdapter;
    int currPage = Constants.CURRPAGE_VALUE;
    int pageSize = Constants.PAGESIZE_VALUE;
    boolean showDialog = true;
    HttpControl httpCntrol = new HttpControl();
    List<BrandInfo> items = new ArrayList<BrandInfo>();
    //搜索文本框
    EditText srearchbrandlist_edittext;
    //顶部 搜索框的父类
    RelativeLayout searchbrandlist_head_relativelayout;
    String StoreId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);//加入回退栈
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.searchbrandlist_layout);
        super.onCreate(savedInstanceState);
        activity = this;
        StoreId=this.getIntent().getStringExtra("StoreId");
        if(StoreId==null || StoreId.equals(""))
        {
            MyApplication.getInstance().showMessage(this,"无效的商场ID");
            return;
        }
        initView();
        initPullView();
        requestFalshData();
    }


    void initView() {
        searchbrandlist_head_relativelayout = (RelativeLayout) findViewById(R.id.searchbrandlist_head_relativelayout);
        //返回
        TextView tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //搜索文本框
        srearchbrandlist_edittext = (EditText) findViewById(R.id.srearchbrandlist_edittext);
        //搜索按钮
        Button bt_top_right = (Button) findViewById(R.id.bt_top_right);
        bt_top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = srearchbrandlist_edittext.getText().toString().trim();
                if (str.equals("")) {
                    srearchbrandlist_edittext.requestFocus();
                    MyApplication.getInstance().showMessage(SearchBrandListActivity.this, "请输入品牌关键字");
                    return;
                } else {
                    closeInputMethod();
                    searchbrandlist_head_relativelayout.requestFocus();
                    requestFalshData();
                }
            }
        });
    }

    void initPullView() {
        pull_refresh_list = (PullToRefreshGridView) findViewById(R.id.pull_refresh_gridview);
        pull_refresh_list.getRefreshableView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeInputMethod();
                searchbrandlist_head_relativelayout.requestFocus();
                return false;
            }
        });

        pull_refresh_list.setMode(Mode.PULL_FROM_START);
        pull_refresh_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BrandInfo brandInfo = items.get(arg2);
                Intent intent = new Intent(activity, BrandListActivity.class);
                intent.putExtra("BrandId", brandInfo.getBrandId());
                intent.putExtra("BrandName", brandInfo.getBrandName());
                activity.startActivity(intent);
            }
        });

        ToolsUtil.initPullResfresh(pull_refresh_list, activity);

        pull_refresh_list.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                requestFalshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                Log.i("TAG", "onPullUpToRefresh");
                requestData();
            }
        });
        brandAdapter = new BrandAdapter(activity, items);
        pull_refresh_list.setAdapter(brandAdapter);
    }

    void requestData() {
        sendHttp(currPage, 1);

    }

    void requestFalshData() {
        showDialog = true;
        sendHttp(1, 0);
    }


    void addData(RequestBrandInfoBean bean) {
        currPage++;
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                items.addAll(bean.getData().getItems());
            }
        }
        if (brandAdapter != null) {
            brandAdapter.notifyDataSetChanged();
        }

    }

    void falshData(RequestBrandInfoBean bean) {
        currPage++;
        items.clear();
        if (bean.getData() != null) {
            if (bean.getData().getItems() != null) {
                items.addAll(bean.getData().getItems());
            }
        }

        if (brandAdapter != null) {
            brandAdapter.notifyDataSetChanged();
        }


    }


    /******
     * @param page int 访问页面
     * @param type int 0：刷新 1 加载
     ***/
    void sendHttp(final int page, final int type) {
        ToolsUtil.showNoDataView(activity, false);
        httpCntrol.getBrandProductList(page, pageSize, showDialog, new HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                ToolsUtil.pullResfresh(pull_refresh_list);
                currPage = page;
                showDialog = false;
                if (obj != null && obj instanceof RequestBrandInfoBean) {
                    RequestBrandInfoBean bean = (RequestBrandInfoBean) obj;
                    switch (type) {
                        case 0:
                            falshData(bean);
                            break;
                        case 1:
                            addData(bean);
                            break;
                    }

                    setPageStatus(bean, page);
                } else {
                    http_Fails(500, activity.getResources()
                            .getString(R.string.errorpagedata_str));
                }

            }

            @Override
            public void http_Fails(int error, String msg) {
                ToolsUtil.pullResfresh(pull_refresh_list);
                MyApplication.getInstance().showMessage(activity, msg);
            }
        }, activity);
    }


    void setPageStatus(RequestBrandInfoBean data, int page) {
        if (page == 1 && (data.getData() == null
                || data.getData().getItems() == null || data
                .getData().getItems().size() == 0)) {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.PULL_FROM_START);
            }

            ToolsUtil.showNoDataView(activity, true);
        } else if (page != 1
                && (data.getData() == null || data.getData().getItems() == null || data.getData().getItems().size() == 0)) {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.BOTH);
            }
            MyApplication.getInstance().showMessage(
                    activity,
                    activity.getResources().getString(
                            R.string.lastpagedata_str));
        } else {
            if (pull_refresh_list != null) {
                pull_refresh_list.setMode(Mode.BOTH);
            }
        }
    }


    private void closeInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        boolean isOpen = imm.isActive();

        if (isOpen) {

            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(srearchbrandlist_edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

}