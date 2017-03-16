package com.sugar.myapplication.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sugar.myapplication.R;
import com.sugar.myapplication.util.CommonUtil;

/**
 * Created by sugar on 2016/11/22.
 */

public class DiscoveryFragment extends BaseFragment {

    private WebView mWebView;

    @Override
    protected View getSuccessView() {
        /*TextView tv = new TextView(App.mContext);
        tv.setText("发现");
        tv.setTextColor(Color.RED);

        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(26);
        return tv;*/
        View view = View.inflate(getActivity(), R.layout.fragment_discovery, null);
        mWebView = (WebView) view.findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        return view;
    }

    @Override
    protected Object requestData() {
        final String url = "http://c.m.suning.com/channel/snhgsale.html?tIndex=0";
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(url);
            }
        });
        return url;
    }
}
