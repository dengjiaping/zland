package com.zhisland.android.blog.aa.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.common.view.TimeCountUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 使用验证码登录
 */
public class FragLoginByVerifyCode extends FragBase {

    @InjectView(R.id.internationalPhoneView)
    InternationalPhoneView phoneView;
    @InjectView(R.id.etVerifyCode)
    EditText etVerifyCode;
    @InjectView(R.id.tvVerifyCode)
    TextView tvVerifyCode;
    @InjectView(R.id.btnLoginByVerifyCode)
    Button btnLoginByVerifyCode;

    private TimeCountUtil timeCountUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login_by_verifycode, container, false);
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
        AAUtil.getInstance().addVerifyCodeTextChangeListener(etPhoneNum, etVerifyCode, btnLoginByVerifyCode);
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tvVerifyCode)
    public void onClickGetVerfiyCode() {
        if (AAUtil.getInstance().checkMobile(phoneView)) {
            AAUtil.getInstance().getVerifyCodeTask(getActivity(), phoneView.getMobileNum(), phoneView.getCurrentDict().code, timeCountUtil);
        }
    }

    @OnClick(R.id.tvLoginByPwd)
    public void onClickLoginByPwd() {
        //跳转用密码登录
        ((ActRegisterAndLogin) getActivity()).showFragByPwd(phoneView.getMobileNum(), phoneView.getCurrentDict().code);
    }

    @OnClick(R.id.btnLoginByVerifyCode)
    public void onClickLogin() {
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (AAUtil.getInstance().checkMobile(phoneView) && AAUtil.getInstance().checkVerifyCode(verifyCode)) {
            //save country code
            Country.cachUserCountry(phoneView.getCurrentDict());
            showProgressDlg();
            //验证码登录
            ZHApis.getAAApi().loginByVerifyCode(getActivity(), phoneView.getMobileNum(), verifyCode, new TaskCallback<LoginResponse>() {
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
        return "EntranceLogin";
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
