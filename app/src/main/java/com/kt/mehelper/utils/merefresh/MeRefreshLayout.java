package com.kt.mehelper.utils.merefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kt.mehelper.R;


public class MeRefreshLayout extends FrameLayout {

    //    默认摩擦系数
    private static final float DEFAULT_FRICTION = 0.5f;
    private View mHeaderView;
    private View mContentView;
    private View mFooterView;
    //    头部高度
    protected int mHeaderHeight;
    //    底部高度
    protected int mFooterHeight;
    //   下拉偏移
    private int mHeadOffY;
    //    上拉偏移
    private int mFootOffY;
    //    内容偏移
    private int mContentOffY;
    private boolean isSetHeaderHeight;
    private boolean isSetFooterHeight;
    //    摩擦系数
    private float mFriction = DEFAULT_FRICTION;
    //  最后一次触摸的位置
    private float lastY;
    private float currentOffSetY;
    //  触摸移动的位置
    private int offsetSum;
    //    触摸移动的位置之和
    private int scrollSum;
    //   刷新完成时，默认平滑滚动单位距离  除CLASSIC外有效
    private static final int DEFAULT_SMOOTH_LENGTH = 50;
    //   刷新完成时，默认平滑滚动单位时间 除CLASSIC外有效
    private static final int DEFAULT_SMOOTH_DURATION = 3;
    //    平滑滚动单位距离
    private int mSmoothLength = DEFAULT_SMOOTH_LENGTH;
    //    平滑滚动单位时间
    private int mSmoothDuration = DEFAULT_SMOOTH_DURATION;

    public MeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public MeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount < 1) return;
        mHeaderView = findViewById(R.id.me_refresh_header);
        mContentView = findViewById(R.id.me_refresh_content);
        mFooterView = findViewById(R.id.me_refresh_footer);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        childLayout();
    }

    /**
     * 设置上拉下拉中间view的位置
     */
    private void childLayout() {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin - mHeaderHeight + mHeadOffY;
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }

        if (mFooterView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = getMeasuredHeight() + paddingTop + lp.topMargin - mFootOffY;
            final int right = left + mFooterView.getMeasuredWidth();
            final int bottom = top + mFooterView.getMeasuredHeight();
            mFooterView.layout(left, top, right, bottom);

        }
        if (mContentView != null) {

            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + mContentOffY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();

            mContentView.layout(left, top, right, bottom);
        }

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v != mHeaderView && v != mFooterView && v != mContentView) {
                MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
                int left = paddingLeft + lp.leftMargin;
                int top = paddingTop + lp.topMargin + mContentOffY;
                int right = left + v.getMeasuredWidth();
                int bottom = top + v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
            }
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();

            if (!isSetHeaderHeight) {
                mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }

        }

        if (mFooterView != null) {
            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();

            if (!isSetFooterHeight) {
                mFooterHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }


        }
        if (mContentView != null) {


            measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }

        int count = getChildCount();

        for (int i = 0; i < count; i++) {

            View v = getChildAt(i);

            if (v != mHeaderView && v != mFooterView && v != mContentView) {

                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);


            }
        }

    }

    /**
     * 滑动距离越大比率越小，越难拖动
     *
     * @return float
     */
    private float getRatio() {
        return 1 - (Math.abs(offsetSum) / (float) getMeasuredHeight()) - 0.3f * mFriction;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastY > 0) {
                    currentOffSetY = (int) (e.getY() - lastY);
                    offsetSum += currentOffSetY;
                }
                lastY = e.getY();
                boolean isCanMove = offsetSum > 0;
                if (isCanMove) {
                    float ratio = getRatio();
                    if (ratio < 0) {
                        ratio = 0;
                    }
                    int scrollNum = -((int) (currentOffSetY * ratio));
                    scrollSum += scrollNum;
                    mHeadOffY = mHeaderHeight;
                    mContentOffY = Math.abs(scrollSum);

                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                tempY = Math.abs(scrollSum);
                if (onRefreshListener != null) {
                    onRefreshListener.onRefresh();
                }
                break;
            default:

                break;
        }

        return true;
    }

    private int tempY;

    private void layoutSmoothMove() {
        if (tempY == 0) {
            return;
        }
        tempY -= mSmoothLength;
        if (tempY <= 0) {
            tempY = 0;
        }
        mContentOffY = Math.abs(tempY);
        mHeadOffY = mHeaderHeight;
        requestLayout();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutSmoothMove();
            }
        }, mSmoothDuration);
    }

    public void refreshComplete() {
        layoutSmoothMove();
        resetParameter();
    }

    /**
     * 重置参数
     */
    private void resetParameter() {
        lastY = 0;
        offsetSum = 0;
        scrollSum = 0;
    }

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
