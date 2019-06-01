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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment4,container,false);

        wishlist_recycleview = (RecyclerView) rootview.findViewById(R.id.wishlist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        wishlist_recycleview.setHasFixedSize(true);
        wishlist_recycleview.setLayoutManager(layoutManager);

        String like = PreferenceUtil.getInstance(getContext()).getStringExtra("like");

        Log.d("like 0530", like);
        final String[] like_array = like.split("#");
        for ( int i = 0 ; i < like_array.length ; i++)
            Log.d("ddd",like_array[i]);

        Arrays.sort(like_array);
        Log.d("cnt ",like_array.length+"");
        for ( int i = 0 ; i < like_array.length ; i++)
            Log.d("ddd",like_array[i]);


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
                Log.d( "넌 실행 되니","ddddd");
                WishlistAdapter wishlistAdapter = new WishlistAdapter(getContext(),wish_items,R.layout.fragment_tab_fragment4);
                wishlist_recycleview.setAdapter(wishlistAdapter);
                Log.d( "넌 실행 되었니","kkk");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootview;
    }


}