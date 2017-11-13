package com.zhisland.android.blog.freshtask.view;

import android.content.Context;

import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public interface IAddResourceView extends IMvpView {

    public void initViews();

    /**
     * 获取当前输入的资源详情
     */
    public String getContent();

    /**
     * 设置行业名称到View
     */
    public void setIndustryTag(String tagId, String tagName);

    /**
     * 设置类别名称到View
     */
    public void setCategoryTag(String tagId, String tagName);

    /**
     * 去行业选择页面
     */
    public void goToSelectIndustry(ZHDicItem selectedItem);

    /**
     * 去类别选择页面
     */
    public void goToSelectCategory(ZHDicItem selectedItem);

    /**
     * 显示加载对话框
     */
    public void showProgressDialog(String content);

    /**
     * 隐藏加载对话框
     */
    public void hideProgressDialog();

    /**
     * 关闭acitivty
     */
    public void finishSelf();

    /**
     * 获取 Context 对象
     */
    public Context getViewContext();
}
