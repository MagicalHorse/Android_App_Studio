package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.baijia.modle.RequestBaiJiaOrdeDetailsInfoBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author gyj
 * @version 创建时间：2015-6-12 上午9:59:08
 *          程序的简单说明  订单申诉页面
 */

public class OrderAppealActivity extends BaseActivityWithTopView implements OnClickListener {
    View parementView;
    //买手头像
    RoundImageView orderappeal_layout_buyericon_textview;
    //电话图标
    ImageView orderappeal_layout_phoneicon_textview;
    //买手账号
    TextView orderappeal_layout_buyernovalue_textview;
    //买手电话
    TextView orderappeal_layout_buyermobilevalue_textview;
    //提货地址
    TextView orderappeal_layout_goodsaddressvalue_textview;
    //申诉理由
    EditText orderappeal_layout_appealreason_textview;
    //提货手机号
    EditText affirmorder_item_tihuophonevalue_textview;
    String orderNo = null;
    BaiJiaOrdeDetailsInfoBean bean;//订单详情对象
    HttpControl httpControl = new HttpControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);//加入回退栈
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        parementView = this.getLayoutInflater().inflate(R.layout.orderappeal_layout, null);
        setContentView(parementView);
        super.onCreate(savedInstanceState);
        if (this.getIntent().getSerializableExtra("DATA") == null) {
            MyApplication.getInstance().showMessage(this, "数据错误，请重试");
            finish();
            return;
        } else {
            orderNo = (String) this.getIntent().getSerializableExtra("DATA");
        }
        MyApplication.getInstance().addActivity(this);
        initView();
        //请求订单信息
        requestOrderInfo();
    }


    void initView() {
        ToolsUtil.setFontStyle(OrderAppealActivity.this, parementView, R.id.tv_top_title, R.id.orderappeal_layout_buyerno_textview, R.id.orderappeal_layout_buyernovalue_textview, R.id.orderappeal_layout_buyermobile_textview, R.id.orderappeal_layout_buyermobilevalue_textview, R.id.orderappeal_layout_goodsaddress_textview, R.id.orderappeal_layout_goodsaddressvalue_textview, R.id.orderappeal_layout_appealreason_textview, R.id.affirmorder_item_tihuophone_textview, R.id.affirmorder_item_tihuophonevalue_textview, R.id.affirmorder_item_tihuophonetitle_textview, R.id.orderappeal_layout_footersubmit_button);
        setTitle("申请申诉");
        setLeftTextView(new OnClickListener() {

            @Override
            public void onClick(View v) {
                OrderAppealActivity.this.finish();
            }
        });

        //买手头像
        orderappeal_layout_buyericon_textview = (RoundImageView) parementView.findViewById(R.id.orderappeal_layout_buyericon_textview);
        //电话图标
        orderappeal_layout_phoneicon_textview = (ImageView) parementView.findViewById(R.id.orderappeal_layout_phoneicon_textview);
        orderappeal_layout_phoneicon_textview.setOnClickListener(this);
        //买手账号
        orderappeal_layout_buyernovalue_textview = (TextView) parementView.findViewById(R.id.orderappeal_layout_buyernovalue_textview);
        //买手电话
        orderappeal_layout_buyermobilevalue_textview = (TextView) parementView.findViewById(R.id.orderappeal_layout_buyermobilevalue_textview);
        //提货地址
        orderappeal_layout_goodsaddressvalue_textview = (TextView) parementView.findViewById(R.id.orderappeal_layout_goodsaddressvalue_textview);
        //申诉理由
        orderappeal_layout_appealreason_textview = (EditText) parementView.findViewById(R.id.orderappeal_layout_appealreason_textview);
        //提货手机号
        affirmorder_item_tihuophonevalue_textview = (EditText) parementView.findViewById(R.id.affirmorder_item_tihuophonevalue_textview);
        //申诉按钮
        Button orderappeal_layout_footersubmit_button = (Button) parementView.findViewById(R.id.orderappeal_layout_footersubmit_button);
        orderappeal_layout_footersubmit_button.setOnClickListener(this);

    }

    /*****
     * 请求订单详情
     ***/
    void requestOrderInfo() {
        httpControl.getBaijiaOrderDetails(orderNo, true, new HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
                if (obj == null) {
                    http_Fails(500, "加载数据失败");
                } else {
                    RequestBaiJiaOrdeDetailsInfoBean requestBaiJiaOrdeDetailsInfoBean = (RequestBaiJiaOrdeDetailsInfoBean) obj;
                    if (requestBaiJiaOrdeDetailsInfoBean.getData() == null) {
                        http_Fails(500, "加载数据失败");
                    } else {
                        bean = requestBaiJiaOrdeDetailsInfoBean.getData();
                        setValue();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(OrderAppealActivity.this, msg);
                OrderAppealActivity.this.finish();
            }
        }, this);
    }

    /*****
     * 负值
     ***/
    void setValue() {
        MyApplication.getInstance().getBitmapUtil().display(orderappeal_layout_buyericon_textview, ToolsUtil.nullToString(bean.getBuyerLogo()));
        orderappeal_layout_buyernovalue_textview.setText(bean.getBuyerName());
        orderappeal_layout_buyermobilevalue_textview.setText(bean.getBuyerMobile());
        orderappeal_layout_goodsaddressvalue_textview.setText(bean.getPickAddress());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderappeal_layout_footersubmit_button://申诉按钮
                submit();
                break;
            case R.id.orderappeal_layout_phoneicon_textview://电话图标
                ToolsUtil.callActivity(OrderAppealActivity.this, bean.getBuyerMobile());
                break;
        }
    }

    /***
     * 提交申诉
     **/
    void submit() {
        String msg = orderappeal_layout_appealreason_textview.getText().toString().trim();
        String phome = affirmorder_item_tihuophonevalue_textview.getText().toString().trim();
        if (msg.length() < 3) {
            MyApplication.getInstance().showMessage(this, "申诉理由必须大于三个字母");
            return;
        }
        if (phome.equals("")) {
            MyApplication.getInstance().showMessage(this, "联系电话必须填写");
            return;
        }
        //申诉
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);//加入回退栈
        super.onDestroy();
    }
}
