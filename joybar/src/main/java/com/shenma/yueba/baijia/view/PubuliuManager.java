package com.shenma.yueba.baijia.view;

import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListInfo;
import com.shenma.yueba.baijia.modle.MyFavoriteProductListPic;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.wxapi.utils.UtilsTool;
import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author gyj
 * @version 创建时间：2015-6-9 下午2:48:26
 *          程序的简单说明  瀑布流管理
 */

public class PubuliuManager {
    Context context;
    View parent;
    //瀑布流 左右布局
    LinearLayout pubuliy_left_linearlayout, pubuliy_right_linearlayout;
    int leftHeight;//左侧高度
    int rightHeight;//右侧高度
    HttpControl httpControl = new HttpControl();

    public PubuliuManager(Context context, View parent) {
        this.context = context;
        this.parent = parent;
        initView();
    }

    /***
     * 加载视图
     **/
    void initView() {
        pubuliy_left_linearlayout = (LinearLayout) parent.findViewById(R.id.pubuliy_left_linearlayout);
        pubuliy_right_linearlayout = (LinearLayout) parent.findViewById(R.id.pubuliy_right_linearlayout);
    }

    /****
     * 设置刷新
     *
     * @param item List<MyFavoriteProductListInfo>
     ***/
    public void onResher(List<MyFavoriteProductListInfo> item) {
        leftHeight = 0;
        rightHeight = 0;
        pubuliy_left_linearlayout.removeAllViews();
        pubuliy_right_linearlayout.removeAllViews();
        addItem(item);
    }


    /****
     * 加载数据
     *
     * @param item List<MyFavoriteProductListInfo>
     ****/
    public void onaddData(List<MyFavoriteProductListInfo> item) {
        addItem(item);
    }


