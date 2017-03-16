package com.sugar.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.TopicPListAdapter;
import com.sugar.myapplication.adapter.TopicPListRvAdapter;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.bean.TopicPListResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.view.NestedScrollingListView;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.util.List;

public class TopicPListActivity extends AppCompatActivity implements HttpLoader.HttpListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private NestedScrollingListView mListView;
//    private List<ProductListInfo> mList = new ArrayList<>();

    private boolean isDown;
    private String orderby = "saleDown";
    private String mId;
    private TopicPListAdapter mAdapter;
    private List<ProductListInfo> mProductList;
    private RecyclerView mRecyclerView;
    private ImageButton mBtnListView;
    private ImageButton mBtnGridView;
    private TextView mTvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getStringExtra("id");
        setContentView(R.layout.activity_topic_plist);
        setToolBar();
        initView();
        requestData();

    }

    private void initView() {
        //
        mTvSearch = (TextView) findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(this);
        //
        mListView = (NestedScrollingListView) findViewById(R.id.lv_topic_plist);
        //
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_topic_plist);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        //
        mBtnListView = (ImageButton) findViewById(R.id.btn_listview);
        mBtnGridView = (ImageButton) findViewById(R.id.btn_gridview);
        mBtnListView.setOnClickListener(this);
        mBtnGridView.setOnClickListener(this);
        //
        RadioGroup rgOrder = (RadioGroup) findViewById(R.id.rg_order);
        rgOrder.setOnCheckedChangeListener(this);
        RadioButton rbPrice = (RadioButton) findViewById(R.id.rb_price);
        rbPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDown) {
                    orderby = "priceUp";
                } else {
                    orderby = "priceDown";
                }
                isDown = !isDown;
                requestData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopicPListActivity.this, GoodsInfoActivity.class);
                intent.putExtra("id", mProductList.get(position).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.silde_bottom_in,R.anim.silde_top_out);
            }
        });

    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void requestData() {
        HttpParams params = new HttpParams();
        params.put("page", "1");
        params.put("pageNum", "20");
        params.put("id", mId);
        params.put("orderby", orderby);
        App.mLoader.get(Api.URL_TOPIC_LIST, params, TopicPListResponse.class, Api.REQUEST_CODE_PROMOTIONS_PLIST, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.silde_bottom_out, R.anim.silde_top_in);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        if (requestCode == Api.REQUEST_CODE_PROMOTIONS_PLIST) {
            TopicPListResponse listResponse = (TopicPListResponse) response;
            mProductList = listResponse.getProductList();
            if (mProductList != null) {
//                mList.addAll(productList);
                mAdapter = new TopicPListAdapter(mProductList);
                mListView.setAdapter(mAdapter);
                mRecyclerView.setAdapter(new TopicPListRvAdapter(mProductList));
                //mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sale_down:
                orderby = "saleDown";
                break;
            case R.id.rb_price:
                orderby = "priceUp";
                isDown = true;
                break;
            case R.id.rb_comment_down:
                orderby = "priceDown";
                break;
            case R.id.rb_shelves_down:
                orderby = "shelvesDown";
                break;
        }
        requestData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.silde_bottom_out, R.anim.silde_top_in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_listview:
                mBtnGridView.setVisibility(View.VISIBLE);
                mBtnListView.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_gridview:
                mBtnGridView.setVisibility(View.INVISIBLE);
                mBtnListView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
