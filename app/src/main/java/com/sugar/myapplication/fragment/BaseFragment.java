package com.sugar.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugar.myapplication.util.CommonUtil;
import com.sugar.myapplication.view.LoadingPage;

/**
 * 基类Fragment, 所有Fragment继承此类
 * 
 * 1. 定义Activity常量,方便子类使用 2. 定义抽象方法initViews,初始化布局,必须实现 3.
 * 定义方法initData,初始化数据,可以不实现
 * 
 * @author slf
 * 
 */
public abstract class BaseFragment extends Fragment {

	protected AppCompatActivity mActivity;
	public LoadingPage loadingPage;//注意：修饰符不能是private
	protected LoadingPage.PageState requestState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mActivity = (AppCompatActivity) getActivity();
		if(loadingPage==null){
			loadingPage = new LoadingPage(getActivity()) {
				@Override
				public View createSuccessView() {
					return getSuccessView();
				}
				/**
				 * 这是子线程
				 * @return
                 */
				@Override
				public Object getDataFromServer() {
					requestState = requestData()==null?PageState.STATE_ERROR:PageState.STATE_SUCCESS;
					return requestData();
				}
			};
		}else {
			CommonUtil.removeSelfFromParent(loadingPage);
		}
		return loadingPage;
	}
	/**
	 * 获取每个子类的successView
	 * @return
	 */
	protected abstract View getSuccessView();
	/**
	 * 获取每个子类的数据
	 * @return
	 */
	protected abstract Object requestData();

	public LoadingPage.PageState getRequestState() {
		return requestState;
	}
}
