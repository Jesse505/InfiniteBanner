package com.jesse.infinitebanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jesse.infinitebanner.transformer.BGAPageTransformer;
import com.jesse.infinitebanner.transformer.TransitionEffect;

import java.util.List;

/**
 * 文件描述
 * 创建人：zhaoyf
 * 创建时间：2017/12/14
 */

public class InfiniteBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private InfiniteViewPager mViewPager;
    private List<String> mTips;
    private LinearLayout mPointRealContainerLl;
    private TextView mTipTv;
    private boolean mAutoPlayAble = true;
    private int mAutoPlayInterval = 3000;        //自动轮播的时间
    private int mPageChangeDuration = 800;       //页码切换过程的时间
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM; //指示器的位置
    private int mPointLeftRightMargin;           //指示点左右外边距
    private int mPointTopBottomMargin;           //指示点上下外边距
    private int mPointContainerLeftRightPadding; //指示点容器左右内边距
    private int mTipTextSize;                    //提示文案的文字大小
    private int mTipTextColor = Color.WHITE;
    private int mPointDrawableResId = R.drawable.bga_banner_selector_point_solid; //指示点背景
    private Drawable mPointContainerBackgroundDrawable;   //指示点容器背景
    private TransitionEffect mTransitionEffect;  //页面切换动画
    private int mOverScrollMode = OVER_SCROLL_NEVER;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private boolean mIsNumberIndicator = false;  //是否是数字指示器
    private TextView mNumberIndicatorTv;
    private int mNumberIndicatorTextColor = Color.WHITE;
    private int mNumberIndicatorTextSize;    //数字指示器文字大小
    private Drawable mNumberIndicatorBackground;
    private boolean mIsNeedShowIndicatorOnOnlyOnePage;   //设置一页时是否要显示指示器
    private boolean mAllowUserScrollable = true;
    private int mContentBottomMargin;      //

    InfinitePagerAdapter infinitePagerAdapter;


    public InfiniteBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfiniteBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        initView(context);
    }

    private void initDefaultAttrs(Context context) {

        mPointLeftRightMargin = InfiniteBannerUtil.dp2px(context, 3);
        mPointTopBottomMargin = InfiniteBannerUtil.dp2px(context, 6);
        mPointContainerLeftRightPadding = InfiniteBannerUtil.dp2px(context, 10);
        mTipTextSize = InfiniteBannerUtil.sp2px(context, 10);
        mPointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#44aaaaaa"));
        mTransitionEffect = TransitionEffect.Default;
        mNumberIndicatorTextSize = InfiniteBannerUtil.sp2px(context, 10);
        mContentBottomMargin = 0;
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGABanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGABanner_banner_pointDrawable) {
            mPointDrawableResId = typedArray.getResourceId(attr, R.drawable.bga_banner_selector_point_solid);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerBackground) {
            mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.BGABanner_banner_pointLeftRightMargin) {
            mPointLeftRightMargin = typedArray.getDimensionPixelSize(attr, mPointLeftRightMargin);
        } else if (attr == R.styleable.BGABanner_banner_pointContainerLeftRightPadding) {
            mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mPointContainerLeftRightPadding);
        } else if (attr == R.styleable.BGABanner_banner_pointTopBottomMargin) {
            mPointTopBottomMargin = typedArray.getDimensionPixelSize(attr, mPointTopBottomMargin);
        } else if (attr == R.styleable.BGABanner_banner_indicatorGravity) {
            mPointGravity = typedArray.getInt(attr, mPointGravity);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayAble) {
            mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);
        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayInterval) {
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BGABanner_banner_pageChangeDuration) {
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.BGABanner_banner_transitionEffect) {
            int ordinal = typedArray.getInt(attr, TransitionEffect.Accordion.ordinal());
            mTransitionEffect = TransitionEffect.values()[ordinal];
        } else if (attr == R.styleable.BGABanner_banner_tipTextColor) {
            mTipTextColor = typedArray.getColor(attr, mTipTextColor);
        } else if (attr == R.styleable.BGABanner_banner_tipTextSize) {
            mTipTextSize = typedArray.getDimensionPixelSize(attr, mTipTextSize);
        } else if (attr == R.styleable.BGABanner_banner_isNumberIndicator) {
            mIsNumberIndicator = typedArray.getBoolean(attr, mIsNumberIndicator);
        } else if (attr == R.styleable.BGABanner_banner_numberIndicatorTextColor) {
            mNumberIndicatorTextColor = typedArray.getColor(attr, mNumberIndicatorTextColor);
        } else if (attr == R.styleable.BGABanner_banner_numberIndicatorTextSize) {
            mNumberIndicatorTextSize = typedArray.getDimensionPixelSize(attr, mNumberIndicatorTextSize);
        } else if (attr == R.styleable.BGABanner_banner_numberIndicatorBackground) {
            mNumberIndicatorBackground = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.BGABanner_banner_isNeedShowIndicatorOnOnlyOnePage) {
            mIsNeedShowIndicatorOnOnlyOnePage = typedArray.getBoolean(attr, mIsNeedShowIndicatorOnOnlyOnePage);
        } else if (attr == R.styleable.BGABanner_banner_contentBottomMargin) {
            mContentBottomMargin = typedArray.getDimensionPixelSize(attr, mContentBottomMargin);
        }
    }

    private void initView(Context context) {
        RelativeLayout pointContainerRl = new RelativeLayout(context);
        if (Build.VERSION.SDK_INT >= 16) {
            pointContainerRl.setBackground(mPointContainerBackgroundDrawable);
        } else {
            pointContainerRl.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        pointContainerRl.setPadding(mPointContainerLeftRightPadding, mPointTopBottomMargin, mPointContainerLeftRightPadding, mPointTopBottomMargin);
        LayoutParams pointContainerLp = new LayoutParams(RMP, RWC);
        // 处理圆点在顶部还是底部
        if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        addView(pointContainerRl, pointContainerLp);


        LayoutParams indicatorLp = new LayoutParams(RWC, RWC);
        indicatorLp.addRule(CENTER_VERTICAL);
        if (mIsNumberIndicator) {
            mNumberIndicatorTv = new TextView(context);
            mNumberIndicatorTv.setId(R.id.banner_indicatorId);
            mNumberIndicatorTv.setGravity(Gravity.CENTER_VERTICAL);
            mNumberIndicatorTv.setSingleLine(true);
            mNumberIndicatorTv.setEllipsize(TextUtils.TruncateAt.END);
            mNumberIndicatorTv.setTextColor(mNumberIndicatorTextColor);
            mNumberIndicatorTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumberIndicatorTextSize);
            mNumberIndicatorTv.setVisibility(View.INVISIBLE);
            if (mNumberIndicatorBackground != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    mNumberIndicatorTv.setBackground(mNumberIndicatorBackground);
                } else {
                    mNumberIndicatorTv.setBackgroundDrawable(mNumberIndicatorBackground);
                }
            }
            pointContainerRl.addView(mNumberIndicatorTv, indicatorLp);
        } else {
            mPointRealContainerLl = new LinearLayout(context);
            mPointRealContainerLl.setId(R.id.banner_indicatorId);
            mPointRealContainerLl.setOrientation(LinearLayout.HORIZONTAL);
            mPointRealContainerLl.setGravity(Gravity.CENTER_VERTICAL);
            pointContainerRl.addView(mPointRealContainerLl, indicatorLp);
        }

        LayoutParams tipLp = new LayoutParams(RMP, RWC);
        tipLp.addRule(CENTER_VERTICAL);
        mTipTv = new TextView(context);
        mTipTv.setGravity(Gravity.CENTER_VERTICAL);
        mTipTv.setSingleLine(true);
        mTipTv.setEllipsize(TextUtils.TruncateAt.END);
        mTipTv.setTextColor(mTipTextColor);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipTextSize);
        pointContainerRl.addView(mTipTv, tipLp);

        int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点在左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tipLp.addRule(RelativeLayout.RIGHT_OF, R.id.banner_indicatorId);
            mTipTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicatorId);
        } else {
            indicatorLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_indicatorId);
        }
    }

    private int getItemCount() {
        if (infinitePagerAdapter != null) {
            return infinitePagerAdapter.getRealItemCount();
        } else {
            return 0;
        }
    }

    private void initIndicator() {
        if (mPointRealContainerLl != null) {
            mPointRealContainerLl.removeAllViews();

            if (mIsNeedShowIndicatorOnOnlyOnePage || (!mIsNeedShowIndicatorOnOnlyOnePage && getItemCount() > 1)) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
                lp.setMargins(mPointLeftRightMargin, mPointTopBottomMargin, mPointLeftRightMargin, mPointTopBottomMargin);
                ImageView imageView;
                for (int i = 0; i < getItemCount(); i++) {
                    imageView = new ImageView(getContext());
                    imageView.setLayoutParams(lp);
                    imageView.setImageResource(mPointDrawableResId);
                    mPointRealContainerLl.addView(imageView);
                }
            }
        }
        if (mNumberIndicatorTv != null) {
            if (mIsNeedShowIndicatorOnOnlyOnePage || (!mIsNeedShowIndicatorOnOnlyOnePage && getItemCount() > 1)) {
                mNumberIndicatorTv.setVisibility(View.VISIBLE);
            } else {
                mNumberIndicatorTv.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initViewPager() {
        if (mViewPager != null && this.equals(mViewPager.getParent())) {
            mViewPager.removeOnPageChangeListener(this);
            this.removeView(mViewPager);
            mViewPager = null;
        }
        mViewPager = new InfiniteViewPager(getContext());
//        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(infinitePagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOverScrollMode(mOverScrollMode);
        mViewPager.setAllowUserScrollable(mAllowUserScrollable);
        mViewPager.setPageTransformer(true, BGAPageTransformer.getPageTransformer(mTransitionEffect));
        setPageChangeDuration(mPageChangeDuration);

        LayoutParams layoutParams = new LayoutParams(RMP, RMP);
        layoutParams.setMargins(0, 0, 0, mContentBottomMargin);
        addView(mViewPager, 0, layoutParams);
        if (mAutoPlayAble) {
            int zeroItem = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getItemCount();
            mViewPager.setCurrentItem(zeroItem);
            startAutoPlay();
            onPageSelected(0);
        } else {
            switchToPoint(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    private void startAutoPlay() {
        stopAutoPlay();
        if (mViewPager != null) {
            mViewPager.startAutoScroll(mAutoPlayInterval);
        }
    }

    private void stopAutoPlay() {
        if (mViewPager != null) {
            mViewPager.stopAutoScroll();
        }
    }

    private void switchToPoint(int newCurrentPoint) {
        if (mTipTv != null) {
            if (mTips == null || mTips.size() < 1 || newCurrentPoint >= mTips.size()) {
                mTipTv.setVisibility(View.GONE);
            } else {
                mTipTv.setVisibility(View.VISIBLE);
                mTipTv.setText(mTips.get(newCurrentPoint));
            }
        }

        if (mPointRealContainerLl != null) {
            if (getItemCount() > 0 && newCurrentPoint < getItemCount() && ((mIsNeedShowIndicatorOnOnlyOnePage || (!mIsNeedShowIndicatorOnOnlyOnePage && getItemCount() > 1)))) {
                mPointRealContainerLl.setVisibility(View.VISIBLE);
                for (int i = 0; i < mPointRealContainerLl.getChildCount(); i++) {
                    mPointRealContainerLl.getChildAt(i).setEnabled(i == newCurrentPoint);
                    // 处理指示器选中和未选中状态图片尺寸不相等
                    mPointRealContainerLl.getChildAt(i).requestLayout();
                }
            } else {
                mPointRealContainerLl.setVisibility(View.GONE);
            }
        }

        if (mNumberIndicatorTv != null) {
            if (getItemCount() > 0 && newCurrentPoint < getItemCount() && ((mIsNeedShowIndicatorOnOnlyOnePage || (!mIsNeedShowIndicatorOnOnlyOnePage && getItemCount() > 1)))) {
                mNumberIndicatorTv.setVisibility(View.VISIBLE);
                mNumberIndicatorTv.setText((newCurrentPoint + 1) + "/" + getItemCount());
            } else {
                mNumberIndicatorTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mTipTv != null) {
            if (InfiniteBannerUtil.isCollectionNotEmpty(mTips)) {
                mTipTv.setVisibility(View.VISIBLE);

                int leftPosition = position % mTips.size();
                int rightPosition = (position + 1) % mTips.size();
                if (rightPosition < mTips.size() && leftPosition < mTips.size()) {
                    if (positionOffset > 0.5) {
                        mTipTv.setText(mTips.get(rightPosition));
                        ViewCompat.setAlpha(mTipTv, positionOffset);
                    } else {
                        ViewCompat.setAlpha(mTipTv, 1 - positionOffset);
                        mTipTv.setText(mTips.get(leftPosition));
                    }
                }
            } else {
                mTipTv.setVisibility(View.GONE);
            }
        }

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position % getItemCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = position % getItemCount();
        switchToPoint(position);

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    public void setPageChangeDuration(int duration) {
        if (duration >= 0 && duration <= 2000) {
            mPageChangeDuration = duration;
            if (mViewPager != null) {
                mViewPager.setPageChangeDuration(duration);
            }
        }
    }

    /**
     * 设置是否开启自动轮播
     *
     * @param autoPlayAble
     */
    public void setAutoPlayAble(boolean autoPlayAble) {
        mAutoPlayAble = autoPlayAble;
        if (mViewPager != null) {
            if (mAutoPlayAble) {
                mViewPager.startAutoScroll();
            } else {
                mViewPager.stopAutoScroll();
            }
        }
    }

    /**
     * 设置自动轮播的时间间隔
     *
     * @param autoPlayInterval
     */
    public void setAutoPlayInterval(int autoPlayInterval) {
        mAutoPlayInterval = autoPlayInterval;
        mViewPager.setAutoScrollTime(mAutoPlayInterval);
    }

    /**
     * 设置是否允许用户手指滑动
     *
     * @param allowUserScrollable true表示允许跟随用户触摸滑动，false反之
     */
    public void setAllowUserScrollable(boolean allowUserScrollable) {
        mAllowUserScrollable = allowUserScrollable;
        if (mViewPager != null) {
            mViewPager.setAllowUserScrollable(mAllowUserScrollable);
        }
    }

    /**
     * 添加ViewPager滚动监听器
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     * 获取ViewPager
     * @return
     */
    public InfiniteViewPager getViewPager() {
        return mViewPager;
    }

    public void setOverScrollMode(int overScrollMode) {
        mOverScrollMode = overScrollMode;
        if (mViewPager != null) {
            mViewPager.setOverScrollMode(mOverScrollMode);
        }
    }

    /**
     * 设置adapter
     * @param infinitePagerAdapter
     */
    public void setAdapter(InfinitePagerAdapter infinitePagerAdapter) {
        this.infinitePagerAdapter = infinitePagerAdapter;
        initIndicator();
        initViewPager();
    }

    /**
     * 设置当只有一页数据时是否显示指示器
     *
     * @param isNeedShowIndicatorOnOnlyOnePage
     */
    public void setIsNeedShowIndicatorOnOnlyOnePage(boolean isNeedShowIndicatorOnOnlyOnePage) {
        mIsNeedShowIndicatorOnOnlyOnePage = isNeedShowIndicatorOnOnlyOnePage;
    }

    /**
     * 设置页面切换换动画（这个方法必须在setAdapter()之后）
     *
     * @param effect
     */
    public void setTransitionEffect(TransitionEffect effect) {
        mTransitionEffect = effect;
        if (mViewPager != null) {
            mViewPager.setPageTransformer(true, BGAPageTransformer.getPageTransformer(mTransitionEffect));
        }
    }

    /**
     * 设置自定义页面切换动画（这个方法必须在setAdapter()之后）
     *
     * @param transformer
     */
    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        if (transformer != null && mViewPager != null) {
            mViewPager.setPageTransformer(true, transformer);
        }
    }
}
