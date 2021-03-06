/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.shenma.yueba.view.imageshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.shenma.yueba.application.MyApplication;

public class UrlTouchImageView extends RelativeLayout{
	private static final String TAG="UrlTouchImageView";
	protected ProgressBar mProgressBar;
	protected TouchImageView mImageView;
	protected Context mContext;
	private Handler handler = new Handler();
	public UrlTouchImageView(Context ctx) {
		super(ctx);
		mContext = ctx;
		init();

	}
	public UrlTouchImageView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mContext = ctx;
		init();
	}
	public TouchImageView getImageView() {
		return mImageView;
	}
	protected void init() {
		mImageView = new TouchImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mImageView.setLayoutParams(params);
		mImageView.setScaleType(ScaleType.MATRIX);
		this.addView(mImageView);
		
		mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleInverse);
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(30, 0, 30, 0);
		mProgressBar.setLayoutParams(params);
		mProgressBar.setIndeterminate(false);
		mProgressBar.setMax(100);
		mProgressBar.setVisibility(View.GONE);
		this.addView(mProgressBar);
	}

	public void setUrl(final String imageUrl) {
//		new Thread() {
//			public void run() {
//				final Bitmap bitmap = ImageManager
//						.instantiate()
//						.getBitmap(
//								imageUrl,
//								System.currentTimeMillis()
//										+ "", 600f);
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						if (mImageView != null) {
//							if (bitmap != null) {
//								mImageView.setBackgroundDrawable(new BitmapDrawable(
//										bitmap));
//							} else {
//								mImageView.setBackgroundDrawable(getResources()
//										.getDrawable(R.drawable.default_img));
//							}
//						}
//					}
//				});
////				runOnUiThread(new Runnable() {
////					@Override
////					public void run() {
////						if (bitmap != null) {
////							imageView
////									.setImageBitmap(bitmap);
////						} else {
////							imageView
////									.setImageResource(R.drawable.default_pic);
////						}
////					}
////				});
//
//			};
//		}.start();
		com.nostra13.universalimageloader.core.ImageLoader loader = MyApplication.getInstance().getImageLoader();
		loader.displayImage(imageUrl, mImageView,MyApplication.getInstance().getDisplayImageOptions(), new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
			}
			@Override
			public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
				mProgressBar.setVisibility(View.GONE);
			}
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
	}
}
