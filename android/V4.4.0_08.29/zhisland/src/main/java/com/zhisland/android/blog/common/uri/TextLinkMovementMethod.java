package com.zhisland.android.blog.common.uri;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by Mr.Tui on 2016/8/16.
 */
public class TextLinkMovementMethod extends LinkMovementMethod {

    Context mContext;

    public TextLinkMovementMethod(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
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
                AUriMgr.instance().viewRes(mContext, url);
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

}
