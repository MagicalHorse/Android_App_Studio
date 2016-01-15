package im.control;

import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.util.FontManager;
import com.shenma.yueba.util.ToolsUtil;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import im.form.BaseChatBean;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-3 下午5:10:04  
 * 程序的简单说明   本类定义文本消息管理类 用于初始化 文本消息样式 
 */

public class TextViewManager extends ChatBaseManager{
	
	/*****
	 * @param type ChatView_Type
	 * ***/
	public TextViewManager(Context context,ChatView_Type type ) {
		super(context);
		this.type=type;
		
	}
	ChatView_Type type=ChatView_Type.left;
	TextView chat_layout_item_leftmsg_msg_textview;//信息
	RelativeLayout ll;
	/*******
	 * 初始化视图
	 * ***/
	public  void initView(View view)
	{
		if(view==null)
		{
			new Throwable("holder=null or view =null", new NullPointerException());
		}
		switch(type)
		{
		case left:
			ll=(RelativeLayout)view.findViewById(R.id.chat_layout_item_leftmsg_include);
			break;
		case right:
			ll=(RelativeLayout)view.findViewById(R.id.chat_layout_item_rightmsg_include);
			break;
		}
		parentinitView(ll);
		infaterView();
	}
	
	void infaterView()
	{
		chat_layout_item_leftmsg_msg_textview=(TextView)ll.findViewById(R.id.chat_layout_item_leftmsg_msg_textview);
		FontManager.changeFonts(context, chat_layout_item_leftmsg_msg_textview);
	}
	
	
	@Override
	public void child_isshow(boolean b,BaseChatBean bean) {
		
		chat_layout_item_leftmsg_msg_textview.setText(ToolsUtil.analysisFace(context,(String)bean.getBody()),BufferType.SPANNABLE);

	}
	
	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
