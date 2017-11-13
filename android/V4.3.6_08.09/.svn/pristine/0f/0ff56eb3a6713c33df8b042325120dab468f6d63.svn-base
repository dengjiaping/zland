package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.IFriendCallModel;
import com.zhisland.android.blog.freshtask.view.IFriendCallView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 建立好友圈 召唤好友 prsenter
 */
public class FriendCallPresenter extends BasePresenter<IFriendCallModel, IFriendCallView> {

    private List<User> datas;

    /**
     * 设置数据
     */
    public void setData(List<User> datas) {
        this.datas = datas;
        if (setupDone()) {
            view().setData(this.datas);
        }
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (this.datas == null) {
            getCallFriendList();
        } else {
            view().setData(this.datas);
        }
    }

    /**
     * 获取可召唤好友列表
     */
    public void getCallFriendList() {
        view().showProgress();
        model().getCallFriendList().observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<User>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgress();
                        view().showShortToast("获取数据失败");
                    }

                    @Override
                    public void onNext(ZHPageData<User> content) {
                        view().hideProgress();
                        if (content != null) {
                            FriendCallPresenter.this.setData(content.data);
                        }
                    }
                });
    }


    /**
     * 召唤好友
     */
    public void onClickCallFriend(final User item) {
        ArrayList<User> users = new ArrayList<>();
        users.add(item);
        view().showProgress();

        model().callFriend(view().getAppContext(), users, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {
                view().hideProgress();
                for (User user : datas) {
                    if (user.userMobile == item.userMobile) {
                        //使用User中 sendApplyFriendRequest 字段来判断是否  是召唤过的好友
                        user.sendApplyFriendRequest = true;
                    }
                }
                view().notifyDataChange();
                view().setJumpText("完成");
                PrefUtil.Instance().setIsCallFriend(PrefUtil.Instance().getUserId());
                view().showShortToast("您的召唤已发出");
                checkContactTaskIsFinished();
            }

            @Override
            public void onFailure(Throwable error) {
                view().hideProgress();
            }
        });
    }

    /**
     * 召唤所有好友
     */
    public void onClickBottomBtn() {
        if (datas != null && datas.size() > 0) {
            List<User> tmpUsers = new ArrayList<>();
            for (User user : datas) {
                if (user.sendApplyFriendRequest == null || !user.sendApplyFriendRequest) {
                    tmpUsers.add(user);
                }
            }
            if (tmpUsers.size() == 0) {
                view().showShortToast("您已没有可召唤的好友");
                return;
            }
            view().showProgress();
            model().callFriend(view().getAppContext(), tmpUsers, new TaskCallback<Object>() {
                @Override
                public void onSuccess(Object content) {
                    view().hideProgress();
                    for (User user : datas) {
                        //使用User中 sendApplyFriendRequest 字段来判断是否是召唤过的好友
                        user.sendApplyFriendRequest = true;
                    }
                    view().notifyDataChange();
                    view().setJumpText("完成");
                    PrefUtil.Instance().setIsCallFriend(PrefUtil.Instance().getUserId());
                    onClickTvJump();
                }

                @Override
                public void onFailure(Throwable error) {
                    view().hideProgress();
                }
            });
        } else {
            onClickTvJump();
        }
    }

    /**
     * 跳过/完成，关闭上一个界面
     */
    public void onClickTvJump() {
        checkContactTaskIsFinished();
        BusFreshTask.Bus().post(new EventFreshTask(EventFreshTask.TYPE_CLOSE_FRAG_ADD_FRIEND, TaskStatus.NORMAL));
        view().finish();
    }

    /**
     * 检查是否通讯录任务是否为完成状态
     */
    private void checkContactTaskIsFinished() {
        if (PrefUtil.Instance().getIsAddFriend(PrefUtil.Instance().getUserId())
                && PrefUtil.Instance().getIsCallFriend(PrefUtil.Instance().getUserId())) {
            BusFreshTask.Bus().post(new EventFreshTask(TaskType.CONTACT, TaskStatus.FINISHED));
        }
    }
}
