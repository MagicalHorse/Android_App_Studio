package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shenma.yueba.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/12/29.
 */
public class BaiJiaOrderDetail_ck_Activity extends  BaseActivityWithTopView{
    TextView orderdetails_ck_layout_storename_textview;//店名
    TextView orderdetails_ck_layout_storephone_textview;//退货电话
    TextView orderdetails_ck_layout_storeaddress_textview;//退货地址
    TextView orderdetails_ck_layout_storecontanct_textview;//退货联系人
    TextView orderdetails_ck_layout_storeppost_textview;//邮编
    TextView orderdetails_ck_layout_storebark_textview;//提示信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails_ck_layout);
        initView();
    }

    void initView()
    {
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiJiaOrderDetail_ck_Activity.this.finish();
            }
        });
        setTitle("订单详情");
        orderdetails_ck_layout_storename_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storename_textview);
        orderdetails_ck_layout_storephone_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storephone_textview);
        orderdetails_ck_layout_storeaddress_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storeaddress_textview);
        orderdetails_ck_layout_storecontanct_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storecontanct_textview);
        orderdetails_ck_layout_storeppost_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storeppost_textview);
        orderdetails_ck_layout_storebark_textview=(TextView)findViewById(R.id.orderdetails_ck_layout_storebark_textview);
    }
}
