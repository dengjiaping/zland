package com.zhisland.android.blog.aa.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 使用密码登录
 */
public class FragLoginByPwd extends FragBase {

    @InjectView(R.id.internationalPhoneView)
    InternationalPhoneView phoneView;
    @InjectView(R.id.etLoginPwd)
    EditText etLoginPwd;
    @InjectView(R.id.btnLoginByPwd)
    Button btnLoginByPwd;
    @InjectView(R.id.ivRtPwdEye)
    public ImageView ivRtPwdEye;

    //是否显示密码
    private boolean isShowPwd = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login_by_pwd, container, false);
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
        AAUtil.getInstance().addPwdTextChangeListener(etPhoneNum, etLoginPwd, btnLoginByPwd);
    }

    @OnClick(R.id.tvLoginByVerifyCode)
    public void onClickLoginByVerifyCode() {
        //跳转用验证码登录
        ((ActRegisterAndLogin) getActivity()).showFragByVerifyCode(phoneView.getMobileNum(), phoneView.getCurrentDict().code);
    }

    @OnClick(R.id.btnLoginByPwd)
    public void onClickLogin() {
        String pwd = etLoginPwd.getText().toString().trim();
        if (AAUtil.getInstance().checkMobile(phoneView) && AAUtil.getInstance().checkPwd(pwd)) {
            //save country code
            Country.cachUserCountry(phoneView.getCurrentDict());
            final AProgressDialog progressDialog = new AProgressDialog(getActivity());
            progressDialog.show();
            //密码登录
            ZHApis.getAAApi().loginByPwd(getActivity(), phoneView.getMobileNum(), pwd, new TaskCallback<LoginResponse>() {
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
                    progressDialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.ivRtPwdEye)
    public void onPwdEye() {
        if (isShowPwd) {
            ivRtPwdEye.setSelected(false);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etLoginPwd.setSelection(etLoginPwd.getText().length());
        } else {
            ivRtPwdEye.setSelected(true);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etLoginPwd.setSelection(etLoginPwd.getText().length());
        }
        isShowPwd = !isShowPwd;
    }

    @Override
    protected String getPageName() {
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
