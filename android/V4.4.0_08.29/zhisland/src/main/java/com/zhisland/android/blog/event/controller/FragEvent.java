package com.zhisland.android.blog.event.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.common.view.banner.BannerView;
import com.zhisland.android.blog.event.controller.view.EventFilterUtil;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.EventListStruct;
import com.zhisland.android.blog.event.dto.EventSpread;
import com.zhisland.android.blog.event.eb.EBEvent;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.tabhome.View.TitleFreshTaskView;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.pulltorefresh.PullEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 活动列表页面
 */
public class FragEvent extends FragPullList<Event> implements EventFilterUtil.EventFilterListener {

    public static final String PAGE_NAME = "EventList";
    private static final String CACHE_EVENT_SPREAD = "CACHE_EVENT_SPREAD";
    private static final String KEY_LAST_GET_DATA_TIME = "KEY_LAST_GET_DATA_TIME";

    private static final int TAG_LEFT_BTN = 1003;
    private static final int TAG_RIGHT_TV_BTN = 1004;

    // 新手任务title
    private TitleFreshTaskView titleFreshTaskView;
    // banner 轮播器
    private BannerView bannerView;
    // 筛选 view
    private View filterView;
    // 活动筛选 util
    private EventFilterUtil filterUtil;
    // 查看往期活动
    private View viewPastEvent;
    // 是否已经展示了往期活动
    private boolean isShowPast = false;
    // 是否在点击了往期活动的拉取状态，用于防止 loadMore的bug
    private boolean isClickPastState = false;
    // 当前活动的maxId，用于点击往期活动传给服务器用
    private String currentEventMaxId;
    // 活动推广
    private ArrayList<EventSpread> spreads;

