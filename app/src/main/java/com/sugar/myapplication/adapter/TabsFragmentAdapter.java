package com.sugar.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sugar.myapplication.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by sugar on 2016/11/22.
 */

public class TabsFragmentAdapter extends FragmentPagerAdapter {
    protected ArrayList<BaseFragment> mTabs;
    public TabsFragmentAdapter(FragmentManager fm, ArrayList<BaseFragment> mTabs) {
        super(fm);
        this.mTabs = mTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (mTabs==null || mTabs.size() < 1) {
            throw new IllegalArgumentException("TabsFragmentAdapter needs a not null(size>0) ArrayList!");
        }
        return mTabs.get(position);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

}
