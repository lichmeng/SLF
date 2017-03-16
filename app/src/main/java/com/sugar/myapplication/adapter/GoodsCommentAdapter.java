package com.sugar.myapplication.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sugar.myapplication.bean.ProductDetailCommend;
import com.sugar.myapplication.holder.GoodsCommentHolder;

import org.senydevpkg.base.AbsBaseAdapter;
import org.senydevpkg.base.BaseHolder;

import java.util.List;

/**
 * Created by wjx on 2016/11/25.
 */

public class GoodsCommentAdapter extends AbsBaseAdapter<ProductDetailCommend.CommentBean> {


    /**
     * 接收AbsListView要显示的数据
     *
     * @param context
     * @param data    要显示的数据
     */
    public GoodsCommentAdapter(Context context, List<ProductDetailCommend.CommentBean> data) {
        super(context, data);
    }

    @Override
    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsCommentHolder(mContext);
    }
}
