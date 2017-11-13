package com.zhisland.android.blog.profile.controller.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserContactInfo;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.AvatarUploader.OnUploaderCallback;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.EditTextUtil.IKeyBoardAction;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 基本信息页面
 */
public class FragUserContactInfo extends FragBase {

	private static final int REQ_IMAGE = 115;
	private static final int TAG_ID_RIGHT = 101;

	public static final String INTENT_KEY_CONTACT_INFO_BLOCK = "intent_key_contact_info_block";

	public static void invoke(Context context,
			SimpleBlock<UserContactInfo> contactInfoBlock) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragUserContactInfo.class;
		param.title = contactInfoBlock.title;
		param.enableBack = true;
		param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
		param.runnable = titleRunnable;
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT, TitleBtn.TYPE_TXT);
		tb.btnText = "保存";
		tb.textColor = ZhislandApplication.APP_RESOURCE
				.getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_CONTACT_INFO_BLOCK, contactInfoBlock);
		context.startActivity(intent);
	}

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragUserContactInfo) {
					((FragUserContactInfo) fragment).save();
				}
			}
		}
	};

	@Override
	protected String getPageName() {
		return "ProfileMyContactWay";
	}

	@InjectView(R.id.ivAvatar)
	ImageView ivAvatar;

	@InjectView(R.id.internationalPhoneView)
	InternationalPhoneView internationalPhoneView;

	@InjectView(R.id.etMail)
	EditText etMail;

	@InjectView(R.id.tvWhoCanSee)
	TextView tvWhoCanSee;

	@InjectView(R.id.rbSexNv)
	RadioButton rbSexNv;

	@InjectView(R.id.rbSexNan)
	RadioButton rbSexNan;

	@InjectView(R.id.etName)
	EditText etName;

	private AProgressDialog progressDialog;
	private Dialog dlgWhoCanSee;

	ActionItem actionItemAll = new ActionItem(
			UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL, R.color.color_dc,
			UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_ALL);
	ActionItem actionItemVip = new ActionItem(
			UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_VIP, R.color.color_dc,
			UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_VIP);
	ActionItem actionItemFriend = new ActionItem(
			UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND,
			R.color.color_dc, UserContactInfo.NAME_CONTACT_VISIBLE_RANGE_FRIEND);

	private UserContactInfo userContact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_user_contact_info, null);
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
		User self = DBMgr.getMgr().getUserDao().getSelfUser();
		SimpleBlock<UserContactInfo> contactInfoBlock = (SimpleBlock<UserContactInfo>) getActivity()
				.getIntent()
				.getSerializableExtra(INTENT_KEY_CONTACT_INFO_BLOCK);
		if (contactInfoBlock != null && contactInfoBlock.data != null
				&& contactInfoBlock.data.size() > 0) {
			userContact = contactInfoBlock.data.get(0);
		}
		// 如果userContact为空，new一个，并且将user中的信息赋值过去。
		if (userContact == null) {
			userContact = new UserContactInfo();
			userContact.countryCode = self.countryCodeShow;
			userContact.email = self.email;
			userContact.mobile = self.userMobile;
			userContact.userAvatar = self.userAvatar;
			userContact.userName = self.name;
		}
		// 如果联系方式可见范围为空，默认设置为岛邻可见
		if (userContact.visibleRange == null) {
			userContact.visibleRange = UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_VIP;
		}
		// 屏蔽邮箱输入换行
		EditTextUtil.setKeyBoard(etMail, EditTextUtil.ID_DONE, nothingAction);
		EditTextUtil.limitEditTextLengthChinese(etName, 20, null);
	}

	IKeyBoardAction nothingAction = new IKeyBoardAction() {

		@Override
		public void action() {

		}
	};

	@OnFocusChange(R.id.etMail)
	void onMailFocusChange() {
		if (etMail.isFocused()) {
			etMail.setSelection(etMail.getText().length());
		}
	}

	@OnClick(R.id.rbSexNan)
	void sexNanClick() {
		rbSexNan.setChecked(true);
		rbSexNv.setChecked(false);
	}

	@OnClick(R.id.rbSexNv)
	void sexNvClick() {
		rbSexNan.setChecked(false);
		rbSexNv.setChecked(true);
	}

	private void setDataToView() {
		ImageWorkFactory.getCircleFetcher().loadImage(userContact.userAvatar,
				ivAvatar, R.drawable.avatar_default);
		if (!StringUtil.isNullOrEmpty(userContact.userName)) {
			etName.setText(userContact.userName);
			if (userContact.userType == null
					|| (userContact.userType == User.VALUE_TYPE_VIP || userContact.userType == User.VALUE_TYPE_HAIKE)) {
				etName.setEnabled(false);
			}
		}
		if (userContact.sex == null) {
			userContact.sex = User.VALUE_SEX_MAN;
		}
		if (userContact.sex == User.VALUE_SEX_WOMAN) {
			rbSexNv.setChecked(true);
			rbSexNan.setChecked(false);
		} else {
			rbSexNan.setChecked(true);
			rbSexNv.setChecked(false);
		}

		etMail.setText(userContact.email == null ? "" : userContact.email);
		internationalPhoneView.setCountryByCode(userContact.countryCode);
		if (!StringUtil.isNullOrEmpty(userContact.mobile)) {
			internationalPhoneView.setMobileNum(userContact.mobile);
		}
		setWhoCanSeeView();
	}

	public void save() {
		String email = etMail.getText().toString().trim();
		String name = etName.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(name)) {
			ToastUtil.showShort("请输入您的姓名");
			return;
		}
		if (!StringUtil.isNullOrEmpty(email) && !StringUtil.isEmail(email)) {
			ToastUtil.showShort("您输入的邮箱格式不合法");
			return;
		}
		if (!internationalPhoneView.checkInput()) {
			ToastUtil.showShort("请输入正确的手机号码");
			return;
		}
		userContact.userName = name;
		userContact.sex = rbSexNan.isChecked() ? User.VALUE_SEX_MAN
				: User.VALUE_SEX_WOMAN;
		userContact.countryCode = internationalPhoneView.getCurrentDict().code;
		userContact.email = email;
		userContact.mobile = internationalPhoneView.getMobileNum();

		showProgress("请求中...");
		ZHApis.getProfileApi().updateContactInfo(getActivity(), userContact,
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

	@OnClick(R.id.ivAvatar)
	void onAvatarClick() {
		MultiImgPickerActivity.invoke(getActivity(), 1, 1, REQ_IMAGE);
	}

	@OnClick(R.id.tvWhoCanSee)
	void onWhoCanSeeClick() {
		showWhoCanSeeDialog();
	}

	private void setWhoCanSeeView() {
		switch (userContact.visibleRange) {
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_ALL:
			tvWhoCanSee.setText(actionItemAll.name);
			break;
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_VIP:
			tvWhoCanSee.setText(actionItemVip.name);
			break;
		case UserContactInfo.VALUE_CONTACT_VISIBLE_RANGE_FRIEND:
			tvWhoCanSee.setText(actionItemFriend.name);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			switch (requestCode) {
			case REQ_IMAGE:
				List<String> paths = (List<String>) data
						.getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
				String localUrl = paths.get(0);
				ImageWorkFactory.getCircleFetcher().loadImage(localUrl,
						ivAvatar, R.drawable.avatar_default);
				showProgress("正在上传您的头像...");
				AvatarUploader.instance().uploadAvatar(localUrl,
						onUploaderCallback);
				break;
			}

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

	OnUploaderCallback onUploaderCallback = new OnUploaderCallback() {

		@Override
		public void callBack(String backUrl) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.cancel();
			}
			if (StringUtil.isNullOrEmpty(backUrl)) {
				ToastUtil.showShort("上传头像失败");
				ImageWorkFactory.getCircleFetcher().loadImage(
						userContact.userAvatar, ivAvatar,
						R.drawable.avatar_default);
			} else {
				userContact.userAvatar = backUrl;
			}
		}
	};

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
						userContact.visibleRange = id;
						setWhoCanSeeView();
						dlgWhoCanSee.dismiss();
					}
				});
		dlgWhoCanSee.show();
	}
}