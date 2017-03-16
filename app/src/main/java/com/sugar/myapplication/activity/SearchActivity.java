package com.sugar.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.sugar.myapplication.R;
import com.sugar.myapplication.manager.SearchManager;
import com.sugar.myapplication.view.ResultSearchView;
import com.sugar.myapplication.view.SearchView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends AppCompatActivity{


    @InjectView(R.id.resultView)
    ResultSearchView mResultView;
    @InjectView(R.id.fl_container)
    FrameLayout mFlContainer;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主要负责显示没有搜索记录和有搜索记录
        mSearchView = new SearchView(this) {

            @Override
            public Object initData() {
                return requestData();
            }
        };
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        mFlContainer.addView(mSearchView);
        SearchManager.getInstance().setOnRequestFinishListener(mSearchView);
        //mResultView.setOnSearchingListener(this);

    }

    /**
     * 从数据库请求数据
     *
     * @return
     */
    private Object requestData() {
        return SearchManager.getInstance().searchFromDB();
    }

//    /**
//     * 当用户正在搜索的回调
//     *
//     * @param searchName
//     */
//    @Override
//    public void onSearching(String searchName) {
//        mResultView.setAdapter(new SearchResultAdapter(this, mQuery));
//    }
}
