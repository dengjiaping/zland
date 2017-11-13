package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.Feed;

import java.util.ArrayList;

/**
 * feed详情中的评论
 * Created by arthur on 2016/9/6.
 */
public interface IFeedDetailView extends IFeedView {

    //显示feed已经删除的视图
    void showDeletedView();

    //关掉当前页
    void finishSelf();

    //feed数据远程加载成功
    void onLoadFeedOk(Feed data);

    //显示评论编辑view
    void showSendCommentView(String feedId);

    //设置评论数目
    void setCommentTagCount(Integer count);

    void showDeteleFeedAction();

    void showReportFeedAction();

    void showDeleteFeedDlg();

    void showReportFeedDlg(User user, ArrayList<Country> datas);

}
