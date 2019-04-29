package com.example.hobbigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CategoryActivity extends AppCompatActivity {

    int count = 0 ;
    String test= "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("카테고리");
    public RecyclerView recyclerView;
    List<CategoryInfo> items=new ArrayList<>();
    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView=(RecyclerView)findViewById(R.id.myrecyclerview);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
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
                recyclerView.setAdapter(new CategoryAdapter(getApplicationContext(),items,R.layout.activity_category));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
