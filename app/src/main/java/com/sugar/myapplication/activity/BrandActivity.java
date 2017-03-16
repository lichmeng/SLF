package com.sugar.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.BrandAdapter;
import com.sugar.myapplication.bean.RecommendedBrandResponse;
import com.sugar.myapplication.global.Api;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.resp.IResponse;

import java.util.ArrayList;
import java.util.List;

public class BrandActivity extends AppCompatActivity implements HttpLoader.HttpListener {

    private List<Object> mList = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        setToolBar();
        mListView = (ListView) findViewById(R.id.lv_brand);
        mListView.setDividerHeight(0);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = mList.get(position);
                if (obj instanceof RecommendedBrandResponse.BrandBean.ValueBean) {
                    RecommendedBrandResponse.BrandBean.ValueBean info = (RecommendedBrandResponse.BrandBean.ValueBean) obj;
                    Intent intent = new Intent(BrandActivity.this, BrandPListActivity.class);
                    intent.putExtra("id", ""+info.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.silde_bottom_in,R.anim.silde_top_out);
                }
            }
        });
        requestData();
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void requestData() {
        App.mLoader.get(Api.URL_RECOMMENDED_BRAND, null, RecommendedBrandResponse.class, Api.REQUEST_CODE_RECOMMENDED_BRAND, this).setTag("tag");
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        if (requestCode == Api.REQUEST_CODE_RECOMMENDED_BRAND) {
            RecommendedBrandResponse brandResponse = (RecommendedBrandResponse) response;
            List<RecommendedBrandResponse.BrandBean> brands = brandResponse.getBrand();
            if (brands != null) {
                mList.clear();
                for (RecommendedBrandResponse.BrandBean brand : brands) {
                    String title = brand.getKey();
                    List<RecommendedBrandResponse.BrandBean.ValueBean> infos = brand.getValue();
                    if (infos.size() == 0) {
                        continue;
                    }
                    //mList.add(infos);
                    mList.add(title);
                    mList.addAll(infos);
                }
                BrandAdapter adapter = new BrandAdapter(mList);
                mListView.setAdapter(adapter);
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
    protected void onDestroy() {
        super.onDestroy();
        App.mLoader.cancelRequest(this);
    }
}
