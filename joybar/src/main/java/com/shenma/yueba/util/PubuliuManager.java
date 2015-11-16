package com.shenma.yueba.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.newmodel.PubuliuBeanInfo;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;

import https.CommonHttpControl;
import interfaces.HttpCallBackInterface;

/**
 * @author gyj
 * @version 创建时间：2015-6-9 下午2:48:26
 *          程序的简单说明  瀑布流管理
 */

public class PubuliuManager {
    Activity activity;
    ViewGroup parent;
    //瀑布流 左右布局
    LinearLayout pubuliy_left_linearlayout, pubuliy_right_linearlayout;
    int leftHeight;//左侧高度
    int rightHeight;//右侧高度
    LayoutInflater layoutInflater;
    List<PubuliuBeanInfo> item=new ArrayList<PubuliuBeanInfo>();
    int countAnimationTime=3000;

    public PubuliuManager(Activity activity, ViewGroup parent) {
        this.activity = activity;
        this.parent = parent;
        layoutInflater = LayoutInflater.from(activity);
        initView();
    }

    /***
     * 加载视图
     **/
    void initView() {
        View view = layoutInflater.inflate(R.layout.pubuliu_layout, parent, true);
        pubuliy_left_linearlayout = (LinearLayout) view.findViewById(R.id.pubuliy_left_linearlayout);
        pubuliy_right_linearlayout = (LinearLayout) view.findViewById(R.id.pubuliy_right_linearlayout);
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 200:
                    if(msg.obj!=null && msg.obj instanceof List)
                    {
                        List<PubuliuBeanInfo> item=(List<PubuliuBeanInfo>)msg.obj;
                    }
                    addItem(item);
                    break;
            }

        }
    };

    void sendMsg(Object obj)
    {
        Message msg=handler.obtainMessage(200);
        msg.obj=obj;
        handler.sendMessageDelayed(msg,200);
    }

    /****
     * 设置刷新
     *
     * @param _item List<MyFavoriteProductListInfo>
     ***/
    public void onResher(List<PubuliuBeanInfo> _item) {
        item.clear();
        if(item!=null)
        {
            item.addAll(_item);
        }
        leftHeight = 0;
        rightHeight = 0;
        pubuliy_left_linearlayout.removeAllViews();
        pubuliy_right_linearlayout.removeAllViews();
        sendMsg(_item);
    }


    /****
     * 加载数据
     *
     * @param _item List<MyFavoriteProductListInfo>
     ****/
    public void onaddData(List<PubuliuBeanInfo> _item) {
        if(item!=null)
        {
            item.addAll(_item);
        }
        sendMsg(_item);
    }

    /*******
     * 设置 瀑布流的 高度
     *****/
    void addItem(List<PubuliuBeanInfo> item) {
        if (item != null) {

            for (int i = 0; i < item.size(); i++) {
                PubuliuBeanInfo bean = item.get(i);
                //左侧布局的宽度（即内部图片的 宽度）
                int witdh = pubuliy_left_linearlayout.getWidth();
                String url = bean.getPicurl();
                //根据 图片的宽高比 计算出 图片的 高度
                int height = (int) (witdh * bean.getRation());
                //图片的布局对象
                View parentview = LinearLayout.inflate(activity, R.layout.pubuliu_item_layout, null);
                //价格
                TextView pubuliu_item_layout_pricevalue_textview = (TextView) parentview.findViewById(R.id.pubuliu_item_layout_pricevalue_textview);
                pubuliu_item_layout_pricevalue_textview.setText(bean.getPrice() + "");
                //商品名称
                TextView pubuliu_item_layout_name_textview = (TextView) parentview.findViewById(R.id.pubuliu_item_layout_name_textview);
                pubuliu_item_layout_name_textview.setText(bean.getName());
                //收藏
                LinearLayout pubuliu_item_layout_like_linearlayout = (LinearLayout) parentview.findViewById(R.id.pubuliu_item_layout_like_linearlayout);
                TextView pubuliu_item_layout_like_textview = (TextView) parentview.findViewById(R.id.pubuliu_item_layout_like_textview);
                ImageView pubuliu_item_layout_like_imageview = (ImageView) parentview.findViewById(R.id.pubuliu_item_layout_like_imageview);
                pubuliu_item_layout_like_linearlayout.setOnClickListener(onClickListener);

                pubuliu_item_layout_like_imageview.setSelected(bean.iscollection());
                //pubuliu_item_layout_like_textview.setText(Integer.toString(bean.getFavoriteCount()));
                pubuliu_item_layout_like_linearlayout.setTag(bean);

                ImageView iv = (ImageView) parentview.findViewById(R.id.pubuliu_item_layout_imageview);
                if (height > 0) {
                    Log.i("TAG", "height=" + height + " witdh=" + witdh + "ration=" + bean.getRation());
                    //设置 图片的高度
                    iv.getLayoutParams().height = height;
                } else {
                    height = witdh;
                    Log.i("TAG", "height=" + height + " witdh=" + witdh + "ration=" + bean.getRation());
                    //设置 图片的高度
                    iv.getLayoutParams().height = height;
                }
                iv.setTag(bean);
                iv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (v.getTag() == null) {
                            return;
                        }
                        PubuliuBeanInfo bean = (PubuliuBeanInfo) v.getTag();
                        ToolsUtil.forwardProductInfoActivity(activity, Integer.valueOf(bean.getId()));
                    }
                });
                iv.setImageResource(R.drawable.default_pic);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                android.util.Log.i("TAG", "leftHeight=" + leftHeight + "   rightHeight=" + rightHeight);
                //根据 左右高度判断
                if (leftHeight <= rightHeight) {
                    leftHeight += height;
                    pubuliy_left_linearlayout.addView(parentview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //动画
                    startAnimationn(parentview, i, item.size(),leftHeight);
                } else {
                    rightHeight += height;
                    pubuliy_right_linearlayout.addView(parentview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //动画
                    startAnimationn(parentview, i, item.size(), rightHeight);
                }
                initPic(bean.getPicurl(),iv);

            }
        }
    }


    void startAnimationn(View view,int i,int count,int height)
    {
        int a_time=countAnimationTime/count;
        AlphaAnimation alphaAnimation=new AlphaAnimation(0f,1f);
        //alphaAnimation.setDuration(a_time * (i + 1));

        TranslateAnimation translateAnimation=new TranslateAnimation(0,0,height,0);


        AnimationSet as=new AnimationSet(true);
        as.setDuration(a_time * (i + 1));
        as.addAnimation(alphaAnimation);
        as.addAnimation(translateAnimation);
        as.setFillAfter(true);
        view.startAnimation(as);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pubuliu_item_layout_like_linearlayout:

                    if (v.getTag() != null && v.getTag() instanceof PubuliuBeanInfo) {
                        PubuliuBeanInfo bean = (PubuliuBeanInfo) v.getTag();
                        if (bean.iscollection()) {
                            submitAttention(0, bean, v);
                        } else {
                            submitAttention(1, bean, v);
                        }

                    }
                    break;
            }
        }
    };

    /****
     * 加载图片
     */
    void initPic(final String url, final ImageView iv) {
        MyApplication.getInstance().getImageLoader().displayImage(url, iv, MyApplication.getInstance().getDisplayImageOptions());
    }


    /****
     * 提交收藏与取消收藏商品
     *
     * @param Status int   0表示取消收藏   1表示收藏
     * @param bean   MyFavoriteProductListInfo  商品对象
     * @param v      View  商品对象
     **/
    void submitAttention(final int Status, final PubuliuBeanInfo bean, final View v) {
        CommonHttpControl.setFavor(Integer.toString(Status), bean.getId(), new HttpCallBackInterface<BaseRequest>() {
            @Override
            public void http_Success(BaseRequest obj) {
                if (v != null) {
                    TextView pubuliu_item_layout_like_textview = (TextView) v.findViewById(R.id.pubuliu_item_layout_like_textview);
                    ImageView pubuliu_item_layout_like_imageview = (ImageView) v.findViewById(R.id.pubuliu_item_layout_like_imageview);
                    //MyFavoriteProductListLikeUser myFavoriteProductListLikeUser=bean.getLikeUser();
                    int count = bean.getFavoriteCount();
                    switch (Status) {
                        case 0:
                            count--;
                            if (count < 0) {
                                count = 0;
                            }
                            bean.setFavoriteCount(count);
                            if (pubuliu_item_layout_like_imageview != null) {
                                pubuliu_item_layout_like_imageview.setSelected(false);
                            }
                            if (pubuliu_item_layout_like_textview != null) {
                                pubuliu_item_layout_like_textview.setText(count + "");
                            }
                            bean.setIscollection(false);
                            break;
                        case 1:
                            count++;
                            bean.setFavoriteCount(count);
                            bean.setIscollection(true);
                            if (pubuliu_item_layout_like_imageview != null) {
                                pubuliu_item_layout_like_imageview.setSelected(true);
                            }
                            if (pubuliu_item_layout_like_textview != null) {
                                pubuliu_item_layout_like_textview.setText(count + "");
                            }
                            break;
                    }

                }

                switch (Status) {
                    case 0:
                        //sendBroadcase(bean.getId(), false);
                        break;
                    case 1:
                        //sendBroadcase(bean.getId(), true);
                        break;
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(activity, msg);
            }
        });
    }

    /*******
     * 发送广播 通知 收藏商品 或取消 收藏商品成功
     *
     * @param isFavor    boolean  true 收藏成功  false 取消收藏成功
     * @param product_id int 商品id
     ******/
    void sendBroadcase(int product_id, boolean isFavor) {
        /*Intent intent = new Intent(Constants.PRODUCTFAVOR_INTENT_ACTION);
        intent.putExtra("product_id", product_id);
        intent.putExtra("isFavor", isFavor);
        MyApplication.getInstance().sendBroadcast(intent);*/
    }
}