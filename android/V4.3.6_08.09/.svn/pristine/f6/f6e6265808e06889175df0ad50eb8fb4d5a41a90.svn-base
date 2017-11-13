package com.zhisland.android.blog.setting.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.setting.eb.EBSetting;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;

public class FragSetVisiable extends FragBase {

	@InjectView(R.id.tvPrompt)
	TextView tvPrompt;
	
	@InjectView(R.id.ivLoc)
	ImageView ivLoc;
	
	private CommonDialog commonDialog;
	
	private AProgressDialog progressDialog;

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragSetVisiable.class;
		param.enableBack = true;
		param.title = "隐私设置";
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	protected String getPageName() {
		return "SettingVisible";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_set_visiable, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initView();
		return root;
	}

	private void initView() {
		progressDialog = new AProgressDialog(getActivity());
		SpannableString ss = new SpannableString("  开启您的位置信息，可在人脉中查看您附近的人。");
		Drawable d = getResources().getDrawable(
				R.drawable.img_info_notification);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
		ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		tvPrompt.setText(ss);
		
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if(user == null){
			getActivity().finish();
		}else{
			if(user.invisible != null && user.invisible == User.VALUE_INVISIBLE){
				ivLoc.setBackgroundResource(R.drawable.sel_switch);
			}else{
				ivLoc.setBackgroundResource(R.drawable.switch_on);
			}
		}
	}
	
	@OnClick(R.id.llLoc)
	void locInfoClick(){
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if(user == null){
			getActivity().finish();
		}else{
			if(user.invisible != null && user.invisible == User.VALUE_INVISIBLE){
				ivLoc.setBackgroundResource(R.drawable.switch_on);
				setInvisiblePosition(User.VALUE_VISIBLE);
			}else{
				showDialog();
			}
		}
	}
	
	private void setInvisiblePosition(final Integer invisible){
		if(invisible == null){
			return;
		}
		progressDialog.show();
		ZHApis.getUserApi().setInvisiblePosition(getActivity(), invisible, new TaskCallback<Object>() {
			
			@Override
			public void onFinish() {
				super.onFinish();
				progressDialog.dismiss();
			}
			
			@Override
			public void onSuccess(Object content) {
				User user = new User();
				user.uid = PrefUtil.Instance().getUserId();
				user.invisible = invisible;
				DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
				
				if (isAdded() && !isDetached()) {
					if(invisible == User.VALUE_INVISIBLE){
						ivLoc.setBackgroundResource(R.drawable.sel_switch);
						ToastUtil.showShort("修改成功。");
					}else{
						ivLoc.setBackgroundResource(R.drawable.switch_on);
						ToastUtil.showShort("位置授权已开启，快去发现附近的人吧！");
					}
				}
				((ZhislandApplication)ZHApplication.APP_CONTEXT).startLocation();
				EventBus.getDefault().post(new EBSetting(EBSetting.TYPE_VISIBLE_SETTING, null));
			}
			
			@Override
			public void onFailure(Throwable error) {
				if (isAdded() && !isDetached()) {
					if(invisible == User.VALUE_INVISIBLE){
						ivLoc.setBackgroundResource(R.drawable.switch_on);
					}else{
						ivLoc.setBackgroundResource(R.drawable.sel_switch);
					}
				}
			}
		});
	}
	
	private void showDialog(){
		if (commonDialog == null) {
			commonDialog = new CommonDialog(getActivity());
		}
		if (!commonDialog.isShowing()) {
			commonDialog.show();
			
			commonDialog.setTitle("确定关闭我的位置信息");
			commonDialog.setContent("关闭后您将不上传位置信息，附近的人将不能发现您");
			commonDialog.setRightBtnColor(getResources().getColor(R.color.red));
			commonDialog.setRightBtnContent("确定关闭");
			commonDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					commonDialog.dismiss();
					setInvisiblePosition(User.VALUE_INVISIBLE);
				}
			});
		}
	}
}
