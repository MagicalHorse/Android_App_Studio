package im.form;

import android.content.Context;
import im.control.SocketManger;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-3 下午4:02:10  
 * 程序的简单说明  文本消息
 */

public class TextChatBean extends BaseChatBean{

	public TextChatBean(Context context) {
		super(ChatType.text_trype,RequestMessageBean.type_empty,context);
	}

	@Override
	public void sendData() {
		SocketManger.the().sendMsg(this);
	}

	/*****
	 * 接收到的消息 进行item的赋值
	 * ***/
	@Override
	public void setValue(Object obj) {
		
		super.setParentView(obj);
	}

}
