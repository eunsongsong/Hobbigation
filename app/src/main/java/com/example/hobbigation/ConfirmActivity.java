package com.example.hobbigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 취미 추천 결과를 보여주는 화면
 */
public class ConfirmActivity extends AppCompatActivity {

    String[] tag_array;  //RecommendActivity에서 유저가 누른 이미지의 태그
    int[] weight;  //태그의 가중치 (태그와 같은 인덱스)
    int[] sorted_weigh; //정렬된 가중치 배열

    int row;  //tag_array 와 weight의 배열 개수
    int minus;  //tag_array 배열중 null인 개수
    static int[] comb_int;  //조합 (0,1,0,2,1,2...와 같은 순서로 저장)
    static int j=0;  //조합에서 사용 변수
    static int comb_num;  //조합 개수 (조합이 한 쌍이 아니라 숫자 1개씩 저장되어 있으므로 x2)
    public RecyclerView result_recycler_view;

   //firebase database reference를 취미로 설정
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("취미");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        //제목줄 객체 얻어오기
        ActionBar actionBar = getSupportActionBar();
        //액션바에 뒤로가기 버튼 나타내기
        actionBar.setDisplayHomeAsUpEnabled(true);

        //취미결과를 RecyclerView로 보여줌
        result_recycler_view = (RecyclerView) findViewById(R.id.result_recycler);

        final LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        result_recycler_view.setHasFixedSize(true);
        result_recycler_view.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        //스트링 배열 가중치 (정렬안됨)
        //행 갯수 하나로 맞추기

        tag_array = intent.getStringArrayExtra("tag[]");
        weight = intent.getIntArrayExtra("weighcnt[]");
        row = intent.getIntExtra("row",0);
        minus = intent.getIntExtra("minus",0);

        //wighcnt[]를 sorted_weigh에 복사
         sorted_weigh = weight.clone();
        //sorted weigh 오름차순 정렬
        Arrays.sort(sorted_weigh);


        int i, high = 0;
        for ( i = sorted_weigh.length- 1 ; i>0; i--) {
            //가중치가 1이면 이 뒤로 다 1이므로 소팅 중지
            if (sorted_weigh[i] == 1) break;
            //높은 가중치가 어디에 있는지 찾기
            for (int k = high; k < weight.length - minus; k++) {
                if (sorted_weigh[i] == weight[k]) {
                    //가중치 높은 태그와 가중치값 저장
                    String max_tag = tag_array[k];
                    int max_weight = weight[k];
                    //한칸씩 뒤로 밀기
                    for(int n=k-1; n>=high; n--) {
                        tag_array[n+1] = tag_array[n];
                        weight[n+1] = weight[n];
                    }
                    //배열 맨 앞에 가중치 높은 순으로 배치
                    tag_array[high] = max_tag;
                    weight[high] = max_weight;
                    high++;
                    break;
                }
            }
        }

        // 1보다 큰 가중치 태그 개수
        final int gt_one_tag = sorted_weigh.length - i - 1;

        //중복된 태그 수를 제외한 전체 태그 수
        final int tags_num= tag_array.length - minus;

        myRef.child("이미지_태그").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int hobbycnt = (int) dataSnapshot.getChildrenCount();  //전체 취미 개수
                String[] hobby_2 = new String[hobbycnt]; //추천 결과 취미 이름
                String[] url_2 = new String[hobbycnt];  //추천 결과 취미 이미지 url
                int[] weight_2 = new int[hobbycnt];  //추천 결과 취미 가중치
                int index;

