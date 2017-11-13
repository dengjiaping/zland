package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.contacts.dto.InviteUser;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 新手任务 求邀请 升级 item holder
 */
public class InviteRequestItemHolder {

    @InjectView(R.id.ivAvatar)
    AvatarView avatarView;
    @InjectView(R.id.tvName)
    TextView tvName;
    @InjectView(R.id.tvDesc)
    TextView tvDesc;
    @InjectView(R.id.btnRequestInvite)
    Button btnRequestInvite;

    public InviteUser inviteUser;

    private CustomState state;
    private InviteRequestItemHolderListener listener;
    private Context context;

    public InviteRequestItemHolder(Context context) {
        this.context = context;
    }

    public void fill(InviteUser inviteUser) {
        this.inviteUser = inviteUser;
        User user = inviteUser.user;
        this.state = inviteUser.state;
        avatarView.fill(user, true);
        tvName.setText(user.name);
        tvDesc.setText(user.userCompany + " " + user.userPosition);
        initButtonState();
    }

    private void initButtonState() {
        btnRequestInvite.setText(state.getStateName());
        boolean operable = state.isOperable();
        btnRequestInvite.setEnabled(operable);
        if (operable) {
            btnRequestInvite.setBackgroundResource(R.drawable.rect_bwhite_sdc_clarge);
            btnRequestInvite.setTextColor(context.getResources().getColor(R.color.color_dc));
        } else {
            btnRequestInvite.setBackgroundResource(0);
            btnRequestInvite.setTextColor(context.getResources().getColor(R.color.color_f3));
        }
    }

    /**
     * 更新UI
     */
    public void refresh() {
        if (inviteUser != null) {
            fill(inviteUser);
        }
    }

    @OnClick(R.id.rlInviteRequest)
    public void onClickRlInviteRequest() {
        if (listener != null) {
            listener.onClickRlInviteRequest(inviteUser.user.uid);
        }
    }

    @OnClick(R.id.btnRequestInvite)
    public void onClickRight() {
        if (listener != null) {
            listener.onClickInviteRequest(this);
        }
    }

    public void setListener(InviteRequestItemHolderListener listener) {
        this.listener = listener;
    }
}
