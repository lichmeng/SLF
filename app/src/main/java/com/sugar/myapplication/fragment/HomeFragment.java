package com.sugar.myapplication.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.slf.TranslucentScrollView;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.BrandActivity;
import com.sugar.myapplication.activity.HomeMsgActivity;
import com.sugar.myapplication.activity.HotProductActivity;
import com.sugar.myapplication.activity.LimitBuyActivity;
import com.sugar.myapplication.activity.NewProductActivity;
import com.sugar.myapplication.activity.PromotionsActivity;
import com.sugar.myapplication.activity.SearchActivity;
import com.sugar.myapplication.adapter.HomeTopicPagerAdapter;
import com.sugar.myapplication.adapter.SeckillRvAdapter;
import com.sugar.myapplication.bean.HomeResponse;
import com.sugar.myapplication.bean.LimitBuyReaponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.view.DepthPageTransformer;
import com.sugar.myapplication.view.FixedSpeedScroller;
import com.sugar.myapplication.view.LoadingPage;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.HttpParams;
import org.senydevpkg.net.resp.IResponse;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sugar on 2016/11/22.
 */

public class HomeFragment extends BaseFragment implements HttpLoader.HttpListener,
        View.OnClickListener, TranslucentScrollView.OnScrollChangedListener {

    private FixedSpeedScroller mScroller;
    //feature
    @InjectView(R.id.btn_limit_buy)
    Button mBtnLimitBuy;
    @InjectView(R.id.btn_promotions)
    Button mBtnPromotions;
    @InjectView(R.id.btn_new_product)
    Button mBtnNewProduct;
    @InjectView(R.id.btn_hot_product)
    Button mBtnHotProduct;
    @InjectView(R.id.btn_recommend)
    Button mBtnRecommend;
    @InjectView(R.id.btn_brand)
    Button mBtnBrand;
    @InjectView(R.id.btn_favorite)
    Button mBtnFavorite;
    @InjectView(R.id.btn_shopping_cart)
    Button mBtnShoppingCart;
    int[] buttonBg = {R.drawable.bg_blue, R.drawable.bg_green,
            R.drawable.bg_orange, R.drawable.bg_purple, R.drawable.bg_red};
    //topic
    @InjectView(R.id.vp_home_topic)
    ViewPager mViewPager;
    @InjectView(R.id.ll_container)
    LinearLayout mLlContainer;
    //news
    @InjectView(R.id.tv_news)
    TextView mTvNews;
    private String[] news = {
            "注册送好礼,iWatch智能手表",
            "共度圣诞狂欢购翻天",
            "双12锯惠,满199减198",
            "享岁末抄底价厚礼超乎你想象!",
            "老用户购买指定商品送免邮券",
            "十二狂欢豪礼派送全天不停"
    };

    //seckill
    @InjectView(R.id.rv_seckill)
    RecyclerView mRvSeckill;
    @InjectView(R.id.tv_left_time)
    TextView mTvLeftTime;
    @InjectView(R.id.tv_more)
    TextView mTvMore;
    private int time = 5 * 60 * 60 * 1000;

    private List<HomeResponse.HomeTopicBean> mHomeTopic;
    //private ViewPager mViewPager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++currentItem);
            //
            mTvNews.setText(news[mNewsPos]);
            mNewsPos++;
            if (mNewsPos == news.length) {
                mNewsPos = 0;
            }

            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private TranslucentScrollView mScrollView;
    private List<LimitBuyReaponse.ProductListBean> mSeckillList;
    private int mNewsPos = 1;
    private Handler mTimeHandler;
    //toolbar

    private float headerHeight;
    private float minHeaderHeight;
    private Toolbar mToolBar;
    private TextView mTvSearch;
    private TextView mTvScan;
    private TextView mTvMsg;
    private ImageView mIvScan;
    private ImageView mIvMsg;
    private ImageView mIvSearch;
    private ImageView mIvMic;
    private HomeResponse mHomeResponse;
    private PullToRefreshScrollView mRefreshScrollView;

    @Override
    protected View getSuccessView() {
        View view = View.inflate(mActivity, R.layout.fragment_home, null);
        setToolBar(view);
        initView(view);
        initMeasure();
        initScrollView();
        return view;
    }

    private void initView(View view) {
        mTvScan = (TextView) view.findViewById(R.id.tv_scan);
        mTvMsg = (TextView) view.findViewById(R.id.tv_msg);
        mIvScan = (ImageView) view.findViewById(R.id.iv_scan);
        mIvMsg = (ImageView) view.findViewById(R.id.iv_msg);
        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
        mIvMic = (ImageView) view.findViewById(R.id.iv_mic);
        mRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.ptr_scrollview);
        mRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mScrollView = mRefreshScrollView.getRefreshableView();
        //mScrollView = (TranslucentScrollView) view.findViewById(R.id.tsv_scrollview);
        mScrollView.setOnScrollChangedListener(this);
        mIvMsg.setOnClickListener(this);
    }

    private void setToolBar(View view) {
        mToolBar = (Toolbar) view.findViewById(R.id.tb_toolbar);
        mToolBar.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mActivity.setSupportActionBar(mToolBar);
        mTvSearch = (TextView) view.findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(this);
    }

    private void initScrollView() {
        mRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<TranslucentScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<TranslucentScrollView> refreshView) {
                if (mRefreshScrollView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    refreshHomePage();
                }
            }
        });
        View container = View.inflate(getActivity(), R.layout.layout_home_scrollview, null);
        ButterKnife.inject(this, container);
        bindData();
        mScrollView.addView(container);
    }
    //随机更换颜色
    private void refreshHomePage() {
        mToolBar.setVisibility(View.INVISIBLE);
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mToolBar.setVisibility(View.VISIBLE);
                        requestData();
                        Random random = new Random();
                        mBtnLimitBuy.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnPromotions.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnNewProduct.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnHotProduct.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnRecommend.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnBrand.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnFavorite.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mBtnShoppingCart.setBackgroundResource(buttonBg[random.nextInt(5)]);
                        mRefreshScrollView.onRefreshComplete();
                    }
                });
            }
        });
    }

    private void bindData() {

        mBtnLimitBuy.setOnClickListener(this);
        mBtnPromotions.setOnClickListener(this);
        mBtnNewProduct.setOnClickListener(this);
        mBtnHotProduct.setOnClickListener(this);
        mBtnRecommend.setOnClickListener(this);
        mBtnBrand.setOnClickListener(this);
        mBtnFavorite.setOnClickListener(this);
        mBtnShoppingCart.setOnClickListener(this);
        //topic
        setViewPagerScrollSpeed();//切换速度
        mViewPager.setPageTransformer(true, new DepthPageTransformer());//切换动画
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int relPos = position % mHomeTopic.size();
                for (int i = 0;i < mLlContainer.getChildCount();i++) {
                    ImageView imageView = (ImageView) mLlContainer.getChildAt(i);
                    if (i == relPos) {
                        imageView.setEnabled(true);
                    }else {
                        imageView.setEnabled(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //news
        mTvNews.setText(news[0]);
        //seckill
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSeckill.setLayoutManager(layoutManager);

        mTvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LimitBuyActivity.class);
                startActivity(intent);
            }
        });

        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        mTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                time -= 1000;
                Date date = new Date(time);
                String leftTime = format.format(date);
                mTvLeftTime.setText(leftTime);

                mTimeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        };

    }

    @Override
    protected Object requestData() {
        App.mLoader.get(Api.URL_HOME, null, HomeResponse.class, Api.REQUEST_CODE_TOPIC, this);
        HttpParams params = new HttpParams();
        params.put("page", "1");
        params.put("pageNum", "12");
        App.mLoader.get(Api.URL_LIMITBUY, params, LimitBuyReaponse.class, Api.REQUEST_CODE_LIMITBUY, this);
        return mHomeResponse;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, IResponse response) {
        loadingPage.refreshState(LoadingPage.PageState.STATE_SUCCESS);
        switch (requestCode) {
            case Api.REQUEST_CODE_TOPIC:
                mHomeResponse = (HomeResponse) response;
                mHomeTopic = mHomeResponse.getHomeTopic();
                if (mHomeTopic != null) {
                    mViewPager.setAdapter(new HomeTopicPagerAdapter(mHomeTopic));
                    mViewPager.setCurrentItem(mHomeTopic.size() * 1000);
                    //topic
                    mLlContainer.removeAllViews();
                    for (int i = 0; i < mHomeTopic.size();i++) {
                        ImageView imageView = new ImageView(mActivity);
                        imageView.setImageResource(R.drawable.selector_home_topic_indicator);
                        imageView.setEnabled(false);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
                        if (i > 0) {
                            params.leftMargin = 10;
                        }
                        imageView.setLayoutParams(params);
                        mLlContainer.addView(imageView);
                    }
                }
                break;
            case Api.REQUEST_CODE_LIMITBUY:
                LimitBuyReaponse seckillRresponse = (LimitBuyReaponse) response;
                mSeckillList = seckillRresponse.getProductList();
                if (mSeckillList != null) {
                    mRvSeckill.setAdapter(new SeckillRvAdapter(mSeckillList));
                }
                break;
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        loadingPage.refreshState(LoadingPage.PageState.STATE_ERROR);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.sendEmptyMessageDelayed(0, 3000);
        mTimeHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        mTimeHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_msg:
                intent = new Intent(getActivity(), HomeMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_promotions:
                intent = new Intent(getActivity(), PromotionsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_limit_buy:
                intent = new Intent(getActivity(), LimitBuyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recommend:
                intent = new Intent(getActivity(), BrandActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_new_product:
                intent = new Intent(getActivity(), NewProductActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hot_product:
                intent = new Intent(getActivity(), HotProductActivity.class);
                startActivity(intent);
                break;
        }
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

    private void initMeasure() {
        headerHeight = getResources().getDimension(R.dimen.dimen_300);
        minHeaderHeight = getResources().getDimension(R.dimen.abc_action_bar_default_height_material);

    }

    private void setViewPagerScrollSpeed(){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext( ) );
            mScroller.set(mViewPager, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        //Y轴偏移量
        float scrollY = who.getScrollY();

        //变化率
        float headerBarOffsetY = headerHeight - minHeaderHeight;//Toolbar与header高度的差值
        float offset = 1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f);

        //Toolbar背景色透明度
        mToolBar.setBackgroundColor(Color.argb((int) (offset * 255), 255, 255, 255));
        //文字变色
        if (offset > 0.5) {
            mTvScan.setTextColor(Color.BLACK);
            mTvSearch.setTextColor(Color.GRAY);
            mTvMsg.setTextColor(Color.BLACK);
            mIvScan.setImageResource(R.drawable.ic_scan_black);
            mIvMsg.setImageResource(R.drawable.ic_msg_black);
            mIvSearch.setImageResource(R.drawable.ic_search_gray);
            mIvMic.setImageResource(R.drawable.ic_mic_gray);
        } else {
            mTvScan.setTextColor(Color.WHITE);
            mTvSearch.setTextColor(Color.WHITE);
            mTvMsg.setTextColor(Color.WHITE);
            mIvScan.setImageResource(R.drawable.ic_scan_white);
            mIvMsg.setImageResource(R.drawable.ic_msg_white);
            mIvSearch.setImageResource(R.drawable.ic_search_white);
            mIvMic.setImageResource(R.drawable.ic_mic_white);
        }
//        //header背景图Y轴偏移
//        imgHead.setTranslationY(scrollY / 2);
    }
}