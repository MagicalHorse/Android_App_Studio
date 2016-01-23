package im.control;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.shenma.yueba.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

import im.form.BaseChatBean;
import im.form.RequestMessageBean;

/**
 * Created by Administrator on 2016/1/12.
 */
public class SocketObserverManager {
    public enum SocketObserverType {
        connectSucess,
        UnconnectSucess,
        roomMessage,
        OurRoomMessage,
        sendstauts//发送状态改变
    }
    Handler handler=null;
    private static SocketObserverManager socketObserverManager;

    public boolean isJoinRoom() {
        return isJoinRoom;
    }

    public void setIsJoinRoom(boolean isJoinRoom) {
        SocketObserverManager.isJoinRoom = isJoinRoom;
    }

    private static boolean isJoinRoom = false;//是否成功加入圈子
    List<SocketNoticationListener> list = new ArrayList<SocketNoticationListener>();

    public static SocketObserverManager getInstance() {
        if (socketObserverManager == null) {
            socketObserverManager = new SocketObserverManager();
            socketObserverManager.handler=new Handler(Looper.getMainLooper());
        }
        return socketObserverManager;
    }

    public void addSocketObserver(SocketNoticationListener listener) {
        if (!list.contains(listener)) {
            list.add(listener);
        }
    }

    public void removeSocketObserver(SocketNoticationListener listener) {
        if (list.contains(listener)) {
            list.remove(listener);
        }
    }


    public void clearSocketObserver() {
        list.clear();
    }

    public void Notication(final SocketObserverType status, final Object... obj) {

        for (int i = 0; i < list.size(); i++) {
            Activity activity = null;
            final SocketNoticationListener listener = list.get(i);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (status) {
                        case connectSucess:
                            listener.socketConnectSucess();
                            break;
                        case UnconnectSucess:
                            listener.socketConnectFails();
                            break;
                        case roomMessage:
                            if (obj != null && obj.length > 0) {
                                if (obj[0] instanceof RequestMessageBean) {
                                    listener.receiveMsgFromRoom((RequestMessageBean) obj[0]);
                                }
                            }
                            break;
                        case OurRoomMessage:
                            if (obj != null && obj.length > 0) {
                                if (obj[0] instanceof RequestMessageBean) {
                                    listener.receiveMsgFromUnRoom((RequestMessageBean) obj[0]);
                                }
                            }
                            break;
                        case sendstauts:
                            if (obj != null && obj.length > 0) {
                                if (obj[0] instanceof BaseChatBean) {
                                    listener.sendStatusChaneg((BaseChatBean) obj[0]);
                                }
                            }
                            break;
                    }
                }
            });

        }
    }

    public interface SocketNoticationListener {
        void socketConnectSucess();//服务器连接成功

        void socketConnectFails();//服务器连接失败

        void receiveMsgFromRoom(RequestMessageBean bean);//接收到房间消息

        void receiveMsgFromUnRoom(RequestMessageBean bean);//接收到不在房间的消息

        void sendStatusChaneg(Object obj);//发送状态改变
    }
}
