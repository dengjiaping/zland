package com.zhisland.android.blog.setting.controller;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragModifyPwd extends FragBase {

	@InjectView(R.id.etNewPwd)
	public EditText etNewPwd;

	@InjectView(R.id.ivNewPwdEye)
	public ImageView ivNewPwdEye;

	private boolean isShowNewPwdEye;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_modify_pwd, container, false);
		ButterKnife.inject(this, root);
		return root;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

	public String getNewPwd() {
		String newPwd = etNewPwd.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(newPwd)) {
			return null;
		}
		boolean validPwd = false;
		if (newPwd.length() >= 6 && newPwd.length() <= 16) {
			validPwd = true;
		}
		if (validPwd) {
			return newPwd;
		} else {
			ToastUtil.showShort("密码格式错误");
			return null;
		}
	}

	@OnClick(R.id.ivNewPwdEye)
	public void onPwdEye() {
		if (isShowNewPwdEye) {
			ivNewPwdEye.setSelected(false);
			etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			etNewPwd.setSelection(etNewPwd.getText().length());
		} else {
			ivNewPwdEye.setSelected(true);
			etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			etNewPwd.setSelection(etNewPwd.getText().length());
		}
		isShowNewPwdEye = !isShowNewPwdEye;
	}

	@Override
	protected String getPageName() {
		return "SettingPasswordModify";
	}
}
