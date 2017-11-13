package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.ContactNoPowerModel;
import com.zhisland.android.blog.freshtask.view.IContactNoPower;
import com.zhisland.lib.mvp.presenter.BasePresenter;

/**
 * 建立好友圈,通讯录没权限
 */
public class ContactNoPowerPresenter extends BasePresenter<ContactNoPowerModel, IContactNoPower> {

    @Override
    protected void updateView() {
        super.updateView();
    }

    /**
     * 不添加好友
     */
    public void notAddFriend() {
        view().finish();
        BusFreshTask.Bus().post(new EventFreshTask(EventFreshTask.TYPE_SWITCH_TO_NEXT_CARD, TaskStatus.NORMAL));
    }

}
