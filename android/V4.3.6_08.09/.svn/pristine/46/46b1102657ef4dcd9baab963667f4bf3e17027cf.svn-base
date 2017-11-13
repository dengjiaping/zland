package com.zhisland.android.blog.info.view.impl;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.info.model.impl.InfoHomeTabModel;
import com.zhisland.android.blog.info.presenter.InfoHomeTabPresenter;
import com.zhisland.android.blog.info.view.IInfoHomeTab;
import com.zhisland.android.blog.info.view.impl.holder.AddLinkPrompt;
import com.zhisland.android.blog.tabhome.View.TitleFreshTaskView;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.SwipeView;
import com.zhisland.lib.view.tab.TabBarOnCreateListener;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.TabBarViewListener;
import com.zhisland.lib.view.tab.ZHTabInfo;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 资讯tab
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragInfoHomeTab extends FragBaseMvp<InfoHomeTabPresenter> implements IInfoHomeTab, TabBarViewListener, TabBarOnCreateListener, SwipeView.OnPageChangedListener {

    private static final int TAG_LEFT_BTN = 111;
    private static final int TAG_RIGHT_IV_BTN = 222;

    private static final int TAB_ID_RECOMMEND = 0;
    private static final int TAB_ID_FEATURED = 1;
    private static final int TAB_ID_SPECIAL = 2;

    private FragFeaturedInfo fragFeaturedInfo;
    private FragRecommendInfo fragRecommendInfo;
    private FragSpecialInfo fragSpecialInfo;

    private TabBarView tabBar;
    private TitleFreshTaskView titleFreshTaskView;
    private AddLinkPrompt promptHolder;

    @InjectView(R.id.rlBody)
    RelativeLayout rlBody;
    @InjectView(R.id.tab_container)
    SwipeView lvContainer;

    private boolean pageSelected = false;
    private int curIndex = 0;
    private int tabCount;

    private ArrayList<ZHTabInfo> tabs;
    private Fragment[] pages = null;
    private HashMap<ZHTabInfo, View> pageHolders;// 当启用lazyload的时候，用来占位


    @Override
    protected String getPageName() {
        return "InfomationList";
    }

    @Override
    protected InfoHomeTabPresenter createPresenter() {
        InfoHomeTabPresenter InfoHomeTabPresenter = new InfoHomeTabPresenter();
        InfoHomeTabModel model = new InfoHomeTabModel();
        InfoHomeTabPresenter.setModel(model);
        return InfoHomeTabPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout root = (LinearLayout) inflater.inflate(
                R.layout.frag_tab_info, container, false);
        ButterKnife.inject(this, root);
        initTabBar();
        initTitle(root);
        initPageViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && pageSelected) {
            onFocused();
        }
    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }

    private void initTabBar() {
        tabBar = new TabBarView(getActivity());
        tabBar.setBottomIndicator(getResources().getDrawable(R.drawable.img_info_tab_bottom));
        tabBar.setListener(this);
        tabBar.setCreateListener(this);
        tabs = this.tabToAdded();
        tabBar.setTabs(tabs);
        tabCount = tabs.size();
    }

    private void initTitle(View root) {

        TitleBarProxy titleBar = new TitleBarProxy();
        titleBar.configTitle(root, TitleType.TITLE_LAYOUT, new DefaultTitleBarClickListener(getActivity()) {
            @Override
            public void onTitleClicked(View view, int tagId) {
                switch (tagId) {
                    case TAG_LEFT_BTN:
                        presenter().onFreshTaskClick();
                        break;
                    case TAG_RIGHT_IV_BTN:
                        presenter().onAddLinkClick();
                        break;
                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(DensityUtil.dip2px(80), 0, DensityUtil.dip2px(80), 0);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout) titleBar.getRootView()).addView(tabBar, lp);
        ImageView ivRightBtn = TitleCreator.Instance().createImageButton(getActivity(), R.drawable.bg_nav_add);
        titleBar.addRightTitleButton(ivRightBtn, TAG_RIGHT_IV_BTN);
        titleFreshTaskView = new TitleFreshTaskView(getActivity());
        titleFreshTaskView.addLeftTitle(titleBar, TAG_LEFT_BTN);
    }

    private void initPageViews() {
        lvContainer.setOnPageChangedListener(this);
        pages = new Fragment[tabCount];
        pageHolders = new HashMap<ZHTabInfo, View>(tabCount);

        boolean hasLoadFirstPage = false;
        for (int i = 0, size = tabs.size(); i < size; i++) {
            ZHTabInfo info = tabs.get(i);
            int containerId = getResources().getIdentifier("info_tab_" + i, "id",
                    getActivity().getPackageName());
            LinearLayout singlePageContainer = new LinearLayout(getActivity());
            singlePageContainer.setBackgroundColor(Color.TRANSPARENT);
            singlePageContainer.setId(containerId);
            pageHolders.put(info, singlePageContainer);
            lvContainer.addView(singlePageContainer);

            if (!hasLoadFirstPage) {
                hasLoadFirstPage = true;
                tryLoadPage(i);
            }
        }
    }

    public void onPageSelected() {
        pageSelected = true;
        if (!isHidden() && isResumed()) {
            onFocused();
        }
    }

    public void onPageUnSelected() {
        pageSelected = false;
    }

    private void onFocused() {
        if (fragFeaturedInfo != null) {
            fragFeaturedInfo.onParentFocused();
        }
        if (fragRecommendInfo != null) {
            fragRecommendInfo.onParentFocused();
        }
        presenter().onFocused();
    }

    public void pageStart() {
        setTabOnPageStart(tabBar.getSelectedIndex());
    }

    public void pageEnd() {
        setTabOnPageEnd(tabBar.getSelectedIndex());
    }

    private void setTabOnPageStart(int tabId) {
        switch (tabId) {
            case TAB_ID_RECOMMEND:
                fragRecommendInfo.pageStart();
                break;
            case TAB_ID_FEATURED:
                fragFeaturedInfo.pageStart();
                break;
            case TAB_ID_SPECIAL:
                fragSpecialInfo.pageStart();
                break;
        }
    }

    private void setTabOnPageEnd(int tabId) {
        switch (tabId) {
            case TAB_ID_RECOMMEND:
                fragRecommendInfo.pageEnd();
                break;
            case TAB_ID_FEATURED:
                fragFeaturedInfo.pageEnd();
                break;
            case TAB_ID_SPECIAL:
                fragSpecialInfo.pageEnd();
                break;
        }
    }

    /**
     * 刷新新手任务title
     */
    public void refreshTitleFreshTask(boolean showFreshTask, boolean showRedDot) {
        if (titleFreshTaskView != null) {
            titleFreshTaskView.refreshTitleFreshTask(showFreshTask, showRedDot);
        }
    }

    //--------------tab and swipeView start---------------

    protected ArrayList<ZHTabInfo> tabToAdded() {
        ArrayList<ZHTabInfo> tabs = new ArrayList<ZHTabInfo>();
        ZHTabInfo tabInfo = new ZHTabInfo("推荐", TAB_ID_RECOMMEND);
        tabs.add(tabInfo);
        tabInfo = new ZHTabInfo("精选", TAB_ID_FEATURED);
        tabs.add(tabInfo);
        tabInfo = new ZHTabInfo("专栏", TAB_ID_SPECIAL);
        tabs.add(tabInfo);
        return tabs;
    }

    @Override
    public boolean shouldSelectTab(TabBarView view, ZHTabInfo tab, int atIndex) {
        return true;
    }

    @Override
    public void didSelectTabBar(TabBarView view, ZHTabInfo tab, int atIndex) {
        selectPage(atIndex, false);
    }

    @Override
    public View createTabView(TabBarView view, ZHTabInfo tab, int atIndex) {
        TextView textView = new TextView(getActivity());
        DensityUtil.setTextSize(textView, R.dimen.txt_16);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.color_f3));
        textView.setText(tab.name);
        return textView;
    }

    @Override
    public void selectTabView(View view, ZHTabInfo tabInfo) {
        if (view != null && view instanceof TextView) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.color_f1));
        }
    }

    @Override
    public void unSelectTabView(View view) {
        if (view != null && view instanceof TextView) {
            ((TextView) view).setTextColor(getResources().getColor(R.color.color_f3));
        }
    }

    //-------------- tab end ---------------

    //-------------- swipeView start ---------------

    private void tryLoadPage(int index) {

        if (pages[index] != null)
            return;

        ZHTabInfo tabInfo = tabs.get(index);
        pages[index] = this.createTabPage(tabInfo);
        View pageContainer = pageHolders.remove(tabInfo);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(pageContainer.getId(), pages[index]);
        ft.commit();
    }

    protected Fragment createTabPage(ZHTabInfo tabInfo) {
        switch (tabInfo.tabId) {
            case TAB_ID_RECOMMEND: {
                fragRecommendInfo = new FragRecommendInfo();
                return fragRecommendInfo;
            }
            case TAB_ID_FEATURED: {
                fragFeaturedInfo = new FragFeaturedInfo();
                return fragFeaturedInfo;
            }
            case TAB_ID_SPECIAL: {
                fragSpecialInfo = new FragSpecialInfo();
                return fragSpecialInfo;
            }
        }
        return null;
    }

    @Override
    public boolean shouldSelect(int oldPage, int newPage) {
        return true;
    }

    @Override
    public void onPageChanged(int oldPage, int newPage) {
        selectPage(newPage, true);
    }

    private void selectPage(final int index, final boolean shouldScrollTabBar) {

        handler.post(new Runnable() {

            @Override
            public void run() {

                if (shouldScrollTabBar) {
                    tabBar.setSelectedIndex(index, true, false);
                } else {
                    lvContainer.smoothScrollToPage(index);
                }
            }
        });

        long delayed = shouldScrollTabBar ? 250 : 0;

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                int indexBefor = curIndex;

                curIndex = index;
                tryLoadPage(index);
                didSelectTab(tabs.get(index), indexBefor < 0 ? null : tabs.get(indexBefor));
            }
        }, delayed);
    }

    private void didSelectTab(ZHTabInfo tab, ZHTabInfo tabBefor) {
        if (tabBefor != null) {
            setTabOnPageEnd(tabBefor.tabId);
        }
        setTabOnPageStart(tab.tabId);
    }

    //-------------- swipeView end ---------------

    //----------- IInfoHomeTab start ---------------

    @Override
    public void showPrompt(String url) {
        if (promptHolder == null) {
            View llPrompt = LayoutInflater.from(getActivity()).inflate(R.layout.view_link_pop, null);
            promptHolder = new AddLinkPrompt(llPrompt);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(120), RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.setMargins(0, DensityUtil.dip2px(3), DensityUtil.dip2px(3), 0);
            rlBody.addView(llPrompt, lp);
        }
        promptHolder.show(url);
    }

    @Override
    public void hidePrompt() {
        promptHolder.hide();
    }

    @Override
    public String getClipText() {
        return StringUtil.getClipText(getActivity());
    }

    @Override
    public void gotoFreshTask() {
        BusFreshTask.Bus().post(new EventTitleClick());
    }

    @Override
    public void gotoAddLink() {
        FragLinkEdit.invoke(getActivity());
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    //----------- IInfoHomeTab end ---------------

}
