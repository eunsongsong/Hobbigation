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

public class BeforeSignin extends AppCompatActivity {

    private Button sign_in_btn, sign_up_btn;
    BackPressCloseHandler backPressCloseHandler;

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

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        firebaseAuth = FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser() != null) {
                //이미 로그인 되었다면
                firebaseAuth.signOut();
            }
         myRef3.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 PreferenceUtil.getInstance(getApplicationContext()).putIntExtra("hobby_num",(int)dataSnapshot.getChildrenCount());
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        myRef3.child("이미지_태그").orderByChild("count").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if ( ds.getKey().equals("count")) {
                        Log.d("dcc", ds.getValue().toString());
                        Log.d("ddd",dataSnapshot.getKey());
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

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignUpActivity.class));
            }
        });

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BeforeSignin.this, SignInActivity.class));
            }
        });


    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
