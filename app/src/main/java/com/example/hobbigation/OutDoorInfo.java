package com.example.hobbigation;

/**
 *   카테고리 야외 필터 결과를 담는 item 클래스
 */
public class OutDoorInfo {
    private String name = "";
    private String url = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OutDoorInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
