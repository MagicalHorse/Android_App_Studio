package com.shenma.yueba.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by a on 2015/11/16.
 */
public class TimerDownUtils {
	private long allTime = 10000;
	private TextView tv;
	private boolean hasStarted;
	private long startTime, finishTime;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {// 已经开始了
				finishTime -= 1000;
				if (finishTime > 0) {
					tv.setText("距离结束时间：" + millSecendToStr(finishTime));
				} else {
					tv.setText("活动已结束");
				}
			} else if (msg.what == 1) {// 还没有开始
				startTime -= 1000;
				if (startTime > 0) {
					tv.setText("距离开始时间：" + millSecendToStr(startTime));
				} else {
					if (finishTime > 0) {
						tv.setText("距离结束时间：" + millSecendToStr(finishTime));
						finishTime -= 1000;
					} else {
						tv.setText("活动已结束");
					}
				}

			}
		};
	};

	private static String millSecendToStr(long millSecond) {
		String hourStr, miniteStr, secondStr;
		long second = millSecond / 1000;
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

	public void timerDown(TextView tv, final boolean hasStarted,
						  long startTime, long finishTime) {
		this.tv = tv;
		this.hasStarted = hasStarted;
		this.startTime = startTime;
		this.finishTime = finishTime;
		Timer timer = new Timer();
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

}
