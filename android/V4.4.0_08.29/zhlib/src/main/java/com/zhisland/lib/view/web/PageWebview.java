package com.zhisland.lib.view.web;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Scroller;

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.PageControl;

public class PageWebview extends WebView {

    private static String TAG = "PageWebview";

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    public static int SNAP_VELOCITY = 400; // 速度
    private static int DURATION = 150;

    private static final int MSG_PRE = 100;
    private static final int MSG_NEXT = 101;

    private boolean isHorizontal = true;

    private final Context mContext;
    private VelocityTracker mVelocityTracker = null;
    private Scroller mScroller = null;
    private int mTouchSlop = 0;
    private PageControl pageControl = null;
    private PageWebListener pageWebListener = null;

    private int curScreen = -1;
    private int mTouchState = TOUCH_STATE_REST;
    private float mLastionMotionX = 0;
    private int screenCount = 0;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PRE:
                    pageWebListener.preArticle();
                    pageControl.reset();
                    break;
                case MSG_NEXT:
                    pageControl.reset();
                    pageWebListener.nextArticle();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    public PageWebview(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PageWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @Override
    public void computeScroll() {
        if (!isHorizontal) {
            super.computeScroll();
        } else {
            if (mScroller.computeScrollOffset()) {
                // 产生了动画效果 每次滚动一点
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                // 刷新View 否则效果可能有误差
                postInvalidate();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isHorizontal) {
            return super.onInterceptTouchEvent(ev);
        }

        final int action = ev.getAction();
        // 表示已经开始滑动了，不需要走该Action_MOVE方法了(第一次时可能调用)。
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastionMotionX - x);
                // 超过了最小滑动距离
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastionMotionX = x;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isHorizontal) {
            return super.onTouchEvent(event);
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果屏幕的动画还没结束，你就按下了，我们就结束该动画
                if (mScroller != null) {
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                }
                mLastionMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:

                int detaX = (int) (mLastionMotionX - x);
                scrollBy(detaX, 0);
                mLastionMotionX = x;

                break;
            case MotionEvent.ACTION_UP:
                boolean snapped = false;
                int diff = getScrollX() - curScreen * getWidth();
                if (Math.abs(diff) < mTouchSlop) {

                    // simulate click event
                    event.setAction(MotionEvent.ACTION_DOWN);
                    super.onTouchEvent(event);

                    event.setAction(MotionEvent.ACTION_UP);
                    super.onTouchEvent(event);

                    snapped = true;
                    // }
                }
                if (!snapped) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000);
                    int velocityX = (int) velocityTracker.getXVelocity();
                    MLog.e(TAG, "---velocityX---" + velocityX);

                    if (velocityX > SNAP_VELOCITY && curScreen > 0) {
                        snapToScreen(curScreen - 1);
                    } else if (velocityX < -SNAP_VELOCITY
                            && curScreen < (screenCount - 1)) {
                        snapToScreen(curScreen + 1);
                    } else {
                        snapToDestination();
                    }

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }

                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        return true;
    }

    /**
     * change mode of webview
     */
    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public boolean isHor() {
        return isHorizontal;
    }

    /**
     * 我们是缓慢移动的
     */
    private void snapToDestination() {
        int dx = getScrollX() - curScreen * getWidth();

        int minDx;
        minDx = getWidth() / 3;
        if (dx > 0 && dx >= minDx && curScreen < screenCount) {
            snapToScreen(curScreen + 1);
        } else if (dx < 0 && dx < -minDx && curScreen > -1) {
            snapToScreen(curScreen - 1);
        } else {
            snapToScreen(curScreen);
        }
    }

    private void snapToScreen(int whichScreen) {
        snapToScreen(whichScreen, DURATION);
    }

    private void snapToScreen(int whichScreen, int duration) {

        int destScreen = whichScreen;
        if (whichScreen < 0) {
            if (pageWebListener == null) {
                destScreen = 0;
            } else if (!pageWebListener.canPre()) {
                destScreen = 0;
            }
        }
        if (whichScreen >= screenCount) {
            if (pageWebListener == null) {
                destScreen = screenCount - 1;
            } else if (!pageWebListener.canNext()) {
                destScreen = screenCount - 1;
            }
        }

        int dx = destScreen * getWidth() - getScrollX();
        mScroller.startScroll(getScrollX(), 0, dx, 0, duration);
        invalidate();
        if (destScreen == whichScreen) {
            pageControl.setCurrentPage(destScreen);
        }
        curScreen = destScreen;

        if (pageWebListener != null) {
            if (destScreen == -1) {
                Message msg = handler.obtainMessage(MSG_PRE);
                handler.sendMessage(msg);
            } else if (destScreen == screenCount) {
                Message msg = handler.obtainMessage(MSG_NEXT);
                handler.sendMessage(msg);
            } else {
                pageWebListener.pageChanged(destScreen, curScreen);
            }
        }
    }

    private void init() {
        this.setBackgroundColor(Color.rgb(0xf7, 0xf7, 0xf7));
        mScroller = new Scroller(mContext);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public int getScreenCount() {
        return screenCount;
    }

    public void setScreenCount(int screenCount, int curScreen) {
        this.screenCount = screenCount;
        this.curScreen = curScreen;
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        if (mScroller != null) {
            mScroller.abortAnimation();
        }
        mLastionMotionX = 0;
        mTouchState = TOUCH_STATE_REST;
        if (this.pageControl != null) {
            pageControl.setPageCount(screenCount);
            snapToScreen(this.curScreen, 0);
        }
    }

    public void setPageControl(PageControl pageControl) {
        this.pageControl = pageControl;
    }

    public void setPageChangedListener(PageWebListener pageListener) {
        this.pageWebListener = pageListener;
    }

    public static interface PageWebListener {

        void pageChanged(int newPage, int oldCount);

        /**
         * drag to previous article
         */
        void preArticle();

        /**
         * drag to next article
         */
        void nextArticle();

        /**
         * can navigate to previous
         */
        boolean canPre();

        /**
         * can navigate to next
         *
         * @return
         */
        boolean canNext();
    }

}
