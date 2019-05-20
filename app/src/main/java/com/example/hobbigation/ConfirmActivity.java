package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

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
    private TextView show_tag;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();
        //스트링 배열 가중치 (정렬안됨)
        //행 갯수 하나로 맞추기


        tag_array = intent.getStringArrayExtra("tag[]");
        weighcnt = intent.getIntArrayExtra("weighcnt[]");
        row = intent.getIntExtra("row",0);

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

        myRef.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String con = ds.child("취미_태그").getValue().toString();

                    if ( con.contains(tag_array[0]) && con.contains(tag_array[1])){

                    }
                    else if (con.contains(tag_array[0]) && con.contains(tag_array[1]) && con.contains(tag_array[2]))
                    {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        show_tag = (TextView) findViewById(R.id.tag_result);

        show_tag.setText(PreferenceUtil.getInstance(getApplicationContext()).getStringExtra("tag"));


    }
}
