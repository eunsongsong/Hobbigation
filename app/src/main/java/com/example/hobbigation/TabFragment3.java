package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.StringTokenizer;


public class TabFragment3 extends Fragment {

    FirebaseAuth firebaseAuth;
    TextView username;
    TextView userEmail;
    TextView usergender;
    TextView userage;
    String userid = "";
    Button modify;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment3, container, false);

        username = (TextView)rootview.findViewById(R.id.mypage_userId) ;
        userEmail = (TextView)rootview.findViewById(R.id.mypage_userEmail);
        usergender = (TextView)rootview.findViewById(R.id.mypage_usergender);
        userage = (TextView)rootview.findViewById(R.id.mypage_userage);
        modify = (Button)rootview.findViewById(R.id.modifyinfobtn);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(),"@");
        userid = st.nextToken();

        if (FirebaseInstanceId.getInstance().getToken() != null) {
            if (mFirebaseUser != null) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            StringTokenizer st = new StringTokenizer(ds.getKey(),":");
                            String key = "";
                            key = st.nextToken();
                        key = st.nextToken();
                        if(userid.equals(key))
                        {
                            String target = ds.child("username").getValue().toString();
                            username.setText(" 이름 : "+target);
                            target = ds.child("gender").getValue().toString();
                            usergender.setText(" 성별 : "+target);
                            target = ds.child("age").getValue().toString();
                            userage.setText(" 나이 : "+target);
                        }
                    }
                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                userEmail.setText(" 이메일 : "+mFirebaseUser.getEmail());
            }

        }

        return rootview;
    }
}