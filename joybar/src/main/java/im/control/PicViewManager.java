package im.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.shenma.yueba.R;
import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.baijia.activity.TouchImageViewActivity;
import com.shenma.yueba.util.ToolsUtil;

import java.util.ArrayList;

import im.form.BaseChatBean;
import im.form.PicChatBean;

/**
 * @author gyj
 * @version 创建时间：2015-7-3 下午5:10:04  
 * 程序的简单说明  本类定义 iM 图片数据的视图管理 
 * 包含图片的上传以及显示
 */

public class PicViewManager extends ChatBaseManager implements OnClickListener{

	public PicViewManager(Context context,ChatView_Type type) {
		super(context);
		this.type=type;
	}
	ChatView_Type type= ChatView_Type.left;
	ImageView chat_layout_item_leftimg_msg_imageview;//图片
	LinearLayout chat_layout_item_leftimg_progress_linearlayout;//进度父视图
	ProgressBar chat_layout_item_leftimg_progress_progressbar;//进度条
	TextView chat_layout_item_leftimg_progress_textview;//进度文本

	RelativeLayout ll;

	/*******
	 * 初始化左侧视图
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
				ll=(RelativeLayout)view.findViewById(R.id.chat_layout_item_leftimg_include);
				break;
			case right:
				ll=(RelativeLayout)view.findViewById(R.id.chat_layout_item_rightimg_include);
				break;
		}
		//通用对象赋值
		parentinitView(ll);
		chat_layout_item_leftimg_msg_imageview=(ImageView)ll.findViewById(R.id.chat_layout_item_leftimg_msg_imageview);
		chat_layout_item_leftimg_msg_imageview.setOnClickListener(this);

		chat_layout_item_leftimg_progress_linearlayout=(LinearLayout)ll.findViewById(R.id.chat_layout_item_leftimg_progress_linearlayout);
		chat_layout_item_leftimg_progress_progressbar=(ProgressBar)ll.findViewById(R.id.chat_layout_item_leftimg_progress_progressbar);
		chat_layout_item_leftimg_progress_textview=(TextView)ll.findViewById(R.id.chat_layout_item_leftimg_progress_textview);
	}


	@Override
	public void child_isshow(boolean b,BaseChatBean bean) {

		if(bean!=null && bean instanceof PicChatBean)
		{
			PicChatBean picbean=(PicChatBean)bean;
			chat_layout_item_leftimg_msg_imageview.setTag(bean);
			String user_logo=picbean.getLogo();
			//如果 图片上传完成 则 显示 图片
			if(picbean.isSuccess())
			{
				initBitmap(ToolsUtil.nullToString((String) picbean.getBody()), chat_layout_item_leftimg_msg_imageview);
			}

			//如果图片上传完成 则隐藏进度条 否则显示进度条
			if(!picbean.isUpload() && picbean.isSuccess())//如果上传完成则  隐藏进度
			{
				chat_layout_item_leftimg_progress_linearlayout.setVisibility(View.GONE);
			}else
			{
				chat_layout_item_leftimg_progress_linearlayout.setVisibility(View.VISIBLE);
			}

			//计算上传进度
			int currcount=picbean.getProgress();
			int maxcount=picbean.getMaxProgress();
			int value=(int)(((float)currcount/(float)maxcount)*100);
			if(value>=100)
			{
				value=99;
			}
			chat_layout_item_leftimg_progress_textview.setText(value+"%");
		}

	}


	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.chat_layout_item_leftimg_msg_imageview:
				PicChatBean picbean=(PicChatBean)v.getTag();
				Intent intent=new Intent(context,TouchImageViewActivity.class);
				if(picbean.isSuccess())
				{
					ArrayList<String> str_array=new ArrayList<String>();
					str_array.add(picbean.getBody());
					intent.putStringArrayListExtra("IMG_URL", str_array);
					intent.putExtra("CurrID",0);
					context.startActivity(intent);
					((Activity)context).overridePendingTransition(R.anim.img_scale_in, R.anim.img_scale_out);
				}

				break;
		}
	}

	void initBitmap(final String url, final ImageView iv) {
		MyApplication.getInstance().getBitmapUtil().display(iv, url);
	}
}
