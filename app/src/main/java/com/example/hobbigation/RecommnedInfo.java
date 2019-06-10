package com.example.hobbigation;

/**
 * 이미지를 왼쪽 오른쪽이 한 아이템이다.
 * 각각 태그가 들어 있다.
 */
public class RecommnedInfo {
    private String url;
    private String url_two;
    private String tag;
    private String tag_two;
    private String hobby_name;
    private String hobby_name_two;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrl_two(String url_two) {
        this.url_two = url_two;
    }

    public String getHobby_name() {
        return hobby_name;
    }

    public void setHobby_name(String hobby_name) {
        this.hobby_name = hobby_name;
    }

    public String getHobby_name_two() {
        return hobby_name_two;
    }

    public void setHobby_name_two(String hobby_name_two) {
        this.hobby_name_two = hobby_name_two;
    }

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

    public RecommnedInfo(String url, String url_two, String tag, String tag_two, String hobby_name, String hobby_name_two) {
        this.url = url;
        this.url_two = url_two;
        this.tag = tag;
        this.tag_two = tag_two;
        this.hobby_name = hobby_name;
        this.hobby_name_two = hobby_name_two;
    }

    public String getUrl_two() {
        return url_two;
    }

}
