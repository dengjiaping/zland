package com.zhisland.android.blog.im.view.row;

import android.view.View;
import android.view.View.OnCreateContextMenuListener;

import com.zhisland.android.blog.R;
import com.zhisland.im.data.IMMessage;
import com.zhisland.lib.util.DensityUtil;

public class BaseRowUtil {

	static final int PADDING = DensityUtil.dip2px(8);
	static final int PADDING_BLOCK = DensityUtil.dip2px(6);
	static int selfPaddingLeft, selfPaddingRight, otherPaddingLeft,
			otherPaddingRight;

	/**
	 * config a view with "self" background
	 */ 
	public static void configSelfBackground(View view, int contentType) {
		view.setBackgroundResource(R.drawable.sel_bg_chat_my);
		if (selfPaddingRight <= 0) {
			// 因为自己在右边，可能没有左边的padding，所以判断右边的怕d定
			selfPaddingLeft = view.getPaddingLeft();
			selfPaddingRight = view.getPaddingRight();
		}
		switch (contentType) {
		case BaseRowView.CONTENT_TYPE_BLOCK: {
			view.setPadding(selfPaddingLeft + PADDING_BLOCK, PADDING_BLOCK,
					selfPaddingRight + PADDING_BLOCK, PADDING_BLOCK);
			break;
		}
		case BaseRowView.CONTENT_TYPE_NONE: {
			view.setPadding(selfPaddingLeft, PADDING, selfPaddingRight, PADDING);
			break;
		}
		default: {
			view.setPadding(selfPaddingLeft + PADDING, PADDING,
					selfPaddingRight + PADDING, PADDING);
		}
		}
	}

	/**
	 * config a view with "other" background
	 */
	public static void configOtherBackground(View view, int contentType) {
		view.setBackgroundResource(R.drawable.sel_bg_chat_their);
		if (otherPaddingLeft <= 0) {
			// 因为自己在左边，可能没有右边的padding，所以判断左边的怕d定
			otherPaddingLeft = view.getPaddingLeft();
			otherPaddingRight = view.getPaddingRight();
		}
		switch (contentType) {
		case BaseRowView.CONTENT_TYPE_BLOCK: {
			view.setPadding(otherPaddingLeft + PADDING_BLOCK, PADDING_BLOCK,
					otherPaddingRight + PADDING_BLOCK, PADDING_BLOCK);
			break;
		}
		case BaseRowView.CONTENT_TYPE_NONE: {
			view.setPadding(otherPaddingLeft, PADDING, otherPaddingRight,
					PADDING);
			break;
		}
		default: {
			view.setPadding(otherPaddingLeft + PADDING, PADDING,
					otherPaddingRight + PADDING, PADDING);
		}
		}
	}

	public static void cleanSelfOtherBackground(View view) {
		view.setBackgroundDrawable(null);
		view.setPadding(0, 0, 0, 0);
	}

	public static void fillMenu(View view,
			OnCreateContextMenuListener createMenuListener, IMMessage msg,
			OnRowListener rowListener) {
		view.setOnCreateContextMenuListener(createMenuListener);
		view.setTag(R.id.chat_data_mes, msg);
		view.setTag(R.id.chat_data_listener, rowListener);
	}

	public static void cleanMenu(View view) {
		view.setOnCreateContextMenuListener(null);
		view.setTag(R.id.chat_data_mes, null);
		view.setTag(R.id.chat_data_listener, null);
	}
}
