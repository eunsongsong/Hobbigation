package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabFragment4 extends Fragment {

    private Button search_btn;
    String keyword = "";
    private TextView searchText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment4,container,false);
        searchText = (TextView) rootview.findViewById(R.id.searchText);
        
        search_btn = (Button) rootview.findViewById(R.id.searchBtn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final TextView searchResult = (TextView) findViewById(R.id.searchResult);
                keyword = searchText.getText().toString();

                Intent intent = new Intent(getActivity(), SubActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                //searchResult.setText(str);
            }
        });
        return rootview;
    }


}