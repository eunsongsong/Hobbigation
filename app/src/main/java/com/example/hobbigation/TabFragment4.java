package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TabFragment4 extends Fragment {

    public RecyclerView wishlist_recycleview;
    public RecyclerView relatedlist_recycleview;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef_two = database2.getReference("취미").child("카테고리");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment4,container,false);

        wishlist_recycleview = (RecyclerView) rootview.findViewById(R.id.wishlist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        wishlist_recycleview.setHasFixedSize(true);
        wishlist_recycleview.setLayoutManager(layoutManager);

        relatedlist_recycleview = (RecyclerView) rootview.findViewById(R.id.related_hobby);

        final LinearLayoutManager layoutManager_related=new LinearLayoutManager(getContext());
        relatedlist_recycleview.setHasFixedSize(true);
        relatedlist_recycleview.setLayoutManager(layoutManager_related);

        final String like = PreferenceUtil.getInstance(getContext()).getStringExtra("like");
        boolean is_empty_like;
        if (TextUtils.isEmpty(like))
        {
            is_empty_like = true;
        }
        else
            is_empty_like = false;
        Log.d("like 0530", like);
        final String[] like_array = like.split("#");
        for ( int i = 0 ; i < like_array.length ; i++)
            Log.d("ddd",like_array[i]);

        Arrays.sort(like_array);
        Log.d("cnt ",like_array.length+"");
        for ( int i = 0 ; i < like_array.length ; i++) {
            if(like_array[i].contains("일기"))
            Log.d("ddd", like_array[i]);
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WishlistInfo> wish_items =new ArrayList<>();
                WishlistInfo[] item = new WishlistInfo[like_array.length];

                int a = 0;

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if (a > like_array.length - 1){
                        break;
                    }

                    if (like_array[a].equals(ds.getKey())) {
                        item[a] = new WishlistInfo(like_array[a], ds.child("url_태그").child("0").child("url").getValue().toString());
                        wish_items.add(item[a]);
                        a++;
                    }
                }
                WishlistAdapter wishlistAdapter = new WishlistAdapter(getContext(),wish_items,R.layout.fragment_tab_fragment4);
                wishlist_recycleview.setAdapter(wishlistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final int[][] related = new int[2][2];

        //실내_야외 0번

        //감상_참여 1번

        final int[] category_num = new int[12];
        final String[] category_name = new String[]{"게임_오락","만들기","문화_공연","봉사활동","식물"
        ,"아웃도어","예술","운동_스포츠","음식","음악","책_글","휴식"};
        //게임_오락
        //만들기
        //문화_공연
        //봉사활동
        //식물
        //아웃도어
        //예술
        //운동_스포츠
        //음식
        //음악
        //책_글
        //휴식
        if (!is_empty_like) {
            myRef_two.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int a = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("카테고리", ds.getKey() + " 번호 : " + a);
                        for (int i = 0; i < like_array.length; i++) {
                            if (ds.child("실내_야외").child("실내").getValue().toString().contains(like_array[i])) {
                                Log.d("먼데 몇번인데", like_array[i]);
                                related[0][0] += 1;
                                category_num[a] += 1;
                            }
                            if (ds.child("실내_야외").child("야외").getValue().toString().contains(like_array[i])) {
                                related[0][1] += 1;
                                category_num[a] += 1;
                            }

                            if (ds.child("참여_감상").child("감상").getValue().toString().contains(like_array[i])) {
                                related[1][0] += 1;
                                category_num[a] += 1;
                            }
                            if (ds.child("참여_감상").child("참여").getValue().toString().contains(like_array[i])) {
                                related[1][1] += 1;
                                category_num[a] += 1;
                            }
                        }
                        a++;
                    }
                    for (int i = 0; i < 12; i++) {
                        Log.d(i + "번", category_num[i] + "");
                    }
                    String result = "";
                    String result_two = "";

                    if (related[0][0] > related[0][1]) {
                        result = "실내";
                    } else if (related[0][0] < related[0][1]) {
                        result = "야외";
                    } else {
                        //same
                    }
                    if (related[1][0] > related[1][1]) {
                        result_two = "감상";
                    } else if (related[1][0] < related[1][1]) {
                        result_two = "참여";
                    } else {
                        //same
                    }
                    Log.d("결과물", result + "ddd" + result_two);
                    // 여기까지 카테고리 가중치까지 다 입력됨
                    final String finalResult = result;
                    final String finalResult_two = result_two;

                    myRef_two.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i = 0;

                            final String[] hobby = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];
                            String hobby_name = "";
                            boolean has_in_like = false;

                            //max값을 찾아라
                            int max = category_num[0];
                            int index = 0;
                                    for (int q = 1; q < 12; q++) {
                                        if (max < category_num[q]) {
                                            max = category_num[q];
                                            index = q;
                                        }
                                    }

                                    // 찜이 하나 있는 경우에만 함으로 max != 0 안함
                            Log.d("dddd", category_name[index] + max);

                          category_num[index] = 0;
                          max = category_num[0];
                            int index_2 = 0;
                                for (int q = 1; q < 12; q++) {
                                    if (max < category_num[q]) {
                                        max = category_num[q];
                                        index_2 = q;
                                    }
                                }

                            int index_3 = 0;
                            if (max != 0) { //카테고리가 두개 인 경우
                                Log.d("두개인경우 ", "입니다");
                                category_num[index_2] = 0;
                                max = category_num[0];
                                for (int q = 1; q < 12; q++) {
                                    if (max < category_num[q]) {
                                        max = category_num[q];
                                        index_3 = q;
                                    }
                                }
                            }
                            if (max != 0) {
                                Log.d("dddd","카테고리 세개");
                            }
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if( ds.getKey().equals(category_name[index]) || ds.getKey().equals(category_name[index_2])) {
                                    for (int k = 0; k < (int) ds.child("실내_야외").child(finalResult).getChildrenCount(); k++) {
                                        hobby_name = ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString();
                                        if (hobby_name.equals(""))
                                            continue;

                                        for (int t = 0; t < like_array.length; t++) {
                                            if (like_array[t].equals(hobby_name)) {
                                                has_in_like = true;
                                            }
                                        }
                                        if (has_in_like) {
                                            has_in_like = false;
                                            continue;
                                        }

                                        if (ds.child("참여_감상").child(finalResult_two).getValue().toString().contains(hobby_name)) {
                                            Log.d("실내 참여인 취미", i + " " + ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString());
                                            hobby[i] = hobby_name;
                                            i++;
                                        }
                                    }
                                }
                            }
                            Log.d("갯수갯수", i + "");
                            Arrays.sort(hobby, 0, i);

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.getKey().equals(hobby[a])) {
                                            Log.d("결과물",ds.child("url_태그").child("1").child("url").getValue().toString());
                                            Log.d("실내 참여취미", ds.getKey() + hobby[a]);
                                            a++;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return rootview;
    }

}