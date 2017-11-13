package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IAddLink extends IMvpView {

    /**
     * 显示加载对话框
     */
    public void showProgressDlg();

    /**
     * 隐藏加载对话框
     */
    public void hideProgressDlg();

    /**
     * 加载对话框是否正在显示
     */
    public boolean isProgressShowing();

    /**
     * 显示错误提示
     */
    public void showErrorPrompt(String text);

    /**
     * 隐藏错误提示
     */
    public void hidePrompt();

    /**
     * 跳转到推荐原因页面
     */
    public void gotoRecommendReason(RecommendInfo recommendInfo);

    /**
     * 获取剪切板文字内容
     */
    public String getClipText();

    /**
     * 将str设置到推荐链接编辑框内
     */
    public void setEditLinkText(String str);

    /**
     * 获取推荐链接编辑框内容
     */
    public String getLinkEditText();

    /**
     * 设置提交按钮是否可用
     */
    public void setCommitBtnEnabled(boolean enabled);

}
