package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.AffirmProductInfo;
import com.shenma.yueba.baijia.modle.DaYangGouDisInfoBean;
import com.shenma.yueba.baijia.modle.MemberCardBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestCreateOrderInfo;
import com.shenma.yueba.baijia.modle.RequestMemberCardBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
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
    TextView rl_card_change_textview;
    TextView tv_choose_card;
    TextView et_name;//发票抬头信息
    RadioGroup rg_fapiao;//发票抬头数组
    private boolean isHide = true;
    private EditText et_phone, et_comment;
    private ImageView iv_product;
    private TextView tv_product_name, tv_color, tv_size, tv_product_money, order_money_title, tv_dyg_money, tv_yunfei, tv_dingjin, tv_confirm_pay;
    AffirmProductInfo affirmProductInfo;
    HttpControl httpControl = new HttpControl();
    boolean isShow = true;
    //Vip折扣名字
    TextView vip_name_textview;
    //折扣价格
    TextView vip_namevalue_textview;
    boolean isrunning = false;

    //会员卡信息
    List<MemberCardBean> memberCardBeanList = null;
    //会员卡 视图列表
    List<View> memberCardViewList = new ArrayList<View>();
    //用户选择的会员卡
    MemberCardBean currCheckedMemberCardBean = null;

    int movedistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_order_for_zhuangui);
        super.onCreate(savedInstanceState);
        if (this.getIntent().getSerializableExtra("ProductInfo") == null) {
            MyApplication.getInstance().showMessage(this, "数据错误");
            finish();
            return;
        }
        affirmProductInfo = (AffirmProductInfo) this.getIntent().getSerializableExtra("ProductInfo");
        initView();
        jisuanPrice();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //请求获取 用户数据
        memeberTextChange();
    }

    void memeberTextChange() {
        //当前商品 是否可用 会员卡 支付
        boolean isJoinDeiscount=affirmProductInfo.getData().getData().isJoinDeiscount();
        if(isJoinDeiscount)
        {
            //显示 会员卡 显示折扣
           View rl_card=findViewById(R.id.rl_card);
            rl_card.setVisibility(View.VISIBLE);

            View ll_coupon=findViewById(R.id.ll_coupon);
            ll_coupon.setVisibility(View.VISIBLE);
        }else
        {
            //隐藏 会员卡 显示折扣
            View rl_card=findViewById(R.id.rl_card);
            rl_card.setVisibility(View.GONE);

            View ll_coupon=findViewById(R.id.ll_coupon);
            ll_coupon.setVisibility(View.GONE);
        }



        //是否绑定手机
        boolean IsBindMobile = SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
        if (IsBindMobile) {
            tv_choose_card.setHint("请选择");
            rl_card_change_textview.setText("选择金鹰会员卡");
            iv_card.setBackgroundResource(R.drawable.arrow_down);
            if (!isHide) {
                ll_move.startAnimation(animUp);
                iv_card.startAnimation(rotateAnimationDown);
                isHide = true;
            }
        } else {
            tv_choose_card.setHint("");
            rl_card_change_textview.setText("绑定金鹰会员卡");
            iv_card.setBackgroundResource(R.drawable.arrow_right);
        }
    }


    /********
     * 获取商品可用会员卡
     * 如果 存在 则 返回列表  如果 列表为空  则 提示 用户绑定会员卡
     *********/
    void requestMemberCardList() {
        isrunning = true;
        String ProductId = affirmProductInfo.getData().getData().getProductId();//商品id
        String BuyerId =affirmProductInfo.getData().getData().getBuyerId();
        String count=Integer.toString(affirmProductInfo.getBuycount());
        httpControl.getVipCards(isShow, BuyerId, ProductId,count, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isrunning = false;
                RequestMemberCardBean requestMemberCardBean = (RequestMemberCardBean) obj;
                if (requestMemberCardBean.getData() != null && requestMemberCardBean.getData().size() > 0) {
                    //没有绑定会员卡 提示 绑定会员卡
                    setItemValue(requestMemberCardBean);
                } else {
                    MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, "该商品无可用的会员卡");
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                isrunning = false;
                MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, msg);
            }
        }, this);
    }


    /*******
     * 根据会员卡列表 个数 设置 子类视图
     ****/
    void setItemValue(RequestMemberCardBean requestMemberCardBean) {
        List<MemberCardBean> bean_array = requestMemberCardBean.getData();
        if (bean_array != null && bean_array.size() > 0) {
            memberCardBeanList = bean_array;
            setCardListView();
            initAnimation();
            memberShowOrHidden();
        }

    }


    /********
     * 赋值 会员卡 视图
     ******/
    void setCardListView() {
        if (memberCardBeanList != null) {
            movedistance = 0;
            int childItemHieght = 0;
            for (int i = 0; i < memberCardBeanList.size(); i++) {
                MemberCardBean memberCardBean = memberCardBeanList.get(i);
                View itemll = LayoutInflater.from(ConfirmOrderForZhuanGui.this).inflate(R.layout.cardlist_item_layout, null);
                int height = getResources().getDimensionPixelSize(R.dimen.dimen_cardItemHeight);
                movedistance += height;
                //卡类型
                TextView membercard_name = (TextView) itemll.findViewById(R.id.cardlist_item_layout_name_textview);
                membercard_name.setText(ToolsUtil.nullToString(memberCardBean.getCardtypename()) + "：");
                //卡号
                TextView cardlist_item_layout_no_textview = (TextView) itemll.findViewById(R.id.cardlist_item_layout_no_textview);
                cardlist_item_layout_no_textview.setText(ToolsUtil.nullToString(memberCardBean.getCardno()));
                //折扣
                TextView cardlist_item_layout_discount_textview = (TextView) itemll.findViewById(R.id.cardlist_item_layout_discount_textview);
                cardlist_item_layout_discount_textview.setText(memberCardBean.getVipdiscount() + "折");

                itemll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int j = 0; j < memberCardViewList.size(); j++) {
                            memberCardViewList.get(j).setSelected(false);
                        }
                        currCheckedMemberCardBean = (MemberCardBean) v.getTag();
                        v.setSelected(true);
                        tv_choose_card.setText(ToolsUtil.nullToString(currCheckedMemberCardBean.getCardtypename()) + "(" + currCheckedMemberCardBean.getVipdiscount() + "折)");
                        SystemClock.sleep(100);
                        memberShowOrHidden();
                        jisuanPrice();
                    }
                });

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                itemll.setTag(memberCardBeanList.get(i));
                ll_content.addView(itemll, layoutParams);
                memberCardViewList.add(itemll);
            }
            ViewGroup.LayoutParams params = ll_content.getLayoutParams();
            params.height = (movedistance);
            ll_content.setLayoutParams(params);
        }
    }

    private void initView() {
        rg_fapiao = (RadioGroup) findViewById(R.id.rg_fapiao);
        rg_fapiao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.orderBy1:
                        et_name.setVisibility(View.GONE);
                        break;
                    case R.id.orderBy2:
                        et_name.setVisibility(View.VISIBLE);
                        break;
                    case R.id.orderBy3:
                        et_name.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        //发票抬头
        et_name = (TextView) findViewById(R.id.et_name);
        //Vip折扣名字
        vip_name_textview = (TextView) findViewById(R.id.vip_name_textview);
        //折扣价格
        vip_namevalue_textview = (TextView) findViewById(R.id.vip_namevalue_textview);

        tv_choose_card = getView(R.id.tv_choose_card);

        et_phone = getView(R.id.et_phone);
        iv_product = getView(R.id.iv_product);
        List<ProductsDetailsTagInfo> pic_array = affirmProductInfo.getData().getData().getProductPic();
        if (pic_array.size() > 0) {
            String url = pic_array.get(0).getLogo();
            MyApplication.getInstance().getImageLoader().displayImage(ToolsUtil.nullToString(url), iv_product, MyApplication.getInstance().getDisplayImageOptions());
        }

        tv_product_name = getView(R.id.tv_product_name);
        tv_product_name.setText(ToolsUtil.nullToString(affirmProductInfo.getData().getData().getProductName()));
        tv_size = getView(R.id.tv_size);
        tv_size.setText("尺码：" + ToolsUtil.nullToString(affirmProductInfo.getSizeName()));
        tv_color = getView(R.id.tv_color);
        tv_color.setText("颜色：" + ToolsUtil.nullToString(affirmProductInfo.getColorName()));
        tv_product_money = getView(R.id.tv_product_money);
        tv_product_money.setText("商品金额：￥" + ToolsUtil.nullToString(affirmProductInfo.getData().getData().getPrice() + " x " + affirmProductInfo.getBuycount()));
        et_comment = getView(R.id.et_comment);
        order_money_title = getView(R.id.order_money_title);
        ll_coupon = getView(R.id.ll_coupon);
        tv_dyg_money = getView(R.id.tv_dyg_money);
        tv_yunfei = getView(R.id.tv_yunfei);
        tv_dingjin = getView(R.id.tv_dingjin);
        tv_confirm_pay = getView(R.id.tv_confirm_pay);
        tv_confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumbitOrder();
            }
        });
        setTitle("确认订单");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout rl_card = getView(R.id.rl_card);

        rl_card_change_textview = (TextView) findViewById(R.id.rl_card_change_textview);
        iv_card = getView(R.id.iv_card);

        ll_content = getView(R.id.ll_content);
        ll_move = getView(R.id.ll_move);
        rl_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean IsBindMobile = SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
                //是否参加Vip折扣
                boolean isJoinDeiscount = affirmProductInfo.getData().getData().isJoinDeiscount();
                isJoinDeiscount = true;
                if (!isJoinDeiscount) {
                    MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, "该商品不能使用会员卡");
                    return;
                }
                //如果已经绑定手机号
                if (IsBindMobile) {
                    //判断是否有 会员卡列表  如果有 则直接显示  如果没有则请求
                    if (memberCardBeanList == null) {
                        if (!isrunning) {
                            requestMemberCardList();
                        }
                    } else {
                        memberShowOrHidden();
                    }
                } else {
                    //跳转到绑定页面
                    Intent intent = new Intent(ConfirmOrderForZhuanGui.this, BinderMemberCardActivity.class);
                    startActivityForResult(intent, 200);
                }
            }
        });
    }


    void memberShowOrHidden() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            boolean IsBindMobile = SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
            if (!IsBindMobile) {
                requestUserInfo();
            }
        }
    }

    /******
     * 请求用户信息接口
     ****/
    void requestUserInfo() {
        String userid = SharedUtil.getStringPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_id);
        httpControl.getBaijiaUserInfo(Integer.valueOf(userid), false, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestUserInfoBean bean = (RequestUserInfoBean) obj;
                if (bean.getData() != null) {
                    SharedUtil.setBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile, bean.getData().isBindMobile());
                }
                memeberTextChange();
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        }, ConfirmOrderForZhuanGui.this);
    }


    private void initAnimation() {

        animDown = new TranslateAnimation(0, 0, 0, movedistance);
        animDown.setDuration(500);
        animDown.setFillAfter(true);
        animUp = new TranslateAnimation(0, 0, movedistance, 0);
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

    /******
     * 提交订单
     ****/
    void sumbitOrder() {
        String Mobile = et_phone.getText().toString().trim();
        if (Mobile.equals("")) {
            MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, "提货手机号不能为空");
            return;
        }
        String Memo = et_comment.getText().toString().trim();
        String ProductId = affirmProductInfo.getData().getData().getProductId();
        String Quantity = Integer.toString(affirmProductInfo.getBuycount());
        String SizeId = affirmProductInfo.getSizeId();
        if (SizeId == null || SizeId.equals("")) {
            SizeId = "0";
        }
        String ColorId = affirmProductInfo.getColorId();
        if (ColorId == null || ColorId.equals("")) {
            ColorId = "0";
        }
        String VipCardNo = "";
        if (currCheckedMemberCardBean != null) {
            VipCardNo = currCheckedMemberCardBean.getCardno();
        }

        //是否需要发票
        boolean NeedInvoice = false;
        String InvoiceTitle = "";//发票抬头
        switch (rg_fapiao.getCheckedRadioButtonId()) {
            case R.id.orderBy1:
                NeedInvoice = false;
                break;
            case R.id.orderBy2:
                NeedInvoice = true;
                break;
            case R.id.orderBy3:
                NeedInvoice = true;
                break;
        }

        InvoiceTitle = et_name.getText().toString().trim();
        if (NeedInvoice && InvoiceTitle.equals("")) {
            MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, "发票抬头信息不能为空");
            return;
        }

        httpControl.createOrderV3(true, NeedInvoice, InvoiceTitle, Memo, Mobile, ProductId, Quantity, SizeId, ColorId, VipCardNo, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, "下单成功");
                RequestCreateOrderInfo info = (RequestCreateOrderInfo) obj;
                ToolsUtil.frowardPayActivity(ConfirmOrderForZhuanGui.this, affirmProductInfo.getData().getData().getProductName(), affirmProductInfo.getBuycount(), info.getData().getOrderNo(), info.getData().getActualAmount());
                finish();
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this, msg);
            }
        }, ConfirmOrderForZhuanGui.this);
    }


    /*******
     * 计算 价格
     ******/
    void jisuanPrice() {
        //实际支付价格
        double payPrice = 0;
        //商品单价
        double price = affirmProductInfo.getData().getData().getPrice();
        //吊牌价
        double unitPrice = affirmProductInfo.getData().getData().getUnitPrice();

        //商品单价* 购买数量 =商品总价
        double allprice = price * affirmProductInfo.getBuycount();
        //商家指定的折扣率(格式：85折)
        float vipDiscpunt = affirmProductInfo.getData().getData().getVipDiscount();

        //使用Vip 会员折扣 的价格
        double vipDiscrate = 0;
        //打样够
        DaYangGouDisInfoBean DaYangGouDis = affirmProductInfo.getData().getData().getDaYangGouDis();
        //打样够 立减金额
        double dangyanggouprice = 0;

        order_money_title.setText("订单金额：￥" + allprice);

        //如果 存在 已选择 的 会员卡
        if (currCheckedMemberCardBean != null) {
            //一次折扣率(85折)  返回是 数据 0.03  相当于 97折
            float discrate1 = (100-(currCheckedMemberCardBean.getDiscrate1()*100));
            //二次折扣率(85折)
            float discrate2 = 100-(currCheckedMemberCardBean.getDiscrate2()*100);
            //使用的折扣
            float checked_discrate = 0;
            if (price < unitPrice) {
                checked_discrate = discrate2 < vipDiscpunt ? vipDiscpunt : discrate2;
            } else {
                checked_discrate = discrate1 < vipDiscpunt ? vipDiscpunt : discrate1;
            }
            vipDiscrate = allprice * ((100 - checked_discrate) / 100);
            if (vipDiscrate < 0) {
                vipDiscrate = 0;
            }
            //设置 Vip折扣
            vip_namevalue_textview.setText("立减：￥" + ToolsUtil.DounbleToString_2(vipDiscrate));
        } else {
            //设置 Vip折扣
            vip_namevalue_textview.setText("");
        }

        if (DaYangGouDis != null) {
            //打烊购折扣率(格式：0.03  相当于 97折)
            float discount = DaYangGouDis.getDiscount();
            //打样购最大折扣金额
            double maxPrice = DaYangGouDis.getMaxamount();
            dangyanggouprice = (allprice - vipDiscrate) * discount;
            dangyanggouprice = dangyanggouprice < maxPrice ? dangyanggouprice : maxPrice;
        }

        //打样够立减
        tv_dyg_money.setText(ToolsUtil.DounbleToString_2(dangyanggouprice));

        payPrice = allprice - vipDiscrate - dangyanggouprice;
        if (payPrice < 0) {
            payPrice = 0;
        }
        tv_dingjin.setText(ToolsUtil.DounbleToString_2(payPrice));
    }
}