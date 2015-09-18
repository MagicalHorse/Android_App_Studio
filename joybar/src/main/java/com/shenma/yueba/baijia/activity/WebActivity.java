package com.shenma.yueba.baijia.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.FontManager;
import com.umeng.analytics.MobclickAgent;

/***
 * 所有的需要显示网页的界面
 * 
 * @author Administrator
 * 
 */
public class WebActivity extends BaseActivityWithTopView {
	private WebView webView;
	private String url, title, orderId;
	private String client = "A1";
//	private String client = "CLIENT001";
	private String bindforward = "payment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_layout);
		super.onCreate(savedInstanceState);
		getIntentData();
		initBaseView();
		initWebView();
	}
	
	/**
	 * 获取传过来的数据
	 */
	private void getIntentData() {
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		orderId = getIntent().getStringExtra("orderId");
	}

	/**
	 * 初始化标题
	 */
	private void initBaseView() {
		setLeftTextView("", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		if (TextUtils.isEmpty(title)) {
			setTitle("浏览");
		} else {
			setTitle(title);
		}
	}

	/**
	 * 初始化webView
	 */
	@SuppressLint("JavascriptInterface")
	private void initWebView() {
		webView = getView(R.id.web_view);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 设置滚动条样式
		webView.setVerticalScrollBarEnabled(false);
		WebSettings webSettings = webView.getSettings();
		webSettings.setTextSize(WebSettings.TextSize.NORMAL);
		// webSettings.setSupportMultipleWindows(true);
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setPluginState(PluginState.ON);
		// webSettings.setPluginsEnabled(true);// 可以使用插件
		// webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// webSettings.setAllowFileAccess(true);
		// webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setLoadWithOverviewMode(true);
		// webSettings.setUseWideViewPort(true);
		webView.setWebChromeClient(new WebChromeClient());
		if (url == null || "".equals(url)) {
			return;
		}
		webView.loadUrl(url);
		webView.requestFocus();
		// 设置webkit参数
		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				webView.requestFocus();
				return true;
			}

		});
		webView.addJavascriptInterface(new Object(){
			@JavascriptInterface
			private void clickOnAndroid() {
				Toast.makeText(WebActivity.this, "我接收到了", Toast.LENGTH_SHORT).show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						WebActivity.this.finish();
						Toast.makeText(WebActivity.this, "我接收到了", Toast.LENGTH_SHORT).show();
					}
				});

			}
		}, "demo");
	}

	
	 public class WebChromeClient extends android.webkit.WebChromeClient {
	        @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	            if (newProgress == 100) {
	            	dismissDialog();
	            }else {
	            	showBottomDialog();
	            }
	            super.onProgressChanged(view, newProgress);
	        }

	    }

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	  @Override
	    protected void onDestroy() {
	    	MyApplication.getInstance().removeActivity(this);//移除activity对象
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
