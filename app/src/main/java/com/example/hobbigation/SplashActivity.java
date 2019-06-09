package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 앱의 스플래시 화면을 보여준다.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 2초간 스플래쉬 화면을 보여주고 GuideActivity로 넘어간다.
         */
        try{
            Thread.sleep(2000);
            Intent mainIntent = new Intent(this, GuideActivity.class);
            startActivity(mainIntent);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
