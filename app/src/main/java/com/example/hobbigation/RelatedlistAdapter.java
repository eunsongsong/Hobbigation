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

/**
 * 연관 취미를 보여주는 Adapter
 */
public class RelatedlistAdapter extends RecyclerView.Adapter<RelatedlistAdapter.ViewHolder> {
    Context context;
    List<RelatedlistInfo> items;
    int item_layout;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("사용자");

    public RelatedlistAdapter(Context context, List<RelatedlistInfo> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RelatedlistInfo item = items.get(position);
        //취미 이름
        holder.related_txt.setText(item.getName());

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

        //취미 이미지
        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.related_img);

        //이미지 누르면 API정보 보여주기기
        //SubActivity로 취미 이름 전달
       holder.related_img.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView related_txt;
        ImageView related_img;


        public ViewHolder(View itemView) {
            super(itemView);
            related_txt = (TextView) itemView.findViewById(R.id.relatedlist_txt);
            related_img = (ImageView) itemView.findViewById(R.id.relatedlist_img);
        }
    }
}
