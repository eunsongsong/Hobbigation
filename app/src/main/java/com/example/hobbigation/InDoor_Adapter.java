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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * 취미 카테고리 중 실내 결과만 보여주는 adapter
 */

public class InDoor_Adapter extends RecyclerView.Adapter<InDoor_Adapter.ViewHolder> {
    Context context;
    List<InDoorInfo> items;
    int item_layout;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    public InDoor_Adapter(Context context, List<InDoorInfo> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.indoor_items, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final InDoorInfo item=items.get(position);

        //해당 카테고리에서 실내 취미인 이름
        holder.indoor_tv.setText(item.getName());

        //해당 카테고리에서 실내 취미 url
        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.indoor_img);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();


        holder.indoor_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(v.getContext(),SubActivity.class);


                //실내 취미 클릭시 찜한 취미인지 알기 위해 회원의 찜한 내용을 SharedPreference에 저장
                // 취미 이름을 SubActivity로 전달하기 위해 SharedPreference에 저장
               myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           String target = ds.child("email").getValue().toString();
                           if (mFirebaseUser != null) {
                               if (target.equals(mFirebaseUser.getEmail())) {
                                   PreferenceUtil.getInstance(v.getContext()).putStringExtra("like", ds.child("like").getValue().toString());
                                   PreferenceUtil.getInstance(v.getContext()).putStringExtra("keyword",item.getName());
                                   v.getContext().startActivity(intent);
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
        TextView indoor_tv;
        ImageView indoor_img;
        public ViewHolder(View itemView) {
            super(itemView);
            indoor_tv = (TextView) itemView.findViewById(R.id.indoor_tv);
            indoor_img = (ImageView) itemView.findViewById(R.id.indoor_img_view);
        }
    }
}
