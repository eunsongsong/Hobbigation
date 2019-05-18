package com.example.hobbigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
            case 0:
                Sub_BlogTab tab1 = new Sub_BlogTab();
                return tab1;
            case 1:
                Sub_CafeTab tab2 = new Sub_CafeTab();
                return tab2;
            case 2:
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
