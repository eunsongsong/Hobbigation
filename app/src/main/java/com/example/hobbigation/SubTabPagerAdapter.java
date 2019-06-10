package com.example.hobbigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *  취미 정보 제공 화면 탭레이아웃을 pager 형태로 swipe 가능하게 해주는 adapter
 */
public class SubTabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public SubTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0: //블로그 검색 결과
                Sub_BlogTab tab1 = new Sub_BlogTab();
                return tab1;
            case 1: //카페 검색 결과
                Sub_CafeTab tab2 = new Sub_CafeTab();
                return tab2;
            case 2: //쇼핑 검색 결과
                Sub_ShopTab tab3 = new Sub_ShopTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
