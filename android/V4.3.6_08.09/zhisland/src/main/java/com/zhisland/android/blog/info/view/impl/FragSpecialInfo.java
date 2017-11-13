package com.zhisland.android.blog.info.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.info.view.ISpecialInfo;
import com.zhisland.android.blog.info.model.impl.SpecialInfoModel;
import com.zhisland.android.blog.info.presenter.SpecialInfoPresenter;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 资讯专栏页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragSpecialInfo extends FragBaseMvp<SpecialInfoPresenter> implements ISpecialInfo {

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationSpecilList";

    @Override
    protected SpecialInfoPresenter createPresenter() {
        SpecialInfoPresenter presenter = new SpecialInfoPresenter();
        SpecialInfoModel model = new SpecialInfoModel();
        presenter.setModel(model);
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_special_info, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }

    public void pageStart() {
        ZhislandApplication.trackerPageStart(getPageName());
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.PAGE_NEWS_SPECIAL);
    }

    public void pageEnd() {
        ZhislandApplication.trackerPageEnd(getPageName());
    }
}
