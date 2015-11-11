package com.shenma.yueba.https;


import com.shenma.yueba.config.HttpUrlConfig;

/**
 * Created by Administrator on 2015/11/3.
 */
public class HttpUrlFactory {

    /*********
     * 根据 传递的访问的功能类型 获取 “ 读取数据的”  http url地址
     * @param action_type HttpUrlConfig.Action_Type 访问url 的类型
     * @return 返回 根据指定功能类型的url地址
     *********/
    public static String getReadUrlByActionType(HttpUrlConfig.Action_Type action_type) {
        String url=getUrlReadAddress();
        return getBaseHttpUrlByActionType(url,action_type);
    }

    /*********
     * 根据 传递的访问的功能类型 获取 “ 写入数据的”  http url地址
     * @param action_type HttpUrlConfig.Action_Type 访问url 的类型
     * @return 返回 根据指定功能类型的url地址
     *********/
    public static String getWriteUrlByActionType(HttpUrlConfig.Action_Type action_type) {
        String url=getUrlWriteAddress();
        return getBaseHttpUrlByActionType(url,action_type);
    }


    /*********
     * 根据 传递的访问的功能类型 获取http url地址
     * @param action_type HttpUrlConfig.Action_Type 访问url 的类型
     * @return 返回url地址
     *********/
    private static String getBaseHttpUrlByActionType(String url,final HttpUrlConfig.Action_Type action_type) {
        switch (action_type) {
            case User://用户
                url+= HttpUrlConfig.Action_Type.User.name();
                break;
            case Common://通用
                url+= HttpUrlConfig.Action_Type.Common.name();
                break;
            case Address://地址
                url+= HttpUrlConfig.Action_Type.Address.name();
                break;
            case Brand://品牌
                url+= HttpUrlConfig.Action_Type.Brand.name();
                break;
            case Product://商品
                url+= HttpUrlConfig.Action_Type.Product.name();
                break;
            case Assistant://买手接口
                url+= HttpUrlConfig.Action_Type.Assistant.name();
                break;
            case Buyer://买手接口
                url+= HttpUrlConfig.Action_Type.Buyer.name();
                break;
            case Order://订单
                url+= HttpUrlConfig.Action_Type.Order.name();
                break;
            case Community://圈子
                url+= HttpUrlConfig.Action_Type.Community.name();
                break;
            case Search://搜索
                url+= HttpUrlConfig.Action_Type.Search.name();
                break;
            case Promotion://活动相关
                url+= HttpUrlConfig.Action_Type.Promotion.name();
                break;
        }
        return url+="/";
    }



    /*********
     * 获取http读的地址
     *********/
    public static String getUrlReadAddress() {
        HttpUrlConfig.Cotrol_Type cotrol_type = HttpUrlConfig.Cotrol_Type.read;
        String baseurl = getBaseHttpUrlByControlType(cotrol_type);
        return baseurl;
    }


    /*********
     * 获取http写的地址
     *********/
    public static String getUrlWriteAddress() {
        HttpUrlConfig.Cotrol_Type cotrol_type = HttpUrlConfig.Cotrol_Type.write;
        String baseurl = getBaseHttpUrlByControlType(cotrol_type);
        return baseurl;
    }

    /*********
     * 根据 传递的服务器 类型 获取http url地址
     *
     * @param cotrol_type HttpUrlConfig.Cotrol_Type 访问url 的类型
     * @return 返回url地址
     *********/
    public static String getBaseHttpUrlByControlType(final HttpUrlConfig.Cotrol_Type cotrol_type) {
        HttpadstractUrladdress httpurl = null;
        String url = "";
        switch (HttpUrlConfig.http_Type) {
            case test:
                httpurl = new TestUrlAddress(cotrol_type);
                break;
            case develop:
                httpurl = new DevelopUrlAddress(cotrol_type);
                break;
            case online:
                httpurl = new OnlineUrlAddress(cotrol_type);
                break;
        }
        if (httpurl != null) {
            url = httpurl.getUrl();
        }
        return url;
    }
}
