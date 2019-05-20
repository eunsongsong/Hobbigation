package com.example.hobbigation;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_view);


        //

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
