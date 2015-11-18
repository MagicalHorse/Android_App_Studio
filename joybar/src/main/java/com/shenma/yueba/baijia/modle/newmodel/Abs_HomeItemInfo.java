package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.util.TimerDownUtils;

/**
 * Created by Administrator on 2015/11/9.
 */
public  class Abs_HomeItemInfo {
    long startTime;
    long endTime;
    HomeItemInfoListener listener;
    TimerDownUtils timerDownUtils=new TimerDownUtils();

    public String getShowstr() {
        return showstr;
    }

    public void setShowstr(String showstr) {
        this.showstr = showstr;
    }

    String showstr="";
    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    boolean hasStarted=false;
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {

        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void startTime()
    {
        timerDownUtils.timerDown(null, hasStarted, startTime, endTime, new TimerDownUtils.TimerCallListener() {
            @Override
            public void currTime(String str) {
                setShowstr(str);
                if(listener!=null)
                {
                    listener.callback();
                }
            }
        });
    }

    public void setTimerCallListener(HomeItemInfoListener listener)
    {
        this.listener=listener;
    }

    public interface HomeItemInfoListener
    {
        void callback();
    }
}
