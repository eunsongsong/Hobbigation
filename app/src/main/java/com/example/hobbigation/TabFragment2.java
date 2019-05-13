package com.example.hobbigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TabFragment2 extends Fragment implements View.OnClickListener {

    public ImageButton culture,music,art,book,sports,make,food
            ,game,outdoor,plant,rest,volunteer;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("카테고리");

    public static TabFragment2 newInstance() {
        return new TabFragment2();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment2, container, false);

        culture = (ImageButton)rootview.findViewById(R.id.back_img_culture1);
        music = (ImageButton)rootview.findViewById(R.id.back_img_music2);
        art = (ImageButton) rootview.findViewById(R.id.back_img_art3);
        book = (ImageButton) rootview.findViewById(R.id.back_img_book4);
        sports = (ImageButton) rootview.findViewById(R.id.back_img_sports5);
        make = (ImageButton) rootview.findViewById(R.id.back_img_make6);
        food = (ImageButton) rootview.findViewById(R.id.back_img_food7);
        game = (ImageButton) rootview.findViewById(R.id.back_img_game8);
        outdoor = (ImageButton) rootview.findViewById(R.id.back_img_outdoor9);
        plant = (ImageButton) rootview.findViewById(R.id.back_img_plant10);
        rest = (ImageButton) rootview.findViewById(R.id.back_img_rest11);
        volunteer = (ImageButton) rootview.findViewById(R.id.back_img_vol12);

        culture.setOnClickListener(this);
        music.setOnClickListener(this);
        art.setOnClickListener(this);
        book.setOnClickListener(this);
        sports.setOnClickListener(this);
        make.setOnClickListener(this);
        food.setOnClickListener(this);
        game.setOnClickListener(this);
        outdoor.setOnClickListener(this);
        plant.setOnClickListener(this);
        rest.setOnClickListener(this);
        volunteer.setOnClickListener(this);

        return rootview;
    }
    //카테고리 선택 함수 중복 코드 방지 myRef.child(category)
    public void categorySelect(final String category)
    {
        myRef.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] hobbies = new String[4];

                hobbies[0] =  dataSnapshot.child("실내_야외").child("실내").getValue().toString();
                hobbies[1] =  dataSnapshot.child("실내_야외").child("야외").getValue().toString();
                hobbies[2] =  dataSnapshot.child("참여_감상").child("참여").getValue().toString();
                hobbies[3] =  dataSnapshot.child("참여_감상").child("감상").getValue().toString();

                Intent intent = new Intent(getContext(), CategoryDetailActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("실내",hobbies[0]);
                intent.putExtra("야외",hobbies[1]);
                intent.putExtra("참여",hobbies[2]);
                intent.putExtra("감상",hobbies[3]);
                startActivity(intent);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.back_img_culture1:
                categorySelect("문화_공연");
                break;
            case R.id.back_img_music2:
                categorySelect("음악");
                break;
            case R.id.back_img_art3:
                categorySelect("예술");
                break;
            case R.id.back_img_book4:
                categorySelect("책_글");
                break;
            case R.id.back_img_sports5:
                categorySelect("운동_스포츠");
                break;
            case R.id.back_img_make6:
                categorySelect("만들기");
                break;
            case R.id.back_img_food7:
                categorySelect("음식");
                break;
            case R.id.back_img_game8:
                categorySelect("게임_오락");
                break;
            case R.id.back_img_outdoor9:
                categorySelect("아웃도어");
                break;
            case R.id.back_img_plant10:
                categorySelect("식물");
                break;
            case R.id.back_img_rest11:
                categorySelect("휴식");
                break;
            case R.id.back_img_vol12:
                categorySelect("봉사활등");
                break;

        }

    }
}
