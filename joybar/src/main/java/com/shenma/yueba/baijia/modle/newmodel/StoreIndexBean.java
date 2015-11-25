package com.shenma.yueba.baijia.modle.newmodel;

import java.util.List;

/**
 * Created by a on 2015/11/25.
 */
public class StoreIndexBean {

    private List<StoreIndexItem> items;
    private int pageindex;
    private int pagesize;
    private int totalcount;
    private int totalpaged;


    public List<StoreIndexItem> getItems() {
        return items;
    }

    public void setItems(List<StoreIndexItem> items) {
        this.items = items;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getTotalpaged() {
        return totalpaged;
    }

    public void setTotalpaged(int totalpaged) {
        this.totalpaged = totalpaged;
    }
}
