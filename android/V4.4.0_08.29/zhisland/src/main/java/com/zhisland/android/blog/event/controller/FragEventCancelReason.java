package com.zhisland.android.blog.event.controller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.CountEditText;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 取消活动页面
 * */
public class FragEventCancelReason extends FragBase {

	@InjectView(R.id.rgCancelReason)
	RadioGroup rgCancelReason;

	@InjectView(R.id.rbtnCancelReasonOne)
	RadioButton rbtnCancelReasonOne;

	@InjectView(R.id.rbtnCancelReasonTwo)
	RadioButton rbtnCancelReasonTwo;

	@InjectView(R.id.rbtnCancelReasonOther)
	RadioButton rbtnCancelReasonOther;

	@InjectView(R.id.etCancelReason)
	CountEditText etCancelReason;

	@Override
	public String getPageName() {
		return "EventCancelReason";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_event_cancel_reason, null);
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
		etCancelReason.setMaxCount(60);
		etCancelReason.setBackgroundResource(R.drawable.rect_bwhite_sdiv_clargest);
		etCancelReason.getEditText().setHint("");
		etCancelReason.getEditText().setGravity(Gravity.TOP | Gravity.LEFT);
		rgCancelReason
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbtnCancelReasonOne:
							etCancelReason.setVisibility(View.GONE);
							SoftInputUtil.hideInput(getActivity());
							break;
						case R.id.rbtnCancelReasonTwo:
							etCancelReason.setVisibility(View.GONE);
							SoftInputUtil.hideInput(getActivity());
							break;
						case R.id.rbtnCancelReasonOther:
							etCancelReason.setVisibility(View.VISIBLE);
							break;
						}
					}
				});
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * 获取取消原因
	 * */
	public String getReason() {

		String reason = "";
		int checkedId = rgCancelReason.getCheckedRadioButtonId();

		switch (checkedId) {
		case R.id.rbtnCancelReasonOne:
			etCancelReason.setVisibility(View.GONE);
			reason = getString(R.string.cancel_reason_one);
			break;
		case R.id.rbtnCancelReasonTwo:
			etCancelReason.setVisibility(View.GONE);
			reason = getString(R.string.cancel_reason_two);
			break;
		case R.id.rbtnCancelReasonOther:
			etCancelReason.setVisibility(View.VISIBLE);
			reason = etCancelReason.getText().toString().trim();
			break;
		}

		return reason;
	}

}
