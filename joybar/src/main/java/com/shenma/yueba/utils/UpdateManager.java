package com.shenma.yueba.utils;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager implements View.OnClickListener {
    private Context mContext;
    NotificationManager notiManage;
    private RemoteViews contentView;
    private String newVersionPath;// 更新的新版本链接地址
    private String updateTitle;// 更新题目
    private String updateContent;// 更新内容
    private String NewVersion;// 传过来的新版本号
    /* 下载包安装路径 */
    private static final String savePath = AppConfig.SD;
    private static final String saveFileName = savePath + AppConfig.apkName;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;

    private ProgressBar progress_bar;// 更新进度条
    private AlertDialog promptdialog, downDialog;
    private ProgressDialog pd;

    public UpdateManager(Context context, String NewVersion,
                         String newVersionPath, String updateTitle, String updateContent) {
        this.newVersionPath = newVersionPath;
        this.updateTitle = updateTitle;
        this.updateContent = updateContent;
        this.mContext = context;
        this.NewVersion = NewVersion;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    if (downDialog.isShowing()) {
                        progress_bar.setProgress(progress);
                    } else {
                        showNotificationView();
                    }
                    break;
                case DOWN_OVER:
                    if (notiManage != null) {
                        notiManage.cancel(1);
                    }
                    // 安装
                    downDialog.dismiss();
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 显示升级提示框
     */
    public void startUpdate() {
        promptdialog = new AlertDialog.Builder(mContext).create();
        promptdialog.getWindow().setGravity(Gravity.CENTER);
        promptdialog.show();
        promptdialog.getWindow().setContentView(showNoticeDialog());
    }

    /**
     * 升级提示框view
     *
     * @return
     */
    private View showNoticeDialog() {
        LayoutInflater factory = LayoutInflater.from(mContext);
        View view = factory.inflate(R.layout.updateversion_layout, null);
        TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        title.setText("软件版本更新");
        content.setText(updateContent);
        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setText("确定");
        Button cancel = (Button) view.findViewById(R.id.canel);
        cancel.setText("取消");
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    /**
     * 显示升级中的进度
     *
     * @return
     */
    private View showDownloadDialog() {
        downloadApk();
        LayoutInflater factory = LayoutInflater.from(mContext);
        View view = factory.inflate(R.layout.down_progress_dialog, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView bt_quit = (TextView) view.findViewById(R.id.bt_quit);
        bt_quit.setOnClickListener(this);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress);
        // progress_bar.setProgress(50);
        tv_title.setText("更新中...");
        Button bt_backgroud = (Button) view.findViewById(R.id.bt_backgroud);
        bt_backgroud.setOnClickListener(this);
        return view;
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(newVersionPath);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6 * 1000);
                conn.connect();
                int length = conn.getContentLength();
                int reCode = conn.getResponseCode();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                // mHandler.sendEmptyMessage(DOWN_OVER);
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                promptdialog.cancel();
                downDialog = new AlertDialog.Builder(mContext).create();
                downDialog.getWindow().setGravity(Gravity.CENTER);
                downDialog.show();
                downDialog.getWindow().setContentView(showDownloadDialog());
                break;
            case R.id.canel:
                promptdialog.cancel();
                break;
            case R.id.bt_quit:// 停止下载
                interceptFlag = true;
                downDialog.dismiss();
                break;
            case R.id.bt_backgroud:// 后台运行
                downDialog.dismiss();
                showNotificationView();
                break;
            default:
                break;
        }
    }

    /**
     * 是否跟本地版本对应
     *
     * @param serviceVersion 服务器版本
     * @return
     */
    public boolean haveUpdate(String serviceVersion) {
        String localhostVersion = MyApplication.getInstance().getAppVersionNo();
        String[] sVersion = serviceVersion.split("\\.");
        if (sVersion.length != 3) {
            return false;
        }
        String[] lVersion = localhostVersion.split("\\.");
        if (lVersion.length != 3) {
            return true;
        }

        int sMajorVersion;
        int sMinorVersion;
        int sMiniVersion;
        try {
            sMajorVersion = Integer.parseInt(sVersion[0]);
            sMinorVersion = Integer.parseInt(sVersion[1]);
            sMiniVersion = Integer.parseInt(sVersion[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        int lMajorVersion;
        int lMinorVersion;
        int lMiniVersion;
        try {
            lMajorVersion = Integer.parseInt(lVersion[0]);
            lMinorVersion = Integer.parseInt(lVersion[1]);
            lMiniVersion = Integer.parseInt(lVersion[2]);
        } catch (NumberFormatException e) {
            return true;
        }

        if (sMajorVersion > lMajorVersion) {
            return true;
        } else if (sMajorVersion < lMajorVersion) {
            return false;
        }
        if (sMinorVersion < lMinorVersion) {
            return true;
        } else if (sMinorVersion < lMinorVersion) {
            return false;
        }
        if (sMiniVersion > lMiniVersion) {
            return true;
        } else if (sMiniVersion < lMiniVersion) {
            return false;
        }

        return false;
    }

    private void showNotificationView() {
        Notification note;
        notiManage = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        note = new Notification();
        note.flags = Notification.FLAG_AUTO_CANCEL;
        contentView = new RemoteViews(mContext.getPackageName(),
                R.layout.download_notification_layout);
        contentView
                .setTextViewText(R.id.tv_progress, "当前下载进度" + progress + "%");
        contentView.setProgressBar(R.id.progress_down, 100, progress, false);
        note.contentView = contentView;
        note.tickerText = "正在下载";
        note.icon = R.mipmap.icon;
        // PendingIntent p=PendingIntent.getActivity(mContext, 0, new
        // Intent(Intent.ACTION_VIEW), 0);//这个非要不可。
        // note.contentIntent=p;
        // note.setLatestEventInfo(TestMapActivity.this, "黄金版电子档",
        // "龙湖英雄在线",p);//如果要添加上面自定义的显示效果就不能添加此句。
        notiManage.notify(1, note);
    }

}