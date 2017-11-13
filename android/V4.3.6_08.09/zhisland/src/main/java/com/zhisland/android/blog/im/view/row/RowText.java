package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.ChatViewUtil;
import com.zhisland.android.blog.im.view.MaxWidthLinearLayout;
import com.zhisland.android.blog.im.view.adapter.DoubleClickText;
import com.zhisland.android.blog.im.view.adapter.IMLinkMovementMethod;
import com.zhisland.android.blog.im.view.adapter.ReadTextDialog;
import com.zhisland.android.blog.im.view.adapter.DoubleClickText.OnTextClickListener;
import com.zhisland.im.data.IMMessage;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;

public class RowText extends BaseRowView implements OnTextClickListener {

	private static final int MAX_LINE = 10;

	private DoubleClickText tvContent;
	private OnLinkClickListener onLinkListener;
	private final ReadTextDialog readDialog;

	// private int width;
	private CharSequence content;

	public RowText(Context context, ReadTextDialog readDialog) {
		super(context, CONTENT_TYPE_NORMAL);
		this.readDialog = readDialog;
	}

	// @Override
	// public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	//
	// int newWidth = tvContent.getMeasuredWidth();
	// if (newWidth > 0 && newWidth != width) {
	// width = newWidth;
	// setContent(content);
	// }
	// }

	private void setContent(CharSequence content) {
		if (content == null) {
			return;
		}
		this.content = content;
		tvContent.setText(content);
	}

	// private boolean isLarger(TextView text, String newText) {
	// // 估测的文字两边空余，以及划词产生的空白
	// int padding = DensityUtil.dip2px(10);
	// float textWidth = text.getPaint().measureText(newText);
	// float W = (width - padding * 2) * MAX_LINE;
	// return (textWidth >= W);
	// }

	// ============public methods=============
	public void setOnLinkListener(OnLinkClickListener linkListener) {
		this.onLinkListener = linkListener;
	}

	// ===========create view methods========
	@Override
	public void addContentView(MaxWidthLinearLayout container, Context context) {

		container.setOrientation(LinearLayout.VERTICAL);

		tvContent = new DoubleClickText(context);
		tvContent.setOnClickListener(this);
		DensityUtil.setTextSize(tvContent, R.dimen.txt_18);
		tvContent.setIncludeFontPadding(false);
		tvContent.setTextColor(context.getResources()
				.getColor(R.color.color_bg1));
		tvContent.setGravity(Gravity.CENTER_VERTICAL);
		tvContent.setLineSpacing(0, 1.1f);
//		tvContent.setAutoLinkMask(Linkify.ALL);
		this.tvContent.setMovementMethod(new IMLinkMovementMethod(context));

		container.addView(tvContent,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		container.setGravity(Gravity.CENTER);
	}

	@Override
	public void configMyView() {
		super.configMyView();
		tvContent.setTextColor(context.getResources()
				.getColor(R.color.color_bg1));
	}

	@Override
	public void configOtherView() {
		super.configOtherView();
		tvContent.setTextColor(context.getResources()
				.getColor(R.color.color_f1));
	}

	// ====== override of onRowListener========

	@Override
	public void fill(IMMessage msg) {
		super.fill(msg);

		if (msg != null) {
			CharSequence msgBody = ChatViewUtil.instance(getContext())
					.formatText(msg.body, onLinkListener,
							tvContent.getLineHeight());
			setContent(msgBody);
		}
	}

	@Override
	public void recycle() {
		super.recycle();
		tvContent.setText(null);
	}

	@Override
	public void onDoubleClick(View view) {
		if (readDialog != null) {
			readDialog.setText(msg.body, onLinkListener);
			if (!readDialog.isShowing()) {
				readDialog.show();
			}
		}
	}

	@Override
	public void refresh(int status, int progress) {
		// TODO Auto-generated method stub

	}

}
