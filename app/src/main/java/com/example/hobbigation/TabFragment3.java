package com.example.hobbigation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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

import java.util.StringTokenizer;

/* Tap3 - MyPage (현재 회원정보, 수정 가능 버튼) */
public class TabFragment3 extends Fragment {

    FirebaseAuth firebaseAuth;
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
        final boolean sp_push_set=PreferenceUtil.getInstance(getContext()).getBooleanExtra("PushSetting");  //출력

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
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                        if(ds.child("email").getValue().toString().equals(mFirebaseUser.getEmail()))
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
        //회원정보 수정은 ModifyInfo액티비티에서 진행
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ModifyInfoActivity.class));
            }
        });

        pushsetsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PreferenceUtil.getInstance(getContext()).putBooleanExtra("PushSetting",true);  //true 입력
                    FirebaseMessaging.getInstance().subscribeToTopic("news");

                    //푸시 설정 되었다는 알림 보내기
                    String channelId = "channel";
                    String channelName = "Channel Name";

                    NotificationManager notifManager = (NotificationManager) getActivity().getSystemService  (Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                        notifManager.createNotificationChannel(mChannel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), channelId);

                    Intent notificationIntent = new Intent(getContext(), BeforeSignin.class);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    int requestID = (int) System.currentTimeMillis();

                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                            requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentTitle("푸시 알림 설정 완료") // required
                            .setContentText("이제 푸시 알림을 받으실 수 있습니다!")  // required
                            .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                            .setSound(RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                            .setBadgeIconType(R.mipmap.logo2)
                            .setContentIntent(pendingIntent);

                    notifManager.notify(0, builder.build());
                    //startActivity(new Intent(getContext(), TabFragment3.class));
                }
                else{
                    PreferenceUtil.getInstance(getContext()).putBooleanExtra("PushSetting",false);  //false 입력
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                }
            }
        });

        return rootview;
    }
}