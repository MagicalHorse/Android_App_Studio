package com.shenma.yueba.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a on 2015/11/16.
 */
public class TimerDownUtils {


    /**
     * 传入毫秒字符串，然后返回倒计时
     * @param millisecond
     */
    public void timerDown(final Context ctx,String millisecond){

       final long totalTime = Long.valueOf(millisecond);
        CountDownTimer timer = new CountDownTimer(totalTime,5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat format = new SimpleDateFormat("hh小时mm分钟ss秒");
                Date date = new Date(totalTime-1000);
                String aa = format.format(date);
                Toast.makeText(ctx,aa,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {

            }
        };
    }
}
