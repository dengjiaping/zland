package com.zhisland.android.blog.message.view.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.message.model.IFansMessageModel;
import com.zhisland.android.blog.message.model.impl.FansMessageModel;
import com.zhisland.android.blog.message.presenter.FansMessagePresenter;
import com.zhisland.android.blog.message.view.IFansMessageView;
import com.zhisland.android.blog.profilemvp.model.IMyFansModel;
import com.zhisland.android.blog.profilemvp.view.impl.adapter.InviteUserAdapter;
import com.zhisland.android.blog.profilemvp.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.view.FragPullListMvps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arthur on 2016/9/13.
 */
public class FragFansMessageList extends FragPullListMvps<InviteUser> implements IFansMessageView, InviteUserHolder.OnRightClickListener {

    private FansMessagePresenter fansMessagePresenter;

    /**
     * 调起新增粉丝列表页
     *
     * @param context
     */
    public static void Invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragFansMessageList.class;
        param.enableBack = true;
        param.title = "新增粉丝";
        CommonFragActivity.invoke(context, param);
    }

    //region 生命周期
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().setAdapter(new InviteUserAdapter(getActivity(), this));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setBackgroundColor(Color.WHITE);
    }

    //endregion

    //region 重载的方法
    @Override
    protected Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenterMap = new HashMap<>(1);

        fansMessagePresenter = new FansMessagePresenter();
        IFansMessageModel model = new FansMessageModel();
        fansMessagePresenter.setModel(model);

        presenterMap.put(FansMessagePresenter.class.getName(), fansMessagePresenter);

        return presenterMap;
    }

    @Override
    public void loadNormal() {
        fansMessagePresenter.onLoadNormal();
    }

    @Override
    public void loadMore(String nextId) {
        fansMessagePresenter.onLoadMore(nextId);
    }

    @Override
    public void onRightClick(int position, InviteUser inviteUser) {
        fansMessagePresenter.onRightClicked(inviteUser);
    }

    @Override
    public void updateItem(InviteUser inviteUser) {
        getPullProxy().getAdapter().notifyDataSetChanged();
    }
    //endregion
}