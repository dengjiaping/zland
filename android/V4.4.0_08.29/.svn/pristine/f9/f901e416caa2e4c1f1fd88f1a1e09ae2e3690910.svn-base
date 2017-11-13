/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PullToZoomRefreshListView extends PullToRefreshListView {

	private int curScrollValue;

	public PullToZoomRefreshListView(Context context) {
		super(context);
	}

	public PullToZoomRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToZoomRefreshListView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToZoomRefreshListView(Context context, Mode mode,
			AnimationStyle animStyle) {
		super(context, mode, animStyle);
	}

	@Override
	protected void setHeaderScroll(int value) {
		curScrollValue = value;
		if (headerScrollListener != null) {
			headerScrollListener.setHeaderScroll(value);
		}
		super.setHeaderScroll(value);
	}

	public interface HeaderScrollListener {
		void setHeaderScroll(int value);
	}

	private HeaderScrollListener headerScrollListener;

	public void setHeaderScrollListener(
			HeaderScrollListener headerScrollListener) {
		this.headerScrollListener = headerScrollListener;
	}

	@Override
	protected void myScrollTo(int x, int y) {
		if (y < 0) {

		} else {
			super.myScrollTo(x, y);
		}
	}

	@Override
	protected int getMyScrollY() {
		return curScrollValue;
	}

}
