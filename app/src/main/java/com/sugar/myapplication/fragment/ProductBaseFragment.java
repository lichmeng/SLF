package com.sugar.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugar.myapplication.bean.ProductDetailCommend;
import com.sugar.myapplication.bean.ProductDetailDes;
import com.sugar.myapplication.bean.ProductResponse;
import com.sugar.myapplication.manager.ProductDetailManager;

/**
 * Created by wjx on 2016/11/25.
 */

public abstract class ProductBaseFragment extends Fragment implements ProductDetailManager.OnRequestProductListener{
    protected int pId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        pId = bundle.getInt("pId");
        View view= initView();
        initData();
        return view;
    }

    protected abstract View initView();
    protected abstract void initData();


    @Override
    public void onRequestSuccess(ProductResponse response) {

    }

    @Override
    public void onRequestSuccess(ProductDetailDes response) {

    }

    @Override
    public void onRequestSuccess(ProductDetailCommend response) {

    }

    @Override
    public void onRequestFailed() {

    }
}
