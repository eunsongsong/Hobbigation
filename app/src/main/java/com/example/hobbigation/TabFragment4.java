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
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Tab4 - 찜 목록 및 연관된 취미
 * 유저의 찜한 취미 목록과 찜 목록에 기반한 연관 취미가 보여진다.
 * 연관 취미는 찜한 취미들의 카테고리, 필터에 가중치를 매겨 랜덤으로 5개 보여짐
 */
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

        // 찜 목록 리사이클러뷰 설정
        wishlist_recycleview = (RecyclerView) rootview.findViewById(R.id.wishlist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.HORIZONTAL);
        wishlist_recycleview.setHasFixedSize(true);
        wishlist_recycleview.setLayoutManager(layoutManager);

        // 연관 취미 리사이클러뷰 설정
        relatedlist_recycleview = (RecyclerView) rootview.findViewById(R.id.related_hobby);
        final LinearLayoutManager layoutManager_related=new LinearLayoutManager(getContext());
        layoutManager_related.setOrientation(LinearLayout.HORIZONTAL);
        relatedlist_recycleview.setHasFixedSize(true);
        relatedlist_recycleview.setLayoutManager(layoutManager_related);

        final String like = PreferenceUtil.getInstance(getContext()).getStringExtra("like");
        boolean is_empty_like; // 찜 목록이 비어있는지 확인
        if (TextUtils.isEmpty(like))
            is_empty_like = true;
        else
            is_empty_like = false;
        //유저의 찜 목록을 배열에 저장
        final String[] like_array = like.split("#");
        //찜 목록 가나다순 정렬
        Arrays.sort(like_array);

        /* 유저의 찜한 취미 목록 */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WishlistInfo> wish_items =new ArrayList<>();
                WishlistInfo[] item = new WishlistInfo[like_array.length];

                int a = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //찜 목록을 모두 찾으면 중지 (찜 목록 길이를 벗어남)
                    if (a > like_array.length - 1){
                        break;
                    }

                    //찜한 취미와 DB의 취미가 같으면 그 취미의 url 가져오기
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

        /* 연관된 취미 */
        final int[][] related = new int[2][2];
        final int[] category_num = new int[12];
        final String[] category_name = new String[]{"게임_오락","만들기","문화_공연","봉사활동","식물"
                ,"아웃도어","예술","운동_스포츠","음식","음악","책_글","휴식"};

        if (!is_empty_like) {
            //찜한 취미들의 실내 야외 참여 감상으로 나눠 체크한다.
            //카테고리별로 나온 개수를 체크한다.
            myRef_two.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int a = 0;
                    boolean ischecked = false;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
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

                    String result = "";
                    String result_two = "";

                    //실내가 많은 경우
                    if (related[0][0] > related[0][1]) {
                        result = "실내";

                    }
                    //야외가 많은 경우
                    else if (related[0][0] < related[0][1]) {
                        result = "야외";
                    } else { //0과 1만 출력
                        //실내 야외가 동률인 경우 랜덤
                        int p = (int) (Math.random() * 2);
                        if( p == 0 )
                            result = "실내";
                        else
                            result = "야외";
                    }
                    //감상이 많은 경우
                    if (related[1][0] > related[1][1]) {
                        result_two = "감상";
                    }
                    //참여가 많은 경우
                    else if (related[1][0] < related[1][1]) {
                        result_two = "참여";
                    }
                    //감상과 참여가 동률인경우 랜덤
                    else {
                        int p = (int) (Math.random() * 2);
                        if( p == 0 )
                            result_two = "감상";
                        else
                            result_two = "참여";
                    }

                    //리스너안에서 사용하기위해 final로 처리
                    final String finalResult = result;
                    final String finalResult_two = result_two;

                    myRef_two.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int a = 0;
                            int b = 0;
                            int c = 0;

                            //DB에 있는 취미 갯수를 SharedPreference 에서 가져옴
                            final String[] hobby_one = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];
                            final String[] hobby_two = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];
                            final String[] hobby_three = new String[PreferenceUtil.getInstance(getContext()).getIntExtra("hobby_num")];

                            String hobby_name = "";
                            boolean has_in_like = false;

                            //카테고리 가 제일 높은 3개를 찾는다.
                            int category_cnt = 1;
                            int max = category_num[0];
                            int index = 0;
                            for (int q = 1; q < 12; q++) {
                                if (max < category_num[q]) {
                                    max = category_num[q];
                                    index = q;
                                }
                            }
                            category_num[index] = 0;
                            max = category_num[0];
                            int index_2 = 0;
                            for (int q = 1; q < 12; q++) {
                                if (max < category_num[q]) {
                                    max = category_num[q];
                                    index_2 = q;
                                }
                            }
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
                            }

                            //첫번째 카테고리 취미들을 찜을 제외한 취미들만 가져온다
                            //hobby_one 배열에 저장장
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
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

                                //hooby_two 배열에 두번째 카테고리에서 찜한 취미를 제외하고 저장한다.
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
                                //hooby_three 배열에 세번째 카테고리에서 찜한 취미를 제외하고 저장한다.
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

                            //카테고리별로 나온 취미들을 가나다순으로 정렬 디비에서 읽기 편하기 위해서
                            Arrays.sort(hobby_one, 0, a);
                            Arrays.sort(hobby_two,0, b);
                            Arrays.sort(hobby_three,0,c);

                            //찜을 제외하고 3개의 카테고리에서 나온 취미들의 합
                            final int hobby_sum = a + b + c;

                            //결과를 5개만 보여준다.
                            String[] result = new String[5];

                            //카테고리가 한 개 인경우 5개까지 뽑는다.

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
                            //카테고리가 2개 인경우 hobby_one, hobby_two 배열에서 나누어 뽑는다.
                            //한쪽이 매우 적은 경우
                            //
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
                                else //마지막 날
                                {
                                    if ( a < 2 && b == 2 )
                                    {
                                        System.arraycopy(hobby_one,0,result,0,a);
                                        System.arraycopy(hobby_two,0,result,a,2);
                                        for( int i = a + 2 ; i < 5 ; i++)
                                        {
                                            int ran = (int) (Math.random() * c);
                                            for ( int j = a + 2; j < i ; j++){
                                                if(result[j].equals(hobby_three[ran]))
                                                {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if( !ran_check)
                                            {
                                                result[i] = hobby_three[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                    else if ( a < 2 )
                                    {
                                        System.arraycopy(hobby_one,0,result,0,a);
                                        for( int i = a  ; i < a + 2 ; i++)
                                        {
                                            int ran = (int) (Math.random() * b);
                                            for ( int j = a ; j < i ; j++){
                                                if(result[j].equals(hobby_two[ran]))
                                                {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if( !ran_check)
                                            {
                                                result[i] = hobby_two[ran];
                                            }
                                            ran_check = false;
                                        }
                                        for( int i = a + 2 ; i < 5 ; i++)
                                        {
                                            int ran = (int) (Math.random() * c);
                                            for ( int j = a + 2 ; j < i ; j++){
                                                if(result[j].equals(hobby_three[ran]))
                                                {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if( !ran_check)
                                            {
                                                result[i] = hobby_three[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                    else if ( b < 2 && a == 2)
                                    {
                                        System.arraycopy(hobby_one,0,result,0,2);
                                        System.arraycopy(hobby_two,0,result,2,b);
                                        for( int i = b + 2 ; i < 5 ; i++)
                                        {
                                            int ran = (int) (Math.random() * c);
                                            for ( int j = b+ 2; j < i ; j++){
                                                if(result[j].equals(hobby_three[ran]))
                                                {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if( !ran_check)
                                            {
                                                result[i] = hobby_three[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                    else if ( b < 2) // a 가 3 이상
                                    {
                                        System.arraycopy(hobby_two,0,result,0,b);
                                        for (int i = b; i < b + 3; i++) {
                                            int ran = (int) (Math.random() * a);
                                            for (int j = b; j < i; j++) {
                                                if (result[j].equals(hobby_one[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if (!ran_check) {
                                                result[i] = hobby_one[ran];
                                            }
                                            ran_check = false;
                                        }
                                        for (int i = b + 3; i < 5; i++) {
                                            int ran = (int) (Math.random() * c);
                                            for (int j = b+3; j < i; j++) {
                                                if (result[j].equals(hobby_three[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if (!ran_check) {
                                                result[i] = hobby_three[ran];
                                            }
                                            ran_check = false;
                                        }
                                        count = 5;
                                    }
                                    else{
                                        for (int i = 0; i < 2; i++) {
                                            int ran = (int) (Math.random() * a);
                                            for (int j = 0; j < i; j++) {
                                                if (result[j].equals(hobby_one[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if (!ran_check) {
                                                result[i] = hobby_one[ran];
                                            }
                                            ran_check = false;
                                        }
                                        for (int i = 2; i < 4; i++) {
                                            int ran = (int) (Math.random() * b);
                                            for (int j = 2; j < i; j++) {
                                                if (result[j].equals(hobby_two[ran])) {
                                                    i--;
                                                    ran_check = true;
                                                    break;
                                                }
                                            }
                                            if (!ran_check) {
                                                result[i] = hobby_two[ran];
                                            }
                                            ran_check = false;
                                        }
                                        int ran = (int) (Math.random() * c);
                                        System.arraycopy(hobby_three, ran, result, 4, 1);
                                        count = 5;
                                    }
                                }
                            }

                            Arrays.sort(result,0, count);

                            final String[] final_related_hobby = new String[count];
                            final int final_count = count;
                            System.arraycopy(result, 0, final_related_hobby, 0 , count);

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    List<RelatedlistInfo> relatedlistInfo_items =new ArrayList<>();
                                    RelatedlistInfo[] item = new RelatedlistInfo[5];
                                    RelatedlistAdapter relatedlistAdapter = new RelatedlistAdapter(getContext(),relatedlistInfo_items,R.layout.fragment_tab_fragment4);

                                    if(final_related_hobby.length == 0) //아무 취미가 없는경우
                                        return;
              
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
                                    relatedlistAdapter.notifyDataSetChanged();
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