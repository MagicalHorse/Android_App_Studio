package com.shenma.yueba.baijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.SearchHistoryAdapter;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.yangjia.activity.AboutActivity;

import java.util.ArrayList;

/**
 * 搜索商品的页面
 * Created by a on 2015/9/22.
 */
public class SearchProductActivity extends BaseActivityWithTopView implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private EditText et_search;
    private ListView lv_history;
    private Button bt_searchs;
    private ArrayList<String> mList = new ArrayList<String>();
    private ArrayList<String> allList = new ArrayList<String>();
    private SearchHistoryAdapter adapter;
    private int maxLengh = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyApplication.getInstance().addActivity(this);//加入回退栈
        setContentView(R.layout.search_product_activity);
        super.onCreate(savedInstanceState);
        setTitle("搜索");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_search = (EditText) findViewById(R.id.et_search);
        lv_history = (ListView) findViewById(R.id.lv_history);
        bt_searchs = (Button)findViewById(R.id.bt_searchs);
        adapter = new SearchHistoryAdapter(SearchProductActivity.this,mList);
        et_search.addTextChangedListener(this);
        bt_searchs.setOnClickListener(this);
        lv_history.setAdapter(adapter);
        lv_history.setOnItemClickListener(this);
        View footView = View.inflate(mContext, R.layout.search_bottom_layout, null);
        TextView tv_clear = (TextView)footView.findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil.setStringPerfernece(mContext,SharedUtil.search_history,"");
                mList.clear();
                allList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        lv_history.addFooterView(footView);
        FontManager.changeFonts(this,tv_top_title,et_search);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String historyStr =  SharedUtil.getStringPerfernece(SearchProductActivity.this, SharedUtil.search_history);
        String[] historyArr =  historyStr.split(",");
        mList.clear();
        allList.clear();
        for (int i=0;i<historyArr.length;i++){
            mList.add(historyArr[i]);
            allList.add(historyArr[i]);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_searchs://搜索
                if(TextUtils.isEmpty(et_search.getText().toString().trim())){
                    Toast.makeText(SearchProductActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    String historyStr = SharedUtil.getStringPerfernece(SearchProductActivity.this, SharedUtil.search_history);
                    if(TextUtils.isEmpty(historyStr)){
                        SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history, et_search.getText().toString().trim());
//                        mList.add(et_search.getText().toString().trim());
                        allList.add(et_search.getText().toString().trim());
                    }else if(et_search.getText().toString().trim().contains(",")){
                        Toast.makeText(SearchProductActivity.this,"搜索内容不能包含分号",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!allList.contains(et_search.getText().toString().trim())){
                            if(allList.size()<10){
                                allList.add(0,et_search.getText().toString().trim());
                                SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history,  et_search.getText().toString().trim() + ","+historyStr);
                            }else{
                                allList.add(0, et_search.getText().toString().trim());
                                allList.remove(maxLengh);
                                SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history, "");
                                StringBuffer sb = new StringBuffer();
                                for (int i=0;i<allList.size();i++){
                                    sb.append(allList.get(i)).append(",");
                                }
                                SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history, sb.subSequence(0, sb.length() - 1).toString());
                            }

                        }else{
                            allList.remove(et_search.getText().toString().trim());
                            allList.add(0,et_search.getText().toString().trim());
                            SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history, "");
                            StringBuffer sb = new StringBuffer();
                            for (int i=0;i<allList.size();i++){
                                sb.append(allList.get(i)).append(",");
                            }
                            SharedUtil.setStringPerfernece(SearchProductActivity.this, SharedUtil.search_history, sb.subSequence(0, sb.length() - 1).toString());
                        }

                    }
                }
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()>0){
            mList.clear();
            for (int i=0;i<allList.size();i++){
                if(allList.get(i).contains(s.toString())){
                    mList.add(allList.get(i));
                }
            }
        }else{
            mList.clear();
            mList.addAll(allList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchProductActivity.this, AboutActivity.class);
        startActivity(intent);
    }
}
