package com.shenma.yueba.baijia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.modle.ChooseCouponListBean;

import java.util.ArrayList;

/**
 * Created by a on 2015/9/22.
 */
public class ChooseCouponAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<ChooseCouponListBean> mList;
    public ChooseCouponAdapter(Context ctx, ArrayList<ChooseCouponListBean> mList){
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(ctx,R.layout.item_choose_coupon_layout,null);
            holder.cb = (CheckBox)convertView.findViewById(R.id.cb);
            holder.et_need = (EditText) convertView.findViewById(R.id.et_need);
            holder.tv_introduce = (TextView) convertView.findViewById(R.id.tv_introduce);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }


    class ViewHolder{
        CheckBox cb;
        EditText et_need;
        TextView tv_introduce;

    }
}
