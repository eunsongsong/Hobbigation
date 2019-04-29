package com.example.hobbigation;

public class User {


    public String email = "";
    public String pwd  = "";
    public String username = "";
    public String gender = "";
    public String age = "";
    public String tag = "";

    public User() {

    }


    public User(String email, String pwd, String username, String gender, String age, String tag) {
        this.email = email;
        this.pwd = pwd;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.tag = tag;
    }
}
