package com.shenma.yueba.baijia.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.AttationListActivity;
import com.shenma.yueba.baijia.activity.BaiJiaOrderListActivity;
import com.shenma.yueba.baijia.activity.BuyerCertificationActivity1;
import com.shenma.yueba.baijia.activity.CircleListActivity;
import com.shenma.yueba.baijia.activity.MyCollectionActivity;
import com.shenma.yueba.baijia.activity.UserConfigActivity;
import com.shenma.yueba.baijia.dialog.WeChatDialog;
import com.shenma.yueba.baijia.modle.BaiJiaCheckBuyerStatusBean;
import com.shenma.yueba.baijia.modle.BaiJiaCheckBuyerStatusBeanRequest;
import com.shenma.yueba.baijia.modle.GetUserFlowStatusBackBean;
import com.shenma.yueba.baijia.modle.MyInfoBean;
import com.shenma.yueba.baijia.modle.RequestMyInfoBean;
import com.shenma.yueba.baijia.modle.UserFlowStatusBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.util.WXLoginUtil;
import com.shenma.yueba.yangjia.activity.MainActivityForYangJia;

import config.PerferneceConfig;

/**
 * 败家--个人账户
 * 
 * @author a
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MeFragmentForBaiJia extends BaseFragment implements OnClickListener {
	private View view;
	private ImageView iv_setting;
	private ImageView iv_icon;
	private TextView tv_nickname;
	private TextView tv_all_order;
	private TextView tv_waiting_for_send;
	private TextView tv_waiting_for_recieve;
	private TextView tv_pick_by_myself;
	private TextView tv_will_yangjia;//我要养家
	private TextView tv_my_collection;//我的收藏

	TextView tv_style;//区域

	TextView tv_all_order_count_textview;//总订单数
	TextView tv_waiting_for_send_count_textview;//待支付订单数
	TextView tv_waiting_for_recieve_count_textview;//专柜自提订单数
	TextView tv_pick_by_myself_count_textview;//售后订单数
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("CircleFragment", "oncreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("CircleFragment", "oncreateView");

		if (view == null) {
			initViews(inflater);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		//requestData();
		return view;
	}


	/**
	 * 初始化view
	 */
	private void initViews(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.me_fragment_for_baijia, null);

		//关注
		TextView tv_my_attention=(TextView)view.findViewById(R.id.tv_my_attention);
		tv_my_attention.setOnClickListener(this);

		tv_all_order_count_textview=(TextView)view.findViewById(R.id.tv_all_order_count_textview);
		tv_waiting_for_send_count_textview=(TextView)view.findViewById(R.id.tv_waiting_for_send_count_textview);
		tv_waiting_for_recieve_count_textview=(TextView)view.findViewById(R.id.tv_waiting_for_recieve_count_textview);
		tv_pick_by_myself_count_textview=(TextView)view.findViewById(R.id.tv_pick_by_myself_count_textview);
		
		
		TextView tv_my_circle=(TextView)view.findViewById(R.id.tv_my_circle);
		tv_my_circle.setOnClickListener(this);

		iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		iv_icon.setImageResource(R.drawable.default_pic);
		//买手名称
		tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
		tv_nickname.setText(ToolsUtil.nullToString(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_names)));
	
		//类型 养家模式
		tv_style = (TextView) view.findViewById(R.id.tv_style);
		String cityname=SharedUtil.getStringPerfernece(getActivity(), PerferneceConfig.CURRENT_CITY_NAME);
		String Districtname=SharedUtil.getStringPerfernece(getActivity(), PerferneceConfig.CURRENT_DISTRICT_NAME);
		tv_style.setText(ToolsUtil.nullToString(cityname)+"  "+ToolsUtil.nullToString(Districtname));

		tv_all_order = (TextView) view.findViewById(R.id.tv_all_order);
		tv_waiting_for_send = (TextView) view.findViewById(R.id.tv_waiting_for_send);
		tv_waiting_for_recieve = (TextView) view.findViewById(R.id.tv_waiting_for_recieve);
		tv_pick_by_myself = (TextView) view.findViewById(R.id.tv_pick_by_myself);
		tv_my_collection = (TextView) view.findViewById(R.id.tv_my_collection);
		tv_will_yangjia = (TextView) view.findViewById(R.id.tv_will_yangjia);
		iv_setting.setOnClickListener(this);
		tv_my_collection.setOnClickListener(this);
		tv_all_order.setOnClickListener(this);
		tv_waiting_for_send.setOnClickListener(this);
		tv_waiting_for_recieve.setOnClickListener(this);
		tv_pick_by_myself.setOnClickListener(this);
		tv_will_yangjia.setOnClickListener(this);
		/*FontManager.changeFonts(getActivity(), tv_nickname, tv_style,tv_attention_count,
				tv_attention_title,tv_fans_count,tv_fans_title,tv_collection_count,
				tv_collection_title,tv_all_order,tv_waiting_for_send,
				tv_waiting_for_recieve,tv_pick_by_myself,tv_will_yangjia,tv_my_collection);*/
	}

	@Override
	public void onResume() {
		Log.i("CircleFragment", "onResume");
		tv_nickname.setText(ToolsUtil.nullToString(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_names)));
		super.onResume();
		requestData();
	}

	@Override
	public void onClick(View v) {
		int userID=Integer.parseInt(SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id));
		switch (v.getId()) {
		case R.id.tv_my_circle://圈子
			Intent intent=new Intent(getActivity(),CircleListActivity.class);
			intent.putExtra("userID", userID);
			startActivity(intent);
			break;
		/*case R.id.me_fragment_for_baijie_layout_fans_linearlayout://粉丝
			Intent fansintent=new Intent(getActivity(),AttationListActivity.class);
			fansintent.putExtra("TYPE", AttationListActivity.TYPE_FANS);
			fansintent.putExtra("userID", userID);
			startActivity(fansintent);
			break;*/
		case R.id.tv_my_attention://关注
			Intent attentionintent=new Intent(getActivity(),AttationListActivity.class);
			attentionintent.putExtra("TYPE", AttationListActivity.TYPE_ATTATION);
			attentionintent.putExtra("userID", userID);
			startActivity(attentionintent);
			break;	
		case R.id.iv_setting://设置
			Intent userConfigIntent = new Intent(getActivity(),UserConfigActivity.class);
			startActivity(userConfigIntent);
			break;
		case R.id.tv_all_order://全部
			Intent intent1=new Intent(getActivity(),BaiJiaOrderListActivity.class);
			intent1.putExtra("CURRID", 0);
			startActivity(intent1);
			break;
		case R.id.tv_waiting_for_send://待付款
			Intent intent2=new Intent(getActivity(),BaiJiaOrderListActivity.class);
			intent2.putExtra("CURRID", 1);
			startActivity(intent2);
			break;
		case R.id.tv_waiting_for_recieve://专柜自提
			Intent intent3=new Intent(getActivity(),BaiJiaOrderListActivity.class);
			intent3.putExtra("CURRID", 2);
			startActivity(intent3);
			break;
		case R.id.tv_pick_by_myself://售后
			Intent intent4=new Intent(getActivity(),BaiJiaOrderListActivity.class);
			intent4.putExtra("CURRID", 3);
			startActivity(intent4);
			break;
		case R.id.tv_buyer://申请买手或者我是买手
			break;
		case R.id.tv_will_yangjia://我要养家
			getCheckBuyerStatus();
			break;
		case R.id.tv_my_collection://我的收藏
			Intent intent5 = new Intent(getActivity(),MyCollectionActivity.class);
			startActivity(intent5);
			break;
		default:
			break;
		}

	}
	
	/***
	 * 获取当前用户的认证状态
	 * **/
	void getCheckBuyerStatus()
	{
		HttpControl httpcontrol=new HttpControl();
		httpcontrol.getCheckBuyerStatus(true, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				if(obj==null || !(obj instanceof BaiJiaCheckBuyerStatusBeanRequest))
				{
					http_Fails(500, "获取用户权限失败 请重试");
				}else
				{
					BaiJiaCheckBuyerStatusBeanRequest request=(BaiJiaCheckBuyerStatusBeanRequest)obj;
					if(request.getData()==null)
					{
						http_Fails(500, "获取用户权限失败 请重试");
					}else
					{
						BaiJiaCheckBuyerStatusBean baiJiaCheckBuyerStatusBean=request.getData();
						forwardYangJia(baiJiaCheckBuyerStatusBean);
					}
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(getActivity(), msg);
			}
		}, getActivity());
	}
	
	
	
	
	void forwardYangJia(BaiJiaCheckBuyerStatusBean baiJiaCheckBuyerStatusBean)
	{
		//-1  不可以养家  0  可以提交资料    1  已经是买手 可以进入首页
		switch(baiJiaCheckBuyerStatusBean.getStatus())
		{
		case -1:
			MyApplication.getInstance().showMessage(getActivity(), ToolsUtil.nullToString(baiJiaCheckBuyerStatusBean.getMessage()));
			break;
		case 0:
			if(!SharedUtil.getBooleanPerfernece(getActivity(), SharedUtil.user_IsBindWeiXin)){
				DialogUtils dialog = new DialogUtils();
				dialog.alertDialog(getActivity(), "提示--申请认证买手第一步", "申请认证买手需绑定微信以便后期金钱交易，现在去绑定吗？", new DialogUtilInter() {
					@Override
					public void dialogCallBack(int... which) {
						MyApplication.getInstance().showMessage(getActivity(), "正在绑定微信");
						// 绑定手机号
						WXLoginUtil wxLoginUtil = new WXLoginUtil(getActivity());
						wxLoginUtil.initWeiChatLogin(false,false,false);
					}
				}, true, "绑定", "取消", false, true);
				return;
			}
			GetUserFlowStatus();
			break;
		case 1:
			MyApplication.getInstance().removeAllActivity();
			Intent intentyangjia=new Intent(getActivity(), MainActivityForYangJia.class);
			startActivity(intentyangjia);
			break;
		}
		/**
		 * -2表示还没有申请过认证买手  ，-1表示身亲被拒绝  ，0表示正在申请中，1表示申请通过
		 *//*
		if("0".equals(SharedUtil.getAuditStatus(getActivity()))){//正在审核，不可再次点击审核
			Toast.makeText(getActivity(), "申请审核中，请耐心等待...", Toast.LENGTH_LONG).show();
		}else if("-2".equals(SharedUtil.getAuditStatus(getActivity()))){//还未审核，可以进入
			Intent buyerCertificaitonIntent = new Intent(getActivity(),BuyerCertificationActivity1.class);
			startActivity(buyerCertificaitonIntent);
		}else if("-1".equals(SharedUtil.getAuditStatus(getActivity()))){//表示身亲被拒绝
			Toast.makeText(getActivity(), "您的审核已被拒绝，请三天后重试...", Toast.LENGTH_LONG).show();
		}else if("1".equals(SharedUtil.getAuditStatus(getActivity()))){//表示申请通过
			MyApplication.getInstance().removeAllActivity();
			Intent intentyangjia=new Intent(getActivity(), MainActivityForYangJia.class);
			startActivity(intentyangjia);
		}*/
	}
	
	
	
	
	
	
	/*****
	 * 设置数据
	 * ******/
	void setValue(MyInfoBean myInfoBean)
	{
		initPic(ToolsUtil.nullToString(myInfoBean.getLogo()), iv_icon);

		if(0 != myInfoBean.getAllOrderCount()){
			tv_all_order_count_textview.setVisibility(View.GONE);
		}else{
			tv_all_order_count_textview.setVisibility(View.GONE);
		}
		if(0 != myInfoBean.getWaitPaymentOrderCount()){
			tv_waiting_for_send_count_textview.setVisibility(View.VISIBLE);
		}else{
			tv_waiting_for_send_count_textview.setVisibility(View.GONE);
		}
		if(0 != myInfoBean.getPickedSelfOrderCount()){
			tv_waiting_for_recieve_count_textview.setVisibility(View.VISIBLE);
		}else{
			tv_waiting_for_recieve_count_textview.setVisibility(View.GONE);
		}
		if(0 != myInfoBean.getAfterSaleOrderCount()){
			tv_pick_by_myself_count_textview.setVisibility(View.VISIBLE);
		}else{
			tv_pick_by_myself_count_textview.setVisibility(View.GONE);
		}
		
		tv_all_order_count_textview.setText(ToolsUtil.setOrderCount(myInfoBean.getAllOrderCount()+""));
		tv_waiting_for_send_count_textview.setText(ToolsUtil.setOrderCount(myInfoBean.getWaitPaymentOrderCount()+""));
		tv_waiting_for_recieve_count_textview.setText(ToolsUtil.setOrderCount(myInfoBean.getPickedSelfOrderCount()+""));
		tv_pick_by_myself_count_textview.setText(ToolsUtil.setOrderCount(myInfoBean.getAfterSaleOrderCount()+""));
	}
	
	/***
	 * 请求数据
	 * **/
	void requestData()
	{
		if(!MyApplication.getInstance().isUserLogin(getActivity()))
		{
			return;
		}
		HttpControl httpcontrol=new HttpControl();
		httpcontrol.GetBaijiaMyInfo(false, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				if(obj==null || !(obj instanceof RequestMyInfoBean) || ((RequestMyInfoBean)obj).getData()==null)
				{
					return;
				}else
				{
					RequestMyInfoBean requestMyInfoBean=(RequestMyInfoBean)obj;
					setValue(requestMyInfoBean.getData());
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				
			}
		}, getActivity());
	}
	
	/****
	 * 加载图片
	 * */
	void initPic(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
	
	
	
	/**
	 * 关注微信账号
	 */
	private void GetUserFlowStatus() {
		final HttpControl httpcon = new HttpControl();
		httpcon.getUserFlowStatus(new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				GetUserFlowStatusBackBean bean = (GetUserFlowStatusBackBean) obj;
				UserFlowStatusBean data = bean.getData();
				if(data!=null){
					boolean isFlow = data.isIsFlow();
						if(isFlow){//已經关注
							Intent buyerCertificaitonIntent = new Intent(getActivity(),BuyerCertificationActivity1.class);
							startActivity(buyerCertificaitonIntent);
						}else{//没有关注
							
							WeChatDialog dialog = new WeChatDialog(getActivity(),data.getQRCode(),data.getName());
							dialog.show();
						}
					}
				};

			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(getActivity(), msg);
			}
		}, getActivity(), true);
	}
}
