package com.example.hobbigation;

import android.graphics.drawable.Drawable;

/**
 * 취미 블로그 검색 결과를 담는 item 클래스
 */
public class BlogListviewItem {

    private Drawable iconDrawable;  //블로그 아이콘
    private String titlestr;  //포스트 제목
    private String descstr;  //포스트 내용
    private String blogerstr;  //블로거명
    private String datestr;  //포스트 작성 날짜

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setTitle(String title) {
        titlestr = title;
    }

    public void setDesc(String desc) {
        descstr = desc;
    }

    public void setBloger(String bloger) {
        blogerstr = bloger;
    }

    public void setDate(String date) {
        datestr = date;
    }


    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getTitle() {
        return this.titlestr;
    }

    public String getDesc() {
        return this.descstr;
    }

    public String getBloger() {
        return this.blogerstr;
    }

    public String getDate() {
        return this.datestr;
    }
}

