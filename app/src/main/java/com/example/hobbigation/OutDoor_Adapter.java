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

public class OutDoor_Adapter extends RecyclerView.Adapter<OutDoor_Adapter.ViewHolder> {
    Context context;
    List<OutDoorInfo> items;
    int item_layout;

    public OutDoor_Adapter(Context context, List<OutDoorInfo> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outdoor_items, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OutDoorInfo item=items.get(position);
        holder.outdoor_tv.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.outdoor_img);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView outdoor_tv;
        ImageView outdoor_img;

        public ViewHolder(View itemView) {
            super(itemView);
            outdoor_tv = (TextView) itemView.findViewById(R.id.outdoor_tv);
            outdoor_img = (ImageView) itemView.findViewById(R.id.outdoor_img_view);
        }
    }
}
