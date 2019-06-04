package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TabFragment4 extends Fragment {

    public RecyclerView wishlist_recycleview;
    public RecyclerView relatedlist_recycleview;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미").child("이미지_태그");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference myRef_two = database2.getReference("취미").child("카테고리");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_tab_fragment4,container,false);

        wishlist_recycleview = (RecyclerView) rootview.findViewById(R.id.wishlist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        wishlist_recycleview.setHasFixedSize(true);
        wishlist_recycleview.setLayoutManager(layoutManager);

        relatedlist_recycleview = (RecyclerView) rootview.findViewById(R.id.related_hobby);

        final LinearLayoutManager layoutManager_related=new LinearLayoutManager(getContext());
        layoutManager_related.setOrientation(LinearLayout.HORIZONTAL);
        relatedlist_recycleview.setHasFixedSize(true);
        relatedlist_recycleview.setLayoutManager(layoutManager_related);

        final String like = PreferenceUtil.getInstance(getContext()).getStringExtra("like");
        boolean is_empty_like;
        if (TextUtils.isEmpty(like))
        {
            is_empty_like = true;
        }
        else
            is_empty_like = false;
        Log.d("like 0530", like);
        final String[] like_array = like.split("#");
        for ( int i = 0 ; i < like_array.length ; i++)
            Log.d("ddd",like_array[i]);

        Arrays.sort(like_array);
        Log.d("cnt ",like_array.length+"");
        for ( int i = 0 ; i < like_array.length ; i++) {
            if(like_array[i].contains("일기"))
                Log.d("ddd", like_array[i]);
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WishlistInfo> wish_items =new ArrayList<>();
                WishlistInfo[] item = new WishlistInfo[like_array.length];

                int a = 0;

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if (a > like_array.length - 1){
                        break;
                    }

                    if (like_array[a].equals(ds.getKey())) {
                        item[a] = new WishlistInfo(like_array[a], ds.child("url_태그").child("0").child("url").getValue().toString());
                        wish_items.add(item[a]);
                        a++;
                    }
                }
                WishlistAdapter wishlistAdapter = new WishlistAdapter(getContext(),wish_items,R.layout.fragment_tab_fragment4);
                wishlist_recycleview.setAdapter(wishlistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final int[][] related = new int[2][2];
        final int[] category_num = new int[12];
        final String[] category_name = new String[]{"게임_오락","만들기","문화_공연","봉사활동","식물"
                ,"아웃도어","예술","운동_스포츠","음식","음악","책_글","휴식"};

        //게임_오락
        //만들기
        //문화_공연
        //봉사활동
        //식물
        //아웃도어
        //예술
        //운동_스포츠
        //음식
        //음악
        //책_글
        //휴식

        if (!is_empty_like) {
            myRef_two.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int a = 0;
                    //3개씩 나오는 곳
                    boolean ischecked = false;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("카테고리", ds.getKey() + " 번호 : " + a);
                        for (int i = 0; i < like_array.length; i++) {
                            if (ds.child("실내_야외").child("실내").getValue().toString().contains(like_array[i])) {
                                related[0][0] += 1;
                                category_num[a] += 1;
                                ischecked = true;
                            }
                            if (ds.child("실내_야외").child("야외").getValue().toString().contains(like_array[i])) {
                                related[0][1] += 1;
                                if (!ischecked)
                                    category_num[a] += 1;
                            }
                            if (ds.child("참여_감상").child("감상").getValue().toString().contains(like_array[i])) {
                                related[1][0] += 1;
                            }
                            if (ds.child("참여_감상").child("참여").getValue().toString().contains(like_array[i])) {
                                related[1][1] += 1;
                            }
                            ischecked = false;
                        }
                        a++;
                    }
                    for (int i = 0; i < 12; i++) {
                        Log.d(i + "번", category_num[i] + "");
                    }
                    Log.d("0 0", related[0][0]+"");
                    Log.d("0 1", related[0][1]+"");
                    Log.d("1 0", related[1][0]+"");
                    Log.d("1 1", related[1][1]+"");
                    String result = "";
                    String result_two = "";

                    if (related[0][0] > related[0][1]) {
                        result = "실내";
                    } else if (related[0][0] < related[0][1]) {
                        result = "야외";
                    } else { //0과 1만 출력
                        int p = (int) (Math.random() * 2);
                        if( p == 0 )
                            result = "실내";
                        else
                            result = "야외";
                    }

                    if (related[1][0] > related[1][1]) {
                        result_two = "감상";
                    } else if (related[1][0] < related[1][1]) {
                        result_two = "참여";
                    } else {
                        int p = (int) (Math.random() * 2);
                        if( p == 0 )
                            result_two = "감상";
                        else
                            result_two = "참여";
                    }
                    Log.d("결과물", result + "ddd" + result_two);
                    // 여기까지 카테고리 가중치까지 다 입력됨
                    final String finalResult = result;
                    final String finalResult_two = result_two;

                    myRef_two.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int a = 0;
                            int b = 0;
                            int c = 0;

                            final String[] hobby_one = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];
                            final String[] hobby_two = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];
                            final String[] hobby_three = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];

                            String hobby_name = "";
                            boolean has_in_like = false;

                            //max값을 찾아라
                            int category_cnt = 1;
                            int max = category_num[0];
                            int index = 0;
                            for (int q = 1; q < 12; q++) {
                                if (max < category_num[q]) {
                                    max = category_num[q];
                                    index = q;
                                }
                            }
                            // 찜이 하나 있는 경우에만 함으로 max != 0 안함
                            Log.d("dddd", category_name[index] + max);

                            category_num[index] = 0;
                            max = category_num[0];
                            int index_2 = 0;
                            for (int q = 1; q < 12; q++) {
                                if (max < category_num[q]) {
                                    max = category_num[q];
                                    index_2 = q;
                                }
                            }

                            if( max == 0 )
                                Log.d("카테고리 하나", "ㅇㅇㅇㅇㅇ");
                            int index_3 = 0;
                            if (max != 0) { //카테고리가 두개 인 경우
                                category_cnt++;
                                category_num[index_2] = 0;
                                max = category_num[0];
                                for (int q = 1; q < 12; q++) {
                                    if (max < category_num[q]) {
                                        max = category_num[q];
                                        index_3 = q;
                                    }
                                }
                            }
                            if (max != 0) {
                                category_cnt++;
                                Log.d("세개","카테고리 세개");
                                Log.d("2222",category_name[index_2] + " " + max);
                                Log.d("3333",category_name[index_3] + " " + max);
                            }
                            if (max == 0 && index_3 == 0){
                                Log.d("두개","두개두개");
                                Log.d("ddd",category_name[index_2] + " " + max);
                            }

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //첫번째 카테고리이며 실내 야외  참여 감상 중 각각 하나씩 선택하여 취미  hobby_one 에 담음
                                if ( ds.getKey().equals(category_name[index])) {
                                    for (int k = 0; k < (int) ds.child("실내_야외").child(finalResult).getChildrenCount(); k++) {
                                        hobby_name = ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString();
                                        if (hobby_name.equals(""))
                                            continue;
                                        for (int t = 0; t < like_array.length; t++) {
                                            if (like_array[t].equals(hobby_name)) {
                                                has_in_like = true;
                                            }
                                        }
                                        if (has_in_like) { // 내 취미와 같은거 안찾기위한 중복제거
                                            has_in_like = false;
                                            continue;
                                        }
                                        if (ds.child("참여_감상").child(finalResult_two).getValue().toString().contains(hobby_name)) {
                                            Log.d(category_name[index], a + " " + ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString());
                                            hobby_one[a] = hobby_name;
                                            a++;
                                        }
                                    }
                                }

                                if (category_cnt >= 2)
                                {
                                    if( ds.getKey().equals(category_name[index_2]))
                                    {
                                        for (int k =0; k < (int) ds.child("실내_야외").child(finalResult).getChildrenCount(); k++) {
                                            hobby_name = ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString();
                                            if (hobby_name.equals(""))
                                                continue;
                                            for (int t = 0; t < like_array.length; t++) {
                                                if (like_array[t].equals(hobby_name)) {
                                                    has_in_like = true;
                                                }
                                            }
                                            if (has_in_like) {
                                                has_in_like = false;
                                                continue;
                                            }
                                            if (ds.child("참여_감상").child(finalResult_two).getValue().toString().contains(hobby_name)) {
                                                Log.d(category_name[index_2], b + " " + ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString());
                                                hobby_two[b] = hobby_name;
                                                b++;
                                            }
                                        }
                                    }
                                }
                                if ( category_cnt == 3)
                                {
                                    if (ds.getKey().equals(category_name[index_3]))
                                    {
                                        for (int k = 0; k < (int) ds.child("실내_야외").child(finalResult).getChildrenCount(); k++) {
                                            hobby_name = ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString();
                                            if (hobby_name.equals(""))
                                                continue;
                                            for (int t = 0; t < like_array.length; t++) {
                                                if (like_array[t].equals(hobby_name)) {
                                                    has_in_like = true;
                                                }
                                            }
                                            if (has_in_like) {
                                                has_in_like = false;
                                                continue;
                                            }
                                            if (ds.child("참여_감상").child(finalResult_two).getValue().toString().contains(hobby_name)) {
                                                Log.d(category_name[index_3], c + " " + ds.child("실내_야외").child(finalResult).child(String.valueOf(k)).getValue().toString());
                                                hobby_three[c] = hobby_name;
                                                c++;
                                            }
                                        }
                                    }
                                }
                            }
                            Log.d("갯수갯수카테 1"+ category_name[index], a + "");
                            Log.d("갯수갯수카테 2"+ category_name[index_2], b + "");
                            Log.d("갯수갯수카테 3"+ category_name[index_3], c + "");
                            Arrays.sort(hobby_one, 0, a);
                            Arrays.sort(hobby_two,0, b);
                            Arrays.sort(hobby_three,0,c);

                            for ( int q = 0 ; q < a ; q++){
                                Log.d(category_name[index], hobby_one[q]);
                            }
                            for ( int q = 0 ; q < b ; q++){
                                Log.d(category_name[index_2], hobby_two[q]);
                            }
                            for ( int q = 0 ; q < c ; q++){
                                Log.d(category_name[index_3], hobby_three[q]);
                            }
                            final int hobby_sum = a + b + c;
                            //5개 이하 일때 처리
                            Log.d("취미합 ", hobby_sum+"");
                            System.arraycopy(hobby_two, 0, hobby_one, a, b);
                            System.arraycopy(hobby_three, 0, hobby_one, a + b, c);

                            //  if(hobby_sum <= 5) {
                            for (int i = 0; i < hobby_sum; i++) {
                                Log.d("하비이름 총합" + ";" + i, hobby_one[i]);
                            }
                            Arrays.sort(hobby_one, 0, hobby_sum);
                            for (int i = 0; i < hobby_sum; i++) {
                                Log.d("정렬 이후 하비이름 총합" + ";" + i, hobby_one[i]);
                            }
                            //   }
                            String[] result = new String[5];
                            int count = 0 ;
                            boolean ran_check = false;
                            if ( category_cnt == 1)
                            {
                                if ( a <= 5 )
                                    for ( int i = 0 ; i < 5 ; i++)
                                    {
                                        if ( !TextUtils.isEmpty(hobby_one[i])) {
                                            result[i] = hobby_one[i];
                                            count++;
                                        }
                                    }
                                else
                                {
                                    for (int i = 0; i < 5 ; i++)
                                    {
                                        int ran = (int) (Math.random() * a);
                                        for ( int j = 0; j < i ; j++)
                                        {
                                            if ( result[j].equals(hobby_one[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check) {
                                            result[i] = hobby_one[ran];
                                            count++;
                                        }
                                        ran_check = false;
                                    }
                                }
                            }
                            else if (category_cnt == 2)
                            {
                                if ( a + b <= 5 )
                                {
                                    System.arraycopy(hobby_one,0,result,0,a);

                                    System.arraycopy(hobby_two,0,result,a,b);
                                    count = a + b;
                                }
                                else if ( a < 3 )
                                {
                                    System.arraycopy(hobby_one, 0, result,0 , a);

                                    for( int i = a ; i < 5 ; i++){
                                        int ran = (int) (Math.random() * b);

                                        for ( int j = a ; j < i ; j++){
                                            if (result[j].equals(hobby_two[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check) {
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if ( b < 2 ){
                                    System.arraycopy(hobby_two, 0, result, 0, b);

                                    for( int i = b ; i < 5 ; i++){
                                        int ran = (int) (Math.random() * a);

                                        for( int j = b ; j < i ; j++){
                                            if (result[j].equals(hobby_one[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else
                                {
                                    for( int i = 0 ; i < 3 ; i++){
                                        int ran = (int) (Math.random() * a);

                                        for( int j = 0 ; j < i ; j++){
                                            if (result[j].equals(hobby_one[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for( int i = 3 ; i < 5 ; i++){
                                        int ran = (int) (Math.random() * b);

                                        for( int j = 3 ; j < i ; j++){
                                            if (result[j].equals(hobby_two[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check){
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                            }
                            else //카테 고리 3개 인경우
                            {
                                // a + b + c 가 5가 안되는 경우
                                if ( hobby_sum <= 5)
                                {
                                    System.arraycopy(hobby_one,0,result,0,a);
                                    System.arraycopy(hobby_two,0,result,a,b);
                                    System.arraycopy(hobby_three,0,result, a + b, c);
                                    count = hobby_sum;
                                }
                                else if ( a < 2 && b < 2 )  // c가 큰 경우 입니다
                                {
                                    System.arraycopy(hobby_one, 0 , result, 0, a);
                                    System.arraycopy(hobby_two, 0, result, a , b);
                                    for ( int i = a + b ; i < 5 ; i++) {
                                        int ran = (int) (Math.random() * c);
                                        for (int j = a + b; j < i; j++) {
                                            if (result[j].equals(hobby_three[ran])) {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check) {
                                            result[i] = hobby_three[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if( a < 2 && c < 3)
                                {
                                    System.arraycopy(hobby_one, 0 , result, 0, a);
                                    System.arraycopy(hobby_three, 0, result, a, c);
                                    for ( int i = a + c ; i < 5; i++ )
                                    {
                                        int ran = (int) (Math.random() * b);
                                        for ( int j = a + c ; j < i ; j++)
                                        {
                                            if (result[j].equals(hobby_two[ran])){
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if ( b < 2 && c < 3)
                                {
                                    System.arraycopy(hobby_two, 0 , result, 0, b);
                                    System.arraycopy(hobby_three, 0, result, b, c);
                                    for ( int i = b + c ; i < 5; i++ )
                                    {
                                        int ran = (int) (Math.random() * a);
                                        for ( int j = b + c ; j < i ; j++)
                                        {
                                            if (result[j].equals(hobby_one[ran])){
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if( a == 0 && c == 3)
                                {
                                    for ( int i = 0 ; i < 3 ; i++)
                                    {
                                        int ran = (int) (Math.random() * b);
                                        for (int j = 0 ; j < i ; j++){
                                            if ( result[j].equals(hobby_two[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for( int i = 3; i < 5 ; i++){
                                        int ran = (int) (Math.random() * c);
                                        for (int j = 3; j < i ; j++)
                                        {
                                            if ( result[j].equals(hobby_three[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if( a == 1 && c == 3)
                                {
                                    System.arraycopy(hobby_one, 0, result,0,a);
                                    for ( int i = a ; i < a + 2 ; i++)
                                    {
                                        int ran = (int) (Math.random() * b);
                                        for (int j = a ; j < i ; j++){
                                            if ( result[j].equals(hobby_two[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for( int i = 3; i < 5 ; i++){
                                        int ran = (int) (Math.random() * c);
                                        for (int j = 3; j < i ; j++)
                                        {
                                            if ( result[j].equals(hobby_three[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if( b == 0 && c == 3)
                                { // a 3개 이상 보장
                                    for ( int i = 0 ; i < 3 ; i++)
                                    {
                                        int ran = (int) (Math.random() * a);
                                        for (int j = 0 ; j < i ; j++){
                                            if ( result[j].equals(hobby_one[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for( int i = 3; i < 5 ; i++){
                                        int ran = (int) (Math.random() * c);
                                        for (int j = 3; j < i ; j++)
                                        {
                                            if ( result[j].equals(hobby_three[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_three[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if (b == 1 && c == 3)
                                {
                                    System.arraycopy(hobby_two, 0, result,0,b);
                                    for ( int i = b ; i < b + 2 ; i++)
                                    {
                                        int ran = (int) (Math.random() * a);
                                        for (int j = b ; j < i ; j++){
                                            if ( result[j].equals(hobby_one[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for( int i = 3; i < 5 ; i++){
                                        int ran = (int) (Math.random() * c);
                                        for (int j = 3; j < i ; j++)
                                        {
                                            if ( result[j].equals(hobby_three[ran]))
                                            {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if( !ran_check){
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    count = 5;
                                }
                                else if( c < 1)
                                {
                                    if ( a < 3 ) {
                                        System.arraycopy(hobby_one, 0, result, 0, a);
                                        for ( int i = a ; i < 5; i++)
                                        {
                                            int ran = (int) (Math.random() * b);
                                            for (int j = b ; j < i; j++) {
                                                if (result[j].equals(hobby_two[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if ( !ran_check) {
                                                result[i] = hobby_two[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                    else
                                    {
                                        for ( int i = 0 ; i < 3; i++)
                                        {
                                            int ran = (int) (Math.random() * a);
                                            for (int j = 0 ; j < i; j++) {
                                                if (result[j].equals(hobby_one[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if ( !ran_check) {
                                                result[i] = hobby_one[ran];
                                            }
                                            ran_check = false;
                                        }
                                        for ( int i = 3 ; i < 5; i++)
                                        {
                                            int ran = (int) (Math.random() * b);
                                            for (int j = 3 ; j < i; j++) {
                                                if (result[j].equals(hobby_two[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if ( !ran_check) {
                                                result[i] = hobby_two[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                }
                                else
                                {
                                    for ( int i = 0 ; i < 2; i++)
                                    {
                                        int ran = (int) (Math.random() * a);
                                        for (int j = 0 ; j < i; j++) {
                                            if (result[j].equals(hobby_one[ran])) {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check) {
                                            result[i] = hobby_one[ran];
                                        }
                                        ran_check = false;
                                    }
                                    for ( int i = 2 ; i < 4; i++)
                                    {
                                        int ran = (int) (Math.random() * b);
                                        for (int j = 2 ; j < i; j++) {
                                            if (result[j].equals(hobby_two[ran])) {
                                                i--;
                                                ran_check = true;
                                                break;
                                            }
                                        }
                                        if ( !ran_check) {
                                            result[i] = hobby_two[ran];
                                        }
                                        ran_check = false;
                                    }
                                    int ran = (int) (Math.random() * c);
                                    System.arraycopy(hobby_three, ran, result, 4,1);
                                    count = 5;
                                }
                            }

                            for(int i = 0 ; i < count ; i++){
                                Log.d(" 새벽 네시", result[i]+ "    인덱스 : "+i);
                            }
                            Arrays.sort(result,0, count);
                            for(int i = 0 ; i < count ; i++){
                                Log.d(" 정렬 결과 새벽 네시", result[i]+ "    인덱스 : "+i);
                            }
                            final String[] final_related_hobby = new String[count];
                            final int final_count = count;
                            System.arraycopy(result, 0, final_related_hobby, 0 , count);
                            Log.d("파이널", final_count+"");
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    List<RelatedlistInfo> relatedlistInfo_items =new ArrayList<>();
                                    RelatedlistInfo[] item = new RelatedlistInfo[5];
                                    Log.d("여기","1111");
                                    if(final_related_hobby.length == 0) //아무 취미가 없는경우
                                        return;
                                    Log.d("여기","1123123111");
                                    int k = 0;

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if ( k > final_count - 1)
                                            break;
                                        if (ds.getKey().equals(final_related_hobby[k])) {
                                            item[k] = new RelatedlistInfo(final_related_hobby[k],ds.child("url_태그").child("1").child("url").getValue().toString());
                                            relatedlistInfo_items.add(item[k]);
                                            k++;
                                        }
                                    }
                                    RelatedlistAdapter relatedlistAdapter = new RelatedlistAdapter(getContext(),relatedlistInfo_items,R.layout.fragment_tab_fragment4);
                                    relatedlist_recycleview.setAdapter(relatedlistAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return rootview;
    }

}