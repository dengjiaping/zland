package com.zhisland.android.blog.freshtask.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.FriendAddModel;
import com.zhisland.android.blog.freshtask.presenter.FriendAddPresenter;
import com.zhisland.android.blog.freshtask.view.IFriendAddView;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendAddHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendAddItemHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendAddItemHolderListener;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 建立好友圈 加好友
 */
public class FragFriendAdd extends FragBaseMvp<FriendAddPresenter>
        implements IFriendAddView, FriendAddItemHolderListener {

    private FriendAddHolder viewHolder = new FriendAddHolder();
    private FriendAddAdapter adapter;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragFriendAdd.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "初步建立好友圈";
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_friend_add, container, false);
        ButterKnife.inject(viewHolder, view);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        adapter = new FriendAddAdapter();
        viewHolder.lvData.setAdapter(adapter);
        initBottomHeight();
        return view;
    }

    public void onEventMainThread(EventFreshTask eb) {
        switch (eb.taskType) {
            case EventFreshTask.TYPE_CLOSE_FRAG_ADD_FRIEND:
                finish();
                break;
        }
    }

    @Override
    protected FriendAddPresenter createPresenter() {
        FriendAddPresenter presenter = new FriendAddPresenter();
        FriendAddModel model = new FriendAddModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initBottomHeight() {
        int height = DensityUtil.getHeight();
        if (height > 0) {
            //屏幕高的 1/3，减去蒙层的高度 50dp
            int bottomHeight = height / 3 - DensityUtil.dip2px(50);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, bottomHeight);
            viewHolder.llBottom.setLayoutParams(params);
        }
    }

    @Override
    public String getPageName() {
        return "RookieTaskAddFriend";
    }

    @Override
    public void setData(List<User> task) {
        adapter.setData(task);
    }

    @Override
    public void setDesc(String desc) {
        viewHolder.tvDesc.setText(desc);
    }

    @Override
    public void setBtnText(String text) {
        viewHolder.tvBottomBtn.setText(text);
    }

    @Override
    public void setJumpText(String text) {
        viewHolder.tvJump.setText(text);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showContentView() {
        viewHolder.llEmptyView.setVisibility(View.GONE);
        viewHolder.lvData.setVisibility(View.VISIBLE);
        viewHolder.ivMask.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        viewHolder.llEmptyView.setVisibility(View.VISIBLE);
        viewHolder.lvData.setVisibility(View.GONE);
        viewHolder.ivMask.setVisibility(View.GONE);
    }

    @Override
    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void goToCallFriend() {
        // 跳转召唤好友
        FragFriendCall.invoke(getActivity());
    }

    @Override
    public void goToNoPower() {
        FragContactNoPower.invoke(getActivity());
    }

    @Override
    public void onClickAddFriend(FriendAddItemHolder holder) {
        presenter().onClickAddFriend(holder.item);
    }

    //================UI event start================

    @OnClick(R.id.tvBottomBtn)
    public void onClickBottomBtn() {
        presenter().onClickBottomBtn();
    }

    @OnClick(R.id.tvJump)
    public void onClickTvJump() {
        presenter().onClickTvJump();
    }
    //===============UI event end=============

    //==========viewpager adapter============
    class FriendAddAdapter extends BaseListAdapter<User> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FriendAddItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_friend_add, parent, false);
                holder = new FriendAddItemHolder();
                convertView.setTag(holder);
            } else {
                holder = (FriendAddItemHolder) convertView.getTag();
            }
            holder.setListener(FragFriendAdd.this);
            ButterKnife.inject(holder, convertView);
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

}
