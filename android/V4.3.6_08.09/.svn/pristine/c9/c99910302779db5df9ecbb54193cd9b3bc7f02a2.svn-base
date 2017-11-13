package com.zhisland.android.blog.profile.controller.drip;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhisland.android.blog.common.view.CityPickDlg;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.EditTextUtil.IKeyBoardAction;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 编辑点滴cell
 */
public class UserDripEditCell extends UserDripCellBase implements
		OnClickListener {

	public static final int REQ_INDUSTRY = 10086;

	public UserDripEditCell(View view, Activity context) {
		super(view, context);
	}

	@Override
	protected void initChildView() {
		etDripContent.setVisibility(View.VISIBLE);
		EditTextUtil.setKeyBoard(etDripContent, EditTextUtil.ID_NEXT,
				new IKeyBoardAction() {

					@Override
					public void action() {
					}
				});
		tvDripContent.setOnClickListener(this);
		tvDripContent.setVisibility(View.GONE);
		etDripContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (dict != null) {
					dict.value = s.toString().trim();
				}
			}
		});
	}

	@Override
	protected void fillData() {
		etDripContent.setText(dict.value);
		etDripContent.setHint(dict.hint);
		etDripContent.setEnabled(dict.isCanEdit());
		tvDripRight
				.setVisibility(dict.isMust() ? View.VISIBLE : View.INVISIBLE);
		dripViewLine.setVisibility(dict.isCanEdit() ? View.VISIBLE
				: View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		SoftInputUtil.hideInput(context);
		if (StringUtil.isEquals(dict.alias, CustomDict.ALIAS_CITY)) {
			if (cityPickDlg == null) {
				cityPickDlg = new CityPickDlg(context,cityCallBack,"请选择您的" + dict.name);
			}
			cityPickDlg.setCity(dict.getProvinceId(), dict.getCityId());
			cityPickDlg.show();
		} else if (StringUtil.isEquals(dict.alias, CustomDict.ALIAS_INDUSTRY)) {
			SelectProfileIndustry.launch(context, null, REQ_INDUSTRY);
		}
	}

}