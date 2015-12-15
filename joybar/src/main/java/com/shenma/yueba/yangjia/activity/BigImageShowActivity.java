package com.shenma.yueba.yangjia.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.util.Base64Coder;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 显示大图的activity
 * @author a
 *
 */
public class BigImageShowActivity extends BaseActivityWithTopView {
	private String imageUrl;
	private String title;
	private TextView tv_content;
	private ImageView iv_big;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);//加入回退栈
		setContentView(R.layout.big_image_show);
		super.onCreate(savedInstanceState);
		getIntentData();
		initView();
	}

	private void getIntentData() {
		imageUrl = getIntent().getStringExtra("imageUrl");
		title = getIntent().getStringExtra("title");
	}

	private void initView() {
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BigImageShowActivity.this.finish();
			}
		});
		tv_content = getView(R.id.tv_content);
		tv_content.setText(ToolsUtil.nullToString(title)+"的二维码店铺");
		iv_big = getView(R.id.iv_big);
		LayoutParams params = iv_big.getLayoutParams();
		params.height = ToolsUtil.getDisplayWidth(mContext)-ToolsUtil.dip2px(mContext, 20);
		params.width = ToolsUtil.getDisplayWidth(mContext)-ToolsUtil.dip2px(mContext, 20);
		iv_big.setLayoutParams(params);
		setTitle("首页");
		byte[] bytes = Base64Coder.decode(imageUrl);
		iv_big.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
		FontManager.changeFonts(mContext, tv_content,tv_top_title);
		
	}
	
	
	  @Override
	    protected void onDestroy() {
	    	MyApplication.getInstance().removeActivity(this);//加入回退栈
	    	super.onDestroy();
	    }
	  public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			}
			public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			}
}
