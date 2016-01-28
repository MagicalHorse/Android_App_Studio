package com.shenma.yueba.baijia.activity;



import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.imageshow.TouchImageView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TouchImageViewActivity extends BaseActivityWithTopView {
    /** Called when the activity is first created. */
    ViewPager touch_viewpager_layout_viewpager;
    List<String> str_array=new ArrayList<String>();
    List<View> view_list=new ArrayList<View>();
    int CurrID=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.touch_viewpager_layout);
        super.onCreate(savedInstanceState);
        if(this.getIntent().getStringArrayListExtra("IMG_URL")!=null)
        {
            str_array=this.getIntent().getStringArrayListExtra("IMG_URL");
        }else
        {
            this.finish();
        }

        CurrID= this.getIntent().getIntExtra("CurrID",0);
        if(CurrID<0)
        {
            CurrID=0;
        }

        for(int i=0;i<str_array.size();i++)
        {
            TouchImageView img = new TouchImageView(TouchImageViewActivity.this);
            MyApplication.getInstance().getBitmapUtil().display(img, ToolsUtil.nullToString(str_array.get(i)));
            view_list.add(img);
        }
        initView();

        if(CurrID<str_array.size())
        {
            touch_viewpager_layout_viewpager.setCurrentItem(CurrID);
        }
    }

    void initView()
    {
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TouchImageViewActivity.this.finish();
            }
        });
        touch_viewpager_layout_viewpager=(ViewPager)findViewById(R.id.touch_viewpager_layout_viewpager);
        touch_viewpager_layout_viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return view_list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //container.removeViewAt(position);
                container.removeView(view_list.get(position));
                //super.destroyItem(container, position, object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(view_list.get(position), 0);
                Log.i("TAG", "instantiateItem result:url=" + str_array);
                return view_list.get(position);
            }
        });
    }
}