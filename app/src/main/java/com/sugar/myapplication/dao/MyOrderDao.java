package com.sugar.myapplication.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sugar.myapplication.App;
import com.sugar.myapplication.bean.OrderdetailInfo;
import com.sugar.myapplication.db.MyOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */
public class MyOrderDao {
    private MyOpenHelper mHelper;

    public MyOrderDao() {
        mHelper = new MyOpenHelper(App.mContext);
    }
/*
order_num
order_time
productNameContact
productNumContact
productPriceContact
moneyAll
pay_state
 */
    /**
     * 获取本地购物车中所保存的订单信息
     *
     * @return
     */
    public List<OrderdetailInfo> getAllMyOrders() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("shoppingorder", null, null, null, null, null, null);
        List<OrderdetailInfo> orderList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderdetailInfo oi = new OrderdetailInfo();
            String order_num = cursor.getString(0);
            String order_time = cursor.getString(1);
            String productNameContact = cursor.getString(2);
            String productNumContact = cursor.getString(3);
            String productPriceContact = cursor.getString(4);
            String moneyAll = cursor.getString(5);
            String pay_state = cursor.getString(6);
            oi.order_num = order_num;
            oi.order_time = order_time;
            oi.moneyAll = moneyAll;
            oi.productNameContact = productNameContact;
            oi.productNumContact = productNumContact;
            oi.productPriceContact = productPriceContact;
            oi.pay_state = pay_state;
            orderList.add(oi);
        }

        return orderList;
    }

    /**
     * 根据订单号,查询某条订单的时间
     *
     * @return
     */
    public OrderdetailInfo queryMyOrder(String order_num) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("shoppingorder", null, "order_num=?", new String[]{order_num}, null, null, null, null);
        OrderdetailInfo oi = null;
        if (cursor.moveToNext()) {
            oi = new OrderdetailInfo();
            String order_time = cursor.getString(1);
            String productNameContact = cursor.getString(2);
            String productNumContact = cursor.getString(3);
            String productPriceContact = cursor.getString(4);
            String moneyAll = cursor.getString(5);
            String pay_state = cursor.getString(6);
            oi.order_num = order_num;
            oi.order_time = order_time;
            oi.moneyAll = moneyAll;
            oi.productNameContact = productNameContact;
            oi.productNumContact = productNumContact;
            oi.productPriceContact = productPriceContact;
            oi.pay_state = pay_state;
        }
        return oi;
    }

    /**
     * 插入订单信息
     *
     * @return
     */

    public void insertOrder(String order_num, String order_time, String productNameContact,
                            String productNumContact,String productPriceContact,
                            String moneyAll,String pay_state )
                            {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_num", order_num);
        values.put("order_time", order_time);
        values.put("productNameContact", productNameContact);
        values.put("productNumContact", productNumContact);
        values.put("productPriceContact", productPriceContact);
        values.put("moneyAll", moneyAll);
        values.put("pay_state", pay_state);
        long orderIndex = db.insert("shoppingorder", null, values);

    }

    /**
     * 修改支付状态
     *
     * @return
     */

    public void updateOrderState(String order_num, String newState) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pay_state",newState);
        db.update("shoppingorder",values,"order_num = ?",new String[]{order_num});
    }

    /**
     * 清空订单记录的方法
     */
    public void clearAllOrders() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("shoppingorder", null, null);
        db.close();
    }


    /**
     * 删除订单记录的方法
     */
    public boolean deleteOrder(String order_num) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete("shoppingorder", "order_num = ?", new String[]{order_num});
        db.close();
        if (delete > 0) {
            return true;
        }
        return false;
    }

}
