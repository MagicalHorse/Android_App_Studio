package com.shenma.yueba.baijia.modle.newmodel;

import java.util.List;

/**
 * Created by a on 2015/11/24.
 */
public class BinderData {


    private List<BannerBean> Banners;

    private List<SubjectrBean> Subjects;

    public List<BannerBean> getBanners() {
        return Banners;
    }

    public void setBanners(List<BannerBean> banners) {
        Banners = banners;
    }

    public List<SubjectrBean> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<SubjectrBean> subjects) {
        Subjects = subjects;
    }
}
