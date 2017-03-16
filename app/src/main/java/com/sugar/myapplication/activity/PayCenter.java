package com.sugar.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.BaseAdapter4Home;
import com.sugar.myapplication.bean.CartResponse;
import com.sugar.myapplication.bean.MyCart;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.dao.MyOrderDao;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.NoScrollViewPager;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/23.
 */
//结算中心的界面
public class PayCenter extends Activity implements View.OnClickListener {

    private Button cart_pay;
    private ImageView iv_pay_back;
    private ImageView iv_user_center1;
    private ImageView iv_user_center2;
    private ListView lv_pay_detail;
    private MyCartDao mcd = new MyCartDao();
    private MyOrderDao mod = new MyOrderDao();
    private CartResponse mCartResponse;
    private PayAdapter adapter;
    private TextView tv_pay_sumnum;
    private TextView tv_user;
    private TextView tv_user_phone;
    private TextView tv_user_address;
    private TextView tv_user_channel;
    private TextView tv_pay__money;
    private TextView tv_pay_carry_money;
    private String[] productColor = {"红色", "绿色", "黄色"};
    private String[] productSize = {"M", "XXXL", "XL"};
    public ArrayList<CartResponse.CartBean> list = new ArrayList<>();//请求服务器获取的数据
    private int moneyAll;
    private String sku = "";//选中购买的商品
    private String productNameContact = "";//选中购买的商品名字拼接
    private String productPriceContact = "";//选中购买的商品价格拼接
    private String productNumContact = "";//选中购买的商品数量拼接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        tv_user = (TextView) findViewById(R.id.tv_user);//用户名
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);//用户手机
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);//用户地址
        tv_user_channel = (TextView) findViewById(R.id.tv_user_channel);//支付方式
        cart_pay = (Button) findViewById(R.id.btn_cart_pay);
        iv_user_center1 = (ImageView) findViewById(R.id.iv_user_center1);
        iv_user_center2 = (ImageView) findViewById(R.id.iv_user_center2);
        iv_pay_back = (ImageView) findViewById(R.id.iv_pay_back);
        tv_pay_sumnum = (TextView) findViewById(R.id.tv_pay_sumnum);
        tv_pay__money = (TextView) findViewById(R.id.tv_pay__money);
        tv_pay_carry_money = (TextView) findViewById(R.id.tv_pay_carry_money);
        lv_pay_detail = (ListView) findViewById(R.id.lv_pay_detail);
        cart_pay.setOnClickListener(this);
        iv_pay_back.setOnClickListener(this);


        //访问网络获取数据
        initData();

    }

    private void initData() {

        //获取购物车中的数据
        List<MyCart> myCart = mcd.getMyCart();

        if (myCart.size() == 0) {//购物车为空
//            adapter.notifyDataSetChanged();
            return;
        }

        for (MyCart cart : myCart) {

            int id = cart.id;
            int num = cart.num;
            int product_property_id = cart.product_property_id;

            if (cart.ischeck == 1) {//是被选中的商品
                //拼接请求服务器的参数
                sku = id + ":" + num + ":" + product_property_id + "|" + sku;
            } else {

            }
            ;

        }

        //去请求服务器
        HttpParams params = new HttpParams();
        params.put("sku", sku);

        App.mLoader.post(Api.URL_CART, params, CartResponse.class, 201, new HttpLoader.HttpListener() {
            @Override
            public void onGetResponseSuccess(int requestCode, IResponse response) {
                if (requestCode == Api.REQUEST_CODE_CART) {
                    mCartResponse = (CartResponse) response;

                    if (mCartResponse != null) {
                        list = (ArrayList<CartResponse.CartBean>) mCartResponse.cart;
                        //填充数据
                        fillData();
                    }
                }
            }

            @Override
            public void onGetResponseError(int requestCode, VolleyError error) {
                ToastUtil.showToast("获取服务器数据失败!");
            }

        });

    }

    private void fillData() {
        //填充数据
        tv_user.setText("收货人:" + App.userName);
        tv_pay_sumnum.setText(mCartResponse.totalCount + "");
        tv_pay_carry_money.setText("￥:10");
        moneyAll = mCartResponse.totalPrice + 10;
        tv_pay__money.setText("￥:" + moneyAll);

        adapter = new PayAdapter(list);
        lv_pay_detail.setAdapter(adapter);


    }


    class PayAdapter extends BaseAdapter4Home<CartResponse.CartBean> {

        public PayAdapter(List<CartResponse.CartBean> list) {
            super(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            final CartResponse.CartBean product = mList.get(position);
            if (convertView == null) {
                convertView = View.inflate(App.mContext, R.layout.item_paycenter_listview, null);
                holder = new ViewHolder();

                holder.iv_cart_pay_icon = (ImageView) convertView.findViewById(R.id.iv_cart_pay_icon);
                holder.tv_cart_pay_title = (TextView) convertView.findViewById(R.id.tv_cart_pay_title);
                holder.tv_pay_calor = (TextView) convertView.findViewById(R.id.tv_pay_calor);
                holder.tv_pay_size = (TextView) convertView.findViewById(R.id.tv_pay_size);
                holder.tv_pay_price = (TextView) convertView.findViewById(R.id.tv_pay_price);
                holder.tv_pay_num = (TextView) convertView.findViewById(R.id.tv_pay_numlll);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_cart_pay_title.setText("商品:" + product.product.name);
            HttpLoader.getInstance(App.mContext).display(holder.iv_cart_pay_icon, Api.URL_SERVER + product.product.pic, R.drawable.image_loading, R.drawable.image_loading);
            holder.tv_pay_price.setText("￥:" + product.product.price);
            holder.tv_pay_price.setTextColor(Color.RED);
            holder.tv_pay_num.setText("X" + product.prodNum);
            holder.tv_pay_calor.setText("颜色:" + productColor[position % 3]);
            holder.tv_pay_size.setText("尺码:" + productSize[position % 3]);

            //为了订单页的数据展示,拼接存入数据库,我也是拼了!
            productNameContact = productNameContact + product.product.name + ",";
            productPriceContact = productPriceContact + product.product.price + ",";
            productNumContact = productNumContact + product.prodNum + ",";


            return convertView;

        }

    }

    class ViewHolder {
        ImageView iv_cart_pay_icon;//商品图标
        TextView tv_cart_pay_title;//商品名称
        TextView tv_pay_calor;//商品颜色
        TextView tv_pay_size;//商品尺码
        TextView tv_pay_price;//商品价格
        TextView tv_pay_num;//购买数量

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交订单,弹出订单生成成功,和订单号的土司
            case R.id.btn_cart_pay:

                //生成订单号:时间+uuid
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                String format = sdf.format(new Date());
                Random random = new Random();
                int i = random.nextInt(50000) + 20000;
                String order = i + format;

                //生成订单的时间
                String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date);
                ToastUtil.showToast("恭喜,订单提交成功! \n订单号: " + order);

                //向数据库保存数据,订单号,订单时间,品名的拼接,数量的拼接,单价的拼接,总价,状态:未支付0,已支付1

              /*
order_num
order_time
productNameContact
productNumContact
productPriceContact
moneyAll
pay_state
 */
                mod.insertOrder(order, time, productNameContact, productNumContact, productPriceContact, moneyAll + "", "未支付");


                //获取购物车中的数据
                List<MyCart> myCart = mcd.getMyCart();
                if (myCart.size() == 0) {//购物车为空
                    return;
                } else {
                    for (MyCart cart : myCart) {
                        int id = cart.id;
                        if (cart.ischeck == 1) {//是被选中的商品
                            mcd.deleteCart(id);
                        }
                    }
                }

                //跳转页面
                Intent intent = new Intent(this, CompletePay.class);
                //需要传递所购物品的参数,或者在新页面从新获取参数?
                String[] param = {order, time, moneyAll + "", sku};
                intent.putExtra("param", param);
                startActivity(intent);

                break;
            //退出当前界面
            case R.id.iv_pay_back:
                finish();
                break;
            //跳转到用户中心
            case R.id.iv_user_center1:

                Intent in = new Intent(PayCenter.this, MainActivity.class);
                in.putExtra("jump", "toUserCenter");
                startActivity(in);

                break;
            case R.id.iv_user_center2:
                NoScrollViewPager view2 = (NoScrollViewPager) findViewById(R.id.cotent_container);
                RadioGroup mRgTabs2 = (RadioGroup) findViewById(R.id.rg_tabs);
                mRgTabs2.check(mRgTabs2.getId());
                RadioButton childAt2 = (RadioButton) mRgTabs2.getChildAt(4);
                childAt2.setChecked(true);
                view2.setCurrentItem(4);

                break;


        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (list == null || list.size() == 0) {
            initData();
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化
        productNameContact = "";
        productPriceContact = "";
        productNumContact = "";


    }
}
