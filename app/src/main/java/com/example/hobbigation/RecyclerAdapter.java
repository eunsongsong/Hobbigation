package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<RecommnedInfo> items;
    int item_layout;
    FirebaseAuth firebaseAuth;

    String tag_sum = "";



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

        holder.post1.setOnCheckedChangeListener(null);
        holder.post1.setChecked(item.isSelected());

        holder.post2.setOnCheckedChangeListener(null);
        holder.post2.setChecked(item.isSelected_two());

            Glide.with(holder.itemView.getContext())
                    .load(item.getUrl())
                    .into(holder.image_post);

            Glide.with(holder.itemView.getContext())
                    .load(item.getUrl_two())
                    .into(holder.image_post_two);

        holder.post1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
                tag_sum += item.getTag() + "%";

                myRef_two.addValueEventListener(new ValueEventListener() {


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

        holder.post2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setCheked_two(isChecked);
                tag_sum += item.getTag_two() + "%";
                Log.d("TAG_SUM",tag_sum);
                myRef_two.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {

                            String target = ds.child("email").getValue().toString();
                            Log.d("dd",target);
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
;       CheckBox post1,post2;

        public ViewHolder(View itemView) {
            super(itemView);
            image_post=(ImageView)itemView.findViewById(R.id.post_image);
            image_post_two= (ImageView) itemView.findViewById(R.id.post_image2);
            post1 =(CheckBox) itemView.findViewById(R.id.image_check);
            post2 = ( CheckBox) itemView.findViewById(R.id.image_check2);

        }


    }

}