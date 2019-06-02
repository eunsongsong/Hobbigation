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
        holder.related_txt.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.related_img);

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
