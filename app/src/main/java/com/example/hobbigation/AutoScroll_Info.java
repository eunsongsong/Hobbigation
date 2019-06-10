package com.example.hobbigation;

/**
 * TabFrament1에서 보여지는 Top10 인기 취미 이름과 url 정보를 담는 클래스
 */
public class AutoScroll_Info {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AutoScroll_Info(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
