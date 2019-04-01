package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
