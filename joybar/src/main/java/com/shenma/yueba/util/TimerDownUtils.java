package com.shenma.yueba.util;

import android.os.Handler;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a on 2015/11/16.
 */
public class TimerDownUtils implements Serializable{
    private long allTime = 10000;
    private boolean hasStarted;
    private long startTime, finishTime;



    TimerCallListener timerCallListener;
    Timer timer;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String str = "";
            if (msg.what == 0) {// 已经开始了
                finishTime -= 1000;
                if (finishTime > 0) {
                    str = "距离结束时间：" + millSecendToStr(finishTime);
                } else {
                    str = "活动已结束";
                }
            } else if (msg.what == 1) {// 还没有开始
                startTime -= 1000;
                if (startTime > 0) {
                    str = "距离开始时间：" + millSecendToStr(startTime);
                } else {
                    if (finishTime > 0) {
                        str = "距离结束时间：" + millSecendToStr(finishTime);
                        finishTime -= 1000;
                    } else {
                        str = "活动已结束";
                    }
                }

            }
            if (timerCallListener != null) {
                timerCallListener.currTime(str);
            }
        }

        ;
    };

    public static String millSecendToStr(long millSecond) {
        String hourStr, miniteStr, secondStr;
        long second = millSecond;
        long leftSecond = second % 60;
        long minite = second / 60;
        long leftHour = minite / 60;
        long leftMinite = minite % 60;
        if (String.valueOf(leftSecond).length() == 1) {
            secondStr = "0" + String.valueOf(leftSecond);
        } else {
            secondStr = String.valueOf(leftSecond);
        }
        if (String.valueOf(leftMinite).length() == 1) {
            miniteStr = "0" + String.valueOf(leftMinite);
        } else {
            miniteStr = String.valueOf(leftMinite);
        }
        if (String.valueOf(leftHour).length() == 1) {
            hourStr = "0" + String.valueOf(leftHour);
        } else {
            hourStr = String.valueOf(leftHour);
        }

        return hourStr + ":" + miniteStr + ":" + secondStr;
    }

    public void timerDown(TextView tv, final boolean hasStarted, long startTime, long finishTime,TimerCallListener timerCallListener) {
        this.timerCallListener = timerCallListener;
        this.hasStarted = hasStarted;
        this.startTime = startTime;
        this.finishTime = finishTime;
        stopTimer();
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if (hasStarted) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }

            }
        };
        timer.schedule(task, 0, 1000);
    }

    public interface TimerCallListener {
        void currTime(String str);
    }

    void stopTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
        }
        timer=null;
    }
}
