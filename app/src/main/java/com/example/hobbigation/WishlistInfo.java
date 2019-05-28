package com.example.hobbigation;

public class WishlistInfo {

    private String name = "";
    private String url = "";
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
