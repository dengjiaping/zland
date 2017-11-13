package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.freshtask.bean.TaskCommentDetail;
import com.zhisland.android.blog.freshtask.model.impl.FriendCommentModel;
import com.zhisland.android.blog.freshtask.view.IFriendComment;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import rx.Subscriber;

/**
 * 好友神评论
 */
public class FriendCommentPresenter extends BasePresenter<FriendCommentModel, IFriendComment> {

    private TaskCommentDetail content;

    @Override
    protected void updateView() {
        super.updateView();
    }

    public void getFriendCommentData() {
        model().getFriendCommentData().observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<TaskCommentDetail>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<TaskCommentDetail>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TaskCommentDetail content) {
                        if (content != null) {
                            FriendCommentPresenter.this.content = content;
                            view().setFinishCount(content.finishedCount);
                            view().setTotalCount(content.taskCount);
                            if (content.state != null) {
                                view().setBtnText(content.state.getStateName());
                                view().setBtnEnable(content.state.isOperable());
                            }
                            // 当完成数大于0时，显示去评论列表的textview。否则隐藏
                            if (content.finishedCount != null && content.finishedCount > 0) {
                                view().showGoToCommentList();
                            } else {
                                view().hiGoToCommentList();
                            }
                        }
                    }
                });
    }

    public TaskCommentDetail getData() {
        return content;
    }

}
