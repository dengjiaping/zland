package com.zhisland.android.blog.common.view.pullzoom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * Author: ZhuWenWu Version V1.0 Date: 2014/11/10 14:25. Description:
 * Modification History: Date Author Version Description
 * ------------------------
 * ----------------------------------------------------------- 2014/11/10
 * ZhuWenWu 1.0 1.0 Why & What is modified:
 */
public class PullToZoomScrollViewEx extends PullToZoomBase<ScrollView> {
	private static final String TAG = PullToZoomScrollViewEx.class
			.getSimpleName();
	private boolean isCustomHeaderHeight = false;
	private FrameLayout mHeaderContainer;
	private LinearLayout mRootContainer;
	private View mContentView;
	private int mHeaderHeight;
	private ScalingRunnable mScalingRunnable;

	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float paramAnonymousFloat) {
			float f = paramAnonymousFloat - 1.0F;
			return 1.0F + f * (f * (f * (f * f)));
		}
	};

	public PullToZoomScrollViewEx(Context context) {
		this(context, null);
	}

	public PullToZoomScrollViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScalingRunnable = new ScalingRunnable();
		((InternalScrollView) mRootView)
				.addScrollChangedListener(new OnScrollChangedListener() {
					@Override
					public void onScrollChanged(int left, int top, int oldLeft,
							int oldTop) {
						if (isPullToZoomEnabled() && isParallax()) {
							Log.d(TAG, "onScrollChanged --> getScrollY() = "
									+ mRootView.getScrollY());
							float f = mHeaderHeight
									- mHeaderContainer.getBottom()
									+ mRootView.getScrollY();
							Log.d(TAG, "onScrollChanged --> f = " + f);
							if ((f > 0.0F) && (f < mHeaderHeight)) {
								int i = (int) (0.65D * f);
								mHeaderContainer.scrollTo(0, -i);
							} else if (mHeaderContainer.getScrollY() != 0) {
								mHeaderContainer.scrollTo(0, 0);
							}
						}
					}
				});
	}

	@Override
	protected void pullHeaderToZoom(int newScrollValue) {
		Log.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
		Log.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
		if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
			mScalingRunnable.abortAnimation();
		}

		ViewGroup.LayoutParams localLayoutParams = mHeaderContainer
				.getLayoutParams();
		localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
		mHeaderContainer.setLayoutParams(localLayoutParams);

		if (isCustomHeaderHeight) {
			ViewGroup.LayoutParams zoomLayoutParams = mZoomView
					.getLayoutParams();
			zoomLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
			mZoomView.setLayoutParams(zoomLayoutParams);
		}
	}

	/**
	 * 是否显示headerView
	 *
	 * @param isHideHeader
	 *            true: show false: hide
	 */
	@Override
	public void setHideHeader(boolean isHideHeader) {
		if (isHideHeader != isHideHeader() && mHeaderContainer != null) {
			super.setHideHeader(isHideHeader);
			if (isHideHeader) {
				mHeaderContainer.setVisibility(GONE);
			} else {
				mHeaderContainer.setVisibility(VISIBLE);
			}
		}
	}

	@Override
	public void setHeaderView(View headerView) {
		if (headerView != null) {
			if (headerView.getParent() != null) {
				((ViewGroup) (headerView.getParent())).removeView(headerView);
			}
			mHeaderView = headerView;
			updateHeaderView();
		}
	}

	@Override
	public void setZoomView(View zoomView) {
		int height = (int) (DensityUtil.getWidth() * 0.75f);
		if (zoomView.getTag() instanceof Integer) {
			height = (Integer) zoomView.getTag();
		}
		if (zoomView != null) {
			if (zoomView.getParent() != null) {
				((ViewGroup) (zoomView.getParent())).removeView(zoomView);
			}
			LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(
					DensityUtil.getWidth(), height);
			setHeaderLayoutParams(localObject);
			mZoomView = zoomView;
			updateHeaderView();
		}
	}

	private void updateHeaderView() {
		if (mHeaderContainer != null) {
			mHeaderContainer.removeAllViews();
			if (mZoomView != null) {
				mHeaderContainer.addView(mZoomView);
			}

			if (mHeaderView != null) {
				mHeaderContainer.addView(mHeaderView);
			}
		}
	}

	public void setScrollContentView(View contentView) {
		if (contentView != null) {
			if (contentView.getParent() != null) {
				((ViewGroup) (contentView.getParent())).removeView(contentView);
			}
			if (mContentView != null) {
				mRootContainer.removeView(mContentView);
			}
			mContentView = contentView;
			mRootContainer.addView(mContentView);
			mContentView.layout(mContentView.getLeft(), -200, 0, 0);
			mContentView.postInvalidate();
		}
	}

	@Override
	protected ScrollView createRootView(Context context, AttributeSet attrs) {
		ScrollView scrollView = new InternalScrollView(context, attrs);
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected void smoothScrollToTop() {
		Log.d(TAG, "smoothScrollToTop --> ");
		mScalingRunnable.startAnimation(200L);
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRootView.getScrollY() == 0;
	}

	@Override
	public void handleStyledAttributes(TypedArray a) {
		mRootContainer = new LinearLayout(getContext());
		mRootContainer.setOrientation(LinearLayout.VERTICAL);
		mHeaderContainer = new FrameLayout(getContext());

		if (mZoomView != null) {
			mHeaderContainer.addView(mZoomView);
		}
		if (mHeaderView != null) {
			mHeaderContainer.addView(mHeaderView);
		}
		int contentViewResId = a.getResourceId(
				R.styleable.PullToZoomView_contentView, 0);
		if (contentViewResId > 0) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
			mContentView = mLayoutInflater.inflate(contentViewResId, null,
					false);
		}

		mRootContainer.addView(mHeaderContainer);
		if (mContentView != null) {
			mRootContainer.addView(mContentView);
		}

		mRootContainer.setClipChildren(false);
		mHeaderContainer.setClipChildren(false);

		mRootView.addView(mRootContainer);
	}

	/**
	 * 设置HeaderView高度
	 *
	 * @param width
	 * @param height
	 */
	public void setHeaderViewSize(int width, int height) {
		if (mHeaderContainer != null) {
			Object localObject = mHeaderContainer.getLayoutParams();
			if (localObject == null) {
				localObject = new ViewGroup.LayoutParams(width, height);
			}
			((ViewGroup.LayoutParams) localObject).width = width;
			((ViewGroup.LayoutParams) localObject).height = height;
			mHeaderContainer
					.setLayoutParams((ViewGroup.LayoutParams) localObject);
			mHeaderHeight = height;
			isCustomHeaderHeight = true;
		}
	}

	/**
	 * 设置HeaderView LayoutParams
	 *
	 * @param layoutParams
	 *            LayoutParams
	 */
	public void setHeaderLayoutParams(LinearLayout.LayoutParams layoutParams) {
		if (mHeaderContainer != null) {
			mHeaderContainer.setLayoutParams(layoutParams);
			mHeaderHeight = layoutParams.height;
			isCustomHeaderHeight = true;
		}
	}

	private OnSizeChangedListener mChangedListener;
	// private OnScrollChangedListener onScrollChangedListener;
	private boolean mShowKeyboard = false;

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
		Log.d(TAG, "onLayout --> ");
		if (mHeaderHeight == 0 && mZoomView != null) {
			mHeaderHeight = mHeaderContainer.getHeight();
		}
	}

	class ScalingRunnable implements Runnable {
		protected long mDuration;
		protected boolean mIsFinished = true;
		protected float mScale;
		protected long mStartTime;

		ScalingRunnable() {
		}

		public void abortAnimation() {
			mIsFinished = true;
		}

		public boolean isFinished() {
			return mIsFinished;
		}

		public void run() {
			if (mZoomView != null) {
				float f2;
				ViewGroup.LayoutParams localLayoutParams;
				if ((!mIsFinished) && (mScale > 1.0D)) {
					float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime)
							/ (float) mDuration;
					f2 = mScale
							- (mScale - 1.0F)
							* PullToZoomScrollViewEx.sInterpolator
									.getInterpolation(f1);
					localLayoutParams = mHeaderContainer.getLayoutParams();
					Log.d(TAG, "ScalingRunnable --> f2 = " + f2);
					if (f2 > 1.0F) {
						localLayoutParams.height = ((int) (f2 * mHeaderHeight));
						mHeaderContainer.setLayoutParams(localLayoutParams);
						if (isCustomHeaderHeight) {
							ViewGroup.LayoutParams zoomLayoutParams;
							zoomLayoutParams = mZoomView.getLayoutParams();
							zoomLayoutParams.height = ((int) (f2 * mHeaderHeight));
							mZoomView.setLayoutParams(zoomLayoutParams);
						}
						post(this);
						return;
					}
					mIsFinished = true;
				}
			}
		}

		public void startAnimation(long paramLong) {
			if (mZoomView != null) {
				mStartTime = SystemClock.currentThreadTimeMillis();
				mDuration = paramLong;
				mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
				mIsFinished = false;
				post(this);
			}
		}
	}

	public class InternalScrollView extends ScrollView {
		// 滑动事件监听器
		private List<OnScrollChangedListener> listeners = new ArrayList<OnScrollChangedListener>();

		public InternalScrollView(Context context) {
			this(context, null);
		}

		public InternalScrollView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			if (null != mChangedListener && 0 != oldw && 0 != oldh) {
				if (oldh - h > 300) {
					mShowKeyboard = true;
					mChangedListener.onChanged(mShowKeyboard);
				} else if (h - oldh > 300) {
					mShowKeyboard = false;
					mChangedListener.onChanged(mShowKeyboard);
				}
			}
		}

		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			if (listeners.size() > 0) {
				for (OnScrollChangedListener listener : listeners)
					listener.onScrollChanged(l, t, oldl, oldt);
			}
			super.onScrollChanged(l, t, oldl, oldt);
		}

		public void setOnSizeChangedListener(OnSizeChangedListener listener) {
			mChangedListener = listener;
		}

		/**
		 * 增加监听器
		 * 
		 * @param onScrollChangedListener
		 */
		public void addScrollChangedListener(
				OnScrollChangedListener onScrollChangedListener) {
			listeners.add(onScrollChangedListener);
		}

		/**
		 * 移除不用的滑动监听器
		 * 
		 * @param onScrollChangedListener
		 */
		public void removeScrollListener(
				OnScrollChangedListener onScrollChangedListener) {
			if (listeners.contains(onScrollChangedListener)) {
				listeners.remove(onScrollChangedListener);
			}
		}

	}

	public interface OnSizeChangedListener {
		void onChanged(boolean showKeyboard);
	}

	public void setOnSizeChangedListener(OnSizeChangedListener listener) {
		((InternalScrollView) getRootView()).setOnSizeChangedListener(listener);
	}

	/**
	 * 增加滑动监听器
	 * 
	 * @param listener
	 */
	public void addOnScrollChangedListener(OnScrollChangedListener listener) {
		((InternalScrollView) getRootView()).addScrollChangedListener(listener);
	}

	/**
	 * 移动滑动监听器
	 * 
	 * @param listener
	 */
	public void removeOnScrollChangedListener(OnScrollChangedListener listener) {
		((InternalScrollView) getRootView()).removeScrollListener(listener);
	}

	public void reset() {
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i) instanceof InternalScrollView) {
				continue;
			} else {
				removeViewAt(i);
			}
		}
	}

}
