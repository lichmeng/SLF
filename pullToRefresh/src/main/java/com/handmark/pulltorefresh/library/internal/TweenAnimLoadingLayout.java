/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.Gravity;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

public class TweenAnimLoadingLayout extends LoadingLayout {

    static final int ROTATION_ANIMATION_DURATION = 1200;

    //
    private AnimationDrawable animationDrawable;
    int[] resId = new int[]{
            R.drawable.a20,
            R.drawable.a21,
            R.drawable.a22,
            R.drawable.a23,
            R.drawable.a24,
    };

    public TweenAnimLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

        mHeaderImage.setImageResource(R.drawable.jd_loading);
        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
    }

    public void onLoadingDrawableSet(Drawable imageDrawable) {
    }

    protected void onPullImpl(float scaleOfLayout) {
        //图片下标0-10   //scaleOfLayout在下拉达到刷新之前的值：0<value<2，会随着下拉距离增加而增大
        int idx = (int) Math.floor(scaleOfLayout * 10);
        //设置图片显示大小随着下拉距离增加而变大
        if (idx < 4) {      //通过资源id--->drawable
            //通过资源id--->drawable
            Drawable drawable = getResources().getDrawable(resId[idx]);
            //设置缩放尺寸：宽、高
            drawable.setLevel(200);
            //图像资源级别与drawable.setLevel()设置的相同才会显示图像.(5- idx) / 5.0f, (5- idx) / 5.0f
            ScaleDrawable sd = new ScaleDrawable(drawable, Gravity.CENTER,0.5f,0.5f);
            mHeaderImage.setImageLevel(200);
            mHeaderImage.setImageDrawable(sd);
        } else {
            //下拉距离达到一定时显示最后一张图片不再改变   }
            mHeaderImage.setImageResource(resId[4]);
        }
    }

        @Override
        protected void refreshingImpl () {
            animationDrawable.start();
        }

        @Override
        protected void resetImpl () {
            mHeaderImage.setVisibility(View.VISIBLE);
            mHeaderImage.clearAnimation();
        }

    private void resetImageRotation() {
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.jd_loading;
    }

}
