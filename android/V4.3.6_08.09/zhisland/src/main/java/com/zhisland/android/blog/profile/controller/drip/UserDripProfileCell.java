package com.zhisland.android.blog.profile.controller.drip;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.lib.util.DensityUtil;

/**
 * 个人页点滴 cell
 */
public class UserDripProfileCell extends UserDripCellBase implements
		OnClickListener {

	public UserDripProfileCell(View view, Activity context) {
		super(view, context);
	}

	@Override
	protected void initChildView() {
		etDripContent.setVisibility(View.GONE);
		tvDripContent.setVisibility(View.VISIBLE);
		tvDripContent.setOnClickListener(this);
		llDripRoot.setPadding(DensityUtil.dip2px(0), DensityUtil.dip2px(15),
				DensityUtil.dip2px(0), DensityUtil.dip2px(15));
	}

	@Override
	protected void fillData() {
		tvDripContent.setHint(dict.hint);
		tvDripContent.setText(dict.value);
	}

	public void setOnContentClickListener(
			OnContentClickListener onContentClickListener) {
		this.onContentClickListener = onContentClickListener;
	}

	private OnContentClickListener onContentClickListener;

	public static interface OnContentClickListener {
		public void onClick(View v, CustomDict dict);
	}

	@Override
	public void onClick(View v) {
		if (onContentClickListener != null) {
			onContentClickListener.onClick(v, dict);
		}
	}

}