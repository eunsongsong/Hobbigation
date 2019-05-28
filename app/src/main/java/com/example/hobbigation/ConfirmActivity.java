package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.StringTokenizer;

public class ConfirmActivity extends AppCompatActivity {

    String[] tag_array;
    int[] weighcnt;
    int[] sorted_weigh;
    int row;
    private TextView top1;
    private TextView top2;
    private TextView top3;

    int count = 0;

    int minus;
    int index = 0 ;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        img1 = (ImageView) findViewById(R.id.con_result1);
        img2 = (ImageView) findViewById(R.id.con_result2);
        img3 = (ImageView) findViewById(R.id.con_result3);
        top1 = (TextView) findViewById(R.id.hobby_result1);
        top2 = (TextView) findViewById(R.id.hobby_result2);
        top3 = (TextView) findViewById(R.id.hobby_result3);
        Intent intent = getIntent();
        //스트링 배열 가중치 (정렬안됨)
        //행 갯수 하나로 맞추기


        tag_array = intent.getStringArrayExtra("tag[]");
        weighcnt = intent.getIntArrayExtra("weighcnt[]");
        row = intent.getIntExtra("row",0);
        minus = intent.getIntExtra("minus",0);

        //잘들어갔는지 확인
        for (int i = 0 ; i < row ; i++)
        {
            if(!TextUtils.isEmpty(tag_array[i]))
            Log.d("aaaa",tag_array[i]);
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
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;


                            Log.d("나와라얍 0번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                    }
                    /*
                    String con = ds.child("취미_태그").getValue().toString();
                    for(count = 2; count < tags_num -1 ; count++ ) {
                        if (con.contains(tag_array[0]) && con.contains(tag_array[1])
                                && con.contains(tag_array[count])) {
                            if (count > tags_num - 1)
                                break;
                            Log.d("나와라얍 " + count, con + "### " + count);
                        }
                    }
                    */
                }
                //맥스 찾고 0으로 만들기 x 3번
                int max = weighcnt[0];
                int k, ind = 0;

                for(k=1; k<hobbycnt; k++){
                    if(max < weighcnt[k]) {
                        max = weighcnt[k];
                        ind =k;
                    }
                }

                Glide.with(getApplicationContext())
                        .load(url[ind])
                        .into(img1);
                top1.setText(hobby[ind]);

                weighcnt[ind] = 0;
                max = weighcnt[0];

                for(k=1; k<hobbycnt; k++){
                    if(max < weighcnt[k]) {
                        max = weighcnt[k];
                        ind =k;
                    }
                }

                Glide.with(getApplicationContext())
                        .load(url[ind])
                        .into(img2);
                top2.setText(hobby[ind]);

                weighcnt[ind] = 0;
                max = weighcnt[0];

                for(k=1; k<hobbycnt; k++){
                    if(max < weighcnt[k]) {
                        max = weighcnt[k];
                        ind =k;
                    }
                }

                Glide.with(getApplicationContext())
                        .load(url[ind])
                        .into(img3);
                top3.setText(hobby[ind]);


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
