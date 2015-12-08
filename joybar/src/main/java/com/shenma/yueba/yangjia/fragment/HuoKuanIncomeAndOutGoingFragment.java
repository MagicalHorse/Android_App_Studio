package com.shenma.yueba.yangjia.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.ApplyResultActivity;
import com.shenma.yueba.baijia.dialog.WeChatDialog;
import com.shenma.yueba.baijia.fragment.BaseFragment;
import com.shenma.yueba.baijia.modle.GetUserFlowStatusBackBean;
import com.shenma.yueba.baijia.modle.UserFlowStatusBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.DialogUtilInter;
import com.shenma.yueba.util.DialogUtils;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.WXLoginUtil;
import com.shenma.yueba.yangjia.adapter.HuoKuanIncomeAndOutGoingAdapter;
import com.shenma.yueba.yangjia.modle.HuoKuanItem;
import com.shenma.yueba.yangjia.modle.HuoKuanListBackBean;
/**
 * 货款收支的Fragment
 * @author a
 *
 */
@SuppressLint("ValidFragment")
public class HuoKuanIncomeAndOutGoingFragment extends BaseFragment implements OnClickListener{
	private View rootView;// 缓存Fragment view
	private PullToRefreshListView rlv;
	private List<HuoKuanItem> mList = new ArrayList<HuoKuanItem>();
	private HuoKuanIncomeAndOutGoingAdapter adapter;
	private boolean isRefresh = true;
	private int page = 1;
	private int tag = 0;
	private String status;// 0冻结中，1可提现，2已经提现，3退款
	public TextView tv_nodata;
	private LinearLayout ll_bottom_layout;
	private View bottomView;
	public TextView tv_bottom;
	private List<String> ids = new LinkedList<String>();
	@SuppressLint("ValidFragment")
	public HuoKuanIncomeAndOutGoingFragment(int tag) {
		this.tag = tag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(
					R.layout.refresh_listview_without_title_layout, container,
					false);
			tv_nodata = (TextView) rootView.findViewById(R.id.tv_nodata);
			ll_bottom_layout = (LinearLayout) rootView.findViewById(R.id.ll_bottom_layout);
			bottomView = View.inflate(getActivity(), R.layout.bottom_text_layout, null);
			tv_bottom = (TextView) bottomView.findViewById(R.id.tv_bottom);
			tv_bottom.setOnClickListener(this);
			ll_bottom_layout.addView(bottomView);
			if(tag == 0){
				ll_bottom_layout.setVisibility(View.VISIBLE);
				tv_bottom.setText("提现货款");
			}else{
				ll_bottom_layout.setVisibility(View.GONE);
			}
			rlv = (PullToRefreshListView) rootView
					.findViewById(R.id.pull_refresh_list);
			rlv.setMode(Mode.BOTH);
			rlv.setOnRefreshListener(new OnRefreshListener2() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase refreshView) {
					page = 1;
					isRefresh = true;
					getDataFromNet(false, getActivity());
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase refreshView) {
					page++;
					isRefresh = false;
					getDataFromNet(false, getActivity());
				}
			});
			rlv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
