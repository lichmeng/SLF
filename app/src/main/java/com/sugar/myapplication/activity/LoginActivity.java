package com.sugar.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.LoginResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.global.SPContants;
import com.sugar.myapplication.util.SPUtil;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.et_account)
    EditText mEtAccount;
    @InjectView(R.id.ll_acount)
    LinearLayout mLlAcount;
    @InjectView(R.id.et_pwd)
    EditText mEtPwd;
    @InjectView(R.id.btn_login)
    Button mBtnLogin;
    @InjectView(R.id.tv_login_register)
    TextView mTvLoginRegister;
    @InjectView(R.id.activity_login)
    LinearLayout mActivityLogin;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.btn_login, R.id.tv_login_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.tv_login_register:
                //注册
                Register();
                break;
        }
    }

    private void Register() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 登录
     */
    private void login() {
        account = mEtAccount.getText().toString().trim();
        String pwd = mEtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "用户名密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpParams params = new HttpParams();
        params.put("username", account);
        params.put("password",pwd);
        MyListener listener = new MyListener();
        HttpLoader.getInstance(this).post(Api.URL_LOGIN, params, LoginResponse.class, Api.REQUEST_CODE_LOGIN,listener);

    }
    class MyListener implements HttpLoader.HttpListener{

        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {
            if (Api.REQUEST_CODE_LOGIN==requestCode){
                LoginResponse login = (LoginResponse) response;
                if ("error".equals(((LoginResponse) response).response)) {
                    ToastUtil.showToast("密码或者账户错误,请重试..");
                    return;
                }
                SPUtil.putString(LoginActivity.this, SPContants.USER_ID,login.userInfo.userid);
                App.userID = login.userInfo.userid;
                App.userName = account;
                finish();
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            ToastUtil.showToast("登录失败"+requestCode);
        }
    }

}
