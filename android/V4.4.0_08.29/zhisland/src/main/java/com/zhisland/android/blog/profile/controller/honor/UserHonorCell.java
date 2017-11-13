package com.zhisland.android.blog.profile.controller.honor;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.Honor;

/**
 * 我的荣誉 cell
 */
public class UserHonorCell {

	private Context context;

	@InjectView(R.id.tvHonorTitle)
	TextView tvHonorTitle;

	@InjectView(R.id.ivHonorEdit)
	ImageView ivHonorEdit;

	Honor honor;

	public UserHonorCell(View view, Context context) {
		ButterKnife.inject(this, view);
		this.context = context;
	}

	public void fill(Honor honor, boolean showEdit) {
		this.honor = honor;
		if (honor != null) {
			tvHonorTitle.setText(honor.honorTitle == null ? ""
					: honor.honorTitle);
		}
		ivHonorEdit.setVisibility(showEdit ? View.VISIBLE : View.GONE);
	}

	@OnClick(R.id.ivHonorEdit)
	void onEditClick() {
		if (honor != null) {
			FragHonorEdit.invoke(context, honor);
		}
	}
}
