package com.zhisland.lib.util.text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhisland.lib.util.StringUtil;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ZHLink {

	public static final int FLAG_INCLUDE_OTHER = 1;
	public static final int FLAG_EXCLUDE_OTHER = 2;

	public interface OnLinkClickListener {
		void onLinkClicked(Context context, String regex, String text);
	}

	protected ArrayList<ZHPattern> includeRegexes = new ArrayList<ZHPattern>();
	protected ArrayList<ZHPattern> excluderRegexes = new ArrayList<ZHPattern>();
	private boolean clearSpanBeforeFormat = false;

	public ZHLink(ArrayList<ZHPattern> excluderRegexes,
			ArrayList<ZHPattern> includeRegexes) {
		this.excluderRegexes = excluderRegexes;
		this.includeRegexes = includeRegexes;
	}

	public ZHLink(ArrayList<ZHPattern> excluderRegexes,
			ArrayList<ZHPattern> includeRegexes, boolean clearSpanBeforeFormat) {
		this.excluderRegexes = excluderRegexes;
		this.includeRegexes = includeRegexes;
		this.clearSpanBeforeFormat = clearSpanBeforeFormat;
	}

	public static boolean isValidUrl(String url) {
		if(StringUtil.isNullOrEmpty(url)){
			return false;
		}
		Pattern pattern = Pattern.compile(ZHLinkBuilder.REGEX_URL);
		Matcher matcher = pattern.matcher(url);
		if (matcher != null) {
			return matcher.matches();
		}
		return false;
	}
	public static boolean isValidTitleAndIsUrl(String url){
		//空的也是不合法的
		if(StringUtil.isNullOrEmpty(url)){
			return true;
		}
		Pattern pattern = Pattern.compile(ZHLinkBuilder.REGEX_URL_NO_HEAD);
		Matcher matcher = pattern.matcher(url);
		if (matcher != null) {
			//如果是个网址的话，返回true
			return matcher.matches();
		}
		return false;
	}
	public static boolean isValidNumber(String number) {
		if(StringUtil.isNullOrEmpty(number)){
			return false;
		}
		Pattern pattern = Pattern.compile(ZHLinkBuilder.REGEX_NUMBER);
		Matcher matcher = pattern.matcher(number);
		if (matcher != null) {
			return matcher.matches();
		}
		return false;
	}

	public CharSequence formatEditText(Context context,
			CharSequence originalString, OnLinkClickListener listener) {
		if (originalString == null)
			return null;

		// String originalString = editText.getText().toString();

		ArrayList<Span> excludeSpans = new ArrayList<ZHLink.Span>();
		final SpannableStringBuilder textStyle = new SpannableStringBuilder(
				originalString);

		if (clearSpanBeforeFormat) {
			textStyle.clearSpans();
		}

		for (ZHPattern zpat : excluderRegexes) {

			Matcher matcher = zpat.realPat.matcher(originalString);
			while (matcher.find()) {

				String matchText = matcher.group();
				int start = matcher.start();
				int end = matcher.end();
				boolean crossed = false;
				for (Span span : excludeSpans) {
					if (span.cross(start, end - 1)) {
						crossed = true;
						break;
					}
				}
				if (crossed) {
					continue;
				}
				excludeSpans.add(new Span(start, end - 1));
				textStyle.setSpan(zpat.creator.createSpan(context,
						zpat.realPat.pattern(), matchText, listener), start,
						end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

		}

		for (ZHPattern zpat : includeRegexes) {

			Matcher matcher = zpat.realPat.matcher(originalString);
			while (matcher.find()) {

				String matchText = matcher.group();
				int start = matcher.start();
				int end = matcher.end();
				boolean crossed = false;
				for (Span span : excludeSpans) {
					if (span.cross(start, end)) {
						crossed = true;
						break;
					}
				}
				if (crossed) {
					continue;
				}
				textStyle.setSpan(zpat.creator.createSpan(context,
						zpat.realPat.pattern(), matchText, listener), start,
						end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

		}

		// editText.setText(textStyle);
		return textStyle;
	}

	public static class LinkStyleClickableSpan extends ClickableSpan {

		private final Context context;
		private final String txt;
		private final String regex;
		private final int color;
		private final OnLinkClickListener listener;

		public LinkStyleClickableSpan(Context context, String regex,
				String txt, int color, OnLinkClickListener listener) {
			this.context = context;
			this.txt = txt;
			this.regex = regex;
			this.color = color;
			this.listener = listener;
		}

		@Override
		public void updateDrawState(final TextPaint ds) {
			ds.setUnderlineText(false);
			ds.setColor(this.color);

		}

		@Override
		public void onClick(View widget) {
			if (listener != null) {
				listener.onLinkClicked(context, regex, txt);
			}
		}
	}

	protected class Span {
		int start;
		int end;

		public Span(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public boolean cross(int st, int ed) {

			if (st >= start && st <= end) {
				return true;
			}

			if (ed >= start && ed <= end) {
				return true;
			}

			return false;
		}
	}
}
