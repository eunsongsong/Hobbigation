package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class RecommendationActivity extends AppCompatActivity {

    int count = 0 ;
    int k = 0;
    String test= "";
    String test_two = "";

    private Button confirm_btn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("추천이미지");

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);


        confirm_btn = (Button)findViewById(R.id.confirm);

        recyclerView=(RecyclerView)findViewById(R.id.myrecyclerview);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

         final List<RecommnedInfo> items=new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                RecommnedInfo[] item=new RecommnedInfo[count];

                String[] tag = new String[count];
                String[][] tag_arr = new String[count][10];


                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    test += ds.child("url").getValue().toString()   + "#";
                    //tag[k] = ds.child("태그").getValue().toString();
                    //k++;
                    test_two += ds.child("태그").getValue().toString() + "%";
                }

                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");

              /*  for ( int i = 0 ; i <  count ; i++) {
                    StringTokenizer st_tag = new StringTokenizer(tag[i], "#");
                    int num_tag = st_tag.countTokens();
                    for( int j = 0 ; j < num_tag ; j++) {
                        tag_arr[i][j] = st_tag.nextToken();
                        Log.d("tag_arr"+i+j, tag_arr[i][j]);
                    }
                }*/

                for ( int i = 0 ; i < count/2 ; i++)
                {
                  item[i] = new RecommnedInfo(st.nextToken(),st.nextToken(),st_two.nextToken(),st_two.nextToken());
                 // Log.d("dddd",st_two.nextToken());
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_recommendation));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ConfirmActivity.class));
            }
        });



    }
}
