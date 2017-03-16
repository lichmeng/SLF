package com.sugar.myapplication.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.GoodsCommentAdapter;
import com.sugar.myapplication.bean.ProductDetailCommend;
import com.sugar.myapplication.bean.SearchRecommendResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.manager.ProductDetailManager;
import com.sugar.myapplication.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wjx on 2016/11/24.
 * 评价的fragment
 */

public class EvaluationFragment extends ProductBaseFragment {

    @InjectView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @InjectView(R.id.tv_good_comment)
    TextView tvGoodComment;
    @InjectView(R.id.iv_comment_right)
    ImageView ivCommentRight;
    @InjectView(R.id.ll_comment)
    LinearLayout llComment;
    @InjectView(R.id.ll_empty_comment)
    LinearLayout llEmptyComment;
    @InjectView(R.id.lv_goods_commend)
    ListView lvGoodsCommend;
    private ProductDetailCommend productDetailCommend;
    private ProductDetailManager productDetailManager;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_pingjia, null);
        ButterKnife.inject(this,view);
        //ProductDetailManager.getInstance().setOnRequestProductListener(this);
        productDetailManager = new ProductDetailManager();
        productDetailManager.setOnRequestProductListener(this);
        return view;
    }

    @Override
    protected void initData() {
        productDetailManager.getProductCommend(pId, Api.URL_PRODUCT_COMMENT, 0, 10);
    }

    @Override
    public void onRequestSuccess(ProductDetailCommend response) {
        productDetailCommend = response;
        if (productDetailCommend != null) {
            ToastUtil.showToast("请求成功");
            llEmptyComment.setVisibility(View.GONE);
            lvGoodsCommend.setVisibility(View.VISIBLE);
            lvGoodsCommend.setAdapter(new GoodsCommentAdapter(getActivity(),productDetailCommend.comment));
        }else{
            ToastUtil.showToast("请求失败");
            llEmptyComment.setVisibility(View.VISIBLE);
            lvGoodsCommend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestSearchRecommendSuccess(SearchRecommendResponse response) {

    }

    @Override
    public void onRequestFailed() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
