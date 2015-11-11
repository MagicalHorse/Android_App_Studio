package com.shenma.yueba.interfaces;

import com.shenma.yueba.models.BaseRequest;

/****
 * 定义Http接口回调类 用于接收返回的 成功与失败
 ***/
public interface HttpCallBackInterface<T extends BaseRequest> {
    /**
     * 返回成功
     * @param obj Object
     * @return void
     */
    void http_Success(T obj);

    /**
     * 返回失败
     *
     * @param error int
     * @param msg   String
     * @return void
     */
    void http_Fails(int error, String msg);
}
