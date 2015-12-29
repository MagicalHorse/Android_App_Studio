package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.RequestStoreSalesReturnBean;
import com.shenma.yueba.baijia.modle.StoreSalesReturnInfo;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;

/**
 * Created by Administrator on 2015/12/29.
 * 专柜 申请退款 详情页面
 */
public class BaiJiaOrderDetail_ck_Activity extends BaseActivityWithTopView {
    TextView orderdetails_ck_layout_storename_textview;//店名
    TextView orderdetails_ck_layout_storephone_textview;//退货电话
    TextView orderdetails_ck_layout_storeaddress_textview;//退货地址
    TextView orderdetails_ck_layout_storecontanct_textview;//退货联系人
    TextView orderdetails_ck_layout_storeppost_textview;//邮编
    TextView orderdetails_ck_layout_storebark_textview;//提示信息
    HttpControl httpControl = null;
    boolean ishowDialog = true;
    String orderNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.orderdetails_ck_layout);
        super.onCreate(savedInstanceState);
        orderNo = this.getIntent().getStringExtra("OrderNo");
        if (orderNo == null || orderNo.equals("")) {
            MyApplication.getInstance().showMessage(BaiJiaOrderDetail_ck_Activity.this, "订单号错误");
            finish();
            return;
        }
        initView();
        requestOrderDetailsInfo();
    }

    void initView() {
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiJiaOrderDetail_ck_Activity.this.finish();
            }
        });
        setTitle("订单详情");
        orderdetails_ck_layout_storename_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storename_textview);
        orderdetails_ck_layout_storephone_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storephone_textview);
        orderdetails_ck_layout_storeaddress_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storeaddress_textview);
        orderdetails_ck_layout_storecontanct_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storecontanct_textview);
        orderdetails_ck_layout_storeppost_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storeppost_textview);
        orderdetails_ck_layout_storebark_textview = (TextView) findViewById(R.id.orderdetails_ck_layout_storebark_textview);
    }

    void requestOrderDetailsInfo() {
        if (httpControl == null) {
            httpControl = new HttpControl();
        }
        httpControl.getSalesProductOrderInfo(BaiJiaOrderDetail_ck_Activity.this, orderNo, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestStoreSalesReturnBean bean = (RequestStoreSalesReturnBean) obj;
                if (bean.getData() == null) {
                    http_Fails(500, "未获取到信息,请重试");
                } else {
                    setValue(bean.getData());
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(BaiJiaOrderDetail_ck_Activity.this, msg);
                finish();
            }
        }, ishowDialog);
    }

    void setValue(StoreSalesReturnInfo beanInfo) {
        if (beanInfo != null) {
            orderdetails_ck_layout_storename_textview.setText(ToolsUtil.nullToString(beanInfo.getStoreName()));
            orderdetails_ck_layout_storephone_textview.setText(ToolsUtil.nullToString(beanInfo.getStoreMobile()));
            orderdetails_ck_layout_storeaddress_textview.setText(ToolsUtil.nullToString(beanInfo.getRmaAddress()));
            orderdetails_ck_layout_storecontanct_textview.setText(ToolsUtil.nullToString(beanInfo.getRmaPerson()));
            orderdetails_ck_layout_storeppost_textview.setText(ToolsUtil.nullToString(beanInfo.getStoreZipCode()));
            if (beanInfo.getRmaTips() != null && beanInfo.getRmaTips().length > 0) {
                String str = "";
                for (int i = 0; i < beanInfo.getRmaTips().length; i++) {
                    str += beanInfo.getRmaTips()[i] + "<br>";
                }
                orderdetails_ck_layout_storebark_textview.setText(Html.fromHtml(str));
            }
        }
    }
}
