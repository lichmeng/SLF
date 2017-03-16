package com.sugar.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.RecommendedBrandResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.view.RatioImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangyu on 2016/11/24.
 */

public class BrandAdapter extends BaseAdapter4Home<Object> {

    private static final int BRAND_TITLE = 0;
    private static final int BRAND_INFO = 1;

    public BrandAdapter(List<Object> list) {
        super(list);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = mList.get(position);
        if (obj instanceof String) {
            return BRAND_TITLE;
        } else {
            return BRAND_INFO;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitleHolder titleHolder = null;
        InfoHolder infoHolder = null;
        if (convertView == null) {
            if (getItemViewType(position) == BRAND_TITLE) {
                convertView = View.inflate(App.mContext, R.layout.item_brand_title, null);
                titleHolder = new TitleHolder();
                titleHolder.mtvTitle = (TextView) convertView.findViewById(R.id.tv_brand_title);
                convertView.setTag(titleHolder);
            } else {
                convertView = View.inflate(App.mContext, R.layout.item_brand_info, null);
                infoHolder = new InfoHolder(convertView);
                convertView.setTag(infoHolder);
            }
        } else {
            if (getItemViewType(position) == BRAND_TITLE) {
                titleHolder = (TitleHolder) convertView.getTag();
            } else {
                infoHolder = (InfoHolder) convertView.getTag();
            }
        }

        if (getItemViewType(position) == BRAND_TITLE) {
            String title = (String) mList.get(position);
            titleHolder.mtvTitle.setText(title);
        } else {
            RecommendedBrandResponse.BrandBean.ValueBean info = (RecommendedBrandResponse.BrandBean.ValueBean) mList.get(position);
            App.mLoader.display(infoHolder.mIvImage, Api.URL_SERVER + info.getPic());
            infoHolder.mIvImage.setRatio(1.5f);
            infoHolder.mTvName.setText(info.getName());
        }

        /*ViewHelper.setScaleX(convertView, 0.5f);
        ViewHelper.setScaleY(convertView, 0.5f);
        ViewPropertyAnimator animator = ViewPropertyAnimator.animate(convertView);
        animator.scaleX(1.0f).setDuration(500).start();
        animator.scaleY(1.0f).setDuration(500).start();*/

        return convertView;
    }

    class TitleHolder {
        TextView mtvTitle;
    }

    class InfoHolder {

        @InjectView(R.id.iv_image)
        RatioImageView mIvImage;
        @InjectView(R.id.tv_name)
        TextView mTvName;
        public InfoHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
