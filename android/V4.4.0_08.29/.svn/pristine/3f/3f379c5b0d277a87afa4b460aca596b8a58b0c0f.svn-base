package com.zhisland.android.blog.profilemvp.view.impl.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/9/13.
 */
public class InviteUserHolder {

    InviteUser relationUser;

    Context context;

    int position;

    private OnRightClickListener onRightClickListener;

    @InjectView(R.id.ivAvatar)
    AvatarView avatarView;

    @InjectView(R.id.ivUserType)
    ImageView ivUserType;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    @InjectView(R.id.tvRight)
    TextView tvRight;

    View item;

    public InviteUserHolder(View v, final Context context, OnRightClickListener listener) {
        this.context = context;
        item = v;
        this.onRightClickListener = listener;
        ButterKnife.inject(this, v);
    }

    public void fill(InviteUser relationUser, int position) {
        if (relationUser == null) {
            return;
        }
        this.relationUser = relationUser;
        this.position = position;
        avatarView.fill(relationUser.user, false);
        ivUserType.setImageResource(relationUser.user.getVipIconId());
        tvName.setText(relationUser.user.name);
        tvComAndPos.setText((relationUser.user.userCompany == null ? ""
                : relationUser.user.userCompany)
                + " "
                + (relationUser.user.userPosition == null ? "" : relationUser.user.userPosition));

        tvComAndPos.setVisibility(View.VISIBLE);
        item.setOnClickListener(itemClickListener);
        if (relationUser.state != null) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(relationUser.state.getStateName());
            tvRight.setEnabled(relationUser.state.isOperable());
            if (relationUser.state.getState() == InviteUser.CustomState_State_FANGKE) {
                tvRight.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            } else {
                tvRight.setBackgroundResource(R.drawable.sel_btn_bwhite_sdc_clarge);
            }
        } else {
            tvRight.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tvRight)
    void onRightClick() {
        if (onRightClickListener != null) {
            onRightClickListener.onRightClick(position, relationUser);
        }
    }

    View.OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (relationUser == null) {
                return;
            }
            ActProfileDetail.invoke(context, relationUser.user.uid);
        }
    };

    public interface OnRightClickListener {
        void onRightClick(int position, InviteUser inviteUser);
    }

}