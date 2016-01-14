package com.shenma.yueba.baijia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.shenma.yueba.baijia.activity.BaiJiaShareDataActivity;
import com.shenma.yueba.baijia.adapter.ChattingAdapter;
import com.shenma.yueba.baijia.modle.BaiJiaShareInfoBean;
import com.shenma.yueba.baijia.modle.RequestImMessageInfoBean;
import com.shenma.yueba.baijia.modle.RequestRoomInfo;
import com.shenma.yueba.baijia.modle.RequestRoomInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.NetUtils;
import com.shenma.yueba.util.PhotoUtils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.SoftKeyboardUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.faceview.FaceView;
import com.shenma.yueba.yangjia.modle.CircleDetailBackBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import im.control.SocketManger;
import im.control.SocketObserverManager;
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
public class ChatFragment extends Fragment implements View.OnClickListener , SocketObserverManager.SocketNoticationListener{
    String TAG = "TAG";
    LayoutInflater layoutInflater;
    View parentView;
    LinearLayout chat_alertmsg_linearlayout;// 顶部提示信息
    LinearLayout resertaddinfo_linearlayout;//重新加载页面父视图对象
    TextView resertaddinfo_textview;
    String usericon;//本地头像
    private int circleId = -1;// 圈子id
    private TextView pb_reserttitle_textview;//提示socke重连文本对象
    InputMethodManager manager;
    PullToRefreshListView chat_list;//聊天信息列表对象
    boolean isloading = false;// 是否加载中
    boolean haveMoreData = true;// 是否有更多的数据可以加载
    private ProgressBar loadmorePB;// 加载进度条
    private HttpControl httpControl = new HttpControl();
    int currPage = 1;
    String roomId = null;//房间id
    private int formUser_id;//当前用户的id
    private int toUser_id;//发送指定的人的id
    String userName;//我的昵称
    private LinkedList<BaseChatBean> bean_list = new LinkedList<BaseChatBean>();// 消息列表
    ChattingAdapter chattingAdapter;
    FaceView fView;//表情视图对象
    private EditText mEditTextContent;// 信息文本框
    private RelativeLayout edittext_layout;// 文本框的父视图对象
    private Button buttonSend;// 发生按钮
    private LinearLayout btnContainer;// 扩展视图（照相 图片 链接 收藏 ）
    private ImageView iv_emoticons_normal;// 表情按钮
    Button btnMore;//更多的按钮
    private RequestRoomInfo requestRoomInfo;//房间信息对象
    boolean isPrepare = false;//是否准备完成 即 是否已经获取到 房间号
    boolean isrunning = false;
    //圈子信息
    CircleDetailBackBean circleDetailBackBean;

    private String littlePicPath;
    private String littlePicPath_cache;
    public static final int Result_code_link = 400;// 链接
    public static final int Result_code_collection = 500;// 收藏

