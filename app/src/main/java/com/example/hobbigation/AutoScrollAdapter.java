package com.example.hobbigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AutoScrollAdapter extends PagerAdapter {

    Context context;
    List<AutoScroll_Info> item;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("카테고리");

    String[] top_tv_array = new String[]
            {"어떠신가요..?"
                    ,"해보셨나요?",
                    "안 해보셨죠?"
                    ,"나도 할 수 있을까?",
                    "재밌다고 소문이 자자해요~!!",
                    "꼭 해봐야죠!",
                    "다들 하나 봐요",
                    "해보면 기분이 좋아질껄요??"};

    public AutoScrollAdapter(Context context, List<AutoScroll_Info> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View page = inflater.inflate(R.layout.auto_viewpager,null);
        ImageView image_container = (ImageView) page.findViewById(R.id.image_container);
        Glide.with(context).load(item.get(position).getUrl()).into(image_container);
        TextView top_tv = (TextView)page.findViewById(R.id.top_tv);

        int ran = (int) (Math.random() * top_tv_array.length);
        top_tv.setText("\""+item.get(position).getName()+"\"" + "은(는) " + top_tv_array[ran] );

        container.addView(page);

        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "This page was clicked: " + position + "하나 더 "+ item.get(position).getName());
                PreferenceUtil.getInstance(v.getContext()).putStringExtra("keyword",item.get(position).getName());
                Intent intent = new Intent(v.getContext(),SubActivity.class);
                context.startActivity(intent);
            }
        });
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                myRef.child(category[position]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] hobbies = new String[4];
                        hobbies[0] =  dataSnapshot.child("실내_야외").child("실내").getValue().toString();
                        hobbies[1] =  dataSnapshot.child("실내_야외").child("야외").getValue().toString();
                        hobbies[2] =  dataSnapshot.child("참여_감상").child("참여").getValue().toString();
                        hobbies[3] =  dataSnapshot.child("참여_감상").child("감상").getValue().toString();

                        Intent intent = new Intent(mcontext, CategoryDetailActivity.class);
                        intent.putExtra("category", category[position]);
                        intent.putExtra("실내",hobbies[0]);
                        intent.putExtra("야외",hobbies[1]);
                        intent.putExtra("참여",hobbies[2]);
                        intent.putExtra("감상",hobbies[3]);

                        mcontext.startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        */

        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
