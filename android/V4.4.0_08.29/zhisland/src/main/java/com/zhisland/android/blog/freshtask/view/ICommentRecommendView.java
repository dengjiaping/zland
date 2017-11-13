package com.zhisland.android.blog.freshtask.view;

import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 神评精选
 */
public interface ICommentRecommendView extends IMvpView {

    /**
     * 更新数据
     */
    void setData(List<UserComment> task);

    /**
     * 更新下一步
     *
     * @param next
     */
    void updateNext(String next);

    /**
     * 更新 Holder
     */
    void updateHolder(int position);

    /**
     * 完成所有任务
     */
    void finish();

}
