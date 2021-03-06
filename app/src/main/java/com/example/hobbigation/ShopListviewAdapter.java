package com.example.hobbigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * 취미 정보 제공을 위하여 쇼핑 검색 결과를 리스트뷰로 보여주기 위한 Adapter
 */

public class ShopListviewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ShopListviewItem> listViewItemList = new ArrayList<ShopListviewItem>();

    // ListViewAdapter의 생성자
    public ShopListviewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shoplist_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView title = (TextView) convertView.findViewById(R.id.shoptitle);
        TextView price = (TextView) convertView.findViewById(R.id.shopprice);
        TextView mall = (TextView) convertView.findViewById(R.id.shopmall);
        ImageView urlimg = (ImageView) convertView.findViewById(R.id.shopthumnail);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ShopListviewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(listViewItem.getImgurl()).into(urlimg);
        title.setText(listViewItem.getTitle());
        price.setText(listViewItem.getPrice());
        mall.setText(listViewItem.getMall());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String title, String price, String mall, String url) {
        ShopListviewItem item = new ShopListviewItem();

        item.setUrl(url);
        item.setTitle(title);
        item.setPrice(price);
        item.setMall(mall);

        listViewItemList.add(item);
    }
}




