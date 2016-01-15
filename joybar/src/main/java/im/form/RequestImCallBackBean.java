package im.form;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/15.
 * 发送消息 后 返回数据对象
 */
public class RequestImCallBackBean implements Serializable {
    RequestMessageBean data = new RequestMessageBean();
    String message;//
    String action;//sendMessage",
    String type;//success",
    int errcode;//406
    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public RequestMessageBean getData() {
        return data;
    }

    public void setData(RequestMessageBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
