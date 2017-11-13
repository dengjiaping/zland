package com.zhisland.android.blog.profile.controller.drip;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.lib.component.act.TitleType;

/**
 * 我的点滴
 */
public class ActUserDripList extends FragBaseActivity {

	public static final String INK_DRIP_DATAS = "INK_DRIP_DATAS";
	public static final int TAG_RIGHT_BTN = 122;

	private FragUserDripList frag;

	public static void invoke(Context context, List<CustomDict> datas) {
		Intent intent = new Intent(context, ActUserDripList.class);
		intent.putExtra(INK_DRIP_DATAS, (Serializable) datas);
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		getTitleBar().addBackButton();
		getTitleBar().setTitle("我的点滴");

		TextView rightBtn = TitleCreator.Instance().createTextButton(this,
				"保存", R.color.color_dc);
		getTitleBar().addRightTitleButton(rightBtn, TAG_RIGHT_BTN);

		frag = new FragUserDripList();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, frag);
		ft.commit();
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		super.onTitleClicked(view, tagId);
		if (tagId == TAG_RIGHT_BTN) {
			// 保存我的点滴
			frag.saveDrip();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (frag != null) {
			frag.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}
}
