package com.zhisland.android.blog.setting.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.CountEditText;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragFeedBack extends FragBase {

	private static final int TAG_ID_RIGHT = 100;

	@InjectView(R.id.etFeedBack)
	public CountEditText etFeedBack;

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			final FragFeedBack fragFeedBack = (FragFeedBack) fragment;
			String text = fragFeedBack.etFeedBack.getText();
			if (StringUtil.isNullOrEmpty(text)) {
				ToastUtil.showShort("意见反馈不能为空");
				return;
			}
			fragFeedBack.showProgressDlg();
			ZHApis.getAAApi().feedBack(context, text, new TaskCallback<Object>() {

				@Override
				public void onSuccess(Object content) {
					ToastUtil.showShort("感谢您的反馈，我们会尽快处理");
                    if (fragFeedBack.getActivity() != null) {
                        fragFeedBack.getActivity().finish();
                    }
				}

				@Override
				public void onFailure(Throwable error) {
				}

				public void onFinish() {
					fragFeedBack.hideProgressDlg();
				}
			});
		}
	};

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragFeedBack.class;
		param.title = "意见反馈";
		param.enableBack = true;
		param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
		param.runnable = titleRunnable;
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT,TitleBtn.TYPE_TXT);
		tb.btnText = "提交";
		tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_feed_back, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		EditText editText = etFeedBack.getEditText();
		editText.setHint("请写下您的意见或遇到的问题");
		editText.setGravity(Gravity.TOP);
		editText.requestFocus();
		etFeedBack.setMaxCount(300);
	}

	public String getPageName() {
		return "SettingFeedback";
	}
}
