package com.shenma.yueba.baijia.modle;

/**
 * Created by Administrator on 2015/12/29.
 * 门店退货信息
 */
public class RequestStoreSalesReturnBean extends BaseRequest{
    StoreSalesReturnInfo data;
    public StoreSalesReturnInfo getData() {
        return data;
    }

    public void setData(StoreSalesReturnInfo data) {
        this.data = data;
    }
}
