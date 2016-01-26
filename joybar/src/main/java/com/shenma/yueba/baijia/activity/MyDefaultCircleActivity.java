package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.fragment.ChatFragment;

/**
 * Created by Administrator on 2016/1/23.
 * 我的默认圈子
 */
public class MyDefaultCircleActivity extends FragmentActivity {
    FragmentManager fragmentManager;
    ViewPager mydefaultcircle_layout_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mydefaultcircle_layout);
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        initView();
    }

    void initView() {
        TextView tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setVisibility(View.VISIBLE);
        tv_top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        tv_top_title.setVisibility(View.VISIBLE);
        if (this.getIntent().getStringExtra("chatType") != null) {
            String currChatType = this.getIntent().getStringExtra("chatType");
            if (currChatType.equals("CircleID"))//指定圈子
            {

                // 我的昵称
                String userName = this.getIntent().getStringExtra("Chat_NAME");
                tv_top_title.setText(userName);
            } else if (currChatType.equals("Private"))//私聊
            {
                // 我的昵称
                String userName = this.getIntent().getStringExtra("Chat_NAME");
                tv_top_title.setText(userName);
            }

        }


        mydefaultcircle_layout_viewpager = (ViewPager) findViewById(R.id.mydefaultcircle_layout_viewpager);
        mydefaultcircle_layout_viewpager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        });

    }
}
