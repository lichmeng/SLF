package com.sugar.myapplication.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sugar.myapplication.bean.SearchProductResponse;
import com.sugar.myapplication.holder.ProductHolder;

import org.senydevpkg.base.AbsBaseAdapter;
import org.senydevpkg.base.BaseHolder;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class ProductAdapter extends AbsBaseAdapter<SearchProductResponse.ProductList> {
    /**
     * 接收AbsListView要显示的数据
     *
     * @param context
     * @param data    要显示的数据
     */
    public ProductAdapter(Context context, List<SearchProductResponse.ProductList> data) {
        super(context, data);
    }

    @Override
    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductHolder(mContext);
    }
}
