package com.sugar.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.GoodsInfoAdapter;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.fragment.EvaluationFragment;
import com.sugar.myapplication.fragment.GoodsInfoFragment;
import com.sugar.myapplication.fragment.PhotoDetailFragment;
import com.sugar.myapplication.manager.JumpToLoginManager;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.PagerSlidingTab;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GoodsInfoActivity extends AppCompatActivity implements GoodsInfoFragment.OnCartNumChangeListener{

    @InjectView(R.id.iv_back)
    ImageButton mIvBack;
    @InjectView(R.id.ll_back)
    LinearLayout mLlBack;
    @InjectView(R.id.pst_title)
    PagerSlidingTab mPstTitle;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.ib_goods_info_detail)
    ImageButton mIbGoodsInfoDetail;
    @InjectView(R.id.ll_title_root)
    LinearLayout mLlTitleRoot;
    @InjectView(R.id.vp_content)
    ViewPager mVpContent;
    @InjectView(R.id.tv_goods_info_care)
    TextView tvGoodsInfoCare;
    @InjectView(R.id.tv_goods_info_cart)
    TextView tvGoodsInfoCart;
    @InjectView(R.id.tv_join_cart)
    TextView tvJoinCart;
    @InjectView(R.id.tv_cart_num)
    TextView tvCartNum;
    @InjectView(R.id.fl_container_cart)
    FrameLayout flContainerCart;


    private List<Fragment> mFragmentList = new ArrayList<>();

    private int productId;
    private MyCartDao mcd = new MyCartDao();
    private MyCartDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        productId = intent.getIntExtra("id", 1);
        Log.e("-------", "onCreate: " + productId);
        GoodsInfoFragment goodsInfoFragment = new GoodsInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pId", productId);
        goodsInfoFragment.setArguments(bundle);
        mFragmentList.add(goodsInfoFragment);
        goodsInfoFragment.setOnCartNumChangeListener(this);
        PhotoDetailFragment photoDetailFragment = new PhotoDetailFragment();
        photoDetailFragment.setArguments(bundle);
        mFragmentList.add(photoDetailFragment);

        EvaluationFragment evaluationFragment = new EvaluationFragment();
        evaluationFragment.setArguments(bundle);
        mFragmentList.add(evaluationFragment);

        mVpContent.setAdapter(new GoodsInfoAdapter(getSupportFragmentManager(), mFragmentList));
        mPstTitle.setViewPager(mVpContent);
        //加载所有的页面
        mVpContent.setOffscreenPageLimit(3);
        //EventBus.getDefault().register(this);
        dao = new MyCartDao();
        queryData();
    }

    public void queryData() {
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                final int size = dao.queryAllGoodsCount();
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (size>0) {
                            flContainerCart.setVisibility(View.VISIBLE);
                            tvCartNum.setText(size+"");
                        }else{
                            flContainerCart.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ib_goods_info_detail, R.id.tv_goods_info_care, R.id.tv_goods_info_cart, R.id.tv_join_cart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_goods_info_detail:
                break;
            case R.id.tv_goods_info_care://关注
                if (App.userID == null) {
                    ToastUtil.showToast("使用关注功能之前需要登录");
                    if (!JumpToLoginManager.isJumpToLogin(this)) {
                        //不需要跳转至登录页面
                        ToastUtil.showToast("关注成功");
                    }
                }
                break;
            case R.id.tv_goods_info_cart://购物车
                Intent in= new Intent(GoodsInfoActivity.this,MainActivity.class);
                in.putExtra("jump","fromDetailToCart");
                startActivity(in);
                //finish();
                break;

            //点击加入购物车
            case R.id.tv_join_cart:
                ToastUtil.showToast("加入购物车成功!");
                mcd.addCartFromDetail(productId, 1);
                queryData();
                break;
        }
    }

    @Override
    public void onChange() {
        ToastUtil.showToast("收到消息");
        queryData();
    }
}
