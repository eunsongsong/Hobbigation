package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity {

    public RecyclerView recyclerView_in;
    public RecyclerView recyclerView_out;
    public RecyclerView recyclerView_part;
    public RecyclerView recyclerView_see;
    private TextView cate_tv;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("카테고리");

    int count = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        cate_tv = (TextView)findViewById(R.id.category_tv);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        Log.d("ddddasdsadd",category);
        cate_tv.setText(category);

        recyclerView_in=(RecyclerView)findViewById(R.id.recycler_indoor);
        final LinearLayoutManager layoutManager_in=new LinearLayoutManager(getApplicationContext());
        layoutManager_in.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_in.setHasFixedSize(true);
        recyclerView_in.setLayoutManager(layoutManager_in);

        final List<InDoorInfo> items_in =new ArrayList<>();

        myRef.child(category).child("실내_야외").child("실내").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                InDoorInfo[] item=new InDoorInfo[count];

                int i = 0;
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ddddd",ds.getValue().toString());
                    item[i] = new InDoorInfo(ds.getValue().toString());
                    items_in.add(item[i]);
                    i++;
                }
                recyclerView_in.setAdapter(new InDoor_Adapter(getApplicationContext(),items_in,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView_out=(RecyclerView)findViewById(R.id.recycler_outdoor);
        final LinearLayoutManager layoutManager_out=new LinearLayoutManager(getApplicationContext());
        layoutManager_out.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_out.setHasFixedSize(true);
        recyclerView_out.setLayoutManager(layoutManager_out);

        final List<OutDoorInfo> items_out=new ArrayList<>();

        myRef.child(category).child("실내_야외").child("야외").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                OutDoorInfo[] item=new OutDoorInfo[count];

                int i = 0;

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ddddd",ds.getValue().toString());
                    item[i] = new OutDoorInfo(ds.getValue().toString());
                    items_out.add(item[i]);
                    i++;
                }
                recyclerView_out.setAdapter(new OutDoor_Adapter(getApplicationContext(),items_out,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView_part=(RecyclerView)findViewById(R.id.recycler_part);
        final LinearLayoutManager layoutManager_part=new LinearLayoutManager(getApplicationContext());
        layoutManager_part.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_part.setHasFixedSize(true);
        recyclerView_part.setLayoutManager(layoutManager_part);

        final List<PartInfo> items_part=new ArrayList<>();

        myRef.child(category).child("참여_감상").child("참여").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                PartInfo[] item=new PartInfo[count];

                int i = 0;

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ddddd",ds.getValue().toString());
                    item[i] = new PartInfo(ds.getValue().toString());
                    items_part.add(item[i]);
                    i++;
                }
                recyclerView_part.setAdapter(new Part_Adapter(getApplicationContext(),items_part,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView_see=(RecyclerView)findViewById(R.id.recycler_see);
        final LinearLayoutManager layoutManager_see=new LinearLayoutManager(getApplicationContext());
        layoutManager_see.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_see.setHasFixedSize(true);
        recyclerView_see.setLayoutManager(layoutManager_see);

        final List<SeeInfo> items_see=new ArrayList<>();

        myRef.child(category).child("참여_감상").child("감상").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                SeeInfo[] item=new SeeInfo[count];

                int i = 0;

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ddddd",ds.getValue().toString());
                    item[i] = new SeeInfo(ds.getValue().toString());
                    items_see.add(item[i]);
                    i++;
                }
                recyclerView_see.setAdapter(new See_Adapter(getApplicationContext(),items_see,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
