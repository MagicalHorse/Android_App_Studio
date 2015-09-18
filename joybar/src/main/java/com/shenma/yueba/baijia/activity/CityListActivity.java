package com.shenma.yueba.baijia.activity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.CityBean;
import com.shenma.yueba.baijia.modle.CityBeanList;
import com.shenma.yueba.baijia.modle.CityListRequestBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.sore.CharacterParser;
import com.shenma.yueba.util.sore.ClearEditText;
import com.shenma.yueba.util.sore.PinyinComparator;
import com.shenma.yueba.util.sore.SideBar;
import com.shenma.yueba.util.sore.SideBar.OnTouchingLetterChangedListener;
import com.shenma.yueba.util.sore.SortAdapter;
import com.shenma.yueba.util.sore.SortModel;
import com.umeng.analytics.MobclickAgent;

public class CityListActivity extends BaseActivityWithTopView {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> sourceDateList;
	//城市列表map  string--城市名称    Integer--对应城市的id
	Map<String, Integer> citymap=new HashMap<String, Integer>();
	List<String> city_list=new ArrayList<String>();
	static CityListRequestBean cityBeanList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_list_layout);
		super.onCreate(savedInstanceState);
		initViews();
	}

	private void initViews() {
		setTitle("选择城市");
		setLeftTextView(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CityListActivity.this.finish();
			}
		});
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				//Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				List<SortModel> sortmodel=adapter.getListData();
				if(sortmodel!=null)
				{
					SortModel sortModel=sortmodel.get(position);
					String name=sortModel.getName();
					if(citymap.containsKey(name))
					{
						int value=citymap.get(name);
						Intent intent=new Intent();
						intent.putExtra(FillPersonDataActivity.CITY_NAME, name);
						intent.putExtra(FillPersonDataActivity.CITY_VALUE, value);
						Log.i("TAG",FillPersonDataActivity.CITY_VALUE+ ":"+value);
						setResult(FillPersonDataActivity.REQUESTCODE, intent);
					}
				}
				finish();
			}
		});
		sourceDateList=new ArrayList<SortModel>();
		//SourceDateList = filledData(getResources().getStringArray(R.array.date));
		adapter = new SortAdapter(this, sourceDateList);
		sortListView.setAdapter(adapter);
		//如果从在 城市列表数据 则直接设置 否则 从网络获取
		if(cityBeanList!=null && cityBeanList.getData().size()>0)
		{
			setCityList(cityBeanList);
		}else
		{
			getCityList();
		}
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
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
		
		
		FontManager.changeFonts(mContext, tv_top_title,mClearEditText);
	}

	/*****
	 * 访问网络获取城市列表
	 * **/
	void getCityList()
	{
		HttpControl httpControl=new HttpControl();
		httpControl.getCityList(new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				
				setCityList(obj);
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				
				Toast.makeText(CityListActivity.this, msg, Toast.LENGTH_SHORT).show();
				
			}
		}, this);
		
	}

	/*****
	 * 根据 城市列表对象 获取列表信息
	 * ***/
	void setCityList(Object obj)
	{
		if(obj!=null && obj instanceof CityListRequestBean)
		{
			cityBeanList=(CityListRequestBean)obj;
			List<CityBeanList> list=cityBeanList.getData();
			if(list!=null && list.size()>0 )
			{
				sourceDateList.clear();
				citymap.clear();
				for(int i=0;i<list.size();i++)
				{
					CityBeanList citybeanlsit=list.get(i);
					List<CityBean>  citybean_array=citybeanlsit.getCities();
					for(int j=0;j<citybean_array.size();j++)
					{
						CityBean cityBean=citybean_array.get(j);
						citymap.put(cityBean.getName(), cityBean.getId());
						city_list.add(cityBean.getName());
					}
					
					//CityBean cityBean=list.get(i);
					
				}
				
			}
			
		}
        setShowCityList();
	}
	
	
	void setShowCityList()
	{
		sourceDateList.clear();
		//将汉子转化为拼音
		sourceDateList=filledData(city_list);
		// 根据a-z进行排序源数据
	    Collections.sort(sourceDateList, pinyinComparator);
	    adapter.updateListView(sourceDateList);
	    adapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 为ListView填充数据
	 * @return
	 */
	private List<SortModel> filledData(List<String> city_array){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<city_array.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(city_array.get(i));
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(city_array.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = sourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : sourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
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
