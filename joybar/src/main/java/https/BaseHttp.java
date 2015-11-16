package https;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.util.BaseGsonUtils;
import com.shenma.yueba.util.Md5Utils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import config.HttpPerderneceConfig;
import interfaces.HttpCallBackInterface;

public class BaseHttp {


    /***************************
     * post模式 传递 普通（k-v）数据
     *
     * @param map      Map<String, String> 参数 k-v
     * @param url      String 服务器地址
     * @param inteface HttpCallBackInterface 回调接口
     * @param classzz  Class<T> 结果返回的对象
     *****************************/
    public static <T extends BaseRequest> void postNasinameDataByHttp(Map<String, String> map, String url, HttpCallBackInterface inteface, Class<T> classzz) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = getRequestParamsValue(map, null);
        send(url, requestParams, inteface, classzz);
    }

    /***************************
     * post模式 传递 json 数据
     *
     * @param map      Map<String, String> 参数 k-v
     * @param url      String 服务器地址
     * @param json     String JOSN数据
     * @param inteface HttpCallBackInterface 回调接口
     * @param classzz  Class<T> 结果返回的对象
     *****************************/
    public static <T extends BaseRequest> void postJsonDataByHttp(Map<String, String> map, String url, String json, HttpCallBackInterface inteface, Class<T> classzz) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = getRequestParamsValue(map, json);
        try {
            requestParams.setBodyEntity(new StringEntity(json, "UTF-8"));
            send(url, requestParams, inteface, classzz);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /***************************
     * 访问网络
     *
     * @param url          String URl地址
     * @param params       RequestParams 传递的值
     * @param httpCallBack HttpCallBackInterface 回调接口
     * @param classzz      Class<T> 结果返回的对象
     *****************************/
    static <T extends BaseRequest> void send(final String url, final RequestParams params, final HttpCallBackInterface httpCallBack, final Class<T> classzz) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (httpCallBack != null) {
                    Log.i("result", responseInfo.result);
                    T bean = BaseGsonUtils.getJsonToObject(classzz, responseInfo.result);
                    if (bean == null) {
                        httpCallBack.http_Fails(0, "数据解析异常");

                    } else if (bean.getStatusCode() == 200) {
                        httpCallBack.http_Success(bean);
                    } else if (bean.getStatusCode() == 401)//如果token 失效
                    {
                        MyApplication.getInstance().startLogin(MyApplication.getInstance().getApplicationContext(), "登录已经失效,请重新登录");
                    } else {
                        httpCallBack.http_Fails(bean.getStatusCode(), bean.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (httpCallBack != null) {
                    if (error.getExceptionCode() == 0) {
                        httpCallBack.http_Fails(0, "网络异常，请检查网络");
                    } else {
                        httpCallBack.http_Fails(0, msg);
                    }

                }
            }
        });
    }


    /***************************
     * 根据k-v进行数据 参数 初始化
     *
     * @param map  Map<String, String>
     * @param json String 传递的json 数据 如果没有 设置null
     *****************************/
    static RequestParams getRequestParamsValue(Map<String, String> map, String json) {
        RequestParams requestParams = new RequestParams();
        if (map == null) {
            map = new HashMap<String, String>();
        }

        map.put(HttpPerderneceConfig.HTTPCHANNEL, HttpPerderneceConfig.ANDROID);
        map.put(HttpPerderneceConfig.CLIENTVERSION, ToolsUtil.getVersionCode(MyApplication.getInstance().getApplicationContext()));
        map.put(HttpPerderneceConfig.UUID, UUID.randomUUID().toString());
        String token=SharedUtil.getStringPerfernece(MyApplication.getInstance().getApplicationContext(), SharedUtil.user_token);
        if (token != null) {
            try {
                token = URLEncoder.encode(token, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        map.put(HttpPerderneceConfig.TOKEN, token);
        String md5str = Md5Utils.md5ToString(map, json);
        map.put(HttpPerderneceConfig.SIGN, md5str);
        Set<String> set = map.keySet();
        if (set != null && set.size() > 0) {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String k = iterator.next();
                if (map.containsKey(k)) {
                    String v = map.get(k);
                    requestParams.addBodyParameter(k, v);
                }

            }
        }
        return requestParams;
    }
}
