package com.sugar.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.LimitBuyAdapter;
import com.sugar.myapplication.adapter.LimitBuyPagerAdapter;
import com.sugar.myapplication.bean.LimitBuyReaponse;
import com.sugar.myapplication.global.Api;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LimitBuyActivity extends AppCompatActivity implements HttpLoader.HttpListener {
//    private List<LimitBuyReaponse.ProductListBean> mList = new ArrayList<>();
    private ListView mListView;
    private List<LimitBuyReaponse.ProductListBean> mProductList;
    private int mPosition;
    private ViewPager mViewPager;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++currentItem);

            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };
    private TextView mTvLeftTime;
    private int time = 5*60*60*1000;
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long threadTimeMillis = SystemClock.currentThreadTimeMillis();
            time -= 1000;
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String leftTime = format.format(date);
            mTvLeftTime.setText(leftTime);
            mTimeHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };
    private LimitBuyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_buy);
        setToolBar();
        initView();
        requestData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_limit_buy);
        mPosition = getIntent().getIntExtra("position", 0);
        //header
        View header = View.inflate(this, R.layout.header_limit_buy, null);
        mViewPager = (ViewPager) header.findViewById(R.id.vp_header);
        mTvLeftTime = (TextView) header.findViewById(R.id.tv_left_time);
        mListView.addHeaderView(header);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LimitBuyReaponse.ProductListBean product = mProductList.get(position);
                Intent intent = new Intent(LimitBuyActivity.this, GoodsInfoActivity.class);
                intent.putExtra("id", product.getId());
                startActivity(intent);
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
        App.mLoader.get(Api.URL_LIMITBUY, params, LimitBuyReaponse.class, Api.REQUEST_CODE_LIMITBUY, this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        if (requestCode == Api.REQUEST_CODE_LIMITBUY) {
            LimitBuyReaponse limitBuyReaponse = (LimitBuyReaponse) response;
            mProductList = limitBuyReaponse.getProductList();
            if (mProductList != null) {
//                mList.addAll(mProductList);
                mAdapter = new LimitBuyAdapter(mProductList);
                mListView.setAdapter(mAdapter);
                mListView.setSelection(mPosition);
                mViewPager.setAdapter(new LimitBuyPagerAdapter());
                mViewPager.setCurrentItem(4000);
                //adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.sendEmptyMessageDelayed(0, 3000);
        mTimeHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        mTimeHandler.removeCallbacksAndMessages(null);
    }
}
