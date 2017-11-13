package com.zhisland.android.blog.info.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资讯推荐引导页面
 * Created by Mr.Tui on 2016/7/3.
 */
public class FragRecommendGuide extends FragBase {

    private static final String INTENT_KEY_IS_FIRST = "intent_key_is_first";

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationAddLinkHelp";

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragRecommendGuide.class;
        param.title = "发布";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    public static void invokeFirst(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragRecommendGuide.class;
        param.title = "发布";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_IS_FIRST, true);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_recommend_guide, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @OnClick(R.id.tvNext)
    void nextClick() {
        boolean isFirst = getActivity().getIntent().getBooleanExtra(INTENT_KEY_IS_FIRST, false);
        if (isFirst) {
            FragLinkEdit.TryInvoke(getActivity());
        }
        getActivity().finish();
    }

}
