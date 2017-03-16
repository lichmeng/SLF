package com.sugar.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.TopicPListAdapter;
import com.sugar.myapplication.bean.HotProductResponse;
import com.sugar.myapplication.bean.NewProductResponse;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.bean.TopicPListResponse;
import com.sugar.myapplication.global.Api;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.resp.IResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2016/11/25.
 */

public abstract class BaseActivity4Home extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, HttpLoader.HttpListener {

    protected ListView mListView;
    protected List<ProductListInfo> mList = new ArrayList<>();
    protected boolean isDown;
    protected String orderby = "saleDown";
    protected String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_plist);
        setToolBar();
        initView();
    }

    protected void initView() {
        mListView = (ListView) findViewById(R.id.lv_topic_plist);
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
        productId = getIdFromIntent();
        requestData();
    }

    protected abstract void requestData();

    protected String getIdFromIntent() {
        return null;
    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setToolBarTitle(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract void setToolBarTitle(Toolbar toolbar);

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        List<ProductListInfo> productList = null;
        switch (requestCode) {
            case Api.REQUEST_CODE_PROMOTIONS_PLIST:
                TopicPListResponse topicResponse = (TopicPListResponse) response;
                productList = topicResponse.getProductList();
                break;
            case Api.REQUEST_CODE_NEW_PRODUCT:
                NewProductResponse newProductResponse = (NewProductResponse) response;
                productList = newProductResponse.getProductList();
                break;
            case Api.REQUEST_CODE_HOT_PRODUCT:
                HotProductResponse hotProductResponse = (HotProductResponse) response;
                productList = hotProductResponse.getProductList();
                break;
        }
        if (productList != null) {
            mList.clear();
            mList.addAll(productList);
            TopicPListAdapter adapter = new TopicPListAdapter(mList);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
