package com.zhisland.android.blog.invitation.view.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.eb.EBLogin;
import com.zhisland.android.blog.common.app.AnimUtils;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.eb.EBApproveUser;
import com.zhisland.android.blog.invitation.model.IReqApproveFriendsModel;
import com.zhisland.android.blog.invitation.model.impl.ReqApproveFriendsModel;
import com.zhisland.android.blog.invitation.presenter.ReqApproveFriendsPresenter;
import com.zhisland.android.blog.invitation.view.IReqApproveFriendsView;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteUserHolder;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.FragPullListMvp;
import com.zhisland.lib.util.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lipengju on 16/8/10.
 * 海客升级请求
 */
public class FragReqApproveFriends extends FragPullListMvp<InviteUser, ReqApproveFriendsPresenter> implements IReqApproveFriendsView {

    private static final String KEY_DATAS = "KEY_DATAS";
    static ReqApproveFriendsPresenter reqApproveFriendsPresenter;

    private List<InviteUser> mUsers;

    public static void invoke(Context context, List<InviteUser> users) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragReqApproveFriends.class;
        param.title = "海客升级请求";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATAS, (Serializable) users);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    ApprovedListAdapter approvedListAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        approvedListAdapter = new ApprovedListAdapter();
        getPullProxy().setAdapter(approvedListAdapter);
        getPullProxy().needRefreshOnResume = false;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        internalView.setPadding(DensityUtil.dip2px(16), 0, DensityUtil.dip2px(16), 0);
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.transparent));
        getPullProxy().getPullView().getRefreshableView().setDivider(drawable);
        getPullProxy().getPullView().getRefreshableView().setDividerHeight(DensityUtil.dip2px(16));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, root);
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    protected View createDefaultFragView() {
        View defaultView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_invitation_dealed_list, null);
        defaultView.findViewById(R.id.tvTitle).setVisibility(View.GONE);
        return defaultView;
    }


    @Override
    protected ReqApproveFriendsPresenter createPresenter() {

        mUsers = (List<InviteUser>) getActivity().getIntent().getSerializableExtra(KEY_DATAS);

        reqApproveFriendsPresenter = new ReqApproveFriendsPresenter();
        IReqApproveFriendsModel model = new ReqApproveFriendsModel();
        model.setData(mUsers);

        reqApproveFriendsPresenter.setModel(model);


        return reqApproveFriendsPresenter;
    }


    @Override
    public void gotoAllowHaike(InviteUser user) {
        FragApproveHaiKe.invoke(getActivity(), user);
    }

    @Override
    public void removeRequest(InviteUser user) {
        removeItem(user.user.uid);

    }

    public void removeItem(long uid) {
        Iterator<InviteUser> iterator = approvedListAdapter.getData().iterator();
        while (iterator.hasNext()) {
            InviteUser user1 = iterator.next();
            if (uid == user1.user.uid) {
                iterator.remove();
                break;
            }
        }
        approvedListAdapter.notifyDataSetChanged();
    }


    public void onEventMainThread(EBApproveUser user) {
        if (null == user) {
            return;
        }
        removeItem(user.userId);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadSucessfully(List<InviteUser> data) {
        approvedListAdapter.setData(data);
        approvedListAdapter.notifyDataSetChanged();
        getPullProxy().onRefreshFinished();

    }

    @Override
    public void onLoadSucessfully(ZHPageData<InviteUser> dataList) {

    }

    @Override
    public void onLoadFailed(Throwable failture) {

    }



    /**
     * 列表适配器
     */
    private class ApprovedListAdapter extends BaseListAdapter<InviteUser> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_approved_req_all, null);
                ListHolder holder = new ListHolder(convertView);
                convertView.setTag(holder);
            }

            ListHolder holder = (ListHolder) convertView.getTag();
            final InviteUser user = getItem(position);
            holder.fill(user, getActivity());
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    static class ListHolder {
        @InjectView(R.id.ivReqAvatar)
        AvatarView ivReqAvatar;
        @InjectView(R.id.tvUserName)
        TextView tvUserName;
        @InjectView(R.id.tvCompany)
        TextView tvCompany;
        @InjectView(R.id.tvDate)
        TextView tvDate;
        @InjectView(R.id.tvDes)
        TextView tvDes;
        @InjectView(R.id.tvApproved)
        TextView tvApproved;
        @InjectView(R.id.tvIgnore)
        TextView tvIgnore;
        @InjectView(R.id.tvApproveState)
        TextView tvApproveState;

        private InviteUser request;
        private View mConvertView;

        ListHolder(View view) {
            ButterKnife.inject(this, view);
            mConvertView = view;
        }

        void fill(final InviteUser user, final Context context) {

            this.request = user;

            ivReqAvatar.fill(user.user, false);
            tvCompany.setText(user.user.combineCompanyAndPosition());
            tvUserName.setText(user.user.name);
            tvDate.setText(user.requestTime);
            if (!TextUtils.isEmpty(user.requestExplanation)) {
                tvDes.setText(user.requestExplanation);
            } else {
                tvDes.setText("--");
            }
            tvApproveState.setVisibility(View.GONE);

            mConvertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActProfileDetail.invokeNoHistory(context, user.user.uid);
                }
            });
        }

        @OnClick(R.id.tvIgnore)
        public void onIgoneClicked() {
            reqApproveFriendsPresenter.onIgnoreClicked(request);
        }

        @OnClick(R.id.tvApproved)
        public void onApproveClicked() {
            reqApproveFriendsPresenter.onAllowClicked(request);
        }
    }

}
