package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * 카테고리가 선택되고 해당 카테고리의 취미들을 실내 야외 참여 감상으로 나누어서 보여주는 화면
 * 4개의 RecyclerView가 있다.
 */
public class CategoryDetailActivity extends AppCompatActivity {

    public RecyclerView recyclerView_in;
    public RecyclerView recyclerView_out;
    public RecyclerView recyclerView_part;
    public RecyclerView recyclerView_see;
    private TextView cate_tv;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");

    String url ="lll";
    String[] indoor;
    String[] outdoor;
    String[] see;
    String[] part;
    String temp;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        cate_tv = (TextView)findViewById(R.id.category_tv);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);    //액션바에 뒤로가기 버튼 나타내기

        //어떤 카테고리가 선택되었는 지 intent로 받아온다.
        final Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        //카테고리의 실내 취미들을 A B C D 형태로 담는다.
        temp = intent.getStringExtra("실내").replace("[","");
        temp = temp.replace("]","");
        temp = temp.replace(" ","");
        indoor = temp.split(",");
        Arrays.sort(indoor);

        //카테고리의 야외 취미들을 A B C D 형태로 담는다.
        temp = intent.getStringExtra("야외").replace("[","");
        temp = temp.replace("]","");
        temp = temp.replace(" ","");
        outdoor = temp.split(",");
        Arrays.sort(outdoor);

        //카테고리의 감상 취미들을 A B C D 형태로 담는다.
        temp = intent.getStringExtra("감상").replace("[","");
        temp = temp.replace("]","");
        temp = temp.replace(" ","");
        see = temp.split(",");
        Arrays.sort(see);

        //카테고리의 참여 취미들을 A B C D 형태로 담는다.
        temp = intent.getStringExtra("참여").replace("[","");
        temp = temp.replace("]","");
        temp = temp.replace(" ","");
        part = temp.split(",");
        Arrays.sort(part);

        cate_tv.setText(category);

        //실내 RecyclerView
        recyclerView_in=(RecyclerView)findViewById(R.id.recycler_indoor);
        final LinearLayoutManager layoutManager_in=new LinearLayoutManager(getApplicationContext());
        layoutManager_in.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_in.setHasFixedSize(true);
        recyclerView_in.setLayoutManager(layoutManager_in);

        //실내 취미의 이름과 url를 DB에서 가져와 RecyclerView 아이템에 담는다.
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     List<InDoorInfo> items_in =new ArrayList<>();
                    InDoorInfo[] item = new InDoorInfo[indoor.length];

                    int a = 0;

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            if (a > indoor.length - 1)
                                break;

                            String b = dataSnapshot.child(indoor[a]).getKey().trim();
                            String c = ds.getKey().trim();

                            if (b.equals(c)) {

                                item[a] = new InDoorInfo(b, ds.child("url_태그").child("0").child("url").getValue().toString());
                                items_in.add(item[a]);
                                a++;
                            }

                        }
                    InDoor_Adapter inDoor_adapter = new InDoor_Adapter(getApplicationContext(),items_in,R.layout.activity_category_detail);
                    recyclerView_in.setAdapter(inDoor_adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        //야외 RecyclerView
        recyclerView_out=(RecyclerView)findViewById(R.id.recycler_outdoor);
        final LinearLayoutManager layoutManager_out=new LinearLayoutManager(getApplicationContext());
        layoutManager_out.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_out.setHasFixedSize(true);
        recyclerView_out.setLayoutManager(layoutManager_out);

        //야외 취미의 이름과 url를 DB에서 가져와 RecyclerView 아이템에 담는다.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<OutDoorInfo> items_out=new ArrayList<>();

                OutDoorInfo[] item = new OutDoorInfo[outdoor.length]; // 9

                int a = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if( a > outdoor.length - 1)
                        break;

                    String b = dataSnapshot.child(outdoor[a]).getKey().trim();
                    String c = ds.getKey().trim();

                    if (b.equals(c)) {
                        item[a] = new OutDoorInfo(b, ds.child("url_태그").child("0").child("url").getValue().toString());
                        items_out.add(item[a]);
                        a++;
                    }
                }
                recyclerView_out.setAdapter(new OutDoor_Adapter(getApplicationContext(),items_out,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //참여 RecyclerView
        recyclerView_part=(RecyclerView)findViewById(R.id.recycler_part);
        final LinearLayoutManager layoutManager_part=new LinearLayoutManager(getApplicationContext());
        layoutManager_part.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_part.setHasFixedSize(true);
        recyclerView_part.setLayoutManager(layoutManager_part);


        //참여 취미의 이름과 url를 DB에서 가져와 RecyclerView 아이템에 담는다.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PartInfo> items_part=new ArrayList<>();

                PartInfo[] item = new PartInfo[part.length]; // 9

                int a = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if( a > part.length - 1)
                        break;

                    String b = dataSnapshot.child(part[a]).getKey().trim();
                    String c = ds.getKey().trim();

                    if (b.equals(c)) {

                        item[a] = new PartInfo(b, ds.child("url_태그").child("0").child("url").getValue().toString());
                        items_part.add(item[a]);
                        a++;
                    }
                }
                recyclerView_part.setAdapter(new Part_Adapter(getApplicationContext(),items_part,R.layout.activity_category_detail));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //감상 RecyclerView
        recyclerView_see=(RecyclerView)findViewById(R.id.recycler_see);
        final LinearLayoutManager layoutManager_see=new LinearLayoutManager(getApplicationContext());
        layoutManager_see.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_see.setHasFixedSize(true);
        recyclerView_see.setLayoutManager(layoutManager_see);

        //감상 취미의 이름과 url를 DB에서 가져와 RecyclerView 아이템에 담는다.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SeeInfo> items_see=new ArrayList<>();

                SeeInfo[] item = new SeeInfo[see.length]; // 9

                int a = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if( a > see.length - 1)
                        break;

                    String b = dataSnapshot.child(see[a]).getKey().trim();
                    String c = ds.getKey().trim();

                    if (b.equals(c)) {

                        item[a] = new SeeInfo(b, ds.child("url_태그").child("0").child("url").getValue().toString());
                        items_see.add(item[a]);
                        a++;
                    }
                }
                recyclerView_see.setAdapter(new See_Adapter(getApplicationContext(),items_see,R.layout.activity_category_detail));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //액션바의 뒤로가기 버튼 터치시 액티비티 finish
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
