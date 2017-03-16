package com.sugar.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.PromotionsAdapter;
import com.sugar.myapplication.bean.PromotionsResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.CommonUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.util.List;

public class PromotionsActivity extends AppCompatActivity implements HttpLoader.HttpListener {

//    private List<PromotionsResponse.TopicBean> mList = new ArrayList<>();

    private List<PromotionsResponse.TopicBean> mTopic;
    private GridView mGridView;
    private PullToRefreshGridView mRefreshGridView;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        setToolBar();
        initView();
        requestData();
    }

    private void initView() {
        //
        mRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gv_promotions);
        mRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mGridView = mRefreshGridView.getRefreshableView();
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PromotionsActivity.this, TopicPListActivity.class);
                int pid = mTopic.get(position).getId();
                if (pid > 3) {
                    pid = 3;
                }
                intent.putExtra("id", pid +"");
                startActivity(intent);
                overridePendingTransition(R.anim.silde_bottom_in,R.anim.silde_top_out);
            }
        });

        mRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                if (mRefreshGridView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    CommonUtil.runOnThread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    requestData();
                                }
                            });
                        }
                    });
                }
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
        params.put("page", page+"");
        params.put("pageNum", "8");
        App.mLoader.get(Api.URL_TOPIC, params, PromotionsResponse.class, Api.REQUEST_CODE_PROMOTIONS, this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        switch (requestCode) {
            case Api.REQUEST_CODE_PROMOTIONS:
                PromotionsResponse promotionsResponse = (PromotionsResponse) response;
                mTopic = promotionsResponse.getTopic();
                if (mTopic != null) {
                    PromotionsAdapter adapter = new PromotionsAdapter(mTopic);
                    mGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                mRefreshGridView.onRefreshComplete();
                break;
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
    public void onBackPressed() {
        page--;
        if (page >= 0) {
            requestData();
        }else {
            finish();
        }

    }

}
