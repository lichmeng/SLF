package com.sugar.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wjx on 2016/11/23.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    public MyOpenHelper(Context context) {
        super(context, "redbady.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建搜索记录表-id,userid，搜索名称，搜索次数，搜索时间
        db.execSQL("create table search(_id integer primary key autoincrement,userid text,name text,count integer,avatar integer,time integer)");

          //创建本地购物车记录表 商品id  购买数量num 属性id
        db.execSQL("create table mycart(id integer primary key ,num integer,product_property_id varchar(20),ischeck integer)");

        //创建订单数据库表
        //向数据库保存数据,订单号,订单时间,品名的拼接,数量的拼接,单价的拼接,总价,状态:未支付,已支付
        db.execSQL("create table shoppingorder(order_num varchar(50),order_time varchar(50)," +
                "productNameContact varchar(1000),productNumContact varchar(1000),productPriceContact varchar(1000)," +
                "moneyAll varchar(100),pay_state varchar(10))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
