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

/**
 *  취미 정보 제공 화면
 */
public class SubActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    String keyword = "";
    TextView cate_name_v;
    CheckBox like_c;  //찜하기를 위한 하트 모양 체크박스
    String final_like = "";
    String[] volunteer;  //봉사활동 카테고리의 취미들
    boolean is_volunteer = false;  //취미가 봉사활동 카테고리에 속하는지 체크

    String like = "";  //유저의 찜 목록
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");
    DatabaseReference myRef2 = database.getReference("취미").child("이미지_태그");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //제목줄 객체 얻어오기
        ActionBar actionBar = getSupportActionBar();
        //액션바에 뒤로가기 버튼 나타내기
        actionBar.setDisplayHomeAsUpEnabled(true);

        //봉사활동 카테고리의 취미들 저장
        volunteer = new String[]{"보육원봉사활동","양로원봉사활동","유기동물봉사","재능기부활동"};
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        cate_name_v = (TextView) findViewById(R.id.search_category_name);
        like_c = (CheckBox) findViewById(R.id.like_check);
        like = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("like");
        keyword = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("keyword");

        String tmp = like;
        //유저의 찜목록(like) 존재할 때
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
        cate_name_v.setText(keyword);  //상단에 취미이름 나타내기

        //하트 체크박스 체크 또는 해제시 찜 목록 세팅
        like_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                final boolean OnOff = like_c.isChecked();

                //찜 체크시 해당 취미 이름 like에 추가
                if(OnOff){
                    final_like = like+keyword+"#";
                    Log.d("Final_Like", final_like);
                }
                //찜 해제시 해당 취미 이름 like에서 삭제
                else{
                    String del = keyword+"#";
                    final_like = like.replace(del, "");
                    Log.d("delete_Final_Like", final_like);
                }

                //찜 추가 또는 삭제한 찜 리스트를 유저 DB에 저장
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

        //블로그, 카페 탭 생성
        mTabLayout.addTab(mTabLayout.newTab().setText("블로그"));
        mTabLayout.addTab(mTabLayout.newTab().setText("카페"));

        //봉사활동 취미인지 구별
        for(int i=0; i<volunteer.length; i++) {
            if(volunteer[i].equals(keyword)) {
                is_volunteer = true;
                break;
            }
            else
                is_volunteer = false;
        }
        //봉사활동 취미가 아닐 경우 쇼핑 탭 생성
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

    //액션바의 뒤로가기 버튼 터치시 액티비티 finish
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


