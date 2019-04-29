package com.example.hobbigation;

public class RecommnedInfo {
    private String url;
    private String url_two;
    private String tag;
    private String tag_two;


    private boolean cheked = false;
    private boolean cheked_two = false;

    public String getTag_two() {
        return tag_two;
    }

    public void setTag_two(String tag_two) {
        this.tag_two = tag_two;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public void setCheked_two(boolean cheked_two) { this.cheked_two = cheked_two; }
    public void setChecked(boolean checked)
    {
        this.cheked = checked;
    }
    public boolean isSelected(){
        return cheked;
    }
    public boolean isSelected_two() { return cheked_two; }


    public String getUrl() {
        return url;
    }

    public RecommnedInfo(String url, String url_two, String tag, String tag_two) {
        this.url = url;
        this.url_two = url_two;
        this.tag = tag;
        this.tag_two = tag_two;
    }

    public String getUrl_two() {
        return url_two;
    }

}
