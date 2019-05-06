package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class BeforeSignin extends AppCompatActivity {

    private Button sign_in_btn, sign_up_btn;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_signin);

        sign_in_btn = (Button) findViewById(R.id.b_signin);
        sign_up_btn = (Button) findViewById(R.id.b_signup);

        firebaseAuth = FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser() != null) {
                //이미 로그인 되었다면
                firebaseAuth.signOut();
            }


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignUpActivity.class));
            }
        });

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignInActivity.class));
            }
        });


    }
}
