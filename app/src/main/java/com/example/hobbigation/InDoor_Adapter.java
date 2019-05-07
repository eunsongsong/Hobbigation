package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class InDoor_Adapter extends RecyclerView.Adapter<InDoor_Adapter.ViewHolder> {
    Context context;
    List<InDoorInfo> items;
    int item_layout;

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
        holder.indoor_tv.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView indoor_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            indoor_tv = (TextView) itemView.findViewById(R.id.indoor_tv);
        }
    }
}
