package com.sugar.myapplication.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.LimitBuyActivity;
import com.sugar.myapplication.bean.LimitBuyReaponse;
import com.sugar.myapplication.global.Api;

import java.util.List;

/**
 * Created by wangyu on 2016/11/28.
 */

public class SeckillRvAdapter extends RecyclerView.Adapter<SeckillRvAdapter.SeckillHolder> {
    private List<LimitBuyReaponse.ProductListBean> mList;

    public SeckillRvAdapter(List<LimitBuyReaponse.ProductListBean> list) {
        mList = list;
    }

    @Override
    public SeckillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(App.mContext, R.layout.item_home_seckill, null);
        SeckillHolder holder = new SeckillHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SeckillHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SeckillHolder extends RecyclerView.ViewHolder {
        private ImageView mIvSeckill;
        private TextView mTvLimitPrice;
        private TextView mTvPrice;

        public SeckillHolder(View itemView) {
            super(itemView);
            mIvSeckill = (ImageView) itemView.findViewById(R.id.iv_seckill);
            mTvLimitPrice = (TextView) itemView.findViewById(R.id.tv_limit_price);
            mTvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }

        public void bindData(int position) {
            LimitBuyReaponse.ProductListBean product = mList.get(position);
            App.mLoader.display(mIvSeckill, Api.URL_SERVER + product.getPic());
            mTvLimitPrice.setText("￥" + product.getLimitPrice());
            mTvPrice.setText("￥" + product.getPrice());
            mTvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            final int temp = position;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.mContext, LimitBuyActivity.class);
                    intent.putExtra("position", temp);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.mContext.startActivity(intent);
                }
            });
        }
    }
}
