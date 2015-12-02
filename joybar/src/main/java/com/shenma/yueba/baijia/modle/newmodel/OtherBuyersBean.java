package com.shenma.yueba.baijia.modle.newmodel;

import java.util.List;

/**
 * Created by a on 2015/11/26.
 */
public class OtherBuyersBean {

    private String sourcetype;//0-用户关注的买手信息，1-保留,
    private List<BuyerInfo> Buyers;
    private String pageindex;
    private String pagesize;
    private String totalcount;
    private String totalpaged;
    private String ispaged;

    public String getSourcetype() {
        return sourcetype;
    }


    public String getPageindex() {
        return pageindex;
    }

    public String getPagesize() {
        return pagesize;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public String getTotalpaged() {
        return totalpaged;
    }

    public String getIspaged() {
        return ispaged;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public List<BuyerInfo> getBuyers() {
        return Buyers;
    }

    public void setBuyers(List<BuyerInfo> buyers) {
        Buyers = buyers;
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
