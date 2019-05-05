package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPwActivity extends AppCompatActivity {

    EditText userEmail;
    Button userPass;
    TextView guidetxt;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.btnResetPw);
        guidetxt = findViewById(R.id.pwguidetext);

        firebaseAuth = firebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPwActivity.this,
                                    "이메일이 전송되었습니다.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ForgotPwActivity.this,
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
