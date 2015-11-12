package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;

public class ChooseCouponActivity extends BaseActivityWithTopView {


    private PullToRefreshListView plv_coupon;
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
        setTitle("—°‘Ò”≈ª›»Ø");
        plv_coupon =  getView(R.id.plv_coupon);
    }

}
