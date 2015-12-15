package com.shenma.yueba.baijia.modle.newmodel;

/**
 * Created by a on 2015/11/24.
 */
public class SubjectrBean {
    String Name;//             主题名称
    String Link ;//          Url地址
    String Logo ;//            主题logo
    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

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

}
