package com.sugar.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sugar.myapplication.bean.TextBean;
import com.sugar.myapplication.welcome.fragment.base.LoginAnimTextBaseFragment;

import java.util.ArrayList;


/**
 * 下层文字动画有4页
 */
public class TextFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<TextBean> mTextBeans;

    public TextFragmentStatePagerAdapter(FragmentManager fm, ArrayList<TextBean> tbs) {
        super(fm);
        if(tbs != null){
            mTextBeans = tbs;
        }
        else{
            mTextBeans = new ArrayList<TextBean>();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return new LoginAnimTextBaseFragment(mTextBeans.get(position));
    }

    @Override
    public int getCount() {
        return mTextBeans.size();
    }
}