    /**
     * 打开活动列表页
     *
     * @param context
     */
    public static void Invoke(Context context) {
        CommonFragActivity.CommonFragParams params = new CommonFragActivity.CommonFragParams();
        params.clsFrag = FragEvent.class;
        params.title = "活动";
        params.enableBack = true;
        CommonFragActivity.invoke(context, params);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setAdapter(new EventListAdapter(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(
                R.layout.frag_tab_item, container, false);
        rootView.addView(super
                        .onCreateView(inflater, container, savedInstanceState),
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
        configTitle(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);

        internalView.setFastScrollEnabled(false);
        internalView.setDividerHeight(0);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        internalView.setBackgroundColor(getResources().getColor(
                R.color.white));
        getPullProxy().getPullView().setBackgroundColor(
                getResources().getColor(R.color.color_bg2));

        filterUtil = new EventFilterUtil(getActivity(), this);
        spreads = (ArrayList<EventSpread>) DBMgr.getMgr().getCacheDao().get(CACHE_EVENT_SPREAD);
        addBannerView();
        addFilterView();
        initPastView();

        EventListStruct struct = (EventListStruct) DBMgr.getMgr().getCacheDao().get(getCacheKey());
        dispatchResult(struct, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkNeedLoadData();
        if (bannerView != null) {
            bannerView.startTurning();
        }
    }

    /**
     * 检测是否需要刷新列表，半个小时自动刷新机制
     */
    private void checkNeedLoadData() {
        long lastLoadDataTime = PrefUtil.Instance().getByKey(KEY_LAST_GET_DATA_TIME, System.currentTimeMillis());
        boolean beyondHalfHour = TimeUtil.isBeyondHalfHour(lastLoadDataTime);
        if (beyondHalfHour) {
            getEventListTask(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.stopTurning();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadNormal() {
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUSH_DOWN);
        getEventListTask(null);
        getEventSpreadTask();
    }

    @Override
    public void loadMore(String nextId) {
        super.loadMore(nextId);
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUSH_UP);
        if (!isClickPastState) {
            if (isShowPast) {
                getPastEventListTask(nextId, false);
            } else {
                getEventListTask(nextId);
            }
        }
    }

    /**
     * 获取活动推广 task
     */
    private void getEventSpreadTask() {
        ZHApis.getEventApi().getEventSpread(getActivity(), new TaskCallback<ArrayList<EventSpread>>() {
            @Override
            public void onSuccess(ArrayList<EventSpread> content) {
                spreads = content;
                DBMgr.getMgr().getCacheDao().set(CACHE_EVENT_SPREAD, content);
                addBannerView();
                addFilterView();
            }

            @Override
            public void onFailure(Throwable error) {
            }
        });
    }

    /**
     * 获取活动列表
     */
    public void getEventListTask(final String nextId) {
        ZHApis.getEventApi().getEventList(getActivity(), filterUtil.getCurEventType(),
                filterUtil.getCurEventTag(), filterUtil.getCurOrderId(), nextId,
                new TaskCallback<EventListStruct>() {

                    @Override
                    public void onSuccess(EventListStruct content) {
                        if (nextId == null) {
                            getPullProxy().getAdapter().clearItems();
                            DBMgr.getMgr().getCacheDao().set(getCacheKey(), content);
                        }
                        dispatchResult(content, false);
                        PrefUtil.Instance().setKeyValue(KEY_LAST_GET_DATA_TIME, System.currentTimeMillis());
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        getPullProxy().onRefreshFinished();
                    }
                });
    }

    /**
     * 获取往期活动列表
     */
    private void getPastEventListTask(String maxId, final boolean isClickPast) {
        ZHApis.getEventApi().getPastEventList(getActivity(), filterUtil.getCurEventType(),
                filterUtil.getCurEventTag(), maxId,
                new TaskCallback<EventListStruct>() {

                    @Override
                    public void onSuccess(EventListStruct content) {
                        dispatchResult(content, isClickPast);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        getPullProxy().onRefreshFinished();
                        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        isClickPastState = false;
                    }
                });
    }

    /**
     * 分发处理活动列表接口返回数据
     * <p/>
     * 1. 有当前活动且有往期活动时 显示当前活动列表，如果当前活动 pastIsLast = true ,则显示 “查看往期活动”footerView，点击拉取往期活动列表
     * 2. 有当前活动且无往期活动时，只显示当前活动列表
     * 3. 无当前活动且有往期活动时，显示当前活动为空的view,且自动显示往期活动列表
     * 4. 无当前活动且无往期活动时，显示无活动的EmptyView
     *
     * @param isClickPast 是否为点击往期活动
     */
    private void dispatchResult(EventListStruct content, boolean isClickPast) {
        if (content != null) {
            // 当前活动列表
            ZHPageData<Event> currentEvents = content.current;
            // 往期活动列表
            ZHPageData<Event> pastEvents = content.past;

            if (content.isHasCurrentEvent()) {
                // 有当前活动且有往期活动
                getPullProxy().onLoadSucessfully(currentEvents);
                // 如果当前活动 pastIsLast = true ,则显示 “查看往期活动”footerView
                if (currentEvents.pageIsLast) {
                    FragEvent.this.currentEventMaxId = currentEvents.nextId;
                    showPastEventView();
                } else {
                    hidePastEventView();
                }
                isShowPast = false;
            } else if (!content.isHasCurrentEvent() && content.isHasPastEvent()) {
                // 无当前活动且有往期活动
                if (getPullProxy().getAdapter().getData() == null || getPullProxy().getAdapter().getData().size() == 0) {
                    pastEvents.data.add(0, getTxtPastEventData());
                    pastEvents.data.add(0, getCurrentEventEmptyData());
                } else if (isClickPast) {
                    pastEvents.data.add(0, getTxtPastEventData());
                    getPullProxy().setCurrentEvent(PullEvent.more);
                }
                getPullProxy().onLoadSucessfully(pastEvents);
                hidePastEventView();
                isShowPast = true;
            } else if (!content.isHasCurrentEvent() && !content.isHasPastEvent()) {
                // 无当前活动且无往期活动
                showEventEmptyView();
                hidePastEventView();
                isShowPast = false;
            }
        } else {
            showEventEmptyView();
            hidePastEventView();
        }
    }

    @Override
    public void onClickFilterListener() {
        internalView.smoothScrollToPosition(0);
        getPullProxy().getAdapter().clearItems();
        getPullProxy().getAdapter().notifyDataSetChanged();
        EventListStruct struct = (EventListStruct) DBMgr.getMgr().getCacheDao().get(getCacheKey());
        dispatchResult(struct, false);
        getPullProxy().getPullView().setRefreshing(true);
        getEventListTask(null);
    }

    public void onEventMainThread(EBEvent eb) {
        switch (eb.getType()) {
            case EBEvent.TYPE_EVENT_CREATE:
                getEventListTask(null);
                break;
            case EBEvent.TYPE_EVENT_SIGN_UP:
                List<Event> list = getPullProxy().getAdapter().getData();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).eventId == eb.getEvent().eventId) {
                        list.get(i).signedCount += 1;
                        list.get(i).signedUsers = eb.getEvent().signedUsers;
                        break;
                    }
                }
                getPullProxy().getAdapter().setData(list);
                getPullProxy().getAdapter().notifyDataSetChanged();
                break;
        }
    }

    /**
     * 添加往期活动 TXT data
     */
    private Event getTxtPastEventData() {
        Event event = new Event();
        event.localType = EventListAdapter.TYPE_TXT_PAST_EVENT;
        return event;
    }

    /**
     * 添加 当前活动空白 data
     */
    private Event getCurrentEventEmptyData() {
        Event event = new Event();
        event.localType = EventListAdapter.TYPE_CURRENT_EMPTY_VIEW;
        return event;
    }

    /**
     * 添加 活动空白页
     */
    private void showEventEmptyView() {
        List<Event> datas = new ArrayList<>();
        Event event = new Event();
        event.localType = EventListAdapter.TYPE_EVENT_EMPTY_VIEW;
        datas.add(event);
        getPullProxy().onLoadSucessfully(datas);
    }

    /**
     * 初始化 活动title
     */
    private void configTitle(LinearLayout rootView) {
        TitleBarProxy titleBar = new TitleBarProxy();
        titleBar.configTitle(rootView, TitleType.TITLE_LAYOUT,
                new DefaultTitleBarClickListener(getActivity()) {
                    @Override
                    public void onTitleClicked(View view, int tagId) {
                        switch (tagId) {
                            case TAG_RIGHT_TV_BTN:
                                ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUBLISH_SEND);
                                if (PermissionsMgr.getInstance().canEventPublish()) {
                                    ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUBLISH_START);
                                    ActEventCreate.invoke(getActivity(), null,-1);
                                } else {
                                    DialogUtil.showPermissionsDialog(getActivity(), getPageName());
                                }
                                break;
                            case TAG_LEFT_BTN:
                                //跳转任务列表
                                BusFreshTask.Bus().post(new EventTitleClick());
                                break;
                        }
                    }
                });
        titleBar.setTitle("活动");

        TextView tvRight = TitleCreator.Instance().createTextButton(getActivity(), "发布", R.color.color_dc);
        titleBar.addRightTitleButton(tvRight, TAG_RIGHT_TV_BTN);
        titleFreshTaskView = new TitleFreshTaskView(getActivity());
        titleFreshTaskView.addLeftTitle(titleBar, TAG_LEFT_BTN);
    }

