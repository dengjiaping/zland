package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;

/**
 * 个人页普通 block 基类
 */
public abstract class ProfileBaseCommonBlock<D> extends ProfileBaseBlock<D>
		implements View.OnClickListener {

	// ---------content区------------------------
	// content block 标题
	protected TextView tvBlockContentTitle;
	// content block right
	protected TextView tvBlockContentRight;
	// content block 标题 右上角图片按钮
	protected ImageView ivBlockContentEdit;
	// content block 内容区
	protected LinearLayout llBlockContent;

	// ---------empty区------------------------
	// block 标题 右上角文字按钮
	protected TextView tvBlockEmptyTitle;
	// empty block 标题 右上角图片按钮
	protected ImageView ivBlockEmptyEdit;
	// empty 描述
	protected TextView tvEmptyDesc;
	// empty 按钮
	protected TextView tvEmptyButton;

	private View contentView;
	private View emptyView;

	public ProfileBaseCommonBlock(Activity context, UserDetail userDetail,
			SimpleBlock<D> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildView() {
		initBaseViews();
		initChildViews();
		initClickListener();
	}

	/**
	 * 初始化基础view
	 */
	private void initBaseViews() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.profile_block_content, null);
		tvBlockContentTitle = (TextView) contentView
				.findViewById(R.id.tvBlockContentTitle);
		tvBlockContentRight = (TextView) contentView
				.findViewById(R.id.tvBlockContentRight);
		ivBlockContentEdit = (ImageView) contentView
				.findViewById(R.id.ivBlockContentEdit);
		llBlockContent = (LinearLayout) contentView
				.findViewById(R.id.llBlockContent);

		emptyView = LayoutInflater.from(context).inflate(
				R.layout.profile_block_empty, null);
		tvBlockEmptyTitle = (TextView) emptyView
				.findViewById(R.id.tvBlockEmptyTitle);
		ivBlockEmptyEdit = (ImageView) emptyView
				.findViewById(R.id.ivBlockEmptyEdit);
		tvEmptyDesc = (TextView) emptyView.findViewById(R.id.tvEmptyDesc);
		tvEmptyButton = (TextView) emptyView.findViewById(R.id.tvEmptyButton);

		initTitleView();
	}

	/**
	 * 设置 title view
	 */
	private void initTitleView() {
		tvBlockContentTitle.setText(getBlockTitle());
		tvBlockEmptyTitle.setText(getBlockTitle());
		if (isUserSelf()) {
			ivBlockContentEdit.setVisibility(View.VISIBLE);
		} else {
			ivBlockContentEdit.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化click listener
	 */
	private void initClickListener() {
		ivBlockContentEdit.setOnClickListener(this);
		tvBlockContentRight.setOnClickListener(this);
		tvEmptyButton.setOnClickListener(this);
		ivBlockEmptyEdit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view == ivBlockContentEdit || view == ivBlockEmptyEdit) {
			onIvRightClick();
		} else if (view == tvBlockContentRight) {
			ontvRightClick();
		} else if (view == tvEmptyButton) {
			onTvEmptyButtonClick();
		}
	}

	// 显示内容
	@Override
	protected void showContentView(boolean isUpdate) {
		super.showContentView(isUpdate);
		if (isUserSelf()) {
			ivBlockContentEdit.setVisibility(View.VISIBLE);
		} else {
			ivBlockContentEdit.setVisibility(View.GONE);
		}
	}

	// 显示 EmptyView
	@Override
	protected void showEmptyView() {
		super.showEmptyView();
		ivBlockContentEdit.setVisibility(View.GONE);
	}

	@Override
	protected void updateData(List<D> datas) {
		llBlockContent.removeAllViews();
		loadData(datas);
	}

	/**
	 * 初始化子view
	 */
	protected abstract void initChildViews();

	/**
	 * title 右边图片点击事件
	 */
	protected abstract void onIvRightClick();

	/**
	 * title 右边文字点击事件
	 */
	protected abstract void ontvRightClick();

	/**
	 * 底部empty button click
	 */
	protected abstract void onTvEmptyButtonClick();

	@Override
	protected View setContentView() {
		return contentView;
	}

	@Override
	protected View setEmptyView() {
		return emptyView;
	}

}
