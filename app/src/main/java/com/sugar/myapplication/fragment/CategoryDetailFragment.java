package com.sugar.myapplication.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.activity.ProductListActivity;
import com.sugar.myapplication.bean.CategoryResponse;
import com.sugar.myapplication.global.Api;
import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.util.ToastUtil;

import org.senydevpkg.net.HttpLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.sugar.myapplication.R.id.ll_content1;

/**
 * 分类标签
 * Created by sugar on 2016/11/23.
 */

public class CategoryDetailFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private int parentId;
    private List<CategoryResponse.CategoryBean> list;
    private TextView tv;
    private ImageView headerImage;
    private ListView lvDetail;
    private ArrayList<CategoryResponse.CategoryBean> subList;
    private List<Object> ttList;
    private String keyword;

    @Override
    protected View getSuccessView() {
        View view = View.inflate(mActivity, R.layout.item_category_detail, null);
        lvDetail = (ListView) view.findViewById(R.id.lv_category_detail);
        lvDetail.setVerticalScrollBarEnabled(false);
        lvDetail.setOnItemClickListener(this);
        return view;
    }

    @Override
    protected Object requestData() {
        if (lvDetail != null) {
            CommonUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    lvDetail.setAdapter(new MyCategoryAdapter());
                }
            });
        }


        return list;
    }

    /**
     * 条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        CategoryResponse.CategoryBean clickedItemBean = subList.get(position);
//        //开启一个activity,传递过去,点击的二级节点id作为父id,和全部beanList
//        Intent intent = new Intent(mActivity,ProductListActivity.class);
//        intent.putExtra("parentId",clickedItemBean.getId());
//        intent.putParcelableArrayListExtra("productBean", (ArrayList<? extends Parcelable>) list);
//        startActivity(intent);
        ToastUtil.showToast("pos: " + position);
        System.out.println("pos: " + position);
    }

    class MyCategoryAdapter extends BaseAdapter {
        private final int TYPY_TITLE = 0;
        private final int TYPY_CONTENT = 1;
        @InjectView(R.id.iv_content1)
        ImageView mIvContent1;
        @InjectView(R.id.tv_content1)
        TextView mTvContent1;
        @InjectView(ll_content1)
        LinearLayout mLlContent1;
        @InjectView(R.id.iv_content2)
        ImageView mIvContent2;
        @InjectView(R.id.tv_content2)
        TextView mTvContent2;
        @InjectView(R.id.ll_content2)
        LinearLayout mLlContent2;
        @InjectView(R.id.iv_content3)
        ImageView mIvContent3;
        @InjectView(R.id.tv_content3)
        TextView mTvContent3;
        @InjectView(R.id.ll_content3)
        LinearLayout mLlContent3;

        @Override
        public int getCount() {
            return ttList.size();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return ttList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (ttList.get(position) instanceof ThirdLine) {
                return TYPY_CONTENT;
            } else {
                return TYPY_TITLE;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            View view = null;
            if (type == TYPY_TITLE) {//设置二级节点标题
                view = View.inflate(mActivity, R.layout.item_category_holder, null);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_category_subtitle);
                tvTitle.setText(((CategoryResponse.CategoryBean) ttList.get(position)).getName());
            } else {
                view = View.inflate(mActivity, R.layout.item_category_child, null);
                ButterKnife.inject(this, view);

                ThirdLine line = (ThirdLine) ttList.get(position);
                final ArrayList<CategoryResponse.CategoryBean> contents = line.list;
                if (contents != null && contents.size() > 0) {
                    HttpLoader.getInstance(mActivity).display(mIvContent1, Api.URL_SERVER + contents.get(0).getPic());
                    mTvContent1.setText(contents.get(0).getName());
                    if (contents.size() > 1) {
                        HttpLoader.getInstance(mActivity).display(mIvContent1, Api.URL_SERVER + contents.get(1).getPic());
                        mTvContent2.setText(contents.get(1).getName());

                    } else {
                        mLlContent2.setVisibility(View.INVISIBLE);
                    }
                    if (contents.size() > 2) {
                        HttpLoader.getInstance(mActivity).display(mIvContent1,
                                Api.URL_SERVER + contents.get(2).getPic(),
                                R.drawable.category_mama01,
                                R.drawable.category_mama01, 80, 80, ImageView.ScaleType.FIT_XY);
                        mTvContent3.setText(contents.get(2).getName());
                    } else {
                        mLlContent3.setVisibility(View.INVISIBLE);
                    }
                }
                //点击事件
                final Intent intent = new Intent(mActivity, ProductListActivity.class);
                mLlContent1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contents.size() > 0) {
                            keyword = contents.get(0).getName();
                            intent.putExtra("keyword",keyword);
                            startActivity(intent);
                        }
                    }
                });
                mLlContent2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contents.size() > 1) {
                            keyword = contents.get(1).getName();
                            intent.putExtra("keyword",keyword);
                            startActivity(intent);
                        }
                    }
                });
                mLlContent3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contents.size() > 2) {
                            keyword = contents.get(2).getName();
                            intent.putExtra("keyword",keyword);
                            startActivity(intent);
                        }
                    }
                });
            }
            return view;
        }
    }

    //初始化3个数组
    private void initThreeLevelArray(int parentId, List<CategoryResponse.CategoryBean> list) {
        subList = new ArrayList<CategoryResponse.CategoryBean>();
        //初始化二级节点
        for (CategoryResponse.CategoryBean temp : list) {
            if (temp.getParentId() == parentId) {
                //获取二级节点的数据
                subList.add(temp);
            }
        }
        //ttList two-threeList,二级节点和3级节点的混合体
        ttList = new ArrayList<>();
        //初始化3级节点
        for (CategoryResponse.CategoryBean bean : subList) {
            ttList.add(bean);

            int pid = bean.getId();
            ThirdLine line = new ThirdLine();
            for (CategoryResponse.CategoryBean temp : list) {
                if (temp.getParentId() == pid && temp.isIsLeafNode()) {
                    line.add(temp);
                    if (line.list.size() == 3) {
                        ttList.add(line);
                        line = new ThirdLine();
                    }
                }
            }
            if (!ttList.contains(line)) {
                ttList.add(line);
            }
        }
    }

    /**
     * 3个为一行
     */
    class ThirdLine {
        ArrayList<CategoryResponse.CategoryBean> list;

        public ThirdLine() {
            list = new ArrayList<CategoryResponse.CategoryBean>(3);
        }

        public void add(CategoryResponse.CategoryBean temp) {
            list.add(temp);
        }
    }

    /**
     * 得到bean集合和pid
     *
     * @param parentId
     * @param list
     */
    public void setContentList(int parentId, List<CategoryResponse.CategoryBean> list) {
        this.parentId = parentId;
        this.list = list;
        //初始化3级节点的集合
        initThreeLevelArray(parentId, list);
        requestData();
    }
}
