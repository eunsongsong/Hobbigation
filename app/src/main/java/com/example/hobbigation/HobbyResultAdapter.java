package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HobbyResultAdapter extends RecyclerView.Adapter<HobbyResultAdapter.ViewHolder> {
    Context context;
    List<HobbyResultInfo> items;
    int item_layout;

    public HobbyResultAdapter(Context context, List<HobbyResultInfo> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hobby_result_item, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HobbyResultInfo item = items.get(position);

        Log.d("asdasd",item.getHobby_name());
        holder.hobby_result_tv.setText(item.getHobby_name());


        Glide.with(holder.itemView.getContext())
                .load(item.getHobby_url())
                .into(holder.hobby_result_img);

        holder.hobby_result_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(),SubActivity.class);
                PreferenceUtil.getInstance(v.getContext()).putStringExtra("keyword",item.getHobby_name());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobby_result_tv;
        ImageView hobby_result_img;

        public ViewHolder(View itemView) {
            super(itemView);
            hobby_result_tv = (TextView) itemView.findViewById(R.id.hobby_result1_tv);
            hobby_result_img = (ImageView) itemView.findViewById(R.id.hobby_result1_img);
        }
    }
}
