package com.example.hobbigation;

/**
 * 연관 취미 이름과 url을 담는 클래스
 */
public class RelatedlistInfo {
    private String name = "";
    private String url = "";
    public String getUrl() {
        return url;
    }

    public RelatedlistInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }
    void setName(String name) {
        this.name = name;
    }
}
