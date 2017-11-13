package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserDetail;

/**
 * 个人页block基类
 * 
 * @param <D>
 *            填充的数据类型
 */
public abstract class ProfileBaseBlock<D> extends FrameLayout {

	private static final int TAG_CONTENT_VIEW = 1;
	private static final int TAG_EMPTY_VIEW = 2;

	// 是否是用户自己
	protected Activity context;

	private SimpleBlock<D> block;
	private UserDetail userDetail;

	/**
	 * 获取 block 数据list
	 */
	public List<D> getBlockDatas() {
		return block.data;
	}

	/**
	 * 获取 block
	 */
	public SimpleBlock<D> getBlock() {
		return block;
	}

	/**
	 * 获取block title
	 */
	public String getBlockTitle() {
		return block.title;
	}

	/**
	 * 获取user
	 */
	public User getBlockUser() {
		return userDetail.user;
	}

	/**
	 * 获取userDetail
	 */
	public UserDetail getUserDetail() {
		return userDetail;
	}

	/**
	 * 获取数据总数
	 */
	public int getBlockDataTotal() {
		return block.total;
	}

	/**
	 * 是否是当前用户
	 */
	public boolean isUserSelf() {
		if (userDetail.user != null) {
			return userDetail.user.uid == PrefUtil.Instance().getUserId();
		}
		return false;
	}

	public ProfileBaseBlock(Activity context, UserDetail userDetail,
			SimpleBlock<D> block) {
		super(context, null, 0);
		this.context = context;
		this.userDetail = userDetail;
		this.block = block;

		initChildView();
		initBaseView();
		initBaseData(false);
	}

	/**
	 * 初始化子类view
	 */
	protected abstract void initChildView();

	/**
	 * 添加content view 和 emtpy view
	 */
	private void initBaseView() {
		View contentView = setContentView();
		if (contentView != null) {
			contentView.setTag(TAG_CONTENT_VIEW);
			addView(contentView);
		}
		View emptyView = setEmptyView();
		if (emptyView != null) {
			emptyView.setTag(TAG_EMPTY_VIEW);
			addView(emptyView);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initBaseData(boolean isUpdate) {
		List<D> blockDatas = getBlockDatas();
		if (blockDatas == null || blockDatas.size() == 0
				|| blockDatas.get(0) == null) {
			showEmptyView();
		} else {
			showContentView(isUpdate);
		}
	}

	/**
	 * 设置内容view
	 */
	protected abstract View setContentView();

	/**
	 * 设置empty View
	 */
	protected abstract View setEmptyView();

	/**
	 * 显示内容
	 */
	protected void showContentView(boolean isUpdate) {
		View contentView = findViewWithTag(TAG_CONTENT_VIEW);
		if (contentView != null) {
			contentView.setVisibility(View.VISIBLE);
		}
		View emptyView = findViewWithTag(TAG_EMPTY_VIEW);
		if (emptyView != null) {
			emptyView.setVisibility(View.GONE);
		}
		// 加载数据
		if (isUpdate) {
			updateData(getBlockDatas());
		} else {
			loadData(getBlockDatas());
		}
	}

	/**
	 * 显示 EmptyView
	 */
	protected void showEmptyView() {
		View contentView = findViewWithTag(TAG_CONTENT_VIEW);
		if (contentView != null) {
			contentView.setVisibility(View.GONE);
		}
		View emptyView = findViewWithTag(TAG_EMPTY_VIEW);
		if (emptyView != null) {
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 填充数据
	 * 
	 * @param datas
	 *            数据源
	 */
	protected abstract void loadData(List<D> datas);

	/**
	 * 刷新数据
	 * 
	 * @param datas
	 *            数据源
	 */
	protected abstract void updateData(List<D> datas);

	/**
	 * 刷新block
	 */
	public void updateBlock(SimpleBlock<D> block) {
		this.block = block;
		initBaseData(true);
	}
}
