package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.AttachImg;
import com.zhisland.android.blog.feed.bean.AttachPraise;
import com.zhisland.android.blog.feed.bean.AttachResource;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.feed.model.IFeedModel;
import com.zhisland.android.blog.feed.view.IFeedView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;

import rx.Subscriber;

/**
 * 这个类应该是一个无状态的类，与外界的交互式调用然后发送通知的方式
 * Created by arthur on 2016/8/31.
 */
public class FeedPresenter<M extends IFeedModel, V extends IFeedView> extends BasePresenter<M, V> {

    private static final String TAG_CANCEL_TRANSMIT = "cancelTransmit";

    //region 通用事件处理

    //对feed点赞
    public void onPraiseClicked(final Feed feed) {
        if (feed == null || feed.like == null)
            return;

        final boolean praiseFeed = feed.like.clickState < 1;//是否是想要点赞

        //本地更新
        if (praiseFeed) {
            //点赞
            feed.like.clickState = 1;
            feed.like.quantity++;
        } else {
            //取消赞
            feed.like.clickState = 0;
            feed.like.quantity--;
        }
        view().updateFeed(feed);

        if (praiseFeed) {
            model().praiseFeed(feed.feedId)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            feed.like.operable = 0;
                            view().showToast("点赞失败");
                            view().updateFeed(feed);
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            view().showToast("点赞成功");
                            notifyFeedChange(feed, EbAction.UPDATE);
                        }
                    });
        } else {
            model().cancelPraiseFeed(feed.feedId)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            feed.like.operable = 1;
                            view().showToast("取消赞失败");
                            view().updateFeed(feed);
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            view().showToast("取消赞成功");
                            notifyFeedChange(feed, EbAction.UPDATE);
                        }
                    });
        }

        //请求网络接口
    }

    //转播feed
    public void onRetransmit(final Feed feed) {

        if (feed == null || feed.forward == null)
            return;

        final boolean transmitFeed = feed.forward.clickState < 1;//是否诗想要转播

        //本地更新
        if (transmitFeed) {
            //点赞
            feed.forward.clickState = 1;
            feed.forward.quantity++;
        }
        view().updateFeed(feed);

        if (transmitFeed) {
            //请求网络接口
            model().transmitFeed(feed.feedId)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Feed>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            feed.forward.operable = 0;
                            view().showToast("转播失败");
                            view().updateFeed(feed);
                        }

                        @Override
                        public void onNext(Feed feed) {
                            view().showToast("转播成功");
                            notifyFeedChange(feed, EbAction.UPDATE);
                        }
                    });
        } else {
            view().showConfirmDlg(TAG_CANCEL_TRANSMIT, "确定要取消转播？", "确定", "取消", feed);

        }


    }

    //附件被点击
    public void onAttachmentClicked(Feed feed, Object arg) {
        if (feed == null || feed.attach == null) {
            return;
        }

        if (feed.type == FeedType.COMMENT) {
            view().gotoFeedDetail(feed, false);
            return;
        }

        Object obj = feed.attach;
        if (obj instanceof Attach) {
            view().gotoUri(((Attach) obj).uri);
        } else if (obj instanceof AttachImg) {
            int index = 0;
            if (arg instanceof Integer) {
                index = (int) arg;
            }

            view().browseImages(new FeedImageAdapter(((AttachImg) obj).pictures), index);
        } else if (obj instanceof AttachResource) {
            view().gotoUri(((AttachResource) obj).uri);
        } else if (obj instanceof AttachPraise) {
            view().gotoUri(((AttachPraise) obj).uri);
        }
    }

    //用户区域被点击
    public void onUserClicked(User user) {
        view().gotoUserDetail(user);
    }

    //评论点击
    public void onCommentClicked(Feed curFeed) {
        boolean showComment = curFeed.comment.quantity < 1;
        view().gotoFeedDetail(curFeed, showComment);
    }

    //feed被点击
    public void onFeedClicked(Feed curFeed) {
        view().gotoFeedDetail(curFeed, false);
    }

    //endregion

    private void notifyFeedChange(Feed feed, EbAction action) {
        if (feed == null || action == null)
            return;

        feed.action = action;
        RxBus.getDefault().post(feed);

    }

    public void onDlgOkClicked(String tag, Object arg) {

        view().hideConfirmDlg(TAG_CANCEL_TRANSMIT);

        if (tag == null || !tag.equals(TAG_CANCEL_TRANSMIT) || !(arg instanceof Feed))
            return;

        final Feed feed = (Feed) arg;

        //取消赞
        feed.forward.clickState = 0;
        feed.forward.quantity--;
        view().updateFeed(feed);

        //请求网络接口
        model().cancelTransmitFeed(feed.feedId)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Feed>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        feed.forward.operable = 1;
                        view().showToast("取消转播失败");
                        view().updateFeed(feed);
                    }

                    @Override
                    public void onNext(Feed feed) {
                        view().showToast("取消转播成功");
                        notifyFeedChange(feed, EbAction.UPDATE);
                    }
                });

    }


    public void onDlgNoClicked(String tag, Object arg) {
        view().hideConfirmDlg(TAG_CANCEL_TRANSMIT);
    }
}
