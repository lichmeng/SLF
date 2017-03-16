package com.sugar.myapplication.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sugar.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wjx on 2016/11/24.
 */

public class PhotoDetailFragment extends BaseFragment {
    @InjectView(R.id.fl_content)
    FrameLayout flContent;
    @InjectView(R.id.tv_goods_detail)
    TextView tvGoodsDetail;
    @InjectView(R.id.ll_goods_detail)
    LinearLayout llGoodsDetail;
    @InjectView(R.id.tv_goods_config)
    TextView tvGoodsConfig;
    @InjectView(R.id.ll_goods_config)
    LinearLayout llGoodsConfig;
    @InjectView(R.id.v_tab_indicator_left)
    View vTabIndicatorLeft;
    @InjectView(R.id.v_tab_indicator_right)
    View vTabIndicatorRight;
    @InjectView(R.id.lv_goods_detail_left)
    ListView lvGoodsDetailLeft;

    @Override
    protected View getSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_detail, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    protected Object requestData() {
        return "";
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_goods_detail, R.id.ll_goods_config})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_goods_detail://左边图片详情
                break;
            case R.id.ll_goods_config://右边规格参数
                break;
        }
    }
}
