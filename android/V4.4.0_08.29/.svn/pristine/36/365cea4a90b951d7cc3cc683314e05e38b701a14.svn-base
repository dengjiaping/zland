package com.zhisland.lib.view.shrink;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zhisland.lib.R;

public class ShrinkRelativeLayout extends RelativeLayout implements
		OnClickListener {

	private OnClickListener clickListener;

	public ShrinkRelativeLayout(Context context) {
		super(context);
		init(context);

	}

	public ShrinkRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ShrinkRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		super.setOnClickListener(this);

		MAX_MOVE_LENGTH = getResources().getDimensionPixelOffset(
				R.dimen.shrink_move_size);

		shrinkAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		shrinkAnim.setFillAfter(true);
		shrinkAnim.setDuration(100);

		largeAnim = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		largeAnim.setFillAfter(true);
		largeAnim.setDuration(100);
	}

	private int MAX_MOVE_LENGTH;
	private int downX = -1, downY = -1;
	private boolean shrinked = false;
	private ScaleAnimation shrinkAnim, largeAnim;
	private Runnable clickRunnable = new Runnable() {

		@Override
		public void run() {
			if (clickListener != null) {
				clickListener.onClick(ShrinkRelativeLayout.this);
				postDelayed(clearRunnable, 100);
			}
		}
	};

	private Runnable clearRunnable = new Runnable() {

		@Override
		public void run() {
			ShrinkRelativeLayout.this.clearAnimation();
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			doShrink();
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (downX > 0) {
				int diffX = (int) Math.abs(downX - event.getX());
				int diffY = (int) Math.abs(downY - event.getY());
				if (diffX >= MAX_MOVE_LENGTH || diffY >= MAX_MOVE_LENGTH) {
					doLarge(false);
				}
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			doLarge(false);
			break;
		}
		case MotionEvent.ACTION_UP: {

			// doLarge(true);
			break;
		}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		postDelayed(clickRunnable, 50);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.clickListener = l;
	}

	private void doShrink() {
		startAnimation(shrinkAnim);
		shrinked = true;
	}

	private void doLarge(boolean imediate) {
		if (shrinked) {
			if (imediate) {
				this.clearAnimation();
			} else {
				startAnimation(largeAnim);
				shrinked = false;
			}
		}

	}

}
