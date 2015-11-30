package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/10/19.
 * 获取 商品详情 请求信息对象
 * 从服务器端 获取到的json 数据 转换成 对象
 */
public class RequestCKProductDeatilsInfo extends BaseRequest{
    CKProductDeatilsInfoBean  data=new CKProductDeatilsInfoBean();//商品详情对象
    public CKProductDeatilsInfoBean getData() {
        return data;
    }

    public void setData(CKProductDeatilsInfoBean data) {
        this.data = data;
    }
}
