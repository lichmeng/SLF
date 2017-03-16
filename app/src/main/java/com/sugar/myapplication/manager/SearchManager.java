package com.sugar.myapplication.manager;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.activity.ProductListActivity;
import com.sugar.myapplication.bean.SearchProductResponse;
import com.sugar.myapplication.bean.SearchRecord;
import com.sugar.myapplication.db.MySqliteDataBase;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.util.List;

import static com.sugar.myapplication.App.mContext;

/**
 * Created by wjx on 2016/11/24.
 */

public class SearchManager {
    private static SearchManager mSearchManager = new SearchManager();
    private final MySqliteDataBase mDb;

    private SearchManager(){
        mDb = new MySqliteDataBase();
    }
    public static SearchManager getInstance(){
        return mSearchManager;
    }
    public void search(String keyword){
        if (keyword == null) {
            return;
        }
        HttpParams params = new HttpParams();
        params.put("keyword", "奶粉");
        params.put("page", 1+"");
        params.put("pageNum",10+"");
        params.put("orderby", "priceUp");
        MyListener mListener =  new MyListener();
        HttpLoader.getInstance(mContext).get(Api.URL_SEARCH, params, SearchProductResponse.class, Api.REQUEST_CODE_SEARCH, mListener);
    }

    /**
     * 从数据库获取数据
     * @return
     */
    public Object searchFromDB(){
        if (App.userID != null) {
            List<String> query = mDb.query(App.userID);
            if (query.size()>0) {
                if (listener != null) {
                    listener.onGetDataFromDb(query);
                }
                return query;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 将搜索记录存入数据库中
     * @param name
     */
    public void saveSearchRecord(String name){
        if (TextUtils.isEmpty(name)) {
            return;
        }
       final SearchRecord record = new SearchRecord();
        record.setSearchName(name);
        record.setSearchTime((int) SystemClock.currentThreadTimeMillis());
        record.setSearchCount(1);
        if (App.userID != null) {
            record.setUserId(App.userID);
        }else{
            record.setUserId("0");
        }
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                mDb.addSearchRecord(record);
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("保存成功");

                    }
                });
            }
        });
    }

    /**
     * 根据userid删除搜索记录
     */
    public void deleteRecord(){
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                if (App.userID!=null){
                    mDb.delete(App.userID);
                }
            }
        });
    }
    /**
     * 携带数据跳转到显示商品信息页面
     */
    public void search(String name,Context context){
        //保存搜索记录
        saveSearchRecord(name);
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra("keyword",name);
        context.startActivity(intent);

    }
    class MyListener implements HttpLoader.HttpListener {

        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {
            ToastUtil.showToast("请求成功");
            SearchProductResponse productResponse = (SearchProductResponse) response;
            if (listener != null) {
                listener.onRequestFinish(productResponse);
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {

        }
    }
    private OnRequestFinishListener listener;
    public void setOnRequestFinishListener(OnRequestFinishListener listener){
        this.listener = listener;
    }
    public interface OnRequestFinishListener{
        /**
         * 当搜索完成获得商品信息数据，回调该方法
         * @param productResponse
         */
        void onRequestFinish(SearchProductResponse productResponse);

        /**
         * 当从数据库请求到数据回调该方法
         * @param list
         */
        void onGetDataFromDb(List<String> list);
    }
}
