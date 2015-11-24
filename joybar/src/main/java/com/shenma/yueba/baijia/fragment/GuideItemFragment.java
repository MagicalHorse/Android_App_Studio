package com.shenma.yueba.baijia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shenma.yueba.R;

/**
 * Created by a on 2015/11/23.
 */
public class GuideItemFragment extends BaseFragment{


    private View view;
    private boolean hasData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(hasData){
            view = inflater.inflate(R.layout.shopping_guide_layout,null);
            LinearLayout guide_ll_container = (LinearLayout)view.findViewById(R.id.guide_ll_container);
        }else{
            view  = inflater.inflate(R.layout.shopping_guide_wsx_layout,null);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
