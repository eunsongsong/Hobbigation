package com.example.hobbigation;

public class RecommnedInfo {
    private String url;
    private String url_two;
    private boolean cheked = false;
    public void setChecked(boolean checked)
    {
        this.cheked = checked;
    }
    public boolean isSelected(){
        return cheked;
    }


    public String getUrl() {
        return url;
    }
    public RecommnedInfo(String url, String url_two) {
        this.url = url;
        this.url_two = url_two;
    }

    public String getUrl_two() {
        return url_two;
    }

}
