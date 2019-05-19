package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import java.util.StringTokenizer;

public class SubActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    String keyword = "";
    TextView cate_name_v;
    CheckBox like_c;
    String finallike = "";

    String like = "";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");
    DatabaseReference myRef2 = database.getReference("취미").child("이미지_태그");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        cate_name_v = (TextView) findViewById(R.id.search_category_name);
        like_c = (CheckBox) findViewById(R.id.like_check);
        //  like_c.setOnCheckedChangeListener(null);
        like = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("like");
        keyword = PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("keyword");

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
        Log.d("---쉐어드", "#"+keyword);
        cate_name_v.setText(keyword);
        like_c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                final boolean OnOff = like_c.isChecked();

                if(OnOff){
                    finallike = like+keyword+"#";
                    Log.d("Final_Like", finallike);
                    like = finallike;
                }
                else{
                    String del = keyword+"#";
                    finallike = like.replace(del, "");
                    Log.d("delete_Final_Like", finallike);
                    like = finallike;
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

                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("like").setValue(finallike);
                                    //찜을 눌렀을 때 취미의 count 올리기
                                    if(isChecked) {
                                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot de : dataSnapshot.getChildren()) {
                                                    String hobby = de.getKey().toString();
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

}


