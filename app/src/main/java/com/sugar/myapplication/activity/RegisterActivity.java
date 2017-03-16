package com.sugar.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sugar.myapplication.R;
import com.sugar.myapplication.bean.LoginResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @InjectView(R.id.et_register_account)
    EditText mEtRegisterAccount;
    @InjectView(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @InjectView(R.id.btn_register)
    Button mBtnRegister;
    @InjectView(R.id.tv_register_login)
    TextView mTvRegisterLogin;
    @InjectView(R.id.activity_login)
    LinearLayout mActivityLogin;
    @InjectView(R.id.btn_go_login)
    Button mBtnGoLogin;
    @InjectView(R.id.ll_register)
    LinearLayout mLlRegister;
    @InjectView(R.id.rl_register)
    RelativeLayout mRlRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_register, R.id.tv_register_login, R.id.btn_go_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_register_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_go_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                finish();
                break;
        }
    }

    private void register() {
        String account = mEtRegisterAccount.getText().toString().trim();
        String pwd = mEtRegisterPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpParams params = new HttpParams();
        params.put("username", account);
        params.put("password", pwd);
        MyListener listener = new MyListener();
        HttpLoader.getInstance(this).post(Api.URL_LOGIN, params, LoginResponse.class, Api.REQUEST_CODE_REGISTER, listener);
    }

    class MyListener implements HttpLoader.HttpListener {

        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {
            if (Api.REQUEST_CODE_REGISTER == requestCode) {
//                LoginResponse login = (LoginResponse) response;
//                SPUtil.putString(RegisterActivity.this, SPContants.USER_ID,login.userInfo.userid);
//                App.userID = login.userInfo.userid;
                mLlRegister.setVisibility(View.GONE);
                mRlRegister.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            ToastUtil.showToast("登录失败" + requestCode);
        }
    }
}
