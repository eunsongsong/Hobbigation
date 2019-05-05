package com.example.hobbigation;

public class CategoryInfo {
    private String url;
    private String name;
    private boolean cheked = false;
    public void setChecked(boolean checked)
    {
        this.cheked = checked;
    }
    public boolean isSelected(){
        return cheked;
    }

    public CategoryInfo(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public String getName1() {
        return name;
    }

}