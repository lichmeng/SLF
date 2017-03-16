package com.sugar.myapplication.activity;

import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.readystatesoftware.viewbadger.BadgeView;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.SearchResultAdapter;
import com.sugar.myapplication.bean.ProductListInfo;
import com.sugar.myapplication.bean.SearchResultResponse;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.event.CartChangeEvent;
import com.sugar.myapplication.global.Constants;
import com.sugar.myapplication.global.SPContants;
import com.sugar.myapplication.util.SPUtil;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.CircleImageView;

import org.apache.http.params.HttpParams;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.sugar.myapplication.R.id.ib_search_cart;
import static com.sugar.myapplication.R.id.rb_order_summary;

public class ProductListActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        PullToRefreshBase.OnRefreshListener,
        AbsListView.OnScrollListener,
        AdapterView.OnItemLongClickListener {

    @InjectView(R.id.ib_category_back)
    ImageButton mIbCategoryBack;
    @InjectView(R.id.tv_search_keyword)
    TextView mTvKeyword;
    @InjectView(R.id.ib_mode_lv)
    ImageButton mIbModeLv;
    @InjectView(R.id.ib_mode_gv)
    ImageButton mIbModeGv;
    @InjectView(R.id.ll_search_bar)
    LinearLayout mLlSearchBar;
    @InjectView(rb_order_summary)
    RadioButton mRbOrderSummary;
    @InjectView(R.id.rb_order_salenum)
    RadioButton mRbOrderSalenum;
    @InjectView(R.id.rb_order_price)
    RadioButton mRbOrderPrice;
    @InjectView(R.id.rb_order_condition)
    RadioButton mRbOrderCondition;
    @InjectView(R.id.rg_order)
    RadioGroup mRgOrder;
    @InjectView(R.id.lv_display_mode)
    PullToRefreshListView mLvDisplayMode;
    @InjectView(R.id.gv_display_mode)
    PullToRefreshGridView mGvDisplayMode;
    @InjectView(R.id.ib_back_top)
    ImageButton mIbBackTop;
    @InjectView(R.id.ib_back_record)
    ImageButton mIbBackRecord;
    @InjectView(R.id.rl_container)
    RelativeLayout mRlContainer;
    @InjectView(ib_search_cart)
    ImageButton mIbSearchCart;
    @InjectView(R.id.tb_search_toolbar)
    Toolbar mTbSearchToolbar;
    @InjectView(R.id.ll_no_search_result)
    LinearLayout mLlNoSearchResult;

    private String mKeyWord;

    private SearchResultAdapter mAdapterLv;
    private ListView refreshListView;

    private SearchResultAdapter mAdapterGv;
    private GridView refreshGridView;

    //    填充listview的假数据 search接口不能访问
    private List<ProductListInfo> mDataForListView;
    //排序选择的popwindow
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search_list);
        ButterKnife.inject(this);
        setSupportActionBar(mTbSearchToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLvDisplayMode.setNestedScrollingEnabled(true);
            mGvDisplayMode.setNestedScrollingEnabled(true);
        }
        ViewCompat.setNestedScrollingEnabled(mLvDisplayMode, true);
        ViewCompat.setNestedScrollingEnabled(mGvDisplayMode, true);
        //获取搜索关键字
        initKeyWord();
        //网络没有数据,所以用的假数据
        fetchDataByKeyWord(null);

        initListViewAndGridView();

    }

    /**
     * 初始化listview和gridview
     */
    private void initListViewAndGridView() {
        //先都隐藏
        mGvDisplayMode.setVisibility(View.INVISIBLE);
        mLvDisplayMode.setVisibility(View.VISIBLE);

        //初始化listview
        refreshListView = mLvDisplayMode.getRefreshableView();
        refreshListView.setDividerHeight(0);
        mLvDisplayMode.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLvDisplayMode.setOnRefreshListener(this);

        //初始化gridview
        refreshGridView = mGvDisplayMode.getRefreshableView();
        refreshGridView.setNumColumns(2);
        mGvDisplayMode.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mGvDisplayMode.setOnRefreshListener(this);

        //设置2个的adapter
        setAdapterOrNotifiy();

        //增加监听
        mLvDisplayMode.setOnItemClickListener(this);
        mLvDisplayMode.setOnScrollListener(this);
        refreshListView.setOnItemLongClickListener(this);

        mGvDisplayMode.setOnItemClickListener(this);
        mGvDisplayMode.setOnScrollListener(this);
        refreshGridView.setOnItemLongClickListener(this);

    }

    private void setAdapterOrNotifiy() {

        if (mAdapterLv == null) {
            mAdapterLv = new SearchResultAdapter(this, mDataForListView, SearchResultAdapter.MODE_LISTVIEW);
            refreshListView.setAdapter(mAdapterLv);
        } else {
            mAdapterLv.notifyDataSetChanged();
        }

        //gridview
        if (mAdapterGv == null) {
            mAdapterGv = new SearchResultAdapter(this, mDataForListView, SearchResultAdapter.MODE_GRIDVIEW);
            refreshGridView.setAdapter(mAdapterGv);
        } else {
            mAdapterGv.notifyDataSetChanged();
        }


    }

    /**
     * 从服务器获取数据
     *
     * @param
     */
    private void fetchDataByKeyWord(HttpParams p) {
        Gson gson = new Gson();

        SearchResultResponse testSearchResult = gson.fromJson(Constants.testJson, SearchResultResponse.class);
        mDataForListView = testSearchResult.getProductList();
//        HttpParams params = new HttpParams();
//        params.put("page","1");
//        params.put("pageNum","20");
//        params.put("orderby","saleDown");
//        params.put("keyword","fen");
//        HttpLoader.HttpListener listener = new SearchListener();
//        HttpLoader.getInstance(this).get(Api.URL_SEARCH,params, SearchResultResponse.class,Api.REQUEST_CODE_SEARCH_LIST,listener);
    }

    /**
     * listView的条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //要区分是listview还是gridview,因为pisition不一样
        boolean b = parent instanceof ListView;
        ProductListInfo productListInfo = mDataForListView.get(b ? position - 1 : position);

        Intent intent = new Intent(this, GoodsInfoActivity.class);
        intent.putExtra("id", productListInfo.getId());
        startActivity(intent);
    }

    /**
     * 滑动监听
     *
     * @param view
     * @param scrollState
     */
    //切换显示模式时,第一个显示的位置不变
    private int currentDisplayItem;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        currentDisplayItem = firstVisibleItem ;
        if (firstVisibleItem > visibleItemCount * 1.5) {
            mIbBackTop.setVisibility(View.VISIBLE);
        } else {
            mIbBackTop.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载更多的监听
     *
     * @param refreshView
     */
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        boolean b = refreshView instanceof PullToRefreshListView;

        mDataForListView.addAll(mDataForListView.subList(0, mDataForListView.size() / 2));

        setAdapterOrNotifiy();

        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLvDisplayMode.onRefreshComplete();
                mGvDisplayMode.onRefreshComplete();
            }
        }, 1500);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        //计算偏移量
        boolean b = parent instanceof ListView;
        final ProductListInfo pInfo = mDataForListView.get(b ? position - 1 : position);

        //长按显示dialog对话框,可选加入购物车还是关注
        View dialogview = View.inflate(this, R.layout.view_dialog, null);
        Button btnAdd2Cart = (Button) dialogview.findViewById(R.id.btn_add2cart);
        Button btnAdd2Forcus = (Button) dialogview.findViewById(R.id.btn_add2forcus);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogview).show();

        //给2个按钮增加点击
        btnAdd2Forcus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(pInfo.getName() + "已加关注");
                dialog.dismiss();
            }
        });
        btnAdd2Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //当点击加入购物车,就显示购物车按钮
                mIbSearchCart.setVisibility(View.VISIBLE);
                //加入购物车的动画
                ImageView item = (ImageView) view.findViewById(R.id.iv_product_icon);
                //得到动画执行的商品
                addToCartAnim(item, pInfo);

            }
        });
        return true;
    }

    //计算动画中间值
    private PathMeasure mPathMeasure;
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];

    //角标,显示购物车数量
    private BadgeView shoppintCount;

    /**
     * 加入購物車的動畫
     *
     * @param iv
     * @param pInfo
     */
    private void addToCartAnim(ImageView iv, final ProductListInfo pInfo) {
        //被执行动画的主体
//        final ImageView goods = new ImageView(ProductListActivity.this);
        final CircleImageView goods = new CircleImageView(ProductListActivity.this);
        goods.setImageDrawable(iv.getDrawable());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(110, 110);
        mRlContainer.addView(goods, params);
        //拿到动画起始点和终点
        //父窗体的坐标,辅助计算
        int[] parentLocation = new int[2];
        mRlContainer.getLocationInWindow(parentLocation);

        //点击条目的坐标
        int[] startLoc = new int[2];
        iv.getLocationInWindow(startLoc);

        //购物车在屏幕上的坐标
        int[] endLoc = new int[2];
        mIbSearchCart.getLocationInWindow(endLoc);

        //三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + mIbBackTop.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        //四、计算中间动画的插值坐标（贝塞尔曲线.用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //漂亮的抛物线
        path.quadTo((startX + toX) / 2, startY - ((startY + toY) / 2), toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1300);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                //mCurrentPosition此时就是中间距离点的坐标值
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
        //同时增加一个先变大在变小到0的动画
//        ViewHelper.setScaleX(goods,0.2f);
//        ViewHelper.setScaleY(goods,0.2f);
        ViewPropertyAnimator.animate(goods)
                .scaleX(0.2f)
                .scaleY(0.2f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(1300)
                .start();

        //透明度动画
        ViewPropertyAnimator.animate(goods)
                .alpha(0f)
                .setDuration(1300)
                .setInterpolator(new AccelerateInterpolator())
                .start();
        //五、 开始执行动画
        valueAnimator.start();
        //六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                mRlContainer.removeView(goods);
                //数据加入数据库
                MyCartDao myCartDao = new MyCartDao();
                myCartDao.addCartFromDetail(pInfo.getId(), 1);

                //将购物车数量变化的消息post给mainactivity,by_gu
                EventBus.getDefault().post(new CartChangeEvent(myCartDao.queryAllGoodsCount()));
                //购物车动画,
                //购物车动画:缩放动画
                ViewHelper.setScaleX(mIbSearchCart, 0.85f);
                ViewHelper.setScaleY(mIbSearchCart, 0.85f);
                ViewPropertyAnimator.animate(mIbSearchCart)
                        .scaleX(1.0f)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(400)
                        .start();
                ViewPropertyAnimator.animate(mIbSearchCart)
                        .scaleY(1.0f)
                        .setInterpolator(new CycleInterpolator(3))
                        .setDuration(400)
                        .start();

                //加角标, mIbSearchCart
                if (shoppintCount == null) {
                    shoppintCount = new BadgeView(ProductListActivity.this, mIbSearchCart);
                }
                shoppintCount.setText(myCartDao.queryAllGoodsCount() + "");
                shoppintCount.show();

                //shoppintCount同步播放动画
                ViewHelper.setScaleX(shoppintCount, 0.95f);
                ViewHelper.setScaleY(shoppintCount, 0.85f);
                ViewPropertyAnimator.animate(shoppintCount)
                        .scaleX(1.0f)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(400)
                        .start();
                ViewPropertyAnimator.animate(shoppintCount)
                        .scaleY(1.0f)
                        .setInterpolator(new CycleInterpolator(3))
                        .setDuration(400)
                        .start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
    /**
     * httploader的访问回调
     */
    /*class SearchListener implements HttpLoader.HttpListener {
        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {
            if (requestCode == Api.REQUEST_CODE_SEARCH_LIST) {
                SearchResultResponse searchResult = (SearchResultResponse) response;
            }
        }
        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            error.printStackTrace();
            ToastUtil.showToast("网络出现了问题");
        }
    }*/

    /**
     * 获取搜索输入框中的keyword数据
     */
    private void initKeyWord() {
        Intent intent = getIntent();
        if (intent != null) {
            mKeyWord = intent.getStringExtra("keyword");
             ToastUtil.showToast("keyword:" + mKeyWord);
        }
        if (TextUtils.isEmpty(mKeyWord)) {
            mLlNoSearchResult.setVisibility(View.VISIBLE);
            mRlContainer.setVisibility(View.INVISIBLE);
        } else if (mKeyWord.charAt(0)<127) {
            mLlNoSearchResult.setVisibility(View.VISIBLE);
            mRlContainer.setVisibility(View.INVISIBLE);
        } else {
            mLlNoSearchResult.setVisibility(View.INVISIBLE);
            mRlContainer.setVisibility(View.VISIBLE);
        }
        mTvKeyword.setText(mKeyWord);
    }

    /**
     * 布局里面的点击事件
     *
     * @param view
     */
    @OnClick({R.id.ib_category_back, R.id.ib_mode_lv,
            R.id.ib_mode_gv, R.id.ll_search_bar,
            R.id.ib_back_top, R.id.ib_back_record, ib_search_cart,
            rb_order_summary})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_category_back:
                //返回上一页
                finish();
                break;
            case R.id.ib_mode_lv:
                //显示listview
                showListView();
                break;
            case R.id.ib_mode_gv:
                //显示gridview
                showGridView();
                break;
            case R.id.ll_search_bar:
                //进入搜索输入的activity
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ib_back_top:
                //回到顶部
                mLvDisplayMode.getRefreshableView().smoothScrollToPosition(0);
                mGvDisplayMode.getRefreshableView().smoothScrollToPosition(0);
                break;
            case R.id.ib_back_record:
                //查看浏览记录,需要先判断是否登录,没有登录跳到登录界面,登录了,跳到浏览记录
                String uId = SPUtil.getString(this, SPContants.USER_ID, "");
                if (TextUtils.isEmpty(uId)) {
                    //用户还没有登录过
                    Intent login = new Intent(this, LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.ib_search_cart:
                //进入购物车,
//                if (mainActivity!=null) {
//                    mainActivity.showCart();
//                    finish();
//                }
                Intent intent1 = new Intent(ProductListActivity.this, MainActivity.class);
                intent1.putExtra("jump", "toCart");
                startActivity(intent1);
                break;
            case R.id.rb_order_summary:
                //综合排序,弹出一个popupwindow,让用户选择哪种排序
                if (mPopupWindow == null) {
                    selectSortMode(mRbOrderSummary);
                } else {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
                break;
        }
    }

    /**
     * 弹出排序选择popwindow选择排序方式
     */
    private int currentChoice = 1;

    private void selectSortMode(View view) {
        View popview = View.inflate(this, R.layout.view_popwindow, null);
        final TextView one = (TextView) popview.findViewById(R.id.tv_pop_one);
        final TextView two = (TextView) popview.findViewById(R.id.tv_pop_two);
        final TextView three = (TextView) popview.findViewById(R.id.tv_pop_three);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAsDropDown(view);

        /**
         * pop条目的正确显示,记录住之前的选择
         */
        switch (currentChoice) {
            case 1:
                one.setEnabled(false);
                two.setEnabled(true);
                three.setEnabled(true);
                break;
            case 2:
                one.setEnabled(true);
                two.setEnabled(false);
                three.setEnabled(true);
                break;
            case 3:
                one.setEnabled(true);
                two.setEnabled(true);
                three.setEnabled(false);
                break;
        }
        /**
         * 3中排序方式的点击事件,点击相应的条目
         */
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setEnabled(false);
                two.setEnabled(true);
                three.setEnabled(true);
                mRbOrderSummary.setText("综合");
                currentChoice = 1;
                mPopupWindow.dismiss();

                //重新排序
                Collections.sort(mDataForListView);
                setAdapterOrNotifiy();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setEnabled(true);
                two.setEnabled(false);
                three.setEnabled(true);
                mRbOrderSummary.setText("新品");
                currentChoice = 2;

                mPopupWindow.dismiss();

                //重新排序
                Collections.sort(mDataForListView);
                setAdapterOrNotifiy();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setEnabled(true);
                two.setEnabled(true);
                three.setEnabled(false);
                mRbOrderSummary.setText("评论");
                currentChoice = 3;

                mPopupWindow.dismiss();

                //重新排序
                Collections.sort(mDataForListView);
                setAdapterOrNotifiy();

            }
        });

    }


    /**
     * 显示listView
     */
    private void showListView() {
        mGvDisplayMode.setVisibility(View.INVISIBLE);
        mLvDisplayMode.setVisibility(View.VISIBLE);

        mIbModeGv.setVisibility(View.VISIBLE);
        mIbModeLv.setVisibility(View.INVISIBLE);

        refreshListView.setSelection(currentDisplayItem);
    }

    /**
     * 显示gridiew
     */
    private void showGridView() {
        mLvDisplayMode.setVisibility(View.INVISIBLE);
        mGvDisplayMode.setVisibility(View.VISIBLE);


        mIbModeLv.setVisibility(View.VISIBLE);
        mIbModeGv.setVisibility(View.INVISIBLE);
        refreshGridView.setSelection(currentDisplayItem);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mHandler.removeCallbacksAndMessages(null);
        //退出时隐藏购物车按钮
        mIbSearchCart.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取到mainactivity的引用
     * @param obj
     */
}
