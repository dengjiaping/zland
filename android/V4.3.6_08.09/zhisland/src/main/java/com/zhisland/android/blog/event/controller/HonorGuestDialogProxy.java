package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.lib.util.StringUtil;

/**
 * 活动嘉宾详情对话框
 * */
public class HonorGuestDialogProxy {

	public static void HonorGuestDialog(final Activity context, User guest) {

		final Dialog dialog = new Dialog(context, R.style.DialogGuest);
		dialog.setContentView(R.layout.dlg_creat_demand);
		Holder holder = new Holder(dialog);
		holder.fill(guest);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.width = LayoutParams.MATCH_PARENT;
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	static class Holder {
		Dialog dialog;
		User guest;

		@InjectView(R.id.ivGuestAvatar)
		public AvatarView guestAvatarView;

		@InjectView(R.id.tvName)
		public TextView tvName;

		@InjectView(R.id.tvCompAndPosi)
		public TextView tvCompAndPosi;

		@InjectView(R.id.tvDesc)
		public TextView tvDesc;

		@InjectView(R.id.tvLookTo)
		public TextView tvLookTo;

		public Holder(Dialog dialog) {
			this.dialog = dialog;
			ButterKnife.inject(this, dialog);
		}

		public void fill(User guest) {
			if (guest == null) {
				return;
			}
			this.guest = guest;
			guestAvatarView.fill(guest, true);
			tvName.setText(guest.name);
			String blank = " ";
			if (StringUtil.isNullOrEmpty(guest.userCompany)
					|| StringUtil.isNullOrEmpty(guest.userPosition)) {
				blank = "";
			}
			String compAndPosi = guest.userCompany == null ? ""
					: guest.userCompany;
			compAndPosi += blank
					+ (guest.userPosition == null ? "" : guest.userPosition);
			tvCompAndPosi.setText(compAndPosi);
			tvDesc.setText(guest.profile == null ? "" : guest.profile);
			if (guest.uid <= 0) {
				tvLookTo.setVisibility(View.GONE);
			} else {
				tvLookTo.setVisibility(View.VISIBLE);
			}
		}

		@OnClick(R.id.tvLookTo)
		void LookToClicl() {
			ActProfileDetail.invoke(dialog.getContext(), guest.uid);
		}

		@OnClick(R.id.llRoot)
		void rootClicl() {
			dialog.dismiss();
		}

		@OnClick(R.id.tvDesc)
		void descClicl() {
			dialog.dismiss();
		}
	}

}
