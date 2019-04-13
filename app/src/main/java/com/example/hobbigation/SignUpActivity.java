package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
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

public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private EditText email_join;
    private EditText pwd_join;
    private EditText check_pwd;
    private TextView check_show;
    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;
    public String email = "";
    public String password = "";

    public String e_pwd = "";
    public String ename =  "";
    public String egender = "";
    public String eage = "";

    public int count = 0;
    private EditText e_name;
    private EditText e_age;
    private EditText e_gender;


    // Write a message to the database
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_join = (EditText) findViewById(R.id.emailInput);
        pwd_join =  (EditText)findViewById(R.id.passwordInput);
        check_pwd =  (EditText)findViewById(R.id.passwordCheck);
        check_show = (TextView) findViewById(R.id.checkText);


        e_name = (EditText) findViewById(R.id.name1);
        e_age = (EditText)findViewById(R.id.age1);
        e_gender = (EditText)findViewById(R.id.gender1);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.useAppLanguage();                //해당기기의 언어 설정

        check_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String comp1 = pwd_join.getText().toString();
                String comp2 = check_pwd.getText().toString();

                if(comp1.equals(comp2))
                {
                    check_show.setText("비밀번호가 일치합니다");

                }
                else
                {
                    check_show.setText("비밀번호가 일치하지않습니다");
                }
            }
        });

    }

    public void signUp(View view) {
        email = email_join.getText().toString();
        password = pwd_join.getText().toString();

       final String e_email = email_join.getText().toString();

        Toast.makeText(SignUpActivity.this,e_email,Toast.LENGTH_LONG).show();
        e_pwd = pwd_join.getText().toString();
        ename =  e_name.getText().toString();
       egender = e_gender.getText().toString();
         eage = e_age.getText().toString();


        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
            User user = new User(e_email,e_pwd,ename,egender,eage);
            myRef.child("user"+count).setValue(user);
        }

    }
    private void writeNewUser(String e_email, String e_pwd, String ename, String egender, String eage) {
        User user = new User(e_email, e_pwd, ename,egender,eage);
        myRef.child(e_email).setValue(user);
    }


    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else return PASSWORD_PATTERN.matcher(password).matches();
    }
    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            mFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                //해당 이메일에 확인메일을 보냄
                                        Toast.makeText(SignUpActivity.this, "Verification email sent to " + mFirebaseUser.getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                    } else {                                             //메일 보내기 실패
                                        Toast.makeText(SignUpActivity.this,
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}