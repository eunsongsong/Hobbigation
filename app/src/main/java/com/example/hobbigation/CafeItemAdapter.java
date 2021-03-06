package com.example.hobbigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 취미 정보 제공을 위하여 카페 검색 결과를 리사이클러뷰로 보여주기 위한 Adapter
 */

public class CafeItemAdapter extends RecyclerView.Adapter<com.example.hobbigation.CafeItemAdapter.ViewHolder> {
    Context context;
    List<CafeItemInfo> items;
    int item_layout;

    public CafeItemAdapter(Context context, List<CafeItemInfo> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public com.example.hobbigation.CafeItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafelist_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final com.example.hobbigation.CafeItemAdapter.ViewHolder holder, final int position) {
        final CafeItemInfo item = items.get(position);

        holder.cafetitle.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        holder.cafename.setText(item.getCafename());

        //포스트 제목을 누르면 해당 카페글로 이동
        holder.cafetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                context.startActivity(intent);
            }
        });

        //포스트 내용을 누르면 해당 카페글로 이동
        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                context.startActivity(intent);
            }
        });

        //카페이름을 누르면 카페 홈으로 이동
        holder.cafename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getCafeurl()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cafetitle;
        TextView desc;
        TextView cafename;

        public ViewHolder(View itemView) {
            super(itemView);
            cafetitle = (TextView) itemView.findViewById(R.id.cafetitle);
            desc = (TextView) itemView.findViewById(R.id.cafedesc);
            cafename = (TextView) itemView.findViewById(R.id.cafename);
        }
    }
}


