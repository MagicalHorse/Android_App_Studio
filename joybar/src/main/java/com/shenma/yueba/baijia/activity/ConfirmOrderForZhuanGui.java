package com.shenma.yueba.baijia.activity;

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
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.AffirmProductInfo;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestMemberCardBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import org.w3c.dom.Text;

import java.util.List;

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
    private LinearLayout ll_move;
    RelativeLayout ll_coupon;
    private ImageView iv_card;
    private boolean isHide = true;
    private EditText et_phone, et_comment;
    private ImageView iv_product;
    private TextView tv_product_name, tv_color, tv_size, tv_product_money, order_money_title, tv_dyg_money, tv_yunfei, tv_dingjin, tv_confirm_pay;
    AffirmProductInfo affirmProductInfo;
    HttpControl httpControl=new HttpControl();
    boolean isShow=true;
    //Vip折扣名字
    TextView vip_name_textview;
    //折扣价格
    TextView vip_namevalue_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_order_for_zhuangui);
        super.onCreate(savedInstanceState);
        if(this.getIntent().getSerializableExtra("ProductInfo")==null)
        {
            MyApplication.getInstance().showMessage(this,"数据错误");
            finish();
            return;
        }
        affirmProductInfo=(AffirmProductInfo)this.getIntent().getSerializableExtra("ProductInfo");
        initView();
    }


    /********
     * 获取商品可用会员卡
     * 如果 存在 则 返回列表  如果 列表为空  则 提示 用户绑定会员卡
     * *********/
    void requestMemberCardList()
    {
        String ProductId=affirmProductInfo.getData().getData().getProductId();//商品id
        String BuyerId= PerferneceUtil.getString(SharedUtil.user_id);//用户id
        httpControl.getVipCards(isShow, BuyerId, ProductId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestMemberCardBean requestMemberCardBean=(RequestMemberCardBean)obj;
                if(requestMemberCardBean.getData()!=null && requestMemberCardBean.getData().size()==0)
                {
                    //没有绑定会员卡 提示 绑定会员卡
                }
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        },this);
    }

    private void initView() {
        //Vip折扣名字
        vip_name_textview=(TextView)findViewById(R.id.vip_name_textview);
        //折扣价格
        vip_namevalue_textview=(TextView)findViewById(R.id.vip_namevalue_textview);

        TextView tv_choose_card=getView(R.id.tv_choose_card);
        tv_choose_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(==null)
//                {
//                    requestMemberCardList();
//                }
            }
        });

        et_phone = getView(R.id.et_phone);
        iv_product = getView(R.id.iv_product);
        List<ProductsDetailsTagInfo> pic_array= affirmProductInfo.getData().getData().getProductPic();
        if(pic_array.size()>0)
        {
            String url=pic_array.get(0).getLogo();
            MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(url),iv_product,MyApplication.getInstance().getDisplayImageOptions());
        }

        tv_product_name = getView(R.id.tv_product_name);
        tv_product_name.setText(ToolsUtil.nullToString(affirmProductInfo.getData().getData().getProductName()));
        tv_size = getView(R.id.tv_size);
        tv_size.setText("尺码："+ToolsUtil.nullToString(affirmProductInfo.getSizeName()));
        tv_color = getView(R.id.tv_color);
        tv_color.setText("颜色："+ToolsUtil.nullToString(affirmProductInfo.getColorName()));
        tv_product_money = getView(R.id.tv_product_money);
        tv_product_money.setText("商品金额：￥"+ToolsUtil.nullToString(affirmProductInfo.getData().getData().getPrice()+" x "+affirmProductInfo.getBuycount()));
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