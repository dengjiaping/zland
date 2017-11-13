package com.zhisland.lib.mvp.view;

import java.io.Serializable;

/**
 * 所有view试图的基类
 * Created by 向飞 on 2016/6/2.
 */
public interface IMvpView {

    /**
     * 显示loading dialog
     */
    void showProgressDlg();

    /**
     * 显示loading dialog
     */
    void showProgressDlg(String content);

    /**
     * 隐藏loading dialog
     */
    void hideProgressDlg();

    /**
     * 显示toast
     */
    void showToast(String toast);

    /**
     * 跳转到指定的URI
     */
    void gotoUri(String uri);

    /**
     * 展示确认对话框
     *
     * @param tag
     * @param title
     * @param okText
     * @param noText
     * @param arg    可以为空
     */
    void showConfirmDlg(String tag, String title, String okText, String noText, Object arg);

    /**
     * 隐藏确认对话框
     */
    void hideConfirmDlg(String tag);

    /**
     * 关闭自己
     */
    void finishSelf();

    //获取页面名称
    String getPageName();
}
