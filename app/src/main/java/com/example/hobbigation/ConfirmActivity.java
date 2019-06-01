package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
    int[] weight;
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

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        result_recycler_view = (RecyclerView) findViewById(R.id.result_recycler);

        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        result_recycler_view.setHasFixedSize(true);
        result_recycler_view.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        //스트링 배열 가중치 (정렬안됨)
        //행 갯수 하나로 맞추기
        Log.d("컨펌", "컨펌시작!");

        tag_array = intent.getStringArrayExtra("tag[]");
        weight = intent.getIntArrayExtra("weighcnt[]");
        row = intent.getIntExtra("row",0);
        minus = intent.getIntExtra("minus",0);

        Log.d("로우로우로우 ", row+"");
        Log.d("마이너스", minus+"");

        //잘들어갔는지 확인
        for (int i = 0 ; i < row ; i++)
        {
            if(!TextUtils.isEmpty(tag_array[i]))
            Log.d("aaaa",tag_array[i] + "가중치" + weight[i]);
            else
                Log.d("없는것이다", "없는것이다!이건 빈공간이다 "+ i +" 번");

        }

        //wighcnt[]를 sorted_weigh에 복사
         sorted_weigh = weight.clone();
        //sorted weigh 정렬
        Arrays.sort(sorted_weigh);


        int i, high = 0;
        for ( i = sorted_weigh.length- 1 ; i>0; i--) {
            //가중치가 1이면 이 뒤로 다 1이므로 소팅 중지
            if (sorted_weigh[i] == 1) break;
            //높은 가중치가 어디에 있는지 찾기
            for (int k = high; k < weight.length - minus; k++) {
                if (sorted_weigh[i] == weight[k]) {
                    //가중치 높은 태그와 가중치값 저장
                    String max_tag = tag_array[k];
                    int max_weight = weight[k];
                    //한칸씩 뒤로 밀기
                    for(int n=k-1; n>=high; n--) {
                        tag_array[n+1] = tag_array[n];
                        weight[n+1] = weight[n];
                    }
                    //배열 맨 앞에 가중치 높은 순으로 배치
                    tag_array[high] = max_tag;
                    weight[high] = max_weight;
                    high++;
                    break;
                }
            }

        }


        //이 값이 1보다 큰 가중치 태그 개수
        Log.d("개수", sorted_weigh.length - i - 1 +"");
        int gt_one_tag = sorted_weigh.length - i - 1;

        for (i = 0 ;  i < tag_array.length; i++) {
            if ( !TextUtils.isEmpty(tag_array[i]))
                Log.d("--소트된 태그", tag_array[i] + "가중치 " + weight[i]);
        }
        //정렬끝

        final int tags_num= tag_array.length - minus;

        myRef.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int hobbycnt = (int)dataSnapshot.getChildrenCount();
                String[] hobby = new String[hobbycnt];
                String[] url = new String[hobbycnt];
                int[] weight = new int[hobbycnt];

                int result_cnt = 0;

                //같은 취미가 있는 지 없는지 체크
                boolean exist = false;
                for ( DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String con = ds.child("취미_태그").getValue().toString();
                    for ( count = 4 ;count < tags_num -1 ; count++)
                    {
                        if(con.contains(tag_array[0]) && con.contains(tag_array[1])
                           && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                    hobby[index] = ds.getKey();
                                    result_cnt++;
                                    url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                    weight[index] += 1;
                                    index++;
                            }
                            else
                                exist = false;

                            Log.d("나와라얍 0번 1번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[0]) && con.contains(tag_array[2])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weight[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;

                            Log.d("나와라얍 1번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[0]) && con.contains(tag_array[3])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weight[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;


                            Log.d("나와라얍 0번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[1]) && con.contains(tag_array[2])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weight[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;


                            Log.d("나와라얍 0번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[1]) && con.contains(tag_array[3])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weight[index] += 1;
                                result_cnt++;
                                url[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                index++;
                            }
                            else
                                exist = false;


                            Log.d("나와라얍 0번 2번" + count, con + "### " + count);
                            Log.d("취미가 무엇이니", ds.getKey());
                        }
                        if(con.contains(tag_array[2]) && con.contains(tag_array[3])
                                && con.contains(tag_array[count]))
                        {
                            for(int i=0; i<hobbycnt; i++) {
                                if (ds.getKey().equals(hobby[i])) {
                                    weight[i] += 1;
                                    exist = true;
                                    break;
                                }
                            }
                            if(!exist){
                                hobby[index] = ds.getKey();
                                weight[index] += 1;
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
                int max = weight[0];
                int k, ind = 0;

                //최대 3개 결과 출력
                for ( int i = 0 ; i < 5 ; i++)
                {
                    if ( i > result_cnt - 1 ) {
                        if(result_cnt==0){
                            String logo= "https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/no.png?alt=media&token=568a6365-f948-4460-b712-ee89e672423b";
                            HobbyResultInfo[] null_item = new HobbyResultInfo[1];
                            null_item[0] = new HobbyResultInfo("죄송합니다. 결과가 없습니다. \n다시 한번 골라주세요!", logo);
                            result_items.add(null_item[0]);
                        }
                        break; //결과가 1개 2개 이면 탈출
                    }
                    for ( k = 1; k < result_cnt; k++ ){
                        if ( max  < weight[k]) {
                            max = weight[k];
                            ind = k;
                        }
                    }
                    Log.d("가중치", weight[ind]+"ㅇㅇ"+hobby[ind]);
                    Log.d("ddd",hobby[ind] + " 111" + url[ind]);
                    item[i] = new HobbyResultInfo(hobby[ind],url[ind]);
                    Log.d("아이템",item[i].getHobby_name() + "dasdsad" + item[i].getHobby_url());
                    result_items.add(item[i]);

                    weight[ind] = 0;
                    ind = 0;
                    max = weight[0];
                }
                //마감

                HobbyResultAdapter hobbyResultAdapter = new HobbyResultAdapter(getApplicationContext(),result_items,R.layout.activity_confirm);
                result_recycler_view.setAdapter(hobbyResultAdapter);

                for ( int i = 0 ; i < 20 ; i++) {
                    if (!TextUtils.isEmpty(hobby[i]))
                        Log.d("a12312", hobby[i]);
                    Log.d("tttt", weight[i] + "");

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };
}
