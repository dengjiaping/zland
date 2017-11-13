package com.zhisland.android.blog.info.view;

import android.content.Context;

import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IInfoHomeTab extends IMvpView {

    String getClipText();

    void showPrompt(String url);

    void hidePrompt();

    void gotoFreshTask();

    void gotoAddLink();

    /**
     * 获取 Context 对象
     */
    public Context getViewContext();
}
