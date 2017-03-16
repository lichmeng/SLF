package com.sugar.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.BaseAdapter4Home;
import com.sugar.myapplication.bean.CartResponse;
import com.sugar.myapplication.bean.OrderdetailInfo;
import com.sugar.myapplication.dao.MyOrderDao;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpParams;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class OrderDetailActivity extends Activity {

    private ListView lv_order_detail;
    private MyOrderDao mod = new MyOrderDao();
    private List<OrderdetailInfo> allMyOrders;
    private HttpParams params;
    private CartResponse mCartResponse;
    private ImageView iv_order_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        iv_order_back = (ImageView) findViewById(R.id.iv_order_back);
        iv_order_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lv_order_detail = (ListView) findViewById(R.id.lv_order_detail);
        //从服务器获取订单信息
        initData();

    }

    //从服务器获取订单信息
    private void initData() {

        allMyOrders = mod.getAllMyOrders();
        if (allMyOrders.size() != 0) {//有信息
            MyAdapter adapter = new MyAdapter(allMyOrders);
            lv_order_detail.setAdapter(adapter);

        } else {//没有购买过任何商品
            setContentView(R.layout.layout_orderdetail_buynothing);
            iv_order_back = (ImageView) findViewById(R.id.iv_order_back);
            iv_order_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            return;
        }

    }


    class MyAdapter extends BaseAdapter4Home<OrderdetailInfo> {

        public MyAdapter(List<OrderdetailInfo> list) {
            super(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(App.mContext, R.layout.item_order_info, null);
                holder = new ViewHolder();
                holder.tv_orderdetail_num = (TextView) convertView.findViewById(R.id.tv_orderdetail_num);
                holder.tv_orderdetail_time = (TextView) convertView.findViewById(R.id.tv_orderdetail_time);
                holder.tv_orderdetail_money = (TextView) convertView.findViewById(R.id.tv_orderdetail_money);
                holder.tv_orderdetail_state = (TextView) convertView.findViewById(R.id.tv_orderdetail_state);
                holder.tv_orderdetail_products = (TextView) convertView.findViewById(R.id.tv_orderdetail_products);
                holder.tv_order_topaynow = (TextView) convertView.findViewById(R.id.tv_order_topaynow);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final OrderdetailInfo orderdetailInfo = mList.get(position);
            holder.tv_orderdetail_num.setText("订单:"+orderdetailInfo.order_num);
            holder.tv_orderdetail_time.setText("时间:"+orderdetailInfo.order_time);

             //设置支付入口不可见
            holder.tv_order_topaynow.setVisibility(View.INVISIBLE);
            if("未支付".equals(orderdetailInfo.pay_state)){
                holder.tv_orderdetail_state.setTextColor(Color.RED);
                holder.tv_order_topaynow.setVisibility(View.VISIBLE);
                holder.tv_order_topaynow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast("支付成功!");
                        holder.tv_order_topaynow.setTextColor(Color.GRAY);
                        holder.tv_order_topaynow.setEnabled(false);
                        holder.tv_order_topaynow.setVisibility(View.INVISIBLE);
                        holder.tv_orderdetail_state.setTextColor(Color.argb(223,0,0,0));
                        holder.tv_orderdetail_state.setText("状态:已支付");
                        //更改数据库
                        mod.updateOrderState(orderdetailInfo.order_num,"已支付");

                    }
                });
            }
            holder.tv_orderdetail_state.setText("状态:"+orderdetailInfo.pay_state);
            holder.tv_orderdetail_money.setText("￥:"+orderdetailInfo.moneyAll);

            //获取每个订单的所有产品的明细
            String productNameContact = orderdetailInfo.productNameContact;
            String[] split_productNameContact = productNameContact.split(",");

            String productNumContact = orderdetailInfo.productNumContact;
            String[] split_productNumContact = productNumContact.split(",");

            String productPriceContact = orderdetailInfo.productPriceContact;
            String[] split_productPriceContact = productPriceContact.split(",");


            String productText = "";
            for (int i =0; i<split_productNameContact.length;i++) {
                productText = productText+"品名:"+split_productNameContact[i]+"   数量:"+
                        split_productNumContact[i]  +"   单价:"+
                        split_productPriceContact [i]+"\n";

            }

            holder.tv_orderdetail_products.setText(productText);


            return convertView;
        }
    }


    class ViewHolder {
        TextView tv_orderdetail_num;//订单号
        TextView tv_orderdetail_time;//订单时间
        TextView tv_orderdetail_money;//订单总金额
        TextView tv_orderdetail_state;//订单状态,已支付
        TextView tv_orderdetail_products;//订单产品详情
        TextView tv_order_topaynow;//去支付

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


}
