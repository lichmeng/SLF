package com.sugar.myapplication.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.holder.SearchResultGridViewHolder;
import com.sugar.myapplication.holder.SearchResultListViewHolder;

import org.senydevpkg.base.AbsBaseAdapter;
import org.senydevpkg.base.BaseHolder;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class SearchResultAdapter extends AbsBaseAdapter<ProductListInfo> {
    private int mode;
    public static final int MODE_LISTVIEW = 0;
    public static final int MODE_GRIDVIEW = 1;

    /**
     * 接收AbsListView要显示的数据
     *
     * @param context
     * @param data    要显示的数据
     */
    public SearchResultAdapter(Context context, List<ProductListInfo> data,int mode) {
        super(context, data);
        this.mode = mode;
    }

    @Override
    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (mode) {
            case MODE_GRIDVIEW:
                return new SearchResultGridViewHolder(mContext);
            case MODE_LISTVIEW:
                return new SearchResultListViewHolder(mContext);
        }
        return new SearchResultListViewHolder(mContext);
    }

    public int getMode() {
        return mode;
    }
}
