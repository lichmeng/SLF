package com.sugar.myapplication.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sugar.myapplication.App;
import com.sugar.myapplication.bean.MyCart;
import com.sugar.myapplication.db.MyOpenHelper;
import com.sugar.myapplication.global.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MyCartDao {

    private final MyOpenHelper mHelper;
    private final Uri uri;

    public MyCartDao() {
        uri = Uri.parse(Api.CONTENT_OBSERVER_URI);
        mHelper = new MyOpenHelper(App.mContext);
    }


    /**
     * 获取本地购物车中所保存的购物信息
     *
     * @return
     */
    public List<MyCart> getMyCart() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("mycart", null, null, null, null, null, null);
        List<MyCart> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MyCart myCart = new MyCart();
                int id = cursor.getInt(0);
                int num = cursor.getInt(1);
                int product_property_id = cursor.getInt(2);
                int ischeck = cursor.getInt(3);
                myCart.id = id;
                myCart.num = num;
                myCart.product_property_id = product_property_id;
                myCart.ischeck = ischeck;
                list.add(myCart);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询某件商品的的购物信息
     *
     * @return
     */
    public MyCart queryMyCart(int id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("mycart", null, "id=?", new String[]{id + ""}, null, null, null, null);
        MyCart myCart = null;
        if (cursor.moveToNext()) {
            myCart = new MyCart();
            int _id = cursor.getInt(0);
            int num = cursor.getInt(1);
            int ischeck = cursor.getInt(3);
            myCart.id = _id;
            myCart.num = num;
            myCart.ischeck = ischeck;
        }
        cursor.close();
        db.close();
        return myCart;
    }

    /**
     * 查询购物车中所有商品的个数
     * @return
     */
    public int queryAllGoodsCount() {
        int count=0;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("mycart", new String[]{"num"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int num = cursor.getInt(0);
            count +=   num;
            System.out.println("count :" + count);
        }
        cursor.close();
        db.close();
        return count;
    }


    /**
     * 从产品详情页--向购物车添加记录的方法
     */
    public void addCartFromDetail(int id, int num) {

        if (id == 0) {
            return;
        }
        MyCart myCart = queryMyCart(id);
        if (myCart != null) {
            int num1 = myCart.num + num;
            updateCart(id, num1);

        } else {

            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("num", num);
            //传入属性的值....可能有问题!
            values.put("product_property_id", 1);
            values.put("ischeck", 1);
            long mycart = db.insert("mycart", null, values);
            db.close();
        }
        App.mContext.getContentResolver().notifyChange(uri, null);
    }


    /**
     * 修改购物车中相应商品的数量和属性的方法
     *
     * @ param id:需要修改商品的id
     * @ param num:要修改的数量
     * @ param product_property_id:要更改的属性
     */
    public boolean updateCart(int id, int num) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num", num);

        int mycart = db.update("mycart", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        App.mContext.getContentResolver().notifyChange(uri, null);
        //判断是否更新成功
        if (mycart != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 清空购物车中商品记录的方法
     */
    public void clearCart() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("mycart", null, null);
        db.close();
        App.mContext.getContentResolver().notifyChange(uri, null);
    }


    /**
     * 删除购物车中商品记录的方法
     */
    public boolean deleteCart(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete("mycart", "id = ?", new String[]{id + ""});
        db.close();
        App.mContext.getContentResolver().notifyChange(uri, null);
        if (delete > 0) {
            return true;
        }
        return false;
    }


    /**
     * 购物车修改box属性的专用方法
     */
    public boolean updateCartCheck(int id, int ischeck) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ischeck", ischeck);

        int mycart = db.update("mycart", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        //判断是否更新成功
        if (mycart != 0) {
            return true;
        } else {
            return false;
        }

    }
}