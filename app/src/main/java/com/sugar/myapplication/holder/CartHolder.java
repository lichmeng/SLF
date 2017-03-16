package com.sugar.myapplication.holder;

import android.content.Context;
import android.view.View;

import com.sugar.myapplication.bean.CartResponse;

import org.senydevpkg.base.BaseHolder;

/**
 * Created by Administrator on 2016/11/23.
 */
public class CartHolder extends BaseHolder<CartResponse.CartBean> {



    public CartHolder(Context context) {
        super(context);
    }

    @Override
    protected View initView() {

        return null;
    }

    @Override
    public void bindData(CartResponse.CartBean data) {
        super.bindData(data);

    }
}
