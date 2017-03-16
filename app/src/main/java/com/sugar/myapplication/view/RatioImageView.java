package com.sugar.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 根据指定的宽高比（ratio）来动态设置自身的高度
 * @author Administrator
 *
 */
public class RatioImageView extends ImageView{
	private float ratio = 1.75f;//宽高比
	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取自定义属性的值
		String namespace = "http://schemas.android.com/apk/res-auto";
		ratio = attrs.getAttributeFloatValue(namespace, "ratio", 0f);
	}

	public RatioImageView(Context context) {
		super(context);
	}
	/**
	 * 设置宽高比
	 * @param ratio
	 */
	public void setRatio(float ratio){
		this.ratio = ratio;
	}
	
	/**
	 * widthMeasureSpec和heightMeasureSpec是父View帮我们计算好并且传给子View了
	 * 测量规则： MeasureSpec ,封装了size和mode
	 * size: 就是具体的大小值
	 * mode: 测量模式, 对应的是xml布局中的参数
	 * 		MeasureSpec.AT_MOST 对应的是wrap_content;
	 * 		MeasureSpec.EXACTLY 对应的是match_parent,具体的dp值
	 * 		MeasureSpec.UNSPECIFIED 表示未指定的，一般不用，只在adapter的测量用到
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//1.获取当前ImageView的宽度，
		//通过widthMeasureSpec来取出当前imageView的宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		//2.根据宽高比，计算对应的高度
		if(ratio!=0){
			float height = width/ratio;//获取对应的高度
			//3.将高度设置给ImageView
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
