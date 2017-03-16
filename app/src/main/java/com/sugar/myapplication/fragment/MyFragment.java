package com.sugar.myapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by sugar on 2016/11/22.
 */

public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener {
//    @InjectView(R.id.iv_mine_setting)
//    ImageView ivMineSetting;
//    @InjectView(R.id.iv_mine_session)
//    ImageView ivMineSession;
//    @InjectView(R.id.fl)
//    FrameLayout fl;
//    @InjectView(R.id.btn_level)
//    TextView btnLevel;
//    @InjectView(R.id.btn_username)
//    TextView btnUsername;
//    @InjectView(R.id.mine_point_pay)
//    TextView minePointPay;
//    @InjectView(R.id.tv_mine_refund)
//    TextView tvMineRefund;
//    @InjectView(R.id.rl_mine_order_detail_pay)
//    RelativeLayout rlMineOrderDetailPay;
//    @InjectView(R.id.mine_point_address)
//    TextView minePointAddress;
//    @InjectView(R.id.mine_delivery)
//    TextView mineDelivery;
//    @InjectView(R.id.rl_mine_order_detail_address)
//    RelativeLayout rlMineOrderDetailAddress;
//    @InjectView(R.id.mine_point_pingjia)
//    TextView minePointPingjia;
//    @InjectView(R.id.tv_mine_pingjia)
//    TextView tvMinePingjia;
//    @InjectView(R.id.rl_mine_order_detail_pingjia)
//    RelativeLayout rlMineOrderDetailPingjia;
//    @InjectView(R.id.mine_point_order)
//    TextView minePointOrder;
//    @InjectView(R.id.tv_mine_order)
//    TextView tvMineOrder;
//    @InjectView(R.id.rl_mine_order_detail_order)
//    RelativeLayout rlMineOrderDetailOrder;
//    @InjectView(R.id.my_category_text)
//    TextView myCategoryText;
//    @InjectView(R.id.my_category_tip_text)
//    TextView myCategoryTipText;
//    @InjectView(R.id.seperate_line)
//    View seperateLine;
//    @InjectView(R.id.seperate_line2)
//    View seperateLine2;
//    @InjectView(R.id.more_category_text)
//    TextView moreCategoryText;
//    @InjectView(R.id.subscribe_main_layout)
//    LinearLayout subscribeMainLayout;
//    @InjectView(R.id.iv_mine_item_about_us)
//    ImageView ivMineItemAboutUs;
//    @InjectView(R.id.rl_mine_about_us)
//    RelativeLayout rlMineAboutUs;
//    @InjectView(R.id.iv_mine_item_help)
//    ImageView ivMineItemHelp;
//    @InjectView(R.id.rl_mine_help)
//    RelativeLayout rlMineHelp;
//    @InjectView(R.id.userGridView)
//    DragGrid userGridView;
//    @InjectView(R.id.otherGridView)
//    OtherGridView otherGridView;
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
    @InjectView(R.id.iv_mine_setting)
    ImageView ivMineSetting;
    @InjectView(R.id.iv_mine_session)
    ImageView ivMineSession;
    @InjectView(R.id.fl_login)
    FrameLayout flLogin;
    @InjectView(R.id.btn_level)
    TextView btnLevel;
    @InjectView(R.id.btn_username)
    TextView btnUsername;
    @InjectView(R.id.ll_login)
    LinearLayout llLogin;
    @InjectView(R.id.mine_point_pay)
    TextView minePointPay;
    @InjectView(R.id.tv_mine_refund)
    TextView tvMineRefund;
    @InjectView(R.id.rl_mine_order_detail_pay)
    RelativeLayout rlMineOrderDetailPay;
    @InjectView(R.id.mine_point_address)
    TextView minePointAddress;
    @InjectView(R.id.mine_delivery)
    TextView mineDelivery;
    @InjectView(R.id.rl_mine_order_detail_address)
    RelativeLayout rlMineOrderDetailAddress;
    @InjectView(R.id.mine_point_pingjia)
    TextView minePointPingjia;
    @InjectView(R.id.tv_mine_pingjia)
    TextView tvMinePingjia;
    @InjectView(R.id.rl_mine_order_detail_pingjia)
    RelativeLayout rlMineOrderDetailPingjia;
    @InjectView(R.id.mine_point_order)
    TextView minePointOrder;
    @InjectView(R.id.tv_mine_order)
    TextView tvMineOrder;
    @InjectView(R.id.rl_mine_order_detail_order)
    RelativeLayout rlMineOrderDetailOrder;
    @InjectView(R.id.my_category_text)
    TextView myCategoryText;
    @InjectView(R.id.my_category_tip_text)
    TextView myCategoryTipText;
    @InjectView(R.id.seperate_line)
    View seperateLine;
    @InjectView(R.id.userGridView)
    DragGrid userGridView;
    @InjectView(R.id.seperate_line2)
    View seperateLine2;
    @InjectView(R.id.more_category_text)
    TextView moreCategoryText;
    @InjectView(R.id.otherGridView)
    OtherGridView otherGridView;
    @InjectView(R.id.subscribe_main_layout)
    LinearLayout subscribeMainLayout;
    @InjectView(R.id.iv_mine_item_about_us)
    ImageView ivMineItemAboutUs;
    @InjectView(R.id.rl_mine_about_us)
    RelativeLayout rlMineAboutUs;
    @InjectView(R.id.iv_mine_item_help)
    ImageView ivMineItemHelp;
    @InjectView(R.id.rl_mine_help)
    RelativeLayout rlMineHelp;

    @Override
    public void onStart() {
        super.onStart();
        if (App.userID != null) {
            btnLevel.setText("金牌会员");
            btnUsername.setText("吃瓜群众");
        }
    }

    @Override
    protected View getSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        ButterKnife.inject(this, view);
        if (App.userID == null) {
            btnLevel.setText("登录");
            btnUsername.setText("注册");
            btnLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToLoginManager.isJumpToLogin(getActivity());
                }
            });
            btnUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpToLoginManager.isJumpToRegister(getActivity());
                }
            });
        }
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(App.getApp().getSQLHelper()).getUserChannel());
        otherChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(App.getApp().getSQLHelper()).getOtherChannel());
        userAdapter = new DragAdapter(getActivity(), userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(getActivity(), otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        System.out.println("dfjkdklsk*----******");
        return view;
    }

    @Override
    protected Object requestData() {
        return new Object();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_mine_setting, R.id.iv_mine_session, R.id.rl_mine_order_detail_pay, R.id.rl_mine_order_detail_address, R.id.rl_mine_order_detail_pingjia, R.id.rl_mine_order_detail_order, R.id.rl_mine_about_us, R.id.rl_mine_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mine_setting://设置
                break;
            case R.id.iv_mine_session://消息中心
                break;
            case R.id.rl_mine_order_detail_pay://代付款
                break;
            case R.id.rl_mine_order_detail_address://待收货
                break;
            case R.id.rl_mine_order_detail_pingjia://待评价
                break;
            case R.id.rl_mine_order_detail_order://我的订单
                //点击单开我的订单页面
                Intent intent = new Intent(getActivity(),OrderDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                break;
            case R.id.rl_mine_about_us: //关于
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.rl_mine_help: //帮助

                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
        }
    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
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

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveChannel() {
        ChannelManage.getManage(App.getApp().getSQLHelper()).deleteAllChannel();
        ChannelManage.getManage(App.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
        ChannelManage.getManage(App.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
    }

}
