package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<RecommnedInfo> items;
    int item_layout;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
         RecommnedInfo item=items.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.image_post);

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl_two())
                .into(holder.image_post_two);

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_post,image_post_two;


        public ViewHolder(View itemView) {
            super(itemView);
            image_post=(ImageView)itemView.findViewById(R.id.post_image);
            image_post_two= (ImageView) itemView.findViewById(R.id.post_image2);

        }
    }

}