//					Intent intent = new Intent(getActivity(),
//							OrderDetailActivity.class);
//					intent.putExtra("orderId", mList.get(arg2).getOrderNo());
//					startActivity(intent);

				}
			});
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		adapter = new HuoKuanIncomeAndOutGoingAdapter(
				getActivity(), mList, tag);
		rlv.setAdapter(adapter);
		return rootView;
	}

	
	//重置提现货款按钮
	public void reSetTv_bottom(){
		tv_bottom.setText("提现货款");
	}
	
	public String setListToString(List<String> ids) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			sb.append(ids.get(i)).append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	
	public void setIdToList(String id) {
		if (ids.contains(id)) {
			return;
		} else {
			ids.add(id);
		}
	}

	public void removeIdFromList(String id) {
		if (ids.contains(id)) {
			ids.remove(id);
		} else {
			return;
		}
	}
	
	public void getData(boolean isRefresh,int tag, Context ctx) {
		this.tag = tag;
		if (tag == 0) {// 可提现
			status = "1";
			if (mList.size() != 0 && !isRefresh) {
				return;
			}
		} else if (tag == 1) {// 冻结中
			status = "0";
			if (mList.size() != 0 && !isRefresh) {
				return;
			}
		} else if (tag == 2) {// 已提现
			status = "2";
			if (mList.size() != 0 && !isRefresh) {
				return;
			}
		} else if (tag == 3) {// 退款
			status = "3";
			if (mList.size() != 0 && !isRefresh) {
				return;
			}
		}
		if(isRefresh){
			mList.clear();
			page = 1;
		}
		getDataFromNet(true, ctx);
	}

	private void getDataFromNet(boolean showDialog, Context ctx) {
		HttpControl httpContorl = new HttpControl();
		httpContorl.getHuoKuanList(page, status, new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				rlv.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                    	 rlv.onRefreshComplete();
                     }
             }, 100);
				HuoKuanListBackBean bean = (HuoKuanListBackBean) obj;
				if (isRefresh) {
					ids.clear();//清空保存的ID
					mList.clear();//清空列表
					adapter.clearCountList();//清空数据和价格
					tv_bottom.setText("提现货款");//初始化提现货款按钮
					if (bean.getData() != null
							&& bean.getData().getItems() != null) {
						if(bean.getData().getItems().size()>0){
							tv_nodata.setVisibility(View.GONE);
						}else{
							tv_nodata.setVisibility(View.VISIBLE);
						}
						mList.addAll(bean.getData().getItems());
					}else{
						mList.clear();//清空列表
					}
					adapter.notifyDataSetChanged();
				} else {
					if (bean.getData().getItems() != null
							&& bean.getData().getItems().size() > 0) {
						mList.addAll(bean.getData().getItems());
					} else {
						Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT)
								.show();
					}
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void http_Fails(int error, String msg) {
				rlv.onRefreshComplete();
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		}, ctx, true, false);
	}

	
	/**
	 * 提现货款
	 */
	public void withdraw() {
		HttpControl httpControl = new HttpControl();
		httpControl.withdrawGoods(setListToString(ids), new HttpCallBackInterface() {
			@Override
			public void http_Success(Object obj) {
				Intent intent = new Intent(getActivity(),
						ApplyResultActivity.class);
				intent.putExtra("flag", "withdrawGoods");// 提现货款
				getActivity().startActivityForResult(intent, Constants.REQUESTCODE);
				if(adapter!=null){
					adapter.clearCountList();
				}
				MyApplication.getInstance().getHuoKuanManagerRefreshService().refreshList();
				MyApplication.getInstance().getIndexRefreshService().refreshList();
			}
			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
				
				
			}
		}, getActivity());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bottom://提现货款
			if(!SharedUtil.getBooleanPerfernece(getActivity(), SharedUtil.user_IsBindWeiXin)){
				DialogUtils dialog = new DialogUtils();
				dialog.alertDialog(getActivity(), "提示", "您需要绑定微信才可以提款，现在去绑定吗？", new DialogUtilInter() {
					@Override
					public void dialogCallBack(int... which) {
						Toast.makeText(getActivity(), "正在绑定微信", Toast.LENGTH_SHORT).show();
						// 绑定手机号
						WXLoginUtil wxLoginUtil = new WXLoginUtil(getActivity());
						wxLoginUtil.initWeiChatLogin(false,false,false);
					}
				}, true, "绑定", "取消", false, true);
				return;
			}
			if(ids ==null || ids.size()==0){
				Toast.makeText(getActivity(), "请选择提现订单", Toast.LENGTH_SHORT).show();
			}else{
				GetUserFlowStatus();
			}
			break;
		default:
			break;
		}
		
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
							DialogUtils dialogUtil = new DialogUtils();
							dialogUtil.alertDialog(getActivity(), "提示", "您确定提取货款 ￥"+tv_bottom.getTag()+" 到微信零钱中吗？", new DialogUtilInter() {
								@Override
								public void dialogCallBack(int... which) {
									withdraw();
								}
							}, true, "确定", "取消", false, true);
						}else{//没有关注
							WeChatDialog dialog = new WeChatDialog(getActivity(),data.getQRCode(),data.getName());
							dialog.show();
						}
					}
				};

			@Override
			public void http_Fails(int error, String msg) {
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		}, getActivity(), true);
	}
	
}
