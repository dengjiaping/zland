package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 建立好友圈 召唤好友，item holder
 */
public class FriendCallItemHolder {

    @InjectView(R.id.ivUserAvatar)
    public AvatarView ivUserAvatar;

    @InjectView(R.id.tvCall)
    public TextView tvCall;

    @InjectView(R.id.tvUserName)
    public TextView tvUserName;

    @InjectView(R.id.tvUserMobile)
    public TextView tvUserMobile;

    public User item;
    private FriendCallItemHolderListener listener;

    public void fill(User item) {
        this.item = item;
        ivUserAvatar.fill(item, true);
        tvUserName.setText(item.name);
        tvUserMobile.setText(item.userMobile);
        if (item.sendApplyFriendRequest != null && item.sendApplyFriendRequest) {
            tvCall.setText("已召唤");
            tvCall.setEnabled(false);
        } else {
            tvCall.setText("召唤");
            tvCall.setEnabled(true);
        }
    }

    /**
     * 更新UI
     */
    public void refresh() {
        if (item != null) {
            fill(item);
        }
    }

    @OnClick(R.id.tvCall)
    public void onClickCallFriend() {
        if (listener != null) {
            listener.onClickCallFriend(this);
        }
    }


    public void setListener(FriendCallItemHolderListener listener) {
        this.listener = listener;
    }
}
