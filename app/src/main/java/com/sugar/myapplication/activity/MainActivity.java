package com.sugar.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.readystatesoftware.viewbadger.BadgeView;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.adapter.TabsFragmentAdapter;
import com.sugar.myapplication.bean.VersionInfo;
import com.sugar.myapplication.dao.MyCartDao;
import com.sugar.myapplication.fragment.BaseFragment;
import com.sugar.myapplication.fragment.CartFragment;
import com.sugar.myapplication.fragment.CategoryFragment;
import com.sugar.myapplication.fragment.DiscoveryFragment;
import com.sugar.myapplication.fragment.HomeFragment;
import com.sugar.myapplication.fragment.MineFragment;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.global.Constants;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.LogUtil;
import com.sugar.myapplication.util.ToastUtil;
import com.sugar.myapplication.view.LoadingPage;
import com.sugar.myapplication.view.NoScrollViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity
        implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @InjectView(R.id.cotent_container)
    NoScrollViewPager mCotentContainer;
    @InjectView(R.id.rb_home)
    RadioButton mRbHome;
    @InjectView(R.id.rb_category)
    RadioButton mRbCategory;
    @InjectView(R.id.rb_discovery)
    RadioButton mRbDiscovery;
    @InjectView(R.id.rb_cart)
    RadioButton mRbCart;
    @InjectView(R.id.rb_my)
    RadioButton mRbMy;
    @InjectView(R.id.rg_tabs)
    RadioGroup mRgTabs;
    @InjectView(R.id.activity_main)
    LinearLayout mActivityMain;
    @InjectView(R.id.btn_show_msg)
    Button mBtnShowMsg;
    @InjectView(R.id.pb_download_progress)
    NumberProgressBar mPbDownloadProgress;
    @InjectView(R.id.rl_progress_cover)
    RelativeLayout mRlProgressCover;
    private ArrayList<BaseFragment> mTabs;

    private MyCartDao dao;
    private MyObserver observer;
    private BadgeView badgeView;
    //购物车角标,显示购物车数量
    private BadgeView cartBadget;
    /**
     * 双击返回退出
     */
    private int count = 0;
    private long startTime;
    private long endTime;
    private DownloadTask mDownloadTask;
    private File lastestApkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();

        badgeView = new BadgeView(MainActivity.this, mBtnShowMsg);
        Uri uri = Uri.parse(Api.CONTENT_OBSERVER_URI);
        observer = new MyObserver(App.mHandler);
        getContentResolver().registerContentObserver(uri, true, observer);

        //接受版本更新的消息
        EventBus.getDefault().registerSticky(this);
    }

    class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            queryData();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            queryData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消内容观察者
        getContentResolver().unregisterContentObserver(observer);
        //注销掉eventbus,防止内存泄漏
        EventBus.getDefault().unregister(this);
        App.mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化布局
     */
    private void init() {
        mTabs = new ArrayList<BaseFragment>();
        mTabs.add(new HomeFragment());
        mTabs.add(new CategoryFragment());
        mTabs.add(new DiscoveryFragment());
        mTabs.add(new CartFragment());
        //mTabs.add(new MyFragment());
        mTabs.add(new MineFragment());

        FragmentPagerAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager(), mTabs);
        mCotentContainer.setAdapter(adapter);
        mCotentContainer.setOnPageChangeListener(this);
        mRgTabs.setOnCheckedChangeListener(this);
       // mCotentContainer.setOffscreenPageLimit(4);
        /**
         * 默认显示首页
         */
        mCotentContainer.setCurrentItem(0);
    }

    /**
     * radioGroup的checkedchang监听
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!CommonUtil.isNetworkAvailable(this)) {
            ToastUtil.showToast("当前网络不可用,请检查网络...");
            group.setClickable(false);
            return;
        } else {
            group.setClickable(true);
        }
        switch (checkedId) {
            case R.id.rb_home://加载home页面
                mCotentContainer.setCurrentItem(0, false);
                break;
            case R.id.rb_category://加载分类页面
                mCotentContainer.setCurrentItem(1, false);
                break;
            case R.id.rb_discovery://加载发现页面
                mCotentContainer.setCurrentItem(2, false);
                break;
            case R.id.rb_cart://加载购物车页面
                mCotentContainer.setCurrentItem(3, false);
                break;
            case R.id.rb_my://加载我的账户页面
                //JumpToLoginManager.isJumpToLogin(this);
                mCotentContainer.setCurrentItem(4, false);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String jump = intent.getStringExtra("jump");
            if ("toCart".equals(jump)) {//转跳到购物车fragment
                mCotentContainer.setCurrentItem(3, false);
                RadioGroup mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
                mRgTabs.check(mRgTabs.getId());
                RadioButton childAt = (RadioButton) mRgTabs.getChildAt(3);
                childAt.setChecked(true);
            } else if ("toshopping".equals(jump)) {//转跳到首页fragment
                mCotentContainer.setCurrentItem(0, false);
                RadioGroup mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
                mRgTabs.check(mRgTabs.getId());
                RadioButton childAt = (RadioButton) mRgTabs.getChildAt(0);
                childAt.setChecked(true);

            }else if ("fromDetailToCart".equals(jump)) {//转跳到购物车fragment
                mCotentContainer.setCurrentItem(3, false);
                RadioGroup mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
                mRgTabs.check(mRgTabs.getId());
                RadioButton childAt = (RadioButton) mRgTabs.getChildAt(3);
                childAt.setChecked(true);
            }else if ("toUserCenter".equals(jump)) {//转跳到用户中心fragment
                mCotentContainer.setCurrentItem(4, false);
                RadioGroup mRgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
                mRgTabs.check(mRgTabs.getId());
                RadioButton childAt = (RadioButton) mRgTabs.getChildAt(4);
                childAt.setChecked(true);


            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示购物车购物数量
        queryData();
    }

    public void queryData() {
        CommonUtil.runOnThread(new Runnable() {
            @Override
            public void run() {
                dao = new MyCartDao();
                final int size = dao.queryAllGoodsCount();
                CommonUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (size > 0) {
                            //tvMainCartNum.setText();
                            badgeView.setText(size + "");
                           // badgeView.setBadgeMargin(40, 0);
                            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                            badgeView.show();
                        } else {
                            if (badgeView != null) {
                                //badgeView.setText("");
                                badgeView.hide();
                            }
                        }
                    }
                });
            }
        });
    }
    /**
     * viewpager的pagechanged监听by_guchao
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        BaseFragment fragment = mTabs.get(position);
        LoadingPage loadingPage = fragment.loadingPage;

        if (loadingPage != null) {
            loadingPage.refreshState(LoadingPage.PageState.STATE_SUCCESS);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }


    /**
     * 双击返回退出
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        count++;
        if (count == 1) {
            startTime = System.currentTimeMillis();
            ToastUtil.showToast("再按一次返回键退出");
        } else if (count == 2) {
            endTime = System.currentTimeMillis();
            if (endTime - startTime < 2000) {
                finish();
//                System.exit(0);
            } else {
                startTime = System.currentTimeMillis();
                ToastUtil.showToast("再按一次返回键退出");
                count = 1;
            }
        }
    }

    /**
     * 获取splash传递过来的版本信息
     * @param version
     */
    public void onEvent(VersionInfo version) {
        System.out.println("version: " + version.url + " ,size: " + version.size);
        maxProgress = version.size;
        System.out.println("maxProgress" + maxProgress);
        if (version != null) {
            //弹出升级对话框
            showUpgradeDialog(version.url);
        }
    }
    /**
     * 升级对话框
     *
     * @param url
     */
    private void showUpgradeDialog(final String url) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("升级提醒");
        builder.setCancelable(false);
        builder.setMessage("新版本特性:\n1.修复了几个bug,提高了稳定性\n2.商品推荐更加精准,告别垃圾广告" +
                "\n3.一些隐藏的彩蛋等待着爱探索你哦");
        builder.setPositiveButton("帮我升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDownloadTask = new DownloadTask();
                mDownloadTask.execute(url);
            }
        });
        builder.setNegativeButton("下次提醒", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //显示对话框
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = builder.show();
            }
        },1500);
    }
    /**
     * 升级任务
     */
    class DownloadTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            File dir = new File(Constants.SDCARD_DIR, "RedBoy");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            lastestApkFile = new File(dir, "lastest.apk");

            HttpURLConnection conn = null;
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                int code = conn.getResponseCode();
                InputStream is = null;
                if (code == 200) {//连接成功
                    is = conn.getInputStream();

                    Log.e("------------", "doInBackground: "+lastestApkFile );
                    FileOutputStream fos = new FileOutputStream(lastestApkFile);
                    byte[] buffer = new byte[64];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        //更新进度
                        downloadProgress += len;
                        publishProgress(downloadProgress);
                    }
                    fos.close();
                    return true;
                } else {
                    //下载失败
                    ToastUtil.showToast("network error!");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(this,"升级错误,网络问题");
                return false;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        @Override
        protected void onPreExecute() {
            mRlProgressCover.setVisibility(View.VISIBLE);
            //禁用点击
            mRlProgressCover.setEnabled(true);
            mRlProgressCover.setClickable(true);

            mPbDownloadProgress.setMax(maxProgress);
        }
        @Override
        protected void onPostExecute(Boolean done) {
            mRlProgressCover.setVisibility(ViewPager.INVISIBLE);
//            mRlProgressCover.setClickable(true);
            if (done) {
                //下载完成,是否安装,
                showInstallerDialog();
            } else {
                //下载失败
                ToastUtil.showToast("升级失败");
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            mPbDownloadProgress.setProgress(values[0]);
        }
    }
    /**
     * 显示升级安装界面
     */
    private void showInstallerDialog() {
        // 替换安装apk的隐式意图
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //传一个apk路径
        intent.setDataAndType(Uri.fromFile(lastestApkFile),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
   //下载进度和最大进度
    private int downloadProgress;
    private int maxProgress;

}