package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.StringUtil;

public class ActEventCreate extends FragBaseActivity {

	public static final String KEY_EVENT = "key_event";
	public static final String KEY_FROM = "key_from";
	public static final int FROM_CREATE_EVENT = 0;
	public static final int FROM_MODIFY_EVENT = 1;

	private static final int TAG_LEFT = 1001;
	private static final int TAG_RIGHT = 1002;

	FragEventCreateFirst fragEventCreateFirst;
	FragEventCreateSecond fragEventCreateSecond;

	private CommonDialog dialog;

	TextView leftTextBtn;
	TextView rightTextBtn;

	private int from = FROM_CREATE_EVENT;

	/**
	 * 启动活动编辑页面，创建活动
	 * */
	public static void invoke(Context context, Event event) {
		Intent intent = getInvokeIntent(context, event);
		context.startActivity(intent);
	}

	/**
	 * 启动活动编辑页面，更新活动
	 * */
	public static void invokeForResult(Activity context, Event event,
			int reqCode) {
		Intent intent = getInvokeIntent(context, event);
		context.startActivityForResult(intent, reqCode);
	}

	private static Intent getInvokeIntent(Context context, Event event) {
		Intent intent = new Intent(context, ActEventCreate.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_EVENT, event);
		bundle.putSerializable(KEY_FROM, event == null ? FROM_CREATE_EVENT
				: FROM_MODIFY_EVENT);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);
		fragEventCreateFirst = new FragEventCreateFirst();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragEventCreateFirst);
		ft.commit();
		from = getIntent().getIntExtra(KEY_FROM, FROM_CREATE_EVENT);
		initTitle();
	}

	@Override
	public void onBackPressed() {
		if (fragEventCreateSecond == null) {
			if (from == FROM_CREATE_EVENT) {
				cancelCreate();
			} else if (from == FROM_MODIFY_EVENT) {
				showDialog("确认取消修改此活动?", null, true);
			}
		} else {
			fragEventCreateSecond.setEventToIntent();
			removeSecondStep();
		}
	}

	/**
	 * 发布活动或取消发布活动，对话框
	 * @param isCancel 是否为取消发布的对话框
	 * */
	public void showDialog(String title, String content, final boolean isCancel) {
		if (dialog == null) {
			dialog = new CommonDialog(this);
		}
		if (!dialog.isShowing()) {
			dialog.show();
			dialog.setTitle(title);
			if (!StringUtil.isNullOrEmpty(content)) {
				dialog.setContent(content);
			}
			if (isCancel) {
				dialog.tvDlgCancel.setText("放弃取消");
			} else {
				dialog.tvDlgCancel.setText("取消");
			}
			dialog.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isCancel) {
						fragEventCreateSecond
								.createOrUpdateEvent(from == FROM_CREATE_EVENT);
					} else {
						finish();
					}
					dialog.dismiss();
				}
			});
		}
	}

	private void initTitle() {
		leftTextBtn = TitleCreator.Instance().createTextButton(this, "取消");
		getTitleBar().addLeftTitleButton(leftTextBtn, TAG_LEFT);
		rightTextBtn = TitleCreator.Instance().createTextButton(this, "下一步",
				R.color.color_dc);
		getTitleBar().addRightTitleButton(rightTextBtn, TAG_RIGHT);
		switch (from) {
		case FROM_CREATE_EVENT:
			getTitleBar().setTitle("发起活动");
			break;
		case FROM_MODIFY_EVENT:
			getTitleBar().setTitle("编辑活动");
			break;
		}
	}

	/**
	 * 刷新title按钮的显示状态。
	 * */
	public void refreshTitle() {
		if (fragEventCreateSecond == null) {
			leftTextBtn.setText("取消");
			rightTextBtn.setText("下一步");
			if(fragEventCreateFirst.isInputValidated()){
				rightTextBtn.setEnabled(true);
				rightTextBtn.setTextColor(getResources().getColor(R.color.color_dc));
			}else{
				rightTextBtn.setEnabled(false);
				rightTextBtn.setTextColor(Color.parseColor("#d4d4d4"));
			}
		} else {
			leftTextBtn.setText("上一步");
			rightTextBtn.setEnabled(true);
			rightTextBtn.setTextColor(getResources().getColor(R.color.color_dc));
			rightTextBtn.setText("发布");
		}
	}

	/**
	 * 取消发布
	 * */
	private void cancelCreate() {
		if (fragEventCreateFirst.hasEdited()) {
			showDialog("确认取消发布此活动?", null, true);
		} else {
			finish();
		}
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		super.onTitleClicked(view, tagId);
		if (fragEventCreateSecond == null) {
			if (tagId == TAG_LEFT) {
				if (from == FROM_CREATE_EVENT) {
					cancelCreate();
				} else if (from == FROM_MODIFY_EVENT) {
					showDialog("确认取消修改此活动?", null, true);
				}
			} else if (tagId == TAG_RIGHT) {
				if (!fragEventCreateFirst.isInputValidated())
					return;
				fragEventCreateFirst.setEventToIntent();
				showSecondStep();
			}
		} else {
			if (tagId == TAG_LEFT) {
				fragEventCreateSecond.setEventToIntent();
				removeSecondStep();
			} else if (tagId == TAG_RIGHT && fragEventCreateSecond.checkInputContent()) {
				if (from == FROM_CREATE_EVENT) {
					showDialog("确认发布此活动", null, false);
				} else if (from == FROM_MODIFY_EVENT) {
					showDialog("确认修改此活动", null, false);
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		fragEventCreateFirst.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}

	/**
	 * 显示活动编辑第二步
	 * */
	public void showSecondStep() {
		if (fragEventCreateSecond == null) {
			fragEventCreateSecond = new FragEventCreateSecond();
		}
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUBLISH_FIRST_TO_SECOND);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.hide(fragEventCreateFirst);
		ft.add(R.id.frag_container, fragEventCreateSecond);
		ft.commit();
		refreshTitle();
	}

	/**
	 * 隐藏活动编辑第二步
	 * */
	public void removeSecondStep() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.show(fragEventCreateFirst);
		ft.remove(fragEventCreateSecond);
		ft.commitAllowingStateLoss();
		fragEventCreateSecond = null;
		refreshTitle();
	}

}
