package com.sugar.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.PromotionsResponse;
import com.sugar.myapplication.global.Api;

import java.util.List;

/**
 * Created by wangyu on 2016/11/23.
 */

public class PromotionsAdapter extends BaseAdapter4Home<PromotionsResponse.TopicBean> {

    public PromotionsAdapter(List list) {
        super(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(App.mContext, R.layout.item_promotions, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_topic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        PromotionsResponse.TopicBean topic =  mList.get(position);
        holder.textView.setText(topic.getName());
        App.mLoader.display(holder.imageView, Api.URL_SERVER+topic.getPic());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
