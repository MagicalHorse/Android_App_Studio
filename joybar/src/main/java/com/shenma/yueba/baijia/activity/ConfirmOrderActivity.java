package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.util.FontManager;

/**
 * 订单确认界面--专柜买手
 * Created by a on 2015/9/30.
 */
public class ConfirmOrderActivity extends BaseActivityWithTopView {

    private TextView tv_ziti_title;
    private EditText et_phone;
    private TextView tv_address_tishi;
    private ImageView iv_product;
    private TextView tv_product_name;
    private TextView tv_color;
    private TextView tv_size;
    private TextView tv_product_money;
    private EditText et_comment;
    private TextView order_money_title;
    private TextView tv_dyg_title;
    private TextView tv_dyg_money;
    private TextView tv_yunfei_title;
    private TextView tv_yunfei;
    private TextView tv_fapiao_title;
    private RadioButton orderBy1,orderBy2,orderBy3;
    private EditText et_name;
    private TextView tv_dingjin_tishi;
    private TextView tv_dingjin_title;
    private TextView tv_dingjin;
    private TextView tv_confirm_pay;
    private int fapiaoNo = 0;// 0表示无发票抬头，1表示个人发票抬头，2表示公司发票抬头,-1没有选择发票抬头
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_order_layout);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("确认订单");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrderActivity.this.finish();
            }
        });

        tv_ziti_title = (TextView) findViewById(R.id.tv_ziti_title);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_address_tishi = getView(R.id.tv_address_tishi);
        iv_product = getView(R.id.iv_product);
        tv_product_name = getView(R.id.tv_product_name);
        tv_color = getView(R.id.tv_color);
        tv_size = getView(R.id.tv_size);
        tv_product_money = getView(R.id.tv_product_money);
        et_comment = getView(R.id.et_comment);
        order_money_title = getView(R.id.order_money_title);
        tv_dyg_title = getView(R.id.tv_dyg_title);
        tv_dyg_money = getView(R.id.tv_dyg_money);
        tv_yunfei = getView(R.id.tv_yunfei);
        orderBy1 = getView(R.id.orderBy1);
        orderBy2 = getView(R.id.orderBy2);
        orderBy3 = getView(R.id.orderBy3);
        et_name = getView(R.id.et_name);
        tv_dingjin_tishi = getView(R.id.tv_dingjin_tishi);
        tv_dingjin_title = getView(R.id.tv_dingjin_title);
        tv_dingjin = getView(R.id.tv_dingjin);
        tv_confirm_pay = getView(R.id.tv_confirm_pay);
        orderBy1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fapiaoNo = 0;
                }else{

                }
            }
        });
        orderBy2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        orderBy3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        FontManager.changeFonts(mContext,tv_ziti_title,et_phone,tv_address_tishi,tv_product_name,tv_color,tv_size,tv_product_money,et_comment,order_money_title,tv_dyg_title,
                tv_dyg_money,tv_yunfei_title,tv_yunfei,tv_fapiao_title,orderBy1, orderBy2,orderBy3,et_name,tv_dingjin_tishi,tv_dingjin_title,tv_dingjin,tv_confirm_pay);
    }
}
