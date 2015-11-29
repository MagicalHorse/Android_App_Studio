package com.shenma.yueba.baijia.modle;

import com.shenma.yueba.baijia.modle.newmodel.StoreIndexItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/29.
 */
public class MoreBrandBackInfo {
    List<StoreIndexItem> items=new ArrayList<StoreIndexItem>();
    int pageindex;
    int pagesize;
    int totalcount;
    int totalpaged;

    public boolean ispaged() {
        return ispaged;
    }

    public void setIspaged(boolean ispaged) {
        this.ispaged = ispaged;
    }

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

    boolean ispaged=false;
}
