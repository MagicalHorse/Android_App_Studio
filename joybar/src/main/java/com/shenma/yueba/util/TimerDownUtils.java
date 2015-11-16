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
     * ��������ַ�����Ȼ�󷵻ص���ʱ
     * @param millisecond
     */
    public void timerDown(final Context ctx,String millisecond){

       final long totalTime = Long.valueOf(millisecond);
        CountDownTimer timer = new CountDownTimer(totalTime,5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat format = new SimpleDateFormat("hhСʱmm����ss��");
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
