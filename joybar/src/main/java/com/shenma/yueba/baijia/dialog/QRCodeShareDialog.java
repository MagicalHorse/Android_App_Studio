package com.shenma.yueba.baijia.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.Base64Coder;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ShareUtil;
import com.shenma.yueba.util.ShareUtil.ShareListener;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;
import com.shenma.yueba.yangjia.modle.kaixiaoPiaoBean;

/**
 * @author gyj
 * @version 创建时间：2015-5-26 下午3:01:52 程序的简单说明 圈子二维码 分享对话框
 */

public class QRCodeShareDialog extends Dialog implements
        android.view.View.OnClickListener {
    Context context;
    RelativeLayout ll;
    ImageView qzcodeshare_layouyt_close_imageview;
    TextView qzcodeshare_layouyt_name_textview;
    TextView tv_share;
    kaixiaoPiaoBean obj;
    private TextView qzcodeshare_layouyt_count_textview;
    private ImageView qzcodeshare_layout_content_imageview;
    private Bitmap bitmap;

    public QRCodeShareDialog(Context context, kaixiaoPiaoBean obj) {
        super(context, R.style.MyDialog);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.context = context;
        this.obj = obj;
        setOwnerActivity((Activity) context);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        // this.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.color_transparent));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ll = (RelativeLayout) RelativeLayout.inflate(context,
                R.layout.qzcodeshare_layout, null);
        setContentView(ll);
        initView();
    }

    void initView() {
        RoundImageView qzcodeshare_layouyt_customview = (RoundImageView) ll
                .findViewById(R.id.qzcodeshare_layouyt_customview);
        initBitmap(SharedUtil.getHeadImage(getContext()),
                qzcodeshare_layouyt_customview);
        qzcodeshare_layouyt_close_imageview = (ImageView) ll
                .findViewById(R.id.qzcodeshare_layouyt_close_imageview);
        qzcodeshare_layout_content_imageview = (ImageView) ll
                .findViewById(R.id.qzcodeshare_layout_content_imageview);
        qzcodeshare_layouyt_name_textview = (TextView) ll
                .findViewById(R.id.qzcodeshare_layouyt_name_textview);
        qzcodeshare_layouyt_count_textview = (TextView) ll
                .findViewById(R.id.qzcodeshare_layouyt_count_textview);
        tv_share = (TextView) ll.findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);
        qzcodeshare_layouyt_close_imageview.setOnClickListener(this);
        qzcodeshare_layouyt_name_textview.setText(obj.getProductName());
            qzcodeshare_layouyt_count_textview.setText("￥" + ToolsUtil.DounbleToString_2(obj.getAmount()));
        byte[] bytes = Base64Coder.decode(obj.getQrCode());
        LayoutParams params = qzcodeshare_layout_content_imageview.getLayoutParams();
        params.width = ToolsUtil.getDisplayWidth(context) / 4 * 3;
        params.height = ToolsUtil.getDisplayWidth(context) / 4 * 3;
        qzcodeshare_layout_content_imageview.setLayoutParams(params);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        qzcodeshare_layout_content_imageview.setImageBitmap(bitmap);
        FontManager.changeFonts(context, qzcodeshare_layouyt_name_textview,
                qzcodeshare_layouyt_count_textview, tv_share);
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qzcodeshare_layouyt_close_imageview:
                this.cancel();
                break;
            case R.id.tv_share:// 分享给好友
                this.cancel();
                ShareUtil.shareAll2((Activity) context, "扫码商品分享", "亲，帮我代付下呗", "", bitmap, new ShareListener() {
                    @Override
                    public void sharedListener_sucess() {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void sharedListener_Fails(String msg) {
                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    void initBitmap(final String url, final ImageView iv) {
        if (iv != null) {
            MyApplication.getInstance().getBitmapUtil().display(iv, ToolsUtil.nullToString(url));
        }

    }
}
