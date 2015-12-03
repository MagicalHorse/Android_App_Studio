package com.shenma.yueba.baijia.modle;

import com.shenma.yueba.util.TimerDownUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/12/3.
 */
public class CKProductCountDownBean {
    TimerLinstener timerLinstener;//设置时间回调

    public CKProductDeatilsInfoBean getCkProductDeatilsInfoBean() {
        return ckProductDeatilsInfoBean;
    }

    public void setCkProductDeatilsInfoBean(CKProductDeatilsInfoBean ckProductDeatilsInfoBean) {
        this.ckProductDeatilsInfoBean = ckProductDeatilsInfoBean;
    }

    CKProductDeatilsInfoBean ckProductDeatilsInfoBean=new CKProductDeatilsInfoBean();
    long tem_BusinessTime;
    long tem_RemainTime;
    long DYGTime;
    long tmpBusinessTime;
    boolean isRunning = false;
    String showstr = "";//显示倒计时时间
    boolean isDayangGou=false;
    public boolean isDayangGou() {
        return isDayangGou;
    }

    public void startTimer() {
        if (isRunning) {
            return;
        }
        if(ckProductDeatilsInfoBean.BusinessTime<=0)
        {
            return;
        }
        tem_BusinessTime = ckProductDeatilsInfoBean.BusinessTime;
        tem_RemainTime = ckProductDeatilsInfoBean.RemainTime;
        DYGTime = 24 * 3600 - ckProductDeatilsInfoBean.BusinessTime;
        tmpBusinessTime = ckProductDeatilsInfoBean.BusinessTime;
        isDayangGou=ckProductDeatilsInfoBean.IsStart;

        isRunning = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                jisuan();
            }
        }, 0, 1000);
    }

    public void setTimerLinstener(TimerLinstener timerLinstener) {
        this.timerLinstener = timerLinstener;
    }


    void jisuan() {
        //如果已经开始
        if (ckProductDeatilsInfoBean.IsStart) {
            if (tem_RemainTime > 0) {
                isDayangGou=true;
                tem_RemainTime--;
                showstr = TimerDownUtils.millSecendToStr(tem_RemainTime);
                if (timerLinstener != null) {
                    timerLinstener.timerCallBack(showstr);
                }
            } else {
                if (tmpBusinessTime > 0) {
                    isDayangGou=false;
                    tmpBusinessTime--;
                    showstr = TimerDownUtils.millSecendToStr(tmpBusinessTime);
                    if (timerLinstener != null) {
                        timerLinstener.timerCallBack(showstr);
                    }
                } else {
                    if (DYGTime > 0) {
                        isDayangGou=true;
                        DYGTime--;
                        showstr = TimerDownUtils.millSecendToStr(DYGTime);
                        if (timerLinstener != null) {
                            timerLinstener.timerCallBack(showstr);
                        }
                    }else{
                        isDayangGou=false;
                        tmpBusinessTime=ckProductDeatilsInfoBean.BusinessTime;
                        DYGTime=24 * 3600 - ckProductDeatilsInfoBean.BusinessTime;
                    }
                }
            }


        } else {

            if (tem_RemainTime > 0) {
                isDayangGou=false;
                tem_RemainTime--;
                showstr = TimerDownUtils.millSecendToStr(tem_RemainTime);
                if (timerLinstener != null) {
                    timerLinstener.timerCallBack(showstr);
                }
            }else
            {
                if (DYGTime > 0) {
                    isDayangGou=true;
                    DYGTime--;
                    showstr = TimerDownUtils.millSecendToStr(DYGTime);
                    if (timerLinstener != null) {
                        timerLinstener.timerCallBack(showstr);
                    }
                }else
                {
                    if(tmpBusinessTime>0)
                    {
                        isDayangGou=false;
                        tmpBusinessTime--;
                        showstr = TimerDownUtils.millSecendToStr(tmpBusinessTime);
                        if (timerLinstener != null) {
                            timerLinstener.timerCallBack(showstr);
                        }
                    }else
                    {
                        isDayangGou=true;
                        DYGTime=24 * 3600 - ckProductDeatilsInfoBean.BusinessTime;
                        tmpBusinessTime=ckProductDeatilsInfoBean.BusinessTime;
                    }
                }
            }
        }
    }

    public interface TimerLinstener {
        void timerCallBack(String str);
    }
}
