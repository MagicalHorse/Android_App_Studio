package com.shenma.yueba.baijia.modle.newmodel;

import android.view.View;
import android.widget.LinearLayout;

import com.shenma.yueba.R;
import com.shenma.yueba.baijia.activity.MarketMainActivity;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class HomeItem_1_Info extends Abs_HomeItemInfo {
    LinearLayout home_item_layout_include1;

    @Override
    View getContextView(View view) {
        home_item_layout_include1 = (LinearLayout) view.findViewById(R.id.home_item_layout_include1);
        return home_item_layout_include1;
    }

    @Override
    void setvalue(View view) {
        if (home_item_layout_include1 != null) {
            home_item_layout_include1.setVisibility(View.VISIBLE);
            setValueToView();
        }
    }

    void setValueToView() {
        home_item_top_layout_icon_customimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolsUtil.forwardActivity(activity, MarketMainActivity.class);
            }
        });

        setRoundImage("http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f08202f7fce43e49fbfbedab641b40.jpg");
        setIsAddressShow(true);
        setMainname("百丽商场");
        setMainDesc("地址");
        setDistance("4km", true);
        List list=new ArrayList();
        for(int i=0;i<10;i++)
        {
            list.add(null);
        }
        setBrand(list);
        setRecommendProduct(list,false);
        setCountDown("距离结束：23:23:23");
    }


}
