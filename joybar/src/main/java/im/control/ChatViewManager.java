package im.control;

import com.shenma.yueba.R;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import im.control.ChatBaseManager.ChatView_Type;
import im.form.BaseChatBean;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-3 下午3:55:57  
 * 程序的简单说明  该类 定义 im 视图布局的管理类  用于初始化视图的布局 以及控制 所要显示 布局的样式
 * 该类 主要用于 ListView 对应的Item数据  每个ITME数据  包含不同样式的 布局  根据 类型  显示对应的样式
 * 
 */

public class ChatViewManager {
	LinkViewManager leftlinkViewManager;//左部分 分享样式
	LinkViewManager rightlinkViewManager;//右部分分享样式
	TextViewManager leftTextViewManager;//左部分文本样式
	TextViewManager rightTextViewManager;//右部分文本样式
	PicViewManager leftPicViewManager;//左部分 图片样式
	PicViewManager rightPicViewManager;//右部分 图片样式
	Context context;

	public ChatViewManager(Context context)
	{
		//初始化各样式对象
		this.context=context;
		leftlinkViewManager=new LinkViewManager(context,LinkViewManager.ChatView_Type.left);
		rightlinkViewManager=new LinkViewManager(context,LinkViewManager.ChatView_Type.right);
		leftTextViewManager=new TextViewManager(context,TextViewManager.ChatView_Type.left);
		rightTextViewManager=new TextViewManager(context,TextViewManager.ChatView_Type.right);
		leftPicViewManager=new PicViewManager(context,ChatView_Type.left);
		rightPicViewManager=new PicViewManager(context,ChatView_Type.right);
	}
	
	
	/******
	 * 根据消息类型  设置显示消息视图的样式
	 * @param bean BaseChatBean 
	 * @param view View 
	 * **/
	public  void setView(final BaseChatBean bean,final View view)
	{
		if(bean==null || view ==null)
		{
			new Throwable("bean=null or view=null",new NullPointerException());
		}
		//加载每种 样式的 父视图对象 
		View chat_layout_item_leftlink_include=view.findViewById(R.id.chat_layout_item_linkleft_include);//左链接
		View chat_layout_item_rightlink_include=view.findViewById(R.id.chat_layout_item_linkright_include);//左链接
		View chat_layout_item_leftmsg_include=view.findViewById(R.id.chat_layout_item_leftmsg_include);//左聊天
		View chat_layout_item_rightmsg_include=view.findViewById(R.id.chat_layout_item_rightmsg_include);//右聊天
		View chat_layout_item_rightimg_include=view.findViewById(R.id.chat_layout_item_rightimg_include);//右图片
		View chat_layout_item_leftimg_include=view.findViewById(R.id.chat_layout_item_leftimg_include);//左图片
		//隐藏所有样式
		chat_layout_item_leftlink_include.setVisibility(View.GONE);
		chat_layout_item_rightlink_include.setVisibility(View.GONE);
		chat_layout_item_leftmsg_include.setVisibility(View.GONE);
		chat_layout_item_rightimg_include.setVisibility(View.GONE);
		chat_layout_item_leftimg_include.setVisibility(View.GONE);
		chat_layout_item_rightmsg_include.setVisibility(View.GONE);
		//根据消息的类型 设置 消息对应的布局样式
		switch(bean.getChattype())
		{
		//根据 类型 设置 当前 消息 对应的 视图样式
		case link_type://链接信息
			if(bean.isoneself())
			{
				bean.setBaseManager(rightlinkViewManager);
			}else
			{
				bean.setBaseManager(leftlinkViewManager);
			}
			
			break;
		case text_trype:///文本信息
			if(bean.isoneself())
			{
				bean.setBaseManager(rightTextViewManager);
			}else
			{
				bean.setBaseManager(leftTextViewManager);
			}
			break;
		case pic_type://图片信息
			if(bean.isoneself())
			{
				bean.setBaseManager(rightPicViewManager);
			}else
			{
				bean.setBaseManager(leftPicViewManager);
			}
			break;
			default://默认文本信息
			if(bean.isoneself())
			{
				bean.setBaseManager(rightTextViewManager);
			}else
			{
				bean.setBaseManager(leftTextViewManager);
			}
			break;
				
		}
		//从 消息中获取 对应的样式 并显示出来
		bean.getBaseManager().isshow(true,bean);
	}

	/*****
	 * 加载  im布局 样式  并 初始化
	 * **/
	public View initView()
	{
		RelativeLayout parentView=(RelativeLayout)RelativeLayout.inflate(context, R.layout.chat_layout_item, null);
		
		leftlinkViewManager.initView(parentView);//左链接初始化
		rightlinkViewManager.initView(parentView);//右链接初始化
		
		leftTextViewManager.initView(parentView);//初始化左聊天
		
		rightTextViewManager.initView(parentView);//右侧始化左聊天
		
		leftPicViewManager.initView(parentView);//左侧图片聊天
		rightPicViewManager.initView(parentView);//右侧图片聊天
		return parentView;
	}
}
