package com.shenma.yueba.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CheckAppVersionbean implements Serializable{
    private String Version;
    private String Title;
    private String  Url;
    private String Details;

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        Url = url;
    }

    public String getVersion() {
        return Version;
    }
    public void setVersion(String version) {
        Version = version;
    }
    public String getDetails() {
        return Details;
    }
    public void setDetails(String details) {
        Details = details;
    }
}

