package com.zhisland.lib.util.text;

import android.content.Context;
import android.graphics.Color;

import com.zhisland.lib.util.text.ZHLink.LinkStyleClickableSpan;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;

public class LinkSpanCreator implements SpanCreator {

	private int linkColor = Color.rgb(0x30, 0x58, 0x2c);

	@Override
	public Object createSpan(Context context, String regex, String text,
			Object obj) {
		return new LinkStyleClickableSpan(context, regex, text, linkColor,
				(OnLinkClickListener) obj);
	}

	public void setLinkColor(int color){
		linkColor = color;
	}
}
