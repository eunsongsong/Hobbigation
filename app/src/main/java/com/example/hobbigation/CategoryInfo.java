package com.example.hobbigation;

public class CategoryInfo {
    private String url;
    private String url_two;
    private String name;
    private String name_two;
    private boolean cheked = false;
    public void setChecked(boolean checked)
    {
        this.cheked = checked;
    }
    public boolean isSelected(){
        return cheked;
    }

    public CategoryInfo(String url, String name, String url_two, String name_two) {
        this.url = url;
        this.url_two = url_two;
        this.name = name;
        this.name_two = name_two;
    }

    public String getUrl() {
        return url;
    }
    public String getUrl_two() { return url_two; }
    public String getName1() {
        return name;
    }
    public String getName_two() {
        return name_two;
    }
}