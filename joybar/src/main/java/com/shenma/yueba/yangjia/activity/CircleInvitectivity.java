package com.shenma.yueba.yangjia.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaseActivityWithTopView;
import com.shenma.yueba.baijia.activity.FillPersonDataActivity;
import com.shenma.yueba.baijia.modle.CityListRequestBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.sore.CharacterParser;
import com.shenma.yueba.util.sore.ClearEditText;
import com.shenma.yueba.util.sore.FriendSortAdapter;
import com.shenma.yueba.util.sore.PinyinComparator;
import com.shenma.yueba.util.sore.SideBar;
import com.shenma.yueba.util.sore.SideBar.OnTouchingLetterChangedListener;
import com.shenma.yueba.yangjia.modle.AttationAndFansItemBean;
import com.shenma.yueba.yangjia.modle.FansBackListForInviteCirlce;
import com.shenma.yueba.yangjia.modle.FansItemBean;
import com.umeng.analytics.MobclickAgent;

/**
 * 邀请好友加入圈子
 * 
 * @author a
 */

public class CircleInvitectivity extends BaseActivityWithTopView implements
		OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private FriendSortAdapter adapter;
	private ClearEditText mClearEditText;
	private CheckBox cb_all;
	private List<String> ids = new LinkedList<String>();
	private boolean allChecked;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<FansItemBean> sourceDateList;
	// 城市列表map string--城市名称 Integer--对应城市的id
	Map<String, Integer> citymap = new HashMap<String, Integer>();
	List<String> city_list = new ArrayList<String>();
	static CityListRequestBean cityBeanList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private TextView tv_sure;
	private String circleId;
	private TextView tv_nodata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);//加入回退栈
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invite_list_layout);
		super.onCreate(savedInstanceState);
		circleId = getIntent().getStringExtra("circleId");
		initViews();
	}

	private void initViews() {
		setTitle("邀请好友");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CircleInvitectivity.this.finish();
			}
		});
		cb_all = getView(R.id.cb_all);
		cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				for (int i = 0; i < sourceDateList.size(); i++) { 
					if(isChecked){
						sourceDateList.get(i).setChecked(true);
						if(!ids.contains(sourceDateList.get(i).getUserId())){
							ids.add(sourceDateList.get(i).getUserId());
						}
					}else{
						sourceDateList.get(i).setChecked(false);
						ids.clear();
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		tv_nodata = getView(R.id.tv_nodata);
		tv_sure = getView(R.id.tv_sure);
		tv_sure.setOnClickListener(this);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						getApplication(),
						((AttationAndFansItemBean) adapter.getItem(position))
								.getUserName(), Toast.LENGTH_SHORT).show();
				List<FansItemBean> AttationAndFansItemBean = adapter
						.getListData();
				if (AttationAndFansItemBean != null) {
					FansItemBean attationAndFansItemBean = AttationAndFansItemBean
							.get(position);
					String name = attationAndFansItemBean.getUserName();
					if (citymap.containsKey(name)) {
						int value = citymap.get(name);
						Intent intent = new Intent();
						intent.putExtra(FillPersonDataActivity.CITY_NAME, name);
						intent.putExtra(FillPersonDataActivity.CITY_VALUE,
								value);
						Log.i("TAG", FillPersonDataActivity.CITY_VALUE + ":"
								+ value);
						setResult(FillPersonDataActivity.REQUESTCODE, intent);
					}
				}
				finish();
			}
		});
		sourceDateList = new ArrayList<FansItemBean>();
		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));
		adapter = new FriendSortAdapter(this, sourceDateList);
		sortListView.setAdapter(adapter);
		// 如果从在 城市列表数据 则直接设置 否则 从网络获取

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		FontManager.changeFonts(mContext, tv_top_title,tv_sure,tv_nodata,cb_all);
		getAttationOrFansList("1", CircleInvitectivity.this, true);
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<FansItemBean> filterDateList = new ArrayList<FansItemBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = sourceDateList;
		} else {
			filterDateList.clear();
			for (FansItemBean AttationAndFansItemBean : sourceDateList) {
				String name = AttationAndFansItemBean.getUserName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(AttationAndFansItemBean);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.tv_sure://确定
			if(ids.size() == 0){
				Toast.makeText(mContext, "请选择至少一个粉丝", 1000).show();
				return;
			}
			addFansToCircle(circleId, setListToString(ids), mContext, true);
			break;
		default:
			break;
		}

	}

	
	
	/**
	 * 获取关注列表和fans列表
	 */
	public void getAttationOrFansList(String status, Context ctx,
			boolean showDialog) {
		HttpControl httpControl = new HttpControl();
		httpControl.GetValidFansListToGroup(status,circleId,true,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						FansBackListForInviteCirlce bean = (FansBackListForInviteCirlce) obj;
						if (bean != null && bean.getData() != null
								&& bean.getData().size() > 0) {
							tv_nodata.setVisibility(View.GONE);
							filledData(bean.getData());
							// 根据a-z进行排序源数据
							Collections.sort(sourceDateList, pinyinComparator);
							adapter.updateListView(sourceDateList);
							adapter.notifyDataSetChanged();
						} else {
							tv_nodata.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(CircleInvitectivity.this, msg, 1000)
								.show();
					}
				}, ctx);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private void filledData(List<FansItemBean> dataList) {
		for (int i = 0; i < dataList.size(); i++) {
			String userName = dataList.get(i).getUserName();
			if (TextUtils.isEmpty(userName)) {
				userName = "无名氏";
				dataList.get(i).setUserName(userName);
			}
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(userName);
			
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				dataList.get(i).setSortLetters(sortString.toUpperCase());
			} else {
				dataList.get(i).setSortLetters("#");
			}
			sourceDateList.add(dataList.get(i));
		}
	}

	/**
	 * 添加fansToCircle
	 * 
	 * @param ctx
	 * @param showDialog
	 */
	public void addFansToCircle(String circleId, String ids, Context ctx,boolean showDialog) {
		HttpControl httpControl = new HttpControl();
		httpControl.addFansToCircle(circleId, ids, showDialog,
				new HttpCallBackInterface() {

					@Override
					public void http_Success(Object obj) {
						Toast.makeText(mContext, "邀请成功！", 1000).show();
						setResult(Constants.RESULTCODE2);
						CircleInvitectivity.this.finish();
						
					}

					@Override
					public void http_Fails(int error, String msg) {
						Toast.makeText(mContext, msg, 1000).show();

					}
				}, CircleInvitectivity.this);
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

	private String setListToString(List<String> ids) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			sb.append(ids.get(i)).append(",");
		}
		return sb.substring(0, sb.length() - 1);
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
