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
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class RecommendationActivity extends AppCompatActivity {

    int count = 0 ;
    int k = 0;
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

        int ran1 = (int)(Math.random()*culture.length);
        int ran2  = (int)(Math.random()*music.length);
        int ran3 = (int)(Math.random()*art.length);
        int ran4 = (int)(Math.random()*book.length);
        int ran5 = (int)(Math.random()*sports.length);
        int ran6 = (int)(Math.random()*make.length);
        int ran7 = (int)(Math.random()*food.length);
        int ran8 = (int)(Math.random()*game.length);
        int ran9 = (int)(Math.random()*outdoor.length);
        int ran10 = (int)(Math.random()*plant.length);
        int ran11 = (int)(Math.random()*rest.length);
        int ran12 = (int)(Math.random()*vol.length);


        final String[]  total = {culture[ran1].toString(),music[ran2].toString(), art[ran3].toString(), book[ran4].toString(),
            sports[ran5].toString(), make[ran6].toString(), food[ran7].toString(), game[ran8].toString(), outdoor[ran9].toString(),
            plant[ran10].toString(), rest[ran11].toString(), vol[ran12].toString()};

        Arrays.sort(total);

        //이미지, 태그 가져오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("vvvv",dataSnapshot.getKey());
                RecommnedInfo[] item=new RecommnedInfo[12];

                int i =0;
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if( ds.getKey().equals(total[i]))
                    {
                        int count = (int )ds.child("url_태그").getChildrenCount();
                        int a2 = (int) (Math.random()*count);

                        test =  ds.child("url_태그").child(a2+"").child("url").getValue().toString() + "#";
                        test_two += ds.child("url_태그").child(a2+"").child("태그").getValue().toString() + "%";
                        i++;
                        Log.d("aaa",i+"");
                    }

                }

                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");


                for ( i = 0 ; i < 2 ; i++)
                {
                    item[i] = new RecommnedInfo(st.nextToken(),st.nextToken(),st_two.nextToken(),st_two.nextToken());
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_recommendation));
                /*
                count = (int)dataSnapshot.child("url_태그").getChildrenCount();
                RecommnedInfo[] item=new RecommnedInfo[12];
                int a2 = (int)(Math.random()*count);
                Log.d("-d----d--",a2+"");
                Log.d("key",dataSnapshot.getKey());

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if( ds.getKey().equals("url_태그")){
                        test = ds.child(a2+"").child("url").getValue().toString()+"#";
                        test_two += ds.child(a2+"").child("태그").getValue().toString() + "%";
                    }
                }

                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");


                for ( int i = 0 ; i < 1 ; i++)
                {
                    item[i] = new RecommnedInfo(st.nextToken(),"https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/hobby-%E3%85%8B%2F%EC%BD%98%EC%84%9C%ED%8A%B8(2).png?alt=media&token=04d7558e-1483-4fd3-a2a3-0ad6e804280c",st_two.nextToken(),"");
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_recommendation));
*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                count = (int)dataSnapshot.getChildrenCount();
                RecommnedInfo[] item=new RecommnedInfo[count];

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    test += ds.child("url").getValue().toString()   + "#";
                    //tag[k] = ds.child("태그").getValue().toString();
                    //k++;
                    test_two += ds.child("태그").getValue().toString() + "%";
                }

                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");

                String shuffle[] = new String[count];
                String shuffle_two[] = new String[count];

                for (int i = 0; i < count ; i++) {
                    shuffle[i] = st.nextToken();
                    shuffle_two[i] = st_two.nextToken();
                }

                for(int i = 0 ; i <shuffle.length; i++)
                {
                    int a = (int)(Math.random()*shuffle.length);
                    int b = (int)(Math.random()*shuffle.length);
                    String temp = shuffle[a];
                    shuffle[a] = shuffle[b];
                    shuffle[b] = temp;

                    String temp_two = shuffle_two[a];
                    shuffle_two[a] = shuffle_two[b];
                    shuffle_two[b] = temp_two;
                }

                for ( int i = 0 ; i < count ; i+=2)
                {
                    item[i] = new RecommnedInfo(shuffle[i],shuffle[i+1],shuffle_two[i],shuffle_two[i+1]);
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_recommendation));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */


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
