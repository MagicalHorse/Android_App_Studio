package com.shenma.yueba.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.shenma.yueba.ChatActivity;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaijiaPayActivity;
import com.shenma.yueba.baijia.activity.BaijiaProductInfoActivity;
import com.shenma.yueba.baijia.activity.CircleInfoActivity;
import com.shenma.yueba.baijia.activity.MyDefaultCircleActivity;
import com.shenma.yueba.baijia.activity.ShopMainActivity;
import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.PayResponseFormBean;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.yangjia.modle.AliYunKeyBackBean;
import com.shenma.yueba.yangjia.modle.AliYunKeyBean;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import im.form.MessageUserInfoBean;
import im.form.RequestMessageBean;

public class ToolsUtil {
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    private static double EARTH_RADIUS = 6378.137;
    public static int widthPixels;
    public static int heightPixels;

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 验证手机号
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || "".equals(phone)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证邮编
     */
    public static boolean checkCode(String code) {
        if (code == null || "".equals(code)) {
            return false;
        }
        String strPattern = "[1-9]\\d{5}(?!\\d)";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(code);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证Email 邮箱
     */
    public static boolean checkEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static int dip(Context context, int pixels) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        System.out.println(displayMetrics.density);
        System.out.println(displayMetrics.densityDpi);
        System.out.println(displayMetrics.xdpi);
        System.out.println(displayMetrics.ydpi);

        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        System.out.println(displayMetrics.widthPixels);
        System.out.println(displayMetrics.heightPixels);

        return (int) (pixels * scale + 0.5f);

    }

