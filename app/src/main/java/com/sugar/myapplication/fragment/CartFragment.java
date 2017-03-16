package com.sugar.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.GoodsInfoActivity;
import com.sugar.myapplication.activity.OrderDetailActivity;
import com.sugar.myapplication.activity.PayCenter;
import com.sugar.myapplication.adapter.BaseAdapter4Home;
import com.sugar.myapplication.bean.CartResponse;
import com.sugar.myapplication.bean.MyCart;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.event.CartChangeEvent;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.manager.JumpToLoginManager;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.NoScrollViewPager;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sugar on 2016/11/22.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    public ListView lv_cart;
    public ArrayList<CartResponse.CartBean> list = new ArrayList<>();//请求服务器获取的数据
    private TextView tv;
    private Button btn_cart_pay;
    private ListView refreshableView;
    private CartResponse mCartResponse;
    private CartAdapter adapter;
    private TextView tv_cart_money_total;
    private TextView tv_cart_num_total;
    private TextView tv_cart_clearall;
    private TextView tv_cart_refresh;
    private FrameLayout fl_cart_empty_container;
    private LinearLayout ll_cart_bottom_layout;
    private LinearLayout ll_cart_none;
    private MyCartDao mcd = new MyCartDao();
    private String[] productColor = {"红色", "绿色", "黄色"};
    private String[] productSize = {"M", "XXXL", "XL"};
    private ArrayList<CartResponse.CartBean> templist;
    private HashMap<Integer, Boolean> checkStateMap = new HashMap<>();//用于存checkBox的状态
    private View view;
    private View v_cart;
    private CheckBox cb_cart;//底部布局的checkbox
    private List<MyCart> myCart;
    private TextView tv_cart_myorder;//跳转到订单页

    @Override
    protected View getSuccessView() {

        //购物车中有东西显示的View
        view = View.inflate(mActivity, R.layout.fragment_cart, null);
        tv_cart_myorder = (TextView)view.findViewById(R.id.tv_cart_myorder);
        lv_cart = (ListView) view.findViewById(R.id.lv_cart);
        lv_cart.setVisibility(View.INVISIBLE);//listview显示
        lv_cart.setSelector(android.R.drawable.screen_background_light_transparent);
        lv_cart.setCacheColorHint(Color.TRANSPARENT);
        tv_cart_money_total = (TextView) view.findViewById(R.id.tv_cart_money_total);
        ll_cart_bottom_layout = (LinearLayout) view.findViewById(R.id.ll_cart_bottom_layout);//底部布局
        ll_cart_none = (LinearLayout) view.findViewById(R.id.ll_cart_none);//空购物车的布局
        cb_cart = (CheckBox) view.findViewById(R.id.cb_cart);//空购物车的布局
        tv_cart_num_total = (TextView) view.findViewById(R.id.tv_cart_num_total);
        tv_cart_clearall = (TextView) view.findViewById(R.id.tv_cart_clearall);
        tv_cart_refresh = (TextView) view.findViewById(R.id.tv_cart_refresh);
        v_cart = (View) view.findViewById(R.id.v_cart);


        Button btn_no_data = (Button) view.findViewById(R.id.btn_no_data);
        tv_cart_clearall.setVisibility(View.INVISIBLE);//默认隐藏按钮
        v_cart.setVisibility(View.INVISIBLE);//默认分割线
        ll_cart_none.setVisibility(View.VISIBLE);

        //跳转到订单页
        tv_cart_myorder.setOnClickListener(this);
        //刷新购物车
        tv_cart_refresh.setOnClickListener(this);

        //清空购物车
        tv_cart_clearall.setOnClickListener(this);
        //tem点击事件
        lv_cart.setOnItemClickListener(this);
        //长按事件
        lv_cart.setOnItemLongClickListener(this);
        btn_cart_pay = (Button) view.findViewById(R.id.btn_cart_pay);
        btn_cart_pay.setOnClickListener(this);

        //空购物车布局中的点击事件
        btn_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到首页界面
                NoScrollViewPager view = (NoScrollViewPager) getActivity().findViewById(R.id.cotent_container);
                RadioGroup mRgTabs = (RadioGroup) getActivity().findViewById(R.id.rg_tabs);
                mRgTabs.check(mRgTabs.getId());
                RadioButton childAt = (RadioButton) mRgTabs.getChildAt(0);
                childAt.setChecked(true);
                view.setCurrentItem(0);

            }
        });
        //底部布局的checkbox,控制是否全选
        cb_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cb_cart.isChecked();
                if (checked) {
                    ToastUtil.showToast("偶要全选...");

                } else {
                    ToastUtil.showToast("老子全都不要了...");

                }


            }
        });

        return view;
    }

    //子线程方法
    @Override
    protected Object requestData() {
        //获取购物车中的数据
        myCart = mcd.getMyCart();

        if (myCart.size() == 0) {//购物车为空
            ll_cart_bottom_layout.setVisibility(View.INVISIBLE);
//            loadingPage.refreshState(LoadingPage.PageState.STATE_SUCCESS);
            return "";

        }

        //去请求服务器
        String sku = "";
        for (MyCart cart : myCart) {
            int id = cart.id;
            int num = cart.num;
            int product_property_id = cart.product_property_id;
            //拼接请求服务器的参数
            sku = id + ":" + num + ":" + product_property_id + "|" + sku;
        }
        HttpParams params = new HttpParams();
        params.put("sku", sku);

        App.mLoader.post(Api.URL_CART, params, CartResponse.class, 201, new HttpLoader.HttpListener() {
            @Override
            public void onGetResponseSuccess(int requestCode, IResponse response) {
                if (requestCode == Api.REQUEST_CODE_CART) {
                    mCartResponse = (CartResponse) response;
                    //添加到集合中

                    if (mCartResponse != null) {
                        tv_cart_clearall.setVisibility(View.VISIBLE);//显示清空按钮
                        lv_cart.setVisibility(View.VISIBLE);//listview显示
                        v_cart.setVisibility(View.VISIBLE);//显示分割线
                        ll_cart_none.setVisibility(View.INVISIBLE);//空购物车隐藏
                        ll_cart_bottom_layout.setVisibility(View.VISIBLE);//显示底部布局
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

        return mCartResponse;

    }

    private void fillData() {
        //填充数据
        tv_cart_money_total.setText("金额合计:" + mCartResponse.totalPrice + "");
        tv_cart_num_total.setText("购买数量:" + mCartResponse.totalCount + "");
        adapter = new CartAdapter(list);
        lv_cart.setAdapter(adapter);

        //adapter.notifyDataSetChanged();
    }


    //ListView的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //处理checkbox的事件
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_cart_item);
        ImageView iv_cart_jumptodetail = (ImageView) view.findViewById(R.id.iv_cart_jumptodetail);
        CartResponse.CartBean cartBean = list.get(position);
        if (checkBox.isChecked()) {//减少
            checkBox.setChecked(false);
          //  view.setBackgroundColor();


            //同步底部的数据
            //获取原始总量
            int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
            int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
            //当前条目的总量
            int price = cartBean.product.price;
            int prodNum = cartBean.prodNum;
            //差量
            int currentPrice = priceTotal - price * prodNum;
            tv_cart_money_total.setText("金额合计:" + currentPrice);
            tv_cart_num_total.setText("购买数量:" + (numTotal - prodNum));


           // view.setBackgroundColor(Color.LTGRAY);
            iv_cart_jumptodetail.setClickable(false);


            //修改数据库的删除
            mcd.updateCartCheck(cartBean.product.id, 0);


        } else {//增加
            checkBox.setChecked(true);
           // view.setLongClickable(true);


            //同步底部的数据
            //获取原始总量
            int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
            int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
            //当前条目的总量
            int price = cartBean.product.price;
            int prodNum = cartBean.prodNum;
            //差量
            int currentPrice = priceTotal + price * prodNum;
            tv_cart_money_total.setText("金额合计:" + currentPrice);
            tv_cart_num_total.setText("购买数量:" + (numTotal + prodNum));

           // view.setBackgroundColor(Color.LTGRAY);

            iv_cart_jumptodetail.setClickable(true);

            //修改数据库的删除
            mcd.updateCartCheck(cartBean.product.id, 1);

        }

    }

    //ltem的长按事件

    @Override
    public boolean onItemLongClick(AdapterView<?> parent,  View view, int position, long id) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_cart_item);
        final CartResponse.CartBean cartBean = list.get(position);
        final int temPosition = position;
        if(!checkBox.isChecked()){
            return true;
        }
        //删除当前的item,更新列表和数据库
        AlertDialog.Builder ad = new AlertDialog.Builder(mActivity);
        ad.setTitle("是否删除当前商品?");
        ad.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //同步底部的数据
                //获取原始总量
                int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
                int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
                //当前条目的总量
                int price = cartBean.product.price;
                int prodNum = cartBean.prodNum;
                //差量
                int currentPrice = priceTotal - price * prodNum;

                tv_cart_money_total.setText("金额合计:" + currentPrice);
                tv_cart_num_total.setText("购买数量:" + (numTotal - prodNum));

                list.remove(temPosition);
                ToastUtil.showToast("删除成功!");
                adapter.notifyDataSetChanged();

                //数据库的删除
                mcd.deleteCart(cartBean.product.id);


                //为空显示的布局
                if (list.size() == 0) {
                    clearMycart();
                }

            }
        });

        ad.setNegativeButton("取消", null);
        ad.create().show();

        return true;
    }


    class CartAdapter extends BaseAdapter4Home<CartResponse.CartBean> {
        public CartAdapter(List<CartResponse.CartBean> list) {
            super(list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final CartResponse.CartBean product = mList.get(position);
            if (convertView == null) {
                convertView = View.inflate(App.mContext, R.layout.item_cart_listview, null);
                holder = new ViewHolder();
                holder.iv_cart_icon = (ImageView) convertView.findViewById(R.id.iv_cart_icon);
                holder.iv_cart_jumptodetail = (ImageView) convertView.findViewById(R.id.iv_cart_jumptodetail);
                holder.tv_cart_title = (TextView) convertView.findViewById(R.id.tv_cart_title);
                holder.tv_cart_color = (TextView) convertView.findViewById(R.id.tv_cart_color);
                holder.tv_cart_size = (TextView) convertView.findViewById(R.id.tv_cart_size);
                holder.tv_cart_price = (TextView) convertView.findViewById(R.id.tv_cart_price);
                holder.tv_cart_num = (TextView) convertView.findViewById(R.id.tv_cart_number);
                holder.btn_cart_reduce = (Button) convertView.findViewById(R.id.btn_cart_reduce);
                holder.btn_cart_plus = (Button) convertView.findViewById(R.id.btn_cart_plus);
                holder.cb_cart_item = (CheckBox) convertView.findViewById(R.id.cb_cart_item);

                //跳转到详情界面
                holder.iv_cart_jumptodetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   if (holder.cb_cart_item.isChecked()) {//cb可用


                        Intent intent = new Intent(mActivity, GoodsInfoActivity.class);
                        int id = product.product.id;
                        intent.putExtra("id", id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                        } else {//cb不可用,则禁用该点击事件
//                            holder.iv_cart_jumptodetail.setClickable(false);
//                        }
                    }
                });

                //s数量减少的监听
                holder.btn_cart_reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否选中
                        if (holder.cb_cart_item.isChecked()) {
                            //判断数量
                            if (Integer.parseInt(holder.tv_cart_num.getText().toString()) > 1) {
                                holder.tv_cart_num.setText(Integer.parseInt(holder.tv_cart_num.getText().toString()) - 1 + "");
                                //总金额和总数量的联动
                                int price = Integer.parseInt(holder.tv_cart_price.getText().toString().substring(2));//商品单价
                                int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
                                int currentPrice = priceTotal - price;
                                tv_cart_money_total.setText("金额合计:" + currentPrice + "");

                                int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
                                int currentNum = numTotal - 1;
                                tv_cart_num_total.setText("购买数量:" + currentNum + "");

                                //保存数据库
                                //开始访问数据库的操作
                                //获取购物车中的数据

                                product.prodNum = product.prodNum - 1;

                                mcd.updateCart(product.product.id, product.prodNum);
                                //同步提交变化消息
                                EventBus.getDefault().post(new CartChangeEvent(-1));

                            }
                        }
                    }
                });

                //s数量增加的监听
                holder.btn_cart_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否选中
                        if (holder.cb_cart_item.isChecked()) {
                            //判断数量
                            holder.tv_cart_num.setText(Integer.parseInt(holder.tv_cart_num.getText().toString()) + 1 + "");

                            //总金额和总数量的联动
                            int price = Integer.parseInt(holder.tv_cart_price.getText().toString().substring(2));//商品单价
                            int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
                            int currentPrice = priceTotal + price;
                            tv_cart_money_total.setText("金额合计:" + currentPrice + "");

                            int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
                            int currentNum = numTotal + 1;
                            tv_cart_num_total.setText("购买数量:" + currentNum + "");

                            //保存数据库
                            //开始访问数据库的操作
                            MyCartDao mcd = new MyCartDao();
                            //获取购物车中的数据

                            product.prodNum = product.prodNum + 1;
                            mcd.updateCart(product.product.id, product.prodNum);
                            //同步提交变化消息
                            EventBus.getDefault().post(new CartChangeEvent(1));

                        }
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            //设置checkbox状态

            int ischeck = 0;
            try {
                ischeck = mcd.queryMyCart(myCart.get(position).id).ischeck;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

            if (ischeck == 0) {//false
                holder.cb_cart_item.setChecked(false);

                int priceTotal = Integer.parseInt(tv_cart_money_total.getText().toString().substring(5));//商品总价
                int numTotal = Integer.parseInt(tv_cart_num_total.getText().toString().substring(5));//购买数量
                //当前条目的总量
                int price = product.product.price;
                int prodNum = product.prodNum;
                //差量
                int currentPrice = priceTotal - price * prodNum;

                tv_cart_money_total.setText("金额合计:" + currentPrice);
                tv_cart_num_total.setText("购买数量:" + (numTotal - prodNum));

            } else {
                holder.cb_cart_item.setChecked(true);

            }

            holder.tv_cart_title.setText("商品:" + product.product.name);
            HttpLoader.getInstance(App.mContext).display(holder.iv_cart_icon, Api.URL_SERVER + product.product.pic, R.drawable.image_loading, R.drawable.image_loading);
            holder.tv_cart_color.setText("颜色:" + productColor[position % 3]);
            holder.tv_cart_size.setText("尺码:" + productSize[position % 3]);
            holder.tv_cart_price.setText("￥:" + product.product.price);
            holder.tv_cart_price.setTextColor(Color.RED);
            holder.tv_cart_num.setText(product.prodNum + "");

            return convertView;
        }


    }

    class ViewHolder {
        ImageView iv_cart_icon;//商品图标
        ImageView iv_cart_jumptodetail;//详情跳转
        TextView tv_cart_title;//商品名称
        TextView tv_cart_color;//商品颜色
        TextView tv_cart_size;//商品尺码
        TextView tv_cart_price;//商品价格
        TextView tv_cart_num;//购买数量
        Button btn_cart_reduce;//数量减少
        Button btn_cart_plus;//数量增加
        CheckBox cb_cart_item;//checkBox
    }


    //页面中点击结算事件的响应
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击结算按钮跳到结算的activity
            case R.id.btn_cart_pay:

                //验证是否有商品购买
                if (Integer.parseInt(tv_cart_money_total.getText().toString().substring(5)) <= 0) {//限制提交订单
                    ToastUtil.showToast("未购买任何商品!");
                    return;
                }
                //验证是否登陆过
                if (JumpToLoginManager.isJumpToLogin(mActivity)) {
                    return;
                }

                Intent intent = new Intent(mActivity, PayCenter.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //需要传递所购物品的参数,或者在新页面从新获取参数?
                mActivity.startActivity(intent);
                break;

            //清空购物车
            case R.id.tv_cart_clearall:
                tv_cart_clearall.setVisibility(View.INVISIBLE);
                v_cart.setVisibility(View.INVISIBLE);
                clearMycart();
                break;

            //刷新购物车
            case R.id.tv_cart_refresh:

                SystemClock.sleep(500);
                ToastUtil.showToast("刷新成功");

                break;
            //跳转到订单页
            case R.id.tv_cart_myorder:
                //验证是否登陆过
                if (JumpToLoginManager.isJumpToLogin(mActivity)) {
                    return;
                }
                Intent intent2 = new Intent(mActivity, OrderDetailActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent2);
                break;
        }

    }

    private void clearMycart() {
        list.clear();
        adapter.notifyDataSetChanged();
        ToastUtil.showToast("清空成功!");
        //数据库的删除
        ll_cart_none.setVisibility(View.VISIBLE);//显示空购物车布局
        ll_cart_bottom_layout.setVisibility(View.INVISIBLE);//隐藏底部布局

        new Thread() {
            @Override
            public void run() {
                mcd.clearCart();
            }
        }.start();
    }


    @Override
    public void onResume() {

        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();
        requestData();
        if (myCart.size() == 0) {//购物车为空
            ll_cart_none.setVisibility(View.VISIBLE);
            ll_cart_bottom_layout.setVisibility(View.INVISIBLE);
            tv_cart_clearall.setVisibility(View.INVISIBLE);//默认隐藏按钮
            v_cart.setVisibility(View.INVISIBLE);//默认分割线
            lv_cart.setVisibility(View.INVISIBLE);//listview隐藏

        } else {
            ll_cart_none.setVisibility(View.INVISIBLE);
            lv_cart.setVisibility(View.VISIBLE);//listview隐藏
        }


    }


}
