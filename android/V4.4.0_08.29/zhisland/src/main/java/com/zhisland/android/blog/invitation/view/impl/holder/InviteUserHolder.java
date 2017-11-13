package com.zhisland.android.blog.invitation.view.impl.holder;

import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/8/10.
 */
public class InviteUserHolder {

    @InjectView(R.id.ivAvatar)
    AvatarView ivAvatar;

    @InjectView(R.id.tvTag)
    TextView tvTag;

    @InjectView(R.id.tvStatue)
    TextView tvStatue;

    @InjectView(R.id.tvApprove)
    TextView tvApprove;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    private View root;

    private InviteUser inviteUser;

    private CallBack callBack;

    public InviteUserHolder(View view) {
        this.root = view;
        ButterKnife.inject(this, view);
        root.setOnClickListener(rootClick);
    }

    public void fill(InviteUser inviteUser) {
        this.inviteUser = inviteUser;
        if (inviteUser != null) {
            if (inviteUser.user != null) {
                ivAvatar.fill(inviteUser.user, true);
                tvName.setText(inviteUser.user.name == null ? "" : inviteUser.user.name);
                tvComAndPos.setText(inviteUser.user.combineCompanyAndPosition());
            }
            if (inviteUser.state != null) {
                if (inviteUser.state.isOperable()) {
                    tvApprove.setVisibility(View.VISIBLE);
                    tvStatue.setVisibility(View.GONE);
                    tvApprove.setText(inviteUser.state.getStateName());
                } else {
                    tvStatue.setVisibility(View.VISIBLE);
                    tvApprove.setVisibility(View.GONE);
                    tvStatue.setText(inviteUser.state.getStateName());
                }
            }
            if(inviteUser.inviteRegister == InviteUser.INVITE_FRIEND_OF_CONTACT){
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText("通讯录好友");
            } else {
                tvTag.setVisibility(View.GONE);
            }
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void recycle() {
    }

    @OnClick(R.id.tvApprove)
    void onRightBntClick() {
        if (callBack != null) {
            callBack.onRightBtnClick(inviteUser);
        }
    }

    View.OnClickListener rootClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActProfileDetail.invoke(root.getContext(), inviteUser.user.uid);
        }
    };

    public interface CallBack {
        void onRightBtnClick(InviteUser inviteUser);
    }
}
