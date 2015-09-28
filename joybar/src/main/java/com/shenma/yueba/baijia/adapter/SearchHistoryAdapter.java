package com.shenma.yueba.baijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.util.FontManager;

import java.util.ArrayList;

/**
 * Created by a on 2015/9/22.
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<String> mList;
    public SearchHistoryAdapter(Context ctx,ArrayList<String> mList){
        this.ctx = ctx;
        this.mList = mList;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
             View view = View.inflate(ctx,R.layout.search_history_item,null);
        TextView tv_history_item = (TextView)view.findViewById(R.id.tv_history_item);
        FontManager.changeFonts(ctx,tv_history_item);
        tv_history_item.setText(mList.get(position));
        return view;
    }
}
