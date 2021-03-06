package com.example.hobbigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

/**
 * 취미 추천 이미지를 36개를 보여주는 화면이다.
 * 12개의 카테고리가 있고 카테고리별로 랜덤으로 1개 씩 이미지를 보여준다. ( 1 세트)
 * 3개의 세트로 구성되어 나타난다.
 * 사용자가 이미지를 선택할 때 마다 사용자의 tag 정보가 바뀐다.
 * 사용자가 이미지를 5개 이상 눌렀을 때에만 결과 확인이 가능하다.
 */
public class RecommendationActivity extends AppCompatActivity {

    String test = "";
    String test_two = "";
    int minus = 0;
    private Button confirm_btn;

    int touch = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");

    FirebaseDatabase database_two = FirebaseDatabase.getInstance();
    DatabaseReference myRef_two = database_two.getReference("사용자");

    FirebaseAuth firebaseAuth;

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database2.getReference("사용자");

    public RecyclerView recyclerView;

    String temp2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기

        firebaseAuth = FirebaseAuth.getInstance();

        PreferenceUtil.getInstance(getApplicationContext()).putIntExtra("touch",touch);
        confirm_btn = (Button) findViewById(R.id.confirm);

        recyclerView = (RecyclerView) findViewById(R.id.myrecyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final List<RecommnedInfo> items = new ArrayList<>();
        String[] detail = new String[12];

        //detail 배열에 한 카테고리 별로 취미를 담는다.
        //예외처리를 하여 A,B,C,,, 형태로 만들어 준다.
        for (int i = 0; i < 12; i++) {
            detail[i] = PreferenceUtil.getInstance(this).getStringExtra("detail" + i);
            if (detail[i].startsWith(","))
                detail[i] = detail[i].substring(1);
            else if (detail[i].endsWith(","))
                detail[i] = detail[i].substring(0, detail[i].length() - 1);
        }

        //받은 세부 카테고리 배열에서 중복 값 제거 + 정렬
        String[] dupculture = detail[0].split(",");
        Object culture[] = removeDuplicateArray(dupculture);

        String[] dupmusic = detail[1].split(",");
        Object music[] = removeDuplicateArray(dupmusic);

        String[] dupart = detail[2].split(",");
        Object art[] = removeDuplicateArray(dupart);

        String[] dupbook = detail[3].split(",");
        Object book[] = removeDuplicateArray(dupbook);

        String[] dupsports = detail[4].split(",");
        Object sports[] = removeDuplicateArray(dupsports);

        String[] dupmake = detail[5].split(",");
        Object make[] = removeDuplicateArray(dupmake);

        String[] dupfood = detail[6].split(",");
        Object food[] = removeDuplicateArray(dupfood);

        String[] dupgame = detail[7].split(",");
        Object game[] = removeDuplicateArray(dupgame);

        String[] dupoutdoor = detail[8].split(",");
        Object outdoor[] = removeDuplicateArray(dupoutdoor);

        String[] dupplant = detail[9].split(",");
        Object plant[] = removeDuplicateArray(dupplant);

        String[] duprest = detail[10].split(",");
        Object rest[] = removeDuplicateArray(duprest);

        String[] dupvol = detail[11].split(",");
        Object vol[] = removeDuplicateArray(dupvol);

        //각 카테고리 별로 순서를 섞어 준다.
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

        //섞은 것에서 0번 1번 2번 만 가져온다 -> 3개씩만 뽑는다.
        final String[][] total = {{culture[0].toString(), music[0].toString(), art[0].toString(), book[0].toString(),
                sports[0].toString(), make[0].toString(), food[0].toString(), game[0].toString(), outdoor[0].toString(),
                plant[0].toString(), rest[0].toString(), vol[0].toString()},

                {culture[1].toString(), music[1].toString(), art[1].toString(), book[1].toString(),
                        sports[1].toString(), make[1].toString(), food[1].toString(), game[1].toString(), outdoor[1].toString(),
                        plant[1].toString(), rest[1].toString(), vol[1].toString()},


                {culture[2].toString(), music[2].toString(), art[2].toString(), book[2].toString(),
                        sports[2].toString(), make[2].toString(), food[2].toString(), game[2].toString(), outdoor[2].toString(),
                        plant[2].toString(), rest[2].toString(), vol[2].toString()},

        };

        // DB에서 읽을 때의 편의성을 위해서 가나다 순으로 정렬
        Arrays.sort(total[0]);
        Arrays.sort(total[1]);
        Arrays.sort(total[2]);

        //이미지, 태그 가져오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                RecommnedInfo[] item = new RecommnedInfo[18];

                //12개씩 3번 불러온다.
                //랜덤을 쓰는 이유는 하나의 취미에 여러이미지 중 하나씩 가져온다.
                //각 취미이미지의 태그를 가져온다.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (int k = 0; k < 3; k++) {
                        for (int i = 0; i < 12; i++) {
                            if (ds.getKey().equals(total[k][i])) {
                                int count = (int) ds.child("url_태그").getChildrenCount();
                                int a2 = (int) (Math.random() * count);

                                temp2 += ds.getKey() + "#";
                                test += ds.child("url_태그").child(a2 + "").child("url").getValue().toString() + "#";
                                test_two += ds.child("url_태그").child(a2 + "").child("태그").getValue().toString() + "%";
                            }
                        }
                    }
                }
                //DB에서 취미를 한번 스캔하여 읽을수 있기 때문에
                // #나 %를 붙여 연결하여 저장하고 다시 StringTokenizer를 이용하여 나눈다.
                StringTokenizer st = new StringTokenizer(test, "#");
                StringTokenizer st_two = new StringTokenizer(test_two, "%");
                StringTokenizer st_three = new StringTokenizer(temp2, "#");

                //RecyclerView Item에 담는다.
                for (int i = 0; i < 18; i++) {
                    item[i] = new RecommnedInfo(st.nextToken(), st.nextToken(), st_two.nextToken(), st_two.nextToken()
                    ,st_three.nextToken(), st_three.nextToken());
                    items.add(item[i]);
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_recommendation));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //터치 횟수 측정
        myRef_two.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            //사용자의 tag정보에 %로 연결되서 저장되고
            // % 갯수를 보고 사용자가 이미지를 몇 번 체크 하는 지 센다
            // 5번이 넘으면 결과 확인 버튼 활성화
            @SuppressLint("NewApi")
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                 FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    if( mFirebaseUser.getEmail().equals(dataSnapshot.child("email").getValue().toString())) {
                        StringTokenizer st = new StringTokenizer(dataSnapshot.child("tag").getValue().toString(), "%");
                        int touch_cnt = st.countTokens();
                        if (touch_cnt > 4 )
                        {
                            confirm_btn.setEnabled(true);
                            confirm_btn.setText("결 과 확 인 ->");
                            confirm_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),  R.drawable.round_rectangle_black));
                            confirm_btn.setTextColor(Color.rgb(255,255,255));
                        }
                        else {
                            confirm_btn.setEnabled(false);
                            confirm_btn.setText("5개 이상의 이미지를 선택 해주세요");
                            confirm_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),  R.drawable.round_rectangle));
                            confirm_btn.setTextColor(Color.rgb(0,0,0));
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //결과 확인 버튼 리스너
        //선택된 이미지들의 태그를 같은태그가 있는지 체크를 하고 있으면
        //가중치를 메긴다.
        //정수배열과 스트링 배열에 저장
        //저장된 정보를 ConfirmActivity로 전달
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                if (mFirebaseUser.getEmail().equals(ds.child("email").getValue().toString())) {
                                    String target = ds.child("tag").getValue().toString();

                                    int row = 0;
                                    target = target.replace("%", "#");
                                    StringTokenizer shop = new StringTokenizer(target, "#");
                                    row = shop.countTokens();

                                    //태그 담을 배열 생성
                                    String[] tag = new String[row];
                                    //태그의 가중치 배열 생성
                                    int[] weighcnt = new int[row];

                                    //같은 태그가 있는 지 없는지 체크
                                    boolean exist = false;

                                    //태그하나당 가중치가 1이며
                                    //중복된것이있으면 해당 태그 가중치를 1->2식으로 바꾼다
                                    for (int i = 0; i < row - minus; i++) {
                                        String insert = shop.nextToken();
                                        for (int j = 0; j < i; j++) {
                                            if (insert.equals(tag[j])) {
                                                weighcnt[j] += 1;
                                                exist = true;
                                            }
                                        }
                                        if (!exist) {
                                            tag[i] = insert;
                                            weighcnt[i] += 1;
                                        } else {
                                            exist = false;
                                            i--;
                                            minus++;
                                        }
                                    }

                                    //결과확인 페이지에 태그 배열을 전달
                                    PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("tag", target);
                                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                                    //태그 가중치 배열 전달
                                    intent.putExtra("tag[]", tag);
                                    intent.putExtra("weighcnt[]", weighcnt);
                                    intent.putExtra("row", row);
                                    intent.putExtra("minus",minus);
                                    finish();
                                    startActivity(intent);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

    }
    //입력으로 들어온 스트링 배열에서 중복을 제거하고 Object로 반환
    public Object[] removeDuplicateArray(String[] array) {
        Object[] removeArray = null;
        TreeSet ts = new TreeSet();
        for (int i = 0; i < array.length; i++) {
            ts.add(array[i]);
        }
        removeArray = ts.toArray();
        return removeArray;
    }

    //액션바의 뒤로가기 버튼 터치시 액티비티 finish
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