    /**
     * 获取当前页面的屏幕宽度
     *
     * @param cx
     * @return
     */
    public static int getDisplayWidth(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取当前页面的屏幕高度
     *
     * @param cx
     * @return
     */
    public static int getDisplayHeight(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 刚进入界面时，隐藏软键盘
     *
     * @param context 上下文
     */
    public static void hideSoftInputKeyBoard(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 判断空间是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isAvailableSpace(Context mContext) {
        if (isMounted(mContext) && isEnoughSpace(mContext)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMounted(Context mContext) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        Toast.makeText(mContext, "SD卡不可用", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 空闲区限制不能小于5M
     *
     * @param mContext
     * @return
     */
    public static boolean isEnoughSpace(Context mContext) {
        if (getSDFreeSize() < 5) {
            Toast.makeText(mContext, "存储空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long freeBlocks = sf.getAvailableBlocks();
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 正则表达式抽取内容中的图片本地地址
     */
    public static List<String> convertNormalStringToSpannableString(String content) {
        List<String> listImg = new ArrayList<String>();
        Matcher localMatcher = EMOTION_URL.matcher(content);
        while (localMatcher.find()) {
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k > 4) {
                listImg.add(content.substring(k + 1, m - 1));
            }
        }
        return listImg;
    }

    public static int getImgCountFromContent(String contentStr) {
        return convertNormalStringToSpannableString(contentStr).size();
    }

    public static String nullToString(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 解析表情
     *
     * @param message 传入的需要处理的String
     * @return
     */
    public static CharSequence analysisFace(Context context, String message) {
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {
                if (MyApplication.getInstance().getFaceMap().containsKey(str2)) {
                    int face = MyApplication.getInstance().getFaceMap().get(str2);
                    Drawable d = context.getResources().getDrawable(face);
                    if (d != null) {
                        d.setBounds(0, 0, 45, 45);// 设置表情图片的显示大小
                        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                        value.setSpan(span, k, m, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }

    /**
     * 获取当前时间的字符串
     *
     * @return
     */
    public static String getCurrentTime() {
        return dateToStrLong(new Date(System.currentTimeMillis()));
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 设置textView字体颜色
     *
     * @param tv     textView控件
     * @param str1   第一个字段
     * @param color1 第一个字段颜色
     * @param str2   第二个字段
     * @param color2 第二个字段颜色
     */
    public static void setTextColor(TextView tv, String str1, String color1, String str2, String color2) {
        tv.setText(Html.fromHtml("<font color=\"" + color1 + "\">" + str1 + "</font>" + "<font color=\"" + color2
                + "\">" + str2 + "</font>"));
    }


    /**
     * 设置红色字在前，黑色字在后
     *
     * @param tv        TextView控件
     * @param redStr    红颜色字
     * @param normalStr 黑颜色字
     */
    public static void setTextColorRedAndBlack(TextView tv, String redStr, String normalStr) {
        tv.setText(Html.fromHtml(
                "<font color=\"#C60606\">" + redStr + "</font>" + "<font color=\"#000000\">" + normalStr + "</font>"));
    }

    /**
     * 选中的文字变色
     *
     * @param tv TextView控件
     *           红颜色字
     *           黑颜色字
     */
    public static void setKeyColor(TextView tv, String normalBegin, String redCenter, String normalEnd) {
        tv.setText(Html.fromHtml("<font color=\"#000000\">" + normalBegin + "</font>" + "<font color=\"#C60606\">"
                + redCenter + "</font>" + "<font color=\"#000000\">" + normalEnd + "</font>"));
    }

    /**
     * 设置黑色字在前，红色字在后
     *
     * @param tv
     * @param normalStr 黑颜色字
     * @param redStr    红颜色字
     */
    public static void setTextColorBlackAndRed(TextView tv, String normalStr, String redStr) {
        tv.setText(Html.fromHtml(
                "<font color=\"#000000\">" + normalStr + "</font>" + "<font color=\"#C60606\">" + redStr + "</font>"));
    }

    /**
     * 设置字体颜色为红色
     *
     * @param tv 红颜色字
     */
    public static void setTextColorRed(TextView tv, String redStr) {
        tv.setText(Html.fromHtml("<font color=\"#C60606\">" + redStr + "</font>"));
    }

    /**
     * 获取红色的字体
     *
     * @param redStr 红颜色字
     */
    public static String getTextColorRed(String redStr) {
        return "<font color=\"#C60606\">" + redStr + "</font>";
    }

    public static void saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 传入大小获取网络图片
     *
     * @return
     */
    public static String getImage(String url, int with, int height) {
        return url;
    }


    /******
     * 设置字体样式
     *
     * @param context Context
     * @param v       View 父视图
     * @param id      int 视图的资源id(为TextVIEW本身及子类)
     * @param str     String 值 可以传NULL（null 时不负值）
     ****/
    public static void setFontStyle(Context context, View v, int id, String str) {
        View textview = v.findViewById(id);
        if (textview != null && textview instanceof TextView) {
            FontManager.changeFonts(context, textview);
            if (str != null) {
                ((TextView) textview).setText(str);
            }
        }
    }

    /******
     * 设置字体样式
     *
     * @param context Context
     * @param v       View 父视图
     * @param id      int 视图的资源id(为TextVIEW本身及子类)泛型
     ****/
    public static void setFontStyle(Context context, View v, int... id) {
        for (int i = 0; i < id.length; i++) {
            View textview = v.findViewById(id[i]);
            if (textview != null && textview instanceof TextView || textview != null && textview instanceof Button
                    || textview != null && textview instanceof EditText) {
                FontManager.changeFonts(context, textview);
            }
        }

    }


    /**
     * 获取阿里云需要的key和sign
     */
    public static void getKeyAndSignFromNetSetToLocal(final Context ctx) {
        HttpControl httpControl = new HttpControl();
        httpControl.getALiYunKey(new HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                AliYunKeyBackBean bean = (AliYunKeyBackBean) obj;
                if (bean != null && bean.getData() != null) {
                    AliYunKeyBean data = bean.getData();
                    if(data!=null)
                    {
                        String key=data.getAccessKey();
                        String sign=data.getAccessKeySecret();
                        String ali_key=DES3Manager.decodeByDes3(key);
                        String ali_sign=DES3Manager.decodeByDes3(sign);
                        if(ali_key!=null && ali_sign!=null)
                        {
                            SharedUtil.setAliYunKey(ctx, ali_key);
                            SharedUtil.setAliYunSign(ctx, ali_sign);
                            initAliOSS(ctx,ali_key,ali_sign);

                        }else
                        {
                            Toast.makeText(ctx, "阿里云key获取失败", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        Toast.makeText(ctx, "阿里云key获取失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {


            }
        }, ctx);
    }

    @SuppressLint("NewApi")
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

    public static void hideSoftKeyboard(Context context, View view) {
        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        // 打开软键盘
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /*******
     * 返回带小数点后两位的值
     *
     * @param d double 数据
     * @return String
     **/
    public static String DounbleToString_2(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /***
     * 调用系统呼叫页面
     **/
    public static void callActivity(Context context, String phone) {
        // 调用拨号键
        Uri telUri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
        context.startActivity(intent);
    }

    /**
     * 判断小数是否合法
     *
     * @param price
     * @return
     */
    public static boolean isDecimal(String price) {
        try {
            double priceDouble = Double.valueOf(price);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /**
     * 是否是两位小数
     *
     * @return
     */
    public static boolean isPointTwo(String price) {
        if (price.contains(".")) {
            String[] priceStr = price.split("\\.");
            if (priceStr[1].length() > 2) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / scale) + 0.5f);
    }

    /**
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        if(activity==null)
        {
            return  statusHeight;
        }
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /****
     * 显示 或隐藏 无数据试图
     *
     * @param activity Activity 俯视图
     * @param status   boolean 是否显示 true 显示 false 不显示
     ***/
    public static void showNoDataView(final Activity activity, final boolean status) {
        Log.i("request", "------------->>>> showNoDataView:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " activity:" + activity + "  status:" + status);
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    View v = activity.findViewById(R.id.nodata_layout_textview);
                    if (v != null) {
                        FontManager.changeFonts(activity, v);
                        if (status) {
                            v.setVisibility(View.VISIBLE);
                        } else {
                            v.setVisibility(View.GONE);
                        }
                    }

                }
            });
        }
    }

    /****
     * 显示 或隐藏 无数据试图
     *
     * @param activity Activity 俯视图
     * @param status   boolean 是否显示 true 显示 false 不显示
     ***/
    public static void showNoDataView(final Activity activity, final View parentView, final boolean status) {
        Log.i("request", "------------->>>> showNoDataView:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " activity:" + activity + "  status:" + status);
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (parentView != null) {
                        View v = parentView.findViewById(R.id.nodata_layout_textview);
                        if (v != null) {
                            FontManager.changeFonts(activity, v);
                            if (status) {
                                v.setVisibility(View.VISIBLE);
                            } else {
                                v.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置订单个数
     */
    public static String setOrderCount(String count) {
        if (!TextUtils.isEmpty(count) && count.length() >= 3) {
            return "...";
        }
        return count;
    }

    public static String getVersionCode(Context ctx) {
        PackageInfo info = null;
        try {
            info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info.versionCode + "";
    }

    public static String getVersionName(Context ctx) {
        PackageInfo info = null;
        try {
            info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info.versionName;
    }

    public static void initPullResfresh(final PullToRefreshListView pullListView, final Activity activity) {
        Log.i("request", "------------->>>> initPullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " activity:" + activity);
        if (pullListView != null && activity != null) {
            pullListView.setOnPullEventListener(new OnPullEventListener<ListView>() {

                @Override
                public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {

                    // 设置标签显示的内容
                    if (direction == Mode.PULL_FROM_START) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Refreshonstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.Refreshloadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentherefresh));
                    } else if (direction == Mode.PULL_FROM_END) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Thedropdownloadstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.RefreshLoadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentheloadstr));
                    }
                }
            });
        }
    }

    public static void initPullResfresh(final PullToRefreshGridView pullListView, final Activity activity) {
        Log.i("request", "------------->>>> initPullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " activity:" + activity);
        if (pullListView != null && activity != null) {
            pullListView.setOnPullEventListener(new OnPullEventListener<GridView>() {

                @Override
                public void onPullEvent(PullToRefreshBase<GridView> refreshView, State state, Mode direction) {
                    // 设置标签显示的内容
                    if (direction == Mode.PULL_FROM_START) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Refreshonstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.Refreshloadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentherefresh));
                    } else if (direction == Mode.PULL_FROM_END) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Thedropdownloadstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.RefreshLoadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentheloadstr));
                    }
                }
            });
        }
    }

    public static void initPullResfresh(final PullToRefreshScrollView pullListView, final Activity activity) {
        Log.i("request", "------------->>>> initPullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  activity：" + activity);
        if (pullListView != null && activity != null) {
            pullListView.setOnPullEventListener(new OnPullEventListener<ScrollView>() {

                @Override
                public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, State state, Mode direction) {
                    // 设置标签显示的内容
                    if (direction == Mode.PULL_FROM_START) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Refreshonstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.Refreshloadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentherefresh));
                    } else if (direction == Mode.PULL_FROM_END) {
                        pullListView.getLoadingLayoutProxy()
                                .setPullLabel(activity.getResources().getString(R.string.Thedropdownloadstr));
                        pullListView.getLoadingLayoutProxy()
                                .setRefreshingLabel(activity.getResources().getString(R.string.RefreshLoadingstr));
                        pullListView.getLoadingLayoutProxy()
                                .setReleaseLabel(activity.getResources().getString(R.string.Loosentheloadstr));
                    }
                }
            });
        }
    }

    public static void pullResfresh(final PullToRefreshListView pullListView) {
        Log.i("request", "------------->>>> pullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  pullListView:" + pullListView);
        if (pullListView != null) {
            pullListView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pullListView.onRefreshComplete();
                }
            }, 100);
        }
    }

    public static void pullResfresh(final PullToRefreshScrollView pullListView) {
        Log.i("request", "------------->>>> pullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  pullListView:" + pullListView);
        if (pullListView != null) {
            pullListView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pullListView.onRefreshComplete();
                }
            }, 100);
        }
    }

    public static void pullResfresh(final PullToRefreshGridView pullListView) {
        Log.i("request", "------------->>>> pullResfresh:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  pullListView:" + pullListView);
        if (pullListView != null) {
            pullListView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pullListView.onRefreshComplete();
                }
            }, 500);
        }
    }


    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return true 在前台  false不在前台
     */
    public static boolean isApplicationBroughtToBackground(final String packagename) {
        ActivityManager am = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            String name = topActivity.getClassName().toString();
            if (name.equals(packagename)) {
                return true;
            }
        }
        return false;
    }


