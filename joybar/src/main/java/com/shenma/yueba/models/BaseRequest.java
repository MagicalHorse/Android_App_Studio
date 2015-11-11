package com.shenma.yueba.models;

import java.io.Serializable;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-11 下午6:11:16  
 * 程序的简单说明   本类定义 数据接收基础类
 */

public class BaseRequest implements Serializable {
	boolean isSuccessful=false;// true/false,
    int statusCode=0;
    String message="";
	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
