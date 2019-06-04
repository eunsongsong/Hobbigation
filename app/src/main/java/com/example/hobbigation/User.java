package com.example.hobbigation;

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
