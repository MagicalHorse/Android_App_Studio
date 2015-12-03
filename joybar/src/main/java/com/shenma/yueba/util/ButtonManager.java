package com.shenma.yueba.util;

import java.util.ArrayList;
import java.util.List;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ApplyForRefundActivity;
import com.shenma.yueba.baijia.activity.BaijiaAppealLoadingActivity;
import com.shenma.yueba.baijia.activity.BaijiaPayActivity;
import com.shenma.yueba.baijia.modle.BaiJiaOrderListInfo;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;
import com.shenma.yueba.broadcaseReceiver.OrderBroadcaseReceiver;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**  
 * @author gyj  
 * @version 创建时间：2015-6-24 下午4:01:26  
 * 程序的简单说明  
 */

public class ButtonManager {
public final static String WAITPAY="付款";//0
public final static String CANCELPAY="撤销退款";//1
public final static String QUERENTIHUO="确认提货";//2
public final static String SHENQINGTUIKUAN="申请退款";//3
public final static String SHENSU="申诉";//4
public final static String SHENSULOADING="申诉中";//5
public final static String CANCELORDER="取消订单";//6
	
	/***
	 * 根据订单状态返回操作按钮
	 * **/
	public static List<View> getButton(Activity activity,int type)
	{
		List<View> view_list=new ArrayList<View>();
		switch(type)
		{
		case 0://代付款
			view_list.add(createWaitPayButton(activity, 0));//付款按钮
			view_list.add(createWaitPayButton(activity, 6));//取消订单
			break;
		case 1://申请退款 确认提货
			view_list.add(createWaitPayButton(activity, 3));//申请退款
			view_list.add(createWaitPayButton(activity, 2));// 确认提货
			break;
		case 15://确认提货  申请退款
		case 16:
			//view_list.add(createWaitPayButton(activity, 2));//确认提货
			view_list.add(createWaitPayButton(activity, 3));//申请退款
			break;
		//申请退款
		case 3:
			view_list.add(createWaitPayButton(activity, 1));//撤销退款
			//撤销退款
			break;
		}
		
		return view_list;
		
		
	}
	
	/***
	 * 创建操作按钮
	 * **/
	static View createWaitPayButton(Activity activity,int type)
	{
		CustonOnClickListener custonOnClickListener=new CustonOnClickListener(activity);
		View v=activity.getLayoutInflater().inflate(R.layout.button_layout, null);
		Button btn=(Button)v.findViewById(R.id.baijia_orderdetails_sqtk_button);
		String str="";
		int back_resource_id=-1;
		switch(type)
		{
		case 0:
			str=WAITPAY;
			back_resource_id=R.drawable.yeollow_background;
			break;
		case 1:
			str=CANCELPAY;
			back_resource_id=R.drawable.applyrefund_background;
			break;
		case 2:
			str=QUERENTIHUO;
			back_resource_id=R.drawable.yeollow_background;
			break;
		case 3:
			str=SHENQINGTUIKUAN;
			back_resource_id=R.drawable.applyrefund_background;
			break;
		case 4:
			str=SHENSU;
			back_resource_id=R.drawable.back_background;
			break;
		case 5:
			str=SHENSULOADING;
			back_resource_id=R.drawable.back_background;
			break;
		case 6:
			str=CANCELORDER;
			back_resource_id=R.drawable.back_background;
			
		}
		btn.setText(str);
		btn.setBackgroundResource(back_resource_id);
		btn.setOnClickListener(custonOnClickListener);
		return v;
	}
	
	
	static class CustonOnClickListener implements OnClickListener
	{
		Activity activity;
		CustonOnClickListener(Activity activity)
		{
			this.activity=activity;
		}
		@Override
		public void onClick(View v) {
			buttonControl(v,activity);
		}
	}

	
	
