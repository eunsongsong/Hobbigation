package com.example.hobbigation;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TabFragment1에서 RecyclerView 구성된 카테고리 아이템을
 * 원모양으로 만들어 주는 클래스
 */
public class Main_Category_Decoration extends RecyclerView.ItemDecoration{
    private final int divWidth;

    public Main_Category_Decoration(int divWidth) {
        this.divWidth = divWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = divWidth;
        outRect.left = divWidth;
    }

}
