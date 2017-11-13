package com.zhisland.android.blog.im.view.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.zhisland.lib.util.MLog;

public class DoubleClickText extends TextView implements OnGestureListener,
		OnTouchListener {

	private static final String TAG = "doubletext";

	public static interface OnTextClickListener extends OnClickListener {
		void onDoubleClick(View view);
	}

	private OnTextClickListener textClickListener;
	private GestureDetector gestureScanner;
	private boolean waitDoubleClick = false;

	public DoubleClickText(Context context) {
		super(context);

		this.setOnTouchListener(this);
		gestureScanner = new GestureDetector(this);
	}

	@Override
	public final void setOnClickListener(OnClickListener l) {
		if (!(l instanceof OnTextClickListener)) {
			throw new UnsupportedOperationException(
					"must a OnTextClickListener");
		} else {
			this.setOnClickListener((OnTextClickListener) l);
		}
	}

	public void setOnClickListener(OnTextClickListener clickListener) {
		this.textClickListener = clickListener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
	}

	private boolean performSuperOnTouch(MotionEvent e) {
		MLog.d(TAG,
				Thread.currentThread().getId() + " super on down "
						+ e.toString());
		return super.onTouchEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		if (!waitDoubleClick) {
			performSuperOnTouch(MotionEvent.obtain(e));
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		final MotionEvent event = MotionEvent.obtain(e);
		if (!waitDoubleClick) {
			waitDoubleClick = true;
			getHandler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (waitDoubleClick) {
						performSuperOnTouch(MotionEvent.obtain(event));
						waitDoubleClick = false;
					}
				}
			}, 300);
		} else {
			waitDoubleClick = false;
			if (textClickListener != null) {
				textClickListener.onDoubleClick(this);
			}
		}
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

}