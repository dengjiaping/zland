package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.StringUtil;

/**
 * 活动取消理由页面
 * */
public class ActEventCancelReasion extends FragBaseActivity {

	private static final int TAG_BACK = 1001;
	private static final int TAG_OK = 1002;
	private FragEventCancelReason frag;
	private long eventId;

	public static final String KEY_INTENT_EVENTID = "key_intent_eventid";

	public static void invoke(Context context, long eventId) {
		Intent intent = new Intent(context, ActEventCancelReasion.class);
		intent.putExtra(KEY_INTENT_EVENTID, eventId);
		if (context instanceof Activity)
			((Activity) context).startActivityForResult(intent,
					FragInitiatedEvents.REQ_NEED_FOR_LOAD_AGAIN);
		else
			context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);

		eventId = getIntent().getLongExtra(KEY_INTENT_EVENTID, -1);

		frag = new FragEventCancelReason();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, frag);
		ft.commit();

		TextView leftBtn = TitleCreator.Instance().createTextButton(this, "取消");
		TextView rightBtn = TitleCreator.Instance().createTextButton(this,
				"确定", R.color.color_dc);
		getTitleBar().addLeftTitleButton(leftBtn, TAG_BACK);
		getTitleBar().addRightTitleButton(rightBtn, TAG_OK);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		super.onTitleClicked(view, tagId);
		switch (tagId) {
		case TAG_BACK:
			finish();
			break;
		case TAG_OK:
			String reason = frag.getReason();
			if (StringUtil.isNullOrEmpty(reason)) {
				showToast("请输入活动取消理由！");
				return;
			}

			eventCancelTask(reason);
            showProgressDlg();
			break;
		}
	}

	private void eventCancelTask(String reason) {
		ZHApis.getEventApi().cancelEvent(ActEventCancelReasion.this, eventId, reason,
				new TaskCallback<Object>() {
					@Override
					public void onSuccess(Object content) {
                        showToast("取消活动成功!");
						setResult(Activity.RESULT_OK);
						finish();
					}

					@Override
					public void onFailure(Throwable error) {
					}

					@Override
					public void onFinish() {
						super.onFinish();
						hideProgressDlg();
					}
				});
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}
}
