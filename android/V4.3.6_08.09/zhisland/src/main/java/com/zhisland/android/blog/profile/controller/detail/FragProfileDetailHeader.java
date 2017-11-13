package com.zhisland.android.blog.profile.controller.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragProfileDetailHeader extends FragBase implements
		AvatarUploader.OnUploaderCallback {

	private static final int REQ_IMAGE = 1;

	@InjectView(R.id.rlProfileHead)
	RelativeLayout rlHead;
	@InjectView(R.id.ivProfileHead)
	ImageView ivHead;
	@InjectView(R.id.tvProfileName)
	TextView tvName;
	@InjectView(R.id.tvProfileComAndPos)
	TextView tvComAndPos;
	@InjectView(R.id.tvProfileAdd)
	TextView tvProfileAdd;

	private User user;
	private boolean isSelf;// 是否查看自己

	private String uploadAvatarUrl;
	private AProgressDialog dialog;

	private Dialog actionDialog;

	public FragProfileDetailHeader() {
	}

	/**
	 * 更新user UI
	 * 
	 * @param user
	 */
	public void updateUser(User user) {
		this.user = user;
		isSelf = user.uid == PrefUtil.Instance().getUserId();
		if (rlHead != null) {
			setDataToView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_profile_header, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (user != null) {
			setDataToView();
		}
	}

	/**
	 * 头像 名字 公司 职位
	 */
	private void setDataToView() {
		showFigureView();
		tvName.setText(user.getNameWithTypeGoldFire(getActivity()));
		tvComAndPos.setText((user.userCompany == null ? ""
				: (user.userCompany + " "))
				+ (user.userPosition == null ? "" : user.userPosition));
	}

	/**
	 * 设置形象照
	 */
	private void showFigureView() {
		if (StringUtil.isNullOrEmpty(user.figure)) {
			if (isSelf) {
				ivHead.setBackgroundColor(getResources().getColor(
						R.color.bg_profile_empty));
				tvProfileAdd.setVisibility(View.VISIBLE);
			} else {
				ivHead.setBackgroundResource(R.drawable.img_campaign_default);
				tvProfileAdd.setVisibility(View.GONE);
			}
		} else {
			ImageWorkFactory.getFetcher().loadImage(user.figure, ivHead,
					ImageWorker.ImgSizeEnum.LARGE);
			tvProfileAdd.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.ivProfileHead)
	public void onClickProfileHead() {
		if (!isSelf) {
			return;
		}
		if (actionDialog != null && actionDialog.isShowing()) {
			return;
		}
		ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
		actions.add(new ActionItem(1, R.color.color_dc, "修改形象照片"));

		actionDialog = DialogUtil.createActionSheet(getActivity(), "", "取消",
				actions, new ActionDialog.OnActionClick() {

					@Override
					public void onClick(DialogInterface df, int id,
										ActionItem item) {
						if (actionDialog != null && actionDialog.isShowing()) {
							actionDialog.dismiss();
						}
						switch (id) {
							case 1:
								MultiImgPickerActivity.invoke(getActivity(),
										ivHead.getWidth(), ivHead.getHeight(),
										REQ_IMAGE);
								break;
						}
					}
				});

		actionDialog.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQ_IMAGE:
				List<String> paths = (List<String>) data
						.getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
				String localUrl = paths.get(0);
				ImageWorkFactory.getFetcher().loadImage(localUrl, ivHead);
				tvProfileAdd.setVisibility(View.GONE);
				AvatarUploader.instance().uploadAvatar(localUrl, this);
				showProgress();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showProgress() {
		if (dialog == null) {
			dialog = new AProgressDialog(getActivity());
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					AvatarUploader.instance().loadFinish();
				}
			});
		}
		if (!dialog.isShowing()) {
			dialog.show();
			dialog.setText("正在上传您的图片...");
		}
	}

	@Override
	public void callBack(String backUrl) {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
		uploadAvatarUrl = "";
		if (StringUtil.isNullOrEmpty(backUrl)) {
			ToastUtil.showShort("上传图片失败");
			showFigureView();
		} else {
			uploadAvatarUrl = backUrl;
			uploadFigure();
		}
	}

	/**
	 * 修改形象照
	 */
	private void uploadFigure() {
		User tmpUser = new User();
		tmpUser.uid = PrefUtil.Instance().getUserId();

		if (!StringUtil.isNullOrEmpty(uploadAvatarUrl)) {
			tmpUser.figure = uploadAvatarUrl;
			ZHApis.getUserApi().updateUser(getActivity(), tmpUser,
					TaskUpdateUser.TYPE_UPDATE_OTHER,
					new TaskCallback<Object>() {

						@Override
						public void onSuccess(Object content) {
							user.figure = uploadAvatarUrl;
							DBMgr.getMgr().getUserDao()
									.creatOrUpdateUserNotNull(user);
							ToastUtil.showShort("修改形象照成功");
						}

						@Override
						public void onFailure(Throwable error) {
						}
					});
		}
	}

	@Override
	protected String getPageName() {
		return null;
	}
}
