package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private Button recommend_btn;

    private TextView show_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recommend_btn = (Button) findViewById(R.id.recommend);
        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecommendationActivity.class));
            }
        });
        show_status = (TextView) findViewById(R.id.status1);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();


        if (mFirebaseUser != null) {
            show_status.setText(mFirebaseUser.getEmail()+"반갑습니다");
        }

    }
}
