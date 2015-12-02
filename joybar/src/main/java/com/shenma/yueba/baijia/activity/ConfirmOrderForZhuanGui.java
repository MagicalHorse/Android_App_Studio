package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.AffirmProductInfo;
import com.shenma.yueba.baijia.modle.MemberCardBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestMemberCardBean;
import com.shenma.yueba.baijia.modle.RequestUserInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.PerferneceUtil;
import com.shenma.yueba.util.ShareUtil;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import org.w3c.dom.Text;

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
    boolean isrunning=false;

    //会员卡信息
    List<MemberCardBean>  memberCardBeanList=null;
    //会员卡 视图列表
    List<View> memberCardViewList=new ArrayList<View>();
    //用户选择的会员卡
    MemberCardBean currCheckedMemberCardBean=null;

    int movedistance=0;

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


    @Override
    protected void onResume() {
        super.onResume();
        //请求获取 用户数据
        memeberTextChange();
    }

    void memeberTextChange()
    {
        boolean IsBindMobile=SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
        if(IsBindMobile)
        {
            tv_choose_card.setHint("请选择");
            rl_card_change_textview.setText("选择金鹰会员卡");
            iv_card.setBackgroundResource(R.drawable.arrow_down);
            if (!isHide) {
                ll_move.startAnimation(animUp);
                iv_card.startAnimation(rotateAnimationDown);
                isHide = true;
            }
        }else
        {
            tv_choose_card.setHint("");
            rl_card_change_textview.setText("绑定金鹰会员卡");
            iv_card.setBackgroundResource(R.drawable.arrow_right);
        }
    }


    /********
     * 获取商品可用会员卡
     * 如果 存在 则 返回列表  如果 列表为空  则 提示 用户绑定会员卡
     * *********/
    void requestMemberCardList()
    {
        isrunning=true;
        String ProductId=affirmProductInfo.getData().getData().getProductId();//商品id
        String BuyerId= PerferneceUtil.getString(SharedUtil.user_id);//用户id
        httpControl.getVipCards(isShow, BuyerId, ProductId, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isrunning = false;
                RequestMemberCardBean requestMemberCardBean = (RequestMemberCardBean) obj;
                for(int i=0;i<3;i++)
                {
                    MemberCardBean bean=new MemberCardBean();
                    bean.setCardno("111111"+i);
                    bean.setCardtypename("卡类型" + i);
                    bean.setVipdiscount(1.7f);
                    requestMemberCardBean.getData().add(bean);
                }
                if (requestMemberCardBean.getData() != null && requestMemberCardBean.getData().size() > 0) {
                    //没有绑定会员卡 提示 绑定会员卡
                    setItemValue(requestMemberCardBean);
                }else
                {
                    MyApplication.getInstance().showMessage(ConfirmOrderForZhuanGui.this,"无可用的会员卡");
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
     * ****/
    void setItemValue(RequestMemberCardBean requestMemberCardBean)
    {
        List<MemberCardBean>  bean_array= requestMemberCardBean.getData();
        if(bean_array!=null && bean_array.size()>0)
        {
            memberCardBeanList=bean_array;
            setCardListView();
            initAnimation();
            memberShowOrHidden();
        }

    }


    /********
     * 赋值 会员卡 视图
     * ******/
    void setCardListView()
    {
        if(memberCardBeanList!=null)
        {
            movedistance=0;
            int childItemHieght=0;
            for(int i=0;i<memberCardBeanList.size();i++)
            {
                MemberCardBean memberCardBean=memberCardBeanList.get(i);
                View itemll=LayoutInflater.from(ConfirmOrderForZhuanGui.this).inflate(R.layout.cardlist_item_layout,null);
                int height=getResources().getDimensionPixelSize(R.dimen.dimen_cardItemHeight);
                movedistance+=height;
                //卡类型
                TextView membercard_name=(TextView)itemll.findViewById(R.id.cardlist_item_layout_name_textview);
                membercard_name.setText(ToolsUtil.nullToString(memberCardBean.getCardtypename()) + "：");
                //卡号
                TextView cardlist_item_layout_no_textview=(TextView)itemll.findViewById(R.id.cardlist_item_layout_no_textview);
                cardlist_item_layout_no_textview.setText(ToolsUtil.nullToString(memberCardBean.getCardno()));
                //折扣
                TextView cardlist_item_layout_discount_textview=(TextView)itemll.findViewById(R.id.cardlist_item_layout_discount_textview);
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
                        memberShowOrHidden();
                    }
                });

                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
                itemll.setTag(memberCardBeanList.get(i));
                ll_content.addView(itemll, layoutParams);
                memberCardViewList.add(itemll);
            }
            ViewGroup.LayoutParams params=ll_content.getLayoutParams();
            params.height=(movedistance);
            ll_content.setLayoutParams(params);
        }
    }

    private void initView() {
        //Vip折扣名字
        vip_name_textview=(TextView)findViewById(R.id.vip_name_textview);
        //折扣价格
        vip_namevalue_textview=(TextView)findViewById(R.id.vip_namevalue_textview);

        tv_choose_card=getView(R.id.tv_choose_card);

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

        rl_card_change_textview=(TextView)findViewById(R.id.rl_card_change_textview);
        iv_card = getView(R.id.iv_card);

        ll_content = getView(R.id.ll_content);
        ll_move = getView(R.id.ll_move);
        rl_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean IsBindMobile=SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
                //如果已经绑定手机号
                if(IsBindMobile)
                {
                    //判断是否有 会员卡列表  如果有 则直接显示  如果没有则请求
                    if(memberCardBeanList==null)
                    {
                        if(!isrunning)
                        {
                            requestMemberCardList();
                        }
                    }else
                    {
                        memberShowOrHidden();
                    }
                }else
                {
                    //跳转到绑定页面
                    Intent intent=new Intent(ConfirmOrderForZhuanGui.this,BinderMemberCardActivity.class);
                    startActivityForResult(intent,200);
                }
            }
        });
    }


    void memberShowOrHidden()
    {
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
        if(requestCode==200)
        {
            boolean IsBindMobile=SharedUtil.getBooleanPerfernece(ConfirmOrderForZhuanGui.this, SharedUtil.user_IsBindMobile);
            if(!IsBindMobile)
            {
                requestUserInfo();
            }
        }
    }

    /******
     * 请求用户信息接口
     * ****/
    void requestUserInfo()
    {
        String userid=SharedUtil.getStringPerfernece(ConfirmOrderForZhuanGui.this,SharedUtil.user_id);
        httpControl.getBaijiaUserInfo(Integer.valueOf(userid), false, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                RequestUserInfoBean bean=(RequestUserInfoBean)obj;
                if(bean.getData()!=null)
                {
                    SharedUtil.setBooleanPerfernece(ConfirmOrderForZhuanGui.this,SharedUtil.user_IsBindMobile,bean.getData().isBindMobile());
                }
                memeberTextChange();
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        }, ConfirmOrderForZhuanGui.this);
    }



    private void initAnimation() {

        animDown = new TranslateAnimation(0, 0, 0,movedistance);
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
}