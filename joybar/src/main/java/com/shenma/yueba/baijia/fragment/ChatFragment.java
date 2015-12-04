package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.adapter.ChattingAdapter;
import com.shenma.yueba.baijia.modle.RequestImMessageInfoBean;
import com.shenma.yueba.baijia.modle.RequestRoomInfo;
import com.shenma.yueba.baijia.modle.RequestRoomInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.NetUtils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.SoftKeyboardUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.faceview.FaceView;
import com.shenma.yueba.yangjia.modle.CircleDetailBackBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import im.broadcast.ImBroadcastReceiver;
import im.control.SocketManger;
import im.form.BaseChatBean;
import im.form.NoticeChatBean;
import im.form.PicChatBean;
import im.form.ProductChatBean;
import im.form.RequestMessageBean;
import im.form.RoomBean;
import im.form.TextChatBean;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ChatFragment extends Fragment {
    String TAG = "TAG";
    LayoutInflater layoutInflater;
    View parentView;
    LinearLayout chat_alertmsg_linearlayout;// 顶部提示信息
    ImBroadcastReceiver imBroadcastReceiver;
    String usericon;//本地头像
    private int circleId=-1;// 圈子id
    private TextView pb_reserttitle_textview;//提示socke重连文本对象
    InputMethodManager manager;
    PullToRefreshListView chat_list;//聊天信息列表对象
    boolean isloading = false;// 是否加载中
    boolean haveMoreData = true;// 是否有更多的数据可以加载
    private ProgressBar loadmorePB;// 加载进度条
    private HttpControl httpControl=new HttpControl();
    int currPage=1;
    String roomId=null;//房间id
    private int formUser_id;//当前用户的id
    private int toUser_id;//发送指定的人的id
    String  userName;//我的昵称
    String socketType="group";
    private LinkedList<BaseChatBean> bean_list = new LinkedList<BaseChatBean>();// 消息列表
    ChattingAdapter chattingAdapter;
    FaceView fView;//表情视图对象
    private EditText mEditTextContent;// 信息文本框
    private RelativeLayout edittext_layout;// 文本框的父视图对象
    private Button buttonSend;// 发生按钮
    private LinearLayout btnContainer;// 扩展视图（照相 图片 链接 收藏 ）
    private ImageView iv_emoticons_normal;// 表情按钮
    Button btnMore;//更多的按钮
    private boolean isregister=false;
    boolean isRegisterbroadcase=false;
    private RequestRoomInfo requestRoomInfo;//房间信息对象
    private List<Integer> int_array=new ArrayList<Integer>();
    boolean isPrepare=false;//是否准备完成 即 是否已经获取到 房间号
    boolean isrunning=false;
    //圈子信息
    CircleDetailBackBean circleDetailBackBean;


    int buyerId=-1;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        layoutInflater = LayoutInflater.from(activity);
        buyerId = getArguments().getInt("userID", -1);
        Log.i(TAG, "ChatFragment--->>onAttach ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = layoutInflater.inflate(R.layout.activity_chat, null);
            initView();
        }
        Log.i(TAG, "ChatFragment--->>onCreateView ");
        ViewGroup vg = (ViewGroup) parentView.getParent();
        if (vg != null) {
            vg.removeView(parentView);
        }

        return parentView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "ChatFragment--->>setUserVisibleHint isVisibleToUser:" + isVisibleToUser);
        if (isVisibleToUser) {
            SocketManger.the().setContext(getActivity());
            registerImBroadcastReceiver();//注册广播（接收 消息）
            regiestSockIoBroadCase();//监听sockeoio链接变化

            if(circleDetailBackBean==null)
            {
                if(!isrunning)
                {
                    //获取用户默认圈子信息
                    requestBaseCircleInfo();
                }
            }

            if(roomId==null && circleId>0)
            {
                getMessageByCircleId();
            }

        }
    }

    void initView() {
        chat_alertmsg_linearlayout = (LinearLayout) parentView.findViewById(R.id.chat_alertmsg_linearlayout);
        chat_alertmsg_linearlayout.setVisibility(View.VISIBLE);
        TextView chat_alertmsg_textview = (TextView) parentView.findViewById(R.id.chat_alertmsg_textview);
        FontManager.changeFonts(getActivity(), chat_alertmsg_textview);
        // 我的头像
        usericon = SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_logo);
        //注册监听广播 监听收到的信息
        registerImBroadcase();

        // 我的 userid
        if(SharedUtil.getUserId(getActivity())!=null)
        {
            formUser_id = Integer.parseInt(SharedUtil.getUserId(getActivity()));
        }

        // 我的昵称
        userName = SharedUtil.getUserNames(getActivity());

        /***************
         *  输入法管理对象
         * ***************/
        manager = (InputMethodManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /***************
         *   初始化连接视图
         * ***************/
        pb_reserttitle_textview = (TextView) parentView.findViewById(R.id.pb_reserttitle_textview);
        pb_reserttitle_textview.getBackground().setAlpha(10);
        loadmorePB = (ProgressBar) parentView.findViewById(R.id.pb_load_more);


        /***************
         *   初始化连接视图
         * ***************/
        chat_list = (PullToRefreshListView) parentView.findViewById(R.id.chat_list);
        chat_list.setMode(PullToRefreshBase.Mode.DISABLED);//设置下拉不可用
        chat_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当用户 滑动到顶端时 并且 访问网络还没有开始 以及服务器中 还有新的历史信息时 触发从网络获取数据
                if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                    switch (scrollState) {
                        case SCROLL_STATE_IDLE:
                            loadmorePB.setVisibility(View.VISIBLE);// 显示 加载视图
                            // 从网络获取消息数据
                            getMessage();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        chattingAdapter = new ChattingAdapter(getActivity(), bean_list);
        chat_list.setAdapter(chattingAdapter);

        /***************
         * 初始化聊天界面
         * ***************/
        fView = (FaceView)parentView.findViewById(R.id.faceLayout);//表情
        fView.setOnChickCallback(new FaceView.OnChickCallback() {
            @Override
            public void onChick(int arg1, int resId, String arg2) {
                SpannableString ss = new SpannableString(arg2);
                Drawable d = getResources().getDrawable(resId);
                d.setBounds(0, 0, 50, 50);// 设置表情图片的显示大小
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(span, 0, arg2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mEditTextContent.getText().insert(mEditTextContent.getSelectionStart(), ss);
            }
        });
        edittext_layout = (RelativeLayout)parentView.findViewById(R.id.edittext_layout);
        mEditTextContent = (EditText)parentView.findViewById(R.id.et_sendmessage);
        mEditTextContent.setBackgroundResource(R.drawable.shape_linearlayout10);
        mEditTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideFace();
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                btnContainer.setVisibility(View.GONE);
                pointLast(bean_list.size());
            }
        });

        // 监听文本框 输入的内容
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonSend = (Button)parentView.findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditTextContent.getText().toString().trim();
                if(!isPrepare)
                {
                    return;
                }
                if ("".equals(content)) {
                    Toast.makeText(getActivity(), "发送消息不能为空", 1000).show();
                    return;
                }
                if (!NetUtils.isNetworkConnected(getActivity())) {
                    Toast.makeText(getActivity(), "网络不可用", 1000).show();
                    return;
                }
                if(!showOrHiddenAlertView())
                {
                    MyApplication.getInstance().showMessage(getActivity(), "通信已断开，正在重连");
                    return;
                }
                setTextMsgData(content);
                mEditTextContent.setText("");
            }
        });
        iv_emoticons_normal = (ImageView)parentView.findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideIMM();
            }
        });
        iv_emoticons_normal.setVisibility(View.VISIBLE);

        btnMore = (Button)parentView.findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnContainer.getVisibility() != View.VISIBLE) {
                    btnContainer.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    hideFace();
                } else {
                    btnContainer.setVisibility(View.GONE);
                }
                pointLast(bean_list.size());
            }
        });


        /************
         * 扩展 示图信息   扩展视图（照相 图片 链接 收藏 ）
         * ***************/
        btnContainer = (LinearLayout)parentView.findViewById(R.id.ll_btn_container);


    }



    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        if (btnContainer.getVisibility() == View.VISIBLE) {
            btnContainer.setVisibility(View.GONE);
        }
    }


    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 控制显示或者隐藏表情
     */
    private void showOrHideIMM() {
        if (iv_emoticons_normal.getTag() == null) {
            // 隐藏软键
            SoftKeyboardUtil.hide(getActivity(), mEditTextContent);
            // 显示表情
            showFace();
            // 隐藏其他底部view
            btnContainer.setVisibility(View.GONE);
        } else {
            // 显示软键盘
            SoftKeyboardUtil.show(getActivity(), mEditTextContent);
            // 隐藏表情
            hideFace();
        }
        pointLast(bean_list.size());
    }



    /**
     * 显示表情
     */
    private void showFace() {
        iv_emoticons_normal.setBackgroundResource(R.drawable.chatting_biaoqing_btn_enable);
        iv_emoticons_normal.setTag(1);
        fView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情
     */
    private void hideFace() {
        iv_emoticons_normal.setBackgroundResource(R.drawable.chatting_biaoqing_btn_normal);
        iv_emoticons_normal.setTag(null);
        fView.setVisibility(View.GONE);
    }



    /*****
     * 设置 文本信息内容
     * ****/
    void setTextMsgData(String str) {
        BaseChatBean msgbean = new TextChatBean(getActivity());
        setSendValue(msgbean, str);
    }

    /******
     * 发送数据的通用赋值
     *
     * @param bean
     *            BaseChatBean消息类型对象
     * ***/
    void setSendValue(BaseChatBean bean, String content) {
        if (bean == null) {
            return;
        }
        bean.setContent(content);
        bean.setFrom_id(formUser_id);
        bean.setTo_id(toUser_id);
        bean.setRoom_No(roomId);
        bean.setUserName(userName);
        bean.setIsoneself(true);
        bean.setLogo(ToolsUtil.nullToString(usericon));
        bean.setCreationDate(ToolsUtil.getCurrentTime());
        bean.setSharelink(content);
        bean.sendData();// 发送数据

        addListData(false, bean);
        if (chattingAdapter != null) {
            chattingAdapter.notifyDataSetChanged();
        }
        pointLast(bean_list.size());

    }


    /******
     * 根据socket是否连接 显示 或隐藏  提示信息
     * ***/
    boolean  showOrHiddenAlertView()
    {
        boolean isconnect=false;
        if(SocketManger.the().isConnect())
        {
            isconnect=true;
        }else
        {
            isconnect=false;
        }
        if(pb_reserttitle_textview!=null)
        {
            if(isconnect)
            {
                pb_reserttitle_textview.setVisibility(View.GONE);
            }else
            {
                pb_reserttitle_textview.setVisibility(View.VISIBLE);
            }

        }
        return isconnect;
    }


    /***
     * 获取消息
     * **/
    void getMessage() {
        if (isloading) {
            return;
        }
        loadmorePB.setVisibility(View.VISIBLE);
        isloading = true;
        getMessageRecord(roomId, -10, currPage, Constants.PAGESIZE_VALUE);

    }


    /****
     * 获取聊天记录
     *
     * @param roomId
     *            int 房间id
     * @param lastMessageId
     *            int 信息id 小于0 可不传
     * @param page
     *            int 访问页数
     * @param pageSize
     *            int 每页大小
     * ***/
    void getMessageRecord(String roomId, int lastMessageId, final int page,int pageSize) {
        if(httpControl==null)
        {
            httpControl=new HttpControl();
        }
        httpControl.getRoomMessage(roomId, lastMessageId, page, pageSize,
                false, new HttpControl.HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        currPage = page;
                        chat_list.onRefreshComplete();
                        if (obj != null
                                && obj instanceof RequestImMessageInfoBean) {
                            RequestImMessageInfoBean messagebean = (RequestImMessageInfoBean) obj;
                            if (messagebean.getData() == null
                                    || messagebean.getData().getItems() == null
                                    || messagebean.getData().getItems().size() == 0) {
                                if (page == 1) {
                                    // 如果是第一页
                                }
                            } else {
                                int allPage = messagebean.getData()
                                        .getTotalpaged();
                                if (currPage >= allPage) {
                                    haveMoreData = false;
                                    chat_list.setMode(PullToRefreshBase.Mode.DISABLED);
                                } else {
                                    haveMoreData = true;
                                    currPage++;
                                }
                            }
                            dataSuceeValue(messagebean.getData().getItems());
                        } else {
                            http_Fails(500, "获取失败");
                            isloading = false;
                            loadmorePB.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                        chat_list.onRefreshComplete();
                        isloading = false;
                        loadmorePB.setVisibility(View.GONE);
                    }
                }, getActivity());

    }




    /*****
     * 从网络获取 历史聊天信息 成功后 进行赋值
     * ****/
    void dataSuceeValue(List<RequestMessageBean> items) {
        isloading = false;
        loadmorePB.setVisibility(View.GONE);
        if (items == null || items.size() == 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String type = items.get(i).getType();
            if (type.equals(RequestMessageBean.type_img))// 如果是 图片
            {
                BaseChatBean bean = new PicChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setType(socketType);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_produtc_img))// 是商品图片
            {
                BaseChatBean bean = new ProductChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setType(socketType);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.notice))// 广播
            {
                BaseChatBean bean = new NoticeChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setType(socketType);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_empty))// 文本信息
            {
                BaseChatBean bean = new TextChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setType(socketType);
                addListData(true, bean);
            }
        }
        pointLast(items.size());


    }


    // 定位到知道位置
    void pointLast(int i) {
        if (i <= bean_list.size()) {
            if(chat_list!=null)
            {
                chat_list.getRefreshableView().setSelection(i);
            }

        }
    }


    /*****
     * 同步添加数据 到列表
     *
     * @param isfirst  boolean true 加入列表前面 false 加入列表后面
     * ****/
    synchronized void addListData(boolean isfirst, BaseChatBean... chatBean) {
        if (chatBean != null && chatBean.length > 0) {
            for (int i = 0; i < chatBean.length; i++) {
                BaseChatBean bbean=chatBean[i];
                if (isfirst) {
                    bean_list.addFirst(chatBean[i]);
                } else {
                    bean_list.add(chatBean[i]);
                }
            }
        }

    }



    /*******
     * 注册监听广播 监听接收到的 聊天信息
     ***/
    void registerImBroadcase() {
        imBroadcastReceiver = new ImBroadcastReceiver(new ImBroadcastReceiver.ImBroadcastReceiverLinstener() {

            @Override
            public void newMessage(Object obj) {
                roomMsg(obj);
            }

            @Override
            public void roomMessage(Object obj) {

            }

            @Override
            public void clearMsgNotation(ImBroadcastReceiver.RECEIVER_type type) {

            }
        });
    }


    /*****
     * 加入 房间内 接收到消息
     ***/
    void roomMsg(Object obj) {
        if (obj != null && obj instanceof RequestMessageBean) {
            BaseChatBean baseChatBean = null;
            RequestMessageBean bean = (RequestMessageBean) obj;
            String type = bean.getType();
            if (type.equals(RequestMessageBean.type_img))// 如果是图片
            {
                baseChatBean = new PicChatBean(getActivity());
            } else if (type.equals(RequestMessageBean.type_produtc_img))// 如果是商品图片
            {
                baseChatBean = new ProductChatBean(getActivity());
            } else if (type.equals(RequestMessageBean.notice))// 如果是广播
            {

            } else {
                baseChatBean = new TextChatBean(getActivity());

            }
            // 通知更新
            notification(baseChatBean, bean);
        }
    }


    /*******
     * 通过用圈子id获取历史数据信息
     * ***/
    void getMessageByCircleId()
    {
        if(isrunning)
        {
            return;
        }
        // 获取房间号
        getRoomdId(circleId, formUser_id, toUser_id);
    }

    /****
     *
     * 获取房间号  根据圈子id
     * @param groupId
     *            int 圈子id
     * @param fromUser
     *            int
     * @param toUser
     *            int
     * **/
    void getRoomdId(int groupId, int fromUser, int toUser) {
        if(httpControl==null)
        {
            httpControl=new HttpControl();
        }
        isrunning=true;
        httpControl.getRoom_Id(groupId, fromUser, toUser, true,
                new HttpControl.HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        isrunning=false;
                        if (obj != null && obj instanceof RequestRoomInfoBean) {
                            RequestRoomInfoBean bean = (RequestRoomInfoBean) obj;
                            if (bean.getData() == null) {
                                http_Fails(500, "获取房间号 失败");
                            } else {
                                requestRoomInfo = bean.getData();
                                roomId = bean.getData().getId();
                                // 进入房间
                                int_array = requestRoomInfo.getUserList();
                                getMessage();// 获取历史数据
                                Log.i("TAG", "---->>>socket roomId:" + roomId);
                                inroom();
                                isPrepare = true;
                                setAlertMsgView();//设置 顶部的提示信息自动消失
                            }

                        }
                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        isrunning=false;
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                        /********
                         * 获取失败 显示 失败视图  显示 重新获取  按钮
                         *
                         * ********/
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                    }
                }, getActivity());
    }


    void notification(final BaseChatBean baseChatBean,
                      final RequestMessageBean bean) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (baseChatBean != null) {
                    baseChatBean.setValue(bean);
                    // 讲接收到的信息加入到列表并刷新列表
                    addListData(false, baseChatBean);
                }
                pointLast(bean_list.size());

            }
        });
    }


    /****
     * 加入房间
     * **/
    void inroom() {
        // 加入房间
        RoomBean roomBean = new RoomBean();
        roomBean.setOwner(Integer.toString(formUser_id));
        roomBean.setRoom_id(roomId);
        roomBean.setType(socketType);
        //roomBean.setTitle();ssss
        roomBean.setUserName(userName);
        int[] userint = new int[int_array.size()];
        for (int i = 0; i < int_array.size(); i++) {
            userint[i] = int_array.get(i);
        }
        roomBean.setUsers(userint);
        SocketManger.the().inroon(Integer.toString(formUser_id), roomBean);

        if (requestRoomInfo != null) {
            Log.i("TAG", "socketio---->>已经与服务器建立链接");

            Log.i("TAG", "socketio---->>加入房间 roomId=" + roomId);
        }
    }



    /*******
     * 设置顶部的提示信息
     ***/
    void setAlertMsgView() {
        if (chat_alertmsg_linearlayout == null || chat_alertmsg_linearlayout.getVisibility() == View.GONE) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation alphaa = new AlphaAnimation(1.0f, 0f);
                alphaa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        chat_alertmsg_linearlayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                alphaa.setDuration(2000);
                alphaa.setFillAfter(true);
                chat_alertmsg_linearlayout.startAnimation(alphaa);
            }
        }, 2000);

    }



    /******
     * 注册广播监听 用于接收消息
     * ***/
    void registerImBroadcastReceiver()
    {
        if(!isregister)
        {
            isregister=true;
            getActivity().registerReceiver(imBroadcastReceiver, new IntentFilter(ImBroadcastReceiver.IntentFilter));
        }

    }

    /****
     * 注销广播监听
     * **/
    void unRegisterImBroadcastReceiver()
    {
        if(isregister)
        {
            getActivity().unregisterReceiver(imBroadcastReceiver);
        }

    }

    /*******
     * 注册socketio  链接变化监听
     * ******/
    void regiestSockIoBroadCase()
    {
        if(!isRegisterbroadcase)
        {
            isRegisterbroadcase=true;
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(Constants.IntentFilterChatAtConnect);
            intentFilter.addAction(Constants.IntentFilterChatAtUnConnect);
            getActivity().registerReceiver(broadcastReceiver, intentFilter);
        }
    }


    BroadcastReceiver broadcastReceiver=new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null)
            {
                if(intent.getAction().equals(Constants.IntentFilterChatAtConnect) || intent.getAction().equals(Constants.IntentFilterChatAtUnConnect))
                {
                    showOrHiddenAlertView();
                }
            }
        }

    };



    /*******
     * 注销socketio  链接变化监听
     * ******/
    void unRegiestSockIoBroadCase()
    {
        if(isRegisterbroadcase)
        {
            getActivity().unregisterReceiver(broadcastReceiver);
            isRegisterbroadcase=false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "ChatFragment--->>onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "ChatFragment--->>onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "ChatFragment--->>onStop ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "ChatFragment--->>onDetach ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "ChatFragment--->>onDestroyView ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ChatFragment--->>onDestroy ");
        unRegisterImBroadcastReceiver();//注销消息接收
        unRegiestSockIoBroadCase();//注销socketio  链接变化监听
        outRoom();
    }


    /*****
     * 离开房间
     * ***/
    void outRoom()
    {
        SocketManger.the().outinroon();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "ChatFragment--->>onStart ");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "ChatFragment--->>onViewCreated ");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "ChatFragment--->>onActivityCreated ");
    }

    /*********
     * 请求 基础圈子信息
     * ******/
    void requestBaseCircleInfo()
    {
        isrunning=true;
        String userId= SharedUtil.getStringPerfernece(getActivity(),SharedUtil.user_id);
        httpControl.getBaseCircleDetailByUserID(Integer.toString(buyerId), userId, true, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isrunning=false;
                circleDetailBackBean=(CircleDetailBackBean)obj;
                if(circleDetailBackBean.getData()!=null)
                {
                    circleId= Integer.valueOf(circleDetailBackBean.getData().getGroupId());
                    View circlesettings_imageview=getActivity().findViewById(R.id.circlesettings_imageview);
                    if(circlesettings_imageview!=null)
                    {
                        circlesettings_imageview.setTag(circleId);
                        circlesettings_imageview.setVisibility(View.VISIBLE);
                    }
                    if(roomId==null && circleId>0)
                    {
                        getMessageByCircleId();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                isrunning=false;
                MyApplication.getInstance().showMessage(getActivity(),msg);
            }
        },getActivity());
    }
}
