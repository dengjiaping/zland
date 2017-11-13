package com.zhisland.android.blog.profile.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.lib.component.act.TitleType;

public class ActProfileIntroduction extends FragBaseActivity {

	public static final int TITLE_RIGHT_BUTTON_TAG = 606;
	FragProfileIntroduction frag;
	private TextView rightSaveBtn;

	public static void invoke(Activity context) {
		Intent intent = new Intent(context, ActProfileIntroduction.class);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(true);
		getTitleBar().addBackButton();
		getTitleBar().setTitle("个人简介");
		rightSaveBtn = TitleCreator.Instance().createTextButton(this, "保存",
				R.color.color_dc);
		getTitleBar().addRightTitleButton(rightSaveBtn, TITLE_RIGHT_BUTTON_TAG);
		frag = new FragProfileIntroduction();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, frag);
		ft.commit();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}

	public void onTitleClicked(View view, int tagId) {
		super.onTitleClicked(view, tagId);
		switch (tagId) {
		case TITLE_RIGHT_BUTTON_TAG:
			frag.saveIntroduction();
			break;
		}
	};

	public void setSaveEnable(boolean flag) {
		if (rightSaveBtn != null) {
			rightSaveBtn.setEnabled(flag);
			rightSaveBtn.setTextColor(flag ? getResources().getColor(
					R.color.color_dc) : getResources().getColor(
					R.color.color_f3));
		}
	}
}
