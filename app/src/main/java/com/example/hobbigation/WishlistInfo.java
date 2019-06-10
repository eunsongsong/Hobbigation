package com.example.hobbigation;

/**
 * 유저의 찜 목록를 담는 item 클래스
 */
public class WishlistInfo {

    private String name = "";  //취미 이름
    private String url = "";  //취미 이미지
    public String getUrl() {
        return url;
    }

    public WishlistInfo(String name, String url) {
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
