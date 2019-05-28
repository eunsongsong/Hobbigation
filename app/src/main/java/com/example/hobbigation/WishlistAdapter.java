package com.example.hobbigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    Context context;
    List<WishlistInfo> items;
    int item_layout;

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

        holder.wish_txt.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.wish_img);
        Log.d("url"+position,item.getUrl());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView wish_txt;
        ImageView wish_img;

        public ViewHolder(View itemView) {
            super(itemView);
            wish_txt = (TextView) itemView.findViewById(R.id.wishlist_txt);
            wish_img = (ImageView) itemView.findViewById(R.id.wishlist_img);
        }
    }
}
