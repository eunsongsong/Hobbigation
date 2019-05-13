package com.example.hobbigation;

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
