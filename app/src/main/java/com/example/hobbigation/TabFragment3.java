package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Tab3 - MyPage
 * 현재 회원정보, 푸시 알림 설정 스위치, 회원정보 수정 버튼이 보여진다.
 */
public class TabFragment3 extends Fragment {

    FirebaseAuth firebaseAuth;
    //회원 정보 나타냄
    TextView username;
    TextView userEmail;
    TextView usergender;
    TextView userage;

    Button modify;  //비밀번호 변경 버튼
    Switch pushsetsw;  //push 설정 스위치

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment3, container, false);
        final boolean sp_push_set=PreferenceUtil.getInstance(getContext()).getBooleanExtra("PushSetting");

        username = (TextView)rootview.findViewById(R.id.mypage_userId) ;
        userEmail = (TextView)rootview.findViewById(R.id.mypage_userEmail);
        usergender = (TextView)rootview.findViewById(R.id.mypage_usergender);
        userage = (TextView)rootview.findViewById(R.id.mypage_userage);
        modify = (Button)rootview.findViewById(R.id.modifyinfobtn);
        pushsetsw = (Switch)rootview.findViewById(R.id.pushswitch);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        pushsetsw.setChecked(sp_push_set);  //유저 설정대로 푸시 스위치 유지

        //Current 유저 찾아서 DB에 저장된 정보 화면에 띄우기
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            if (mFirebaseUser != null) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            if (ds.child("email").getValue().toString().equals(mFirebaseUser.getEmail())) {
                                String target = ds.child("username").getValue().toString();
                                username.setText(" 이름 : " + target);
                                target = ds.child("gender").getValue().toString();
                                usergender.setText(" 성별 : " + target);
                                target = ds.child("age").getValue().toString();
                                userage.setText(" 나이 : " + target);
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
        //비밀번호 수정은 ModifyInfo액티비티에서 진행
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ModifyInfoActivity.class));
            }
        });

        //푸시 알림 스위치에 따른 동작
       pushsetsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //유저의 푸시 설정(true)을 SharedPreferences에 저장
                    PreferenceUtil.getInstance(getContext()).putBooleanExtra("PushSetting",true);
                    //FCM 주제 구독
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                    //서비스 시작 (데모용 1분 푸시)
                    Intent intent = new Intent(getContext(), MyService.class);
                    getActivity().startService(intent);

                }
                else{
                    //유저의 푸시 설정(false)을 SharedPreferences에 저장
                    PreferenceUtil.getInstance(getContext()).putBooleanExtra("PushSetting",false);
                    //FCM 주제 구독 취소
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                    //서비스 중지 (데모용 1분 푸시)
                    Intent intent = new Intent(getContext(),MyService.class);
                    getActivity().stopService(intent);
                }
            }
        });

        return rootview;
    }
}