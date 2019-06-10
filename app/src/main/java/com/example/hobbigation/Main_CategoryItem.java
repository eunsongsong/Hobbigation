package com.example.hobbigation;

import android.graphics.drawable.Drawable;

/**
 * TabFragment1 에 보여지는 12개의 카테고리 이름과 이미지를 담는 클래스이다.
 */
public class Main_CategoryItem {
    private String name;
    private Drawable drawable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Main_CategoryItem(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }
}
