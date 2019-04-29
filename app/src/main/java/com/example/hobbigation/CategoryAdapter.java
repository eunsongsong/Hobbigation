package com.example.hobbigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    List<CategoryInfo> items;
    int item_layout;

    public CategoryAdapter(Context context, List<CategoryInfo> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryInfo item=items.get(position);

        holder.post1.setOnCheckedChangeListener(null);
        holder.post1.setChecked(item.isSelected());

        holder.post2.setOnCheckedChangeListener(null);
        holder.post2.setChecked(item.isSelected());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .into(holder.img1);

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl_two())
                .into(holder.img2);

        holder.txt1.setText(item.getName1());
        holder.txt2.setText(item.getName_two());

        holder.post1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);

            }
        });
        holder.post2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img1,img2;
        TextView txt1,txt2;
        CheckBox post1,post2;

        public ViewHolder(View itemView) {
            super(itemView);
            img1=(ImageView)itemView.findViewById(R.id.categ_img1);
            img2= (ImageView) itemView.findViewById(R.id.categ_img2);
            txt1 = (TextView) itemView.findViewById(R.id.categ_txt1);
            txt2 = (TextView) itemView.findViewById(R.id.categ_txt2);
            post1 =(CheckBox) itemView.findViewById(R.id.img_check);
            post2 = (CheckBox) itemView.findViewById(R.id.img_check2);

        }

    }

}