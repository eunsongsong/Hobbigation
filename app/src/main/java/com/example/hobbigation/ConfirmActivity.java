package com.example.hobbigation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class ConfirmActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;

    String userID = "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");
    private TextView show_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        show_tag = (TextView) findViewById(R.id.tag_result);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(),"@");
        userID = st.nextToken();
        Log.d("userId",userID);
        if (mFirebaseUser != null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        StringTokenizer st = new StringTokenizer(ds.getKey(),":");
                        String key = "";
                        key = st.nextToken();
                        key = st.nextToken();
                        if(userID.equals(key))
                        {
                            String target = ds.child("tag").getValue().toString();
                            Log.d("c_Target",target);
                            // "꼬치#먹방#음식#길거리음식#맛집%헬스#런닝머신#실내#운동#달리기#걷기#건강%"
                            //# 4개 태그 5개 % 2개 #6개 태그 7개
                            int row = 0;
                            target = target.replace("%","#");
                            Log.d("r_Target",target);
                            StringTokenizer shop = new StringTokenizer(target, "#");
                            row = shop.countTokens();

                            Log.d("row",row+"");
                            String[] tag = new String[row];
                            int[] weighcnt = new int[row];

                            boolean exist = false;
                            int minus = 0;
                            for (int i = 0 ; i < row - minus ; i++)
                            {
                                String insert = shop.nextToken();
                                for ( int j = 0 ; j < i ; j++)
                                {
                                    if (insert.equals(tag[j]))
                                    {
                                        weighcnt[j] += 1;
                                        Log.d("겹치는 태그 있을 때 tag["+j+"]", tag[j]);
                                        Log.d("겹치는거 태그 있을 때 weighcnt["+j+"]", weighcnt[j]+"");
                                        exist = true;
                                    }
                                }
                                if ( !exist) {
                                    tag[i] = insert;
                                    Log.d("tag[" + i + "]", tag[i]);
                                    weighcnt[i] += 1;
                                    Log.d("weighcnt[" + i + "]", weighcnt[i]+"");
                                }
                                else
                                {
                                    exist = false;
                                    i--;
                                    minus++;
                                }
                            }
                            show_tag.setText(target);
                            Log.d("Target",target);
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
