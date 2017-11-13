package com.zhisland.android.blog.feed.view.impl;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.feed.bean.RecommendData;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.android.blog.feed.model.IFeedListModel;
import com.zhisland.android.blog.feed.model.IFeedModel;
import com.zhisland.android.blog.feed.model.IIntrestableModel;
import com.zhisland.android.blog.feed.model.IRecommendModel;
import com.zhisland.android.blog.feed.model.ModelFactory;
import com.zhisland.android.blog.feed.presenter.FeedListPresenter;
import com.zhisland.android.blog.feed.presenter.FeedPresenter;
import com.zhisland.android.blog.feed.presenter.IntrestablePresenter;
import com.zhisland.android.blog.feed.presenter.RecommendPresenter;
import com.zhisland.android.blog.feed.view.IFeedListView;
import com.zhisland.android.blog.feed.view.IFeedView;
import com.zhisland.android.blog.feed.view.IIntrestableView;
import com.zhisland.android.blog.feed.view.IRecommendView;
import com.zhisland.android.blog.feed.view.impl.adapter.FeedAdapter;
import com.zhisland.android.blog.feed.view.impl.holder.FeedViewListener;
import com.zhisland.android.blog.feed.view.impl.holder.IntrestableHolder;
import com.zhisland.android.blog.feed.view.impl.holder.IntrestableListener;
import com.zhisland.android.blog.feed.view.impl.holder.RecommendHolder;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.tabhome.View.TitleFreshTaskView;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.DensityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arthur on 2016/8/31.
 */
public class FragFeedList extends FragMyPubFeedList<FeedListPresenter> implements IFeedListView, IFeedView, IRecommendView, IIntrestableView, FeedViewListener, IntrestableListener {

    private static final int TAG_LEFT_BTN = 1003;
    private static final int TAG_RIGHT_TV_BTN = 1004;

    private TitleFreshTaskView titleFreshTaskView;
    private TextView tvToast;

    private IntrestablePresenter intrestablePresenter;//可能感兴趣的人主导器
    private RecommendPresenter recommendPresenter;//推广为的presenter

    private RecommendHolder recommendHolder;
    private LinearLayout headerContainer;
    private IntrestableHolder intrestableHolder;


    //region 可能感兴趣的人implementation
    @Override
    public void updateIntrestable(List<RecommendUser> users) {
        intrestableHolder.update(users);
    }

    @Override
    public void removeRecommend(RecommendUser user) {
        intrestableHolder.removeRecommend(user);
    }

    @Override
    public void updateItem(RecommendUser inviteUser) {
        intrestableHolder.updateItem(inviteUser);
    }

    @Override
    public void hideIntrestView() {
        intrestableHolder.hideAll();
    }

    @Override
    public void showIntrestView() {
        intrestableHolder.showAll();
    }

    @Override
    public void gotoAllIntrest(List<RecommendUser> users) {
        FragFeedMorePeople.Invoke(getActivity(), users);
    }

    @Override
    public void onLoadSucess(List<RecommendUser> recommendUsers) {

    }

    //endregion


    //region Feed list View implementation
    @Override
    public void setRecommendData(RecommendData recommendData) {
        recommendPresenter.setData(recommendData);
    }

    @Override
    public void setIntrestableUsers(List<RecommendUser> intrestableUsers) {
        intrestablePresenter.setData(intrestableUsers);
    }

    @Override
    public void showPublishView() {
        ((TabHome) getActivity()).showActions();
    }

    @Override
    public void showFeedToast(String toastText) {
        tvToast.setVisibility(View.VISIBLE);
        tvToast.setText(toastText);
    }

    @Override
    public void hideFeedToast() {
        tvToast.setVisibility(View.GONE);
        tvToast.setText(null);
    }

    @Override
    public void showPermissionsDialog() {
        DialogUtil.showPermissionsDialog(getActivity(), getPageName());
    }

    //endregion


    //region recommend view implementation
    @Override
    public void updateRecommendView(RecommendData data) {
        if (headerContainer == null)
            return;

        if (data == null) {
            recommendHolder.root.setVisibility(View.GONE);
        } else {
            recommendHolder.root.setVisibility(View.VISIBLE);
            ImageWorkFactory.getFetcher().loadImage(data.imgrl, recommendHolder.ivRecommend);
            recommendHolder.ivRecommend.setTag(data);
        }
    }

    @Override
    public void gotoUri(String uri) {
        AUriMgr.instance().viewRes(getActivity(), uri);
    }
    //endregion


    //region 可能认识的人UI事件
    @Override
    public void onUserClicked(User user) {
        intrestablePresenter.onUserClicked(user);
    }

    @Override
    public void onFollowClicked(RecommendUser user) {
        intrestablePresenter.onFollowClicked(user);
    }

    @Override
    public void onIgnoreClicked(RecommendUser user) {
        intrestablePresenter.onIgnoreClicked(user);
    }

