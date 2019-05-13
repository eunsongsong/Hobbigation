package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class Part_Adapter extends RecyclerView.Adapter<Part_Adapter.ViewHolder> {
    Context context;
    List<PartInfo> items;
    int item_layout;

    public Part_Adapter(Context context, List<PartInfo> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_items, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PartInfo item=items.get(position);
        holder.part_tv.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.part_img);

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView part_tv;
        ImageView part_img;

        public ViewHolder(View itemView) {
            super(itemView);
            part_tv = (TextView) itemView.findViewById(R.id.part_tv);
            part_img = (ImageView) itemView.findViewById(R.id.part_img_view);
        }
    }
}
