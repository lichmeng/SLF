package com.sugar.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AboutActivity extends Activity {

    @InjectView(R.id.tv_app_version)
    TextView mTvAppVersion; //版本号
    @InjectView(R.id.iv_back_about)
    ImageView mIvBackAbout; //后退按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_app);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.iv_back_about)
    public void onClick() {
        finish();
    }
}
