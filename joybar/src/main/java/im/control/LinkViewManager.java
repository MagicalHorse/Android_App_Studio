package im.control;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import im.form.BaseChatBean;

/**
 * @author gyj
 * @version 创建时间：2015-7-3 下午4:21:21 程序的简单说明
 * 本类 定义 im 分享链接的 视图管理类 用于初始化布局对象 并赋值
 */

public class LinkViewManager extends ChatBaseManager {

	/*******
	 * @param type  TextView_Type 
	 * ***/
	public LinkViewManager(Context context, ChatView_Type type) {
		super(context);
		this.type = type;

	}
	
	LinearLayout chat_item_content_linearlayout;//内容布局的对象
	ChatView_Type type = ChatView_Type.left;
	RelativeLayout ll;//加载的视图对象
	TextView chat_layout_item_leftmsg_msg_textview;// 内容
	ImageView chat_layout_item_leftmsg_productimg_textview;// 商品图片

	/*******
	 * 初始化视图
	 * ***/
	public void initView(View view) {
		if (view == null) {
			new Throwable("holder=null or view =null",new NullPointerException());
		}
		switch (type) {
		case left://加载左视图布局
			ll = (RelativeLayout) view.findViewById(R.id.chat_layout_item_linkleft_include);
			break;
		case right://加载右视图布局
			ll = (RelativeLayout) view.findViewById(R.id.chat_layout_item_linkright_include);
			break;
		}
		//设置 通用布局对象的初始化
		parentinitView(ll);
		infaterView();
	}

	/***
	 * 初始化 内容信息 初始化 图片
	 * ***/
	void infaterView() {
		chat_item_content_linearlayout=(LinearLayout)ll.findViewById(R.id.chat_item_content_linearlayout);
        chat_item_content_linearlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v.getTag()!=null)
				{
					//点击内容区域 进行相应操作
					BaseChatBean bean=(BaseChatBean)v.getTag();
					ToolsUtil.forwardProductInfoActivity(context,bean.getProductId());
				}
				
			}
		});
		chat_layout_item_leftmsg_msg_textview = (TextView) ll.findViewById(R.id.chat_layout_item_leftmsg_msg_textview);
		chat_layout_item_leftmsg_productimg_textview = (ImageView) ll.findViewById(R.id.chat_layout_item_leftmsg_productimg_textview);

		FontManager.changeFonts(context,chat_layout_item_leftmsg_msg_textview,chat_layout_item_leftmsg_productimg_textview);
	}

	/*****
	 *  给对象赋值
	 * ***/
	public void child_isshow(boolean b, BaseChatBean bean) {
		chat_item_content_linearlayout.setTag(bean);
		String contecnt_str=ToolsUtil.getImage(ToolsUtil.nullToString((String)bean.getBody()), 320, 0);
		chat_layout_item_leftmsg_msg_textview.setText(ToolsUtil.analysisFace(context,contecnt_str),BufferType.SPANNABLE);
		initBitmap(contecnt_str, chat_layout_item_leftmsg_productimg_textview);
	}
	
	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
