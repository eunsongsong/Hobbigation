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
                            show_tag.setText(target);
                            Log.d("Target",target);
                        }
                    }
                    return;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
