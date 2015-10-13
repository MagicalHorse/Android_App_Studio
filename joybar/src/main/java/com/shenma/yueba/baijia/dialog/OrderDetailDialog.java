package com.shenma.yueba.baijia.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zkk
 * @version 创建时间：2015-8-04-11:01
 */

public class OrderDetailDialog extends Dialog implements
		View.OnClickListener, OnLongClickListener {
	Context context;
	RelativeLayout ll;
	String qrCodePath;
	String title;
	private Bitmap bitmap;
	public OrderDetailDialog(Context context, String qrCodePath, String title) {
		super(context, R.style.MyDialog);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		this.context = context;
		this.qrCodePath = qrCodePath;
		this.title = title;
		setOwnerActivity((Activity) context);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		// this.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.color_transparent));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (RelativeLayout) RelativeLayout.inflate(context,
				R.layout.yukuan_and_tihuo_qzcode, null);
		setContentView(ll);
		initView();
	}

	void initView() {
		ImageView iv_code = (ImageView) ll.findViewById(R.id.iv_code);
		iv_code.setOnLongClickListener(this);
		TextView tv_title = (TextView) ll.findViewById(R.id.tv_title);
		TextView tv_money = (TextView) ll.findViewById(R.id.tv_money);
		TextView tv_tishi = (TextView) ll.findViewById(R.id.tv_tishi);
		ImageView iv_close = (ImageView) ll.findViewById(R.id.iv_close);
		iv_close.setOnClickListener(this);
		LayoutParams params = iv_code.getLayoutParams();
		params.width = ToolsUtil.getDisplayWidth(context) / 4 * 3;
		params.height = ToolsUtil.getDisplayWidth(context) / 4 * 3;
		iv_code.setLayoutParams(params);
		MyApplication.getInstance().getImageLoader().displayImage(qrCodePath, iv_code, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				bitmap = arg2;
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		FontManager.changeFonts(context, tv_money, tv_tishi,tv_title);
	}

	@Override
	public void show() {

		super.show();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_close:
			this.cancel();
			break;
		}
	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}

	
	
	
	
	
	/**
	 * 保存图片到相册
	 */
		public static void saveBitmapToAlbum(Context context, Bitmap bmp) {
		    // 首先保存图片
		    File appDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_WENJIANJIA);
		    if (!appDir.exists()) {
		        appDir.mkdirs();
		    }
		    String fileName = "买手" + ".jpg";
		    File file = new File(appDir, fileName);
		    if(file!=null && file.exists()){
		    	file.delete();
		    }
		    try {
		        FileOutputStream fos = new FileOutputStream(file);
		        bmp.compress(CompressFormat.JPEG, 100, fos);
		        fos.flush();
		        fos.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
			}
		    
		    // 其次把文件插入到系统图库
		    try {
		        MediaStore.Images.Media.insertImage(context.getContentResolver(),
						file.getAbsolutePath(), fileName, null);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		    // 最后通知图库更新
		    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir+fileName)));
		    Toast.makeText(context, "图片已经保存至相册", 1000).show();
	}

	@Override
	public boolean onLongClick(View v) {
		if(bitmap!=null){
			saveBitmapToAlbum(context, bitmap);
		}
		return false;
	}
	
}
