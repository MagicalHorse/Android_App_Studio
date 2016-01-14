package im.form;

import android.content.Context;
import im.control.SocketManger;

/**  
 * @author gyj  
 * @version 创建时间：2015-7-6 上午11:14:49  
 * 程序的简单说明  商品数据
 */

public class ProductChatBean extends BaseChatBean{
	/******
	 * @param product_id  int 商品的id
	 * ****/
	public ProductChatBean(Context context) {
		super(ChatType.link_type,RequestMessageBean.type_produtc_img,context);
	}

	@Override
	public void sendData() {
		SocketManger.the().sendMsg(this);
	}

	@Override
	public void setValue(Object obj) {
		super.setParentView(obj);
	}

}
