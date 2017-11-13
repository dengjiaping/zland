package com.zhisland.android.blog.info.view;

import com.zhisland.lib.mvp.view.IMvpView;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IRecommendCommit extends IMvpView {

    /**
     * 跳转到资讯预览页面
     * */
    public void gotoInfoPreview(String url);

    /**
     * 获取view中原因
     * */
    public String getReason();

    /**
     * 设置资讯标题
     * */
    public void setInfoTitle(String title);

}
