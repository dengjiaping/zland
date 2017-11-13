package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.info.bean.CountCollect;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 资讯详情model
 * Created by Mr.Tui on 2016/6/29.
 */
public class InfoDetailModel implements IMvpModel {

    private NewsApi api;

    public InfoDetailModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    public Observable<ZHInfo> getInfoDetail(final long newsId) {
        return Observable.create(new AppCall<ZHInfo>() {
            @Override
            protected Response<ZHInfo> doRemoteCall() throws Exception {
                Call<ZHInfo> call = api.getInfoDetail(newsId);
                return call.execute();
            }
        });
    }

    public void cacheInfo(ZHInfo info) {
        DBMgr.getMgr().getInfoCacheDao().cachInfoDetail(info);
    }

    public ZHInfo getCacheInfo(long newsId) {
        return DBMgr.getMgr().getInfoCacheDao().getInfoById(newsId);
    }

    /**
     * 获取评论列表
     * */
    public Observable<ZHPageData<Comment>> getCommentList(final long newsId, final String nextId) {
        return Observable.create(new AppCall<ZHPageData<Comment>>() {
            @Override
            protected Response<ZHPageData<Comment>> doRemoteCall() throws Exception {
                Call<ZHPageData<Comment>> call = api.getCommentList(newsId, nextId, 10);
                return call.execute();
            }
        });
    }

    /**
     * 资讯 顶操作
     */
    public Observable<CountCollect> infoUp(final long newsId) {
        return Observable.create(new AppCall<CountCollect>() {
            @Override
            protected Response<CountCollect> doRemoteCall() throws Exception {
                Call<CountCollect> call = api.infoUp(newsId);
                return call.execute();
            }
        });
    }

    /**
     * 资讯 踩操作
     */
    public Observable<CountCollect> infoDown(final long newsId) {
        return Observable.create(new AppCall<CountCollect>() {
            @Override
            protected Response<CountCollect> doRemoteCall() throws Exception {
                Call<CountCollect> call = api.infoDown(newsId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论
     */
    public Observable<Comment> comment(final String newsId, final String content) {
        return Observable.create(new AppCall<Comment>() {
            @Override
            protected Response<Comment> doRemoteCall() throws Exception {
                Call<Comment> call = api.comment(newsId, content);
                return call.execute();
            }
        });
    }

    /**
     * 删除资讯评论
     */
    public Observable<Void> deleteComment(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.deleteComment(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论点赞
     */
    public Observable<Void> commentLike(final long commentId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.commentLike(commentId);
                return call.execute();
            }
        });
    }

    /**
     * 对资讯评论或资讯评论的回复，进行回复
     *
     * @param replyId 如果是对评论回复用户的回复，此参数为该条回复的id，如果是对评论的回复，此参数为null
     */
    public Observable<Reply> commentReply(final long viewpointId, final Long replyId, final String content) {
        return Observable.create(new AppCall<Reply>() {
            @Override
            protected Response<Reply> doRemoteCall() throws Exception {
                Call<Reply> call;
                if (replyId == null) {
                    call = api.commentReply(viewpointId, content);
                } else {
                    call = api.commentReply(viewpointId, replyId, content);
                }
                return call.execute();
            }
        });
    }

    /**
     * 删除资讯评论回复
     */
    public Observable<Void> deleteReply(final long replyId) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.deleteReply(replyId);
                return call.execute();
            }
        });
    }


}
