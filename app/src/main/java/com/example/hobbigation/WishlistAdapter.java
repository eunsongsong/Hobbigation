package com.example.hobbigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.StringTokenizer;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    Context context;
    List<WishlistInfo> items;
    int item_layout;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    public WishlistAdapter(Context context, List<WishlistInfo> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WishlistInfo item = items.get(position);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        holder.wish_txt.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.wish_img);
        Log.d("url"+position,item.getUrl());

        //이미지 누르면 API정보 보여주기
        holder.wish_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(v.getContext(),SubActivity.class);
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

        //휴지통 버튼으로 찜 아이템 삭제
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("'"+item.getName()+"' 이(가) 찜 목록에서 삭제됩니다.");
                alert.setMessage("정말 찜을 취소하실 건가요?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());

                                firebaseAuth = FirebaseAuth.getInstance();
                                final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String target = ds.child("email").getValue().toString();
                                            if (mFirebaseUser != null) {
                                                if (target.equals(mFirebaseUser.getEmail())) {
                                                    StringTokenizer st = new StringTokenizer(mFirebaseUser.getEmail(), "@");
                                                    StringTokenizer st_two = new StringTokenizer(ds.getKey(), ":");
                                                    String like = PreferenceUtil.getInstance(v.getContext()).getStringExtra("like");
                                                    like = like.replace(item.getName()+"#", "");

                                                    myRef.child(st_two.nextToken() + ":" + st.nextToken()).child("like").setValue(like);
                                                    PreferenceUtil.getInstance(v.getContext()).putStringExtra("like", like);
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
                alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

            }

        });


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wish_txt;
        ImageView wish_img;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            wish_txt = (TextView) itemView.findViewById(R.id.wishlist_txt);
            wish_img = (ImageView) itemView.findViewById(R.id.wishlist_img);
            delete = (Button) itemView.findViewById(R.id.wishlist_delete);
        }
    }
}
