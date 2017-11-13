package com.zhisland.android.blog.common.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.lib.util.text.ZHLink;

/**
 * 将TextView中的网址变成zhisland风格的文字，并对文字span加入点击事件
 * 现在的功能只有跳转到webview
 * @author zhangxiang
 *
 */
public class TextViewAutoLinkUtil {

	/**
	 * 默认网址点击是跳转到FragWebViewActivity,但如果想要做别的，可以先实现这个接口
	 * @author zhangxiang
	 *
	 */
	public interface ILinkAction {
		public void action();
	}

	/**
	 * 将网址在textview中 强转颜色和点击跳转到zhisland的webview
	 * @param tv
	 * @param linkAction
	 */
	public static void formatLink(final TextView tv,
			final ILinkAction linkAction) {
		int color = tv.getCurrentTextColor();
		final CharSequence str = tv.getText();
		if (!ZHLink.isValidUrl(str.toString()) && !ZHLink.isValidTitleAndIsUrl(str.toString())) {
			return;
		}
		SpannableString ss = new SpannableString(str);

		ss.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				if (linkAction != null) {
					linkAction.action();
				} else {
					FragWebViewActivity.launch(widget.getContext(),
							str.toString(), "", "", "", true, false);
				}

			}
		}, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(color), 0,
				str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new UnderlineSpan() {
			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setUnderlineText(false);
			}
		}, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ss);

		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
