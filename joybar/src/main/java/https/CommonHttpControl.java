package https;


import android.app.Activity;

import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.newmodel.Request_AliYunKeyBackInfo;
import com.shenma.yueba.baijia.modle.newmodel.Request_CheckAppVersionInfo;
import com.shenma.yueba.baijia.modle.newmodel.Request_CityInfo;
import com.shenma.yueba.baijia.modle.newmodel.Request_CityListInfo;
import com.shenma.yueba.util.PerferneceUtil;

import java.util.HashMap;
import java.util.Map;

import config.HttpPerderneceConfig;
import config.HttpUrlConfig;
import config.PerferneceConfig;
import interfaces.HttpCallBackInterface;

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
    public static void getALiYunKey(Activity activity,final HttpCallBackInterface<Request_AliYunKeyBackInfo> httpCallBack) {
        /**
         * 获取阿里云key
         */
        postNasinameDataByHttp(activity,null, HttpUrlConfig.METHOD_GETALIYUNKEY, httpCallBack, Request_AliYunKeyBackInfo.class);
    }

    /***********
     * 根据经纬度获取城市名称
     **********/
    public static void getCityNameByGPS(Activity activity,final HttpCallBackInterface<Request_CityInfo> httpCallBack) {
        String longitude = PerferneceUtil.getString(PerferneceConfig.LONGITUDE);
        String latitude = PerferneceUtil.getString(PerferneceConfig.LATITUDE);
        Map<String, String> map = new HashMap<String, String>();
        map.put(PerferneceConfig.LONGITUDE, longitude);//经度
        map.put(PerferneceConfig.LATITUDE, latitude);//维度
        postNasinameDataByHttp(activity,map, HttpUrlConfig.METHOD_GETCITYBYID, httpCallBack, Request_CityInfo.class);

    }

    /*****************
     * 检查版本更新
     ***************/
    public static void checkAppVersion(Activity activity,final HttpCallBackInterface<Request_CheckAppVersionInfo> httpCallBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(HttpPerderneceConfig.TYPE, "0");//0表示android 1表示IOS
        CommonHttpControl.postNasinameDataByHttp(activity,map, HttpUrlConfig.METHOD_VERSION_UPDATE, httpCallBack, Request_CheckAppVersionInfo.class);
    }


    /*****************
     * 获取城市列表
     ***************/
    public static void getShoppingCityList(Activity activity,final HttpCallBackInterface<Request_CityListInfo> httpCallBack)
    {
        Map<String, String> map = new HashMap<String, String>();
        CommonHttpControl.postNasinameDataByHttp(activity,map, HttpUrlConfig.GETALLSHOPPINGCITY, httpCallBack, Request_CityListInfo.class);
    }

    /************************** 商品信息 ***********************************/
    /**
     * 设置收藏或取消收藏商品
     *
     * @param httpCallBack   HttpCallBackInterface 回调接口
     * @param productId String 商品编号
     * @param status String 商品编号 0表示取消收藏   1表示收藏
     * @return void
     * **/
    public static void setFavor(Activity activity,final String status, final String productId, final HttpCallBackInterface<BaseRequest> httpCallBack)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Id", productId);
        map.put("Status",status);
        CommonHttpControl.postNasinameDataByHttp(activity,map, HttpUrlConfig.METHOD_PRODUCTMANAGER_FAVOR, httpCallBack, BaseRequest.class);
    }
}
