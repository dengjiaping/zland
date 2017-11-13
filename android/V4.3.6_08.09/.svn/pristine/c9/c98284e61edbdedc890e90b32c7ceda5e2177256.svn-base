package com.zhisland.android.blog.profile.controller.comment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.profile.api.TaskTopUserComment;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profile.eb.EBUserComment;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 个人页神评论 cell
 */
public class UserCommentCell {

	/*
	 * 列表的cell
	 */
	public static final int FROM_LIST = 0;
	/*
	 * 编辑的cell
	 */
	public static final int FROM_EDIT = 1;
	/*
	 * 个人页的cell
	 */
	public static final int FROM_PROFILE = 2;

	/**
	 * 1：显示， 2：隐藏
	 */
	private static final int VISIABLE_SHOW = 1;
	private static final int VISIABLE_HIDE = 2;

	@InjectView(R.id.rlUcRoot)
	RelativeLayout rlUcRoot;

	@InjectView(R.id.tvUcContent)
	TextView tvUcContent;

	@InjectView(R.id.tvUcInfo)
	TextView tvUcInfo;

	@InjectView(R.id.tvUcCreateTime)
	TextView tvUcCreateTime;

	@InjectView(R.id.tvUcSign)
	TextView tvUcSign;

	private UserComment comment;
	private Context context;
	private int from;
	private Boolean isUserSelf;

	// 置顶 隐藏 dialog
	private Dialog dialog;

	private AProgressDialog progressDialog;

	public UserCommentCell(View view, final Context context, Boolean isUserSelf) {
		this.context = context;
		this.isUserSelf = isUserSelf;
		ButterKnife.inject(this, view);
	}

	public void fill(UserComment comment, int from) {
		if (comment == null) {
			return;
		}
		this.comment = comment;
		this.from = from;
		tvUcContent.setText(comment.content);
		StringBuffer sbUcInfo = new StringBuffer();
		User publisher = comment.publisher;
		if (publisher != null) {
			if (!StringUtil.isNullOrEmpty(publisher.name)) {
				sbUcInfo.append("- ").append(publisher.name);
			}
			if (!StringUtil.isNullOrEmpty(publisher.userCompany)) {
				sbUcInfo.append(" ").append(publisher.userCompany);
			}
			if (!StringUtil.isNullOrEmpty(publisher.userPosition)) {
				sbUcInfo.append(" ").append(publisher.userPosition);
			}
		}
		String str = sbUcInfo.toString();
        tvUcInfo.setText(str);
		tvUcCreateTime.setText(TimeUtil.getTimeStr(comment.createTime));

		refreshItemUIState();
	}

	@OnClick(R.id.rlUcRoot)
	public void onClickRootView() {
		switch (from) {
		case FROM_PROFILE:
		case FROM_LIST:
			// 跳转评论人个人页
			User publisher = comment.publisher;
			if (publisher != null) {
                ActProfileDetail.invokeNoHistory(context, publisher.uid);
			}
			break;
		case FROM_EDIT:
			// 置顶 隐藏 dialog
			showActionDialog();
			break;
		}
	}

	/**
	 * 编辑评论点击弹出的action sheet
	 */
	private void showActionDialog() {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		ArrayList<ActionItem> actions = new ArrayList<>();
		actions.add(new ActionItem(1, R.color.color_dc, isTop() ? "取消优先显示"
				: "优先显示"));
		actions.add(new ActionItem(2, R.color.color_ac, "删除"));

		dialog = DialogUtil.createActionSheet(context, "", "取消", actions,
				new OnActionClick() {

					@Override
					public void onClick(DialogInterface df, int id,
							ActionItem item) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						switch (id) {
						case 1:
							if (isTop()) {
								// 取消优先显示
								comment.topTime = null;
								EventBus.getDefault()
										.post(new EBUserComment(
												EBUserComment.TYPE_CANCEL_TOP_COMMENT,
												comment));
							} else {
								// 优先显示
								comment.topTime = System.currentTimeMillis();
								EventBus.getDefault()
										.post(new EBUserComment(
												EBUserComment.TYPE_ADD_TOP_COMMENT,
												comment));
							}
							int topState = comment.topTime == null ? TaskTopUserComment.CANCEL_TOP_COMMENT
									: TaskTopUserComment.TOP_COMMENT;
							ZHApis.getProfileApi().topUserComment(null, comment.id,
									topState, new TaskCallback<Object>() {

										@Override
										public void onSuccess(Object content) {
											EventBus.getDefault()
													.post(new EBProfile(
															EBProfile.TYPE_CHANGE_COMMENT));
										}

										@Override
										public void onFailure(Throwable error) {

										}

									});
							EventBus.getDefault()
									.post(new EBProfile(
											EBProfile.TYPE_CHANGE_COMMENT));
							refreshItemUIState();
							break;
						case 2:
							showProgressDialog("删除中...");
							ZHApis.getProfileApi().deleteComment(context, comment.id, new TaskCallback<Object>() {
								@Override
								public void onSuccess(Object content) {
									EventBus.getDefault()
											.post(new EBUserComment(
													EBUserComment.TYPE_DELETE_COMMENT,
													comment));
								}

								@Override
								public void onFailure(Throwable error) {

								}

								@Override
								public void onFinish() {
									super.onFinish();
									if (progressDialog != null) {
										progressDialog.dismiss();
									}
								}
							});
							break;
						}
					}
				});

		dialog.show();
	}

	/**
	 * 是否是置顶评论
	 */
	private boolean isTop() {
		if (comment.topTime != null && comment.topTime > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 刷新item UI状态，包括背景 右下角ui, 看别人个人页时，不显示右下角
	 */
	private void refreshItemUIState() {
		switch (from) {
		case FROM_LIST:
			if (isUserSelf != null && !isUserSelf) {
				break;
			}
			if (comment.isNew != null) {
				showSignNew();
			} else if (isTop()) { // 置顶
				showSignTop();
			} else {
				tvUcSign.setVisibility(View.GONE);
			}
			break;
		case FROM_EDIT:
			if (isTop()) { // 置顶
				showSignTop();
			} else {
				tvUcSign.setVisibility(View.GONE);
			}
			break;
		case FROM_PROFILE:
			rlUcRoot.setBackgroundResource(R.drawable.profile_img_bubble);
			tvUcSign.setVisibility(View.GONE);
			break;
		}

	}

	/**
	 * 显示最新
	 */
	private void showSignNew() {
		tvUcSign.setText("NEW!");
		tvUcSign.setVisibility(View.VISIBLE);
		tvUcSign.setTextColor(context.getResources().getColor(
				R.color.color_user_comment_new));
	}

	/**
	 * 设置优先显示
	 */
	private void showSignTop() {
		tvUcSign.setText("优先显示");
		tvUcSign.setVisibility(View.VISIBLE);
		tvUcSign.setTextColor(context.getResources().getColor(R.color.color_dc));
	}

	/**
	 * 设置隐藏
	 */
	private void showSignHide() {
		tvUcSign.setText("隐藏");
		tvUcSign.setVisibility(View.VISIBLE);
		tvUcSign.setTextColor(context.getResources().getColor(R.color.color_f3));
	}

	private void showProgressDialog(String content) {
		if (progressDialog == null) {
			progressDialog = new AProgressDialog(context);
		}
		progressDialog.show();
		progressDialog.setText(content);
	}


}
