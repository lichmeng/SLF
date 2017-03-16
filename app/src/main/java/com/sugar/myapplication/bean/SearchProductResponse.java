package com.sugar.myapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class SearchProductResponse implements IResponse{
    /**
     * productList : [{"id":30,"marketPrice":200,"name":"超凡奶粉","pic":"/images/product/detail/q26.jpg","price":160},{"id":31,"marketPrice":260,"name":"天籁牧羊奶粉","pic":"/images/product/detail/q26.jpg","price":200}]
     * response : search
     */

    public String response;
    /**
     * id : 30
     * marketPrice : 200
     * name : 超凡奶粉
     * pic : /images/product/detail/q26.jpg
     * price : 160
     */

    public List<ProductList> productList;

    public static class ProductList {
        public int id;
        public int marketPrice;
        public String name;
        public String pic;
        public int price;
    }
}