    /*******
     * 设置 瀑布流的 高度
     *****/
    void addItem(List<MyFavoriteProductListInfo> item) {
        if (item != null) {

            for (int i = 0; i < item.size(); i++) {
                MyFavoriteProductListInfo bean = item.get(i);
                //左侧布局的宽度（即内部图片的 宽度）
                int witdh = pubuliy_left_linearlayout.getWidth();
                MyFavoriteProductListPic myFavoriteProductListPic = bean.getPic();
                if (myFavoriteProductListPic == null) {
                    myFavoriteProductListPic = new MyFavoriteProductListPic();
                }
                //根据 图片的宽高比 计算出 图片的 高度
                int height = (int) (witdh * myFavoriteProductListPic.getRatio());
                //图片的布局对象
                View parentview = LinearLayout.inflate(context, R.layout.pubuliu_item_layout, null);
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

                pubuliu_item_layout_like_imageview.setSelected(bean.isIsFavorite());
                pubuliu_item_layout_like_textview.setText(Integer.toString(bean.getFavoriteCount()));
                pubuliu_item_layout_like_linearlayout.setTag(bean);
                /*if(bean.getLikeUser()!=null)
    			{
    				MyFavoriteProductListLikeUser likeuser=bean.getLikeUser();
    				pubuliu_item_layout_like_textview.setSelected(likeuser.isIsLike());
    				pubuliu_item_layout_like_textview.setText(likeuser.getCount()+"");
    				pubuliu_item_layout_like_textview.setTag(bean);
    			}*/
                ImageView iv = (ImageView) parentview.findViewById(R.id.pubuliu_item_layout_imageview);
                if (height > 0) {
                    Log.i("TAG", "height=" + height + " witdh=" + witdh + "ration=" + myFavoriteProductListPic.getRatio());
                    //设置 图片的高度
                    iv.getLayoutParams().height = height;
                } else {
                    height = witdh;
                    Log.i("TAG", "height=" + height + " witdh=" + witdh + "ration=" + myFavoriteProductListPic.getRatio());
                    //设置 图片的高度
                    iv.getLayoutParams().height = height;
                }
                iv.setTag(bean);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (v.getTag() == null) {
                            return;
                        }
                        MyFavoriteProductListInfo bean = (MyFavoriteProductListInfo) v.getTag();
                        ToolsUtil.forwardProductInfoActivity(context, bean.getId());
                    }
                });
                iv.setImageResource(R.drawable.default_pic);
                iv.setScaleType(ScaleType.CENTER_CROP);
                android.util.Log.i("TAG", "leftHeight=" + leftHeight + "   rightHeight=" + rightHeight);
                //根据 左右高度判断
                if (leftHeight <= rightHeight) {
                    leftHeight += height;
                    pubuliy_left_linearlayout.addView(parentview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    rightHeight += height;
                    pubuliy_right_linearlayout.addView(parentview, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                ToolsUtil.setFontStyle(context, parentview, R.id.pubuliu_item_layout_pricevalue_textview, R.id.pubuliu_item_layout_name_textview, R.id.pubuliu_item_layout_like_textview, R.id.pubuliu_item_layout_price_textview);
                initPic(ToolsUtil.getImage(ToolsUtil.nullToString(myFavoriteProductListPic.getPic()), 320, 0), iv);
            }
        }
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pubuliu_item_layout_like_linearlayout:

                    if (v.getTag() != null && v.getTag() instanceof MyFavoriteProductListInfo) {
                        MyFavoriteProductListInfo bean = (MyFavoriteProductListInfo) v.getTag();
                        if (bean.isIsFavorite()) {
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
        MyApplication.getInstance().getBitmapUtil().display(iv, url);
    }


    /****
     * 提交收藏与取消收藏商品
     *
     * @param Status int   0表示取消收藏   1表示收藏
     * @param bean   MyFavoriteProductListInfo  商品对象
     * @param v      View  商品对象
     **/
    void submitAttention(final int Status, final MyFavoriteProductListInfo bean, final View v) {
        httpControl.setFavor(bean.getId(), Status, new HttpCallBackInterface() {

            @Override
            public void http_Success(Object obj) {
				/*if(v!=null && v instanceof TextView)
				{
					MyFavoriteProductListLikeUser myFavoriteProductListLikeUser=bean.getLikeUser();
					if(myFavoriteProductListLikeUser!=null)
					{
						int count=bean.getLikeUser().getCount();
						switch(Status)
						{
						case 0:
							count--;
							if(count<0)
							{
								count=0;
							}
							bean.getLikeUser().setCount(count);
							v.setSelected(false);
							((TextView)v).setText(count+"");
							myFavoriteProductListLikeUser.setIsLike(false);
							break;
						case 1:
							count++;
							bean.getLikeUser().setCount(count);
							((TextView)v).setText(count+"");
							v.setSelected(true);
							myFavoriteProductListLikeUser.setIsLike(true);
							break;
						}
					}
					
				}*/

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
                            bean.setIsFavorite(false);
                            break;
                        case 1:
                            count++;
                            bean.setFavoriteCount(count);
                            bean.setIsFavorite(true);
                            if (pubuliu_item_layout_like_imageview != null) {
                                pubuliu_item_layout_like_imageview.setSelected(true);
                            }
                            if (pubuliu_item_layout_like_textview != null) {
                                pubuliu_item_layout_like_textview.setText(count + "");
                            }
                            break;
                    }

                }

                switch(Status)
                {
                    case 0:
                        sendBroadcase(bean.getId(),false);
                        break;
                    case 1:
                        sendBroadcase(bean.getId(),true);
                        break;
                }

            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(context, msg);
            }
        }, context);
    }

    /*******
     * 发送广播 通知 收藏商品 或取消 收藏商品成功
     *
     * @param isFavor    boolean  true 收藏成功  false 取消收藏成功
     * @param product_id int 商品id
     ******/
    void sendBroadcase(int product_id,boolean isFavor) {
        Intent intent = new Intent(Constants.PRODUCTFAVOR_INTENT_ACTION);
        intent.putExtra("product_id",product_id);
        intent.putExtra("isFavor",isFavor);
        MyApplication.getInstance().sendBroadcast(intent);
    }
}
