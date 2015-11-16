package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.baijia.adapter.ChooseCouponAdapter;
import com.shenma.yueba.baijia.modle.ChooseCouponListBean;
import com.shenma.yueba.util.HttpControl;

import java.util.ArrayList;

public class ChooseCouponActivity extends BaseActivityWithTopView {


    private PullToRefreshListView plv_coupon;
    private ChooseCouponAdapter adapter;
    private ArrayList<ChooseCouponListBean> mList = new ArrayList<ChooseCouponListBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_coupon_layout);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
      setLeftTextView(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
        setTitle("选择优惠券");
        plv_coupon =  getView(R.id.plv_coupon);
        adapter = new ChooseCouponAdapter(mContext,mList);
        plv_coupon.setAdapter(adapter);
    }




    private void getCouponList(){
        HttpControl httpControl = new HttpControl();
//        httpControl.getShoppingCityList(new HttpControl.HttpCallBackInterface() {
//            @Override
//            public void http_Success(Object obj) {
//                CityListBackBean bean = (CityListBackBean) obj;
//                mList.addAll(bean.getData());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void http_Fails(int error, String msg) {
//                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//            }
//        }, mContext);
    }
}



