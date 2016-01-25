package im.control;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.receiver.ConnectionChangeReceiver;
import com.shenma.yueba.util.BaseGsonUtils;
import com.shenma.yueba.util.Md5Utils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.ToolsUtil;

import org.apache.http.conn.ClientConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import im.db.ImDataBaseManager;
import im.form.*;


/****
 * 通信管理 本类定义 im 通信 管理类 提供  链接服务器  断开服务器   进入房间 及 发送消息等方法
 **/
public class SocketManger {
    static Socket socket;
    static SocketManger socketManger;
    //final String URL = "2".equals(Constants.PublishStatus)?"http://chat.joybar.com.cn/chat?userid=":"http://182.92.7.70:8000/chat?userid=";//服务器地址
    final String URL = "http://182.92.7.70:8000/chat?";
    String userId = null;
    RoomBean roomBean = null;
    int maxCount=3;//设置重新连接的最大次数
    int currCount=0;
    private SocketManger() {
    }

    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /****
     * 获取  SocketManger 对象
     ****/
    public static SocketManger the() {
        if (socketManger == null) {
            socketManger = new SocketManger();
        }
        socketManger.contentSocket();
        return socketManger;
    }

    /***
     * 建立通信连接
     ***/
    public synchronized void contentSocket() {
        //如果对象为null 或者 当前没有链接  则进行链接
        if(!ToolsUtil.isNetworkConnected(MyApplication.getInstance()))
        {
            return;
        }
        if(socket==null)
        {
            try {
                //获取当前用户的userID
                String useriD= SharedUtil.getStringPerfernece(MyApplication.getInstance().getApplicationContext(), SharedUtil.user_id);
                if(useriD==null || useriD.equals(""))
                {
                    Log.i("TAG", "---->>>socket create  useriD:"+useriD);
                    return;
                }
                if(currCount>maxCount)
                {
                    Log.i("TAG", "---->>>socket 重新建立连接等待15000");
                    currCount=0;
                    SystemClock.sleep(15000);
                }

                String timestamp=Long.toString(new Date().getTime());
                String appid="maishouapp";
                String appsecret="maishouapp";
                String sign=useriD+timestamp+appid+appsecret;
                String http_url=URL+"userid="+useriD+"&timestamp="+timestamp+"&appid="+appid+"&sign="+ Md5Utils.encodeByMD5(sign).toLowerCase();
                //String http_url=URL+useriD;
                Log.i("TAG", "---->>>socket create  http_url:"+http_url);
                socket = IO.socket(http_url);
                //注销事件监听
                unsetListtener();
                //设置事件监听
                setListtener();
                //链接
                socket.connect();
                currCount++;
                Log.i("TAG", "---->>>socket create");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("TAG", "---->>>socket error"+e.getMessage());
            }
        }else if(!socket.connected())
        {
            Log.i("TAG", "---->>>socket unconnect");
            //socket.close();
            if(currCount>maxCount)
            {
                Log.i("TAG", "---->>>socket 重新连接等待15000");
                currCount=0;
                SystemClock.sleep(15000);
            }

            socket.connect();
            currCount++;
        }else if(socket.connected())
        {

        }
    }


    /***
     * 断开通信连接
     * ***/
    public synchronized void disContentSocket() {
        if(socket!=null)
        {
            unsetListtener();
            if (isConnect()) {
                Log.i("TAG","---->>>socket  disconnect--->>");
                socket.disconnect();
            }
        }
        socket = null;
    }



