package com.sugar.myapplication.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.GoodsInfoActivity;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.global.Api;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangyu on 2016/11/24.
 */

public class TopicPListAdapter extends BaseAdapter4Home<ProductListInfo> {

    public TopicPListAdapter(List list) {
        super(list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(App.mContext, R.layout.item_topic_plist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductListInfo product = mList.get(position);
        App.mLoader.display(holder.mIvImage, Api.URL_SERVER + product.getPic());
        holder.mTvName.setText(product.getName());
        holder.mTvMarketPrice.setText("￥ " + product.getMarketPrice() + ".00");
        holder.mTvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mTvPrice.setText("￥ " + product.getPrice() + ".00");
        Random random = new Random();
        holder.mTvEvaluateNum.setText(random.nextInt(3000)+8000+"条评价");
        holder.mTvGoodEvaluate.setText(random.nextInt(19)+80+"%好评");
        holder.mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.mContext, GoodsInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", mList.get(position).getId());
                App.mContext.startActivity(intent);
            }
        });

        ViewHelper.setTranslationX(convertView, 450f);
        ViewHelper.setScaleY(convertView, 0.5f);
        ViewPropertyAnimator animator = ViewPropertyAnimator.animate(convertView);
        animator.translationX(0).setDuration(500).start();
        animator.scaleY(1.0f).setDuration(500).start();
        return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.iv_image)
        ImageView mIvImage;
        @InjectView(R.id.tv_name)
        TextView mTvName;
        @InjectView(R.id.tv_price)
        TextView mTvPrice;
        @InjectView(R.id.tv_market_price)
        TextView mTvMarketPrice;
        @InjectView(R.id.tv_evaluate_num)
        TextView mTvEvaluateNum;
        @InjectView(R.id.tv_good_evaluate)
        TextView mTvGoodEvaluate;
        @InjectView(R.id.btn_buy)
        Button mBtnBuy;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
