package com.sugar.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sugar.myapplication.R;

import java.util.List;

import static com.sugar.myapplication.App.mContext;

/**
 * Created by sugar on 201/11/23.
 */

public class CategoriesAdapter extends BaseAdapter {
    private List<String> mdata;
    private int defItem;
    /**
     * 接收AbsListView要显示的数据
     *
     * @param context
     * @param data    要显示的数据
     */
    public CategoriesAdapter(Context context, List<String> data) {
        mdata = data;
    }
//
//    @Override
//    protected BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new CategoriesHolder(mContext);
//    }

    @Override
    public int getCount() {

        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(mContext, R.layout.item_category_navigator, null);
        textView.setText(mdata.get(position));

        if (defItem == position) {
            textView.setBackgroundResource(R.drawable.category_bg_pressed);
            textView.setTextColor(Color.RED);
        } else {
            textView.setBackgroundResource(R.drawable.category_bg_normal);
            textView.setTextColor(Color.parseColor("#ff333333"));
        }
        return textView;
    }

    public void setCurrentSelectItem(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }
}
