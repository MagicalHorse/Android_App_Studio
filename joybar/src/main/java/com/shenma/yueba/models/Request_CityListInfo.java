package com.shenma.yueba.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 * 获取 城市列表 对象的 接收数据类
 */
public class Request_CityListInfo extends BaseRequest {
    List<CityListItembean> data = new ArrayList<CityListItembean>();
    public List<CityListItembean> getData() {
        return data;
    }

    public void setData(List<CityListItembean> data) {
        this.data = data;
    }


}
