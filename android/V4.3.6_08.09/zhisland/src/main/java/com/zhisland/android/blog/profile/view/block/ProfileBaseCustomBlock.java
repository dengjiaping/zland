package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.View;

import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;

/**
 * 个人页自定义 view block 基类
 */
public abstract class ProfileBaseCustomBlock<D> extends ProfileBaseBlock<D> {

	public ProfileBaseCustomBlock(Activity context, UserDetail userDetail,
			SimpleBlock<D> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildView() {
	}

	@Override
	protected View setContentView() {
		return setCustomContentView();
	}

	@Override
	protected View setEmptyView() {
		return setCustomEmptyView();
	}

	@Override
	protected void updateData(List<D> datas) {
		loadData(datas);
	}

	/**
	 * 设置自定义内容view
	 */
	protected abstract View setCustomContentView();

	/**
	 * 设置自定义空view
	 */
	protected abstract View setCustomEmptyView();

}
