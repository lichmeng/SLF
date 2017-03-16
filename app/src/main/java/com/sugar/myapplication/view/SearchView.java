package com.sugar.myapplication.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.ProductDetailCommend;
import com.sugar.myapplication.bean.ProductDetailDes;
import com.sugar.myapplication.bean.ProductResponse;
import com.sugar.myapplication.bean.SearchProductResponse;
import com.sugar.myapplication.bean.SearchRecommendResponse;
import com.sugar.myapplication.manager.ProductDetailManager;
import com.sugar.myapplication.manager.SearchManager;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.DrawableUtil;
import com.sugar.myapplication.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjx on 2016/11/23.
 */

public abstract class SearchView extends LinearLayout implements SearchManager.OnRequestFinishListener, ProductDetailManager.OnRequestProductListener {
    private static final int STATE_NULL = 0;
    private static final int STATE_HISTORY = 2;
    private int currentState = STATE_NULL;
    private View mNullView;
    private View mShowView;
    private LinearLayout mLlSearchClear;
    private LinearLayout mLlSearchHistory;
    private ArrayList<String> mData;
    private LinearLayout mLlSearchContainer;
    private FlowLayout flHotContainer;
    private ProductDetailManager manager;
    private FlowLayout flShowHot;

    public SearchView(Context context) {
        super(context);
        initView();
    }


    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //添加两个view
    private void initView() {
        manager = new ProductDetailManager();
        manager.setOnRequestProductListener(this);
        SearchManager.getInstance().setOnRequestFinishListener(this);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //添加热搜view
        if (mNullView == null) {
            mNullView = View.inflate(getContext(), R.layout.search_record_null, null);
            flHotContainer = (FlowLayout) mNullView.findViewById(R.id.fl_flow);
        }
        mNullView.setLayoutParams(params);
        addView(mNullView);
        if (mShowView == null) {
            mShowView = View.inflate(getContext(), R.layout.search_record_show, null);
            //flShowHot = (FlowLayout) mShowView.findViewById(R.id.fl_hot_show_container);
            mLlSearchHistory = (LinearLayout) mShowView.findViewById(R.id.ll_search_show);
            mLlSearchContainer = (LinearLayout) mShowView.findViewById(R.id.ll_search_container);
            mLlSearchClear = (LinearLayout) mShowView.findViewById(R.id.ll_search_clear);
            mLlSearchClear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLlSearchContainer.getChildCount() == 0) {
                        return;
                    }
                    mLlSearchContainer.removeAllViews();
                    Toast.makeText(getContext(), "清除成功", Toast.LENGTH_SHORT).show();
                    //移除所有的view
                    //listener.onClear();
                    SearchManager.getInstance().deleteRecord();
                    currentState = STATE_NULL;
                    queryDataFromDb();
                }
            });

        }
        mShowView.setLayoutParams(params);
        addView(mShowView);
        //显示
        showView();
        //从数据库中加载数据
        queryDataFromDb();
    }

    /**
     * 从数据库中查询数据
     */
    private void queryDataFromDb() {
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        final Object data = initData();
                        if (data != null) {
                            //manager.getSearchrecommend();
                            currentState = STATE_HISTORY;
                            showView();
                        } else {
                            manager.getSearchrecommend();
                            currentState = STATE_NULL;
                            showView();
                        }
                    }
                });
            }
        });

    }

    public abstract Object initData();

    private void showView() {
        switch (currentState) {
            case STATE_NULL:
                mNullView.setVisibility(View.VISIBLE);
                mShowView.setVisibility(View.GONE);
                break;
            case STATE_HISTORY:
                mNullView.setVisibility(View.GONE);
                mShowView.setVisibility(View.VISIBLE);
                break;
        }
    }

    //    private OnHistoryClearListener listener;
//    public void setOnHistoryClearListener(OnHistoryClearListener listener){
//        this.listener = listener;
//    }
//    public interface OnHistoryClearListener{
//        void onClear();
//    }
    @Override
    public void onRequestSuccess(ProductResponse productResponse) {

    }
    @Override
    public void onRequestSuccess(ProductDetailCommend productResponse) {

    }
    @Override
    public void onRequestSuccess(ProductDetailDes productResponse) {

    }

    /**
     * 当请求搜索推荐关键字成功
     * @param response
     */
    @Override
    public void onRequestSearchRecommendSuccess(SearchRecommendResponse response) {
        Log.e("-------------", "onRequestSearchRecommendSuccess: " );
        if (currentState==STATE_NULL) {
            showHot(response,flHotContainer);
        }else{
            showHot(response,flShowHot);
        }

    }

    private void showHot(SearchRecommendResponse response,FlowLayout flContainer) {
        TextView tv ;
        for (String keyword : response.searchKeywords) {
            tv = new TextView(getContext());
            tv.setBackgroundDrawable(DrawableUtil.generateDrawable(Color.rgb(231, 228, 228), 8));
            tv.setText(keyword);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.selector_text_product_color));
            flContainer.addView(tv);
        }
    }

    @Override
    public void onGetDataFromDb(List<String> list) {
        ToastUtil.showToast("成功");
        mData = (ArrayList<String>) list;
        TextView tv;
        TextView hotTv;
        for (String s : list) {
            tv = (TextView) View.inflate(getContext(), R.layout.search_record_show_tv, null);
            tv.setText(s);
            final TextView finalTv = tv;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchManager.getInstance().search(finalTv.getText().toString(), getContext());
                }
            });
            mLlSearchHistory.addView(tv);
            hotTv = (TextView) View.inflate(getContext(), R.layout.search_hot_tv, null);
            hotTv.setText(s);
            mLlSearchContainer.addView(hotTv);
        }
    }

    @Override
    public void onRequestFailed() {

    }

    @Override
    public void onRequestFinish(SearchProductResponse productResponse) {

    }
}
