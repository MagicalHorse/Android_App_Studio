package com.shenma.yueba.baijia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.AttationListAdapter;
import com.shenma.yueba.baijia.adapter.BuyerSearchAdapter;
import com.shenma.yueba.baijia.modle.newmodel.BuyerInfo;
import com.shenma.yueba.baijia.modle.newmodel.SearchBuyerBackBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

import config.PerferneceConfig;

/**
 * 搜索买手的结果界面
 * Created by a on 2015/12/7.
 */
public class BuyerSearchActivity extends BaseActivityWithTopView{

    private int page = 1;
    private String key;
    private boolean isRefresh = true;
    private PullToRefreshListView pull_refresh_list;
    private BuyerSearchAdapter adapter;
    private List<BuyerInfo> mList = new ArrayList<BuyerInfo>();
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.refresh_listview_with_title_layout);
        super.onCreate(savedInstanceState);
        key = getIntent().getStringExtra("key");
        initView();
        getSearchBuyerList(mContext,true,true);
    }


    private void initView(){
        {
            setTitle("搜索买手");
            setLeftTextView(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            tv_nodata = (TextView) findViewById(R.id.nodata_layout_textview);
            pull_refresh_list = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
            pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            adapter = new BuyerSearchAdapter(mContext,mList);
            pull_refresh_list.setAdapter(adapter);
            pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
            pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

                @Override
                public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                    page = 1;
                    isRefresh = true;
                    getSearchBuyerList(mContext, false, false);

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                    page++;
                    isRefresh = false;
                    getSearchBuyerList(mContext, false, false);
                }
            });

        }
    }

    /**
     * 获取搜到的买手列表
     */
    public void getSearchBuyerList(Context ctx,boolean showDialog,boolean focusRefresh){
        this.key = key;
        if(showDialog && mList!=null && mList.size()>0 && !focusRefresh){
            return;
        }
        HttpControl httpControl = new HttpControl();
        String cityId = SharedUtil.getStringPerfernece(mContext, SharedUtil.getStringPerfernece(mContext, PerferneceConfig.SELECTED_CITY_ID));
        String latitude = PerferneceUtil.getString(PerferneceConfig.LATITUDE);
        String longitude = PerferneceUtil.getString(PerferneceConfig.LONGITUDE);
        httpControl.searchBuyer(key, SharedUtil.getUserId(ctx), cityId, "", longitude, latitude,showDialog ,page, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                pull_refresh_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_refresh_list.onRefreshComplete();
                    }
                }, 100);
                SearchBuyerBackBean bean = (SearchBuyerBackBean) obj;
                if (isRefresh) {
                    if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
                        mList.clear();
                        mList.addAll(bean.getData().getItems());
                        tv_nodata.setVisibility(View.GONE);
                    } else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (bean != null && bean.getData() != null && bean.getData().getItems() != null && bean.getData().getItems().size() > 0) {
                        mList.addAll(bean.getData().getItems());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "没有更多数据了...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }, ctx);

    }

}
