package com.shenma.yueba.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 * 城市列表 观察者模式 当 有需要通知的数据 更新时  调用refreshList 方法 所有的观察者将 接收到通知
 */
public class LoginRefreshObserver {
    static LoginRefreshObserver instance;
    private List<LoginSucessRefreshLinstener> mList = new ArrayList<LoginSucessRefreshLinstener>();

    private LoginRefreshObserver() {

    }

    public static LoginRefreshObserver getInstance() {
        if (instance == null) {
            instance = new LoginRefreshObserver();
        }
        return instance;
    }

    public void addObserver(LoginSucessRefreshLinstener loginSucessRefreshLinstener) {
        if (!mList.contains(loginSucessRefreshLinstener)) {
            mList.add(loginSucessRefreshLinstener);
        }
    }

    public void clearAllObserver() {
        mList.clear();
    }

    public void removeObserver(LoginSucessRefreshLinstener loginSucessRefreshLinstener) {
        if (mList.contains(loginSucessRefreshLinstener)) {
            mList.remove(loginSucessRefreshLinstener);
        }
    }

    public void NoticationLoginSucess() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).loginSucess();
        }
    }


    public interface LoginSucessRefreshLinstener {
        void loginSucess();
    }
}
