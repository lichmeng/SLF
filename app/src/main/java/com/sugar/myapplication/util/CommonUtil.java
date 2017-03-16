package com.sugar.myapplication.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.sugar.myapplication.App;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.pm.PackageManager.GET_INSTRUMENTATION;

public class CommonUtil {
    /**
     * 在主线程执行一段任务
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        App.mHandler.post(r);
    }

    /**
     * 在子线程执行一段任务
     *
     * @param r
     */
    public static void runOnThread(Runnable r) {
        new Thread(r) {
        }.start();
    }

    /**
     * 移除子View
     *
     * @param child
     */
    public static void removeSelfFromParent(View child) {
        if (child != null) {
            ViewParent parent = child.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(child);//移除子View
            }
        }
    }

    public static Drawable getDrawable(int id) {
        return App.mContext.getResources().getDrawable(id);
    }

    public static String getString(int id) {
        return App.mContext.getResources().getString(id);
    }

    public static String[] getStringArray(int id) {
        return App.mContext.getResources().getStringArray(id);
    }

    public static int getColor(int id) {
        return App.mContext.getResources().getColor(id);
    }

    /**
     * 获取dp资源，并且会自动将dp值转为px值
     *
     * @param id
     * @return
     */
    public static int getDimens(int id) {
        return App.mContext.getResources().getDimensionPixelSize(id);
    }

    public static String streamReader(InputStream is) {
        String result = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;

            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = new String(baos.toByteArray(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getVersion(String pkg) {
        PackageManager pm = App.mContext.getPackageManager();
        try {
            String versionName = pm.getPackageInfo(pkg, GET_INSTRUMENTATION).versionName;
            System.out.println("commonutil:  --version --" + versionName);
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
