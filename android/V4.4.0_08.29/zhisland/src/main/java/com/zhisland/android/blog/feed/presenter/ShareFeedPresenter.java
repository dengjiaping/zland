package com.zhisland.android.blog.feed.presenter;

import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.feed.model.IShareFeedModel;
import com.zhisland.android.blog.feed.view.IShareFeedView;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.StringUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * 活动和资讯分享附件到商圈的主导器
 * <p/>
 * Created by arthur on 2016/9/6.
 */
public class ShareFeedPresenter extends BasePresenter<IShareFeedModel, IShareFeedView> {

    private static final String TAG_QUIT = "quit";
    private Feed feed;//活动或者资讯的分享feed
    private long attachId;

    //设置要分享的附件
    public void setData(Feed feed, long attachId) {
        this.feed = feed;
        this.attachId = attachId;
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (feed != null) {
            view().updateView(feed);
        }
    }

    //附件被点击
    public void onAttachClicked() {
        Attach attach = (Attach) feed.attach;
        view().gotoUri(attach.uri);
    }

    public void onQuitClicked() {
        view().showConfirmDlg(TAG_QUIT, "确定退出分享么？", "确定", "取消", null);
    }

    //确认编辑
    public void onQuitOkClicked() {
        view().finishSelf();
    }

    //取消--取消编辑
    public void onQuitNoClicked() {
        view().hideConfirmDlg(TAG_QUIT);
    }

    //发布按钮点击
    public void onPublishClicked() {
        String content = view().getText();
        int length = StringUtil.getLength2(content);
        if (length > 500 * 2) {
            //TODO 判断字数不能超
            view().showToast("字数超过限制，最多输入500字");
            return;
        }

        feed.title = content;
        Observable<Feed> call = null;
        if (feed.type == FeedType.EVENT) {
            call = model().shareEventFeed(attachId, content);

        } else if (feed.type == FeedType.INFO) {
            call = model().shareInfoFeed(attachId, content);
        }

        if (call != null) {
            call.observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Feed>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Feed>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            //TODO 需要展示服务端返回的信息
                            view().showToast("发布失败");
                        }

                        @Override
                        public void onNext(Feed feed) {
                            feed.action = EbAction.ADD;
                            RxBus.getDefault().post(feed);
                            view().finishSelf();
                        }
                    });
        }
    }
}
