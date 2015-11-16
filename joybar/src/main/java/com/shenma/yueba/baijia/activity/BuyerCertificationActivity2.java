package com.shenma.yueba.baijia.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.ApplyAuthBuyerBean;
import com.shenma.yueba.baijia.modle.CityListBackBean;
import com.shenma.yueba.baijia.modle.CityListItembean;
import com.shenma.yueba.baijia.modle.StoreItem;
import com.shenma.yueba.baijia.modle.StoreListBackBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.yangjia.adapter.CityListAdapter;
import com.shenma.yueba.yangjia.adapter.StoreListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 身份认证
 * 
 * @author a
 * 
 */
public class BuyerCertificationActivity2 extends BaseActivityWithTopView
		implements OnClickListener {

	DrawerLayout drawer_layout;
	private List<StoreItem> storeList;
	ListView lv;
	private TextView tv_store_info_title;
	private TextView tv_self_get_point_title;
//	private TextView tv_store_title;
	private TextView tv_store_name;
	private EditText et_store_name;
	private TextView tv_zhuangui_title;
	private EditText et_zhuangui_name;
	
	private TextView tv_number_title;
	private EditText et_number_name;
	private TextView tv_confirm;
	private EditText et_street;
	private TextView tv_county_town;
	private TextView tv_province;
	private TextView tv_city;

	
	private String province_id;
	private String city_id;
	private String town_id;
//	private String store_id;
	private int tag = 1;// 1表示省份，2表示城市，3表示区县
	private List<CityListItembean> mList = new ArrayList<CityListItembean>();
	private ApplyAuthBuyerBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buyer_certification_layout2);
		super.onCreate(savedInstanceState);
		initView();
		getIntentData();
		drawer_layout.setDrawerShadow(R.drawable.gray_round_bg,
				GravityCompat.START);
		drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // 关闭手势滑动
		// drawer_layout.setDrawerListener(new DrawerListener() {
		// /**
		// * 当抽屉滑动状态改变的时候被调用
		// * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）,
		// STATE_SETTLING（固定--2）中之一。
		// * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
		// */
		// @Override
		// public void onDrawerStateChanged(int arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// /**
		// * 当抽屉被滑动的时候调用此方法
		// * arg1 表示 滑动的幅度（0-1）
		// */
		// @Override
		// public void onDrawerSlide(View arg0, float arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		// /**
		// * 当一个抽屉被完全打开的时候被调用
		// */
		// @Override
		// public void onDrawerOpened(View arg0) {
		// drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
		// //打开手势滑动
		//
		// }
		// /**
		// * 当一个抽屉完全关闭的时候调用此方法
		// */
		// @Override
		// public void onDrawerClosed(View arg0) {
		// // TODO Auto-generated method stub
		// drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		// //关闭手势滑动
		// }
		// });
	}

	private void getIntentData() {
		bean = new ApplyAuthBuyerBean();
		// CardBean cardFrontBean = new CardBean();
		// CardBean cardBackBean = new CardBean();
		// CardBean workBackBean = new CardBean();
		Intent intent = getIntent();
		// cardFrontBean.setKey(intent.getStringExtra("pic1"));
		// cardBackBean.setKey(intent.getStringExtra("pic2"));
		// workBackBean.setKey(intent.getStringExtra("pic3"));
		bean.setWorkCard(intent.getStringExtra("pic3"));
		bean.setCardFront(intent.getStringExtra("pic1"));
		bean.setCardBack(intent.getStringExtra("pic2"));
		bean.setName(intent.getStringExtra("name"));
	}

	public void getStoreList() {
		tag = 4;
		HttpControl httpControl = new HttpControl();
		httpControl.getStoreList(new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				StoreListBackBean bean = (StoreListBackBean) obj;
				storeList = bean.getData();
				lv.setAdapter(new StoreListAdapter(mContext, storeList));
				drawer_layout.openDrawer(Gravity.RIGHT);
			}

			@Override
			public void http_Fails(int error, String msg) {
				// TODO Auto-generated method stub

			}
		}, this, true, true);
	}

	private void initView() {
		setTitle("提交身份认证");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		lv = (ListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (tag == 1) {
					tv_province.setText(mList.get(position).getName());
					province_id = mList.get(position).getId();
					drawer_layout.closeDrawers();
				}
				if (tag == 2) {
					tv_city.setText(mList.get(position).getName());
					city_id = mList.get(position).getId();
					drawer_layout.closeDrawers();
				}
				if (tag == 3) {
					tv_county_town.setText(mList.get(position).getName());
					town_id = mList.get(position).getId();
					drawer_layout.closeDrawers();
				}
//				if(tag == 4){
//					tv_store_title.setText("商城名称 "+storeList.get(position).getStoreName());
//					store_id = storeList.get(position).getStoreId();
//					drawer_layout.closeDrawers();
//				}
			}
		});
		tv_store_info_title = getView(R.id.tv_store_info_title);
		tv_self_get_point_title = getView(R.id.tv_self_get_point_title);

		tv_store_name = getView(R.id.tv_store_name);
		et_store_name = getView(R.id.et_store_name);
		tv_zhuangui_title = getView(R.id.tv_zhuangui_title);
		et_zhuangui_name = getView(R.id.et_zhuangui_name);
		tv_number_title = getView(R.id.tv_number_title);
		et_number_name = getView(R.id.et_number_name);
		tv_confirm = getView(R.id.tv_confirm);
		tv_confirm.setOnClickListener(this);
		et_street = getView(R.id.et_street);

		tv_county_town = getView(R.id.tv_county_town);
		tv_province = getView(R.id.tv_province);
		tv_city = getView(R.id.tv_city);
		tv_county_town.setOnClickListener(this);
		tv_province.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		FontManager.changeFonts(mContext, tv_store_info_title,
				tv_self_get_point_title, tv_store_name,et_store_name, tv_zhuangui_title,
				et_zhuangui_name, tv_number_title, et_number_name, tv_confirm,
				et_street,tv_top_title,tv_province,tv_city,tv_county_town);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_province:// 省
			tag = 1;
			HttpControl httpControl = new HttpControl();
			httpControl.getCityListById("0", new HttpCallBackInterface() {
				@Override
				public void http_Success(Object obj) {
					CityListBackBean bean = (CityListBackBean) obj;
					mList.clear();
					mList.addAll(bean.getData());
					lv.setAdapter(new CityListAdapter(mContext, mList));
					drawer_layout.openDrawer(Gravity.RIGHT);
				}

				@Override
				public void http_Fails(int error, String msg) {
					MyApplication.getInstance().showMessage(
							BuyerCertificationActivity2.this, msg);
				}
			}, BuyerCertificationActivity2.this);
			break;
		case R.id.tv_city:// 市
			tag = 2;
			if (!TextUtils.isEmpty(province_id)) {
				HttpControl httpControl2 = new HttpControl();
				httpControl2.getCityListById(province_id,
						new HttpCallBackInterface() {
							@Override
							public void http_Success(Object obj) {
								CityListBackBean bean = (CityListBackBean) obj;
								mList.clear();
								mList.addAll(bean.getData());
								lv.setAdapter(new CityListAdapter(mContext,
										mList));
								drawer_layout.openDrawer(Gravity.RIGHT);
							}

							@Override
							public void http_Fails(int error, String msg) {
								MyApplication.getInstance().showMessage(
										BuyerCertificationActivity2.this, msg);
							}
						}, BuyerCertificationActivity2.this);
				drawer_layout.openDrawer(Gravity.RIGHT);
			} else {
				Toast.makeText(mContext, "请先选择省份", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_county_town:// 县
			tag = 3;
			if (!TextUtils.isEmpty(city_id)) {
				HttpControl httpControl3 = new HttpControl();
				httpControl3.getCityListById(city_id,
						new HttpCallBackInterface() {
							@Override
							public void http_Success(Object obj) {
								CityListBackBean bean = (CityListBackBean) obj;
								mList.clear();
								mList.addAll(bean.getData());
								lv.setAdapter(new CityListAdapter(mContext,
										mList));
								drawer_layout.openDrawer(Gravity.RIGHT);
							}

							@Override
							public void http_Fails(int error, String msg) {
								MyApplication.getInstance().showMessage(
										BuyerCertificationActivity2.this, msg);
							}
						}, BuyerCertificationActivity2.this);
				drawer_layout.openDrawer(Gravity.RIGHT);
			} else {
				Toast.makeText(mContext, "请先选择城市", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_confirm:// 提交申请
			if (TextUtils.isEmpty(et_store_name.getText().toString())) {//
				Toast.makeText(mContext, "商场名称不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(et_zhuangui_name.getText().toString().trim())) {//
				Toast.makeText(mContext, "专柜名称不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(et_number_name.getText().toString().trim())) {//
				Toast.makeText(mContext, "专柜位置不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(province_id)) {//
				Toast.makeText(mContext, "请选择省份", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(city_id)) {//
				Toast.makeText(mContext, "请选择城市", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(town_id)) {//
				Toast.makeText(mContext, "请选择区县", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(et_street.getText().toString().trim())) {//
				Toast.makeText(mContext, "请填写详细地址", Toast.LENGTH_SHORT).show();
				return;
			}
			uploadData();
			break;
//		case R.id.tv_store_title://获取店铺列表
		//	getStoreList();
//			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			if (drawer_layout.isShown()) {
				drawer_layout.closeDrawer(Gravity.RIGHT);
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setData() {
		bean.setStoreName(et_store_name.getText().toString().trim());
		bean.setSectionName(et_zhuangui_name.getText().toString().trim());
		bean.setSectionLocate(et_number_name.getText().toString().trim());
		bean.setAddress(et_street.getText().toString().trim());
		bean.setProvinceId(province_id);
		bean.setCityId(city_id);
		bean.setDistrictId(town_id);
//		bean.setStoreId(store_id);
	}

	private void uploadData() {
		tv_confirm.setEnabled(false);
		setData();
		HttpControl httpControl = new HttpControl();
		httpControl.getAppliyAuthBuyer(bean, new HttpCallBackInterface() {

			@Override
			public void http_Success(Object obj) {
				tv_confirm.setEnabled(true);
				SharedUtil.setStringPerfernece(mContext, SharedUtil.user_AuditStatus, "0");
				
				Intent intent = new Intent(mContext, ApplyResultActivity.class);
				intent.putExtra("flag", "userCertification");
				startActivity(intent);
				MyApplication.getInstance().finishActivity(BuyerCertificationActivity1.class);
				MyApplication.getInstance().finishActivity(BuyerCertificationActivity2.class);
				
			}

			@Override
			public void http_Fails(int error, String msg) {
				tv_confirm.setEnabled(true);
                 MyApplication.getInstance().showMessage(BuyerCertificationActivity2.this, msg);
			}
		}, BuyerCertificationActivity2.this);

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
