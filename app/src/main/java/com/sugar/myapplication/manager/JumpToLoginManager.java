package com.sugar.myapplication.manager;

import android.content.Context;
import android.content.Intent;

import com.sugar.myapplication.App;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.welcome.WelcomeActivity;

/**
 * Created by wjx on 2016/11/25.
 */

public class JumpToLoginManager {
    /**
     * 跳转到登录页面
     */
    public static boolean isJumpToLogin(Context context){
        if (App.userID==null){
            ToastUtil.showToast("需要先登录");
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.putExtra("onlyLogin",1);
            context.startActivity(intent);
            return true;
        }else{
            return false;
        }
    }
    public static boolean isJumpToRegister(Context context){
        if (App.userID==null){
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.putExtra("onlyLogin",1);
            context.startActivity(intent);
            return true;
        }else{
            return false;
        }
    }
}
