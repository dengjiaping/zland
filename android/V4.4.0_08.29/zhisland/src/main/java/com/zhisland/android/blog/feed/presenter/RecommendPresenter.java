package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.feed.bean.RecommendData;
import com.zhisland.android.blog.feed.model.IRecommendModel;
import com.zhisland.android.blog.feed.view.IRecommendView;
import com.zhisland.lib.mvp.presenter.BasePresenter;

/**
 * 推广位的主导器
 * Created by arthur on 2016/9/6.
 */
public class RecommendPresenter extends BasePresenter<IRecommendModel, IRecommendView> {

    private RecommendData data;

    public void setData(RecommendData data) {
        this.data = data;
        if (setupDone()) {
            updateView();
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (data != null) {
            view().updateRecommendView(data);
        }
    }

    //推广为数据被点击
    public void onRecommendClicked(RecommendData data) {
        view().gotoUri(data.uri);
    }
}
