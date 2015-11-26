package com.shenma.yueba.baijia.modle.newmodel;

import java.util.List;

/**
 * Created by a on 2015/11/26.
 */
public class RecommondBuyerListBean {

    private String sourcetype;//0-用户关注的买手信息，1-保留,
    private List<BuyerInfo> buyers;
    private String pageindex;
    private String pagesize;
    private String totalcount;
    private String totalpaged;
    private String ispaged;


    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public void setBuyers(List<BuyerInfo> buyers) {
        this.buyers = buyers;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public void setTotalpaged(String totalpaged) {
        this.totalpaged = totalpaged;
    }

    public void setIspaged(String ispaged) {
        this.ispaged = ispaged;
    }
}
