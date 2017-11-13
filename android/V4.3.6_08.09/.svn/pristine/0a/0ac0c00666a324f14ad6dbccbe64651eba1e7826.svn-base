package com.zhisland.android.blog.info.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.webview.WVWrapper;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.file.FileMgr;

/**
 * 推荐成功后的预览页面
 * Created by Mr.Tui on 2016/7/11.
 */
public class FragPreview extends FragBase {

    /**
     * 资讯缓存的资源文件和数据库的路径
     */
    public final static String CACHE_PATH = FileMgr.Instance().getDir(FileMgr.DirType.HTML).getAbsolutePath();

    /**
     * 资讯数据的最大缓存量为3M,就维持这个大小及可，5.0以前点击设置清理缓存的时候，都可以清理掉
     */
    private final static long MAX_CACHE_SIZE = 1024 * 1024 * 3;


    public final static String INTENT_KEY_URL = "intent_key_url";

    public static void invoke(Context context, String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragPreview.class;
        param.title = "资讯详情";
        param.enableBack = true;
        param.swipeBackEnable = false;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return "FragPreview";
    }

    WebView webView;

    WVWrapper wvWrapper;

    String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        webView = new WebView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(lp);
        initWebView();
        return webView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        url = getActivity().getIntent().getStringExtra(INTENT_KEY_URL);
        loadUrl(url);
    }

    private void initWebView() {
        wvWrapper = new WVWrapper(webView, false, true);
        //开启WebView的本地缓存功能，存入APPCACHE
        webView.getSettings().setAppCacheEnabled(true);
        //设置缓存路径，5.0以后会自动管理
        webView.getSettings().setAppCachePath(CACHE_PATH);
        //设置缓存的最大值，5.0以后会自动管理
        webView.getSettings().setAppCacheMaxSize(MAX_CACHE_SIZE);
        webView.setFocusable(false);
    }

    private void loadUrl(String url) {
        if (!StringUtil.isNullOrEmpty(url)) {
            wvWrapper.loadUrl(url);
        }
    }

    @Override
    public boolean onBackPressed() {
        HomeUtil.NavToHome(getActivity());
        return true;
    }
}