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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class RecommendationActivity extends AppCompatActivity {

    int count = 0 ;
    String test= "";
    String test_two = "";

    private Button confirm_btn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");


    public RecyclerView recyclerView;
    private Object String;

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
        String[] detail = new String[12];

        for(int i =0; i<12; i++) {
            detail[i] = PreferenceUtil.getInstance(this).getStringExtra("detail" + i);
            detail[i] = FirstStringTest(detail[i]);
            detail[i] = LastStringTest(detail[i]);
            Log.d("----받은거", detail[i]);
        }

        //받은 세부 카테고리 배열에서 중복 값 제거 + 정렬
        String[] dupculture = detail[0].split(",");
        Object culture[]= removeDuplicateArray(dupculture);

        String[] dupmusic = detail[1].split(",");
        Object music[]= removeDuplicateArray(dupmusic);

        String[] dupart = detail[2].split(",");
        Object art[]= removeDuplicateArray(dupart);

        String[] dupbook = detail[3].split(",");
        Object book[]= removeDuplicateArray(dupbook);

        String[] dupsports = detail[4].split(",");
        Object sports[]= removeDuplicateArray(dupsports);

        String[] dupmake = detail[5].split(",");
        Object make[]= removeDuplicateArray(dupmake);

        String[] dupfood = detail[6].split(",");
        Object food[] = removeDuplicateArray(dupfood);

        String[] dupgame = detail[7].split(",");
        Object game[] = removeDuplicateArray(dupgame);

        String[] dupoutdoor = detail[8].split(",");
        Object outdoor[]= removeDuplicateArray(dupoutdoor);

        String[] dupplant = detail[9].split(",");
        Object plant[] = removeDuplicateArray(dupplant);

        String[] duprest = detail[10].split(",");
        Object rest[] = removeDuplicateArray(duprest);

        String[] dupvol = detail[11].split(",");
        Object vol[] = removeDuplicateArray(dupvol);
        Collections.shuffle(Arrays.asList(culture));
        Collections.shuffle(Arrays.asList(music));
        Collections.shuffle(Arrays.asList(art));
        Collections.shuffle(Arrays.asList(book));
        Collections.shuffle(Arrays.asList(sports));
        Collections.shuffle(Arrays.asList(make));
        Collections.shuffle(Arrays.asList(food));
        Collections.shuffle(Arrays.asList(game));
        Collections.shuffle(Arrays.asList(outdoor));
        Collections.shuffle(Arrays.asList(plant));
        Collections.shuffle(Arrays.asList(rest));
        Collections.shuffle(Arrays.asList(vol));


        final String[][]  total = {{culture[0].toString(),music[0].toString(), art[0].toString(), book[0].toString(),
            sports[0].toString(), make[0].toString(), food[0].toString(), game[0].toString(), outdoor[0].toString(),
            plant[0].toString(), rest[0].toString(), vol[0].toString()},

                {culture[1].toString(),music[1].toString(), art[1].toString(), book[1].toString(),
                        sports[1].toString(), make[1].toString(), food[1].toString(), game[1].toString(), outdoor[1].toString(),
                        plant[1].toString(), rest[1].toString(), vol[1].toString()},


                {culture[2].toString(),music[2].toString(), art[2].toString(), book[2].toString(),
                        sports[2].toString(), make[2].toString(), food[2].toString(), game[2].toString(), outdoor[2].toString(),
                        plant[2].toString(), rest[2].toString(), vol[2].toString()},

        };
        Arrays.sort(total[0]);
        Arrays.sort(total[1]);
        Arrays.sort(total[2]);

        //이미지, 태그 가져오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            
                RecommnedInfo[] item=new RecommnedInfo[18];

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    for ( int k = 0; k < 3 ; k++) {
                        for(int i = 0 ; i < 12 ;i++) {
                            if (ds.getKey().equals(total[k][i])) {
                                int count = (int) ds.child("url_태그").getChildrenCount();
                                int a2 = (int) (Math.random() * count);

                                test += ds.child("url_태그").child(a2 + "").child("url").getValue().toString() + "#";
                                test_two += ds.child("url_태그").child(a2 + "").child("태그").getValue().toString() + "%";

                            }
                        }
                    }
                }

                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");


                for ( int i = 0 ; i < 18; i++)
                {
                    Log.d("i",i+"");
                   item[i] = new RecommnedInfo(st.nextToken(),st.nextToken(),st_two.nextToken(),st_two.nextToken());
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

    //스트링 첫번째가 ,로 시작하면 ,없애고 반환
    public String FirstStringTest(String str) {
        String tmp;
        tmp = str.substring(0,1);
        if(tmp.equals(",")){
            str = str.substring(1);
        }
        return str;
    }
    //스트링 첫번째가 ,로 시작하면 ,없애고 반환
    public String LastStringTest(String str) {
        String tmp;
        tmp = str.substring(str.length()-1);
        if(tmp.equals(",")){
            str = str.substring(0,str.length()-1);
        }
        return str;
    }


    public Object[] removeDuplicateArray(String[] array){
        Object[] removeArray=null;
        TreeSet ts=new TreeSet();
        for(int i=0; i<array.length; i++){
            ts.add(array[i]);
        }
        removeArray= ts.toArray();
        return removeArray;
    }

}
