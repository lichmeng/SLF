package com.sugar.myapplication.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.global.Api;

import org.senydevpkg.net.HttpLoader;

import java.util.List;

/**
 * Created by wjx on 2016/11/25.
 */

public class GoodsInfoPicAdapter extends PagerAdapter {
    private List<String> list;
    public GoodsInfoPicAdapter(List<String> bigPics){
        list = bigPics;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(App.mContext);

        HttpLoader.getInstance(App.mContext).display(iv, Api.URL_SERVER+list.get(position), R.drawable.product_loading, R.drawable.product_loading);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
