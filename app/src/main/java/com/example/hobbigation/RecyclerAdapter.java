package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 추천 이미지를 보여주는 RecyclerView Adpater
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<RecommnedInfo> items;
    int item_layout;
    FirebaseAuth firebaseAuth;

    String tag_sum = "";
    String deletestr = "";

    FirebaseDatabase database_two = FirebaseDatabase.getInstance();
    DatabaseReference myRef_two = database_two.getReference("사용자");

    public RecyclerAdapter(Context context, List<RecommnedInfo> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         final RecommnedInfo item=items.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        //이미지를 체크하고 각각 이미지 체크박스를 터치여부를 체크하여 반영
        //RecyclerView가  스크롤을 내리거나 올리면 계속해서 아이템을 재사용하기 때문에 터치여부를 item에 저장하여 동기화 시킨다.
        holder.post1.setOnCheckedChangeListener(null);
        holder.post1.setChecked(item.isSelected());

        holder.post2.setOnCheckedChangeListener(null);
        holder.post2.setChecked(item.isSelected_two());


        //각각 이미지를 Glide 라이브러리를 이용해 보여줌
            Glide.with(holder.itemView.getContext())
                    .load(item.getUrl())
                    .into(holder.image_post);

            Glide.with(holder.itemView.getContext())
                    .load(item.getUrl_two())
                    .into(holder.image_post_two);

        //왼쪽 체크박스에 대한 이벤트 처리
        //체크할때 마다 사용자의 태그가 변경
        holder.post1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
                //isChecked = true 일때 태그 추가
                if ( isChecked)
                {
                    tag_sum += item.getTag() + "%";
                }
                //isChecked = false 일때 태그 삭제
                else
                {
                    deletestr = item.getTag()+"%";
                    tag_sum = tag_sum.replace(deletestr, "");
                }

                //DB에 업데이트
                //사용자 태그에 업데이트
                //사용자 노드 예시 user001:rnjsdnfka
                //따라서 사용자의 이메일 정보를 받아  StringTokenizer를 이용하여 회원 정보 업데이트
                myRef_two.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String target = ds.child("email").getValue().toString();
                                if (mFirebaseUser != null) {
                                    if (target.equals(mFirebaseUser.getEmail())) {
                                        StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                        StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");

                                        myRef_two.child(st_two.nextToken() + ":" + st.nextToken()).child("tag").setValue(tag_sum);
                                    }
                                }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //오른쪽 체크박스에 대한 이벤트 처리
        //왼쪽 체크박스와 동일하다
        holder.post2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setCheked_two(isChecked);
                //isChecked = true 일때 태그 추가
                if ( isChecked)
                {
                    tag_sum += item.getTag_two() + "%";
                }
                //isChecked = false 일때 태그 삭제
                else
                {
                    deletestr = item.getTag_two()+"%";
                    tag_sum = tag_sum.replace(deletestr, "");
                }

                //DB에 업데이트
                myRef_two.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {

                            String target = ds.child("email").getValue().toString();

                            if (mFirebaseUser != null) {
                                if( target.equals(mFirebaseUser.getEmail()))
                                {
                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");

                                        myRef_two.child(st_two.nextToken()+":"+st.nextToken()).child("tag").setValue(tag_sum);
                                }
                            }
                        }
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
        ImageView image_post,image_post_two;
        final CheckBox post1,post2;

        public ViewHolder(View itemView) {
            super(itemView);
            image_post=(ImageView)itemView.findViewById(R.id.post_image);
            image_post_two= (ImageView) itemView.findViewById(R.id.post_image2);
            post1 = (CheckBox) itemView.findViewById(R.id.image_check);
            post2 = (CheckBox) itemView.findViewById(R.id.image_check2);
        }
    }

}