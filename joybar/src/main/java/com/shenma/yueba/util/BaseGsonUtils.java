package com.shenma.yueba.util;

import com.google.gson.Gson;

/**  
 * @author gyj  
 * @version 创建时间：2015-5-10 下午4:29:03  
 * 程序的简单说明:本类定义 Gson  用于 json转对象  对象转json的泛型类  
 * @see #getJsonToObject(Class, String)
 * @see #getObjectToJson(Object)
 */

public class BaseGsonUtils {

	/*****
	 * json字符串 转 对象
	 * 这是一个泛型方法 需要传递一个 对象的类最终返回这个 类的对象
	 * @param <T>
	 * @param classzz Class<T>  转换的类
	 * @param json String  json类型的字符串
	 * @return T 
	 * ****/
	public static <T> T getJsonToObject(Class<T> classzz, String json) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, classzz);
		} catch (Exception e) {
			return null;
		}

	}
	
	/******
	 * 对象转json
	 * @param obj Object  对象
	 * @return String 
	 * ****/
	public static String getObjectToJson(Object obj)
	{
		try
		{
		Gson gson=new Gson();
		return gson.toJson(obj);
		}catch(Exception e)
		{
			return null;
		}
	}
}
