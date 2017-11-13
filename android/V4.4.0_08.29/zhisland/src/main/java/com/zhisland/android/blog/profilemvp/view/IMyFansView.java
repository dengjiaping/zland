package com.zhisland.android.blog.profilemvp.view;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.IMvpListView;

/**
 * 我的粉丝view
 * Created by Mr.Tui on 2016/9/6.
 */
public interface IMyFansView extends IMvpListView<InviteUser> {

    void onLoadSucessfully(ZHPageData<InviteUser> dataList, boolean showSeeVisitors);

    //刷新列表
    void refreshList();

    void pullDownToRefresh(boolean show);
}
