
package com.shenma.yueba;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.BaiJiaShareDataActivity;
import com.shenma.yueba.baijia.adapter.ChattingAdapter;
import com.shenma.yueba.baijia.dialog.CreateOrderDialog;
import com.shenma.yueba.baijia.modle.BaiJiaShareInfoBean;
import com.shenma.yueba.baijia.modle.CKProductDeatilsInfoBean;
import com.shenma.yueba.baijia.modle.ProductsDetailsTagInfo;
import com.shenma.yueba.baijia.modle.RequestCKProductDeatilsInfo;
import com.shenma.yueba.baijia.modle.RequestCk_SPECDetails;
import com.shenma.yueba.baijia.modle.RequestImMessageInfoBean;
import com.shenma.yueba.baijia.modle.RequestRoomInfo;
import com.shenma.yueba.baijia.modle.RequestRoomInfoBean;
import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;
import com.shenma.yueba.util.NetUtils;
import com.shenma.yueba.util.PhotoUtils;
import com.shenma.yueba.util.SharedUtil;
import com.shenma.yueba.util.SoftKeyboardUtil;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.faceview.FaceView;
import com.shenma.yueba.view.faceview.FaceView.OnChickCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import im.control.SocketManger;
import im.control.SocketObserverManager;
import im.form.BaseChatBean;
import im.form.NoticeChatBean;
import im.form.PicChatBean;
import im.form.PicChatBean.PicChatBean_Listener;
import im.form.ProductChatBean;
import im.form.RequestMessageBean;
import im.form.RoomBean;
import im.form.TextChatBean;
import roboguice.activity.RoboActivity;

/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 聊天页面
 */
