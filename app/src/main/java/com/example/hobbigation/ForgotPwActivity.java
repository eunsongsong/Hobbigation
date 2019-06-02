package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/* 비밀번호 변경 Activity - firebase의 sendPasswordResetEmail 사용,
*  이메일을 전송받아서 링크를 누르면 비밀번호 재설정 창이 뜨고 새 비밀번호를 입력하면 변경 가능 */
public class ForgotPwActivity extends AppCompatActivity {

    EditText userEmail; //유저가 자신의 이메일 입력
    Button userPass;  //이메일을 전송하는 버튼
    TextView guidetxt;  //이메일이 전송된다는 설명

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.btnResetPw);
        guidetxt = findViewById(R.id.pwguidetext);

        firebaseAuth = firebaseAuth.getInstance();

        //유저가 PW 변경 이메일 전송 버튼 클릭 -> PW 재설정 이메일 전송
        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailcheck = userEmail.getText().toString();
                //이메일 입력을 하지 않으면 이메일 입력 요구
                if (TextUtils.isEmpty(emailcheck)) {
                    emailtoastMsg();
                }
                //이메일 입력칸이 Null이 아니면 이메일 전송 실행
                else {
                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPwActivity.this,
                                                "이메일이 전송되었습니다.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ForgotPwActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    //이메일 입력 요구 토스트 메세지
    public void emailtoastMsg(){
        Toast.makeText(this, "Email을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