                if (true) {
                    j = 0;
                    int n = gt_one_tag;
                    comb_num = n * (n - 1);
                    comb_int = new int[comb_num];
                    int[] arr = new int[n];  //조합 생성을 위한 배열1
                    for (int i = 0; i < n; i++)
                        arr[i] = i;
                    boolean[] visited = new boolean[n]; //조합 생성을 위한 배열2

                    //n개 중 2개 뽑기
                    combination(arr, visited, 0, n, 2);

                    boolean exist = false;
                    index = 0;
                    int result_cnt_2 = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String con = ds.child("취미_태그").getValue().toString();

                        //취미 태그와 사용자가 선택한 가중치 높은 2개 태그 + 가중치 낮은 1개 태그 비교
                        for (int m = 0; m < comb_int.length - 1; m = m + 2) {
                            for (int k = gt_one_tag; k < tags_num; k++) {
                                //취미 태그가 사용자 선택 태그를 포함하면 추천결과배열에 저장
                                if (con.contains(tag_array[comb_int[m]]) && con.contains(tag_array[comb_int[m + 1]]) && con.contains(tag_array[k])) {
                                    for (int i = 0; i < hobbycnt; i++) {
                                        //이미 저장된 취미가 다시 검색되면 가중치만 높임
                                        if (ds.getKey().equals(hobby_2[i])) {
                                            weight_2[i] += 1;
                                            exist = true;
                                            break;
                                        }
                                    }
                                    //취미 이름과 이미지url, 가중치 저장
                                    if (!exist) {
                                        hobby_2[index] = ds.getKey();
                                        result_cnt_2++;
                                        url_2[index] = ds.child("url_태그").child("0").child("url").getValue().toString();
                                        weight_2[index] += 1;
                                        index++;
                                    } else
                                        exist = false;
                                }
                            }
                        }
                    }

                    List<HobbyResultInfo> result_items = new ArrayList<>();
                    HobbyResultInfo[] item = new HobbyResultInfo[result_cnt_2];

                    //최댓값 찾기 x 5번
                    int max = weight_2[0];
                    int k, ind = 0;

                    //최대 5개 결과 출력
                    for (int i = 0; i < 5; i++) {
                        if (i > result_cnt_2 - 1) {
                            //결과가 아예 없으면 로고 이미지 띄우고 다시 선택 요구
                            if (result_cnt_2 == 0) {
                                String logo = "https://firebasestorage.googleapis.com/v0/b/habbigation-27e01.appspot.com/o/no.png?alt=media&token=568a6365-f948-4460-b712-ee89e672423b";
                                HobbyResultInfo[] null_item = new HobbyResultInfo[1];
                                null_item[0] = new HobbyResultInfo("죄송합니다. 결과가 없습니다. \n다시 한번 골라주세요!", logo);
                                result_items.add(null_item[0]);
                            }
                            break; //결과가 5개 미만이면 탈출
                        }
                        //가중치 높은 추천결과 찾기
                        for (k = 1; k < result_cnt_2; k++) {
                            if (max < weight_2[k]) {
                                max = weight_2[k];
                                ind = k;
                            }
                        }
                        item[i] = new HobbyResultInfo(hobby_2[ind], url_2[ind]);
                        result_items.add(item[i]);

                        weight_2[ind] = 0;
                        ind = 0;
                        max = weight_2[0];
                    }

                    HobbyResultAdapter hobbyResultAdapter = new HobbyResultAdapter(getApplicationContext(), result_items, R.layout.activity_confirm);
                    result_recycler_view.setAdapter(hobbyResultAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // 조합 구하기 - 백트래킹 사용
    // 사용 예시 : combination(arr, visited, 0, n, r)
    static void combination(int[] arr, boolean[] visited, int start, int n, int r) {
        if(r == 0) {
            for(int i=0; i<n; i++) {
                if (visited[i] == true) {
                    if (j>=comb_num) break;
                    //조합 결과를 숫자 1개씩 저장
                    comb_int[j] = arr[i];
                    j++;
                }
            }
            return;
        } else {
            //r을 줄이면서 조합 찾기
            for(int i=start; i<n; i++) {
                visited[i] = true;
                combination(arr, visited, i + 1, n, r - 1);
                visited[i] = false;
            }
        }
    }

    //액션바의 뒤로가기 버튼 터치시 액티비티 finish
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

}
