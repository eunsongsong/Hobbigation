package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * 로그인과 회원가입을 선택 할 수 있는 화면
 */
public class BeforeSignin extends AppCompatActivity {

    private Button sign_in_btn, sign_up_btn;
    BackPressCloseHandler backPressCloseHandler;

    //Firebase database Reference를 가져온다
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database3 = FirebaseDatabase.getInstance();
    DatabaseReference myRef3 = database3.getReference("취미");

    String total= "";
    String names = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_signin);

        backPressCloseHandler = new BackPressCloseHandler(this);
        sign_in_btn = (Button) findViewById(R.id.b_signin);
        sign_up_btn = (Button) findViewById(R.id.b_signup);

        //제목줄 객체 얻어오기
        ActionBar actionBar = getSupportActionBar();
        //액션바에 뒤로가기 버튼 나타내기
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        //FriebaseAush 인스턴스를 가져와 항상 로그아웃 시킨다.
            if (firebaseAuth.getCurrentUser() != null) {
                //이미 로그인 되었다면
                firebaseAuth.signOut();
            }

          //현재까지 Firebase database에 있는 취미의 갯수를 SharedPreference를 저장한다.
         myRef3.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 PreferenceUtil.getInstance(getApplicationContext()).putIntExtra("hobby_num",(int)dataSnapshot.getChildrenCount());
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

         //각 취미가 가지고 있는 count를 오름차순으로 정렬하여 뒤에서부터 count가
        //가장 높은 취미 이미지와 url을 SharedPreference에 저장한다.
        myRef3.child("이미지_태그").orderByChild("count").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //10개의 이름과 url을 #을 붙여서 연결하여 저장
                    if ( ds.getKey().equals("count")) {
                        total +=  dataSnapshot.child("url_태그").child("0").child("url").getValue().toString() + "#";
                        names += dataSnapshot.getKey() + "#";
                    }
                }
                PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("total",total);
                PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("names",names);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //회원가입 화면으로 넘어가는 버튼 리스너
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignUpActivity.class));
            }
        });

        //로그인 화면으로 넘어가는 버튼 리스너
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignInActivity.class));
            }
        });


    }

    //액션바의 뒤로가기 버튼 2번 터치시 종료
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}
