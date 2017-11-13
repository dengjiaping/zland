package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.event.dto.VoteChoiceTo;
import com.zhisland.android.blog.event.dto.VoteOptionTo;
import com.zhisland.android.blog.event.dto.VoteTo;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import org.apache.http.client.HttpResponseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 报名确认页面
 * */
public class FragSignConfirm extends FragBase {

	public static final String PAGE_NAME = "EventSignConfirm";

	public static final String INTENT_KEY_EVENT = "intent_key_event";
	public static final String INTENT_KEY_VOTE = "intent_key_vote";

	public static void invoke(Activity context, Event event,
							  ArrayList<VoteTo> voteToList) {
		if (event == null) {
			return;
		}
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragSignConfirm.class;
		param.enableBack = true;
		param.title = "报名信息确认";
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_EVENT, event);
		intent.putExtra(INTENT_KEY_VOTE, voteToList);
		context.startActivity(intent);
	}

	@InjectView(R.id.tvTitle)
	TextView tvTitle;

	@InjectView(R.id.tvTime)
	TextView tvTime;

	@InjectView(R.id.tvPrice)
	TextView tvPrice;

	@InjectView(R.id.tvSignName)
	TextView tvSignName;

	@InjectView(R.id.tvVoteToTitle)
	TextView tvVoteToTitle;

	@InjectView(R.id.internationalPhoneView)
	InternationalPhoneView internationalPhoneView;

	@InjectView(R.id.llVoteTo)
	LinearLayout llVoteTo;

	@InjectView(R.id.rlBottom)
	RelativeLayout rlBottom;

	@InjectView(R.id.llInvoice)
	LinearLayout llInvoice;

	@InjectView(R.id.ivSwitch)
	ImageView ivSwitch;

	@InjectView(R.id.llFinancePhone)
	LinearLayout llFinancePhone;

	@InjectView(R.id.etFinancePhone)
	EditText etFinancePhone;

	@InjectView(R.id.tvPersonalPrompt)
	TextView tvPersonalPrompt;

	private Event event;

	private User self;

	private ArrayList<VoteTo> voteToList;

	boolean signUping = false;

	private boolean needInvoice = false;

	ArrayList<VoteToViewHolder> VoteToViewHolderList = new ArrayList<FragSignConfirm.VoteToViewHolder>();

	@Override
	public String getPageName() {
		return PAGE_NAME;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_sign_confirm, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initView();
		return root;
	}

	private void initView() {
		internationalPhoneView.getEditText().setFocusable(false);
		internationalPhoneView.getEditText().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						etPhoneClick();
					}
				});
		Object obj = getActivity().getIntent().getSerializableExtra(
				INTENT_KEY_VOTE);
		if (obj != null) {
			voteToList = (ArrayList<VoteTo>) obj;
		}
		obj = getActivity().getIntent().getSerializableExtra(INTENT_KEY_EVENT);
		if (obj != null) {
			event = (Event) obj;
		}
		self = DBMgr.getMgr().getUserDao().getSelfUser();
		fillEventToView();
		fillVoteToView();
		fillInvoiceView();
		checkBottomBtn();
	}

	/**
	 * 检查报名按钮是否可用，不可用置灰
	 * */
	private void checkBottomBtn() {
		boolean enabled = true;
		for (VoteToViewHolder holder : VoteToViewHolderList) {
			if (holder.voteTo.required
					&& holder.voteChoiceTo.optionChoiced.size() == 0) {
				enabled = false;
				break;
			}
		}
		if (enabled) {
			rlBottom.setEnabled(true);
		} else {
			rlBottom.setEnabled(false);
		}
	}

	/**
	 * 将活动信息添加到View中显示
	 * */
	private void fillEventToView() {
		tvTitle.setText(event.eventTitle);
		tvTime.setText(event.period);
		tvPrice.setText(event.priceText);
		tvSignName.setText(self.name);
		internationalPhoneView.setCountryByCode(self.countryCodeShow);
		internationalPhoneView.setEditHint(self.userMobile + "(点击修改)");
		if (event.type == Event.CATEGORY_TYPE_UGC) {
			tvPersonalPrompt.setVisibility(View.VISIBLE);
		}else{
			tvPersonalPrompt.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置发票View
	 * */
	private void fillInvoiceView() {
		//在官方活动，价格不是免费的情况下显示
		if (event.type == Event.CATEGORY_TYPE_OFFICIAL && event.currentPrice != null && event.currentPrice > 0) {
			needInvoice = true;
			llInvoice.setVisibility(View.VISIBLE);
			llFinancePhone.setVisibility(View.VISIBLE);
		} else {
			llInvoice.setVisibility(View.GONE);
			llFinancePhone.setVisibility(View.GONE);
		}
	}

	/**
	 * 将投票数据添加到View中显示
	 * */
	private void fillVoteToView() {
		if (voteToList == null || voteToList.size() == 0) {
			tvVoteToTitle.setVisibility(View.GONE);
			return;
		}
		tvVoteToTitle.setVisibility(View.VISIBLE);
		for (int i = 0; i < voteToList.size(); i++) {
			VoteToViewHolder voteToViewHolder = new VoteToViewHolder(voteToList.get(i));
			llVoteTo.addView(voteToViewHolder.voteToView);
			if (i < (voteToList.size() - 1)) {
				View line = new View(getActivity());
				line.setBackgroundColor(getResources().getColor(R.color.color_div));
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(0.7f));
				int marginLeft = DensityUtil.dip2px(15);
				int marginTop = DensityUtil.dip2px(5);
				lp.setMargins(marginLeft, marginTop, marginLeft, marginTop);
				llVoteTo.addView(line, lp);
			}
			VoteToViewHolderList.add(voteToViewHolder);
		}
	}

	class VoteToViewHolder {

		/**
		 * 投票题数据
		 * */
		VoteTo voteTo;

		/**
		 * 投票选择结果数据
		 * */
		VoteChoiceTo voteChoiceTo;

		LinearLayout voteToView;

		/**
		 * 每个选项的 选择ImageView
		 * */
		List<ImageView> ivChoiceList = new ArrayList<ImageView>();

		TextView tvVoteTitle;
		TextView tvIsMulti;

		public VoteToViewHolder(VoteTo voteTo) {
			this.voteTo = voteTo;
			voteChoiceTo = new VoteChoiceTo();
			voteChoiceTo.voteId = voteTo.id;
			makeVoteToView();
		}

		private void makeVoteToView() {
			voteToView = (LinearLayout) LayoutInflater.from(getActivity())
					.inflate(R.layout.item_vote_to, null);
			tvVoteTitle = (TextView) voteToView.findViewById(R.id.tvVoteTitle);
			tvIsMulti = (TextView) voteToView.findViewById(R.id.tvIsMulti);
			tvVoteTitle.setText(voteTo.desc);
			tvIsMulti.setText(voteTo.isMulti ? "(多选)" : "(单选)");
			for (final VoteOptionTo option : voteTo.options) {
				View optionView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_vote_to_option, null);
				TextView tvOptionContent = (TextView) optionView
						.findViewById(R.id.tvOptionContent);
				final ImageView ivChoice = (ImageView) optionView
						.findViewById(R.id.ivChoice);
				ivChoiceList.add(ivChoice);
				tvOptionContent.setText(option.desc);
				optionView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (voteChoiceTo.optionChoiced.contains(option)) {
							voteChoiceTo.optionChoiced.remove(option);
							ivChoice.setImageResource(R.drawable.radio_box);
						} else {
							if (voteTo.isMulti) {
								voteChoiceTo.optionChoiced.add(option);
								ivChoice.setImageResource(R.drawable.chk_checked);
							} else {
								clearChoice();
								voteChoiceTo.optionChoiced.add(option);
								ivChoice.setImageResource(R.drawable.radio_box_press);
							}
						}
						SoftInputUtil.hideInput(getActivity());
						checkBottomBtn();
					}
				});
				voteToView.addView(optionView);
			}
		}

		/**
		 * 清空这道题的所有选项为未选择
		 * */
		private void clearChoice() {
			voteChoiceTo.optionChoiced.clear();
			for (ImageView ivChoice : ivChoiceList) {
				ivChoice.setImageResource(R.drawable.radio_box);
			}
		}

	}

	void etPhoneClick() {
		EditText etPhone = internationalPhoneView.getEditText();
		if (StringUtil.isNullOrEmpty(etPhone.getText().toString())) {
			etPhone.setFocusable(true);
			etPhone.setFocusableInTouchMode(true);
			etPhone.requestFocus();
			SoftInputUtil.showKeyboard(etPhone);
		}
	}

	@OnClick(R.id.ivSwitch)
	void switchClick(){
		if(needInvoice){
			needInvoice = false;
			ivSwitch.setBackgroundResource(R.drawable.switch_off);
			llFinancePhone.setVisibility(View.GONE);
		}else{
			needInvoice = true;
			ivSwitch.setBackgroundResource(R.drawable.switch_on);
			llFinancePhone.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.rlBottom)
	void bottomBtnClick() {
		// 检查手机号是否合法
		String userMobile = null;
		String phone = internationalPhoneView.getMobileNum();
		if (StringUtil.isNullOrEmpty(phone)) {
			userMobile = internationalPhoneView.getCurrentDict().code + "-" + self.userMobile;
		} else {
			if (!internationalPhoneView.checkInput()) {
				showToast("您输入的手机号不正确");
				return;
			} else {
				userMobile = internationalPhoneView.getDictMobile();
			}
		}
		// 获取每道选择题的答案
		List<VoteChoiceTo> voteChoiceToList = new ArrayList<VoteChoiceTo>();
		for (VoteToViewHolder holder : VoteToViewHolderList) {
			if (holder.voteTo.required
					&& holder.voteChoiceTo.optionChoiced.size() == 0) {
				showToast(holder.voteTo.desc);
				return;
			}
			voteChoiceToList.add(holder.voteChoiceTo.makeChoiceStringByList());
		}
		String choice = GsonHelper.GetCommonGson().toJson(voteChoiceToList);

		Integer invoiceOption = null;
		String financePhone = null;
		if (needInvoice) {
			invoiceOption = Event.INVOICE_OPTION_NEED;
			financePhone = etFinancePhone.getText().toString();
		}
		signUp(userMobile, choice, invoiceOption, financePhone);
	}

	/**
	 * 请求报名
	 * */
	private void signUp(String userMobile, String choice, Integer invoiceOption, String financePhone) {
		showProgressDlg("请求中...");
		if (signUping) {
			return;
		}
		signUping = true;
		ZHApis.getEventApi().signUp(getActivity(), event.eventId, userMobile, choice, invoiceOption, financePhone,
				new TaskCallback<PayData>() {

					@Override
					public void onSuccess(PayData content) {
						if (!isAdded() || isDetached()) {
							showToast("报名成功");
							return;
						}
						event.signStatus = Event.SIGN_STATUS_YES;
						event.auditStatus = Event.STATUS_AUDIT_WAIT;
						if (event.signedUsers == null) {
							event.signedUsers = new ArrayList<User>();
						}
						User self = DBMgr.getMgr().getUserDao().getSelfUser();
						if (self != null) {
							event.signedUsers.add(0, self);
							event.signedCount += 1;
						}
						event.payData = content;
						if(content != null && content.amounts != null && content.amounts > 0){
							if (content.isOnLine == PayData.TYPE_IS_ON_LINE) {
								FragEventOnlinePayment.invoke(getActivity(), event, FragSignConfirm.class.getName());
							} else {
								FragEventOfflinePayment.invoke(getActivity(), event, FragSignConfirm.class.getName());
							}
						}else{
							FragResultPage.invoke(getActivity(), event, this.getClass().getName());
						}
						EventBus.getDefault().post(
								new EBEvent(EBEvent.TYPE_EVENT_SIGN_UP, event));
					}

					@Override
					public void onFailure(Throwable error) {
						String errorStr = "报名失败";
						if (error != null) {
							if (error instanceof HttpResponseException) {
								HttpResponseException e = (HttpResponseException) error;
								int statusCode = e.getStatusCode();
								switch (statusCode) {
									case CodeUtil.CODE_EVENT_OVER:
										errorStr = "活动已结束。";
										break;
									case CodeUtil.CODE_EVENT_CANCEL:
										errorStr = "活动已取消。";
										break;
									case CodeUtil.CODE_HAS_SIGNED:
										errorStr = "您已经成功报名";
										break;
									case CodeUtil.CODE_SIGN_OVER:
										errorStr = "报名期限已结束";
										break;
									case CodeUtil.CODE_SIGN_NUM_FULL:
									case CodeUtil.CODE_VIP_SIGN_NUM_FULL:
									case CodeUtil.CODE_NOVIP_SIGN_NUM_FULL:
										errorStr = "报名人数已满";
										break;
								}
							}
						}
						showToast(errorStr);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						signUping = false;
						if (isAdded() || !isDetached()) {
							hideProgressDlg();
						}
					}
				});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
