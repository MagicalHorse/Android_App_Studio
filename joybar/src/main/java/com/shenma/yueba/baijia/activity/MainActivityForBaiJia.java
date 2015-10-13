package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.UpdateManager;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.CircleFragment;
import com.shenma.yueba.baijia.fragment.FindFragment;
import com.shenma.yueba.baijia.fragment.IndexFragmentForBaiJia;
import com.shenma.yueba.baijia.fragment.MeFragmentForBaiJia;
import com.shenma.yueba.baijia.fragment.MessageFragment;
import com.shenma.yueba.baijia.modle.CheckVersionBackBean;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.inter.LocationBackListner;
import com.shenma.yueba.util.AlartMangerUtil;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.LocationUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import im.broadcast.ImBroadcastReceiver;
import im.broadcast.ImBroadcastReceiver.ImBroadcastReceiverLinstener;
import im.broadcast.ImBroadcastReceiver.RECEIVER_type;
import im.form.RequestMessageBean;

/******
 * 败家主页  包含败家主页面的 布局 以及 加载或替换 fragment 来更新显示内容
 * *******/
public class MainActivityForBaiJia extends FragmentActivity implements ImBroadcastReceiverLinstener{
	FrameLayout baijia_main_framelayout;
	private long exitTime = 0;// 初始化退出时间，用于两次点击返回退出程序
	LinearLayout baijia_main_foot_linearlayout;
	List<FragmentBean> fragment_list = new ArrayList<FragmentBean>();
	List<View> footer_list = new ArrayList<View>();
	// 当前选中的id
	int currid = -1;
	Fragment indexFragmentForBaiJia, circleFragment, messageFragment,
			findFragment, meFragmentForBaiJia;
	FragmentManager fragmentManager;
	ImBroadcastReceiver imBroadcastReceiver;
	boolean isbroadcase=false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.baijia_main_layout);
		MyApplication.getInstance().addActivity(this);
		AlartMangerUtil.startHeartAlart(MainActivityForBaiJia.this);
		imBroadcastReceiver=new ImBroadcastReceiver(this);
		initView();
		initaddFooterView();
		setCurrView(0);
		checkVersion();
