package com.zhisland.lib.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.lib.util.DensityUtil;

/**
 * 方格排版ViewGroup。先横向排版child，超过width换行继续排。
 * */
public class AutoWrapViewGroup extends ViewGroup {

	private int rowContentCount = 1;
	private int horInterval = 10;
	private int verInterval = 10;
	private int imgSize = DensityUtil.dip2px(60);

	public AutoWrapViewGroup(Context context) {
		super(context);
	}

	/**
	 * 设置横向item间距
	 * 
	 * @param interval
	 */
	public void setHorizontalInterval(int interval) {
		this.horInterval = interval;
	}

	/**
	 * 设置纵向item间距
	 * 
	 * @param interval
	 */
	public void setVerticalInterval(int interval) {
		this.verInterval = interval;
	}

	/**
	 * 设置item边长
	 */
	public void setChildSize(int size) {
		this.imgSize = size;
	}

	/**
	 * 设置行数
	 * */
	public void setChildRowCount(int count) {
		this.rowContentCount = count;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int count = getChildCount();
		int visibleCount = 0;
		for (int i = 0; i < count; i++) {
			if (getChildAt(i).getVisibility() == View.VISIBLE) {
				visibleCount++;
			}
		}
		int rowCount = (visibleCount + rowContentCount - 1) / rowContentCount;
		int height = rowCount * imgSize + (rowCount - 1) * verInterval;
		int expandSpec = MeasureSpec.makeMeasureSpec(height,
				MeasureSpec.AT_MOST);

		// for (int index = 0; index < getChildCount(); index++) {
		// final View child = getChildAt(index);
		// // measure
		// child.measure(expandSpec, expandSpec);
		// }

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		final int count = getChildCount();
		int row = 0;// which row lay you view relative to parent
		int lengthX = 0; // right position of child relative to parent
		int lengthY = 0; // bottom position of child relative to parent
		for (int i = 0; i < count; i++) {

			final View child = this.getChildAt(i);
			int width = imgSize;
			int height = imgSize;
			if (i == 0) {
				lengthX += width;
			} else {
				lengthX += width + horInterval;
			}
			if (row == 0) {
				lengthY = height;
			} else {
				lengthY = row * (height + verInterval) + height;
			}
			// if it can't drawing on a same line , skip to next line
			if (lengthX > right) {
				lengthX = width;
				row++;
				lengthY = row * (height + verInterval) + height;

			}

			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);

		}
	}
}