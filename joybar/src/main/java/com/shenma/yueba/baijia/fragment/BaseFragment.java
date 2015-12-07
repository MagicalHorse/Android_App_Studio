package com.shenma.yueba.baijia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shenma.yueba.R;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.CustomProgressDialog;

import java.util.ArrayList;

public class BaseFragment extends Fragment {

	public BaseFragment()
	{

	}


	private HttpUtils httpUtils;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		httpUtils = new HttpUtils();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	protected HttpUtils getHttpUtils(){
		if(httpUtils == null){
			httpUtils = new HttpUtils();
		}else{
			return httpUtils;
		}
		return httpUtils;
	}


	protected void setCursorAndText(int index,ArrayList<ImageView> cursorImageList,ArrayList<TextView> titleTextList) {
		for (int i = 0; i < cursorImageList.size(); i++) {
			if (i != index) {
				cursorImageList.get(i).setVisibility(View.INVISIBLE);
			} else {
				cursorImageList.get(i).setVisibility(View.VISIBLE);
			}
		}
		for (int j = 0; j < titleTextList.size(); j++) {
			if (j != index) {
				titleTextList.get(j).setTextSize(Constants.title_text_normal_size);
				titleTextList.get(j).setTextColor(getResources().getColor(R.color.text_gray_color));
			} else {
				titleTextList.get(j).setTextSize(Constants.title_text_selected_size);
				titleTextList.get(j).setTextColor(getResources().getColor(R.color.main_color));
			}
		}
	}
}
