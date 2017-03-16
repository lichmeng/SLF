package com.sugar.myapplication.welcome.fragment.outlayer.loginlayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.MainActivity;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.welcome.WelcomeActivity;


/**
 * 注册
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText etRegisterName;
    private EditText etPutIdentify;
    private Button btn_next;
    private Button btn_login;
    private WelcomeActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WelcomeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_register, null);
        etRegisterName = (EditText) view.findViewById(R.id.et_write_phone);
        etPutIdentify = (EditText) view.findViewById(R.id.et_put_identify);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_next.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next://发送
                String account = etRegisterName.getText().toString().trim();
                if (TextUtils.isEmpty(account)){
                    ToastUtil.showToast("手机号码不能为空");
                    return;
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                activity.finish();
                break;
            case R.id.btn_login://验证并登录
                if (isEmpty()) {
                    ToastUtil.showToast("登录成功");
                    Intent intent2 = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent2);
                    activity.finish();
                }
                break;
        }
    }
    public boolean isEmpty(){
        String account = etRegisterName.getText().toString().trim();
        String code = etPutIdentify.getText().toString().trim();
        if (TextUtils.isEmpty(account)||TextUtils.isEmpty(code)){
            ToastUtil.showToast("用户名验证码不能为空");
            return false;
        }
        return true;
    }
}