public class ChatActivity extends RoboActivity implements OnClickListener,
        OnChickCallback, OnScrollListener, SocketObserverManager.SocketNoticationListener {
    public static final int Result_code_link = 400;// 链接
    public static final int Result_code_collection = 500;// 收藏
    private LinkedList<BaseChatBean> bean_list = new LinkedList<BaseChatBean>();// 消息列表
    private RelativeLayout chat_product_head_layout_include;// 商品信息
    private TextView tv_top_right;// 头部右侧按钮
    private InputMethodManager manager;
    private PullToRefreshListView chat_list;
    private FaceView fView;// 表情列表视图
    private EditText mEditTextContent;// 信息文本框
    private RelativeLayout edittext_layout;// 文本框的父视图对象
    private Button buttonSend;// 发生按钮
    private LinearLayout btnContainer;// 扩展视图（照相 图片 链接 收藏 ）
    private ImageView iv_emoticons_normal;// 表情按钮
    private ProgressBar loadmorePB;// 加载进度条
    private Button btnMore;// 扩展更多按钮
    boolean isloading = false;// 是否加载中
    boolean haveMoreData = true;// 是否有更多的数据可以加载
    private ChattingAdapter chattingAdapter;
    private CreateOrderDialog createOrderDialog;// 创建订单对话框
    private HttpControl httpControl = new HttpControl();
    private boolean ishowStatus = true;
    private int formUser_id;
    private int toUser_id;
    private int circleId;// 圈子id
    private int currPage = Constants.CURRPAGE_VALUE;
    private String roomId = null;
    private String userName = "";
    private String usericon = "";
    private RequestRoomInfo requestRoomInfo;
    private String chat_name = "";
    private TextView tv_top_title;
    private TextView pb_reserttitle_textview;//提示socke重连
    private String littlePicPath;
    private String littlePicPath_cache;
    public static Map<Integer, String> userid_logo = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.getInstance().addActivity(this);// 加入回退栈
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //头文件
        View top_bar = findViewById(R.id.top_bar);
        top_bar.setVisibility(View.VISIBLE);

        MyApplication.getInstance().addActivity(this);
        // 我的 userid
        formUser_id = Integer.parseInt(SharedUtil.getUserId(ChatActivity.this));
        // 我的昵称
        userName = SharedUtil.getUserNames(ChatActivity.this);
        initView();
        // 设置购买商品信息 视图
        setProduct(this.getIntent());
        // 我的头像
        usericon = SharedUtil.getStringPerfernece(this, SharedUtil.user_logo);
        if (this.getIntent().getStringExtra("Chat_NAME") != null)// 名字
        {
            // 圈子名称 或 私聊用户名称
            chat_name = this.getIntent().getStringExtra("Chat_NAME");
            tv_top_title = (TextView) findViewById(R.id.tv_top_title);
            tv_top_title.setText(chat_name);
            tv_top_title.setVisibility(View.VISIBLE);
            FontManager.changeFonts(this, tv_top_title, tv_top_right, mEditTextContent);
        }
        Intent getintent = ChatActivity.this.getIntent();
        //获取圈子id
        circleId = getintent.getIntExtra("circleId", 0);
        //获取roomid
        roomId = getintent.getStringExtra("Chat_RoomID");
        //获取私聊的对方id
        toUser_id = getintent.getIntExtra("toUser_id", 0);

        // 群聊
        if (circleId > 0) {
            // 显示设置
            tv_top_right.setVisibility(View.VISIBLE);
        } else {
            // 隐藏设置
            tv_top_right.setVisibility(View.GONE);

        }
        if (roomId != null) {
            getMessageByRoomID();
        } else if (circleId > -1) {
            getMessageByCircleId();
        } else if (toUser_id > -1) {
            getMessageByToUserId();
        } else {
            finish();
            return;
        }
    }


    /*******
     * 通过用圈子id获取历史数据信息
     ***/
    void getMessageByCircleId() {
        // 获取房间号
        getRoomdId(circleId, formUser_id, toUser_id);
    }


    /*******
     * 通过用户id与 对方ID 获取历史数据信息
     ***/
    void getMessageByToUserId() {
        // 获取房间号
        getRoomdId(circleId, formUser_id, toUser_id);
    }


    /*******
     * 通过房间号 获取历史数据信息
     ***/
    void getMessageByRoomID() {
        // 获取历史消息
        getMessage();
        inroom();
    }


    /**
     * 初始化
     */
    protected void initView() {
        TextView tv_top_left = (TextView) findViewById(R.id.tv_top_left);
        tv_top_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //退出房间
                outRoom();
                finish();
            }
        });

        //
        pb_reserttitle_textview = (TextView) findViewById(R.id.pb_reserttitle_textview);
        pb_reserttitle_textview.getBackground().setAlpha(10);
        showOrHiddenAlertView();

        //商品对象
        chat_product_head_layout_include = (RelativeLayout) findViewById(R.id.chat_product_head_layout_include);
        tv_top_right = (TextView) findViewById(R.id.tv_top_right);
        tv_top_right.setOnClickListener(this);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        chat_list = (PullToRefreshListView) findViewById(R.id.chat_list);
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
        chat_list.setMode(Mode.DISABLED);
        chat_list.setOnScrollListener(this);
        chattingAdapter = new ChattingAdapter(ChatActivity.this, bean_list);
        chat_list.setAdapter(chattingAdapter);

        fView = (FaceView) findViewById(R.id.faceLayout);
        fView.setOnChickCallback(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSend = (Button) findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(this);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new OnClickListener() {
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
        mEditTextContent.setBackgroundResource(R.drawable.shape_linearlayout10);
        edittext_layout.requestFocus();
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideFace();
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                btnContainer.setVisibility(View.GONE);
                pointLast(bean_list.size());
            }
        });
        // 监听文字框
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
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userid_logo.clear();
        SocketObserverManager.getInstance().removeSocketObserver(this);
        outRoom();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocketManger.the().setContext(this);
        SocketObserverManager.getInstance().addSocketObserver(this);
        if (fView.getVisibility() == View.VISIBLE) {
            hideFace();
        }
        inroom();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

