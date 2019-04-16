package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
         RecommnedInfo item=items.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.image_post);

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl_two())
                .into(holder.image_post_two);

        holder.image_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_btn1.setEnabled(false);
            }
        });
        holder.image_btn1.setEnabled(true);
        holder.image_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_btn2.setEnabled(false);
            }
        });
        holder.image_btn2.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_post,image_post_two;
        Button image_btn1,image_btn2;

        public ViewHolder(View itemView) {
            super(itemView);
            image_post=(ImageView)itemView.findViewById(R.id.post_image);
            image_post_two= (ImageView) itemView.findViewById(R.id.post_image2);
            image_btn1 = (Button) itemView.findViewById(R.id.image_btn1);
            image_btn2 = (Button) itemView.findViewById(R.id.image_btn2);
        }
    }

}