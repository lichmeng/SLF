package com.sugar.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.ProductAdapter;
import com.sugar.myapplication.bean.SearchProductResponse;
import com.sugar.myapplication.manager.SearchManager;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class ResultSearchView extends LinearLayout implements View.OnClickListener , SearchManager.OnRequestFinishListener {

    private View mView;
    private EditText mEt_search;
    private ImageButton mIbMic;
    private ImageButton mIbClear;
    private ListView mLvSearch;
    private OnSearchingListener listener;
    private TextView mTvSearch;
    private ImageButton mIbBack;
    private Context mContext;

    public ResultSearchView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ResultSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    public ResultSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }



    public interface OnSearchingListener {
        void onSearching(String searchName);
    }

    public void setOnSearchingListener(OnSearchingListener listener) {
        this.listener = listener;
    }

    private void initView() {
        SearchManager.getInstance().setOnRequestFinishListener(this);
        mView = View.inflate(getContext(), R.layout.search_layout, null);
        addView(mView);
        mTvSearch = (TextView) mView.findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(this);
        mIbBack = (ImageButton) mView.findViewById(R.id.ib_search_back);
        mIbBack.setOnClickListener(this);
        mEt_search = (EditText) mView.findViewById(R.id.et_search);
        mEt_search.addTextChangedListener(new MyTextChangeListener());
        mEt_search.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        mIbMic = (ImageButton) mView.findViewById(R.id.ib_search_mic);
        mIbClear = (ImageButton) mView.findViewById(R.id.ib_search_clear);
        mIbClear.setOnClickListener(this);
        mLvSearch = (ListView) mView.findViewById(R.id.lv_search_result);
        mLvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Activity) mContext).finish();

            }
        });
    }

    /**
     * 搜索的方法
     */
    public void search(String keyword) {
        SearchManager.getInstance().search(keyword);
    }

    /**
     * 点击搜索按钮或者点击条目跳转到显示搜索结果的页面
     */
    public void search(){
            SearchManager.getInstance().search(mEt_search.getText().toString().trim(),getContext());
    }
    /**
     * 数据请求完毕回调
     * @param productResponse
     */
    @Override
    public void onRequestFinish(SearchProductResponse productResponse) {
        //把数据设置给listview
        setAdapter(new ProductAdapter(getContext(),productResponse.productList));
}

    @Override
    public void onGetDataFromDb(List<String> list) {

    }

    //给listview设置adapter
    public void setAdapter(BaseAdapter adapter) {
        mLvSearch.setAdapter(adapter);
    }

    class MyTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length()>0){
                mIbMic.setVisibility(View.INVISIBLE);
                mIbClear.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String result = s.toString().trim();
            if (result.length()>0){
                //当用户输入完毕访问网络请求数据
               search(result);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search://搜索
                //Intent intent = new Intent();
                search();
                ((Activity) mContext).finish();
                break;
            case R.id.ib_search_back://返回
                Activity activity = (Activity) mContext;
                activity.finish();
                break;
            case R.id.ib_search_clear://清空et内容
                mEt_search.setText("");
                mIbMic.setVisibility(View.INVISIBLE);
                mIbClear.setVisibility(View.VISIBLE);
                break;
        }
    }
}
