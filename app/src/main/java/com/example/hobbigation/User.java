package com.example.hobbigation;

/**
 * 사용자의 정보를 담는 클래스
 * 이메일, 이름, 성별, 나이, 태그, 찜
 */
public class User {

    public String email = "";
    public String username = "";
    public String gender = "";
    public String age = "";
    public String tag = "";
    public String like = "";

    public User() {

    }


    public User(String email, String username, String gender, String age, String tag, String like) {
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.tag = tag;
        this.like = like;
    }
}
