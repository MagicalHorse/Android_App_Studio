package im.form;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.shenma.yueba.baijia.modle.RequestUploadChatImageInfo;
import com.shenma.yueba.baijia.modle.RequestUploadChatImageInfoBean;
import com.shenma.yueba.util.HttpControl;
import com.shenma.yueba.util.HttpControl.HttpCallBackInterface;

import android.content.Context;
import im.control.SocketManger;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-6 上午11:13:48  
 * 程序的简单说明  图片数据
 */

public class PicChatBean extends BaseChatBean{
int progress=0;//上传进度
int maxProgress=0;//总进度
boolean isUpload=false;//是否上传或下载中
boolean isSuccess=true;//是否完成
boolean isFails=false;//是否上传失败
String picaddress="";//本地图片的地址
String ali_content;//阿里云返回的数据
PicChatBean_Listener listener;
	HttpControl httpControl;
	public PicChatBean_Listener getListener() {
	return listener;
}

public void setListener(PicChatBean_Listener listener) {
	this.listener = listener;
}

	public String getAli_content() {
	return ali_content;
}

public void setAli_content(String ali_content) {
	this.ali_content = ali_content;
}

	public int getProgress() {
	return progress;
}

public void setProgress(int progress) {
	this.progress = progress;
}

public int getMaxProgress() {
	return maxProgress;
}

public void setMaxProgress(int maxProgress) {
	this.maxProgress = maxProgress;
}

public boolean isUpload() {
	return isUpload;
}

public void setUpload(boolean isUpload) {
	this.isUpload = isUpload;
}

public boolean isSuccess() {
	return isSuccess;
}

public void setSuccess(boolean isSuccess) {
	this.isSuccess = isSuccess;
}

public String getPicaddress() {
	return picaddress;
}

public void setPicaddress(String picaddress) {
	this.picaddress = picaddress;
}

	public PicChatBean(Context context) {
		super(ChatType.pic_type,RequestMessageBean.type_img,context);
	}
	@Override
	public void sendData() {
		UploadAlI();//上传阿里云
	}

	@Override
	public void setValue(Object obj) {
		super.setParentView(obj);
	}
	
	/******
	 * 发送通知
	 * ****/
	void sendIm(String url)
	{
		content=url;
		MessageBean bean=getMessageBean();
		SocketManger.the().sendMsg(bean);
		if(listener!=null)
		{
			listener.pic_notifaction();
		}
	}
	
	
	/****
	 * 上传阿里云成功后 从服务器获取图片地址
	 * **/
	 void uploadPic()
	{
		if(httpControl==null)
		{
			httpControl=new HttpControl();
		}
		httpControl.getUploadChatImage(ali_content, false, new HttpCallBackInterface() {
			
			@Override
			public void http_Success(Object obj) {
				if(obj!=null && obj instanceof RequestUploadChatImageInfoBean)
				{
					RequestUploadChatImageInfoBean uploadbean=(RequestUploadChatImageInfoBean)obj;
					if(uploadbean.getData()!=null)
					{
						RequestUploadChatImageInfo bean=uploadbean.getData();
						String url=bean.getImageurl();
						//发送数据通知
						sendIm(url);
						isSuccess=true;
						if(listener!=null)
						{
							listener.pic_notifaction();
						}
					}else
					{
						http_Fails(500, "上传成功获取地址失败");
					}
				}
			}
			
			@Override
			public void http_Fails(int error, String msg) {
				if(listener!=null)
				{
					listener.pic_showMsg(msg);
				}
			}
		}, context);
	}
	
	/****
	 * 上传阿里
	 * **/
	void UploadAlI()
	{
		if(httpControl==null)
		{
			httpControl=new HttpControl();
		}
		httpControl.syncUpload(picaddress, new SaveCallback() {
			
			@Override
			public void onProgress(String arg0, int arg1, int arg2) {
				//上传进度
				isFails=false;
				progress=arg1;
				maxProgress=arg2;
				isSuccess=false;
				isUpload=true;
				if(listener!=null)
				{
					listener.pic_notifaction();
				}
				
			}
			
			@Override
			public void onFailure(String arg0, OSSException arg1) {
				isFails=true;
				if(listener!=null)
				{
					listener.pic_showMsg(arg0);
				}
			}
			
			@Override
			public void onSuccess(String arg0) {
				//上传完成 发送数据
				isFails=false;
				ali_content=arg0;
				isUpload=false;
				content=arg0;
				if(listener!=null)
				{
					listener.pic_notifaction();
				}
				uploadPic();//上传完成 通知服务器 并获取图片地址
			}
		});
	}

	public interface PicChatBean_Listener
	{
		void pic_notifaction();//通知刷新界面
		void pic_showMsg(String msg);//显示消息
	}
}
