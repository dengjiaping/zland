package com.zhisland.android.blog.invitation.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.AAUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 通过手机号邀请
 */
public class FragInviteByPhone extends FragBase {

    @InjectView(R.id.internationalPhoneView)
    InternationalPhoneView phoneView;
    @InjectView(R.id.btnInviteByPhone)
    Button btnInviteByPhone;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragInviteByPhone.class;
        param.title = "手机号邀请";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_invite_by_phone, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        TextView tvCode = phoneView.getTvInternationalCode();
        tvCode.setTextColor(getResources().getColor(R.color.color_f1));
        EditText etPhone = phoneView.getEditText();
        etPhone.addTextChangedListener(textWatcher);
        etPhone.setHint("输入手机号码");
        etPhone.setTextColor(getResources().getColor(R.color.color_f1));
        etPhone.setHintTextColor(getResources().getColor(R.color.color_f3));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = DensityUtil.dip2px(20);
        etPhone.setLayoutParams(params);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String userPhone = phoneView.getMobileNum();
            if (StringUtil.isNullOrEmpty(userPhone)) {
                btnInviteByPhone.setEnabled(false);
            } else {
                btnInviteByPhone.setEnabled(true);
            }
        }
    };

    @OnClick(R.id.btnInviteByPhone)
    public void onClickInviteByPhone() {
        if (AAUtil.getInstance().checkMobile(phoneView)) {
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_INVITE_MOBILE_CONFIRM_CLICK);
            final String userPhone = phoneView.getMobileNum();
            final String code = phoneView.getCurrentDict().code;
            final AProgressDialog progressDialog = new AProgressDialog(getActivity());
            progressDialog.show();
            ZHApis.getUserApi().inviteByPhone(getActivity(), null, code, userPhone, new TaskCallback<String>() {
                @Override
                public void onSuccess(String content) {
                    ToastUtil.showShort("已发送");
                    getActivity().finish();
                }

                @Override
                public void onFailure(Throwable error) {
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progressDialog.dismiss();
                }
            });
        }
    }


    @Override
    protected String getPageName() {
        return "InviteMobileInvitation";
    }

}

