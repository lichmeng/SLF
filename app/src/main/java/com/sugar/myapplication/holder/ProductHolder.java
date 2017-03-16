package com.sugar.myapplication.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.SearchProductResponse;

import org.senydevpkg.base.BaseHolder;

/**
 * Created by wjx on 2016/11/24.
 */

public class ProductHolder extends BaseHolder<SearchProductResponse.ProductList> {

    private TextView mTv;

    public ProductHolder(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_search_product,null);
        mTv = (TextView) view.findViewById(R.id.tv_search_reslut);
        return view;
    }

    @Override
    public void bindData(SearchProductResponse.ProductList data) {
        mTv.setText(data.name);
    }
}