//		Toast.makeText(this, ""+SocializeConstants.SDK_VERSION, 1000).show();
		registerBroadcase();
	}

	
	private void checkVersion() {
		HttpControl httpControl = new HttpControl();
		httpControl.checkVersion(new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				CheckVersionBackBean bean = (CheckVersionBackBean) obj;
				if(bean.getData()!=null){
					String versionRemote = bean.getData().getVersion();
					String localVersionStr = ToolsUtil.getVersionName(MainActivityForBaiJia.this);
					//float localVersonFloat = Float.valueOf(localVersionStr);
					if(!versionRemote.equals(localVersionStr)){
						UpdateManager manager = new UpdateManager(MainActivityForBaiJia.this, versionRemote+"", bean.getData().getUrl(), bean.getData().getTitle(), bean.getData().getDetails());
						manager.startUpdate();
					}
				}
				
				

			}

			@Override
			public void http_Fails(int error, String msg) {
				
			}
		}, MainActivityForBaiJia.this);

	}

	void initView() {
		fragmentManager = getSupportFragmentManager();
		baijia_main_framelayout = (FrameLayout) findViewById(R.id.baijia_main_framelayout);
		baijia_main_foot_linearlayout = (LinearLayout) findViewById(R.id.baijia_main_foot_linearlayout);
		indexFragmentForBaiJia = new IndexFragmentForBaiJia();
		circleFragment = new CircleFragment();
		messageFragment = new MessageFragment();
		findFragment = new FindFragment();
		meFragmentForBaiJia = new MeFragmentForBaiJia();

		fragment_list.add(new FragmentBean("主页",
				R.drawable.baijia_home_background, indexFragmentForBaiJia));
		fragment_list.add(new FragmentBean("圈子",
				R.drawable.baijia_circle_background, circleFragment));
		fragment_list.add(new FragmentBean("消息",
				R.drawable.baijia_msg_background, messageFragment));
		fragment_list.add(new FragmentBean("发现",
				R.drawable.baijia_find_background, findFragment));
		fragment_list.add(new FragmentBean("我",
				R.drawable.baijia_setting_background, meFragmentForBaiJia));

	}

	void initaddFooterView() {
		for (int i = 0; i < fragment_list.size(); i++) {
			LinearLayout ll = (LinearLayout) LinearLayout.inflate(this,
					R.layout.tab_image_textview_layout, null);
			ImageView imageview = (ImageView) ll.findViewById(R.id.imageview);
			imageview.setImageResource(fragment_list.get(i).getIcon());
			TextView tv1 = (TextView) ll.findViewById(R.id.tv1);
			tv1.setVisibility(View.GONE);
			/*
			 * tv1.setText(fragment_list.get(i).getName()); tv1.setTextSize(12);
			 * FontManager.changeFonts(getApplication(), tv1);
			 */
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			param.weight = 1;
			baijia_main_foot_linearlayout.addView(ll, param);
			footer_list.add(ll);
			ll.setTag(i);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int i = (Integer) v.getTag();
					setCurrView(i);
				}
			});
		}
	}

	synchronized void setCurrView(int i) {
		switch (i) {
		case 0:

			break;
		case 1:
			if (!MyApplication.getInstance().isUserLogin(
					MainActivityForBaiJia.this)) {
				return;
			}
			break;
		case 2:
			if (!MyApplication.getInstance().isUserLogin(
					MainActivityForBaiJia.this)) {
				return;
			}
			break;
		case 4:
			if (!MyApplication.getInstance().isUserLogin(
					MainActivityForBaiJia.this)) {
				return;
			}
			break;
		default:

			break;
		}
		setRedView(i,false);//清除红点
		if (currid == -1 && i == 0) {
			if(!(((Fragment)fragment_list.get(i).getFragment()).isAdded()))
			{
			   fragmentManager.beginTransaction().add(R.id.baijia_main_framelayout,(Fragment)fragment_list.get(i).getFragment()).commit();
			}
		} else if (currid == i) {
			return;
		}else{
			if(!(((Fragment)fragment_list.get(i).getFragment()).isAdded()))
			{
				fragmentManager.beginTransaction().replace(R.id.baijia_main_framelayout,(Fragment) fragment_list.get(i).getFragment()).commit();
			}
		}
		currid = i;
		setTextColor(i);
	}

	void setTextColor(int value) {
		for (int i = 0; i < footer_list.size(); i++) {
			LinearLayout ll = (LinearLayout) footer_list.get(i);
			ImageView iv = (ImageView) ll.findViewById(R.id.imageview);
			// TextView tv=(TextView)ll.findViewById(R.id.tv1);
			if (i == value) {
				// tv.setTextColor(this.getResources().getColor(R.color.color_deeoyellow));
				iv.setSelected(true);

			} else {
				// tv.setTextColor(this.getResources().getColor(R.color.black));
				iv.setSelected(false);
			}

		}
	}

	public void onResume() {
		/*if(fragmentManager!=null)
		{
			if(currid>=0)
			{
				if(!(((Fragment)fragment_list.get(currid).getFragment()).isAdded()))
				{
				   fragmentManager.beginTransaction().add(R.id.baijia_main_framelayout,(Fragment)fragment_list.get(currid).getFragment()).commitAllowingStateLoss();
				}
			}
		}*/
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		
		super.onPause();
		MobclickAgent.onPause(this);
	}

	
	@Override
	protected void onStop() {
		/*if(fragmentManager!=null)
		{
			if(currid>=0)
			{
				if((((Fragment)fragment_list.get(currid).getFragment()).isAdded()))
				{
					fragmentManager.beginTransaction().remove((Fragment)fragment_list.get(currid).getFragment()).commitAllowingStateLoss();
				}
			}
			
			
		}*/
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		MyApplication.getInstance().removeActivity(this);// 加入回退栈
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				exitTime = System.currentTimeMillis();
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
			} else {
				MyApplication.getInstance().exit();
				
			}
			return true; // 返回true表示执行结束不需继续执行父类按键响应
		}
		return super.onKeyDown(keyCode, event);
	}


	/******
	 * 注册 消息广播监听
	 * ***/
	void registerBroadcase()
	{
		if(!isbroadcase)
		{
			isbroadcase=true;
			MainActivityForBaiJia.this.registerReceiver(imBroadcastReceiver, new IntentFilter(ImBroadcastReceiver.IntentFilterRoomMsg));
		}
		
	}
	
	
	/******
	 * 注册 消息广播监听
	 * ***/
	void unregisterBroadcase()
	{
		if(isbroadcase)
		{
			MainActivityForBaiJia.this.unregisterReceiver(imBroadcastReceiver);
			isbroadcase=false;
		}
		
	}


	@Override
	public void newMessage(Object obj) {
		
		
	}


	@Override
	public void roomMessage(Object obj) {
	   if(obj!=null && obj instanceof RequestMessageBean)
	   {
		   RequestMessageBean bean=(RequestMessageBean)obj;
		   int touserid=bean.getToUserId();
		   if(touserid<=0)//群聊信息
		   {
			   if(currid!=1)
			   {
				 //设置显示原点
				   setRedView(1, true);
			   }
			   
		   }else//私聊信息
		   {
			   if(currid!=2)
			   {
				   setRedView(2, true);
			   }
		   }
	   }
	}
	
	/***
	 * 设置 红色的按钮显示或隐藏
	 * @param i int 需要控制的 item 的 下标
	 * @param b  boolean 是否显示 true显示  false否
	 * **/
	void setRedView(int i,boolean b)
	{
	   if(i>-1 && i<footer_list.size())
	   {
		   View view=footer_list.get(i).findViewById(R.id.round_view);
		   if(view!=null)
		   {
			  if(b)
			  {
				  view.setVisibility(View.VISIBLE);
			  }else
			  {
				  view.setVisibility(View.GONE);
			  }
		   }
	   }
		
	}

	@Override
	public void clearMsgNotation(RECEIVER_type type) {
		switch(type)
		{
		case circle:
			setRedView(1, false);
			break;
		case msg:
			setRedView(2, false);
			break;
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//super.onSaveInstanceState(outState);//禁止应用保持fragment 的数据
	}
	
}
