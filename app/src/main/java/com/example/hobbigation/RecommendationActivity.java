package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        PreferenceUtil.getInstance(getApplicationContext()).putIntExtra("touch",touch);
        confirm_btn = (Button) findViewById(R.id.confirm);

        recyclerView = (RecyclerView) findViewById(R.id.myrecyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final List<RecommnedInfo> items = new ArrayList<>();
        String[] detail = new String[12];

        for (int i = 0; i < 12; i++) {
            detail[i] = PreferenceUtil.getInstance(this).getStringExtra("detail" + i);
            if (detail[i].startsWith(","))
                detail[i] = detail[i].substring(1);
            else if (detail[i].endsWith(","))
                detail[i] = detail[i].substring(0, detail[i].length() - 1);
            Log.d("----받은거", detail[i]);
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
        final Object game[] = removeDuplicateArray(dupgame);

        String[] dupoutdoor = detail[8].split(",");
        Object outdoor[] = removeDuplicateArray(dupoutdoor);

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
        for (int i = 0; i < 12; i++)
            Log.d("total[0]", total[0][i]);
        for (int i = 0; i < 12; i++)
            Log.d("total[1]", total[1][i]);
        for (int i = 0; i < 12; i++)
            Log.d("total[2]", total[2][i]);
        Arrays.sort(total[0]);
        Arrays.sort(total[1]);
        Arrays.sort(total[2]);
        for (int i = 0; i < 12; i++)
            Log.d("total[10]", total[0][i]);
        for (int i = 0; i < 12; i++)
            Log.d("total[11]", total[1][i]);
        for (int i = 0; i < 12; i++)
            Log.d("total[1\2]", total[2][i]);

        //이미지, 태그 가져오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                RecommnedInfo[] item = new RecommnedInfo[18];

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (int k = 0; k < 3; k++) {
                        for (int i = 0; i < 12; i++) {
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


                for (int i = 0; i < 18; i++) {

                    item[i] = new RecommnedInfo(st.nextToken(), st.nextToken(), st_two.nextToken(), st_two.nextToken());

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

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                firebaseAuth = FirebaseAuth.getInstance();
                 FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    if( mFirebaseUser.getEmail().equals(dataSnapshot.child("email").getValue().toString())) {
                        Log.d("아이들 벨류ㅜ", dataSnapshot.child("email").getValue().toString());
                        StringTokenizer st = new StringTokenizer(dataSnapshot.child("tag").getValue().toString(), "%");
                        int touch_cnt = st.countTokens();
                        if (touch_cnt > 4 )
                        {
                            confirm_btn.setEnabled(true);
                        }
                        else
                            confirm_btn.setEnabled(false);
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


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("무엇이무엇ㅇ;",dataSnapshot.getKey());
                            Log.d("12313무엇이무엇ㅇ;",dataSnapshot.getValue().toString());

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                if (mFirebaseUser.getEmail().equals(ds.child("email").getValue().toString())) {
                                    String target = ds.child("tag").getValue().toString();
                                    Log.d("c_Target", target);
                                    // "꼬치#먹방#음식#길거리음식#맛집%헬스#런닝머신#실내#운동#달리기#걷기#건강%"
                                    //# 4개 태그 5개 % 2개 #6개 태그 7개
                                    int row = 0;
                                    target = target.replace("%", "#");
                                    Log.d("r_Target", target);
                                    StringTokenizer shop = new StringTokenizer(target, "#");
                                    row = shop.countTokens();

                                    Log.d("row", row + "");
                                    String[] tag = new String[row];
                                    int[] weighcnt = new int[row];

                                    //같은 태그가 있는 지 없는지 체크
                                    boolean exist = false;

                                    for (int i = 0; i < row - minus; i++) {
                                        String insert = shop.nextToken();
                                        for (int j = 0; j < i; j++) {
                                            if (insert.equals(tag[j])) {
                                                weighcnt[j] += 1;
                                                Log.d("겹치는 태그 있을 때 tag[" + j + "]", tag[j]);
                                                Log.d("겹치는거 태그 있을 때 weighcnt[" + j + "]", weighcnt[j] + "");
                                                exist = true;
                                            }
                                        }
                                        if (!exist) {
                                            tag[i] = insert;
                                            Log.d("tag[" + i + "]", tag[i]);
                                            weighcnt[i] += 1;
                                            Log.d("weighcnt[" + i + "]", weighcnt[i] + "");
                                        } else {
                                            exist = false;
                                            i--;
                                            minus++;
                                        }
                                    }
                                    // show_tag.setText(target);
                                    //요것이 최종 태그다.
                                    Log.d("Target", target);

                                    PreferenceUtil.getInstance(getApplicationContext()).putStringExtra("tag", target);
                                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
                                    intent.putExtra("tag[]", tag);
                                    intent.putExtra("weighcnt[]", weighcnt);
                                    intent.putExtra("row", row);
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

    public Object[] removeDuplicateArray(String[] array) {
        Object[] removeArray = null;
        TreeSet ts = new TreeSet();
        for (int i = 0; i < array.length; i++) {
            ts.add(array[i]);
        }
        removeArray = ts.toArray();
        return removeArray;
    }


}
