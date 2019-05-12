package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class TabFragment1 extends Fragment {

    private Button recommend_btn;
    private RecyclerView recyclerView;

    AutoScrollViewPager autoViewPager;
    int count = 0;
    String total = "";
    String temp = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("추천이미지");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment1,container,false);

        autoViewPager = (AutoScrollViewPager)rootview.findViewById(R.id.autoViewPager);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.frag_1_recycle);

        //카테고리 RecyclerView
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //가로 Swipe
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new Main_Category_Decoration(15));
        recyclerView.setLayoutManager(layoutManager);

        final List<Main_CategoryItem> items =new ArrayList<>();
        Main_CategoryItem[] item=new Main_CategoryItem[12];

        item[0] = new Main_CategoryItem("문화/공연", getResources().getDrawable(R.drawable.show));
        item[1] = new Main_CategoryItem("음악",getResources().getDrawable(R.drawable.music));
        item[2] = new Main_CategoryItem("예술",getResources().getDrawable(R.drawable.art));
        item[3] = new Main_CategoryItem("책/글",getResources().getDrawable(R.drawable.book));
        item[4] = new Main_CategoryItem("운동/스포츠",getResources().getDrawable(R.drawable.sports));
        item[5] = new Main_CategoryItem("만들기",getResources().getDrawable(R.drawable.craft));
        item[6] = new Main_CategoryItem("음식",getResources().getDrawable(R.drawable.burger));
        item[7] = new Main_CategoryItem("게임/오락",getResources().getDrawable(R.drawable.game));
        item[8] = new Main_CategoryItem("아웃도어",getResources().getDrawable(R.drawable.outdoor));
        item[9] = new Main_CategoryItem("식물",getResources().getDrawable(R.drawable.plant));
        item[10] = new Main_CategoryItem("휴식",getResources().getDrawable(R.drawable.sleep));
        item[11] = new Main_CategoryItem("봉사활동",getResources().getDrawable(R.drawable.volunteer));

      //  문화/공연 음악 예술 책/글 운동/스포츠 만들기 음식 게임/오락 아웃도어 식물 휴식 봉사활동
        for ( int i = 0 ; i < 12 ; i++)
        {
            items.add(item[i]);
        }
        recyclerView.setAdapter(new Main_CategoryAdapter(getContext(),items,R.layout.fragment_tab_fragment1));

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