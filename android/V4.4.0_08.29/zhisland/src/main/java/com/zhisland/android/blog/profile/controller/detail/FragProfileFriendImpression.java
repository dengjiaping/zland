package com.zhisland.android.blog.profile.controller.detail;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.view.block.ProfileBaseBlock;
import com.zhisland.android.blog.profile.view.block.UserCommentBlock;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 朋友眼中的我 fragment
 */
public class FragProfileFriendImpression extends FragBase {

	private static final String PAGE_NAME = "FragProfileFriendImpression";

	// -----block 填充区域-----
	@InjectView(R.id.llFriendImpression)
	LinearLayout llFriendImpression;

	private UserDetail userDetail;
	private User user;
	private boolean commentEnable = true;

	public FragProfileFriendImpression() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.frag_profile_friend_impression, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rootView.setLayoutParams(lp);
		ButterKnife.inject(this, rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (userDetail == null) {
			return;
		}
		setDataToView();
	}

	/**
	 * 将user数据填充到view，目前只展示评论
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setDataToView() {
		// TODO 填充数据
		llFriendImpression.removeAllViews();

		List<SimpleBlock> blocks = userDetail.blocks;
		if (blocks != null) {
			for (SimpleBlock block : blocks) {
				if (block == null) {
					continue;
				}
				ProfileBaseBlock blockView = null;
				switch (block.type) {
				case SimpleBlock.TYPE_USER_COMMENT:
					blockView = new UserCommentBlock(getActivity(), userDetail, block);
					((UserCommentBlock)blockView).setCommentEnable(commentEnable);
					break;
				}

				if (blockView != null) {
					blockView.setTag(block.type);
					llFriendImpression.addView(blockView);
				}
			}
		}

		// for (int i = 0; i < 20; i++) {
		// TextView view = new TextView(getActivity());
		// view.setText("text" + i);
		// llFriendImpression.addView(view, LayoutParams.MATCH_PARENT,
		// DensityUtil.dip2px(100));
		// }

	}

	/**
	 * profile框架 传过来的user
	 */
	public void updateUser(UserDetail userDetail) {
		this.userDetail = userDetail;
		this.user = userDetail.user;
		if (this.isAdded()) {
			setDataToView();
		}
	}

	@Override
	public String getPageName() {
		return PAGE_NAME;
	}

	public void setCommentEnable(boolean commentEnable){
		this.commentEnable = commentEnable;
		Object blockView = llFriendImpression.findViewWithTag(SimpleBlock.TYPE_USER_COMMENT);
		if(blockView != null && blockView instanceof UserCommentBlock){
			((UserCommentBlock)blockView).setCommentEnable(commentEnable);
		}
	}

}
