package com.shenma.yueba.baijia.modle;

import com.shenma.yueba.baijia.modle.newmodel.IndexItems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class IndexItemData implements Serializable{
    List<IndexItems> items=new ArrayList<IndexItems>();
    int pageindex;
    int pagesize;
    int totalcount;
    int totalpaged;
    boolean ispaged=false;
    public boolean ispaged() {
        return ispaged;
    }

    public void setIspaged(boolean ispaged) {
        this.ispaged = ispaged;
    }

    public List<IndexItems> getItems() {
        if(items!=null)
        {
            for(int i=0;i<items.size();i++)
            {
                items.get(i).startTime();
            }
        }
        return items;
    }

    public void setItems(List<IndexItems> items) {
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
