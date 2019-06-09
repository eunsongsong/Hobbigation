package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rd.PageIndicatorView;

/**
 * Hobbigation 앱의 전체적인 기능을 아주 간략히 보여주는 화면이다.
 * ViewPager를 이용하여 양 옆으로 화면을 넘길 수 있다.
 */
public class GuideActivity extends AppCompatActivity {

    private ViewPager viewPager ;
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ActionBar actionBar = getSupportActionBar();  //제목줄 객체 얻어오기
        actionBar.setDisplayHomeAsUpEnabled(true);   //업버튼 <- 만들기
        backPressCloseHandler = new BackPressCloseHandler(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        //PageIndicator는 현재 페이지를 표시해준다.
        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);

    }

    //회원가입과 로그인인을 선택 할 수있는 페이지로 넘어갈 수 있는 버튼 클릭 이벤트
    public void login(View v){
            finish();
            startActivity(new Intent(getApplicationContext(),BeforeSignin.class));
    }

    //
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                backPressCloseHandler.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //뒤로가기 버튼 클릭시 호출
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}
