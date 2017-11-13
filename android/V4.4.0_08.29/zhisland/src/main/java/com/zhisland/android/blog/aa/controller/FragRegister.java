package com.zhisland.android.blog.aa.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.common.view.TimeCountUtil;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册
 */
public class FragRegister extends FragBase {

    @InjectView(R.id.internationalPhoneView)
    InternationalPhoneView phoneView;
    @InjectView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @InjectView(R.id.tvVerifyCode)
    TextView tvVerifyCode;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.tvDeclare)
    TextView tvDeclare;

    private TimeCountUtil timeCountUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_register, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        EditText etPhoneNum = phoneView.getEditText();
        etPhoneNum.setHint("请输入手机号");
        DensityUtil.setTextSize(etPhoneNum, R.dimen.txt_16);
        etPhoneNum.setHintTextColor(getResources().getColor(R.color.color_f3));
        etPhoneNum.setTextColor(getResources().getColor(R.color.color_f1));
        phoneView.getTvInternationalCode().setTextColor(getResources().getColor(R.color.color_f1));
        DensityUtil.setTextSize(phoneView.getTvInternationalCode(), R.dimen.txt_16);
        timeCountUtil = new TimeCountUtil(getActivity(), 60000, 1000,
                tvVerifyCode);
        timeCountUtil.setEnableTvColor(R.color.color_dc);
        timeCountUtil.setDisableTvColor(R.color.color_f3);
        AAUtil.getInstance().addVerifyCodeTextChangeListener(etPhoneNum, etVerifyCode, btnRegister);

        String preStr = "点击注册即表示同意";
        String sufStr = "《正和岛用户服务协议及隐私协议》";
        SpannableString ss = new SpannableString(preStr + sufStr);
        ss.setSpan(spanPrivacy, preStr.length(), ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDeclare.setMovementMethod(LinkMovementMethod.getInstance());
        tvDeclare.append(ss);
    }

    ClickableSpan spanPrivacy = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            FragPrivacy.invoke(getActivity());
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_f1));
            ds.setUnderlineText(false);
        }

    };

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvVerifyCode)
    public void onClickGetVerfiyCode() {
        if (AAUtil.getInstance().checkMobile(phoneView)) {
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_REGISTER_VERIFY_CODE);
            AAUtil.getInstance().getVerifyCodeTask(getActivity(), phoneView.getMobileNum(), phoneView.getCurrentDict().code, timeCountUtil);
        }
    }

    @OnClick(R.id.btnGoToLogin)
    public void onClickGoToLogin() {
        //跳转用验证码登录
        ((ActRegisterAndLogin) getActivity()).showFragByVerifyCode(phoneView.getMobileNum(), phoneView.getCurrentDict().code);
    }

    @OnClick(R.id.btnRegister)
    public void onClickRegister() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_REGISTER_CLICK_BUTTON);
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (AAUtil.getInstance().checkMobile(phoneView) && AAUtil.getInstance().checkVerifyCode(verifyCode)) {
            //save country code
            Country.cachUserCountry(phoneView.getCurrentDict());
            showProgressDlg();
            //注册
            ZHApis.getAAApi().register(getActivity(), phoneView.getMobileNum(), verifyCode, new TaskCallback<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse content) {
                    ResultCodeUtil.getInstance().dispatchSuccess(getActivity(), content);
                }

                @Override
                public void onFailure(Throwable error) {
                    ResultCodeUtil.getInstance().dispatchErrorCode(getActivity(), error, phoneView);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideProgressDlg();
                }
            });
        }
    }

    @Override
    public String getPageName() {
        return "EntranceRegister";
    }

    /**
     * 设置电话和国家码
     */
    public void setMobile(String mobileNum, String countryCode) {
        if (phoneView != null) {
            phoneView.getEditText().requestFocus();
            phoneView.setMobileNum(mobileNum);
            phoneView.setCountryByCode(countryCode);
        }
    }
}
