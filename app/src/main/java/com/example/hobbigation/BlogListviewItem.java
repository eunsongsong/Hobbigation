package com.example.hobbigation;

import android.graphics.drawable.Drawable;

public class BlogListviewItem {

    private Drawable iconDrawable;
    private String titlestr;
    private String descstr;
    private String blogerstr;
    private String datestr;

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

