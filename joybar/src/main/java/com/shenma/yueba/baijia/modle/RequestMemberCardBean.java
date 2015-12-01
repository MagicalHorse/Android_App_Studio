package com.shenma.yueba.baijia.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 * 会员卡 列表 数据
 */
public class RequestMemberCardBean extends BaseRequest {
    List<MemberCardBean> data=new ArrayList<MemberCardBean>();
    public List<MemberCardBean> getData() {
        return data;
    }

    public void setData(List<MemberCardBean> data) {
        this.data = data;
    }
}
