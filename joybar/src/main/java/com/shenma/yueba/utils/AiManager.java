package com.shenma.yueba.utils;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.shenma.yueba.https.CommonHttpControl;
import com.shenma.yueba.interfaces.HttpCallBackInterface;
import com.shenma.yueba.models.AliYunKeyBean;
import com.shenma.yueba.models.Request_AliYunKeyBackInfo;

/**
 * Created by Administrator on 2015/11/3.
 */
public class AiManager {


    /**
     * 获取阿里云需要的key和sign
     */
    public static void getKeyAndSignFromNetSetToLocal(final Context ctx) {

        CommonHttpControl.getALiYunKey(new HttpCallBackInterface<Request_AliYunKeyBackInfo>() {
            @Override
            public void http_Success(Request_AliYunKeyBackInfo bean) {
                if (bean != null && bean.getData() != null) {
                    AliYunKeyBean data = bean.getData();
                    if (data != null && data.getKey() != null) {
                        String[] keyAndSign = data.getKey().split(",");
                        if (keyAndSign != null && keyAndSign.length == 2) {
                            String[] keyArr = keyAndSign[0].split("=");
                            String[] signArr = keyAndSign[1].split("=");
                            if (keyArr != null && keyArr.length == 2) {
                                PerferneceUtil.setAliYunKey(ToolsUtil.nullToString(keyArr[1]));
                            }
                            if (signArr != null && signArr.length == 2) {
                                PerferneceUtil.setAliYunSign(ToolsUtil.nullToString(signArr[1]));
                            }
                            initAliOSS(ctx, ToolsUtil.nullToString(keyArr[1]), ToolsUtil.nullToString(signArr[1]));
                        }
                    } else {
                        Toast.makeText(ctx, "阿里云key获取失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {

            }
        });
    }


    /*********
     * 初始化阿里云
     *******/
    public static void initAliOSS(Context ctx, final String key, final String sign) {
        OSSService ossService = OSSServiceProvider.getService();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // 初始化设置
        ossService.setApplicationContext(ctx);
        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date, String ossHeaders,
                                        String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders + resource;

                return OSSToolKit.generateToken(key, sign, content);
            }
        });
        ossService.setGlobalDefaultHostId("oss-cn-beijing.aliyuncs.com");
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);
        ossService.setGlobalDefaultACL(AccessControlList.PUBLIC_READ); // 默认为private

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(15 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(15 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50
        ossService.setClientConfiguration(conf);

    }
}
