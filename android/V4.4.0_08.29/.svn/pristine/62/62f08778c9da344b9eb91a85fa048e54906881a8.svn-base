package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * 资讯详情页面的阅读功能view接口
 */
public interface IInfoReadView extends IMvpView {

    /**
     * 隐藏所有的view
     */
    void hideListView();

    /**
     * 隐藏推荐阅读模式对话框
     */
    void hideModeDlg();

    /**
     * 显示推荐阅读模式对话框
     */
    void showToReadDlg();


    /**
     * 加载阅读模式的内容
     */
    void loadContentRead(String content);

    /**
     * 加载原网页模式url
     */
    void loadContentUrl(String url);

    /**
     * 设置“阅读模式和原网页模式切换按钮”是否显示。
     */
    void setSwitchShowStatue(boolean show);

    /**
     * 设置“阅读模式和原网页模式切换按钮”为 阅读模式状态
     */
    void setSwitchRead();

    /**
     * 设置“阅读模式和原网页模式切换按钮”为 原网页模式状态
     */
    void setSwitchUrl();

    /**
     * 设置推荐人view
     */
    void setFromName(String name);

    /**
     * @param info
     */
    void shareData(ZHInfo info);

    /**
     * 资讯内容已经展示完成，开始处理socialview
     */
    void onReadConentloaded();

    /**
     * 跳转到举报页面
     *
     * @param info
     */
    void gotoReport(ZHInfo info);

    /**
     * 跳转到个人页
     */
    void gotoProfileAct(long uid);
}
