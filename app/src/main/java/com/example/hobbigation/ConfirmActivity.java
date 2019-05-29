package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ConfirmActivity extends AppCompatActivity {

    String[] tag_array;
    int[] weighcnt;
    int[] sorted_weigh;

    int row;

    int count = 0;

    int minus;
    int index = 0 ;
    public RecyclerView result_recycler_view;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        result_recycler_view = (RecyclerView) findViewById(R.id.result_recycler);

        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        result_recycler_view.setHasFixedSize(true);
        result_recycler_view.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        //스트링 배열 가중치 (정렬안됨)
        //행 갯수 하나로 맞추기
        Log.d("컨펌", "컨펌시작!");

        tag_array = intent.getStringArrayExtra("tag[]");
        weighcnt = intent.getIntArrayExtra("weighcnt[]");
        row = intent.getIntExtra("row",0);
        minus = intent.getIntExtra("minus",0);

        //잘들어갔는지 확인
        for (int i = 0 ; i < row ; i++)
        {
            if(!TextUtils.isEmpty(tag_array[i]))
            Log.d("aaaa",tag_array[i]);
            else
                Log.d("없는것이다", "없는것이다!이건 빈공간이다 "+ i +" 번");
            Log.d("bbbbb",weighcnt[i]+"");
        }

        //wighcnt[]를 sorted_weigh에 복사
         sorted_weigh = weighcnt.clone();
        //sorted weigh 정렬
        Arrays.sort(sorted_weigh);

        int i, p = 0;
        for ( i = sorted_weigh.length- 1 ; i>0; i--) {
            if (sorted_weigh[i] == 1) break;
            for (int k = 0; k < weighcnt.length; k++) {
                if (sorted_weigh[i] == weighcnt[k]) {
                    String tmp = tag_array[k];
                    tag_array[k] = tag_array[p];
                    tag_array[p] = tmp;
                    weighcnt[k] = 1;
                    p++;
                    break;
                }
            }
        }

        for ( i = 0 ;  i < tag_array.length; i++) {
            if ( !TextUtils.isEmpty(tag_array[i]))
                Log.d("--소트된 태그", tag_array[i]);
        }
        //정렬끝

        final int tags_num= tag_array.length - minus;

        myRef.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int hobbycnt = (int)dataSnapshot.getChildrenCount();
                String[] hobby = new String[hobbycnt];
                String[] url = new String[hobbycnt];
                int[] weighcnt = new int[hobbycnt];

                int result_cnt = 0;

                //같은 취미가 있는 지 없는지 체크
                boolean exist = false;
                for ( DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String con = ds.child("취미_태그").getValue().toString();
                    for ( count = 3 ;count < tags_num -1 ; count++)
                    {
                        if(con.contains(tag_array[0]) && con.contains(tag_array[1])
                           && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weighcnt[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                    hobby[index] = ds.getKey();
                                    result_cnt++;
                                    url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                    weighcnt[index] += 1;
                                    index++;
                            }
                            else
                                exist = false;

                            Log.d("나와라얍 0번 1번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[1]) && con.contains(tag_array[2])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weighcnt[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weighcnt[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;

                            Log.d("나와라얍 1번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[0]) && con.contains(tag_array[2])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weighcnt[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weighcnt[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;


                            Log.d("나와라얍 0번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                    }

                }

                // 취미 가중치는 매겨져있음
                List<HobbyResultInfo> result_items =new ArrayList<>();
                HobbyResultInfo[] item = new HobbyResultInfo[result_cnt];

                //맥스 찾고 0으로 만들기 x 3번
                int max = weighcnt[0];
                int k, ind = 0;

                for ( int i = 0 ; i < 3 ; i++)
                {
                    for ( k = 1; k < result_cnt; k++ ){
                        if ( max  < weighcnt[k]) {
                            max = weighcnt[k];
                            ind = k;
                        }
                    }
                    weighcnt[ind] = 0;
                    Log.d("ddd",hobby[ind] + " 111" + url[ind]);
                    item[i] = new HobbyResultInfo(hobby[ind],url[ind]);
                    Log.d("아이템",item[i].getHobby_name() + "dasdsad" + item[i].getHobby_url());
                    result_items.add(item[i]);

                    ind = 0;

                    max = weighcnt[0];
                }

                HobbyResultAdapter hobbyResultAdapter = new HobbyResultAdapter(getApplicationContext(),result_items,R.layout.activity_confirm);
                result_recycler_view.setAdapter(hobbyResultAdapter);

                for ( int i = 0 ; i < 20 ; i++) {
                    if (!TextUtils.isEmpty(hobby[i]))
                        Log.d("a12312", hobby[i]);
                    Log.d("tttt", weighcnt[i] + "");

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
