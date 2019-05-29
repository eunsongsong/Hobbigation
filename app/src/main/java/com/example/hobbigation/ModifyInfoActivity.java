package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class ModifyInfoActivity extends AppCompatActivity {

    private Button modipwbtn, saveChangebtn;
    private CheckBox male_check, female_check;
    private EditText modify_name, modify_age;

    //수정된 변수 (DB에 저장할 값)
    private String egender = "";
    private String ename =  "";
    private String eage =  "";

    FirebaseAuth firebaseAuth;
    FirebaseUser mFirebaseUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        modipwbtn = (Button)findViewById(R.id.modipwbtn);
        male_check = (CheckBox) findViewById(R.id.modiTomale);
        female_check = (CheckBox) findViewById(R.id.modiTofemale);
        modify_name = (EditText) findViewById(R.id.modiname);
        modify_age = (EditText)findViewById(R.id.modiage);
        saveChangebtn =(Button)findViewById(R.id.modi_confirm_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.useAppLanguage();

        male_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                egender = "남자";
                female_check.setChecked(false);
                male_check.setChecked(true);
            }
        });
        female_check.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                egender = "여자";
                male_check.setChecked(false);
                female_check.setChecked(true);
            }
        });

        //비밀번호 변경은 이메일을 전송받아서 진행
        modipwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPwActivity.class));
            }
        });

        //결과 저장 함수 실행, 정보수정화면은 null로
        saveChangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyUserInfo();

                modify_name.setText(null);
                modify_age.setText(null);
                male_check.setChecked(false);
                female_check.setChecked(false);
            }
        });
    }

    //수정된 사항 유저 DB에 저장
    public void ModifyUserInfo(){
        ename = modify_name.getText().toString();
        eage = modify_age.getText().toString();
        final boolean mcheck = male_check.isChecked();
        final boolean fcheck = female_check.isChecked();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();

        //변경사항이 없을 때 - 메세지만 띄우기
        if(!mcheck&&!fcheck&&(TextUtils.isEmpty(eage))&&(TextUtils.isEmpty(ename))) {
            Toast.makeText(this, "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        //변경사항이 있을 때 - DB 업데이트
        else {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String target = ds.child("email").getValue().toString();
                        if (mFirebaseUser != null) {
                            if (target.equals(mFirebaseUser.getEmail())) {

                                //변경사항이 있을 때 - Null 아닌 부분만 DB에 저장
                                if (!(TextUtils.isEmpty(ename))) {
                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");
                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("username").setValue(ename);
                                }
                                if (!(TextUtils.isEmpty(eage))) {
                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");
                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("age").setValue(eage);
                                }
                                if (mcheck||fcheck) {
                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");
                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("gender").setValue(egender);
                                }
                                //변경 사항이 저장되었음을 알림
                                Toast.makeText(getApplicationContext(), "변경사항이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
