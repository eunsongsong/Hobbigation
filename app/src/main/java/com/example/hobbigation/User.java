package com.example.hobbigation;

public class User {


    public String email = "";
    public String pwd  = "";
    public String username = "";
    public String gender = "";
    public String age = "";


    public User() {

    }


    public User(String email, String pwd, String username, String gender, String age) {
        this.email = email;
        this.pwd = pwd;
        this.username = username;
        this.gender = gender;
        this.age = age;

    }
}
