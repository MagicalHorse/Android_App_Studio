package com.shenma.yueba.https;

import com.shenma.yueba.config.HttpPerderneceConfig;
import com.shenma.yueba.config.HttpUrlConfig;
import com.shenma.yueba.config.PerferneceConfig;
import com.shenma.yueba.interfaces.HttpCallBackInterface;
import com.shenma.yueba.models.Request_AliYunKeyBackInfo;
import com.shenma.yueba.models.Request_CheckAppVersionInfo;
import com.shenma.yueba.models.Request_CityInfo;
import com.shenma.yueba.models.Request_CityListInfo;
import com.shenma.yueba.utils.PerferneceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/3.
 */
public class CommonHttpControl extends BaseHttp {

    /**
     * 获取阿里云key
     *
     * @param httpCallBack HttpCallBackInterface 回调接口
     * @return void
     **/
    public static void getALiYunKey(final HttpCallBackInterface<Request_AliYunKeyBackInfo> httpCallBack) {
        /**
         * 获取阿里云key
         */
        postNasinameDataByHttp(null, HttpUrlConfig.METHOD_GETALIYUNKEY, httpCallBack, Request_AliYunKeyBackInfo.class);
    }

    /***********
     * 根据经纬度获取城市名称
     **********/
    public static void getCityNameByGPS(final HttpCallBackInterface<Request_CityInfo> httpCallBack) {
        String longitude = PerferneceUtil.getString(PerferneceConfig.LONGITUDE);
        String latitude = PerferneceUtil.getString(PerferneceConfig.LATITUDE);
        Map<String, String> map = new HashMap<String, String>();
        map.put(PerferneceConfig.LONGITUDE, longitude);//经度
        map.put(PerferneceConfig.LATITUDE, latitude);//维度
        postNasinameDataByHttp(map, HttpUrlConfig.METHOD_GETCITYBYID, httpCallBack, Request_CityInfo.class);

    }

    /*****************
     * 检查版本更新
     ***************/
    public static void checkAppVersion(final HttpCallBackInterface<Request_CheckAppVersionInfo> httpCallBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(HttpPerderneceConfig.TYPE, "0");//0表示android 1表示IOS
        CommonHttpControl.postNasinameDataByHttp(map, HttpUrlConfig.METHOD_VERSION_UPDATE, httpCallBack, Request_CheckAppVersionInfo.class);
    }


    /*****************
     * 获取城市列表
     ***************/
    public static void getShoppingCityList(final HttpCallBackInterface<Request_CityListInfo> httpCallBack)
    {
        Map<String, String> map = new HashMap<String, String>();
        CommonHttpControl.postNasinameDataByHttp(map, HttpUrlConfig.GETALLSHOPPINGCITY, httpCallBack, Request_CityListInfo.class);
    }
}
