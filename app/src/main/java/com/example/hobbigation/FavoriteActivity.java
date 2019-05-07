package com.example.hobbigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class
FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //현재 객체에 TextView객체 생성
        TextView textview = new TextView(this);
        //출력할 문자 설정
        textview.setText("문제집 목록 화면");
        //현재 객체에 TextView 객체에서 설정한 문자 출력
        setContentView(textview);
    }
}
