package com.zhisland.android.blog.event.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 活动线下支付
 */
public class FragEventOfflinePayment extends FragBase {

    private static final String KEY_EVENT = "KEY_EVENT";
    private static final String KEY_FROM = "KEY_FROM";

    @InjectView(R.id.tvEventPrice)
    TextView tvEventPrice;
    @InjectView(R.id.tvWCPay)
    TextView tvWCPay;
    @InjectView(R.id.tvPayDesc)
    TextView tvPayDesc;

    private Event event;

    private String from;

    public static void invoke(Context context, Event event, String from) {
        if(event == null || event.payData == null || from == null){
            return;
        }
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragEventOfflinePayment.class;
        param.title = "等待支付";
        param.enableBack = true;
        param.swipeBackEnable = false;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EVENT, event);
        bundle.putString(KEY_FROM, from);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_event_offline_payment, null);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = (Event) getActivity().getIntent().getSerializableExtra(KEY_EVENT);
        from = getActivity().getIntent().getStringExtra(KEY_FROM);
        if (event == null) {
            getActivity().finish();
        }
        initView();
    }

    private void initView() {
        tvEventPrice.setText("￥ " + event.payData.getAmountsFormat());
        //当用户支付额度大于限额时，不能微信支付，【进入微信支付】按钮隐藏。
        if (event.payData != null && event.payData.isOnLine != null && event.payData.isOnLine == PayData.TYPE_IS_ON_LINE) {
            //线上支付
            tvWCPay.setVisibility(View.VISIBLE);
            tvPayDesc.setText("想尽快完成报名？建议您采用微信支付");
        } else {
            //线下支付
            tvWCPay.setVisibility(View.GONE);
            tvPayDesc.setMovementMethod(LinkMovementMethod.getInstance());
            String phone = getResources().getString(R.string.app_service_phone);
            String content = "如仍有问题，请联系正和岛\n客服电话";
            SpannableString ssb = new SpannableString(content + phone);
            ssb.setSpan(spanDial, content.length(), content.length() + phone.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPayDesc.setText(ssb);
        }
    }

    ClickableSpan spanDial = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            String phone = getResources().getString(R.string.app_service_phone);
            IntentUtil.dialTo(getActivity(),phone);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_dc));
            ds.setUnderlineText(false);
        }
    };

    @OnClick(R.id.tvIKnow)
    public void onClickGiveUpPay() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_EVENT_PAYMENT_OFFLINE_CONFIRM);
        if (from.equals(FragSignConfirm.class.getName())) {
            //从报名信息确认页过来，弹出FragResultPage
            FragResultPage.invoke(getActivity(), event, from);
        } else {
            if (from.equals(FragSignUpEvents.class.getName())) {
                ActSignUpEvents.invoke(getActivity());
            } else {
                ActEventDetail.invoke(getActivity(), event.eventId, false);
            }
        }
    }

    @OnClick(R.id.tvWCPay)
    public void onClickWCPay() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_EVENT_PAYMENT_OFFLINE_TO_ONLINE);
        FragEventOnlinePayment.invoke(getActivity(), event, from);
    }

    public boolean onBackPressed() {
        if (from.equals(FragSignUpEvents.class.getName())) {
            ActSignUpEvents.invoke(getActivity());
        } else {
            ActEventDetail.invoke(getActivity(), event.eventId, false);
        }
        return true;
    }

    @Override
    public String getPageName() {
        return "EventPayOffline";
    }

}
