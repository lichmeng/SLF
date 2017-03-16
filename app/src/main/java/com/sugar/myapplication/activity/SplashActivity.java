package com.sugar.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.VersionInfo;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.global.SPContants;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.LogUtil;
import com.sugar.myapplication.util.SPUtil;
import com.sugar.myapplication.welcome.WelcomeActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class SplashActivity extends Activity {

    @InjectView(R.id.tv_splash_time)
    TextView mTvSplashTime;
    private Handler handler = new Handler();
    private MyCountDownTimer mc;
    private String isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        isLogin = SPUtil.getString(this, SPContants.USER_ID, "");
        mc = new MyCountDownTimer(5000, 1000);
        mc.start();

        /**
         * 开启子线程检测新版本
         */
        checkVersion(Api.URL_UPDATA);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(isLogin)){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }
                finish();
            }
        }, 5000);


    }

    /**
     * 检测是否有新版本,
     * @param path
     */
    private void checkVersion(final String path) {
        System.out.println("升级url: " + path);
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    int code = conn.getResponseCode();
                    InputStream is = null;
                    if (code == 200) {//连接成功
                        is = conn.getInputStream();
                        String json = CommonUtil.streamReader(is);
                        Gson gson = new Gson();
                        VersionInfo versionInfo = gson.fromJson(json, VersionInfo.class);
                        if (!versionInfo.version.equals(CommonUtil.getVersion(getPackageName()))) {
                            //如果本地和服务器的versionName不一致,说明有新版本
                            // 通知新版本下载的地址.
                            EventBus.getDefault().postSticky(versionInfo);
                            LogUtil.e(this,"发现新版本");
                        } else  {
                            LogUtil.e(this,"现在是最新版了");
                        }
                    } else {
                        LogUtil.e(this,"splashing,upgrade,network error!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 倒数计时
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            mTvSplashTime.setText("正在跳转");
        }

        public void onTick(long millisUntilFinished) {
            mTvSplashTime.setText("跳过(" + millisUntilFinished / 1000 + ")");
        }
    }


    @OnClick(R.id.tv_splash_time)
    public void onClick() {
        if (!TextUtils.isEmpty(isLogin)){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            finish();
        }
        handler.removeCallbacksAndMessages(null);
        finish();
    }
}
