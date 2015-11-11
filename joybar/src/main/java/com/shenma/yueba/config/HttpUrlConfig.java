package com.shenma.yueba.config;

import com.shenma.yueba.https.HttpUrlFactory;

/**
 * Created by Administrator on 2015/11/3.
 */
public class HttpUrlConfig {
    /*****
     * 访问类型
     ***/
    public enum Http_Type {
        test,//测试环境
        develop,//开发环境
        online,//生产环境
    }


    //服务器类型
    public final static Http_Type http_Type = Http_Type.test;

    /*********
     * 写 数据
     ******/
    public static final String url_develop_write = "http://123.57.52.187:8080/app/";//开发环境
    public static final String url_online_write = "http://appw.joybar.com.cn/app/";//生产环境
    public static final String url_test_write = "http://123.57.77.86:8080/app/";//测试环境

    /*********
     * 读 数据
     ******/
    public static final String url_develop_read = "http://123.57.52.187:8080/app/";//开发环境
    public static final String url_online_read = "http://appr.joybar.com.cn/app/";//生产环境
    public static final String url_test_read = "http://123.57.77.86:8080/app/";//测试环境


    /*****
     * 操作类型
     ***/
    public enum Cotrol_Type {
        read,//读操作
        write//写操作
    }

    /*****
     * 功能类型
     ***/
    public enum Action_Type {
        User,//用户
        Common,//通用
        Address,//地址
        Brand,//品牌
        Product,//商品
        Assistant,//买手接口
        Buyer,//买手接口
        Order,//订单
        Community,//圈子
        Search,//搜索
        Promotion,//活动相关
    }


    public static String getUrlHead() {
        switch (http_Type) {
            case test:
                break;
            case develop:
                break;
            case online:
                break;
        }
        return "";
    }


    /*********
     * 获取阿里云key
     ****/
    public static String METHOD_GETALIYUNKEY = HttpUrlFactory.getReadUrlByActionType(Action_Type.Common) + "GetALiYunAccessKey";
    /*********
     * 根据经纬度 获取城市信息
     ****/
    public static String METHOD_GETCITYBYID = HttpUrlFactory.getReadUrlByActionType(Action_Type.Common) + "GetCityByCoord";

    /*********
     * 检查app版本号
     ****/
    public static String METHOD_VERSION_UPDATE = HttpUrlFactory.getReadUrlByActionType(Action_Type.Common) + "METHOD_VERSION_UPDATE";

    /*********
     * 获取城市列表
     ****/
    public static String GETALLSHOPPINGCITY = HttpUrlFactory.getReadUrlByActionType(Action_Type.Common) + "GetAllShoopingCity";


}
