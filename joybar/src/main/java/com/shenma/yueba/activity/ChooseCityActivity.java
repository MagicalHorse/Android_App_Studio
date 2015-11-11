package com.shenma.yueba.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenma.yueba.R;
import com.shenma.yueba.adapter.ChooseCityAdapter;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.dialog.CustomProgressDialog;
import com.shenma.yueba.https.CommonHttpControl;
import com.shenma.yueba.interfaces.HttpCallBackInterface;
import com.shenma.yueba.interfaces.LocationBackListner;
import com.shenma.yueba.models.CityListItembean;
import com.shenma.yueba.models.Request_CityInfo;
import com.shenma.yueba.models.Request_CityListInfo;
import com.shenma.yueba.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 开通SHOPPING的城市列表
 * Created by a on 2015/9/28.
 */
public class ChooseCityActivity extends BaseActivity {
    private ListView lv_city;
    private List<CityListItembean> cityList = new ArrayList<CityListItembean>();
    private View headerView;
    private View footerView;
    private ChooseCityAdapter adapter;
    private TextView tv_city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView()上边
        setContentView(R.layout.choose_city_layout);
        super.onCreate(savedInstanceState);
        initView();
        getCityList();
        //模拟数据
        for(int i=0;i<10;i++)
        {
            CityListItembean cityListItembean=new CityListItembean();
            cityListItembean.setId("id"+i);
            cityListItembean.setKey("key"+i);
            cityListItembean.setName("name"+i);
            cityList.add(cityListItembean);
        }
    }

    private void initView() {
        setTitleName("选择城市");
        setLeftTextView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityActivity.this.finish();
                overridePendingTransition(0, R.anim.out_from_bottom);
            }
        });

        adapter = new ChooseCityAdapter(mContext, cityList);
        lv_city = (ListView) findViewById(R.id.lv_city);
        lv_city.setAdapter(adapter);

        headerView = LayoutInflater.from(mContext).inflate(R.layout.choose_city_item,null);
        tv_city_name = (TextView) headerView.findViewById(R.id.tv_city_name);
        TextView tv_grey_title = (TextView) headerView.findViewById(R.id.tv_grey_title);
        tv_grey_title.setText("当前定位城市");
        ImageView iv_address = (ImageView) headerView.findViewById(R.id.iv_address);
        iv_address.setVisibility(View.VISIBLE);
        iv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityById();
            }
        });
        View line_top = headerView.findViewById(R.id.line_top);
        View line_bottom = headerView.findViewById(R.id.line_bottom);
        line_top.setVisibility(View.VISIBLE);
        line_bottom.setVisibility(View.VISIBLE);
        footerView = View.inflate(mContext, R.layout.choose_city_footer, null);
        TextView tv_more_city = (TextView) footerView.findViewById(R.id.tv_more_city);
        lv_city.addHeaderView(headerView);
        lv_city.addFooterView(footerView);
    }


    private void getCityList() {
        CommonHttpControl.getShoppingCityList(new HttpCallBackInterface<Request_CityListInfo>() {
            @Override
            public void http_Success(Request_CityListInfo bean) {
                cityList.addAll(bean.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void http_Fails(int error, String msg) {
                MyApplication.getInstance().showMessage(ChooseCityActivity.this, msg);
            }
        });
    }


    private void getCityById() {
        final CustomProgressDialog dialog = new CustomProgressDialog(mContext).createDialog(mContext);
        dialog.setMessage("定位中...");
        dialog.show();
        LocationUtil.getLocation(ChooseCityActivity.this, new LocationBackListner() {
            @Override
            public void callBack(boolean result) {
                if (result) {
                    Toast.makeText(mContext, "定位成功", Toast.LENGTH_SHORT).show();
                    //开始调用接口，根据经纬度获取城市名称
                    CommonHttpControl.getCityNameByGPS(new HttpCallBackInterface<Request_CityInfo>() {
                        @Override
                        public void http_Success(Request_CityInfo back) {
                            String str = back.getData().getName();
                            tv_city_name.setText(str);
                        }

                        @Override
                        public void http_Fails(int error, String msg) {
                            tv_city_name.setText("全国");
                            Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
                    tv_city_name.setText("全国");
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);//加入回退栈
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.out_from_bottom);
    }


}
