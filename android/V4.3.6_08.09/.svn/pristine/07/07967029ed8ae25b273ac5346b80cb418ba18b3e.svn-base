package com.zhisland.android.blog.common.view.express;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.text.ZHImageSpan;
import com.zhisland.lib.util.text.ZHLink;
import com.zhisland.lib.util.text.ZHLink.LinkStyleClickableSpan;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;
import com.zhisland.lib.util.text.ZHLinkBuilder;

public class ChatViewUtil {
	public static final int PIC_MAX_SIZE = 160;
	public static final int PIC_LARGE = DensityUtil.dip2px(PIC_MAX_SIZE);
	private static ChatViewUtil _instance = null;
	private static final Object lockObj = new Object();

	private final Context context;
	private final ExpressParser parser;
	private final ZHLink link;

	public static ChatViewUtil instance(Context context) {
		if (_instance == null) {
			synchronized (lockObj) {
				if (_instance == null) {
					_instance = new ChatViewUtil(context);
				}
			}
		}
		return _instance;
	}

	private ChatViewUtil(Context context) {
		this.context = context;
		parser = ExpressParser.getInstance(context);
		link = new ZHLinkBuilder()
				.registerPattern(ZHLinkBuilder.REGEX_URL,
						ZHLink.FLAG_EXCLUDE_OTHER,
						CreatorFactory.getLinkSpanCreator())
				.registerPattern(ZHLinkBuilder.REGEX_TOPIC,
						ZHLink.FLAG_EXCLUDE_OTHER,
						CreatorFactory.getLinkSpanCreator())
				.registerPattern(ZHLinkBuilder.REGEX_APP_SCHEMA_URL,
						ZHLink.FLAG_EXCLUDE_OTHER,
						CreatorFactory.getLinkSpanCreator())
				.registerPattern(ZHLinkBuilder.REGEX_NUMBER,
						ZHLink.FLAG_EXCLUDE_OTHER,
						CreatorFactory.getLinkSpanCreator())
				.registerPattern(parser.getRegex(), ZHLink.FLAG_EXCLUDE_OTHER,
						CreatorFactory.getImageSpanCreator()).create();
	}

	public CharSequence formatText(String text, OnLinkClickListener listener,
			int textHeight) {
		if (StringUtil.isNullOrEmpty(text))
			return text;

		if (text != null) {
			text = text.replace("\n", "<br //>");
		}

		if (text != null) {
			String urlTest = text.replace("|", "&mid");
			if (ZHLink.isValidUrl(urlTest)) {
				return link.formatEditText(context, urlTest, listener);
			}
		}

		CharSequence seq = Html.fromHtml(text, parser.expIconGetter(), null);

		if (seq instanceof SpannableStringBuilder) {
			SpannableStringBuilder spans = (SpannableStringBuilder) seq;
			URLSpan[] urls = spans.getSpans(0, spans.length(), URLSpan.class);
			for (URLSpan span : urls) {
				int start = spans.getSpanStart(span);
				int end = spans.getSpanEnd(span);
				CharSequence txt = spans.subSequence(start, end);
				LinkStyleClickableSpan newSpan = new LinkStyleClickableSpan(
						context, span.getURL(), txt.toString(),
						ZhislandApplication.APP_RESOURCE
								.getColor(R.color.txt_blue), listener);
				spans.setSpan(newSpan, start, end, 1);
			}
		}

		seq = link.formatEditText(context, seq, listener);

		if (seq instanceof Spanned) {
			Spanned spanned = (Spanned) seq;
			ZHImageSpan[] images = spanned.getSpans(0, spanned.length(),
					ZHImageSpan.class);
			for (ZHImageSpan image : images) {
				image.textHeight = textHeight;
			}
		}
		return seq;
	}

	public static boolean setSize(View v, int width, int height) {
		Object obj = v.getLayoutParams();
		if (obj instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) obj;
			param.width = width;
			param.height = height;
			v.setLayoutParams(param);
			return true;
		} else if (obj instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) obj;
			param.width = width;
			param.height = height;
			v.setLayoutParams(param);
			return true;
		} else {
			return false;
		}
	}

	public static boolean setMargin(View v, int left, int top, int right,
			int bottom) {
		Object obj = v.getLayoutParams();
		if (obj instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) obj;
			param.leftMargin = left;
			param.rightMargin = right;
			param.topMargin = top;
			param.bottomMargin = bottom;
			v.setLayoutParams(param);
			return true;
		} else if (obj instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) obj;
			param.leftMargin = left;
			param.rightMargin = right;
			param.topMargin = top;
			param.bottomMargin = bottom;
			v.setLayoutParams(param);
			return true;
		} else {
			return false;
		}
	}

	public static void adjustSize(View v, int w, int h) {
		int m = w > h ? w : h;
		float scale = m > PIC_LARGE ? 1f : (float) PIC_LARGE / (float) m;
		ChatViewUtil.setSize(v, (int) (w * scale), (int) (h * scale));
	}

}
