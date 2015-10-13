package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shenma.yueba.R;
import com.shenma.yueba.view.MyListView;

import java.util.Random;

/**
 * Created by Administrator on 2015/10/12.
 */
public class ProductExtTab1Fragment extends Fragment {

    Activity activity;
    View parentView;
    MyListView productexttab1_layout_listview;
    Random random=new Random();
    int aaa;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        aaa=random.nextInt(10)+1;
        Log.i("TAG", "getCount :" + aaa);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.productexttab1_layout, null);
            initView();
        }
        if(parentView.getParent()!=null )
        {
            ((ViewGroup)parentView.getParent()).removeView(parentView);
        }
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    void initView() {
        productexttab1_layout_listview = (MyListView) parentView.findViewById(R.id.productexttab1_layout_listview);
        productexttab1_layout_listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {

                return aaa;

            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView=new ImageView(activity);
                }
                ((ImageView)convertView).setImageResource(R.drawable.default_pic);
                DisplayMetrics dm=new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                ((ImageView)convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(new AbsListView.LayoutParams(dm.widthPixels, dm.widthPixels));
                return convertView;
            }
        });


    }
}
