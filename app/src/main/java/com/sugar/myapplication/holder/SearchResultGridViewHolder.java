package com.sugar.myapplication.holder;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.global.Api;

import org.senydevpkg.base.BaseHolder;
import org.senydevpkg.net.HttpLoader;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sugar on 2016/11/25.
 */

public class SearchResultGridViewHolder extends BaseHolder<ProductListInfo> {
    @InjectView(R.id.iv_product_icon)
    ImageView mIvProductIcon;
    @InjectView(R.id.tv_product_name)
    TextView mTvProductName;
    @InjectView(R.id.tv_product_price)
    TextView mTvProductPrice;
    @InjectView(R.id.tv_product_marketPrice)
    TextView mTvProductMarketPrice;
    @InjectView(R.id.tv_product_comment)
    TextView mTvProductComment;

    public SearchResultGridViewHolder(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_search_gridview, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void bindData(ProductListInfo data) {
        mTvProductName.setText("Smartisan M1" +data.getName() );
        mTvProductPrice.setText("¥"+ data.getPrice()+".00");
        mTvProductComment.setText("已有 "+new Random().nextInt(1000)+" 人评价");
        HttpLoader.getInstance(mContext).display(mIvProductIcon, Api.URL_SERVER + data.getPic());

        //市场价加横线
        mTvProductMarketPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mTvProductMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTvProductMarketPrice.setText("¥"+data.getMarketPrice()+".00");
    }
}
