package com.example.hobbigation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.mindrot.jbcrypt.BCrypt;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 회원가입 화면
 */
public class SignUpActivity extends AppCompatActivity {

    public static int TIME_OUT = 1001;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    ProgressDialog dialog;

    private EditText email_join, pwd_join, check_pwd;
    private EditText e_name, e_age;

    private TextView check_show;
    private Button sign_btn;

    private CheckBox male_check;
    private CheckBox female_check;

    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;

    private String email = "";
    private String password = "";
    private String ename = "";
    private String egender = "";
    private String eage = "";
    long count = 0;


    // Write a message to the database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        email_join = (EditText) findViewById(R.id.emailInput);
        pwd_join = (EditText) findViewById(R.id.passwordInput);
        check_pwd = (EditText) findViewById(R.id.passwordCheck);
        e_name = (EditText) findViewById(R.id.name1);
        e_age = (EditText) findViewById(R.id.age1);

        check_show = (TextView) findViewById(R.id.checkText);

        sign_btn = (Button) findViewById(R.id.sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.useAppLanguage();
        male_check = (CheckBox) findViewById(R.id.male);
        female_check = (CheckBox) findViewById(R.id.female);


        //남성 여부 체크
        male_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                egender = "남자";
                female_check.setChecked(false);
                male_check.setChecked(true);
            }
        });

        //여성 여부 체크
        female_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                egender = "여자";
                male_check.setChecked(false);
                female_check.setChecked(true);
            }
        });

        //비밀번호 일치 여부 확인
        //일치하면 회원가입 버튼 활성화
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

                if (comp1.equals(comp2)) {
                    check_show.setText("비밀번호가 일치합니다");
                    sign_btn.setEnabled(true);
                } else {
                    check_show.setText("비밀번호가 일치하지않습니다");
                }
            }
        });

        //회원가입 하면 유저를 등록
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

                email_join.setText(null);
                pwd_join.setText(null);
                check_pwd.setText(null);
                e_name.setText(null);
                e_age.setText(null);
                male_check.setChecked(false);
                female_check.setChecked(false);

            }
        });
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == TIME_OUT) { // 타임아웃이 발생하면
                dialog.dismiss(); // ProgressDialog를 종료
            }
        }
    };
    // 이메일 유효성 검사
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
    //firebase에 이메일 인증 방식 사용
    //등록된 이메일이 아니면 firebase authentication에 등록되고 인증 메일 발송
    private void createUser(final String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            mFirebaseUser = firebaseAuth.getCurrentUser();
                            if (mFirebaseUser != null)
                                mFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this,
                                                    "Verification email sent to " + mFirebaseUser.getEmail(),
                                                    Toast.LENGTH_SHORT).show();
                                            dialog = ProgressDialog.show(SignUpActivity.this, "회원가입이 완료되었습니다!"
                                                    , mFirebaseUser.getEmail() + "으로 인증메일이 전송되었습니다.", true);
                                            mHandler.sendEmptyMessageDelayed(TIME_OUT, 3000);

                                            //디비에 유저 등록
                                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        count++;
                                                    }
                                                    //유저정보를 디비에 넣는다.
                                                    User user = new User(email, ename, egender, eage, "empty", "");

                                                    StringTokenizer st = new StringTokenizer(email, "@");
                                                    if (count >= 9) {
                                                        myRef.child("user0" + (count + 1) + ":" + st.nextToken()).setValue(user);
                                                    } else
                                                        myRef.child("user00" + (count + 1) + ":" + st.nextToken()).setValue(user);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            //Default로 푸시알림 설정

                                            FirebaseMessaging.getInstance().subscribeToTopic("news");
                                            startActivity(new Intent(SignUpActivity.this, BeforeSignin.class));
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

    //회원 등록 함수
    public void registerUser(){
        email = email_join.getText().toString();
        password = pwd_join.getText().toString();
        ename =  e_name.getText().toString();
        eage = e_age.getText().toString();

        //이메일 입력 칸이 빈칸인 경우 알림
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //비밀번호 입력 칸이 빈칸인 경우 알림
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //유저 등록 함수 실행
        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
            }

    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}