package com.zhisland.android.blog.profile.view.block;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.controller.detail.CommFrinedsAdapter;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.HorizontalListView;

/**
 * 共同好友 block
 */
public class UserCommonFriendBlock extends ProfileBaseCommonBlock<User> {

	public UserCommonFriendBlock(Activity context, UserDetail userDetail,
			SimpleBlock<User> block) {
		super(context, userDetail, block);
	}

	@Override
	protected void initChildViews() {
		tvEmptyDesc.setText("你和TA没有共同好友");
		tvEmptyButton.setVisibility(View.GONE);
	}

	@Override
	protected void loadData(final List<User> datas) {
		HorizontalListView view = (HorizontalListView) LayoutInflater.from(
				context).inflate(R.layout.profile_common_friend, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, context.getResources()
						.getDimensionPixelSize(R.dimen.common_friend_width));
		params.topMargin = DensityUtil.dip2px(5);
		view.setLayoutParams(params);
		CommFrinedsAdapter commFrinedsAdapter = new CommFrinedsAdapter(context);
		commFrinedsAdapter.add(datas);
		view.setAdapter(commFrinedsAdapter);
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				ActProfileDetail.invokeNoHistory(context,
						datas.get(position).uid);
			}
		});
		llBlockContent.addView(view);
	}

	@Override
	protected void onIvRightClick() {
	}

	@Override
	protected void ontvRightClick() {
	}

	@Override
	protected void onTvEmptyButtonClick() {
	}
}
