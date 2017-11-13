package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.freshtask.model.impl.IntroductionModel;
import com.zhisland.android.blog.freshtask.presenter.IntroductionPresenter;
import com.zhisland.android.blog.freshtask.view.IIntroductionView;
import com.zhisland.android.blog.freshtask.view.impl.holder.IntroductionHolder;
import com.zhisland.android.blog.profile.controller.FragProfileIntroduction;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 新手任务 个人简介
 */
public class FragIntroduction extends FragBaseMvp<IntroductionPresenter> implements IIntroductionView {

    private static final int TAG_ID_RIGHT = 101;

    private IntroductionHolder viewHolder = new IntroductionHolder();

    private AProgressDialog progressDialog;

    @Override
    protected String getPageName() {
        return "RookieTaskSelfIntro";
    }

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragIntroduction.class;
        param.titleBtns = new ArrayList<>();
        param.enableBack = true;
        param.runnable = titleRunnable;
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "保存";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.white);
        param.titleBtns.add(tb);
        param.title = "打造你的商界形象";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }


    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (tagId == TAG_ID_RIGHT) {
                if (fragment != null && fragment instanceof FragIntroduction) {
                    ((FragIntroduction) fragment).presenter().saveIntroduction();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_introduction, container, false);
        ButterKnife.inject(viewHolder, root);
        return root;
    }

    @Override
    public void initViews() {
        //FragProfileIntroduction中英文占1位,保持一致,最大字数200
        EditTextUtil.limitEditTextLength(viewHolder.etContent,
                FragProfileIntroduction.EDIT_TEXT_COUNT, viewHolder.tvCount);
    }

    @Override
    public String getContent() {
        return viewHolder.etContent.getText().toString().trim();
    }

    @Override
    public void showProgressDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        progressDialog.show();
        progressDialog.setText(content);
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void finishSelf() {
        getActivity().finish();
    }


    @Override
    protected IntroductionPresenter createPresenter() {
        IntroductionPresenter presenter = new IntroductionPresenter();
        IntroductionModel model = new IntroductionModel();
        presenter.setModel(model);
        return presenter;
    }
}
