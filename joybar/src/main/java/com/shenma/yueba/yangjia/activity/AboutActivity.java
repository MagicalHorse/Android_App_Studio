package com.shenma.yueba.yangjia.activity;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.constants.HttpConstants;
import com.shenma.yueba.util.CustomProgressDialog;
import com.shenma.yueba.util.FileUtils;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.ParserJson;
import com.shenma.yueba.util.PhotoUtils;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.SelectePhotoType;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于我们
 * 
 * @author a
 * 
 */
public class AboutActivity extends BaseActivityWithTopView {

    @Bind(R.id.tv_version) TextView tv_version;
	@Bind(R.id.tv_content)  TextView tv_content;
	@Bind(R.id.tv_right)  TextView tv_right;
	@Bind(R.id.tv_company_name)  TextView tv_company_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_layout);
		ButterKnife.bind(this);
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setTitle("关于我们");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
		tv_version.setText("版本 "+ToolsUtil.getVersionName(mContext));
		FontManager.changeFonts(mContext, tv_version, tv_content, tv_right,tv_company_name,tv_top_title);
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
