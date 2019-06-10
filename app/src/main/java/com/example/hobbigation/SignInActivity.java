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

/**
 * 로그인 액티비티
 */
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
        actionBar.setDisplayHomeAsUpEnabled(true);  //액션바에 뒤로가기 버튼 나타내기

        //자동로그인 체크여부를 체크
        boolean autologin = PreferenceUtil.getInstance(this).getBooleanExtra("AutoLogin");

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

    /**
     * sign in 버튼 클릭시 실행
     */
    public void sign_In(View v) {

        //로그인 되어있는 경우 로그아웃
        if(mFirebaseUser != null)
            FirebaseAuth.getInstance().signOut();

        email = email_login.getText().toString();
        password = pwd_login.getText().toString();

        //이메일 입력 칸이 빈칸인 경우 알림
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //비밀번호 입력 칸이 빈칸인 경우 알림
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //로그인 성공 여부를 firebase를 통해서 확인
        //이메일 인증을 한경우에만 로그인
        if(isValidEmail() && isValidPasswd()) {

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

                                        //다이얼로그 로딩화면
                                        dialog = ProgressDialog.show(SignInActivity.this, "로그인중입니다."
                                                , "잠시만 기다려주세요");
                                        mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);

                                        //자동로그인 체크 되어있는 경우 AESCipher을 이용하여 로그인시 이메일과 비밀번호를 암호화하여 SharedPreference에 저장
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
                                        //자동 로그인 취소 된 경우 이메일과 비밀번호를 SharedPreference에서 삭제 시킴
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
        //자동로그인 체크된 상태로 이 액티비티로 오면 SharedPreference에 저장된 이메일과 비밀번호를 복호한다.
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
