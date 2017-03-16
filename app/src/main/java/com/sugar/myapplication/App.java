package com.sugar.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.sugar.myapplication.dao.SQLHelper;
import com.sugar.myapplication.global.SPContants;
import com.sugar.myapplication.util.SPUtil;

import org.senydevpkg.net.HttpLoader;

/**
 * Created by sugar on 2016/11/21.
 */

public class App extends Application {
    public static App mAppApplication;
    public static Context mContext;
    public static HttpLoader mLoader;
    public static Handler mHandler;
    public static String userID =null;
    public static String userName ="123";
    private SQLHelper sqlHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication=this;
        mContext = this;
        mLoader = HttpLoader.getInstance(this);
        mHandler = new Handler();
        userID = SPUtil.getString(this, SPContants.USER_ID,null);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
//                startActivity(new Intent(App.this, MainActivity.class));
               // android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    /** 获取Application */
    public static App getApp() {
        return mAppApplication;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    /** 摧毁应用进程时候调用 */
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }
}
