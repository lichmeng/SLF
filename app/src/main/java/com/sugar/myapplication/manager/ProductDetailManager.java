package com.sugar.myapplication.manager;

import android.util.Log;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.bean.ProductDetailCommend;
import com.sugar.myapplication.bean.ProductDetailDes;
import com.sugar.myapplication.bean.ProductResponse;
import com.sugar.myapplication.bean.SearchRecommendResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

/**
 * Created by wjx on 2016/11/25.
 * 请求商品详情页数据的管理类
 */

public class ProductDetailManager {

    private ProductResponse productResponse;

    /**
     * @return ProductDetailManager对象
     */

    /**
     * 商品详情和描述数据
     * @param pId
     */
    public void getProductDetail(int pId,String url,int code) {
        if (pId<=0){
            if (listener != null) {
                listener.onRequestFailed();
            }
            return;
        }
        HttpParams params = new HttpParams();
        params.put("pId", pId + "");
        MyListener listener = new MyListener();
        HttpLoader.getInstance(App.mContext).get(url,
                params,
                code==Api.REQUEST_CODE_PRODUCT_DETAIL?ProductResponse.class:ProductDetailDes.class,
                code, listener);
        ToastUtil.showToast("成功");
    }
    /**
     * 商品详情和描述数据
     * @param pId
     */
    public void getProductCommend(int pId,String url,int page,int pageNum) {
        if (pId<=0){
            if (listener != null) {
                listener.onRequestFailed();
            }
            return;
        }
        HttpParams params = new HttpParams();
        params.put("pId", pId + "");
        params.put("page", page + "");
        params.put("pageNum", pageNum + "");
        MyListener listener = new MyListener();
        HttpLoader.getInstance(App.mContext).get(url, params, ProductDetailCommend.class,
                Api.REQUEST_CODE_PRODUCT_DETAIL, listener);

    }

    /**
     * 获取搜索推荐关键字
     */
    public void getSearchrecommend(){
        MyListener listener = new MyListener();
        HttpLoader.getInstance(App.mContext).get(Api.URL_RECOMMEND, null,SearchRecommendResponse.class,Api.REQUEST_CODE_SEARCH_RECOMMEND,listener);
    }
    class MyListener implements HttpLoader.HttpListener {

        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {
            Log.e("----------------", "onGetResponseSuccess: "+requestCode );
            if (requestCode == Api.REQUEST_CODE_PRODUCT_DETAIL) {
                if (listener != null) {
                    productResponse = (ProductResponse) response;
                    Log.e("-----1-----------", "onGetResponseSuccess: "+productResponse.product.name );
                        listener.onRequestSuccess(productResponse);

                }
            }else if (requestCode==Api.REQUEST_CODE_PRODUCT_DES){
                productResponse = (ProductResponse) response;
                if (listener != null) {
                    ProductDetailDes productDetailDes = (ProductDetailDes) response;
                    Log.e("-----2-----------", "onGetResponseSuccess: "+productResponse );
                        listener.onRequestSuccess(productDetailDes);
                }
            }else if (requestCode==Api.REQUEST_CODE_PRODUCT_COMMEND){
                if (listener != null) {
                    ProductDetailCommend productDetailCommend = (ProductDetailCommend) response;
                    Log.e("-----3-----------", "onGetResponseSuccess: "+productResponse );
                        listener.onRequestSuccess(productDetailCommend);
                }
            }else if (requestCode==Api.REQUEST_CODE_SEARCH_RECOMMEND){
                if (listener!=null){
                    SearchRecommendResponse searchRecommendResponse = (SearchRecommendResponse) response;
                    listener.onRequestSearchRecommendSuccess(searchRecommendResponse);
                }
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            Log.e("-------error---------", "error: "+error );
        }
    }

    private OnRequestProductListener listener;
    public void setOnRequestProductListener(OnRequestProductListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }
    public interface OnRequestProductListener {
        /**
         * 当请求商品详情成功回调该方法
         *
         * @param response
         */
        void onRequestSuccess(ProductResponse response);
        void onRequestSuccess(ProductDetailDes response);
        void onRequestSuccess(ProductDetailCommend response);
        void onRequestSearchRecommendSuccess(SearchRecommendResponse response);
        /**
         * 当请求商品详情失败回调该方法
         */
        void onRequestFailed();
    }
}
