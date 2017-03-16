package com.sugar.myapplication.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;

/**
 * Created by wangyu on 2016/11/29.
 */

public class LimitBuyPagerAdapter extends PagerAdapter {
    int[] topic = {
            R.drawable.topic2,
            R.drawable.topic5,
            R.drawable.topic7,
            R.drawable.topic10,
    };
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
        imageView.setImageResource(topic[position%topic.length]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
