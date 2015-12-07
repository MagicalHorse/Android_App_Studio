package com.shenma.yueba.baijia.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.fragment.BaiJiaOrderListFragment;
import com.shenma.yueba.baijia.modle.FragmentBean;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.OrderChangeRefreshObserver;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-6-2 上午10:39:58
 *          程序的简单说明  我的订单主页
 */

public class BaiJiaOrderListActivity extends FragmentActivity {
    FragmentManager fragmentManager;
    FrameLayout baijia_main_framelayout;
    LinearLayout baijia_fragment_tab1_head_linearlayout;
    List<FragmentBean> fragment_list = new ArrayList<FragmentBean>();
    List<View> footer_list = new ArrayList<View>();
    BaiJiaOrderListFragment allorder, waitpayorder, zitiorder, afterorder;

    int currid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.baijia_myorder_layout);
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        initView();
        initaddFooterView();
        //获取 显示的视图
        int currview = this.getIntent().getIntExtra("CURRID", 0);
        setCurrView(currview);

    }


    void initView() {
        TextView tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        FontManager.changeFonts(this, tv_top_title);
        tv_top_title.setVisibility(View.VISIBLE);
        tv_top_title.setText("我的订单");
        TextView tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setVisibility(View.VISIBLE);
        tv_top_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BaiJiaOrderListActivity.this.finish();
            }
        });
        fragmentManager = this.getSupportFragmentManager();
        baijia_main_framelayout = (FrameLayout) findViewById(R.id.baijia_main_framelayout);
        baijia_fragment_tab1_head_linearlayout = (LinearLayout) findViewById(R.id.baijia_fragment_tab1_head_linearlayout);
        //全部订单 0，待付款 1，专柜自提 2，售后 3

         allorder = new BaiJiaOrderListFragment(0);
         waitpayorder = new BaiJiaOrderListFragment(1);
         zitiorder = new BaiJiaOrderListFragment(2);
         afterorder = new BaiJiaOrderListFragment(3);

        fragment_list.add(new FragmentBean("全部", -1, allorder));
        fragment_list.add(new FragmentBean("待付款", -1, waitpayorder));
        fragment_list.add(new FragmentBean("专柜自提", -1, zitiorder));
        fragment_list.add(new FragmentBean("售后", -1, afterorder));
    }


    void initaddFooterView() {

        for (int i = 0; i < fragment_list.size(); i++) {
            RelativeLayout rl = (RelativeLayout) RelativeLayout.inflate(BaiJiaOrderListActivity.this, R.layout.tab_line_layout, null);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            FontManager.changeFonts(BaiJiaOrderListActivity.this, tv);
            rl.setTag(i);
            rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    setCurrView(i);
                }
            });
            tv.setGravity(Gravity.CENTER);
            tv.setText(fragment_list.get(i).getName());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1;
            param.gravity = Gravity.CENTER;
            baijia_fragment_tab1_head_linearlayout.addView(rl, param);
            footer_list.add(rl);
        }
    }


    void setCurrView(int i) {
        if (currid == -1 && i == 0) {
            if (!(((BaiJiaOrderListFragment) fragment_list.get(i).getFragment()).isAdded())) {
                fragmentManager.beginTransaction().add(R.id.baijia_main_framelayout, (BaiJiaOrderListFragment) fragment_list.get(i).getFragment()).commit();
            }
        } else if (currid == i) {
            return;
        } else {
            if (!(((BaiJiaOrderListFragment) fragment_list.get(i).getFragment()).isAdded())) {
                fragmentManager.beginTransaction().replace(R.id.baijia_main_framelayout, (BaiJiaOrderListFragment) fragment_list.get(i).getFragment()).commit();
            }

        }
        currid = i;
        setTextColor(i);
    }

    /*****
     * 设置字体颜色及选中后显示的图片
     ***/
    void setTextColor(int value) {
        for (int i = 0; i < footer_list.size(); i++) {
            RelativeLayout rl = (RelativeLayout) footer_list.get(i);
            TextView tv = (TextView) rl.findViewById(R.id.tab_line_textview);
            View v = (View) rl.findViewById(R.id.tab_line_view);
            if (i == value) {
                tv.setTextColor(this.getResources().getColor(R.color.color_deeoyellow));
                v.setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(this.getResources().getColor(R.color.black));
                v.setVisibility(View.INVISIBLE);
            }

        }
    }

	/*@Override
		protected void onActivityResult(int arg0, int arg1, Intent arg2) {
			if(arg0==200)
			{
				if(arg2!=null)
				{
					if(arg2.getStringExtra("PAYRESULT").equals("SUCESS"))//如果支付返回成功（即微信通知成功）
					{
						//刷新数据
						BaiJiaOrderListFragment fragment=(BaiJiaOrderListFragment) fragment_list.get(currid).getFragment();
						if(fragment!=null)
						{
							fragment.requestFalshData();
						}
					}
				}
			}
		}
	*/


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);//加入回退栈
        super.onDestroy();
        OrderChangeRefreshObserver.getInstance().removeObserver(allorder);
        OrderChangeRefreshObserver.getInstance().removeObserver(waitpayorder);
        OrderChangeRefreshObserver.getInstance().removeObserver(zitiorder);
        OrderChangeRefreshObserver.getInstance().removeObserver(afterorder);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        //super.onSaveInstanceState(outState);
    }
}
