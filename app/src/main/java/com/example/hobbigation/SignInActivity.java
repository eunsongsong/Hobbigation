package com.example.hobbigation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity  {

    public static int TIME_OUT = 1001;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private String email = "";
    private String password = "";
    ProgressDialog dialog;
    private EditText email_login;
    private EditText pwd_login;

    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        email_login = (EditText) findViewById(R.id.sign_email);
        pwd_login = (EditText) findViewById(R.id.sign_pwd);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    @SuppressLint("HandlerLeak") Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == TIME_OUT)
            { // 타임아웃이 발생하면
                dialog.dismiss(); // ProgressDialog를 종료
            }
        }
    };
    public void sign_In(View v) {

        if(mFirebaseUser != null)
            FirebaseAuth.getInstance().signOut();

        email = email_login.getText().toString();
        password = pwd_login.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isValidEmail() && isValidPasswd()) {
         //   loginUser(email, password);
                // 로그인 성공
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this,"dddddd",Toast.LENGTH_LONG).show();
                                mFirebaseUser = firebaseAuth.getCurrentUser();
                                if (mFirebaseUser != null) {
                                    if (!(mFirebaseUser.isEmailVerified())) { //인증되면
                                        Toast.makeText(SignInActivity.this, "인증해주세요", Toast.LENGTH_LONG).show();
                                        return;
                                    } else { //인증안되면
                                        dialog = ProgressDialog.show(SignInActivity.this, "로그인중입니다."
                                                , "잠시만 기다려주세요");
                                        mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                }
                            } else {
                                // 로그인 실패
                                Toast.makeText(SignInActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else
        {
            Toast.makeText(SignInActivity.this,"로그인 실패입니다11123121.",Toast.LENGTH_LONG).show();
            return;
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }


}
