package com.shenma.yueba.baijia.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/***
 * 所有的需要显示网页的界面
 * 
 * @author Administrator
 * 
 */
public class ThemeWebActivity extends BaseActivityWithTopView {
	private WebView webView;
	private String url, title, orderId;
	Map<String,Class> map=new ArrayMap<String,Class>();
	enum contentKey
	{
		type,
		id,
		key
	}

	enum ContenttypeValue
	{
		product,
		brand,
		buyer,
		store
	}

	String urlHead="app://";
	String urlContent="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_layout);
		super.onCreate(savedInstanceState);
		addMapData();
		getIntentData();
		initBaseView();
		initWebView();
	}


	void  addMapData()
	{
		map.put(ContenttypeValue.product.name(),BaijiaProductInfoActivity.class);
		map.put(ContenttypeValue.brand.name(),BrandListActivity.class);
		map.put(ContenttypeValue.buyer.name(),ShopMainActivity.class);
		map.put(ContenttypeValue.store.name(),MarketMainActivity.class);
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
				//判断URL类型 是否属于app
				if(judgeUrlType(url))
				{
					//根据 类型 跳转页面
					forwardActivityForType();
					return true;
				}else
				{
					view.loadUrl(url);
					webView.requestFocus();
					return true;
				}

			}

		});
		webView.addJavascriptInterface(new Object(){
			@JavascriptInterface
			private void clickOnAndroid() {
				Toast.makeText(ThemeWebActivity.this, "我接收到了", Toast.LENGTH_SHORT).show();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ThemeWebActivity.this.finish();
						Toast.makeText(ThemeWebActivity.this, "我接收到了", Toast.LENGTH_SHORT).show();
					}
				});

			}
		}, "demo");
	}


	void forwardActivityForType()
	{
		String typeValue=null;
		String idValue=null;
		String keyValue=null;
		if(urlContent!=null && urlContent.length()>0)
		{
			String[] str_array=urlContent.split("&");
			for(int i=0;i<str_array.length;i++)
			{
				if(str_array[i].indexOf(contentKey.type.name() + "=")==0)
				{
					typeValue=str_array[i].substring((contentKey.type.name()+"=").length());
				}else if(str_array[i].indexOf(contentKey.id.name()+"=")==0)
				{
					idValue=str_array[i].substring((contentKey.id.name()+"=").length());
				}else if(str_array[i].indexOf(contentKey.key.name()+"=")==0)
				{
					keyValue=str_array[i].substring((contentKey.key.name()+"=").length());
				}
			}
		}

		try {
			typeValue= URLDecoder.decode(ToolsUtil.nullToString(typeValue),"UTF-8");
			idValue= URLDecoder.decode(ToolsUtil.nullToString(idValue),"UTF-8");
			keyValue= URLDecoder.decode(ToolsUtil.nullToString(keyValue),"UTF-8");
			Log.i("TAG", "forwardActivityForType --->typeValue:" + typeValue + "  idValue:" + idValue + "  keyValue:" + keyValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//如果获取的 类型有值
		if(typeValue!=null && typeValue.length()>0)
		{
			if(map.get(typeValue)!=null)
			{
				Class c=map.get(typeValue);
				Intent intent=new Intent(ThemeWebActivity.this,c);
				if(typeValue.equals(ContenttypeValue.product.name()))
				{
					try {
						if (idValue != null) {
							int id = Integer.valueOf(idValue);
							intent.putExtra("productID", id);
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}

				}else if(typeValue.equals(ContenttypeValue.brand.name()))
				{
					try {
						if (idValue != null) {
							int id = Integer.valueOf(idValue);
							intent.putExtra("BrandId", id);
							intent.putExtra("BrandName",ToolsUtil.nullToString(keyValue));
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}				}
				else if(typeValue.equals(ContenttypeValue.buyer.name()))
				{
					try {
						if (idValue != null) {
							int id = Integer.valueOf(idValue);
							intent.putExtra("buyerId", id);
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}else if(typeValue.equals(ContenttypeValue.store.name()))
				{
					intent.putExtra("StoreId", ToolsUtil.nullToString(idValue));
				}
				startActivity(intent);
			}
		}


	}



	/*******
	 * 判断 url 是否是 app 开头
	 * *****/
	boolean judgeUrlType(String url)
	{
		if(url!=null && url.length()>0)
		{
			if(url.indexOf(urlHead)==0)
			{
				urlContent=url.substring(urlHead.length());
				return  true;
			}
		}
		return  false;
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
