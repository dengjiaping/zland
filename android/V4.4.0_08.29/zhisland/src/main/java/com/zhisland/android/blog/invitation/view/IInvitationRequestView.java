package com.zhisland.android.blog.invitation.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * Created by arthur on 2016/8/10.
 */
public interface IInvitationRequestView extends IMvpView {


    /**
     * 设置数字
     *
     * @param totalCount 总额
     * @param leftCount  剩余的名额
     */
    void setCount(int totalCount, int leftCount);

    /**
     * 隐藏横向的请求
     */
    void hideRequest();

    /**
     * 替换请求数据并展示
     *
     * @param requestUsers
     */
    void setRequestData(List<InviteUser> requestUsers);

    /**
     * 导航到已处理页面
     */
    void gotoDealedList();

    /**
     * 导航到全部请求劣币奥
     *
     * @param users
     */
    void gotoRequestList(List<InviteUser> users);

    /**
     * 批准海客
     *
     * @param user
     */
    void gotoAllowHaike(InviteUser user);


    /**
     * 移除某个请求
     *
     * @param user
     */
    boolean removeRequest(InviteUser user);

    //设置请求的标题
    void setRequestTitle(String format);

    //展示toast
    void showToast(String content);

    //第一次进入显示的对话框
    void showFirstDlg();

    //关闭页面
    void finishSelf();

    //跳转到个人页
    void gotoActProfile(long uid);
}
