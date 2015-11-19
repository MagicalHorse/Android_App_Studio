package com.shenma.yueba.baijia.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;

import roboguice.util.RoboAsyncTask;

/**
 * 确认订单--专柜
 * Created by a on 2015/11/12.
 */
public class ConfirmOrderForZhuanGui extends BaseActivityWithTopView {


    private TranslateAnimation animDown;
    private TranslateAnimation animUp;
    private RotateAnimation rotateAnimationUp;
    private RotateAnimation rotateAnimationDown;
    private LinearLayout ll_content;
    private LinearLayout ll_move, ll_coupon;
    private ImageView iv_card;
    private boolean isHide = true;
    private EditText et_phone, et_comment;
    private ImageView iv_product;
    private TextView tv_product_name, tv_color, tv_size, tv_product_money, order_money_title, tv_dyg_money, tv_yunfei, tv_dingjin, tv_confirm_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_order_for_zhuangui);
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        et_phone = getView(R.id.et_phone);
        iv_product = getView(R.id.iv_product);
        tv_product_name = getView(R.id.tv_product_name);
        tv_size = getView(R.id.tv_size);
        tv_color = getView(R.id.tv_color);
        tv_product_money = getView(R.id.tv_product_money);
        et_comment = getView(R.id.et_comment);
        order_money_title = getView(R.id.order_money_title);
        ll_coupon = getView(R.id.ll_coupon);
        tv_dyg_money = getView(R.id.tv_dyg_money);
        tv_yunfei = getView(R.id.tv_yunfei);
        tv_dingjin = getView(R.id.tv_dingjin);
        tv_confirm_pay = getView(R.id.tv_confirm_pay);
        setTitle("确认订单");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout rl_card = getView(R.id.rl_card);
        iv_card = getView(R.id.iv_card);
        ll_content = getView(R.id.ll_content);
        initAnimation();
        ll_move = getView(R.id.ll_move);
        rl_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide) {
                    ll_move.startAnimation(animDown);
                    iv_card.startAnimation(rotateAnimationUp);
                    isHide = false;
                } else {
                    ll_move.startAnimation(animUp);
                    iv_card.startAnimation(rotateAnimationDown);
                    isHide = true;
                }

            }
        });
    }


    private void initAnimation() {

        animDown = new TranslateAnimation(0, 0, 0, ll_content.getHeight());
        animDown.setDuration(500);
        animDown.setFillAfter(true);
        animUp = new TranslateAnimation(0, 0, 0, ll_content.getHeight());
        animUp.setDuration(500);
        animUp.setFillAfter(true);
        rotateAnimationUp = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationUp.setDuration(500);
        rotateAnimationUp.setFillAfter(true);
        rotateAnimationDown = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimationDown.setDuration(500);
        rotateAnimationDown.setFillAfter(true);
    }
}