package com.zhisland.android.blog.event.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.WechatUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.event.dto.PayRequest;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.android.blog.wxapi.eb.EBWxPayRes;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.view.dialog.AProgressDialog;

import org.apache.http.client.HttpResponseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 活动线上支付
 */
public class FragEventOnlinePayment extends FragBase {

    private static final String KEY_EVENT = "KEY_EVENT";
    private static final String KEY_FROM = "KEY_FROM";

    @InjectView(R.id.tvEventPrice)
    TextView tvEventPrice;

    private Event event;

    private String from;

    private AProgressDialog progressDialogReq;
    private AProgressDialog progressDialogRes;

    public static void invoke(Context context, Event event, String from) {
        if (event == null || event.payData == null || from == null) {
            return;
        }
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragEventOnlinePayment.class;
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
        View root = inflater.inflate(R.layout.frag_event_online_payment, null);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        event = (Event) getActivity().getIntent().getSerializableExtra(KEY_EVENT);
        from = getActivity().getIntent().getStringExtra(KEY_FROM);
        if (event == null || event.payData == null) {
            getActivity().finish();
        }
        tvEventPrice.setText("￥ " + event.payData.getAmountsFormat());
    }

    public void onEventMainThread(EBWxPayRes eb) {
        if (eb.result == EBWxPayRes.RESULT_CANCEL) {
            //如果取消支付，不做操作。
        } else {
            //无论成功还是失败，都去服务器查询支付状态。
            refreshEventPayStatus();
        }
    }

    /**
     * 访问服务器，刷新支付状态。
     * */
    private void refreshEventPayStatus(){
        showResProgressDlg();
        ZHApis.getEventApi().getPayRes(getActivity(), event.eventId, new TaskCallback<PayData>() {

            @Override
            public void onFinish() {
                if (isAdded() && !isDetached()) {
                    if (progressDialogRes != null) {
                        progressDialogRes.dismiss();
                    }
                }
                EventBus.getDefault().post(new EBEvent(EBEvent.TYPE_EVENT_PAY_STATUS_CHANGED, event));
                super.onFinish();
            }

            @Override
            public void onSuccess(PayData content) {
                if (isAdded() && !isDetached()) {
                    if (content != null && content.payStatus == PayData.PAY_STATUS_OK) {
                        event.payData = content;
                        FragResultPage.invoke(getActivity(), event, from);
                    } else {
                        getPayResFailure();
                    }
                }
            }

            @Override
            public void onFailure(Throwable error) {
                getPayResFailure();
            }
        });
    }

    private void getPayResFailure(){
        final CommonDialog failuerDlg = new CommonDialog(getActivity());
        failuerDlg.show();
        failuerDlg.setTitle("暂未获得支付结果");
        String phone = getResources().getString(R.string.app_service_phone);
        String content = " 如您支付失败但仍有扣款，请联系正和岛客服电话";
        failuerDlg.setContent(content + phone);
        failuerDlg.setLeftBtnGone();
        failuerDlg.setRightBtnContent("关闭");
        failuerDlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failuerDlg.dismiss();
            }
        });
    }

    @OnClick(R.id.tvWCPay)
    public void onClickWCPay() {
        payOnWeiXin();
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_EVENT_PAYMENT_ONLINE_PAY);
    }

    @OnClick(R.id.tvGiveUpPay)
    public void onClickGiveUpPay() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_EVENT_PAYMENT_ONLINE);
        //跳转线下支付
        FragEventOfflinePayment.invoke(getActivity(), event, from);
        getActivity().finish();
    }


    @Override
    public boolean onBackPressed() {
        showExitDlg();
        return true;
    }

    /**
     * 退出dialog
     */
    private void showExitDlg() {
        final CommonDialog exitDlg = new CommonDialog(getActivity());
        exitDlg.show();
        exitDlg.setTitle("确认要放弃支付？");
        exitDlg.setLeftBtnContent("暂不支付");
        exitDlg.setRightBtnContent("继续支付");
        exitDlg.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDlg.dismiss();
                if (from.equals(FragSignUpEvents.class.getName())) {
                    ActSignUpEvents.invoke(getActivity());
                } else {
                    ActEventDetail.invoke(getActivity(), event.eventId, false);
                }
            }
        });
        exitDlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDlg.dismiss();
                payOnWeiXin();
            }
        });
    }

    private void payOnWeiXin() {
        boolean installed = WechatUtil.getInstance().isInstallWechat(getActivity());
        if (!installed) {
            return;
        }
        boolean isPaySupported = WechatUtil.getInstance().isPaySupported(getActivity());
        if(!isPaySupported){
            showToast("您的微信App版本过低。");
            return;
        }
        showReqProgressDlg();
        ZHApis.getEventApi().getWXPayReq(getActivity(), event.eventId, new TaskCallback<PayRequest>() {

            @Override
            public void onFinish() {
                if (isAdded() && !isDetached()) {
                    if (progressDialogReq != null) {
                        progressDialogReq.dismiss();
                    }
                }
                super.onFinish();
            }

            @Override
            public void onSuccess(PayRequest content) {
                if (isAdded() && !isDetached() && content != null) {
                    PayReq req = new PayReq();
                    req.appId = content.appId;
                    req.partnerId = content.partnerId;
                    req.prepayId = content.prepayId;
                    req.nonceStr = content.nonceStr;
                    req.timeStamp = content.timeStamp;
                    req.sign = content.sign;
                    req.packageValue = content.packageValue;
                    WechatUtil.getInstance().wechatPay(getActivity(), req);
                }
            }

            @Override
            public void onFailure(Throwable error) {
                if (error != null) {
                    if (error instanceof HttpResponseException) {
                        HttpResponseException e = (HttpResponseException) error;
                        int statusCode = e.getStatusCode();
                        switch (statusCode) {
                            case CodeUtil.CODE_REPEAT_PAY:
                                refreshEventPayStatus();
                                break;
                        }
                    }
                }
            }
        });
    }

    private void showReqProgressDlg() {
        if (progressDialogReq == null) {
            progressDialogReq = new AProgressDialog(getActivity());
            progressDialogReq.setText("正在获取数据...");
        }
        progressDialogReq.show();
    }

    private void showResProgressDlg() {
        if (progressDialogRes == null) {
            progressDialogRes = new AProgressDialog(getActivity(), AProgressDialog.OrientationEnum.vertical);
            progressDialogRes.setText("正在获取支付结果");
            progressDialogRes.setCancelable(false);
        }
        progressDialogRes.show();
    }

    @Override
    public String getPageName() {
        return "EventPayOnline";
    }
}
