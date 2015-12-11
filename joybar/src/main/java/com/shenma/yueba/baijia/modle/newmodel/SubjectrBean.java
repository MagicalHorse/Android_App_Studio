package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by a on 2015/11/24.
 */
public class SubjectrBean {
    String Name;//             主题名称
    String Pic ;//            主题logo
    String Link ;//          Url地址

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }
}
