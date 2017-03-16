package com.sugar.myapplication.welcome.fragment.outlayer.loginlayer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.MainActivity;
import com.sugar.myapplication.bean.LoginResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.global.SPContants;
import com.sugar.myapplication.util.SPUtil;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;


/**
 * 登录
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPsw;
    //private WelcomeActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //activity = (WelcomeActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_new_login, null);
        TextView tv_down = (TextView)view.findViewById(R.id.tv_down);
        TextView tv_no_login = (TextView)view.findViewById(R.id.tv_no_login);
        tv_no_login.setOnClickListener(this);
        TextView tvWelcomeLogin = (TextView)view.findViewById(R.id.tv_welcome_login);
        tvWelcomeLogin.setOnClickListener(this);
        etUserName = (EditText) view.findViewById(R.id.et_phone);
        etPsw = (EditText) view.findViewById(R.id.et_psw);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_welcome_login:
                String account = etUserName.getText().toString().trim();
                String psw = etPsw.getText().toString().trim();
                if (TextUtils.isEmpty(account)||TextUtils.isEmpty(psw)){
                    ToastUtil.showToast("用户名密码不能为空");
                    return;
                }
                HttpParams params = new HttpParams();
                params.put("username",account);
                params.put("password",psw);
                MyListener listener = new MyListener();
                HttpLoader.getInstance(getActivity()).post(Api.URL_LOGIN, params, LoginResponse.class, Api.REQUEST_CODE_LOGIN,listener);
                break;
            case R.id.tv_no_login:
                SPUtil.putString(getActivity(), SPContants.USER_ID,null);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);

                getActivity().finish();
                //activity.finish();
                break;
        }

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
                SPUtil.putString(getActivity(), SPContants.USER_ID,login.userInfo.userid);
                App.userID = login.userInfo.userid;
                App.userName = etUserName.getText().toString().trim();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            ToastUtil.showToast("登录失败"+requestCode);
        }
    }
    /**
     * 下载正式版小红书
     */
    private void gotoRankMe() {
        try {
            Uri uri = Uri.parse("market://details?id=com.xingin.xhs");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }


}
