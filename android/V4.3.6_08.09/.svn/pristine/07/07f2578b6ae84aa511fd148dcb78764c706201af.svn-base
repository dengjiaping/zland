package com.zhisland.android.blog.im.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.ToastUtil;

public class IMLinkMovementMethod extends LinkMovementMethod {

	private final Context mContext;

	public IMLinkMovementMethod(Context ctx) {
		super();
		mContext = ctx;
	}

	/**
	 * cannot call this method, please new it directly
	 */
	@Deprecated
	public static MovementMethod getInstance() {
		throw new UnsupportedOperationException(
				"cannot call this method, please new it directly");
	}

	@Override
	public boolean onTouchEvent(android.widget.TextView widget,
			android.text.Spannable buffer, android.view.MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_UP) {

			if (event.getEventTime() - event.getDownTime() > ViewConfiguration
					.getLongPressTimeout() - 10) {
				return true;
			}

			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();

			x += widget.getScrollX();
			y += widget.getScrollY();

			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
			if (link.length != 0) {
				String url = link[0].getURL();
				int start = buffer.getSpanStart(link[0]);
				int end = buffer.getSpanEnd(link[0]);
				String name = buffer.subSequence(start, end).toString();// buffer.toString();
//				AUriMgr.instance().viewRes(mContext, url, name, null);
				// TODO view res
				return true;
			}
		}

		return super.onTouchEvent(widget, buffer, event);
	}

	public static void onNumberClicked(String data, Context context) {
		IntentUtil.dialTo(context, data);
	}

}
