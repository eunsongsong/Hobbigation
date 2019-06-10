package com.example.hobbigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *  메인 화면 탭레이아웃을 pager 형태로 swipe 가능하게 해주는 adapter
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0: //홈
                TabFragment1 tabFragment1 = new TabFragment1();
                return tabFragment1;
            case 1: //카테고리
                TabFragment2 tabFragment2 = new TabFragment2();
                return tabFragment2;
            case 2: //마이페이지
                TabFragment3 tabFragment3 = new TabFragment3();
                return tabFragment3;
            case 3: //찜목록 + 연관취미
                TabFragment4 tabFragment4 = new TabFragment4();
                return tabFragment4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}