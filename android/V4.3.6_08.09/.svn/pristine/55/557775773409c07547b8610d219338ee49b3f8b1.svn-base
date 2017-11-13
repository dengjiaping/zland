package com.zhisland.android.blog.event.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 活动推广页面
 * */
public class FragEventSpread extends FragBase {

	public static final String PAGE_NAME = "EventSpread";

	public static final String INTENT_KEY_EVENT_ID = "intent_key_event_id";

	private static final int TAG_ID_RIGHT = 101;

	private static final int MAX_TEXT_COUNT = 60;

	public static void invoke(Activity context, long eventId) {
		if (eventId <= 0) {
			return;
		}
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragEventSpread.class;
		param.enableBack = false;
		param.swipeBackEnable = false;
		param.title = "让更多人来参与";
		param.titleBtns = new ArrayList<TitleBtn>();
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT, TitleBtn.TYPE_TXT);
		tb.isLeft = false;
		tb.btnText = "完成";
		tb.textColor = context.getResources().getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		param.runnable = titleRunnable;
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_EVENT_ID, eventId);
		context.startActivity(intent);
	}

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragEventSpread) {
					((FragEventSpread) fragment).spreadEvent();
				}
			}
		}
	};

	@InjectView(R.id.etSpread)
	EditText etSpread;

	@InjectView(R.id.tvCount)
	TextView tvCount;

	long eventId;

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		eventId = getActivity().getIntent()
				.getLongExtra(INTENT_KEY_EVENT_ID, 0);
		View root = inflater.inflate(R.layout.frag_event_spread, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initView();
		return root;
	}

	private void initView() {
		EditTextUtil.limitEditTextLength(etSpread, MAX_TEXT_COUNT, tvCount);
	}

	public void spreadEvent() {
		String content = etSpread.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(content)) {
			ToastUtil.showShort("请输入邀约描述");
		} else {
			ZHApis.getEventApi().saveExtension(null, eventId, content, null);
			giveUpClick();
		}
	}

	@OnClick(R.id.tvGiveUp)
	void giveUpClick() {
		ActEventDetail.invoke(getActivity(), eventId, true);
		getActivity().finish();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public boolean onBackPressed() {
		giveUpClick();
		return true;
	}
}
