package com.example.hobbigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Top10 인기 취미를 자동스크롤 형태로 보여주는 Adapter
 */
public class AutoScrollAdapter extends PagerAdapter {

    Context context;
    List<AutoScroll_Info> item;
    //인기 취미 권유 문장들
    String[] top_tv_array = new String[]
            {"어떠신가요..?"
                    ,"해보셨나요?",
                    "안 해보셨죠?"
                    ,"나도 할 수 있을까?",
                    "재밌다고 소문이 자자해요~!!",
                    "꼭 해봐야죠!",
                    "다들 하나 봐요!",
                    "해보면 기분이 좋아질 거예요!"};

    public AutoScrollAdapter(Context context, List<AutoScroll_Info> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View page = inflater.inflate(R.layout.auto_viewpager,null);
        ImageView image_container = (ImageView) page.findViewById(R.id.image_container);

        //url으로 이미지를 보여줄 수 있는 Glide 라이브러리를 이용하여 취미 이미지를 보여준다
        Glide.with(context).load(item.get(position).getUrl()).into(image_container);
        TextView top_tv = (TextView)page.findViewById(R.id.top_tv);

        int ran = (int) (Math.random() * top_tv_array.length);
        //인기 취미이름을 권유메시지가 담긴 배열에서 랜덤으로 조합하여 보여준다.
        top_tv.setText("\'"+item.get(position).getName()+"\'" + "은(는) " + top_tv_array[ran] );

        container.addView(page);

        //인기취미 이미지 클릭 시 취미정보제공화면으로 이동
        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //취미이름을 SharedPreference에 저장하여 SubActivity에 전달한다.
                PreferenceUtil.getInstance(v.getContext()).putStringExtra("keyword",item.get(position).getName());
                Intent intent = new Intent(v.getContext(),SubActivity.class);
                context.startActivity(intent);
            }
        });

        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return this.item.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
