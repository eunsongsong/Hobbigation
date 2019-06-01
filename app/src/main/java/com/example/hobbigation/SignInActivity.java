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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SignInActivity extends AppCompatActivity  {

    public static int TIME_OUT = 1001;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private String email = "";
    private String password = "";
    ProgressDialog dialog;
    private EditText email_login;
    private EditText pwd_login;
    private Button forgot_pw_btn;  //비밀번호 변경 버튼
    private CheckBox remember;

    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        boolean autologin = PreferenceUtil.getInstance(this).getBooleanExtra("AutoLogin");  //출력

        forgot_pw_btn = (Button) findViewById(R.id.forgotPw_btn);
        remember =(CheckBox) findViewById(R.id.rememberlogin);
        email_login = (EditText) findViewById(R.id.sign_email);
        pwd_login = (EditText) findViewById(R.id.sign_pwd);

        firebaseAuth = FirebaseAuth.getInstance();

        remember.setChecked(autologin);

        //비밀번호 변경은 ForgotPw 액티비티로 이동해서 진행
        forgot_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPwActivity.class));
            }
        });

        if(autologin){
            load();
        }
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
                                mFirebaseUser = firebaseAuth.getCurrentUser();
                                if (mFirebaseUser != null) {
                                    if (!(mFirebaseUser.isEmailVerified())) { //인증안되면
                                        Toast.makeText(SignInActivity.this, "Email 인증을 해주세요", Toast.LENGTH_LONG).show();
                                        return;
                                    } else { //인증되면
                                        email_login.setText(null);
                                        pwd_login.setText(null);

                                        dialog = ProgressDialog.show(SignInActivity.this, "로그인중입니다."
                                                , "잠시만 기다려주세요");
                                        mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);

                                        if(remember.isChecked()) {
                                            PreferenceUtil.getInstance(getApplicationContext()).putBooleanExtra("AutoLogin", true);
                                            try {
                                                PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("LoginID", AESCipher.AES_Encode(email));
                                                PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("LoginPW", AESCipher.AES_Encode(password));
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            } catch (NoSuchAlgorithmException e) {
                                                e.printStackTrace();
                                            } catch (NoSuchPaddingException e) {
                                                e.printStackTrace();
                                            } catch (InvalidKeyException e) {
                                                e.printStackTrace();
                                            } catch (InvalidAlgorithmParameterException e) {
                                                e.printStackTrace();
                                            } catch (IllegalBlockSizeException e) {
                                                e.printStackTrace();
                                            } catch (BadPaddingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            PreferenceUtil.getInstance(getApplicationContext()).putBooleanExtra("AutoLogin", false);
                                            PreferenceUtil.getInstance(getApplicationContext()).removePreference("LoginID");
                                            PreferenceUtil.getInstance(getApplicationContext()).removePreference("LoginPW");
                                        }

                                        startActivity(new Intent(getApplicationContext(), TapViewActivity.class));
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
            Toast.makeText(SignInActivity.this,R.string.failed_login,Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void load() {
        try {
            email_login.setText(AESCipher.AES_Decode(PreferenceUtil.getInstance(this).getStringExtra("LoginID")));
            pwd_login.setText(AESCipher.AES_Decode(PreferenceUtil.getInstance(this).getStringExtra("LoginPW")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
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

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
