package com.example.hobbigation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TapViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String total= "";
    FirebaseDatabase database3 = FirebaseDatabase.getInstance();
    DatabaseReference myRef3 = database3.getReference("취미");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_view);


        //Top 10 이미지 URL 저장
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


        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        Drawable drawable1 = getResources().getDrawable(R.drawable.selector_click_tab);
        Drawable drawable2 = getResources().getDrawable(R.drawable.selector_click_tab2);
        Drawable drawable3 = getResources().getDrawable(R.drawable.selector_click_tab3);
        Drawable drawable4 = getResources().getDrawable(R.drawable.selector_click_tab4);

        tabLayout.addTab(tabLayout.newTab().setIcon(drawable1));
        tabLayout.addTab(tabLayout.newTab().setIcon(drawable2));
        tabLayout.addTab(tabLayout.newTab().setIcon(drawable3));
        tabLayout.addTab(tabLayout.newTab().setIcon(drawable4));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
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
