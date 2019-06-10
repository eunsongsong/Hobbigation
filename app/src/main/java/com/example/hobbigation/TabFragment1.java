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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/* Tap1 - MainPage (인기취미, 취미카테고리 버튼, 취미 추천받기 버튼) */
public class TabFragment1 extends Fragment {

    private Button recommend_btn;
    private RecyclerView recyclerView;

    AutoScrollViewPager autoViewPager;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("추천이미지");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database2.getReference("취미").child("카테고리");

    FirebaseDatabase database3 = FirebaseDatabase.getInstance();
    DatabaseReference myRef3 = database3.getReference("사용자");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment1,container,false);

        autoViewPager = (AutoScrollViewPager)rootview.findViewById(R.id.autoViewPager);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.frag_1_recycle);
        //Top10 이미지를 보여줌

        //카테고리 RecyclerView
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //가로 Swipe 가능
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new Main_Category_Decoration(15));
        recyclerView.setLayoutManager(layoutManager);

        final List<Main_CategoryItem> items =new ArrayList<>();
        //취미 카테고리 버튼을 RecyclerView로 만들어줌
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

        //취미 추천받기 화면으로 넘어감
        recommend_btn = (Button) rootview.findViewById(R.id.recommend);
        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RecommendationActivity.class));
            }
        });

        //Top 10 이미지를 보여줌
        ArrayList<AutoScroll_Info>  auto_items = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        final AutoScroll_Info[] auto_item = new AutoScroll_Info[10];

        //BeforSignIn에서 저장한 Top10 이미지와 이름을 가져오고 #으로 구분
        StringTokenizer st_url = new StringTokenizer(PreferenceUtil.getInstance(getContext()).getStringExtra("total"),"#");
        StringTokenizer st_names = new StringTokenizer(PreferenceUtil.getInstance(getContext()).getStringExtra("names"),"#");

        for ( int j = 0 ; j < 10; j++)
        {
            auto_item[j] = new AutoScroll_Info(st_names.nextToken(), st_url.nextToken());
        }
        //10위부터 읽어 들어 오기 때문에 add를 뒤에서부터한다
        for ( int k = 9 ; k >= 0 ; k--) {
            auto_items.add(auto_item[k]);
        }

        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getContext(), auto_items);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(3500); // 페이지 넘어갈 시간 간격 설정;
        autoViewPager.setBorderAnimation(true);
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        //세부 카테고리 스트링 보내기 ( 실내  야외 만으로 한 카테고리의 취미가 다 포함됨)
        String[] category = {"문화_공연","음악","예술","책_글","운동_스포츠","만들기","음식","게임_오락","아웃도어","식물","휴식","봉사활동"};
        for(int i=0; i<12; i++) {
            final int finalI = i;
            myRef2.child(category[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String hobby, tmp = "";
                    hobby = dataSnapshot.child("실내_야외").child("실내").getValue().toString();
                    hobby += "," + dataSnapshot.child("실내_야외").child("야외").getValue().toString();

                    tmp = hobby.replace("]", "");
                    tmp= tmp.replace("[", "");
                    tmp = tmp.replace(" ", "");

                    PreferenceUtil.getInstance(getContext()).putStringExtra("detail" + finalI, tmp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        //회원이 찜 해놓은 취미를 DB에서 읽어서 취미이름들을 저장
        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if( ds.child("email").getValue().toString().equals(mFirebaseUser.getEmail()))
                    {
                        PreferenceUtil.getInstance(getContext()).putStringExtra("like",ds.child("like").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootview;
    }
}