    @Override
    public void onRemoveAll() {
        intrestablePresenter.onRemoveAll();
    }

    @Override
    public void onViewAllClicked() {
        intrestablePresenter.onViewAllClicked();
    }
    //endregion


    //region 生命周期

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        FeedAdapter feedAdapter = new FeedAdapter(getActivity(), this);
        getPullProxy().setAdapter(feedAdapter);
    }

    @Override
    public void onOkClicked(String tag, Object arg) {
        feedPresenter.onDlgOkClicked(tag, arg);
    }

    @Override
    public void onNoClicked(String tag, Object arg) {
        feedPresenter.onDlgNoClicked(tag, arg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getPullProxy().getPullView().getRefreshableView().setFastScrollEnabled(false);

        headerContainer = new LinearLayout(getActivity());
        headerContainer.setOrientation(LinearLayout.VERTICAL);
        getPullProxy().getPullView().getRefreshableView().addHeaderView(headerContainer);

        View recommendView = View.inflate(getActivity(), R.layout.item_recommend, null);
        recommendHolder = new RecommendHolder(recommendView);
        headerContainer.addView(recommendView);
        recommendHolder.ivRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if (obj instanceof RecommendData) {
                    recommendPresenter.onRecommendClicked((RecommendData) obj);
                }
            }
        });
        recommendView.setVisibility(View.GONE);

        View intrestView = LayoutInflater.from(getActivity()).inflate(R.layout.item_intrestable, null);
        intrestableHolder = new IntrestableHolder(getActivity(), intrestView, this);
        headerContainer.addView(intrestView);
        intrestView.setVisibility(View.GONE);
        return view;
    }

    public void pageEnd() {
        //TODO
    }

    public void pageStart() {
        //TODO
    }

    public void refreshTitleFreshTask(boolean showFreshTask, boolean showRedDot) {
        if (titleFreshTaskView != null) {
            titleFreshTaskView.refreshTitleFreshTask(showFreshTask, showRedDot);
        }
    }


    //endregion


    //region 重载方法

    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(3);

        feedListPresenter = new FeedListPresenter();
        IFeedListModel feedListModel = ModelFactory.CreateFeedListModel();
        feedListPresenter.setModel(feedListModel);
        presenterMap.put(FeedListPresenter.class.getSimpleName(), feedListPresenter);

        feedPresenter = new FeedPresenter();
        IFeedModel feedModel = ModelFactory.CreateFeedModel();
        feedPresenter.setModel(feedModel);
        presenterMap.put(FeedPresenter.class.getSimpleName(), feedPresenter);

        recommendPresenter = new RecommendPresenter();
        IRecommendModel model = ModelFactory.createRecommendModel();
        recommendPresenter.setModel(model);
        presenterMap.put(RecommendPresenter.class.getSimpleName(), recommendPresenter);

        intrestablePresenter = new IntrestablePresenter();
        IIntrestableModel intrestableModel = ModelFactory.createIntrestableModel();
        intrestablePresenter.setModel(intrestableModel);
        presenterMap.put(IntrestablePresenter.class.getSimpleName(), intrestablePresenter);

        return presenterMap;
    }

    @Override
    protected View createDefaultFragView() {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.frag_feed_list, null);
        configTitle(view);
        tvToast = (TextView) view.findViewById(R.id.tvFeedListToast);
        return view;
    }


    /**
     * 初始化 活动title
     */
    private void configTitle(View rootView) {
        TitleBarProxy titleBar = new TitleBarProxy();
        titleBar.configTitle(rootView, TitleType.TITLE_LAYOUT,
                new DefaultTitleBarClickListener(getActivity()) {
                    @Override
                    public void onTitleClicked(View view, int tagId) {
                        switch (tagId) {
                            case TAG_RIGHT_TV_BTN:
                                feedListPresenter.onPublishClicked();
                                break;
                            case TAG_LEFT_BTN:
                                //跳转任务列表
                                BusFreshTask.Bus().post(new EventTitleClick());
                                break;
                        }
                    }
                });
        titleBar.setTitle("商圈");

        View tvRight = TitleCreator.Instance().createImageButton(getActivity(), R.drawable.bg_nav_add);
        int padding = DensityUtil.dip2px(10);
        tvRight.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleBar.addRightTitleButton(tvRight, TAG_RIGHT_TV_BTN, param);
        titleFreshTaskView = new TitleFreshTaskView(getActivity());
        titleFreshTaskView.addLeftTitle(titleBar, TAG_LEFT_BTN);

    }

    @Override
    public void loadNormal() {
        feedListPresenter.onPullDown();
    }

    @Override
    public void loadMore(String nextId) {
        feedListPresenter.onPullUp(nextId);
    }

    @Override
    public String getPageName() {
        return "FeedList";
    }


    //endregion


}
