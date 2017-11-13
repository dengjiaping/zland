package com.zhisland.android.blog.profile.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

public class FragVipService extends FragBase {

	@InjectView(R.id.term)
	TextView term;
	@InjectView(R.id.internationalPhoneView)
	InternationalPhoneView internationalPhoneView;
	@InjectView(R.id.title)
	TextView title;
	@InjectView(R.id.profileImage)
	ImageView profileImage;
	@InjectView(R.id.next)
	Button next;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.register_vip, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		internationalPhoneView.setEditHint("若有不便请填写您助理的电话");
		title.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (StringUtil.isNullOrEmpty(user.assistantMobile)) {
			internationalPhoneView.setDictMobile(user.assistantMobile);
		}

		String text = "感谢您使用美图服务，我们将在三个工作日之内与您注册时填写的手机号码联系。如您有任何疑问，请与我们联系 ";
		String phone = getResources().getString(R.string.app_service_phone);
		int start = text.length();
		int end = phone.length();
		SpannableStringBuilder style = new SpannableStringBuilder(text + phone);
		TextViewURLSpan myURLSpan = new TextViewURLSpan();
		style.setSpan(myURLSpan, start, start + end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		term.setText(style);
		term.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private class TextViewURLSpan extends ClickableSpan {
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.color_dc_p));
			ds.setUnderlineText(false); // 去掉下划线
		}

		@Override
		public void onClick(View widget) {// 点击事件
			IntentUtil.dialTo(getActivity(), getResources().getString(R.string.app_service_phone));
		}
	}

	@Override
	protected String getPageName() {
		return "ProfileVipFigure";
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	public void saveClick() {
		User user = new User();
		user.uid = PrefUtil.Instance().getUserId();
		user.vipAvatarService = 1;

		if (!mobileValidated(user))
			return;

		ZHApis.getUserApi().updateUser(null, user, TaskUpdateUser.TYPE_UPDATE_OTHER,
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						ToastUtil.showShort("信息提交成功。");
					}

					@Override
					public void onFailure(Throwable error) {
					}
				});
		getActivity().finish();
	}

	private boolean mobileValidated(User user) {
		String phone = internationalPhoneView.getMobileNum();
		if (StringUtil.isNullOrEmpty(phone)) {
			return true;
		}
		if (!internationalPhoneView.checkInput()) {
			ToastUtil.showShort("您输入的手机号不正确");
			return false;
		}
		user.assistantMobile = internationalPhoneView.getDictMobile();
		return true;
	}
}
