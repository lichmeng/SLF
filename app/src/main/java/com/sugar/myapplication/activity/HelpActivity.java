package com.sugar.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sugar.myapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HelpActivity extends Activity {


    @InjectView(R.id.iv_back_helpcenter)
    ImageView mIvBackHelpcenter;
    @InjectView(R.id.rv_shoppingguide_help)
    RelativeLayout mRvShoppingguideHelp;
    @InjectView(R.id.rv_soldserver_hel)
    RelativeLayout mRvSoldserverHel;
    @InjectView(R.id.rv_diliverway_help)
    RelativeLayout mRvDiliverwayHelp;
    @InjectView(R.id.rv_opinion_help)
    RelativeLayout mRvOpinionHelp;
    @InjectView(R.id.rl_servenum_helpcenter)
    RelativeLayout mRlServenumHelpcenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.inject(this);
    }


    @OnClick({R.id.iv_back_helpcenter, R.id.rv_shoppingguide_help, R.id.rv_soldserver_hel, R.id.rv_diliverway_help, R.id.rv_opinion_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_helpcenter:
                finish();
                break;
            case R.id.rv_shoppingguide_help:
                startActivity(new Intent(this, ShoppingGuide.class));
                break;
            case R.id.rv_soldserver_hel:
                startActivity(new Intent(this, ShoppingGuide.class));
                break;
            case R.id.rv_diliverway_help:
                startActivity(new Intent(this, ShoppingGuide.class));
                break;
            case R.id.rv_opinion_help:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;

        }
    }

    @OnClick(R.id.rl_servenum_helpcenter)
    public void onClick() {
        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:222222"));
        startActivity(intent);

    }
}