/*	*//**
     * 返回
     *
     * @param view
     *//*
    public void back(View view) {
		finish();
	}*/

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (btnContainer.getVisibility() != View.VISIBLE
                && fView.getVisibility() != View.VISIBLE) {
            super.onBackPressed();
        } else {
            if (btnContainer.getVisibility() == View.VISIBLE) {
                btnContainer.setVisibility(View.GONE);
            }
            if (fView.getVisibility() == View.VISIBLE) {
                fView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        outRoom();
        Log.i("TAG", "onNewIntent------->>>>");
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_right:// 设置
                ToolsUtil.forwardCircleActivity(this, circleId, -1);
                break;
            case R.id.iv_emoticons_normal:// 点击显示表情框
                showOrHideIMM();
                break;
            case R.id.btn_send:// 发送消息
                String content = mEditTextContent.getText().toString().trim();
                if ("".equals(content)) {
                    MyApplication.getInstance().showMessage(ChatActivity.this, "发送消息不能为空");
                    return;
                }
                if (!NetUtils.isNetworkConnected(this)) {
                    MyApplication.getInstance().showMessage(ChatActivity.this, "网络不可用");
                    return;
                }
                if (!showOrHiddenAlertView()) {
                    MyApplication.getInstance().showMessage(ChatActivity.this, "通信已断开，正在重连");
                    return;
                }
                setTextMsgData(content);
                mEditTextContent.setText("");
                break;
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
            default:
                break;
        }

    }

    /*****
     * 设置 文本信息内容
     ****/
    void setTextMsgData(String str) {
        BaseChatBean msgbean = new TextChatBean(ChatActivity.this);
        setSendValue(msgbean, str);
    }

    /***
     * 发送链接或收藏数据
     ***/
    void sendLinkOrCollect(List<BaiJiaShareInfoBean> check_list) {
        if (check_list != null) {
            for (int i = 0; i < check_list.size(); i++) {
                BaiJiaShareInfoBean sharebean = check_list.get(i);
                BaseChatBean msgbean = new ProductChatBean(ChatActivity.this);
                msgbean.setProductId(sharebean.getId());
                setSendValue(msgbean,ToolsUtil.nullToString(sharebean.getLogo()));

            }
        }
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

        //如果当前不在房间内 则加入房间
        if(!SocketObserverManager.getInstance().isJoinRoom())
        {
            inroom();
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

    // 定位到知道位置
    void pointLast(int i) {
        if (i <= bean_list.size()) {
            if (chat_list != null) {
                chat_list.getRefreshableView().setSelection(i);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    setResult(Constants.RESULTCODE);
                    ChatActivity.this.finish();
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
        if (ToolsUtil.isAvailableSpace(ChatActivity.this)) {
            littlePicPath = PhotoUtils.takePicture(ChatActivity.this);
            Log.i("TAG", "littlePicPath:" + littlePicPath);
        }

    }

    /****
     * 相机回调
     **/
    void cameraCallBack(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            if (littlePicPath != null) {
                upLoadPic(littlePicPath);
            }
        }
    }

    /****
     * 打开照图片
     **/
    void openPicture() {
        if (ToolsUtil.isAvailableSpace(ChatActivity.this)) {
            PhotoUtils.selectPhoto(ChatActivity.this);
        }
    }

    /**
     * 裁剪返回
     */
    void clipCallBack(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
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
        if (resultCode == RESULT_OK) {
            if (data.getData() == null) {
                return;
            }
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    littlePicPath = cursor.getString(column_index);
                    if (littlePicPath != null) {
                        upLoadPic(littlePicPath);
                    }
					/*
					 * // 裁剪之后存储的路径 littlePicPath_cache = Environment
					 * .getExternalStorageDirectory().toString() +
					 * File.separator + UUID.randomUUID().toString() +
					 * "littlePic.jpg"; // 裁剪图片
					 * startActivityForResult(PhotoUtils.getZoomIntent(Uri
					 * .fromFile(new File(littlePicPath)), Uri
					 * .fromFile(FileUtils
					 * .createNewFile(littlePicPath_cache))),
					 * PhotoUtils.INTENT_REQUEST_CODE_CROP);
					 */
                }
            }
        }
    }

    /****
     * 链接
     **/
    void openLink() {
        Intent intent = new Intent(ChatActivity.this,
                BaiJiaShareDataActivity.class);
        intent.putExtra("TYPE", BaiJiaShareDataActivity.LINK);
        startActivityForResult(intent, Result_code_link);
    }

    /****
     * 收藏
     **/
    void openCollention() {
        Intent intent = new Intent(ChatActivity.this,
                BaiJiaShareDataActivity.class);
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

    /*****
     * 通过阿里云上传
     ***/
    void upLoadPic(String imageLocalPath) {
        if (imageLocalPath == null || imageLocalPath.equals("")) {
            return;
        }
        final PicChatBean baseChatBean = new PicChatBean(ChatActivity.this);
        baseChatBean.setListener(new PicChatBean_Listener() {

            @Override
            public void pic_showMsg(final String msg) {
                ChatActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        MyApplication.getInstance().showMessage(
                                ChatActivity.this, msg);
                    }
                });

            }

            @Override
            public void pic_notifaction() {
                ChatActivity.this.runOnUiThread(new Runnable() {

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

    /**
     * 显示表情
     */
    private void showFace() {
        iv_emoticons_normal
                .setBackgroundResource(R.drawable.chatting_biaoqing_btn_enable);
        iv_emoticons_normal.setTag(1);
        fView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情
     */
    private void hideFace() {
        iv_emoticons_normal
                .setBackgroundResource(R.drawable.chatting_biaoqing_btn_normal);
        iv_emoticons_normal.setTag(null);
        fView.setVisibility(View.GONE);
    }

    /**
     * 控制显示或者隐藏表情
     */
    private void showOrHideIMM() {
        if (iv_emoticons_normal.getTag() == null) {
            // 隐藏软键
            SoftKeyboardUtil.hide(ChatActivity.this, mEditTextContent);
            // 显示表情
            showFace();
            // 隐藏其他底部view
            btnContainer.setVisibility(View.GONE);
        } else {
            // 显示软键盘
            SoftKeyboardUtil.show(ChatActivity.this, mEditTextContent);
            // 隐藏表情
            hideFace();
        }
        pointLast(bean_list.size());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SoftKeyboardUtil.hide(ChatActivity.this,
                ChatActivity.this.getCurrentFocus());
        int height = fView.getTop();
        int y = (int) event.getY() + 50;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (y < height) {
                if (fView.getVisibility() == View.VISIBLE) {
                    hideFace();
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onChick(int arg1, int resId, String arg2) {
        SpannableString ss = new SpannableString(arg2);
        Drawable d = getResources().getDrawable(resId);
        d.setBounds(0, 0, 50, 50);// 设置表情图片的显示大小
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        ss.setSpan(span, 0, arg2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditTextContent.getText().insert(mEditTextContent.getSelectionStart(),
                ss);
    }

    /*****
     * 设置商品信息(如果存在商品信息)
     ***/
    void setProduct(Intent intent) {
        // 判断是否传递了商品的信息
        if (intent.getSerializableExtra("DATA") == null) {
            chat_product_head_layout_include.setVisibility(View.GONE);
            return;
        } else {
            chat_product_head_layout_include.setVisibility(View.VISIBLE);
        }

        // 产品图片
        ImageView chat_product_head_layout_imageview = (ImageView) findViewById(R.id.chat_product_head_layout_imageview);
        // 产品名称
        TextView chat_product_head_layout_name_textview = (TextView) findViewById(R.id.chat_product_head_layout_name_textview);
        // 价格
        TextView chat_product_head_layout_price_textview = (TextView) findViewById(R.id.chat_product_head_layout_price_textview);
        // 立即购买
        Button chat_product_head_layout_button = (Button) findViewById(R.id.chat_product_head_layout_button);
        chat_product_head_layout_button.setEnabled(false);
        chat_product_head_layout_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (createOrderDialog != null) {
                    createOrderDialog.cancel();
                }
                RequestCKProductDeatilsInfo bean = (RequestCKProductDeatilsInfo) ChatActivity.this.getIntent().getSerializableExtra("DATA");
                RequestCk_SPECDetails standard_data = (RequestCk_SPECDetails) ChatActivity.this.getIntent().getSerializableExtra("standard_data");
                createOrderDialog = new CreateOrderDialog(ChatActivity.this, bean, standard_data);
                createOrderDialog.show();
            }
        });
        Object obj = this.getIntent().getSerializableExtra("DATA");
        if (obj != null && obj instanceof RequestCKProductDeatilsInfo) {
            RequestCKProductDeatilsInfo bean = (RequestCKProductDeatilsInfo) obj;
            CKProductDeatilsInfoBean productsDetailsInfoBean = bean.getData();
            if (productsDetailsInfoBean != null) {
                List<ProductsDetailsTagInfo> productsDetailsTagInfo_list = productsDetailsInfoBean
                        .getProductPic();
                chat_product_head_layout_button.setTag(bean);
                if (productsDetailsTagInfo_list != null
                        && productsDetailsTagInfo_list.size() > 0) {
                    initBitmap(ToolsUtil.nullToString(ToolsUtil.getImage(ToolsUtil.nullToString(productsDetailsTagInfo_list.get(0).getLogo()), 320, 0)), chat_product_head_layout_imageview);
                }

                chat_product_head_layout_name_textview
                        .setText(ToolsUtil.nullToString(productsDetailsInfoBean
                                .getProductName()));
                chat_product_head_layout_price_textview
                        .setText(ToolsUtil.nullToString("￥"
                                + ToolsUtil.DounbleToString_2(productsDetailsInfoBean.getPrice())));
                chat_product_head_layout_button.setEnabled(true);
            }

        } else {
            chat_product_head_layout_include.setVisibility(View.GONE);
            chat_product_head_layout_button.setEnabled(false);
        }
        FontManager.changeFonts(this, chat_product_head_layout_name_textview,
                chat_product_head_layout_name_textview,
                chat_product_head_layout_price_textview,
                chat_product_head_layout_button);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    loadmorePB.setVisibility(View.VISIBLE);// 显示 加载视图
                    // 从网络获取消息数据
                    getMessage();
            }

        }
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


    void notification(final BaseChatBean baseChatBean,
                      final RequestMessageBean bean) {
        ChatActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (baseChatBean != null) {
                    baseChatBean.setValue(bean);
                    // 讲接收到的信息加入到列表并刷新列表
                    addListData(false, baseChatBean);
                }

                // chat_list.getRefreshableView().setSelection(bean_list.size());
                // pointLast(chat_list.getRefreshableView().getCount() - 1);
                pointLast(bean_list.size());

            }
        });
    }

    /****
     * 加入房间
     **/
    void inroom() {
        if (roomId == null || roomId.equals("")) {
            return;
        }
        // 加入房间
        RoomBean roomBean = new RoomBean();
        roomBean.setRoom_id(roomId);
        roomBean.setUserName(userName);
        SocketManger.the().inroon(Integer.toString(formUser_id), roomBean);

        if (requestRoomInfo != null) {
            Log.i("TAG", "socketio---->>已经与服务器建立链接");

            Log.i("TAG", "socketio---->>加入房间 roomId=" + roomId);
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

    /****
     * @param groupId  int 圈子id
     * @param fromUser int
     * @param toUser   int
     **/
    void getRoomdId(int groupId, int fromUser, int toUser) {
        if(fromUser<toUser)
        {
          roomId= fromUser+"_"+toUser;
        }else
        {
            roomId= toUser+"_"+fromUser;
        }
        getMessage();// 获取历史数据
        Log.i("TAG", "---->>>socket roomId:" + roomId);
        inroom();
    }

    /****
     * 获取聊天记录
     *
     * @param roomId        int 房间id
     * @param lastMessageId int 信息id 小于0 可不传
     * @param page          int 访问页数
     * @param pageSize      int 每页大小
     ***/
    void getMessageRecord(String roomId, int lastMessageId, final int page,
                          int pageSize) {
        httpControl.getRoomMessage(roomId, lastMessageId, page, pageSize,
                ishowStatus, new HttpCallBackInterface() {

                    @Override
                    public void http_Success(Object obj) {
                        currPage = page;
                        chat_list.onRefreshComplete();
                        if (obj != null
                                && obj instanceof RequestImMessageInfoBean) {
                            RequestImMessageInfoBean messagebean = (RequestImMessageInfoBean) obj;
                            int allPage = messagebean.getData().getTotalpaged();
                            if (currPage >= allPage) {
                                haveMoreData = false;
                                chat_list.setMode(Mode.DISABLED);
                            } else {
                                haveMoreData = true;
                                currPage++;
                            }
                            dataSuceeValue(messagebean.getData().getItems());
                        } else {
                            http_Fails(500, "获取失败");
                            isloading = false;
                            ishowStatus = false;
                            loadmorePB.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void http_Fails(int error, String msg) {
                        MyApplication.getInstance().showMessage(
                                ChatActivity.this, msg);
                        chat_list.onRefreshComplete();
                        isloading = false;
                        loadmorePB.setVisibility(View.GONE);
                    }
                }, this);

    }

    /*****
     * 数据加载完成赋值
     ****/
    void dataSuceeValue(List<RequestMessageBean> items) {
        isloading = false;
        ishowStatus = false;
        loadmorePB.setVisibility(View.GONE);
        if (items == null || items.size() == 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            String type = Integer.toString(items.get(i).getMessageType());
            if (type.equals(RequestMessageBean.type_img))// 如果是 图片
            {
                BaseChatBean bean = new PicChatBean(ChatActivity.this);
                bean.setValue(items.get(i));
                bean.setMessageType(0);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_produtc_img))// 是商品图片
            {
                BaseChatBean bean = new ProductChatBean(ChatActivity.this);
                bean.setValue(items.get(i));
                bean.setMessageType(0);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.notice))// 广播
            {
                BaseChatBean bean = new NoticeChatBean(ChatActivity.this);
                bean.setValue(items.get(i));
                bean.setMessageType(0);
                addListData(true, bean);
            } else if (type.equals(RequestMessageBean.type_empty))// 文本信息
            {
                BaseChatBean bean = new TextChatBean(ChatActivity.this);
                bean.setValue(items.get(i));
                bean.setMessageType(0);
                addListData(true, bean);
            }
        }
		/*
		 * if(items.size()<=bean_list.size()) {
		 * chat_list.getRefreshableView().setSelection(items.size()); }
		 */

        pointLast(items.size());

        // chattingAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketManger.the().setContext(null);
    }


    /*****
     * 离开房间
     ***/
    void outRoom() {
        SocketManger.the().outinroon();
    }


    void initBitmap(final String url, final ImageView iv) {
        MyApplication.getInstance().getBitmapUtil().display(iv, url);
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


    @Override
    public void socketConnectSucess() {
        showOrHiddenAlertView();
    }

    @Override
    public void socketConnectFails() {
        showOrHiddenAlertView();
        //将当前 正在发送的消息 状态置为 发布失败
        if(bean_list!=null)
        {
            for(int i=0;i<bean_list.size();i++)
            {
                switch(bean_list.get(i).getSendStatus())
                {
                    case send_loading:
                        bean_list.get(i).setSendStatus(BaseChatBean.SendStatus.send_fails);
                        break;
                }
            }
            sendStatusChaneg();
        }
    }

    @Override
    public void receiveMsgFromRoom(RequestMessageBean bean) {
        if (bean != null ) {
            if(!roomId.equals(bean.getRoomId()))
            {
                return;
            }

            BaseChatBean baseChatBean = null;
            String type = Integer.toString(bean.getMessageType());
            if (type.equals(RequestMessageBean.type_img))// 如果是图片
            {
                baseChatBean = new PicChatBean(ChatActivity.this);
            } else if (type.equals(RequestMessageBean.type_produtc_img))// 如果是商品图片
            {
                baseChatBean = new ProductChatBean(ChatActivity.this);
            } else if (type.equals(RequestMessageBean.notice))// 如果是广播
            {

            } else {
                baseChatBean = new TextChatBean(ChatActivity.this);

            }
            // 通知更新
            notification(baseChatBean, bean);
        }
    }

    @Override
    public void receiveMsgFromUnRoom(RequestMessageBean bean) {

    }

    @Override
    public void sendStatusChaneg() {
        ChatActivity.this.runOnUiThread(new Runnable() {

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


