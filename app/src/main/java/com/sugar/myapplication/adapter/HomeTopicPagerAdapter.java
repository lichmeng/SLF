package com.sugar.myapplication.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.bean.HomeResponse;
import com.sugar.myapplication.global.Api;

import java.util.List;

/**
 * Created by wangyu on 2016/11/23.
 */

public class HomeTopicPagerAdapter extends PagerAdapter {
    private List<HomeResponse.HomeTopicBean> mList;

    public HomeTopicPagerAdapter(List<HomeResponse.HomeTopicBean> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(App.mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        App.mLoader.display(imageView, Api.URL_SERVER+mList.get(position%mList.size()).getPic());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