    int buyerId = -1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        layoutInflater = LayoutInflater.from(activity);
        buyerId = getArguments().getInt("buyerId", -1);
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
        SocketObserverManager.getInstance().addSocketObserver(this);
        return parentView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "ChatFragment--->>setUserVisibleHint isVisibleToUser:" + isVisibleToUser);
        //如果用户已经登录
        if (isVisibleToUser && MyApplication.getInstance().isUserLoginNoForward(getActivity())) {
            SocketManger.the().setContext(getActivity());
            requestChatInfo();
            inroom();
        }
    }

    /********
     * 获取圈子信息及 房间信息
     *******/
    void requestChatInfo() {
        //如果未圈子信息
        if (circleDetailBackBean == null) {
            if (!isrunning) {
                //获取用户默认圈子信息
                requestBaseCircleInfo();
            }
        }
        //如果未获得房间信息
        if (roomId == null && circleId > 0) {
            getMessageByCircleId();
        }
    }

    void initView() {
        //照相
        ImageView btn_camera=(ImageView)parentView.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        //图片
        ImageView btn_picture=(ImageView)parentView.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        //链接
        ImageView btn_link=(ImageView)parentView.findViewById(R.id.btn_link);
        btn_link.setOnClickListener(this);
        //收藏
        ImageView btn_collention=(ImageView)parentView.findViewById(R.id.btn_collention);
        btn_collention.setOnClickListener(this);


        chat_alertmsg_linearlayout = (LinearLayout) parentView.findViewById(R.id.chat_alertmsg_linearlayout);
        chat_alertmsg_linearlayout.setVisibility(View.VISIBLE);
        TextView chat_alertmsg_textview = (TextView) parentView.findViewById(R.id.chat_alertmsg_textview);
        FontManager.changeFonts(getActivity(), chat_alertmsg_textview);
        // 我的头像
        usericon = SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_logo);

        // 我的 userid
        if (SharedUtil.getUserId(getActivity()) != null) {
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
        chat_list.getRefreshableView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //隐藏输入法
                hideKeyboard();
                //回复默认键盘输入界面
                btnContainer.setVisibility(View.GONE);
                hideFace();
                return false;
            }
        });
        chat_list.setMode(PullToRefreshBase.Mode.DISABLED);//设置下拉不可用
        chat_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //当用户 滑动到顶端时 并且 访问网络还没有开始 以及服务器中 还有新的历史信息时 触发从网络获取数据
                if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                    switch (scrollState) {
                        case SCROLL_STATE_IDLE:
                            if(MyApplication.getInstance().isUserLoginNoForward(getActivity()))
                            {
                                loadmorePB.setVisibility(View.VISIBLE);// 显示 加载视图
                                // 从网络获取消息数据
                                getMessage();
                            }
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
        fView = (FaceView) parentView.findViewById(R.id.faceLayout);//表情
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
        edittext_layout = (RelativeLayout) parentView.findViewById(R.id.edittext_layout);
        mEditTextContent = (EditText) parentView.findViewById(R.id.et_sendmessage);
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

        buttonSend = (Button) parentView.findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditTextContent.getText().toString().trim();
                if (!isPrepare) {
                    return;
                }
                if ("".equals(content)) {
                    MyApplication.getInstance().showMessage(getActivity(), "发送消息不能为空");
                    return;
                }
                if (!NetUtils.isNetworkConnected(getActivity())) {
                    MyApplication.getInstance().showMessage(getActivity(), "网络不可用");
                    return;
                }
                if (!showOrHiddenAlertView()) {
                    MyApplication.getInstance().showMessage(getActivity(), "通信已断开，正在重连");
                    return;
                }
                setTextMsgData(content);
                mEditTextContent.setText("");
            }
        });
        iv_emoticons_normal = (ImageView) parentView.findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideIMM();
            }
        });
        iv_emoticons_normal.setVisibility(View.VISIBLE);

        btnMore = (Button) parentView.findViewById(R.id.btn_more);
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
        btnContainer = (LinearLayout) parentView.findViewById(R.id.ll_btn_container);


        /********
         * 重新加载
         * *******/
        resertaddinfo_linearlayout = (LinearLayout) parentView.findViewById(R.id.resertaddinfo_linearlayout);
        resertaddinfo_textview = (TextView) parentView.findViewById(R.id.resertaddinfo_textview);
        resertaddinfo_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestChatInfo();
            }
        });

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
     ****/
    void setTextMsgData(String str) {
        BaseChatBean msgbean = new TextChatBean(getActivity());
        setSendValue(msgbean, str);
    }

    /******
     * 发送数据的通用赋值
     *
     * @param bean BaseChatBean消息类型对象
     ***/
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
     ***/
    boolean showOrHiddenAlertView() {
        boolean isconnect = false;
        if (SocketManger.the().isConnect()) {
            isconnect = true;
        } else {
            isconnect = false;
        }
        if (pb_reserttitle_textview != null) {
            if (isconnect) {
                pb_reserttitle_textview.setVisibility(View.GONE);
            } else {
                pb_reserttitle_textview.setVisibility(View.VISIBLE);
            }

        }
        return isconnect;
    }


    /***
     * 获取消息
     **/
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
     * @param roomId        int 房间id
     * @param lastMessageId int 信息id 小于0 可不传
     * @param page          int 访问页数
     * @param pageSize      int 每页大小
     ***/
    void getMessageRecord(String roomId, int lastMessageId, final int page, int pageSize) {
        if (httpControl == null) {
            httpControl = new HttpControl();
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
                            int allPage = messagebean.getData()
                                    .getTotalpaged();
                            if (currPage >= allPage) {
                                haveMoreData = false;
                                chat_list.setMode(PullToRefreshBase.Mode.DISABLED);
                            } else {
                                haveMoreData = true;
                                currPage++;
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
     ****/
    void dataSuceeValue(List<RequestMessageBean> items) {
        isloading = false;
        loadmorePB.setVisibility(View.GONE);
        if (items == null || items.size() == 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String type = Integer.toString(items.get(i).getMessageType());
            if (type.equals(RequestMessageBean.type_img))// 如果是 图片
            {
                BaseChatBean bean = new PicChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setMessageType(1);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_produtc_img))// 是商品图片
            {
                BaseChatBean bean = new ProductChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setMessageType(1);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.notice))// 广播
            {
                BaseChatBean bean = new NoticeChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setMessageType(1);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_empty))// 文本信息
            {
                BaseChatBean bean = new TextChatBean(getActivity());
                bean.setValue(items.get(i));
                bean.setMessageType(1);
                addListData(true, bean);
            }
        }
        pointLast(items.size());


    }


    // 定位到知道位置
    void pointLast(int i) {
        if (i <= bean_list.size()) {
            if (chat_list != null) {
                chat_list.getRefreshableView().setSelection(i);
            }

        }
    }


    /*****
     * 同步添加数据 到列表
     *
     * @param isfirst boolean true 加入列表前面 false 加入列表后面
     ****/
    synchronized void addListData(boolean isfirst, BaseChatBean... chatBean) {
        if (chatBean != null && chatBean.length > 0) {
            for (int i = 0; i < chatBean.length; i++) {
                BaseChatBean bbean = chatBean[i];
                if (isfirst) {
                    bean_list.addFirst(chatBean[i]);
                } else {
                    bean_list.add(chatBean[i]);
                }
            }
        }

    }

    /*****
     * 加入 房间内 接收到消息
     ***/
    void roomMsg(Object obj) {
        if (obj != null && obj instanceof RequestMessageBean) {
            BaseChatBean baseChatBean = null;
            RequestMessageBean bean = (RequestMessageBean) obj;
            //判断 接受到的消息是否 是当然roomid 是则 显示 不是则不处理
            if (bean.getRoomId() == null || roomId == null || !bean.getRoomId().equals(roomId)) {
                return;
            }

            String type = Integer.toString(bean.getMessageType());
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
     ***/
    void getMessageByCircleId() {
        if (isrunning) {
            return;
        }
        if (resertaddinfo_linearlayout != null) {
            resertaddinfo_linearlayout.setVisibility(View.GONE);
        }
        // 获取房间号
        getRoomdId(circleId, formUser_id, toUser_id);
    }

    /****
     * 获取房间号  根据圈子id
     *
     * @param groupId  int 圈子id
     * @param fromUser int
     * @param toUser   int
     **/
    void getRoomdId(int groupId, int fromUser, int toUser) {
        if (httpControl == null) {
            httpControl = new HttpControl();
        }
        isrunning = true;
        httpControl.getRoom_Id(groupId, fromUser, toUser, true,
                new HttpControl.HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        isrunning = false;
                        if (obj != null && obj instanceof RequestRoomInfoBean) {
                            RequestRoomInfoBean bean = (RequestRoomInfoBean) obj;
                            if (bean.getData() == null) {
                                http_Fails(500, "获取房间号 失败");
                            } else {
                                requestRoomInfo = bean.getData();
                                roomId = bean.getData().getId();
                                // 进入房间eeeeeee
                                //int_array = requestRoomInfo.getUserList();
                                getMessage();// 获取历史数据
                                Log.i("TAG", "---->>>socket roomId:" + roomId);
                                inroom();
                                isPrepare = true;
                                //asetAlertMsgView();//设置 顶部的提示信息自动消失
                            }

                        }
                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        isrunning = false;
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                        /********
                         * 获取失败 显示 失败视图  显示 重新获取  按钮
                         *
                         * ********/
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                        //显示重新加载页面
                        if (resertaddinfo_linearlayout != null) {
                            resertaddinfo_linearlayout.setVisibility(View.VISIBLE);
                        }

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
     **/
    void inroom() {
        if(roomId==null || roomId.equals(""))
        {
            return;
        }

        // 加入房间
        RoomBean roomBean = new RoomBean();
        roomBean.setRoom_id(roomId);
        roomBean.setType("group");
        roomBean.setUserName(userName);
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
        outRoom();
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
        SocketObserverManager.getInstance().removeSocketObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ChatFragment--->>onDestroy ");

    }


    /*****
     * 离开房间
     ***/
    void outRoom() {
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
     ******/
    void requestBaseCircleInfo() {
        if (isrunning) {
            return;
        }
        if (resertaddinfo_linearlayout != null) {
            resertaddinfo_linearlayout.setVisibility(View.GONE);
        }
        isrunning = true;
        String userId = SharedUtil.getStringPerfernece(getActivity(), SharedUtil.user_id);
        httpControl.getBaseCircleDetailByUserID(Integer.toString(buyerId), userId, true, new HttpControl.HttpCallBackInterface() {
            @Override
            public void http_Success(Object obj) {
                isrunning = false;
                circleDetailBackBean = (CircleDetailBackBean) obj;
                if (circleDetailBackBean.getData() != null) {
                    circleId = Integer.valueOf(circleDetailBackBean.getData().getGroupId());
                    View circlesettings_imageview = getActivity().findViewById(R.id.circlesettings_imageview);
                    if (circlesettings_imageview != null) {
                        circlesettings_imageview.setTag(circleId);
                        circlesettings_imageview.setVisibility(View.VISIBLE);
                    }
                    if (roomId == null && circleId > 0) {
                        getMessageByCircleId();
                    }
                }
            }

            @Override
            public void http_Fails(int error, String msg) {
                isrunning = false;
                MyApplication.getInstance().showMessage(getActivity(), msg);
                if (resertaddinfo_linearlayout != null) {
                    resertaddinfo_linearlayout.setVisibility(View.VISIBLE);
                }

            }
        }, getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:// 拍照
                openCamera();
                break;
            case R.id.btn_picture:// 照片
                openPicture();
                break;
            case R.id.btn_link:// 链接
                openLink();
                break;
            case R.id.btn_collention:// 收藏
                openCollention();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
                cameraCallBack(data, resultCode);// 相机回调
                break;
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
                picCallBack(data, resultCode);// 图片回调
                break;
            case PhotoUtils.INTENT_REQUEST_CODE_CROP:
                clipCallBack(data, resultCode);
                break;
            case Result_code_link:// 链接回调
                linkOrCollectCallBack(data);
                break;
            case Result_code_collection:// 收藏
                linkOrCollectCallBack(data);
                break;
            case Constants.REQUESTCODE:
                if (resultCode == Constants.RESULTCODE) {
                    getActivity().setResult(Constants.RESULTCODE);
                    getActivity().finish();
                }
        }
    }


    /****
     * 相机回调
     **/
    void cameraCallBack(Intent data, int resultCode) {
        if (resultCode == getActivity().RESULT_OK) {
            if (littlePicPath != null) {
                upLoadPic(littlePicPath);
            }
        }
    }


    /****
     * 打开照相机并回去返回的图片
     **/
    void openCamera() {
        /*
		 * Intent intent = new Intent();
		 * intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		 * startActivityForResult(intent, Result_code_camera);
		 */
        if (ToolsUtil.isAvailableSpace(getActivity())) {
            littlePicPath = PhotoUtils.takePicture(getActivity());
            Log.i("TAG", "littlePicPath:" + littlePicPath);
        }

    }

    /****
     * 打开照图片
     **/
    void openPicture() {
        if (ToolsUtil.isAvailableSpace(getActivity())) {
            PhotoUtils.selectPhoto(getActivity());
        }
    }

    /**
     * 裁剪返回
     */
    void clipCallBack(Intent data, int resultCode) {
        if (resultCode == getActivity().RESULT_OK) {
            if (littlePicPath_cache != null) {
                upLoadPic(littlePicPath_cache);
            }

        }
    }

    /****
     * 图片回调
     **/
    void picCallBack(Intent data, int resultCode) {
        // 调用相册返回
        if (resultCode == getActivity().RESULT_OK) {
            if (data.getData() == null) {
                return;
            }
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    littlePicPath = cursor.getString(column_index);
                    if (littlePicPath != null) {
                        upLoadPic(littlePicPath);
                    }
                }
            }
        }
    }

    /****
     * 链接
     **/
    void openLink() {
        Intent intent = new Intent(getActivity(),
                BaiJiaShareDataActivity.class);
        intent.putExtra("TYPE", BaiJiaShareDataActivity.LINK);
        startActivityForResult(intent, Result_code_link);
    }

    /****
     * 收藏
     **/
    void openCollention() {
        Intent intent = new Intent(getActivity(), BaiJiaShareDataActivity.class);
        intent.putExtra("TYPE", BaiJiaShareDataActivity.COLLECT);
        startActivityForResult(intent, Result_code_collection);
    }

    /*****
     * 链接或收藏的回调
     **/
    void linkOrCollectCallBack(Intent data) {
        if (data != null && data.getSerializableExtra("RESULT_DATA") != null) {
            List<BaiJiaShareInfoBean> check_list = (List<BaiJiaShareInfoBean>) data
                    .getSerializableExtra("RESULT_DATA");
            sendLinkOrCollect(check_list);
        }

    }

    /***
     * 发送链接或收藏数据
     ***/
    void sendLinkOrCollect(List<BaiJiaShareInfoBean> check_list) {
        if (check_list != null) {
            for (int i = 0; i < check_list.size(); i++) {
                BaiJiaShareInfoBean sharebean = check_list.get(i);
                BaseChatBean msgbean = new ProductChatBean(getActivity());
                msgbean.setProductId(sharebean.getId());
                setSendValue(msgbean,
                        ToolsUtil.nullToString(sharebean.getLogo()));

            }
        }
    }


    /*****
     * 通过阿里云上传
     ***/
    void upLoadPic(String imageLocalPath) {
        if (imageLocalPath == null || imageLocalPath.equals("")) {
            return;
        }
        final PicChatBean baseChatBean = new PicChatBean(getActivity());
        baseChatBean.setListener(new PicChatBean.PicChatBean_Listener() {

            @Override
            public void pic_showMsg(final String msg) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        MyApplication.getInstance().showMessage(getActivity(), msg);
                    }
                });

            }

            @Override
            public void pic_notifaction() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (chattingAdapter != null) {
                            chattingAdapter.notifyDataSetChanged();
                            // chat_list.invalidate();
                        }
                    }
                });

            }
        });

        baseChatBean.setPicaddress(imageLocalPath);

        setSendValue(baseChatBean, "");

    }

    @Override
    public void socketConnectSucess() {
        showOrHiddenAlertView();
    }

    @Override
    public void socketConnectFails() {
        showOrHiddenAlertView();
    }

    @Override
    public void receiveMsgFromRoom(RequestMessageBean bean) {
        roomMsg(bean);
    }

    @Override
    public void receiveMsgFromUnRoom(RequestMessageBean bean) {
    }

    @Override
    public void sendStatusChaneg() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (chattingAdapter != null) {
                    chattingAdapter.notifyDataSetChanged();
                    // chat_list.invalidate();
                }
            }
        });
    }
}
