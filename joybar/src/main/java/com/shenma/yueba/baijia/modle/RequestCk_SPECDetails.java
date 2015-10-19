package com.shenma.yueba.baijia.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 * 专柜 商品 规格详情（根据商品id 获取商品的规格信息 请求对象 用于josn转换成对象）
 */
public class RequestCk_SPECDetails extends BaseRequest {
    List<ProductColorTypeBean> data=new ArrayList<ProductColorTypeBean>();//商品规格信息
    public List<ProductColorTypeBean> getData() {
        return data;
    }

    public void setData(List<ProductColorTypeBean> data) {
        this.data = data;
    }
}
