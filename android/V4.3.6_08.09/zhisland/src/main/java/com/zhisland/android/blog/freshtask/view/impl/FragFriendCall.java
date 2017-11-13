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
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.freshtask.model.IFriendCallModel;
import com.zhisland.android.blog.freshtask.model.ModelFactory;
import com.zhisland.android.blog.freshtask.presenter.FriendCallPresenter;
import com.zhisland.android.blog.freshtask.view.IFriendCallView;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendCallHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendCallItemHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.FriendCallItemHolderListener;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 建立朋友圈 召唤好友
 */
public class FragFriendCall extends FragBaseMvp<FriendCallPresenter>
        implements IFriendCallView, FriendCallItemHolderListener {

    private FriendCallHolder viewHolder = new FriendCallHolder();
    private FriendAdapter adapter;
    private AProgressDialog progressDialog;

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragFriendCall.class;
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
        View view = inflater.inflate(R.layout.frag_call_friend, container, false);
        ButterKnife.inject(viewHolder, view);
        ButterKnife.inject(this, view);
        adapter = new FriendAdapter();
        viewHolder.lvData.setAdapter(adapter);
        initBottomHeight();
        return view;
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
    protected String getPageName() {
        return "RookieTaskInviteFriend";
    }

    @Override
    public void setData(List<User> task) {
        adapter.setData(task);
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
    public void refreshHolder(FriendCallItemHolder holder) {
        holder.refresh();
    }

    @Override
    public void showShortToast(String toast) {
        ToastUtil.showShort(toast);
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public Context getAppContext() {
        return getActivity();
    }

    //================UI event start================

    @OnClick(R.id.tvBottomBtn)
    public void onClickBottomBtn() {
        //召唤所有好友
        presenter().onClickBottomBtn();
    }

    @OnClick(R.id.tvJump)
    public void onClickTvJump() {
        presenter().onClickTvJump();
    }

    @Override
    public void onClickCallFriend(FriendCallItemHolder holder) {
        presenter().onClickCallFriend(holder.item);
    }

    @Override
    protected FriendCallPresenter createPresenter() {
        FriendCallPresenter presenter = new FriendCallPresenter();
        IFriendCallModel model = ModelFactory.CreateFriendCallModel();
        presenter.setModel(model);
        return presenter;
    }
    //===============UI event end=============

    //==========viewpager adapter============
    class FriendAdapter extends BaseListAdapter<User> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FriendCallItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_call_friend, parent, false);
                holder = new FriendCallItemHolder();
                convertView.setTag(holder);
            } else {
                holder = (FriendCallItemHolder) convertView.getTag();
            }
            holder.setListener(FragFriendCall.this);
            ButterKnife.inject(holder, convertView);
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

}