    /*****
     * 发送 接收到新消息广播  （ 广播通知 显示 状态栏通知）
     ***/
    public static void sendNoticationBroadcase(RequestMessageBean requestMessageBean) {
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("type", "14");
            jsonobject.put("roomid", requestMessageBean.getRoomId());
            jsonobject.put("fromuserid", requestMessageBean.getFromUserId());
            String nickname="";
            if(requestMessageBean.getUser()!=null)
            {
                MessageUserInfoBean userbean=requestMessageBean.getUser();
                nickname=userbean.getNickName();
            }
            jsonobject.put("username",nickname);
            Intent notificationintent = new Intent(JPushInterface.ACTION_MESSAGE_RECEIVED);
            notificationintent.putExtra(JPushInterface.EXTRA_MESSAGE, requestMessageBean.getBody());
            notificationintent.putExtra(JPushInterface.EXTRA_TITLE,nickname);
            notificationintent.putExtra(JPushInterface.EXTRA_EXTRA, jsonobject.toString());
            //如果消息免打扰  没有开启 则 弹出 通知
            if (!SharedUtil.getBooleanPerfernece(MyApplication.getInstance().getApplicationContext(), SharedUtil.user_canPush)) {
                MyApplication.getInstance().getApplicationContext().sendBroadcast(notificationintent);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /*********
     * 跳转 默认圈子
     *
     * @param activity     Activity
     * @param buyerId int 用户id()用于根据用户id 获取默认圈子
     *
     ******/
    public static void forwardMyDefaultCircleActivity(Activity activity,int buyerId)
    {
        Intent intent = new Intent(activity, MyDefaultCircleActivity.class);
        intent.putExtra("buyerId", buyerId);
        activity.startActivity(intent);

    }

    /******
     * 进入个人店铺首页
     * @param id int
     ****/
    public static void forwardShopMainActivity(Context ctx, int id) {
        Intent intent = new Intent(ctx, ShopMainActivity.class);
        intent.putExtra("buyerId", id);
        ctx.startActivity(intent);
    }


    /******
     * 进入默认圈子
     * @param id int
     ****/
    public static void forwardShopMainCircleActivity(Context ctx, int id) {
        Intent intent = new Intent(ctx, ShopMainActivity.class);
        intent.putExtra("buyerId", id);
        intent.putExtra("Type","Circle");
        ctx.startActivity(intent);
    }


    /*****
     * 跳转到 私聊聊天界面
     * @param Chat_NAME             String  圈子名称  没有 传 null
     * @param toUser_id             int 私聊对象id  没有 传 0
     ****/
    public static void forwardChatActivity(Context ctx,String Chat_NAME, int toUser_id) {
        MyApplication.getInstance().finishActivity(MyDefaultCircleActivity.class);
        Intent intent = new Intent(ctx, MyDefaultCircleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("chatType", "Private");//跳转类型
        intent.putExtra("Chat_NAME", Chat_NAME);
        intent.putExtra("toUser_id", toUser_id);
        ctx.startActivity(intent);
    }

    /*****
     * 跳转到 私聊聊天界面 带 商品信息
     * @param Chat_NAME             String  圈子名称  没有 传 null
     * @param toUser_id             int 私聊对象id  没有 传 0
     * @param bean                  RequestProductDetailsInfoBean 商品信息 没有穿null
     * @param requestCk_SPECDetails RequestCk_SPECDetails 商品的尺寸信息  没有传NULL
     ****/
    public static void forwardChatActivity(Context ctx,String Chat_NAME, int toUser_id, RequestCKProductDeatilsInfo bean, RequestCk_SPECDetails requestCk_SPECDetails) {
        MyApplication.getInstance().finishActivity(MyDefaultCircleActivity.class);
        Intent intent = new Intent(ctx, MyDefaultCircleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("chatType", "Private");//跳转类型
        intent.putExtra("Chat_NAME", Chat_NAME);
        intent.putExtra("toUser_id", toUser_id);
        intent.putExtra("DATA", bean);//商品信息
        intent.putExtra("standard_data", requestCk_SPECDetails);//尺寸
        ctx.startActivity(intent);
    }



    /*****
     * 跳转到 圈子聊天界面
     * @param circleId              int 圈子id  没有 传 0
     ****/
    public static void forwardCircleChatActivity(Context ctx,String Chat_NAME,int circleId) {
        MyApplication.getInstance().finishActivity(MyDefaultCircleActivity.class);
        Intent intent = new Intent(ctx, MyDefaultCircleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("chatType", "CircleID");//跳转类型
        intent.putExtra("Chat_NAME", Chat_NAME);
        intent.putExtra("circleId", circleId);
        ctx.startActivity(intent);
    }

    /*****
     * 跳转到 默认圈子聊天界面
     * @param buyerId              买手id
     ****/
    public static void forwardDefaultCircleActivity(Context ctx,int buyerId) {
        MyApplication.getInstance().finishActivity(MyDefaultCircleActivity.class);
        Intent intent = new Intent(ctx, MyDefaultCircleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("chatType", "DefaultCircle");//跳转类型
        intent.putExtra("buyerId", buyerId);
        ctx.startActivity(intent);
    }


    /******
     * 跳转到商品详情
     *
     * @param id int
     ****/
    public static void forwardProductInfoActivity(Context ctx, int id) {
        Intent intent = new Intent(ctx, BaijiaProductInfoActivity.class);
        intent.putExtra("productID", id);
        ctx.startActivity(intent);
    }

    /******
     * 跳转到圈子详情
     *
     * @param id          int
     * @param requestCode int 是否需要返回数据 resultCode>0 将以startActivityForResult 方式启动   否则 以普通方式启动
     ****/
    public static void forwardCircleActivity(Context ctx, int id, int requestCode) {
        Intent intent = new Intent(ctx, CircleInfoActivity.class);
        intent.putExtra("circleId", id);
        if (requestCode > 0) {
            ((Activity) ctx).startActivityForResult(intent, requestCode);
        } else {
            ctx.startActivity(intent);
        }

    }

    public static void forwardActivity(Context activity, Class classz) {
        Intent intent = new Intent(activity, classz);
        activity.startActivity(intent);
    }


    /**
     * 跳转到微信
     *
     * @param context
     */
    public static void skipToWechat(Context context) {
        ComponentName componetName = new ComponentName(
                // 这个是另外一个应用程序的包名
                "com.tencent.mm",
                // 这个参数是要启动的Activity
                "com.tencent.mm.ui.LauncherUI");
        try {
            Intent intent = new Intent();
            intent.setComponent(componetName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context,
                    "亲爱的用户您好，您的手机没有安装微信或者微信版本过低！", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    /********
     * 分享
     *
     * @param content String 内容提示
     * @param url     String 链接地址
     * @param icon    String 图片地址
     ****/
    public static void shareUrl(final Activity activity, final int productid, String title, String content, String url, String icon) {
        ShareUtil.shareAll(activity, title, content, url, icon, new ShareUtil.ShareListener() {

            @Override
            public void sharedListener_sucess() {
                requestShared(activity, productid);
            }

            @Override
            public void sharedListener_Fails(String msg) {
                MyApplication.getInstance().showMessage(activity, msg);
            }
        });
    }


    /*****
     * 分享成功后 回调
     ****/
    public static void requestShared(final Activity activity, int productid) {
        HttpControl httpControl = new HttpControl();
        httpControl.createProductShare(productid, false,
                new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        if (obj != null && obj instanceof BaseRequest) {
                            MyApplication.getInstance().showMessage(activity,
                                    "分享成功");
                        } else {
                            /*MyApplication.getInstance().showMessage(activity,"分享失败");*/
                        }
                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(activity, msg);
                    }
                }, activity);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

   /* *//*******
     * @param  lat1 纬度
     * @param  lng1 经度
     * @param  lat2 纬度
     * @param  lng2 经度
     * 根据 经纬度 计算 距离
     * *****//*
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }*/


    /*********
     * 跳转 支付选页面择
     *
     * @param activity     Activity
     * @param producetName String商品名称
     * @param buycount     int 购买数量
     * @param orderNo      String 订单号
     * @param price        double 订单金额
     ******/
    public static void frowardPayActivity(Activity activity, String producetName, int buycount, String orderNo, double price) {
        PayResponseFormBean bean = new PayResponseFormBean();
        bean.setContent(producetName);
        bean.setDesc(producetName + "  x " + buycount);
        bean.setOrderNo(orderNo);
        bean.setPrice(price);
        bean.setUrl(com.shenma.yueba.constants.Constants.WX_NOTIFY_URL);
        Intent intent = new Intent(activity, BaijiaPayActivity.class);
        intent.putExtra("PAYDATA", bean);
        activity.startActivity(intent);
    }


    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param long1 第一点经度
     * @param lat1  第一点纬度
     * @param long2 第二点经度
     * @param lat2  第二点纬度
     * @return 返回距离 单位：米
     */
    public static String Distance(double long1, double lat1, double long2, double lat2) {
        if (long1 < 0 || lat1 <0 || long2 <0 || lat2 < 0) {
            return "";
        }
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        d=Math.abs(d/1000);
        return ToolsUtil.DounbleToString_2(d)+"km";
    }

}
