package com.sugar.myapplication.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sugar.myapplication.App;
import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.SearchActivity;
import com.sugar.myapplication.adapter.CategoriesAdapter;
import com.sugar.myapplication.bean.CategoryResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.global.Constants;
import com.sugar.myapplication.view.LoadingPage;

import org.senydevpkg.net.HttpLoader;
import org.senydevpkg.net.resp.IResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by sugar on 2016/11/22.
 */

public class CategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.lv_category_navigator)
    ListView mLvCategoryNavigator;//左侧导航
    @InjectView(R.id.fl_category_container)
    FrameLayout mFlCategoryContainer;//右侧内容
    @InjectView(R.id.ll_search_intent)
    LinearLayout mLlSearchIntent;

    private int levelOneSize;//一级菜单数量
    private int defaultPos;//默认选中的条目位置0
    private FragmentManager sm;
    private CategoryDetailFragment fragment;

    //分类的集合
    private ArrayList<String> levelOne;
    private ArrayList<String> fakeDataList;
    private List<CategoryResponse.CategoryBean> mBeanList;
    private CategoriesAdapter mAdapter;

    @Override
    protected View getSuccessView() {
        View view = View.inflate(mActivity, R.layout.fragment_category, null);
        ButterKnife.inject(this, view);

        sm = mActivity.getSupportFragmentManager();
        fragment = new CategoryDetailFragment();
        /**
         * 展示出界面
         */
        show();
        return view;
    }

    private void show() {

        //这是假数据
        initFakeDataList();

        //去掉垂直滑动条
        mLvCategoryNavigator.setVerticalScrollBarEnabled(false);
        //item点击监听
        mLvCategoryNavigator.setOnItemClickListener(this);
//        mLvCategoryNavigator.setAdapter(new CategoriesAdapter(mActivity, list));
    }


    @Override
    protected Object requestData() {
        Request<?> request = HttpLoader.getInstance(mActivity).get(Api.URL_CATEGORY, null, CategoryResponse.class, Api.REQUEST_CODE_CATEGORY, new MyListner(), true);
        request.setTag("category");

        return new Object();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.ll_search_intent)
    public void onClick() {
            Intent intent = new Intent(mActivity, SearchActivity.class);
            startActivity(intent);
    }

    private class MyListner implements HttpLoader.HttpListener {


        @Override
        public void onGetResponseSuccess(int requestCode, IResponse response) {

            if (requestCode == Api.REQUEST_CODE_CATEGORY) {
                //访问成功
                CategoryResponse categories = (CategoryResponse) response;
                EventBus.getDefault().post(categories);
                //category的集合
                mBeanList = categories.getCategory();
                levelOne = new ArrayList<String>();
                for (CategoryResponse.CategoryBean bean : mBeanList) {
                    if (!bean.isIsLeafNode() && bean.getParentId() == 0) {
                        //说明是一级节点,
                        levelOne.add(bean.getName());
                    }
                }
            }
            levelOneSize = levelOne.size();
            levelOne.addAll(fakeDataList);

            if (mAdapter==null) {
                mAdapter = new CategoriesAdapter(mActivity, levelOne);
                mLvCategoryNavigator.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            //设置默认显示第一项
            sm.beginTransaction().replace(R.id.fl_category_container, fragment).commit();
            mAdapter.setCurrentSelectItem(0);
            fragment.setContentList(1, mBeanList);

        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            loadingPage.refreshState(LoadingPage.PageState.STATE_ERROR);
            error.printStackTrace();
        }
    }

    /**
     * listiew的条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

        //告知fragment的parentId和所有集合
        int temPos = position >= levelOneSize ? position % levelOneSize : position + 1;
        fragment.setContentList(temPos, mBeanList);
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //将当前点击项设为选中

                mAdapter.setCurrentSelectItem(position);
            }
        }, 80);
        /**
         * listview 居中显示
         */
        int itemheight = view.getMeasuredHeight();
        int listheight = mLvCategoryNavigator.getMeasuredHeight();
        int offset = (listheight - itemheight)>>1;
        mLvCategoryNavigator.smoothScrollToPositionFromTop(position,offset,300);
    }

    private void initFakeDataList() {
        //fakedatalist是假数据
        fakeDataList = new ArrayList<String>();
        for (int i = 0; i < Constants.categories.length; i++) {
            fakeDataList.add(Constants.categories[i]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        HttpLoader.getInstance(mActivity).cancelRequest("category");
    }
}
