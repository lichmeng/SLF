package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

/**
 * Created by Administrator on 2016/11/23.
 */
//根据用户个人的购物车数据库,封装成的bean,用于访问服务器的商品详情
public class MyCart implements IResponse {

    public int id;
    public int num;
    public int product_property_id;
    public int ischeck;

}
