package com.sugar.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.dao.MyOrderDao;
import com.sugar.myapplication.util.ToastUtil;

/**
 * Created by Administrator on 2016/11/24.
 */
public class CompletePay extends Activity {

    private TextView tv_complete_order_num;
    private TextView tv_order_time_num;
    private TextView tv_order_complete;
    private Button btn_cart_complete;
    private ImageView iv_complete_back;
    private MyCartDao mcd = new MyCartDao();
    private MyOrderDao mod = new MyOrderDao();
    private String[] params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_pay);
        Intent intent = getIntent();
        //获取前一页传递的数据
        params = intent.getStringArrayExtra("param");


        tv_complete_order_num = (TextView) findViewById(R.id.tv_complete_order_num);
        tv_complete_order_num.setText(params[0]);

        tv_order_time_num = (TextView) findViewById(R.id.tv_order_time_num);
        tv_order_time_num.setText(params[1]);

        tv_order_complete = (TextView) findViewById(R.id.tv_order_complete);
        tv_order_complete.setText(params[2]);


        //点击支付button 完成支付
        btn_cart_complete = (Button) findViewById(R.id.btn_cart_complete);
        btn_cart_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清理购物车数据
//                //获取购物车中的数据
//                List<MyCart> myCart = mcd.getMyCart();
//                if (myCart.size() == 0) {//购物车为空
//                    return;
//                } else {
//                    for (MyCart cart : myCart) {
//                        int id = cart.id;
//                        if (cart.ischeck == 1) {//是被选中的商品
//                            mcd.deleteCart(id);
//                        }
//                    }
//                }
                ToastUtil.showToast("支付成功!");

                //修改数据库订单的支付状态

                mod.updateOrderState(params[0],"已支付");

                //界面跳转对话框
                final Intent in = new Intent(CompletePay.this, MainActivity.class);

                AlertDialog.Builder ad = new AlertDialog.Builder(CompletePay.this);
                ad.setTitle("支付成功!");
                ad.setCancelable(false);
                ad.setPositiveButton("返回购物车", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        in.putExtra("jump", "toCart");
                        startActivity(in);
                        finish();

                    }
                });
                ad.setNegativeButton("继续购物", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        in.putExtra("jump", "toshopping");
                        startActivity(in);
                        finish();
                    }
                });
                ad.create().show();
            }
        });
        //点击返回图标,关闭界面
        iv_complete_back = (ImageView) findViewById(R.id.iv_complete_back);
        iv_complete_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(CompletePay.this, MainActivity.class);
                in.putExtra("jump", "toCart");
                startActivity(in);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent in = new Intent(CompletePay.this, MainActivity.class);
        in.putExtra("jump", "toCart");
        startActivity(in);
        finish();

    }
}

