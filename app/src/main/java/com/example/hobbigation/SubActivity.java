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
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.StringTokenizer;

public class SubActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    String keyword = "";
    TextView cate_name_v;
    CheckBox like_c;
    String final_like = "";
    String[] volunteer;
    boolean is_volunteer = false;

    String like = "";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");
    DatabaseReference myRef2 = database.getReference("취미").child("이미지_태그");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        volunteer = new String[]{"보육원봉사활동","양로원봉사활동","유기동물봉사","재능기부활동"};
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        cate_name_v = (TextView) findViewById(R.id.search_category_name);
        like_c = (CheckBox) findViewById(R.id.like_check);
        //  like_c.setOnCheckedChangeListener(null);
        like = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("like");
        keyword = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("keyword");

        Log.d("찜하기",like);

        String tmp = like;
        //like 있을 때만
        if(!TextUtils.isEmpty(tmp)) {
            tmp = tmp.substring(0, like.length() - 1);
            String[] setlike = tmp.split("#");

            //찜 유지
            for (int i = 0; i < setlike.length; i++) {
                if (setlike[i].equals(keyword)) {
                    like_c.setChecked(true);
                }
            }
        }
        cate_name_v.setText(keyword);
        like_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                final boolean OnOff = like_c.isChecked();

                if(OnOff){
                    final_like = like+keyword+"#";
                    Log.d("Final_Like", final_like);
                    FCM();
                }
                else{
                    String del = keyword+"#";
                    final_like = like.replace(del, "");
                    Log.d("delete_Final_Like", final_like);
                }

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            String target = ds.child("email").getValue().toString();
                            if (mFirebaseUser != null)
                            {
                                if(target.equals(mFirebaseUser.getEmail())){
                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");

                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("like").setValue(final_like);
                                    PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("like", final_like);
                                    //찜을 눌렀을 때 취미의 count 올리기
                                    if(isChecked) {
                                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot de : dataSnapshot.getChildren()) {
                                                    String hobby = de.getKey();
                                                    if (hobby.equals(keyword)) {
                                                        String cntstr = de.child("count").getValue().toString();
                                                        int cnt = Integer.parseInt(cntstr);
                                                        myRef2.child(keyword).child("count").setValue(cnt + 1);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                    //찜 취소했을 때 취미의 count 내리기
                                    else {
                                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot de : dataSnapshot.getChildren()) {
                                                    String hobby = de.getKey().toString();
                                                    if (hobby.equals(keyword)) {
                                                        String cntstr = de.child("count").getValue().toString();
                                                        int cnt = Integer.parseInt(cntstr);
                                                        myRef2.child(keyword).child("count").setValue(cnt - 1);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.subpager);
        mTabLayout = (TabLayout) findViewById(R.id.subtab);


        mTabLayout.addTab(mTabLayout.newTab().setText("블로그"));
        mTabLayout.addTab(mTabLayout.newTab().setText("카페"));

        //지역 정보 보여주는 취미 구별
        for(int i=0; i<volunteer.length; i++) {
            if(volunteer[i].equals(keyword)) {
                is_volunteer = true;
                break;
            }
            else
                is_volunteer = false;
        }
        if(!is_volunteer)
            mTabLayout.addTab(mTabLayout.newTab().setText("쇼핑"));


        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final SubTabPagerAdapter adapter = new SubTabPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    public void FCM(){
        PreferenceUtil.getInstance(getApplicationContext()).putBooleanExtra("PushSetting",true);  //true 입력
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        //푸시 설정 되었다는 알림 보내기
        String channelId = "channel";
        String channelName = "Channel Name";

        NotificationManager notifManager = (NotificationManager) getSystemService  (Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notifManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);

        Intent notificationIntent = new Intent(getApplicationContext(), TabFragment3.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("찜완료") // required
                .setContentText("찜조아")  // required
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
}


