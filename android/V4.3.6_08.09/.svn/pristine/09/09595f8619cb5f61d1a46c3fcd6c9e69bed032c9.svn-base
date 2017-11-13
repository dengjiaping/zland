package com.zhisland.android.blog.im.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.im.view.ChatViewUtil;
import com.zhisland.android.blog.im.view.adapter.DoubleClickText.OnTextClickListener;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.text.ZHLink.OnLinkClickListener;
 
public class ReadTextDialog extends Dialog implements OnTextClickListener {

	private static final int PADDING = DensityUtil.dip2px(8);
	private final DoubleClickText tvContent;

	public ReadTextDialog(Context context) {
		super(context, android.R.style.Theme);
		setOwnerActivity((Activity) context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		tvContent = new DoubleClickText(getContext());
		tvContent.setMovementMethod(new IMLinkMovementMethod(context));
		DensityUtil.setTextSize(tvContent, R.dimen.txt_22);
		tvContent.setTextColor(Color.BLACK);
		tvContent.setPadding(PADDING, PADDING, PADDING, PADDING);
		tvContent.setGravity(Gravity.CENTER);
		tvContent.setOnClickListener(this);
		tvContent.setBackgroundColor(Color.WHITE);
	}

	public void setText(String text, OnLinkClickListener linkListener) {
		tvContent.setText(ChatViewUtil.instance(getContext()).formatText(text,
				linkListener, tvContent.getLineHeight()));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(getContext());
		tvContent.setMinHeight(DensityUtil.getHeight()
				- DensityUtil.getStatusHeight(getContext()));
		sv.addView(tvContent);
		setContentView(sv, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = DensityUtil.getWidth();
		lp.height = DensityUtil.getHeight();
		lp.alpha = 1.0f;
		dialogWindow.setAttributes(lp);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onDoubleClick(View view) {
		this.dismiss();
	}

}