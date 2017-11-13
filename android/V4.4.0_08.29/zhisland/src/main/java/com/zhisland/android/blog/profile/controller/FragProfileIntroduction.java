package com.zhisland.android.blog.profile.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.CountEditText;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * 个人简介
 */
public class FragProfileIntroduction extends FragBase {

    public static final int EDIT_TEXT_COUNT = 200;
    private ActProfileIntroduction mActivity;

    @InjectView(R.id.etIntroduction)
    public CountEditText etIntroduction;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_profile_introduction, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initViews();
        return root;
    }

    @Override
    public String getPageName() {
        return "ProfileSelfIntro";
    }

    public void initViews() {
        if (getActivity() instanceof ActProfileIntroduction)
            mActivity = (ActProfileIntroduction) getActivity();
        etIntroduction.setMaxCount(EDIT_TEXT_COUNT);
        fillUser();

        etIntroduction.getEditText().setLines(4);
        etIntroduction.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mActivity != null) {
                    mActivity.setSaveEnable(!StringUtil
                            .isNullOrEmpty(etIntroduction.getEditText()
                                    .getText().toString().trim()));
                }
            }
        });
    }

    private void fillUser() {
        user = DBMgr.getMgr().getUserDao().getSelfUser();
        if (canNext()) {
            if (!StringUtil.isNullOrEmpty(user.introduce)) {
                etIntroduction.setDefaultText(user.introduce);
                etIntroduction.getEditText().setSelection(
                        user.introduce.length() > EDIT_TEXT_COUNT ? EDIT_TEXT_COUNT : user.introduce.length());
            }
            mActivity.setSaveEnable(true);
        } else {
            mActivity.setSaveEnable(false);
        }
    }

    private boolean canNext() {
        return user != null && user.introduce != null
                && user.introduce.trim().length() > 0;
    }

    public void saveIntroduction() {
        String introduction = etIntroduction.getEditText().getText().toString();
        if (StringUtil.isNullOrEmpty(introduction)) {
            showToast("请输入自我介绍");
        } else {
            showProgressDlg();

            final User tmpUser = new User();
            tmpUser.uid = PrefUtil.Instance().getUserId();
            tmpUser.introduce = introduction;
            ZHApis.getUserApi().updateUser(getActivity(), tmpUser,
                    TaskUpdateUser.TYPE_UPDATE_OTHER,
                    new TaskCallback<Object>() {

                        @Override
                        public void onSuccess(Object content) {
                            showToast("自我介绍修改成功");
                            EventBus.getDefault()
                                    .post(new EBProfile(
                                            EBProfile.TYPE_CHANGE_INTRODUCTION));
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Throwable error) {
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            hideProgressDlg();
                        }
                    });
        }
    }
}