package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class TabFragment1 extends Fragment {

    private Button recommend_btn;

    AutoScrollViewPager autoViewPager;
    int count = 0;
    String total = "";
    String temp = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("추천이미지");
    public RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_frament1,container,false);

        autoViewPager = (AutoScrollViewPager)rootview.findViewById(R.id.autoViewPager);

        recommend_btn = (Button) rootview.findViewById(R.id.recommend);

        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RecommendationActivity.class));
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    count++;
                    total += ds.child("url").getValue().toString()   + "#";
                }
                StringTokenizer st = new StringTokenizer(total, "#");
                String shuffle[] = new String[count];
                Log.d("count",count+"");
                for (int i = 0; i < count ; i++) {
                    shuffle[i] = st.nextToken();
                }
                for(int i = 0 ; i <shuffle.length; i++)
                {
                    int a = (int)(Math.random()*shuffle.length);
                    int b = (int)(Math.random()*shuffle.length);
                    String temp = shuffle[a];
                    shuffle[a] = shuffle[b];
                    shuffle[b] = temp;
                }
                for (int i = 0; i < count ; i++) {
                    data.add(shuffle[i]);
                }

                AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getContext(), data);
                autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                autoViewPager.setInterval(3500); // 페이지 넘어갈 시간 간격 설정
                autoViewPager.startAutoScroll(); //Auto Scroll 시작
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootview;
    }
}