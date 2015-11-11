package com.shenma.yueba.activity.baijia;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shenma.yueba.R;
import com.shenma.yueba.activity.BaseActivity;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.fragment.baijia.IndexFragmentForBaiJia;
import com.shenma.yueba.fragment.baijia.MeFragmentForBaiJia;
import com.shenma.yueba.fragment.baijia.MessageFragment;
import com.shenma.yueba.fragment.baijia.QueryshoppingGuideFragment;
import com.shenma.yueba.models.FragmentBean;
import com.shenma.yueba.utils.TabFragmentManager;
import com.shenma.yueba.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class MainActivityForBaiJia extends BaseActivity {
    private long exitTime = 0;// 初始化退出时间，用于两次点击返回退出程序
    LinearLayout baijia_main_foot_linearlayout;
    List<FragmentBean> fragment_list = new ArrayList<FragmentBean>();
    List<View> footer_list = new ArrayList<View>();
    // 当前选中的id
    int currid = -1;
    Fragment indexFragmentForBaiJia, queryshoppingGuideFragment, messageFragment, meFragmentForBaiJia;
    FragmentManager fragmentManager;
    boolean isbroadcase = false;

    FrameLayout baijia_main_framelayout;//fragment内容视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView()上边
        setContentView(R.layout.baijia_main_layout);

        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);
        initView();
        initaddFooterView();
        ToolsUtil.checkVersion(MainActivityForBaiJia.this);
    }

    void initView() {
        fragmentManager = getFragmentManager();
        baijia_main_framelayout = (FrameLayout) findViewById(R.id.baijia_main_framelayout);
        baijia_main_foot_linearlayout = (LinearLayout) findViewById(R.id.baijia_main_foot_linearlayout);
        indexFragmentForBaiJia = new IndexFragmentForBaiJia();
        queryshoppingGuideFragment = new QueryshoppingGuideFragment();
        messageFragment = new MessageFragment();
        meFragmentForBaiJia = new MeFragmentForBaiJia();

        fragment_list.add(new FragmentBean("主页",
                R.drawable.baijia_footer_main_background, indexFragmentForBaiJia));
        fragment_list.add(new FragmentBean("找导购",
                R.drawable.baijia_footer_shoppingguide_background, queryshoppingGuideFragment));
        fragment_list.add(new FragmentBean("消息",
                R.drawable.baijia_footer_message_background, messageFragment));
        fragment_list.add(new FragmentBean("个人",
                R.drawable.baijia_footer_people_background, meFragmentForBaiJia));

    }

    /***********
     * 初始化 tab 与 framelayout 之间的 视图切换与数据关联
     ***********/
    void initaddFooterView() {
        TabFragmentManager tabFragmentManager = new TabFragmentManager(fragment_list, MainActivityForBaiJia.this, baijia_main_framelayout, baijia_main_foot_linearlayout, new TabFragmentManager.TabFragmentOnClickListener() {


            @Override
            public void tabFragmentOnClick(int i) {

            }
        });
        tabFragmentManager.setHiddenImage(false);
        tabFragmentManager.setHiddenText(false);
        tabFragmentManager.initView();
        tabFragmentManager.setCurrView(0);
    }

}
