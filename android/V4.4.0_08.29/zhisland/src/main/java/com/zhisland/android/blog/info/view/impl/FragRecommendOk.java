package com.zhisland.android.blog.info.view.impl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.lib.component.frag.FragBase;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推荐成功页面
 */
public class FragRecommendOk extends FragBase {

    private static final int TAG_ID_RIGHT = 100;

    @Override
    public String getPageName() {
        return "InformationAddLinkResult";
    }

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragRecommendOk.class;
        param.title = "发布";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "如何推荐？";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            FragRecommendGuide.invoke(fragment.getActivity());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_recommend_ok, null);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @OnClick(R.id.btnContinue)
    void continueClick() {

    }

    @OnClick(R.id.tvSucBack)
    void backClick() {

    }
}
