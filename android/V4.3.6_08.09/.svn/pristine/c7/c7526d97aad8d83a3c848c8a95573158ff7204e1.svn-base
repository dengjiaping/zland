package com.zhisland.android.blog.event.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 活动结果页
 */
public class FragResultPage extends FragBase {

	public static final String INK_FROM = "ink_from";

	public static final String KEY_INTENT_EVENT = "key_intent_event";
	private static final String KEY_FROM = "KEY_FROM";

	@InjectView(R.id.ivSucIcon)
	ImageView ivSucIcon;

	@InjectView(R.id.tvSucTitle)
	TextView tvSucTitle;

	@InjectView(R.id.tvSucDesc)
	TextView tvSucDesc;

	@InjectView(R.id.tvSucBack)
	TextView tvSucBack;

	@InjectView(R.id.btnSucBottom)
	Button btnSucBottom;

	@InjectView(R.id.tvPayInfo)
	TextView tvPayInfo;

	private Event event;

	private String from;

	@Override
	protected String getPageName() {
		return "EventSignResult";
	}

	public static void invoke(Context context, Event event, String from) {
		if(event == null || from == null){
			return;
		}
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragResultPage.class;
		param.noTitle = true;
		param.swipeBackEnable = false;
		Intent intent = CommonFragActivity.createIntent(context, param);
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_INTENT_EVENT, event);
		bundle.putString(KEY_FROM, from);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_result_page, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initView();
		return root;
	}

	private void initView() {
		event = (Event) getActivity().getIntent().getSerializableExtra(KEY_INTENT_EVENT);
		from = getActivity().getIntent().getStringExtra(KEY_FROM);
		btnSucBottom.setText("邀请好友来参加");

		if(event.auditStatus == Event.STATUS_AUDIT_ACCEPT){
            ivSucIcon.setImageResource(R.drawable.img_finish_icon_green);
			if(event.payData != null && event.payData.payStatus != null && event.payData.payStatus == PayData.PAY_STATUS_OK){
				tvSucTitle.setText("报名成功");
				tvSucDesc.setText("恭喜您成功报名活动。记得提早安排行程，准时参会哦");
			} else {
				tvSucTitle.setText("报名已通过");
				tvSucDesc.setText("您的报名已通过审核，完成付款后即可参与。记得提早安排行程，准时参会哦");
			}
		}else{
            ivSucIcon.setImageResource(R.drawable.img_finish_icon_red);
			tvSucTitle.setText("报名确认中");
			tvSucDesc.setText("感谢您的报名，审核通过后您将收到报名成功的短信，请在此之后再安排行程");
		}
		setPayInfoView();
		setBackInfoView();
	}

	private void setBackInfoView(){
		if(from.equals(FragSignUpEvents.class.getName())){
			tvSucBack.setText("返回我报名的活动");
		}else{
			tvSucBack.setText("返回活动详情");
		}
	}

	private void setPayInfoView(){
		if (event.payData != null && event.payData.amounts != null && event.payData.amounts > 0) {
			tvPayInfo.setVisibility(View.VISIBLE);
			if(event.payData.payStatus != null && event.payData.payStatus == PayData.PAY_STATUS_OK){
				tvPayInfo.setText("支付成功￥" + event.payData.getPayAmountsFormat());
				Drawable drawable= getResources().getDrawable(R.drawable.img_campaign_pay_successed);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tvPayInfo.setCompoundDrawables(drawable,null,null,null);
			}else{
				tvPayInfo.setText("待支付￥" + event.payData.getAmountsFormat());
				Drawable drawable= getResources().getDrawable(R.drawable.img_campaign_pay_wait);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tvPayInfo.setCompoundDrawables(drawable,null,null,null);
			}
		} else {
			tvPayInfo.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.tvSucBack)
	void backClick() {
		if (from.equals(FragSignUpEvents.class.getName())) {
			ActSignUpEvents.invoke(getActivity());
		} else {
			ActEventDetail.invoke(getActivity(), event.eventId, false);
		}
	}

	@Override
	public boolean onBackPressed() {
		backClick();
		return true;
	}

	@OnClick(R.id.btnSucBottom)
	void shareClick() {
		Event event = (Event) getActivity().getIntent().getSerializableExtra(
				KEY_INTENT_EVENT);
		cachImage(event.getEventImgUrl());
		DialogUtil.getInstatnce().showEventDialog(getActivity(), event,
				DialogUtil.FROM_EVENT_SIGN_OK, getPageName());
	}

	// 将活动图片缓存起来，以用于分享
	private void cachImage(String url) {
		ImageView imageView = new ImageView(getActivity());
		imageView.setVisibility(View.GONE);
		ImageWorkFactory.getFetcher().loadImage(url, imageView);
	}
}