    void setListtener() {
        // 连接监听
        socket.on(Socket.EVENT_CONNECT, new Listener() {

            @Override
            public void call(Object... arg0) {
                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.connectSucess);
                Log.i("TAG", "---->>>socket  Socket.EVENT_CONNECT");
                currCount=0;
                inroon(userId, roomBean);
            }
        });
        // 连接失败监听
        socket.on(Socket.EVENT_CONNECT_ERROR, new Listener() {

            @Override
            public void call(Object... arg0) {
                SystemClock.sleep(5000);
                Log.i("TAG", "---->>>socket Socket.EVENT_CONNECT_ERROR   arg0:" + arg0);
                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.UnconnectSucess);
                contentSocket();
            }
        });

        // 连接超时
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Listener() {

            @Override
            public void call(Object... arg0) {
                SystemClock.sleep(5000);
                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.UnconnectSucess);
                Log.i("TAG", "---->>>socket Socket.EVENT_CONNECT_TIMEOUT");
                contentSocket();
            }
        });

        // 重新连接监听
        socket.on(Socket.EVENT_RECONNECT, new Listener() {

            @Override
            public void call(Object... arg0) {
                Log.i("TAG", "---->>>socket Socket.EVENT_RECONNECT");
                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.connectSucess);
                if (context != null) {
                    //MyApplication.getInstance().showMessage(context, "网络连接成功");
                }
            }
        });

        // 断开连接监听
        socket.on(Socket.EVENT_DISCONNECT, new Listener() {
            @Override
            public void call(Object... arg0) {
                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.UnconnectSucess);
                //contentSocket();
                Log.i("TAG", "---->>>socket Socket.EVENT_DISCONNECT");
            }
        });

        /*****
         * 加入房间后 获取到的 信息
         * ***/
        socket.on("new message", new Listener() {

            @Override
            public void call(Object... arg0) {
                if (arg0[0] != null && arg0[0] instanceof JSONObject) {
                    JSONObject json = (JSONObject) arg0[0];
                    Log.i("TAG", "---->>>socket new message");
                    Gson gson = new Gson();
                    Object obj = gson.fromJson(json.toString(), RequestMessageBean.class);
                    if (obj != null && obj instanceof RequestMessageBean) {
                        Log.i("TAG", "---->>>socket new message 收到:"+json.toString());
                        RequestMessageBean requestMessageBean = (RequestMessageBean) obj;
                        ImDataBaseManager.getInstance().writeImMessageData(requestMessageBean);
                        SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.roomMessage,requestMessageBean);
                    }
                }
            }
        });

        /****
         * 不在当前房间 接收到的消息
         * */
        socket.on("room message", new Listener() {

            @Override
            public void call(Object... arg0) {
                if (arg0[0] != null && arg0[0] instanceof JSONObject) {
                    JSONObject json = (JSONObject) arg0[0];
                    Log.i("TAG", "---->>>socket room message:"+json.toString());
                    Gson gson = new Gson();
                    Object obj = gson.fromJson(json.toString(), RequestMessageBean.class);
                    if (obj != null && obj instanceof RequestMessageBean) {
                        final RequestMessageBean requestMessageBean = (RequestMessageBean)obj;
                        ImDataBaseManager.getInstance().writeImMessageData(requestMessageBean);
                        SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.OurRoomMessage,requestMessageBean);
                    }
                }
            }
        });
    }


    /****
     * 进入房间
     * @param bean RoomBean 信息
     * ***/
    public void inroon(String userId,RoomBean bean) {
        //Map<String, String> data2=getMap(bean);
        //Log.i("TAG", new JSONObject(data2).toString());
        if(socket==null || !isConnect())
        {
            Log.i("TAG","inroon:socket 未连接 进行重连");
            currCount=0;
            contentSocket();
            return;
        }
        if(socket.connected())
        {
            if(bean==null || bean.getRoom_id()==null || bean.getRoom_id().equals(""))
            {
                return;
            }
            if(bean!=null)
            {
                roomBean=bean;
                Gson gson=new Gson();
                final String json=gson.toJson(bean);
                Log.i("TAG", json);
                try {
                    Log.i("TAG", "---->>>socket inroom userId:"+userId+" json:" + json);
                    socket.emit("join room",userId, new JSONObject(json), new Ack() {

                        @Override
                        public void call(Object... arg0) {
                            if(arg0[0] != null && arg0[0] instanceof JSONObject)
                            {
                                JSONObject jsonObject=(JSONObject)arg0[0];
                                Log.i("TAG", "---->>>socket inroom  加入房间返回jsonObject:" + jsonObject.toString());
                                if(jsonObject.has("type"))
                                {
                                    try {
                                        String type=jsonObject.getString("type");
                                        if(type.equals("success"))
                                        {
                                            Log.i("TAG", "---->>>socket inroom  加入房间成功" );
                                                    SocketObserverManager.getInstance().setIsJoinRoom(true);
                                            //通知 加入房间成功
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            Log.i("TAG", "---->>>socket inroom");
                        }
                    });
                } catch (Exception e) {
                    Log.i("TAG", "---->>>socket inroom error:"+e.getMessage());
                }
            }
        }

    }

    /****
     * 退出房间
     * ***/
    public void outinroon() {

        try {
            roomBean=null;
            Log.i("TAG", "---->>>socket outinroon");
            socket.emit("leaveRoom", new Ack() {

                @Override
                public void call(Object... arg0) {
                    if(arg0!=null && arg0.length>0)
                    {
                        JSONObject jsonObject=(JSONObject)arg0[0];
                        Log.i("TAG", "---->>>socket outinroon:"+jsonObject.toString());
                    }
                    SocketObserverManager.getInstance().setIsJoinRoom(false);
                    Log.i("TAG", "---->>>socket outinroon");
                }
            });
        } catch (Exception e) {
            Log.i("TAG", "---->>>socket outinroon error:"+e.getMessage());
        }
    }


    /***
     * 发送信息
     * @param baseChatBean BaseChatBean 发送信息
     * ***/
    public synchronized void sendMsg(final BaseChatBean baseChatBean) {
        baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_loading);
        MessageBean sendbean=baseChatBean.getMessageBean();
        if (isConnect()) {
            // Map<String, String> data2=getMap(messageBean);
            Gson gson=new Gson();
            String json=gson.toJson(sendbean);
            Log.i("TAG","---->>>socket  sendMsg--->>"+json);
            try {
                socket.emit("sendMessage", new JSONObject(json), new Ack() {

                    @Override
                    public void call(Object... arg0) {
                        if(arg0!=null && arg0.length>0)
                        {
                            JSONObject json = (JSONObject) arg0[0];
                            RequestImCallBackBean bean= BaseGsonUtils.getJsonToObject(RequestImCallBackBean.class, json.toString());
                            if(json!=null && bean!=null)
                            {
                                if(bean.getType().equals("success"))
                                {
                                    baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_sucess);
                                    if(bean.getData()!=null)
                                    {
                                        baseChatBean.set_id(bean.getData().get_id());
                                    }
                                    Log.i("TAG", "---->>>socket sendMsg 收到发送 成功回调:成功:"+json.toString());
                                    if(bean.getData()!=null)
                                    {
                                        baseChatBean.set_id(bean.getData().get_id());
                                    }
                                    ImDataBaseManager.getInstance().writeImMessageData(bean.getData());
                                    //发送成功
                                    SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts,baseChatBean);
                                }else
                                {
                                    Log.i("TAG", "---->>>socket sendMsg 收到发送 成功回调:失败");
                                    baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_fails);
                                    Log.i("TAG", "---->>>socket sendMsg 收到发送 成功回调 失败:json:"+json.toString());
                                    //发送失败
                                    SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts);
                                }
                            }else
                            {
                                Log.i("TAG", "---->>>socket sendMsg 收到发送 成功回调:成功");
                                baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_fails);
                                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts);
                                //发送失败
                            }
                            JSONObject jsonObject=(JSONObject)arg0[0];

                        }else
                        {
                            baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_fails);
                            SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts);
                            //发送失败
                        }
                        Log.i("TAG", "sendMessage " + arg0.toString());
                    }
                });
            } catch (Exception e) {
                Log.i("TAG", e.getMessage());
            }
        } else {
            baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_fails);
            SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts);
            contentSocket();
            if(context!=null)
            {
                //MyApplication.getInstance().showMessage(context, "网络已经断开，正在重连");
            }

            Log.i("TAG", "未连接");
        }
    }



    /******
     * 判断是否建立连接
     * ****/
    public boolean isConnect() {
        if (socket == null || !(socket.connected())) {
            return false;
        } else {
            return true;
        }
    }



    /******
     * 注销监听
     **/
    void unsetListtener() {
        if(socket!=null)
        {
            // 连接监听
            socket.off(Socket.EVENT_CONNECT);
            // 连接失败监听
            socket.off(Socket.EVENT_CONNECT_ERROR);

            // 连接超时
            socket.off(Socket.EVENT_CONNECT_TIMEOUT);

            // 断开连接监听
            socket.off(Socket.EVENT_DISCONNECT);

            socket.off("new message");
            socket.off("room message");
            // 发送消息监听
            socket.off(Socket.EVENT_MESSAGE);
            // 重新连接监听
            socket.off(Socket.EVENT_RECONNECT);
        }
    }
}