	/****
	 * 按钮控制
	 * ***/
	static void buttonControl(View btn,Activity activity)
	{
		if(btn==null || btn.getTag()==null || !(btn.getTag() instanceof BaiJiaOrderListInfo))
        {
        	return;
        }
		BaiJiaOrderListInfo baiJiaOrderListInfo=(BaiJiaOrderListInfo)btn.getTag();
		String str=((Button)btn).getText().toString();
		if(str.equals(ButtonManager.WAITPAY))//如果是等待支付
		{
			ButtonManager.payOrder(activity, baiJiaOrderListInfo);
		}else if(str.equals(ButtonManager.CANCELPAY))//撤销退货
		{
			showDiaglog(activity, str, baiJiaOrderListInfo);
		}else if(str.equals(ButtonManager.QUERENTIHUO))//确认提货
		{
			showDiaglog(activity,str,baiJiaOrderListInfo);
			
		}else if(str.equals(ButtonManager.SHENQINGTUIKUAN))//申请退款
		{
			ButtonManager.applyforRefund(activity, baiJiaOrderListInfo);
		}else if(str.equals(ButtonManager.CANCELORDER))//取消订单
		{
			showDiaglog(activity, str, baiJiaOrderListInfo);
		}
	}
	
	
	static void showDiaglog(final Activity activity,final String str,final BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		Dialog dialog=new AlertDialog.Builder(activity).setTitle("确认").setMessage("是否 "+str).setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if(str.equals(ButtonManager.QUERENTIHUO))//确认提货
				{
					ButtonManager.affirmPUG(activity, baiJiaOrderListInfo);
					
				}else if(str.equals(ButtonManager.CANCELPAY))//申请退货
				{
					ButtonManager.cancelRefund(activity, baiJiaOrderListInfo);
				}else if(str.equals(ButtonManager.CANCELORDER))//取消订单
				{
					ButtonManager.cancelOrder(activity, baiJiaOrderListInfo);
				}
			}
		}).setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create();
		dialog.show();
	}
	
	
	
	
	
	/****
	 * 确认提货
	 * ***/
	
	public static void affirmPUG(final Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		HttpControl httpControl=new HttpControl();
		httpControl.affirmPickToGood(baiJiaOrderListInfo.getOrderNo(), true, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				MyApplication.getInstance().showMessage(context, QUERENTIHUO+"成功");
				ToolsUtil.sendOrderBroadcase();
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(context, msg);
			}
		}, context);
	}
	
	
	/****
	 * 撤销退货
	 * **/
	public static void cancelRefund(final Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		
		HttpControl httpControl=new HttpControl();
		httpControl.cancelRma(baiJiaOrderListInfo.getOrderNo(), true, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				MyApplication.getInstance().showMessage(context, CANCELPAY+"成功");
				ToolsUtil.sendOrderBroadcase();
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(context, msg);
			}
		}, context);
	}
	
	
	
	/******
	 * 申请退款
	 * **/
	public static void applyforRefund(Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		
		Intent intent=new Intent(context,ApplyForRefundActivity.class);
		intent.putExtra("DATA", baiJiaOrderListInfo);
		context.startActivity(intent);
		//((Activity)context).startActivityForResult(intent, 200);
	}
	
	/***
	 * 付款跳转
	 * ***/
	public static void payOrder(Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		ToolsUtil.frowardPayActivity((Activity)context,baiJiaOrderListInfo.getProduct().getName(), baiJiaOrderListInfo.getProduct().getProductCount(), baiJiaOrderListInfo.getOrderNo(), baiJiaOrderListInfo.getAmount());

		//((Activity)context).startActivityForResult(intent, 200);
	}
	
	
	/****
	 * 申诉中
	 * **/
	public static void cancelRefundLoading(final Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		Intent intent=new Intent(context,BaijiaAppealLoadingActivity.class);
		intent.putExtra("DATA", baiJiaOrderListInfo);
		context.startActivity(intent);
		//((Activity)context).startActivityForResult(intent, 200);
	}
	
	/***
	 * 取消订单
	 * ***/
	public static void cancelOrder(final Context context,BaiJiaOrderListInfo baiJiaOrderListInfo)
	{
		HttpControl httpControl=new HttpControl();
		httpControl.cancelOrder(baiJiaOrderListInfo.getOrderNo(), true, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				MyApplication.getInstance().showMessage(context, CANCELORDER+"成功");
				ToolsUtil.sendOrderBroadcase();
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				MyApplication.getInstance().showMessage(context, msg);
			}
		}, context);
	}
	
}
