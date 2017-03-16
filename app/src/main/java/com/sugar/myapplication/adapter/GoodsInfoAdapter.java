package com.sugar.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class GoodsInfoAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] tabs = new String[]{"商品","详情","评价"};
    public GoodsInfoAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        Log.e("-----------", "GoodsInfoAdapter: "+list.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
