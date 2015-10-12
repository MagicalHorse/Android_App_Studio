package com.shenma.yueba.broadcaseReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shenma.yueba.constants.Constants;

/**
 * Created by Administrator on 2015/10/8.
 * 广播监听类 用于 监听 商品收藏成功 或取消收藏成功 所发送的广播通知
 *
 */
public class ProductFavorBroadcase extends BroadcastReceiver {
    ProductFavorBroadcaseListener productFavorBroadcaseListener;

    public ProductFavorBroadcase(ProductFavorBroadcaseListener productFavorBroadcaseListener) {
        this.productFavorBroadcaseListener = productFavorBroadcaseListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(Constants.PRODUCTFAVOR_INTENT_ACTION)) {
                boolean isFavor = intent.getBooleanExtra("isFavor", false);
                int product_id = intent.getIntExtra("product_id", -1);
                if(productFavorBroadcaseListener!=null)
                {
                    productFavorBroadcaseListener.productFavor(isFavor,product_id);
                }
            }
        }
    }

    public interface ProductFavorBroadcaseListener {
        /********
         * 当前商品 的收藏状态
         *
         * @param isFavor    boolean true已收藏 false没有收藏
         * @param product_id int 商品id
         ***/
        void productFavor(boolean isFavor, int product_id);
    }
}
