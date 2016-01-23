package com.shenma.yueba.yangjia.activity;

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.RequestUploadProductDataBean;
import com.shenma.yueba.camera2.ActivityCapture;
import com.shenma.yueba.util.FileUtils;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.SelecteKXPOrPublishType;
import com.shenma.yueba.yangjia.fragment.CircleFragment;
import com.shenma.yueba.yangjia.fragment.IndexFragmentForYangJia;
import com.shenma.yueba.yangjia.fragment.MeFragmentForYangJia;
import com.shenma.yueba.yangjia.fragment.MessageFragmentForYangJia;
import com.shenma.yueba.yangjia.fragment.TaskRewardFragment;
import com.umeng.analytics.MobclickAgent;

import im.control.SocketObserverManager;
import im.form.RequestMessageBean;


/**
 * 养家模式
 * @author a
 *
 */
public final class MainActivityForYangJia extends FragmentActivity implements SocketObserverManager.SocketNoticationListener{
	private long exitTime = 0;// 初始化退出时间，用于两次点击返回退出程序
	private FragmentTabHost mTabHost;
//	// 定义数组来存放按钮图片
	private int mImageViewArray[] = { R.drawable.tab_index_selector,
			R.drawable.tab_reward_selector, R.drawable.tab_publish_selector,
			R.drawable.tab_msg_selector, R.drawable.tab_me_selector };
	// Tab选项卡的文字
		private String mTextviewArray[] = { "主页", "红榜", "发布", "消息","我" };
	// 定义数组来存放Fragment界面
	private Class fragmentArray[] = { IndexFragmentForYangJia.class,TaskRewardFragment.class,CircleFragment.class,MessageFragmentForYangJia.class,MeFragmentForYangJia.class};
	View round_view;//消息的原点
	boolean isbroadcase=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		SocketObserverManager.getInstance().addSocketObserver(this);
//		CustomProgressDialog progressDialog = CustomProgressDialog.createDialog(this);
//		progressDialog.setMessage("正在加载中...");
//		progressDialog.show();

}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		TextView yangjia_msg_textview=(TextView)findViewById(R.id.yangjia_msg_textview);
		round_view=findViewById(R.id.round_view);
//		yangjia_msg_textview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				setRedView(false);
//			}
//		});
		TextView tv_center = (TextView) findViewById(R.id.tv_center);
		tv_center.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){//当手抬起的时候出发事件
//					showDialog();
					showBottomDialog();
				}
				return true;
			}
		});
		
		tv_center.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivityForYangJia.this, "click", 1000).show();
			}
		});
		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		mTabHost.getTabWidget().setDividerDrawable(null);  //去掉tab之间的竖线
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals("消息"))
				{
					setRedView(false);
				}
			}
		});
	}
	
/**
 * 给Tab按钮设置图标和文字
 */
private View getTabItemView(int index) {
	View view = View.inflate(this,R.layout.tab_item_view, null);
	ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
	imageView.setImageResource(mImageViewArray[index]);
//	TextView textView = (TextView) view.findViewById(R.id.textview);
//	textView.setText(mTextviewArray[index]);
//	FontManager.changeFonts(getApplicationContext(), textView);
	return view;
}




@Override
protected void onDestroy() {
	MyApplication.getInstance().removeActivity(this);
	SocketObserverManager.getInstance().removeSocketObserver(this);
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
		
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					exitTime = System.currentTimeMillis();
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
				} else {
					SocketObserverManager.getInstance().clearSocketObserver();
					MyApplication.getInstance().exit();
				}
				return true; // 返回true表示执行结束不需继续执行父类按键响应
			}
			return super.onKeyDown(keyCode, event);
		}

		
		
		/**
		 * 弹出选择框(开小票和发布商品)
		 */
		protected void showBottomDialog() {
			ToolsUtil.hideSoftInputKeyBoard(MainActivityForYangJia.this);
			ShowMenu showMenu = new ShowMenu(MainActivityForYangJia.this, findViewById(R.id.parent),
					R.layout.main_popwindow);
			showMenu.createView();
		}

	@Override
	public void socketConnectSucess() {

	}

	@Override
	public void socketConnectFails() {

	}

	@Override
	public void receiveMsgFromRoom(RequestMessageBean bean) {
		if(!mTabHost.getCurrentTabTag().equals("消息"))
		{
			setRedView(true);
		}
	}

	@Override
	public void receiveMsgFromUnRoom(RequestMessageBean bean) {
		if(!mTabHost.getCurrentTabTag().equals("消息"))
		{
			setRedView(true);
		}
	}

	@Override
	public void sendStatusChaneg(Object obj) {

	}

	/**
		 * 弹出底部菜单
		 * 
		 * @author
		 */
		class ShowMenu extends SelecteKXPOrPublishType {
			public ShowMenu(Activity activity, View parent, int popLayout) {
				super(activity, parent, popLayout);
			}

			@Override
			public void onExitClick(View v) {
				canceView();
			}

			@Override
			public void onPublish(View v) {
				MyApplication.getInstance().getPublishUtil().setBean(new RequestUploadProductDataBean());
				MyApplication.getInstance().getPublishUtil().setIndex("0");
				MyApplication.getInstance().getPublishUtil().getTagCacheList().clear();
				FileUtils.delAllFile(FileUtils.getRootPath() + "/tagPic/");
				Intent intentCamera = new Intent(MainActivityForYangJia.this,ActivityCapture.class);
				startActivity(intentCamera);
				canceView();
			}

			@Override
			public void onKxp(View v) {
				// 跳转到开下票的界面
				Intent intentXP = new Intent(MainActivityForYangJia.this,KaiXiaoPiaoActivity.class);
				startActivity(intentXP);
				canceView();
			}

		}

		/***
		 * 设置 红色的按钮显示或隐藏
		 * @param b  boolean 是否显示 true显示  false否
		 * **/
		void setRedView(boolean b)
		{
			if(round_view!=null)
			   {
				  if(b)
				  {
					  round_view.setVisibility(View.VISIBLE);
				  }else
				  {
					  round_view.setVisibility(View.GONE);
				  }
			   }
		}

}
