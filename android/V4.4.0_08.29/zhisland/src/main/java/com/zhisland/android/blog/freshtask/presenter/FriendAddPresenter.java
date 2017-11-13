package com.zhisland.android.blog.freshtask.presenter;

import android.content.Context;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.FriendAddModel;
import com.zhisland.android.blog.freshtask.view.IFriendAddView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 建立好友圈 加好友 presenter
 */
public class FriendAddPresenter extends BasePresenter<FriendAddModel, IFriendAddView> {

    private List<User> datas;

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

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
        if (datas == null) {
            checkContactPower();
        } else {
            view().setData(this.datas);
        }
    }

    /**
     * 判断是否有通讯录权限，没有跳转无通讯录权限UI
     */
    public void checkContactPower() {
        if (PhoneContactUtil.hasContactData()) {
            getContactFriend();
        } else {
            view().goToNoPower();
            view().finish();
        }
    }

    /**
     * 获取通讯录中可加好友的人
     */
    public void getContactFriend() {
        view().showProgressDlg();
        final PhoneContactUtil.ContactResult<String> contacts = PhoneContactUtil.getChangeContact();
        model().uploadContact(context, contacts, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {
                if(view() != null){
                    view().hideProgressDlg();
                    PhoneContactUtil.setLastTimestamp(contacts.timestamp);
                }
            }

            @Override
            public void onFailure(Throwable error) {
                if (view() != null) {
                    view().hideProgressDlg();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                getContactFriendTask();
            }
        });
    }

    private void getContactFriendTask() {
        model().getContactFriend().
                observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<ZHPageData<User>>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<ZHPageData<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().showToast("获取数据失败");
                    }

                    @Override
                    public void onNext(ZHPageData<User> content) {
                        if (content != null && content.data != null) {
                            FriendAddPresenter.this.setData(content.data);
                            view().setDesc("你的通讯录有 " + content.count + " 位联系人已经登岛");
                            if (content.data.size() > 0) {
                                view().showContentView();
                                view().setBtnText("全部加为好友");
                                view().setJumpText("跳过");
                                checkHasSendFriendRequest();
                            } else {
                                view().showEmptyView();
                                view().setBtnText("扩大你的人脉圈");
                                view().setJumpText("完成");
                                //当用户返回数据为空时，为没有匹配到用户，算完成此任务
                                PrefUtil.Instance().setIsAddFriend(PrefUtil.Instance().getUserId());
                                checkContactTaskIsFinished();
                            }
                        }
                    }
                });
    }

    /**
     * 检测是否过去加过好友，如果有则底部按钮显示为“完成”
     */
    private void checkHasSendFriendRequest() {
        for (User user : datas) {
            if (user.sendApplyFriendRequest != null && user.sendApplyFriendRequest) {
                view().setJumpText("完成");
                PrefUtil.Instance().setIsAddFriend(PrefUtil.Instance().getUserId());
                break;
            }
        }
    }

    /**
     * 右边按钮，加好友，成功后刷新UI
     */
    public void onClickAddFriend(final User item) {
        if (item.sendApplyFriendRequest == null || !item.sendApplyFriendRequest) {
            ArrayList<User> users = new ArrayList<>();
            users.add(item);
            view().showProgressDlg();
            model().addFriend(users)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            view().hideProgressDlg();
                            view().showToast("加好友失败");
                        }

                        @Override
                        public void onNext(Void aLong) {
                            view().hideProgressDlg();
                            for (User user : datas) {
                                if (user.uid == item.uid) {
                                    user.sendApplyFriendRequest = true;
                                }
                            }
                            view().setJumpText("完成");
                            PrefUtil.Instance().setIsAddFriend(PrefUtil.Instance().getUserId());
                            view().notifyDataChange();
                            checkContactTaskIsFinished();
                        }
                    });
        }
    }

    /**
     * 全部加为好友/扩大你的人脉圈
     */
    public void onClickBottomBtn() {
        if (datas != null && datas.size() > 0) {
            List<User> tmpUsers = new ArrayList<>();
            for (User user : datas) {
                if (user.sendApplyFriendRequest == null || !user.sendApplyFriendRequest) {
                    tmpUsers.add(user);
                }
            }
            if (tmpUsers.size() > 0) {
                view().showProgressDlg();
                model().addFriend(tmpUsers)
                        .observeOn(getSchedulerObserver())
                        .subscribeOn(getSchedulerSubscribe())
                        .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                view().hideProgressDlg();
                                view().showToast("加好友失败");
                            }

                            @Override
                            public void onNext(Void aLong) {
                                view().hideProgressDlg();
                                for (User user : datas) {
                                    user.sendApplyFriendRequest = true;
                                }
                                view().setJumpText("完成");
                                PrefUtil.Instance().setIsAddFriend(PrefUtil.Instance().getUserId());
                                view().notifyDataChange();
                                onClickTvJump();
                            }
                        });
            } else {
                onClickTvJump();
            }
        } else {
            onClickTvJump();
        }
    }

    /**
     * 跳过/完成,跳转召唤好友
     */
    public void onClickTvJump() {
        checkContactTaskIsFinished();
        view().goToCallFriend();
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
