package com.sugar.myapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.AboutActivity;
import com.sugar.myapplication.activity.HelpActivity;
import com.sugar.myapplication.activity.OrderDetailActivity;
import com.sugar.myapplication.adapter.DragAdapter;
import com.sugar.myapplication.adapter.OtherAdapter;
import com.sugar.myapplication.bean.ChannelItem;
import com.sugar.myapplication.bean.ChannelManage;
import com.sugar.myapplication.manager.JumpToLoginManager;
import com.sugar.myapplication.view.DragGrid;
import com.sugar.myapplication.view.OtherGridView;
import com.sugar.myapplication.view.PullToZoomScrollViewEx;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by wjx on 2016/11/28.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private View view;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    DragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    OtherAdapter otherAdapter;
    /**
     * 其它栏目列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /**
     * 用户栏目列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    private DragGrid userGridView;
    private OtherGridView otherGridView;
    private RelativeLayout rlAboutUs;
    private RelativeLayout rlHelp;
    private RelativeLayout rl_mine_order_detail_order;
    private TextView tvLogin;
    private TextView tvRegister;
    private View headView;

    @Override
    public void onStart() {
        super.onStart();
        //view.scrollTo(0,0);
        headView.scrollTo(0, 0);
        if (App.userID != null) {
            tvLogin.setText("金牌会员");
            tvLogin.setOnClickListener(null);
            tvRegister.setText("吃瓜群众");
            tvRegister.setOnClickListener(null);
        }
    }

    @Override
    protected View getSuccessView() {
        view = View.inflate(getActivity(), R.layout.mine_back_up_mine, null);
        loadViewForCode();
        if (App.userID == null) {
            tvLogin.setText("登录");
            tvRegister.setText("注册");
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToLoginManager.isJumpToLogin(getActivity());
                }
            });
            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToLoginManager.isJumpToRegister(getActivity());
                }
            });
        }
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(App.getApp().getSQLHelper()).getUserChannel());
        otherChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(App.getApp().getSQLHelper()).getOtherChannel());
        userAdapter = new DragAdapter(getActivity(), userChannelList);
        otherAdapter = new OtherAdapter(getActivity(), otherChannelList);
        userGridView.setAdapter(userAdapter);
        otherGridView.setAdapter(otherAdapter);
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        //设置GRIDVIEW的ITEM的点击监听
        return view;
    }


    @Override
    protected Object requestData() {
        return new Object();
    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_head_view, null, false);
        //ButterKnife.inject(this,headView);
        tvLogin = (TextView) headView.findViewById(R.id.tv_login);
        //tvLogin.setOnClickListener(this);
        tvRegister = (TextView) headView.findViewById(R.id.tv_register);
        //tvRegister.setOnClickListener(this);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_content_view, null, false);
        //ButterKnife.inject(this, contentView);
        userGridView = (DragGrid) contentView.findViewById(R.id.userGridView);
        //进入我的订单页面
        rl_mine_order_detail_order = (RelativeLayout) contentView.findViewById(R.id.rl_mine_order_detail_order);
        rl_mine_order_detail_order.setOnClickListener(this);
        otherGridView = (OtherGridView) contentView.findViewById(R.id.otherGridView);
        rlAboutUs = (RelativeLayout) contentView.findViewById(R.id.rl_mine_about_us);
        rlAboutUs.setOnClickListener(this);
        rlHelp = (RelativeLayout) contentView.findViewById(R.id.rl_mine_help);
        rlHelp.setOnClickListener(this);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mine_about_us:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.rl_mine_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.rl_mine_order_detail_order://我的订单
                //点击单开我的订单页面
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0，1 的不可以进行任何操作
                if (position != 0 && position != 1) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        //添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(getActivity());
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getActivity());
        iv.setImageBitmap(cache);
        return iv;
    }

}
