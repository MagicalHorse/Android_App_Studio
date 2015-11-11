package com.shenma.yueba.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;

/**
 * Created by Administrator on 2015/11/4.
 */
public class ImageLoaderManager {
    static  ImageLoaderManager imageLoaderManager;
    BitmapUtils  bitmapUtils;
    BitmapDisplayConfig bigPicDisplayConfig;
    ImageLoaderManager()
    {
        initBitmapUtils();
    }

    public static ImageLoaderManager getInstace()
    {
        if(imageLoaderManager==null)
        {
            imageLoaderManager=new ImageLoaderManager();
        }
        return imageLoaderManager;
    }

    private void initBitmapUtils() {
        bitmapUtils = new BitmapUtils(MyApplication.getInstance().getApplicationContext());
        bigPicDisplayConfig = new BitmapDisplayConfig();
        // bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
        // 图片太大时容易OOM。
        bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
        bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(MyApplication.getInstance().getApplicationContext()));
        bitmapUtils.configDefaultLoadingImage(R.mipmap.default_pic);// 默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.default_pic);// 加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
    }

    /*********
     * 异步加载图片
     * **********/
    public  void display(ImageView iv,String url)
    {
        bitmapUtils.display(iv, ToolsUtil.nullToString(url), bigPicDisplayConfig);
    }

    public void display(View iv,String url,BitmapLoadCallBack callBack)
    {
        bitmapUtils.display(iv, ToolsUtil.nullToString(url), bigPicDisplayConfig,callBack);
    }
}
