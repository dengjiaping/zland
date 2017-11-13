package com.zhisland.android.blog.profile.controller.comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.eb.EBUserComment;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.DensityUtil;

import de.greenrobot.event.EventBus;

/**
 * 编辑神评价
 */
@SuppressLint("InflateParams")
public class FragUserCommentEdit extends FragPullList<UserComment> {

	private static final String INK_ORIGINA_DATA = "INK_ORIGINA_DATA";
	private static final String INK_USER_FROM = "INK_USER_FROM";
	private static final String PAGE_NAME = "ProfileCommentManagment";
	private ArrayList<Long> topComments = new ArrayList<Long>();

	private User userFrom;

	public static void invoke(Context context, User userFrom,
			List<UserComment> originalData) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragUserCommentEdit.class;
		param.title = "编辑评价";
		param.enableBack = true;
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INK_USER_FROM, userFrom);
		intent.putExtra(INK_ORIGINA_DATA, (Serializable) originalData);
		context.startActivity(intent);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getPullProxy().setAdapter(
				new UserCommentAdapter(getActivity(),
						UserCommentCell.FROM_EDIT, null));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(EBUserComment eb) {
		switch (eb.getType()) {
			case EBUserComment.TYPE_ADD_TOP_COMMENT:
				addTopComment(eb.getUserComment());
				break;
			case EBUserComment.TYPE_CANCEL_TOP_COMMENT:
				deleteTopComment(eb.getUserComment());
				break;
			case EBUserComment.TYPE_DELETE_COMMENT:
				UserComment deleteItem = eb.getUserComment();
				if(deleteItem != null){
					getPullProxy().getAdapter().removeItem(deleteItem);
				}
				break;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getPullProxy().getPullView().setBackgroundColor(
				getResources().getColor(R.color.white));
		internalView.setBackgroundColor(getResources().getColor(R.color.white));
		internalView.setPadding(DensityUtil.dip2px(20), 0,
				DensityUtil.dip2px(20), 0);
		ColorDrawable drawable = new ColorDrawable(getResources().getColor(
				R.color.white));
		internalView.setDivider(drawable);
		internalView.setDividerHeight(DensityUtil.dip2px(15));
		internalView.setHeaderDividersEnabled(false);
		internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		internalView.setVerticalScrollBarEnabled(false);
		internalView.setFastScrollEnabled(false);

		addHeadrView();

		userFrom = (User) getActivity().getIntent().getSerializableExtra(
				INK_USER_FROM);
		List<UserComment> comments = (List<UserComment>) getActivity()
				.getIntent().getSerializableExtra(INK_ORIGINA_DATA);
		getPullProxy().onLoadSucessfully(comments);
		initTopComment(comments);

		EmptyView ev = new EmptyView(getActivity());
		ev.setImgRes(R.drawable.img_emptybox);
		ev.setPrompt("暂时没有评价");
		ev.setBtnVisibility(View.INVISIBLE);
		getPullProxy().getPullView().setEmptyView(ev);
	}

	/**
	 * 添加header view
	 */
	private void addHeadrView() {
		View headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.user_comment_edit_header, null);
		internalView.addHeaderView(headerView);
	}

	@Override
	public void loadNormal() {
		super.loadNormal();
		getUserCommentListTask(null);
	}

	@Override
	public void loadMore(String nextId) {
		super.loadMore(nextId);
		getUserCommentListTask(nextId);
	}

	private void getUserCommentListTask(String maxId) {
		ZHApis.getProfileApi().getUserCommentList(getActivity(), userFrom.uid, maxId,
				new TaskCallback<ZHPageData<UserComment>>() {

					@Override
					public void onSuccess(ZHPageData<UserComment> content) {
						getPullProxy().onLoadSucessfully(content);
						if (content != null) {
							initTopComment(content.data);
						}
					}

					@Override
					public void onFailure(Throwable error) {
						getPullProxy().onLoadFailed(error);
					}

				});
	}

	/**
	 * 初始化置顶评论list
	 */
	private void initTopComment(List<UserComment> data) {
		if (data == null) {
			return;
		}
		topComments.clear();
		if (data != null) {
			for (UserComment uc : data) {
				if (uc.topTime != null && uc.topTime > 0) {
					topComments.add(uc.id);
				}
			}
		}
	}

	/**
	 * 添加置顶评论，如果置顶评论总数大于最大数，则需要删除最早的那个，刷新ui
	 */
	private void addTopComment(UserComment uc) {
		int maxCount = PrefUtil.Instance().getCommentMaxTop();
		if (topComments.size() < maxCount) {
			topComments.add(0, uc.id);
		} else {
			// 删除最早的置顶评论
			long ucId = topComments.get(topComments.size() - 1);
			List<UserComment> data = getPullProxy().getAdapter().getData();
			for (UserComment comment : data) {
				if (ucId == comment.id) {
					comment.topTime = null;
				}
			}
			topComments.remove(topComments.size() - 1);
			topComments.add(0, uc.id);
			getPullProxy().getAdapter().notifyDataSetChanged();
		}
	}

	/**
	 * 删除本地置顶管理的神评论
	 */
	private void deleteTopComment(UserComment uc) {
		Iterator<Long> iterator = topComments.iterator();
		while (iterator.hasNext()) {
			long ucId = (long) iterator.next();
			if (ucId == uc.id) {
				iterator.remove();
				break;
			}
		}
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

}
