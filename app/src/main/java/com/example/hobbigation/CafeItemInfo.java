package com.example.hobbigation;

/**
 * 취미 카페 검색 결과를 담는 item 클래스
 */

public class CafeItemInfo {
        private String title;  //포스트 제목
        private String desc;  //포스트 내용
        private String url;  //포스트 url
        private String cafename;  //카페명
        private String cafeurl;  //카페url

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
