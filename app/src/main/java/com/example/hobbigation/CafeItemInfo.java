package com.example.hobbigation;

public class CafeItemInfo {
        private String title;
        private String desc;
        private String url;
        private String cafename;
        private String cafeurl;

    public CafeItemInfo(String title, String desc, String url, String cafename, String cafeurl) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.cafename = cafename;
        this.cafeurl = cafeurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCafename() {
        return cafename;
    }

    public void setCafename(String cafename) {
        this.cafename = cafename;
    }

    public String getCafeurl() {
        return cafeurl;
    }

    public void setCafeurl(String cafeurl) {
        this.cafeurl = cafeurl;
    }
}
