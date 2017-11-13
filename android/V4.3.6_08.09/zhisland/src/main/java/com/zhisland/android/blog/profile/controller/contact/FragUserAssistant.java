package com.zhisland.android.blog.profile.controller.contact;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserAssistant;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 我的助理页面
 */
public class FragUserAssistant extends FragBase {

	private static final int TAG_ID_RIGHT = 101;

	public static final String INTENT_KEY_ASSISTANT_BLOCK = "intent_key_assistant_block";

	public static void invoke(Context context,
			SimpleBlock<UserAssistant> assistantBlock) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragUserAssistant.class;
		param.title = "我的助理";
		param.enableBack = true;
		param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
		param.runnable = titleRunnable;
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT, TitleBtn.TYPE_TXT);
		tb.btnText = "保存";
		tb.textColor = ZhislandApplication.APP_RESOURCE
				.getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_ASSISTANT_BLOCK, assistantBlock);
		context.startActivity(intent);
	}

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragUserAssistant) {
					((FragUserAssistant) fragment).save();
				}
			}
		}
	};

	@Override
	protected String getPageName() {
		return "ProfileMyAssistant";
	}

	@InjectView(R.id.internationalPhoneView)
	InternationalPhoneView internationalPhoneView;

	@InjectView(R.id.etName)
	EditText etName;

	@InjectView(R.id.tvWhoCanSee)
	TextView tvWhoCanSee;

	private AProgressDialog progressDialog;
	private Dialog dlgWhoCanSee;

	private UserAssistant userAssistant;

	ActionItem actionItemAll = new ActionItem(
			UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_ALL, R.color.color_dc,
			UserAssistant.NAME_CONTACT_VISIBLE_RANGE_ALL);
	ActionItem actionItemVip = new ActionItem(
			UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_VIP, R.color.color_dc,
			UserAssistant.NAME_CONTACT_VISIBLE_RANGE_VIP);
	ActionItem actionItemFriend = new ActionItem(
			UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_FRIEND, R.color.color_dc,
			UserAssistant.NAME_CONTACT_VISIBLE_RANGE_FRIEND);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_user_assistant, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		init();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setDataToView();
	}

	private void init() {
		SimpleBlock<UserAssistant> block = (SimpleBlock<UserAssistant>) getActivity()
				.getIntent().getSerializableExtra(INTENT_KEY_ASSISTANT_BLOCK);
		if (block != null && block.data != null && block.data.size() > 0) {
			userAssistant = block.data.get(0);
		}
		if (userAssistant == null) {
			userAssistant = new UserAssistant();
		}

		// 可见范围如果为空，默认为岛邻可见
		if (userAssistant.visibleRange == null) {
			userAssistant.visibleRange = UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_VIP;
		}
		// 如果user为空，create，省的下面空判断。
		if (userAssistant.assistant == null) {
			userAssistant.assistant = new User();
		}
		EditTextUtil.limitEditTextLengthChinese(etName, 20, null);
		final EditText etPhone = internationalPhoneView.getEditText();
		etPhone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etPhone.setSelection(etPhone.getText().length());
				}
			}
		});
	}

	@OnFocusChange(R.id.etName)
	void onNameFocusChange() {
		if (etName.isFocused()) {
			etName.setSelection(etName.getText().length());
		}
	}

	private void setDataToView() {
		etName.setText(userAssistant.assistant.name == null ? ""
				: userAssistant.assistant.name);
		internationalPhoneView
				.setCountryByCode(userAssistant.assistant.countryCodeShow);
		if (!StringUtil.isNullOrEmpty(userAssistant.assistant.userMobile)) {
			internationalPhoneView
					.setMobileNum(userAssistant.assistant.userMobile);
		}
		setWhoCanSeeView();
	}

	public void save() {
		String name = etName.getText().toString();
		if (StringUtil.isNullOrEmpty(name.trim())) {
			ToastUtil.showShort("请输入助理姓名");
			return;
		} else {
			userAssistant.assistant.name = name;
		}
		if (!internationalPhoneView.checkInput()) {
			ToastUtil.showShort("请输入正确的手机号码");
			return;
		} else {
			userAssistant.assistant.userMobile = internationalPhoneView
					.getMobileNum();
			userAssistant.assistant.countryCodeShow = internationalPhoneView
					.getCurrentDict().code;
		}
		showProgress("请求中...");
		ZHApis.getProfileApi().updateAssistant(getActivity(), userAssistant,
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						if (isAdded() || !isDetached()) {
							ToastUtil.showShort("信息提交成功。");
							getActivity().finish();
						}
					}

					@Override
					public void onFailure(Throwable error) {
					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (isAdded() || !isDetached()) {
							if (progressDialog != null) {
								progressDialog.dismiss();
							}
						}
					}
				});
	}

	@OnClick(R.id.tvWhoCanSee)
	void onWhoCanSeeClick() {
		showWhoCanSeeDialog();
	}

	private void setWhoCanSeeView() {
		switch (userAssistant.visibleRange) {
		case UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_ALL:
			tvWhoCanSee.setText(actionItemAll.name);
			break;
		case UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_VIP:
			tvWhoCanSee.setText(actionItemVip.name);
			break;
		case UserAssistant.VALUE_CONTACT_VISIBLE_RANGE_FRIEND:
			tvWhoCanSee.setText(actionItemFriend.name);
			break;
		}
	}

	private void showProgress(String content) {
		if (progressDialog == null) {
			progressDialog = new AProgressDialog(getActivity());
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							AvatarUploader.instance().loadFinish();
						}
					});
		}
		if (!progressDialog.isShowing()) {
			progressDialog.show();
			progressDialog.setText(content);
		}
	}

	private void showWhoCanSeeDialog() {
		if (dlgWhoCanSee != null && dlgWhoCanSee.isShowing()) {
			return;
		}
		ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
		actions.add(actionItemAll);
		actions.add(actionItemVip);
		actions.add(actionItemFriend);
		dlgWhoCanSee = DialogUtil.createActionSheet(getActivity(), "", "取消",
				actions, new OnActionClick() {

					@Override
					public void onClick(DialogInterface dialog, int id,
							ActionItem item) {
						userAssistant.visibleRange = id;
						setWhoCanSeeView();
						dlgWhoCanSee.dismiss();
					}
				});
		dlgWhoCanSee.show();
	}
}