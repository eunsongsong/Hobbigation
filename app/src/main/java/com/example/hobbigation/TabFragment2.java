package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TabFragment2 extends Fragment {

    int count = 0 ;
    String test= "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("카테고리");
    public RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment2,container,false);

        recyclerView=(RecyclerView)rootview.findViewById(R.id.myrecyclerview);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final List<CategoryInfo> items=new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    count++;
                    test += ds.child("url").getValue().toString()   + "#";
                    test += ds.child("name").getValue().toString()   + "#";
                }
                CategoryInfo[] item=new CategoryInfo[count];
                StringTokenizer st = new StringTokenizer(test, "#");

                for ( int i = 0 ; i < count/2 ; i++)
                {
                    item[i] = new CategoryInfo(st.nextToken(),st.nextToken(), st.nextToken(), st.nextToken());
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new CategoryAdapter(getContext(),items,R.layout.fragment_tab_fragment2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootview;
    }

}