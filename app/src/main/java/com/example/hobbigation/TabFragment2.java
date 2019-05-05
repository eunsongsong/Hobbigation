package com.example.hobbigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class TabFragment2 extends Fragment implements View.OnClickListener {

    public ImageButton art, book, collection, make, food, picture, game, shop, plant, music, study, volunteer,
            media, rest, concert, travel, outdoor, sports;
    public ImageView art_i, book_i, collection_i, make_i, food_i, picture_i, game_i, shop_i, plant_i, music_i, study_i, volunteer_i,
            media_i, rest_i, concert_i, travel_i, outdoor_i, sports_i;
    FragmentManager fm;
    FragmentTransaction tran;


    public static TabFragment2 newInstance() {
        return new TabFragment2();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment2, container, false);


        art = (ImageButton) rootview.findViewById(R.id.back_img_art1);
        book = (ImageButton) rootview.findViewById(R.id.back_img_book2);
        collection = (ImageButton) rootview.findViewById(R.id.back_img_collection3);
        make = (ImageButton) rootview.findViewById(R.id.back_img_make4);
        food = (ImageButton) rootview.findViewById(R.id.back_img_food5);
        picture = (ImageButton) rootview.findViewById(R.id.back_img_picture6);
        game = (ImageButton) rootview.findViewById(R.id.back_img_game7);
        shop = (ImageButton) rootview.findViewById(R.id.back_img_shop8);
        plant = (ImageButton) rootview.findViewById(R.id.back_img_plant9);
        music = (ImageButton) rootview.findViewById(R.id.back_img_music10);
        study = (ImageButton) rootview.findViewById(R.id.back_img_study11);
        volunteer = (ImageButton) rootview.findViewById(R.id.back_img_vol12);
        media = (ImageButton) rootview.findViewById(R.id.back_img_media13);
        rest = (ImageButton) rootview.findViewById(R.id.back_img_rest14);
        concert = (ImageButton) rootview.findViewById(R.id.back_img_concert15);
        travel = (ImageButton) rootview.findViewById(R.id.back_img_travel16);
        outdoor = (ImageButton) rootview.findViewById(R.id.back_img_outdoor17);
        sports = (ImageButton) rootview.findViewById(R.id.back_img_sport18);

        art_i = (ImageView) rootview.findViewById(R.id.art_img);
        book_i = (ImageView) rootview.findViewById(R.id.book_img);
        collection_i = (ImageView) rootview.findViewById(R.id.collection_img);
        make_i = (ImageView) rootview.findViewById(R.id.make_img);
        food_i = (ImageView) rootview.findViewById(R.id.food_img);
        picture_i = (ImageView) rootview.findViewById(R.id.picture_img);
        game_i = (ImageView) rootview.findViewById(R.id.game_img);
        shop_i = (ImageView) rootview.findViewById(R.id.show_img);
        plant_i = (ImageView) rootview.findViewById(R.id.plant_img);
        music_i = (ImageView) rootview.findViewById(R.id.music_img);
        study_i = (ImageView) rootview.findViewById(R.id.study_img);
        volunteer_i = (ImageView) rootview.findViewById(R.id.volunteer_img);
        media_i = (ImageView) rootview.findViewById(R.id.media_img);
        rest_i = (ImageView) rootview.findViewById(R.id.rest_img);
        concert_i = (ImageView) rootview.findViewById(R.id.concert_img);
        travel_i = (ImageView) rootview.findViewById(R.id.travel_img);
        outdoor_i = (ImageView) rootview.findViewById(R.id.outdoor_img);
        sports_i = (ImageView) rootview.findViewById(R.id.sports_img);

        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fart.png?alt=media&token=39173d1e-f6c5-452a-a4c9-5e9fe6eb1426")
                .into(art_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fbook.png?alt=media&token=fc137c8f-b065-4252-b251-032565a7af16")
                .into(book_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fcollecting.png?alt=media&token=82ea12cf-4d2a-4088-90eb-31096b99a7f6")
                .into(collection_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fknitting.png?alt=media&token=d0a5562b-b708-437f-b66f-2b7656b3445b")
                .into(make_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fburger.png?alt=media&token=d3227fd5-a3f1-42e7-8609-dbce63383d55")
                .into(food_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fpicture.png?alt=media&token=17502272-0560-44f5-883f-1416f598fe11")
                .into(picture_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fgame.png?alt=media&token=ceeed593-5fc5-47ba-940a-3273d8bd78db")
                .into(game_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fshopping.png?alt=media&token=bf5dde9f-e6b9-4f3d-a2a9-714e10e9681e")
                .into(shop_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fplant.png?alt=media&token=d5865b4c-eff3-4e64-9b20-3eed8ff42d46")
                .into(plant_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fmusic.png?alt=media&token=8346f971-387a-4336-a2df-0d664e7525d5")
                .into(music_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fstudy.png?alt=media&token=09c3402d-287d-494b-bc11-96374119630f")
                .into(study_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fvolunteer.png?alt=media&token=d04020b2-86bd-4c4b-8da3-abee3a9945fc")
                .into(volunteer_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fvideo.png?alt=media&token=38bf7cdb-76c1-4832-a7cb-2fb78692a537")
                .into(media_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fsleep.png?alt=media&token=313b58ad-076e-43c7-b211-5f2fb6878f26")
                .into(rest_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fshow.png?alt=media&token=ec12eab4-80ed-4501-8f1d-d84626ae75e5")
                .into(concert_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Ftrip.png?alt=media&token=26b0f27c-2bb5-4dca-9487-a4269d35e355")
                .into(travel_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Foutdoor.png?alt=media&token=bcfb910c-8dda-438e-9504-2c7e53fb8435")
                .into(outdoor_i);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/category%2Fsports.png?alt=media&token=ee293e7d-5452-4ba1-b0d9-5f9e81135e0d")
                .into(sports_i);

        art.setOnClickListener(this);
        book.setOnClickListener(this);
        collection.setOnClickListener(this);
        make.setOnClickListener(this);
        food.setOnClickListener(this);
        picture.setOnClickListener(this);
        shop.setOnClickListener(this);
        plant.setOnClickListener(this);
        music.setOnClickListener(this);
        study.setOnClickListener(this);
        volunteer.setOnClickListener(this);
        media.setOnClickListener(this);
        rest.setOnClickListener(this);
        concert.setOnClickListener(this);
        travel.setOnClickListener(this);
        outdoor.setOnClickListener(this);
        sports.setOnClickListener(this);
        Log.d("망할","11111");
        return rootview;
    }

    //public ImageView art_i,book_i,collection_i,make_i,food_i,p
    //icture_i,game_i,shop_i,plant_i,music_i,study_i,volunteer_i,
    //    media_i,rest_i,concert_i,travel_i,outdoor_i,sports_i;
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_img_art1:
                setFrag(0);
                break;
            case R.id.back_img_book2:
                setFrag(1);
                break;
        }

           /* case R.id.btn_tow:
                fg = ChildTowFragment.newInstance();
                setChildFragment(fg);
                break;
                */
    }


    public void setFrag(int n) {    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.container, new ArtDetailFragment());
                Log.d("망할","122221111");//replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.container, new BookDetailFragment());  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
        }


    }
}