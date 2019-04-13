package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private String email = "";
    private String password = "";
    private TextView text;
    private Button to_sign_up_btn;
    private EditText email_login;
    private EditText pwd_login;
    private Button sign_in_btn;
    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text = (TextView) findViewById(R.id.textView);
        sign_in_btn = (Button) findViewById(R.id.loginButton);
        email_login = (EditText) findViewById(R.id.sign_email);
        pwd_login = (EditText) findViewById(R.id.sign_pwd);

        firebaseAuth = FirebaseAuth.getInstance();

        to_sign_up_btn  = (Button) findViewById(R.id.move_sign_up);
        to_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sign = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent_sign);
            }
        });

    }

    public void signIn(View view) {

        if(mFirebaseUser != null){
            sign_in_btn.setText("Sign In");
            sign_Out();
        }
        else{
            email = email_login.getText().toString();
            password = pwd_login.getText().toString();

            if(isValidEmail() && isValidPasswd() ) {
                    loginUser(email, password);
            }
            else
            {
                Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_LONG).show();
            }
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

    public void sign_Out(){
        FirebaseAuth.getInstance().signOut();
        mFirebaseUser = firebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            Toast.makeText(LoginActivity.this,"로그아웃 실패",Toast.LENGTH_LONG).show();
            }
         else{
            text.setText("로그아웃상태");
        }
    }
    // 로그인
    private void loginUser(String email, final String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();

                            if(!(mFirebaseUser.isEmailVerified())){
                                Toast.makeText(LoginActivity.this,"인증해주세요",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                sign_in_btn.setText("Sign Out");
                              //  myRef.child(mFirebaseUser.getEmail()).child("유효여부").setValue("1");
                                text.setText(mFirebaseUser.getEmail() + "님 환영합니다");
                                pwd_login.setText(null);
                                email_login.setText(null);
                            }

                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
