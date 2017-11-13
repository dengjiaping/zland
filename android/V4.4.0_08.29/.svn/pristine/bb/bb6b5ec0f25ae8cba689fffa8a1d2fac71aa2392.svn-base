package com.zhisland.android.blog.find.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.webview.WVWrapper;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.tabhome.View.TitleFreshTaskView;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.file.FileMgr;

/**
 * Created by Mr.Tui on 2016/9/18.
 */
public class FragFindTabNew extends FragBase {

    private static final int TAG_LEFT_BTN = 111;
    private static final int TAG_SEARCH_BTN = 185;

    private TitleBarProxy titleBar;
    private TitleFreshTaskView titleFreshTaskView;

    private WVWrapper wvWrapper;
    public WebView webView;

    @Override
    public String getPageName() {
        return "DiscoveryHome";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout root = (LinearLayout) inflater.inflate(
                R.layout.frag_tab_item, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);

        webView = new WebView(getActivity());
        root.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        titleBar = new TitleBarProxy();
        titleBar.configTitle(root, TitleType.TITLE_LAYOUT, new DefaultTitleBarClickListener(getActivity()) {
            @Override
            public void onTitleClicked(View view, int tagId) {
                switch (tagId) {
                    case TAG_LEFT_BTN:
                        //跳转任务列表
                        BusFreshTask.Bus().post(new EventTitleClick());
                        break;
                    case TAG_SEARCH_BTN:
                        ActSearch.invoke(getActivity(), ActSearch.TYPE_ALL);
                        break;
                }
            }
        });
        titleBar.setTitle("发现");
        ImageView searchBtn = TitleCreator.Instance().createImageButton(getActivity(), R.drawable.sel_btn_search);
        titleBar.addRightTitleButton(searchBtn, TAG_SEARCH_BTN);
        titleFreshTaskView = new TitleFreshTaskView(getActivity());
        titleFreshTaskView.addLeftTitle(titleBar, TAG_LEFT_BTN);

        initWebView();
        wvWrapper.loadUrl(Config.getFindUrl(), Config.getOrgDomain(), "uid=" + PrefUtil.Instance().getUserId());
        return root;
    }

    private void initWebView() {
        wvWrapper = new WVWrapper(webView, false, true);
        //开启WebView的本地缓存功能，存入APPCACHE
        webView.getSettings().setAppCacheEnabled(true);
        //设置缓存路径，5.0以后会自动管理
        webView.getSettings().setAppCachePath(FileMgr.Instance().getDir(FileMgr.DirType.HTML).getAbsolutePath());

        webView.setFocusable(false);
        wvWrapper.setWebView(webView);
        wvWrapper.setWebListener(new WVWrapper.WebListener() {

            @Override
            public void showShare() {

            }

            @Override
            public void onPageStart() {

            }

            @Override
            public void onPageFinish() {
            }

            @Override
            public void onReceivedTitle(String title) {

            }

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            }

            @Override
            public void onHideCustomView() {

            }

            @Override
            public void loadError() {

            }
        });
    }

    /**
     * 刷新新手任务title
     */
    public void refreshTitleFreshTask(boolean showFreshTask, boolean showRedDot) {
        if (titleFreshTaskView != null) {
            titleFreshTaskView.refreshTitleFreshTask(showFreshTask, showRedDot);
        }
    }

    /**
     * 友盟上报 PageStart
     */
    public void pageStart() {
        ZhislandApplication.trackerPageStart(getPageName());
    }

    /**
     * 友盟上报 PageEnd
     */
    public void pageEnd() {
        ZhislandApplication.trackerPageEnd(getPageName());
    }

    @Override
    public boolean onBackPressed() {
        if(wvWrapper.canGoBack()){
            wvWrapper.goBack();
            return true;
        }
        return super.onBackPressed();
    }
}
