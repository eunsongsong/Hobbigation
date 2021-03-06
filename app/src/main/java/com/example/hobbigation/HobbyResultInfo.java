package com.example.hobbigation;

/**
 * 취미 결과 정보를 담는 아이템 Information Class
 * 이름과 url로 구성됨
 */
public class HobbyResultInfo {
    private String hobby_name;
    private String hobby_url;

    public String getHobby_name() {
        return hobby_name;
    }

    public void setHobby_name(String hobby_name) {
        this.hobby_name = hobby_name;
    }

    public String getHobby_url() {
        return hobby_url;
    }

    public void setHobby_url(String hobby_url) {
        this.hobby_url = hobby_url;
    }

    public HobbyResultInfo(String hobby_name, String hobby_url) {
        this.hobby_name = hobby_name;
        this.hobby_url = hobby_url;
    }
}
