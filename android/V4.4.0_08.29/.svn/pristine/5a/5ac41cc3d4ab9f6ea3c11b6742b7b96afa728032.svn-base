package com.zhisland.android.blog.info.view.impl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.FeaturedInfoModel;
import com.zhisland.android.blog.info.presenter.FeaturedInfoPresenter;
import com.zhisland.android.blog.info.view.IFeaturedInfo;
import com.zhisland.android.blog.info.view.impl.adapter.FeaturedInfoAdapter;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.FragPullListMvp;

import java.util.List;

/**
 * 资讯精选列表页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragFeaturedInfo extends FragPullListMvp<ZHInfo, FeaturedInfoPresenter> implements IFeaturedInfo {

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationDailyList";

    private boolean isShowing = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        getPullProxy().setAdapter(new FeaturedInfoAdapter());
        getPullProxy().setRefreshKey(PAGE_NAME + PrefUtil.Instance().getUserId());
    }

    public void onParentFocused() {
        if (isShowing) {
            presenter().onFocused();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getPullProxy().saveCacheData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setDividerHeight(0);
        internalView.setFastScrollEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.color_bg2));
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pullView.setLoadingDrawable(getResources().getDrawable(R.drawable.img_logo_download), PullToRefreshBase.Mode.PULL_FROM_START);
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));

        EmptyView ev = new EmptyView(getActivity());
        ev.setImgRes(R.drawable.img_information_empty);
        ev.setPrompt("暂时还没有资讯哦");
        ev.setBtnVisibility(View.INVISIBLE);
        getPullProxy().getPullView().setEmptyView(ev);

    }

    @Override
    public void loadNormal() {
        presenter().getDataFromInternet(null);
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_REFINE_PUSH_DOWN);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        presenter().getDataFromInternet(nextId);
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_REFINE_PUSH_UP);
    }

    @Override
    protected FeaturedInfoPresenter createPresenter() {
        FeaturedInfoPresenter presenter = new FeaturedInfoPresenter();
        FeaturedInfoModel model = new FeaturedInfoModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void onloadSuccess(ZHPageData<ZHInfo> data) {
        getPullProxy().onLoadSucessfully(data);
    }

    @Override
    public void onLoadFailed(Throwable error) {
        getPullProxy().onLoadFailed(error);
    }

    @Override
    public void pullDownToRefresh(boolean showRefreshingHeader) {
        getPullProxy().pullDownToRefresh(showRefreshingHeader);
    }

    @Override
    public void refreshListView() {
        getPullProxy().getAdapter().notifyDataSetChanged();
    }

    @Override
    public List<ZHInfo> getCurListData() {
        return getPullProxy().getAdapter().getData();
    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }

    public void pageStart() {
        isShowing = true;
        if (isResumed()) {
            presenter().onFocused();
        }
        ZhislandApplication.trackerPageStart(getPageName());
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.PAGE_NEWS_REFINE);
    }

    public void pageEnd() {
        isShowing = false;
        ZhislandApplication.trackerPageEnd(getPageName());
    }

}
