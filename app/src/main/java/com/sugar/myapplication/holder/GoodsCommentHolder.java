package com.sugar.myapplication.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.ProductDetailCommend;

import org.senydevpkg.base.BaseHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wjx on 2016/11/25.
 */

public class GoodsCommentHolder extends BaseHolder<ProductDetailCommend.CommentBean> {
    @InjectView(R.id.tv_comment_name)
    TextView tvCommentName;
    @InjectView(R.id.tv_comment_content)
    TextView tvCommentContent;
    @InjectView(R.id.ib_comment_session)
    ImageButton ibCommentSession;
    @InjectView(R.id.ib_comment_like)
    ImageButton ibCommentLike;
    @InjectView(R.id.tv_comment_time)
    TextView tvCommentTime;

    public GoodsCommentHolder(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_goods_comment, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void bindData(ProductDetailCommend.CommentBean data) {
        tvCommentName.setText(data.username);
        tvCommentContent.setText(data.content);
        tvCommentTime.setText(data.time);
    }

    @OnClick({R.id.ib_comment_session, R.id.ib_comment_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_comment_session:
                break;
            case R.id.ib_comment_like:
                break;
        }
    }
}