    /**
     * 添加 bannerView
     */
    private void addBannerView() {
        pullView.getRefreshableView().removeHeaderView(bannerView);
        if (spreads != null && spreads.size() > 0) {
            if (bannerView == null) {
                bannerView = new BannerView(getActivity());
                int bannerHeight = DensityUtil.getWidth() * 9 / 16;
                bannerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, bannerHeight));
                bannerView.setPageIndicatorPadding(DensityUtil.dip2px(5));
                bannerView.setPageIndicator(new int[]{R.drawable.bg_circle_white_60, R.drawable.bg_circle_white});
            }
            bannerView.setPages(new BannerView.BannerHolder<EventSpread>() {
                private ImageView imageView;

                @Override
                public View createView(Context context) {
                    imageView = new ImageView(context);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return imageView;
                }

                @Override
                public void UpdateUI(Context context, int position, EventSpread data) {
                    ImageWorkFactory.getFetcher().loadImage(data.imgUrl, imageView, R.drawable.img_info_default_pic);
                }
            }, spreads);
            bannerView.setOnItemClickListener(new BannerView.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ActEventDetail.invoke(getActivity(), spreads.get(position).eventId, false);
                }
            });
            pullView.getRefreshableView().addHeaderView(bannerView);
        }
    }

    /**
     * 添加 活动筛选 view
     */
    private void addFilterView() {
        if (filterView == null) {
            filterView = filterUtil.getFilterView();
        }
        pullView.getRefreshableView().removeHeaderView(filterView);
        pullView.getRefreshableView().addHeaderView(filterView);
    }

    /**
     * 初始化往期活动 view
     */
    private void initPastView() {
        viewPastEvent = LayoutInflater.from(getActivity()).inflate(R.layout.past_event, null);
        viewPastEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClickPastState = true;
                getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                getPullProxy().getPullView().setRefreshing(true);
                getPastEventListTask(FragEvent.this.currentEventMaxId, true);
            }
        });
    }

    /**
     * 添加查看往期活动 view
     */
    private void showPastEventView() {
        hidePastEventView();
        pullView.getRefreshableView().addFooterView(viewPastEvent);
    }

    /**
     * 移除查看往期活动 view
     */
    private void hidePastEventView() {
        pullView.getRefreshableView().removeFooterView(viewPastEvent);
    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    public String getCacheKey() {
        return PAGE_NAME + filterUtil.getCurEventType() + filterUtil.getCurEventTag() + filterUtil.getCurOrderId();
    }

    public void pageStart() {
        ZhislandApplication.trackerPageStart(getPageName());
    }

    public void pageEnd() {
        ZhislandApplication.trackerPageEnd(getPageName());
    }
}