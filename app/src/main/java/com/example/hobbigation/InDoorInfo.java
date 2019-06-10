package com.example.hobbigation;


/**
 *   카테고리 실내 필터 결과를 담는 item 클래스
 */
public class InDoorInfo {
    private String name = "";
    private String url = "";
    public String getUrl() {
        return url;
    }

    public InDoorInfo(String name, String url) {
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
