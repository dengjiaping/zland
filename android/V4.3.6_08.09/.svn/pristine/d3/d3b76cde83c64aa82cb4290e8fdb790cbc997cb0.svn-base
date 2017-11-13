package com.zhisland.android.blog.info.view.impl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.umeng.analytics.MobclickAgent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.RecommendInfoModel;
import com.zhisland.android.blog.info.presenter.RecommendInfoPresenter;
import com.zhisland.android.blog.info.view.IRecommendInfo;
import com.zhisland.android.blog.info.view.impl.adapter.RecommendInfoAdapter;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.mvp.view.FragPullListMvp;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 资讯推荐列表页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragRecommendInfo extends FragPullListMvp<ZHInfo, RecommendInfoPresenter> implements IRecommendInfo {

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationRecommendList";

    private RelativeLayout rlContainer;
    private TextView promptText;
    private boolean isShowing = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        getPullProxy().setAdapter(new RecommendInfoAdapter());
        getPullProxy().setRefreshKey(
                PAGE_NAME + PrefUtil.Instance().getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rlContainer = new RelativeLayout(getActivity());
        View pullView = super.onCreateView(inflater, container,
                savedInstanceState);
        rlContainer.addView(pullView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlContainer.setLayoutParams(lp);
        return rlContainer;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter().cacheData();
    }

    public void onParentFocused() {
        if (isShowing) {
            presenter().onFocused();
        }
    }

    @Override
    protected RecommendInfoPresenter createPresenter() {
        RecommendInfoPresenter presenter = new RecommendInfoPresenter();
        RecommendInfoModel model = new RecommendInfoModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setDividerHeight(0);
        internalView.setFastScrollEnabled(false);
        internalView.setBackgroundColor(getResources().getColor(
                R.color.color_bg2));
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pullView.setPullLabel("下拉更新", PullToRefreshBase.Mode.PULL_FROM_START);
        pullView.setRefreshingLabel("正在刷新", PullToRefreshBase.Mode.PULL_FROM_START);
        pullView.setReleaseLabel("松开推荐", PullToRefreshBase.Mode.PULL_FROM_START);
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
        presenter().loadNormal();
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_RECOMMEND_PUSH_DOWN);
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        presenter().loadMore(nextId);
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_NEWS_RECOMMEND_PUSH_UP);
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
    public void cacheListData() {
        getPullProxy().saveCacheData();
    }

    @Override
    public void showPrompt(String Content) {
        if (promptText == null) {
            promptText = new TextView(getActivity());
            promptText.setGravity(Gravity.CENTER);
            promptText.setBackgroundColor(Color.parseColor("#b2dbb786"));
            DensityUtil.setTextSize(promptText, R.dimen.txt_12);
            promptText.setTextColor(getResources().getColor(R.color.color_bg1));
            rlContainer.addView(promptText, RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(20));
        }
        promptText.setText(Content);
        promptText.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePrompt() {
        promptText.setVisibility(View.GONE);
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
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.PAGE_NEWS_RECOMMEND);
    }

    public void pageEnd() {
        isShowing = false;
        ZhislandApplication.trackerPageEnd(getPageName());
    }
}
