package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.StoreItem;

import java.util.List;

/**
 * Created by a on 2015/11/25.
 */
public class SearchBuyerBean {


    private List<BuyerInfo> items;
    private String pageindex;
    private String pagesize;
    private String totalcount;
    private String totalpaged;
    private String ispaged;

    public List<BuyerInfo> getItems() {
        return items;
    }

    public void setItems(List<BuyerInfo> items) {
        this.items = items;
    }

    public String getPageindex() {
        return pageindex;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getTotalpaged() {
        return totalpaged;
    }

    public void setTotalpaged(String totalpaged) {
        this.totalpaged = totalpaged;
    }

    public String getIspaged() {
        return ispaged;
    }

    public void setIspaged(String ispaged) {
        this.ispaged = ispaged;
    }
}
