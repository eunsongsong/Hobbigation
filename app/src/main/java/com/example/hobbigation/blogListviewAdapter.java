package com.example.hobbigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 취미 정보 제공을 위하여 블로그 검색 결과를 리스트뷰로 보여주기 위한 Adapter
 */

public class blogListviewAdapter extends BaseAdapter{

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<BlogListviewItem> listViewItemList = new ArrayList<BlogListviewItem>();

    //ListViewAdapter의 생성자
    public blogListviewAdapter() {

    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bloglist_item, parent, false);
        }

        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconimg = (ImageView) convertView.findViewById(R.id.blogthumnail);
        TextView titletxt = (TextView) convertView.findViewById(R.id.blogtitle);
        TextView desctxt = (TextView) convertView.findViewById(R.id.blogdesc);
        TextView bloger = (TextView) convertView.findViewById(R.id.bloger);
        TextView postdate = (TextView) convertView.findViewById(R.id.postdate);

        //Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BlogListviewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        iconimg.setImageDrawable(listViewItem.getIcon());
        titletxt.setText(listViewItem.getTitle());
        desctxt.setText(listViewItem.getDesc());
        bloger.setText(listViewItem.getBloger());
        postdate.setText(listViewItem.getDate());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(Drawable icon, String title, String desc, String bloger, String date) {
        BlogListviewItem item = new BlogListviewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);
        item.setBloger(bloger);
        item.setDate(date);

        listViewItemList.add(item);
    }
}


