package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * TabFragment1에서 카테고리 12개를 보여주는 adapter
 */
public class Main_CategoryAdapter extends RecyclerView.Adapter<Main_CategoryAdapter.ViewHolder>{
    Context context;
    List<Main_CategoryItem> items;
    int item_layout;
    Context mcontext;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("카테고리");
    String[] category = {"문화_공연","음악","예술","책_글","운동_스포츠","만들기","음식","게임_오락","아웃도어","식물","휴식","봉사활동"};

    public Main_CategoryAdapter(Context context, List<Main_CategoryItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_item,null);
        mcontext = parent.getContext();
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Main_CategoryItem item=items.get(position);

        //카테고리 아이템 이미지 설정
        holder.cate_img.setImageDrawable(item.getDrawable());

        //아이템 클릭시 실내,야외,참여,감상별로 해당 카테고리의 취미들을 가져와서
        //CategoryDetailActivity 로 전달하여 취미들을 보여준다.
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
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cate_img;

        public ViewHolder(View itemView) {
            super(itemView);
            cate_img = (ImageView) itemView.findViewById(R.id.cate_main_img);
        }
    }
}