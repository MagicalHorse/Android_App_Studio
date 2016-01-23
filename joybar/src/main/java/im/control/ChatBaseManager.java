package im.control;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.ToolsUtil;
import com.shenma.yueba.view.RoundImageView;

import im.form.BaseChatBean;


/**
 * @author gyj
 * @version 创建时间：2015-7-3 下午5:37:09
 *          程序的简单说明   本类定义通信管理基础类  用于定义 通信的通用方法 以及通用对象的创建
 *          该类是抽象了  需要子类实现 抽象的方法
 */

public abstract class ChatBaseManager {
    HttpControl httpControl = new HttpControl();
    Context context;
    TextView chat_layout_item_msg_time_textview;//时间
    TextView chat_layout_item_msg_name_textview;//名字
    RoundImageView chat_layout_item_img_icon_roundimageview;//头像
    ImageView chat_layout_item_msg_gantanhao_imageview;//感叹号 点击重新发送信息
    ProgressBar chat_layout_item_msg_gantanhao_progressbar;//进度条
    View parentView;

    public enum ChatView_Type {
        left,
        right;
    }


    public ChatBaseManager(Context context) {
        this.context = context;
    }

    /*****
     * 通用对象的初始化方法  需要传递父视图的对象
     *
     * @param v View 父视图的对象
     ****/
    public void parentinitView(View v) {
        if (v != null) {
            parentView = v;
            chat_layout_item_msg_time_textview = (TextView) v.findViewById(R.id.chat_layout_item_msg_time_textview);
            chat_layout_item_msg_name_textview = (TextView) v.findViewById(R.id.chat_layout_item_msg_name_textview);
            chat_layout_item_img_icon_roundimageview = (RoundImageView) v.findViewById(R.id.chat_layout_item_img_icon_roundimageview);
            /*chat_layout_item_img_icon_roundimageview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(v.getTag()!=null && v.getTag() instanceof BaseChatBean)
					{
						BaseChatBean chatBean=(BaseChatBean)v.getTag();
						if(!chatBean.isIsoneself())
						{
							ToolsUtil.forwardShopMainActivity(context,chatBean.getFrom_id());
						}
					}
				}
			});*/

            chat_layout_item_msg_gantanhao_imageview = (ImageView) v.findViewById(R.id.chat_layout_item_msg_gantanhao_imageview);
            if (chat_layout_item_msg_gantanhao_imageview != null) {
                chat_layout_item_msg_gantanhao_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getTag() != null) {
                            BaseChatBean baseChatBean = (BaseChatBean) v.getTag();
                            if (baseChatBean.getSendStatus() == BaseChatBean.SendStatus.send_fails) {
                                baseChatBean.setSendStatus(BaseChatBean.SendStatus.send_loading);
                                SocketObserverManager.getInstance().Notication(SocketObserverManager.SocketObserverType.sendstauts);
                                baseChatBean.sendData();
                            }
                        }
                    }
                });
            }
            chat_layout_item_msg_gantanhao_progressbar = (ProgressBar) v.findViewById(R.id.chat_layout_item_msg_gantanhao_progressbar);
            ToolsUtil.setFontStyle(context, v, R.id.chat_layout_item_msg_time_textview, R.id.chat_layout_item_msg_name_textview);
        }
    }

    /******
     * 显示或隐藏 对象的方法  该方法中 调用了 子对象的 child_isshow()方法
     * 用于进行赋值 操作
     *
     * @param b    boolan 是否显示   true 是  false
     * @param bean BaseChatBean  传递的参数对象
     ****/
    public void isshow(boolean b, BaseChatBean bean) {
        if (b) {
            parentView.setVisibility(View.VISIBLE);
        } else {
            parentView.setVisibility(View.GONE);
        }
        chat_layout_item_msg_time_textview.setText(ToolsUtil.nullToString(bean.getCreationDate()));
        chat_layout_item_msg_name_textview.setText(ToolsUtil.nullToString(bean.getUserName()));
        //chat_layout_item_img_icon_roundimageview.setTag(bean);
        child_isshow(b, bean);
        initBitmap(ToolsUtil.nullToString(bean.getLogo()));
        if (chat_layout_item_msg_gantanhao_imageview != null) {
            chat_layout_item_msg_gantanhao_imageview.setTag(bean);
        }
        switch (bean.getSendStatus()) {
            case send_unsend:
            case send_loading:
                setShowOrHiddenProgressbar(true);
                setShowOrHiddenGanTanHan(false);
                break;
            case send_fails:
                setShowOrHiddenProgressbar(false);
                setShowOrHiddenGanTanHan(true);
                break;
            case send_sucess:
                setShowOrHiddenProgressbar(false);
                setShowOrHiddenGanTanHan(false);
                break;

        }
        Log.i("TAG", "socket isshow SendStatus：" + bean.getSendStatus() + "  body:" + bean.getBody());
    }


    void setShowOrHiddenProgressbar(boolean b) {
        if (chat_layout_item_msg_gantanhao_progressbar != null) {
            if (b) {
                chat_layout_item_msg_gantanhao_progressbar.setVisibility(View.VISIBLE);
            } else {
                chat_layout_item_msg_gantanhao_progressbar.setVisibility(View.INVISIBLE);
            }
        }
    }


    void setShowOrHiddenGanTanHan(boolean b) {
        if (chat_layout_item_msg_gantanhao_imageview != null) {
            if (b) {
                chat_layout_item_msg_gantanhao_imageview.setVisibility(View.VISIBLE);
            } else {
                chat_layout_item_msg_gantanhao_imageview.setVisibility(View.INVISIBLE);
            }
        }
    }

    /*****
     * 初始化视图
     *
     * @param v View 父视图对象
     */
    public abstract void initView(View v);

    /****
     * 是否显示视图   并赋值（子视图对象须实现该方法）
     *
     * @param b    boolean 是否显示   true 是  false
     * @param bean BaseChatBean  传递的参数对象
     */
    public abstract void child_isshow(boolean b, BaseChatBean bean);

    void initBitmap(String logo) {
        if (chat_layout_item_img_icon_roundimageview != null) {
            MyApplication.getInstance().getBitmapUtil().display(chat_layout_item_img_icon_roundimageview, ToolsUtil.nullToString(logo));
        }
    }
}
