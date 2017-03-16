package com.sugar.myapplication.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.GoodsInfoActivity;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.view.RatioImageView;

import java.util.List;
import java.util.Random;

/**
 * Created by wangyu on 2016/11/29.
 */

public class TopicPListRvAdapter extends RecyclerView.Adapter<TopicPListRvAdapter.PListHolder> {
    private List<ProductListInfo> mList;

    public TopicPListRvAdapter(List<ProductListInfo> list) {
        mList = list;
    }

    @Override
    public PListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(App.mContext, R.layout.item_topic_plist_rv, null);
        PListHolder holder = new PListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PListHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PListHolder extends RecyclerView.ViewHolder {
        private RatioImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvMarketPrice;
        private TextView tvEvaluateNum;
        private TextView tvGoodEvaluate;

        public PListHolder(View itemView) {
            super(itemView);
            ivImage = (RatioImageView) itemView.findViewById(R.id.iv_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvMarketPrice = (TextView) itemView.findViewById(R.id.tv_market_price);
            tvEvaluateNum = (TextView) itemView.findViewById(R.id.tv_evaluate_num);
            tvGoodEvaluate = (TextView) itemView.findViewById(R.id.tv_good_evaluate);
        }

        public void bindData(final int position) {
            final ProductListInfo info = mList.get(position);
            App.mLoader.display(ivImage, Api.URL_SERVER + info.getPic());
            ivImage.setRatio(0.66f);
            tvName.setText(info.getName());
            tvPrice.setText("￥" + info.getPrice());
            tvMarketPrice.setText("￥" + info.getMarketPrice());
            tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Random random = new Random();
            tvEvaluateNum.setText(random.nextInt(3000)+8000+"条评价");
            tvGoodEvaluate.setText(random.nextInt(19)+80+"%好评");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.mContext, GoodsInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", info.getId());
                    App.mContext.startActivity(intent);
                }
            });
        }
    }
}
