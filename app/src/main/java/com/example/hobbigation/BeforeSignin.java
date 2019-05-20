package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class BeforeSignin extends AppCompatActivity {

    private Button sign_in_btn, sign_up_btn;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database3 = FirebaseDatabase.getInstance();
    DatabaseReference myRef3 = database3.getReference("취미");
    String total= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_signin);

        sign_in_btn = (Button) findViewById(R.id.b_signin);
        sign_up_btn = (Button) findViewById(R.id.b_signup);

        firebaseAuth = FirebaseAuth.getInstance();

            if (firebaseAuth.getCurrentUser() != null) {
                //이미 로그인 되었다면
                firebaseAuth.signOut();
            }
        myRef3.child("이미지_태그").orderByChild("count").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //  ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
                //  Log.d("오더바이차트 테스트 ", dataSnapshot.child("이미지_태그").getValue().toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //   Log.d("오더바이차트 테스트 " + i, ds.getKey());
                    if ( ds.getKey().equals("count")) {
                        Log.d("dcc", ds.getValue().toString());
                        Log.d("ddd",dataSnapshot.getKey());
                        total +=  dataSnapshot.child("url_태그").child("0").child("url").getValue().toString() + "#";
                        //   Log.d("urls",urls[count]);
                    }
                    //  Log.d("오더바이차트 테스트 " + i, dataSnapshot.child("이미지_태그").getValue().toString());
                }
                PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("total",total);




                //    Log.d("url입니다",dataSnapshot.child("이미지_태그").child("url_태그").child("0").child("url").getValue().toString());
                //  data.add(dataSnapshot.child("url_태그").child("0").child("url").getValue().toString());
                //      Log.d("오더바이차트 테스트 ", dataSnapshot.child("이미지_태그").getKey());

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
}
