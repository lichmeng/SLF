package com.sugar.myapplication.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.GoodsInfoActivity;
import com.sugar.myapplication.bean.LimitBuyReaponse;
import com.sugar.myapplication.global.Api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wangyu on 2016/11/24.
 */

public class LimitBuyAdapter extends BaseAdapter4Home<LimitBuyReaponse.ProductListBean> {

    private int goneTime = 0;

    public LimitBuyAdapter(List<LimitBuyReaponse.ProductListBean> list) {
        super(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(App.mContext, R.layout.item_limit_buy_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LimitBuyReaponse.ProductListBean product = mList.get(position);
        App.mLoader.display(holder.mIvImage, Api.URL_SERVER + product.getPic());
        holder.mTvName.setText(product.getName());
        holder.mTvPrice.setText("￥" + product.getPrice() + ".00");
        holder.mTvLimitPrice.setText("￥" + product.getLimitPrice() + ".00");
        holder.mTvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        Random random = new Random();
        int progress = random.nextInt(79)+20;
        holder.mTvRatio.setText("已售"+progress+"%");
        holder.mPbProgress.setProgress(progress);
        //时间
        int time = (product.getLeftTime() * 1000);
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String leftTime = format.format(date);
        holder.mTvLimtTime.setText("剩余时间: " + leftTime);

        holder.mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.mContext, GoodsInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", product.getId());
                App.mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {

        @InjectView(R.id.iv_image)
        ImageView mIvImage;
        @InjectView(R.id.tv_name)
        TextView mTvName;
        @InjectView(R.id.tv_des)
        TextView mTvDes;
        @InjectView(R.id.tv_price)
        TextView mTvPrice;
        @InjectView(R.id.tv_limit_price)
        TextView mTvLimitPrice;
        @InjectView(R.id.tv_limt_time)
        TextView mTvLimtTime;
        @InjectView(R.id.btn_buy)
        Button mBtnBuy;
        @InjectView(R.id.pb_progress)
        ProgressBar mPbProgress;
        @InjectView(R.id.tv_ratio)
        TextView mTvRatio;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
