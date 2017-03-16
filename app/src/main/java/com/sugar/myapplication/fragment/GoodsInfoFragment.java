package com.sugar.myapplication.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.GoodsInfoPicAdapter;
import com.sugar.myapplication.bean.ProductResponse;
import com.sugar.myapplication.bean.SearchRecommendResponse;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.manager.ProductDetailManager;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.DrawableUtil;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.FlowLayout;
import com.sugar.myapplication.view.MyDialog;

import org.senydevpkg.net.HttpLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wjx on 2016/11/24.
 */

public class GoodsInfoFragment extends ProductBaseFragment implements ProductDetailManager.OnRequestProductListener, View.OnClickListener {

    @InjectView(R.id.vp_show_photo)
    ViewPager vpShowPhoto;
    @InjectView(R.id.tv_goods_title)
    TextView tvGoodsTitle;
    @InjectView(R.id.tv_new_price)
    TextView tvNewPrice;
    @InjectView(R.id.tv_old_price)
    TextView tvOldPrice;
    @InjectView(R.id.ll_activity)
    LinearLayout llActivity;
    @InjectView(R.id.tv_current_goods)
    TextView tvCurrentGoods;
    @InjectView(R.id.ll_current_goods)
    LinearLayout llCurrentGoods;
    @InjectView(R.id.iv_ensure)
    ImageView ivEnsure;
    @InjectView(R.id.tv_comment_count)
    TextView tvCommentCount;
    @InjectView(R.id.tv_good_comment)
    TextView tvGoodComment;
    @InjectView(R.id.iv_comment_right)
    ImageView ivCommentRight;
    @InjectView(R.id.ll_comment)
    LinearLayout llComment;
    @InjectView(R.id.ll_empty_comment)
    LinearLayout llEmptyComment;
    @InjectView(R.id.ll_recommend)
    LinearLayout llRecommend;
    @InjectView(R.id.tv_none_comment)
    TextView tvNoneComment;
    @InjectView(R.id.tv_comment_name)
    TextView tvCommentName;
    @InjectView(R.id.tv_comment_time)
    TextView tvCommentTime;
    @InjectView(R.id.tv_comment_content)
    TextView tvCommentContent;
    @InjectView(R.id.ib_comment_session)
    ImageButton ibCommentSession;
    @InjectView(R.id.ib_comment_like)
    ImageButton ibCommentLike;
    private ProductResponse productResponse;
    private ProductDetailManager productDetailManager;
    private MyDialog dialog;
    private View selectView;
    private TextView tvShopName;
    private TextView tvShopPrice;
    private TextView tvShopColor;
    private TextView tvShopNum;
    private TextView tvShopSize;
    private Button btnShopCut;
    private Button btnShopAdd;
    private Button btnShopCart;
    private ImageView ivClose;
    private ImageView ivShopPhoto;
    private ViewPager v;
    private FlowLayout flContainer;
    private FlowLayout flContainer2;
    private ArrayList<TextView> colorList;
    private ArrayList<TextView> sizeList;
    private int colorChecked = 0;
    private int sizeChecked = 0;
    private MyCartDao dao;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_goods, null);
        //ProductDetailManager.getInstance().setOnRequestProductListener(this);
        productDetailManager = new ProductDetailManager();
        productDetailManager.setOnRequestProductListener(this);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected void initData() {
        productDetailManager.getProductDetail(pId,
                Api.URL_PRODUCT_DETAIL,
                Api.REQUEST_CODE_PRODUCT_DETAIL);
    }

    public void setViewPager(ViewPager v) {
        this.v = v;
    }

    @Override
    public void onRequestSuccess(ProductResponse response) {
        productResponse = response;
        Log.e("------product----------", "productResponse: " + productResponse);
        if (productResponse != null) {
            List<String> list;
            if (productResponse.product.bigPic.size() == 0) {
                list = productResponse.product.pics;
            } else {
                list = productResponse.product.bigPic;
            }
            vpShowPhoto.setAdapter(new GoodsInfoPicAdapter(list));
            tvGoodsTitle.setText("红孩子，红孩子，红孩子，红孩子，红孩子，红孩子，红孩子" + productResponse.product.name);
            tvNewPrice.setText(productResponse.product.price + "");
            tvOldPrice.setText(productResponse.product.marketPrice + "");
            tvCurrentGoods.setText(productResponse.product.productProperty.get(0).v + ",1件");
            tvCommentCount.setText("(" + productResponse.product.commentCount + ")");
            //添加评论
        }
    }

    @Override
    public void onRequestSearchRecommendSuccess(SearchRecommendResponse response) {

    }


    @Override
    public void onRequestFailed() {
        ToastUtil.showToast("请求失败");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_current_goods, R.id.ll_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_current_goods://选择商品属性
                if (productResponse == null) {
                    return;
                }
                showProductSelector();
                break;
            case R.id.btn_shop_cut://减少
                int numCut = Integer.parseInt(tvShopNum.getText().toString());
                if (numCut == 1) {
                    break;
                }
                tvShopNum.setText((--numCut) + "");
                break;
            case R.id.btn_shop_add://增加
                int numAdd = Integer.parseInt(tvShopNum.getText().toString());
                tvShopNum.setText(++numAdd + "");
                break;
            case R.id.iv_close://关闭
                dialog.hide();
                break;
        }
    }

    private void joinToCart() {
        ToastUtil.showToast("加入购物车");
        final String resultNum = tvShopNum.getText().toString();
        TextView colorView = colorList.get(colorChecked);
        String color = colorView.getText().toString();
        TextView sizeView = sizeList.get(sizeChecked);
        String size = sizeView.getText().toString();
        dao = new MyCartDao();
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                dao.addCartFromDetail(pId,Integer.parseInt(resultNum));
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onChange();
                        }
                    }
                });
            }
        });
        //EventBus.getDefault().post(new CartChangeEvent(1));
        dialog.hide();
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog=null;
        }
    }

    /**
     * 显示商品属性选择view
     */
    private void showProductSelector() {
        initSelectView();
        dialog = new MyDialog(getActivity(), R.style.GoodDialog);
        dialog.inDuration(350);
        dialog.outDuration(350);
        dialog.setContentView(selectView);
        dialog.show();
    }

    /**
     * 初始化选择商品属性view
     */
    private void initSelectView() {
        selectView = View.inflate(getActivity(), R.layout.goods_info_select, null);
        tvShopName = (TextView) selectView.findViewById(R.id.tv_shop_name);
        tvShopName.setOnClickListener(this);
        tvShopPrice = (TextView) selectView.findViewById(R.id.tv_shop_price);
        tvShopColor = (TextView) selectView.findViewById(R.id.tv_shop_color);
        tvShopNum = (TextView) selectView.findViewById(R.id.tv_shop_num);
        tvShopSize = (TextView) selectView.findViewById(R.id.tv_shop_size);
        btnShopCut = (Button) selectView.findViewById(R.id.btn_shop_cut);
        btnShopCut.setOnClickListener(this);
        btnShopAdd = (Button) selectView.findViewById(R.id.btn_shop_add);
        btnShopAdd.setOnClickListener(this);
        btnShopCart = (Button) selectView.findViewById(R.id.btn_buy_input_message);
        flContainer = (FlowLayout) selectView.findViewById(R.id.fl_container);
        flContainer2 = (FlowLayout) selectView.findViewById(R.id.fl_container_size);
        btnShopCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinToCart();
            }
        });
        ivClose = (ImageView) selectView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        ivShopPhoto = (ImageView) selectView.findViewById(R.id.iv_shop_photo);
        tvShopName.setText(productResponse.product.name);
        tvShopPrice.setText("¥" + productResponse.product.price);
        ivShopPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView tv;
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
        for (ProductResponse.ProductBean.ProductPropertyBean propertyBean : productResponse.product.productProperty) {
            if ("颜色".equals(propertyBean.k)) {
                tv = new TextView(getActivity());

                tv.setBackgroundDrawable(DrawableUtil.generateDrawable(Color.rgb(231, 228, 228), 8));
                tv.setText(propertyBean.v);
                tv.setPadding(8, 8, 8, 8);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(R.color.selector_text_product_color));
                flContainer.addView(tv);
                colorList.add(tv);
            } else if ("尺码".equals(propertyBean.k)) {
                tv = new TextView(getActivity());
                tv.setBackgroundDrawable(DrawableUtil.generateDrawable(Color.rgb(231, 228, 228), 8));
                tv.setText(propertyBean.v);
                tv.setPadding(8, 8, 8, 8);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(R.color.selector_text_product_color));
                flContainer2.addView(tv);
                sizeList.add(tv);
            }
        }
        setOnClick();
        String url;
        if (productResponse.product.bigPic.size() == 0) {
            url = productResponse.product.pics.get(0);
        } else {
            url = productResponse.product.bigPic.get(0);
        }
        HttpLoader.getInstance(getActivity()).display(ivShopPhoto,
                Api.URL_SERVER + url,
                R.drawable.image_border, R.drawable.image_border);
    }

    public void setOnClick() {
        colorList.get(0).setTextColor(Color.RED);
        colorList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorList.get(0).setTextColor(Color.RED);
                colorList.get(1).setTextColor(Color.BLACK);
                colorChecked = 0;
            }
        });
        colorList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorList.get(0).setTextColor(Color.BLACK);
                colorList.get(1).setTextColor(Color.RED);
                colorChecked = 1;
            }
        });
        sizeList.get(0).setTextColor(Color.RED);
        sizeList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeList.get(0).setTextColor(Color.RED);
                sizeList.get(1).setTextColor(Color.BLACK);
                sizeList.get(2).setTextColor(Color.BLACK);
                sizeChecked = 0;
            }
        });
        if (sizeList.size() > 1) {
            sizeList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sizeList.get(0).setTextColor(Color.BLACK);
                    sizeList.get(1).setTextColor(Color.RED);
                    sizeList.get(2).setTextColor(Color.BLACK);
                    sizeChecked = 1;
                }
            });
        } else if (sizeList.size() > 2) {
            sizeList.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sizeList.get(0).setTextColor(Color.BLACK);
                    sizeList.get(1).setTextColor(Color.BLACK);
                    sizeList.get(2).setTextColor(Color.RED);
                    sizeChecked = 2;
                }
            });
        }
    }
    public interface OnCartNumChangeListener{
        void onChange();
    }
    private OnCartNumChangeListener listener;
    public void setOnCartNumChangeListener(OnCartNumChangeListener listener){
        this.listener = listener;
    }
}
